/**
 * $Id: ApplicationWizard.java,v 1.3 2009/02/27 13:41:54 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: ApplicationWizard.java,v $
 * Revision 1.3  2009/02/27 13:41:54  a125788
 * MVA : Moved Message class to upper level
 *
 * Revision 1.2  2008/07/03 13:32:06  a125788
 * MVA : Wizard is finishable only when last page is displayed at least once
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui.project;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.ApplicationWizardMessages;
import com.worldline.appprofiles.wizard.ui.IApplicationWizardPage;
import com.worldline.appprofiles.wizard.ui.model.AbstractConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ApplicationConfiguration;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.OptionalConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.pages.ApplicationChooseProfilePage;
import com.worldline.appprofiles.wizard.ui.project.pages.ApplicationConfigureProfilePage;
import com.worldline.appprofiles.wizard.ui.project.pages.ApplicationMavenInfoPage;
import com.worldline.appprofiles.wizard.ui.project.pages.ApplicationProjectBasePage;

/**
 * Project Wizard using Maven 2 Archetypes definition
 * 
 * @author mvanbesien
 * @version $Revision: 1.3 $
 * @since $Date: 2009/02/27 13:41:54 $
 * 
 */
public class ApplicationWizard extends Wizard implements INewWizard {

	/**
	 * Number of pages created by the wizard for the default case (no extra
	 * pages) .
	 */
	private static final int DEFAULT_PAGE_COUNT = 4;

	private ApplicationWizardOutput applicationWizardOutput;

	private Map<IWizardPage, Boolean> visitedPages = new HashMap<IWizardPage, Boolean>();

	private String applicationKind;

	private static final String XA_SITE = "http://xa.atosworldline.com/";

	public ApplicationWizard() {
		this(new ApplicationWizardOutput(), "standard");
	}

	public ApplicationWizard(ApplicationWizardOutput applicationWizardOutput) {
		this(applicationWizardOutput, "standard");
	}

	public ApplicationWizard(String applicationKind) {
		this(new ApplicationWizardOutput(), applicationKind);
	}

	public ApplicationWizardOutput getApplicationWizardOutput() {
		return applicationWizardOutput;
	}

	public String getApplicationKind() {
		return applicationKind;
	}

	public ApplicationWizard(ApplicationWizardOutput applicationWizardOutput, String applicationKind) {
		super();
		this.setWindowTitle(ApplicationWizardMessages.WINDOW_TITLE.value());
		this.applicationKind = applicationKind;
		this.applicationWizardOutput = applicationWizardOutput;
	}

	@Override
	public void addPages() {
		IWizardPage locationPage = new ApplicationProjectBasePage(applicationWizardOutput);
		IWizardPage archetypePage = new ApplicationChooseProfilePage(applicationKind, applicationWizardOutput);
		IWizardPage archetypeConfigPage = new ApplicationConfigureProfilePage(applicationWizardOutput);
		IWizardPage archetypeInfoPage = new ApplicationMavenInfoPage(applicationWizardOutput);

		this.addPage(locationPage);
		this.addPage(archetypePage);
		this.addPage(archetypeConfigPage);
		this.addPage(archetypeInfoPage);
	}

	@Override
	public void addPage(IWizardPage page) {
		super.addPage(page);
		this.visitedPages.put(page, false);
	}

	@Override
	public boolean performFinish() {
		new ApplicationCreationJob(applicationWizardOutput).schedule();

		if (this.applicationWizardOutput.isShowDocumentation()) {
			try {
				String url = applicationWizardOutput.getSelectedApplicationProfile().getDocumentationURL();
				if ("".equals(url))
					url = XA_SITE;
				PlatformUI.getWorkbench().getBrowserSupport().createBrowser("XA").openURL(new URL(url));
			} catch (Exception e) {
				ILog log = Activator.getDefault().getLog();
				log.log(new Status(IStatus.ERROR, Activator.PLUGIN_ID,
						ApplicationWizardMessages.EXCEPTION_OPENING_DOCUMENTATION.value(), e));
			}
		}

		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
	}

	@Override
	public IWizardPage getPreviousPage(IWizardPage page) {
		/*
		 * The eclipse wizard API does not allow removing pages from a wizard,
		 * so we must disallow going back to the archetype selection page, which
		 * might require a different set of pages.
		 */
		if (page instanceof IApplicationWizardPage) {
			/* check if it is the first of the added pages. */
			List<IWizardPage> pages = Arrays.asList(this.getPages());
			int index = pages.indexOf(page);
			if (ApplicationWizard.DEFAULT_PAGE_COUNT == index)
				return null;
		}
		return super.getPreviousPage(page);
	}

	/**
	 * Method overriden, so when selected profile doesn't have any
	 * configuration, it jumps to last page.
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page instanceof ApplicationChooseProfilePage) {
			ApplicationProfile applicationProfile = applicationWizardOutput.getSelectedApplicationProfile();
			if (applicationProfile == null)
				return super.getNextPage(page);

			for (ApplicationConfiguration configuration : applicationProfile.getConfigurations()) {
				for (AbstractConfigurationEntry entry : configuration.getConfigurationEntries()) {
					if (entry instanceof OptionalConfigurationEntry || entry instanceof ValueConfigurationEntry)
						return super.getNextPage(page);
				}
			}
			for (ApplicationModule applicationModule : applicationProfile.getModules()) {
				if (applicationModule.isMandatory()
						|| applicationWizardOutput.getSelectedModules().contains(applicationModule)) {
					for (ApplicationConfiguration configuration : applicationModule.getConfigurations()) {
						for (AbstractConfigurationEntry entry : configuration.getConfigurationEntries()) {
							if (entry instanceof OptionalConfigurationEntry || entry instanceof ValueConfigurationEntry)
								return super.getNextPage(page);
						}
					}
				}
			}

			// If we're here, it means that we don't have any configuration
			// editable. Hence, we skip the page.
			setPageAsVisited(super.getNextPage(page));
			return super.getNextPage(super.getNextPage(page));
		}
		return super.getNextPage(page);
	}

	public void setPageAsVisited(IWizardPage page) {
		if (this.visitedPages.containsKey(page))
			this.visitedPages.put(page, true);
	}

	@Override
	public boolean canFinish() {
		boolean areVisitsOK = true;
		Iterator<Boolean> visits = visitedPages.values().iterator();
		while (areVisitsOK && visits.hasNext()) {
			areVisitsOK = areVisitsOK & visits.next();
		}
		return areVisitsOK && super.canFinish();
	}

}
