package com.worldline.appprofiles.wizard.ui.project.pages;

import java.util.Iterator;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;

import com.worldline.appprofiles.wizard.ui.ApplicationWizardPageStatus;
import com.worldline.appprofiles.wizard.ui.FormDataBuilder;
import com.worldline.appprofiles.wizard.ui.ValidatingApplicationWizardPage;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;
import com.worldline.appprofiles.wizard.ui.project.pages.components.ProfileListViewerSorter;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ModuleListCheckStateListener;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ModuleListCheckStateProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ModuleListContentProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ProfileListContentProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ProfileListLabelProvider;
import com.worldline.appprofiles.wizard.ui.project.pages.tools.ProfileListSelectionChangedListener;

public class ApplicationChooseProfilePage extends ValidatingApplicationWizardPage {

	private String applicationKind;

	private ApplicationWizardOutput applicationWizardOutput;

	private ComboViewer comboViewer;

	public ApplicationChooseProfilePage(String applicationKind, ApplicationWizardOutput applicationWizardOutput) {
		super(ApplicationChooseProfilePageMessages.PAGE_NAME.value());
		this.applicationWizardOutput = applicationWizardOutput;
		this.applicationKind = applicationKind;
		setTitle(ApplicationChooseProfilePageMessages.PAGE_TITLE.value());
		setDescription(ApplicationChooseProfilePageMessages.PAGE_DESC.value());
	}

	public void createControl(Composite parent) {
		// 1- background
		Composite background = new Composite(parent, SWT.NONE);
		background.setLayout(new FormLayout());

		// 2- profiles' combo-list
		Label comboLabel = new Label(background, SWT.NONE);
		comboLabel.setText(ApplicationChooseProfilePageMessages.PAGE_LABEL_SELECT_PROFILE.value());
		new FormDataBuilder().left().top(0, 9).width(80).apply(comboLabel);

		final Combo combo = new Combo(background, SWT.READ_ONLY);
		new FormDataBuilder().left(comboLabel).right().top().apply(combo);
		this.comboViewer = new ComboViewer(combo);
		comboViewer.setLabelProvider(new ProfileListLabelProvider());
		comboViewer.setContentProvider(new ProfileListContentProvider(this.applicationKind));
		comboViewer.setSorter(new ProfileListViewerSorter());

		// 3- profile description
		Group descriptionGroup = new Group(background, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 5;
		fillLayout.marginWidth = 5;
		descriptionGroup.setLayout(fillLayout);
		descriptionGroup.setText("Profile description : ");
		Label descriptionLabel = new Label(descriptionGroup, SWT.WRAP);
		new FormDataBuilder().left().right().top(combo).height(7 * 5 + 7).apply(descriptionGroup);

		// 4- modules treeview
		final Group modulesGroup = new Group(background, SWT.NONE);
		modulesGroup.setText(ApplicationChooseProfilePageMessages.PAGE_LABEL_MODULELIST.value());
		modulesGroup.setLayout(new FillLayout());
		new FormDataBuilder().left().right().top(descriptionGroup).bottom().apply(modulesGroup);

		final Tree modulesTree = new Tree(modulesGroup, SWT.CHECK | SWT.V_SCROLL | SWT.SINGLE | SWT.BORDER);
		modulesTree.setLinesVisible(false);
		modulesTree.setHeaderVisible(false);

		CheckboxTreeViewer modulesTreeViewer = new CheckboxTreeViewer(modulesTree);
		modulesTreeViewer.setCheckStateProvider(new ModuleListCheckStateProvider());
		// modulesTreeViewer.setLabelProvider(new ModuleListLabelProvider());
		modulesTreeViewer.setContentProvider(new ModuleListContentProvider());

		modulesTreeViewer.setAutoExpandLevel(3);

		comboViewer.addSelectionChangedListener(new ProfileListSelectionChangedListener(modulesTreeViewer,
				applicationWizardOutput, descriptionLabel) {

			@Override
			protected void validate() {
				ApplicationChooseProfilePage.this.validate();
			}

		});

		modulesTreeViewer.addCheckStateListener(new ModuleListCheckStateListener(applicationWizardOutput) {

			@Override
			protected void validate() {
				ApplicationChooseProfilePage.this.validate();

			}

		});

		setControl(background);
		comboViewer.setInput(new Object());
		validate();
	}

	@Override
	protected ApplicationWizardPageStatus isPageValid() {

		if (applicationWizardOutput.getSelectedApplicationProfile() == null) {
			return ApplicationWizardPageStatus.info(ApplicationChooseProfilePageMessages.PAGE_ERROR_NOPROFILE.value(),
					false);
		}
		for (ApplicationModule module : applicationWizardOutput.getSelectedModules()) {
			for (ApplicationModule req : module.getRequirements()) {
				if (!applicationWizardOutput.getSelectedModules().contains(req)) {
					return ApplicationWizardPageStatus.error(ApplicationChooseProfilePageMessages.PAGE_ERROR_MODULEREQ
							.value(module.getName(), req.getName()));
				}
			}
			for (ApplicationModule inc : module.getIncompatibilities()) {
				if (applicationWizardOutput.getSelectedModules().contains(inc)) {
					return ApplicationWizardPageStatus.error(ApplicationChooseProfilePageMessages.PAGE_ERROR_MODULEINC
							.value(module.getName(), inc.getName()));
				}
			}
		}

		boolean hasMandatoryModule = false;
		for (Iterator<ApplicationModule> iterator = applicationWizardOutput.getSelectedApplicationProfile()
				.getModules().iterator(); iterator.hasNext() && !hasMandatoryModule;) {
			hasMandatoryModule = iterator.next().isMandatory();
		}

		if (!hasMandatoryModule && applicationWizardOutput.getSelectedModules().size() == 0)
			return ApplicationWizardPageStatus.info("At least one module should be selected.", false);
		return ApplicationWizardPageStatus.ok();
	}

	public ApplicationWizardOutput getApplicationWizardOutput() {
		return applicationWizardOutput;
	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		comboViewer.getCombo().setFocus();
	}

}
