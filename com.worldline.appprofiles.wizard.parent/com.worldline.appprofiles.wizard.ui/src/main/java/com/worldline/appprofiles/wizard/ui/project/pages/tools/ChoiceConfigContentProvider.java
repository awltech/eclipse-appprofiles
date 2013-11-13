package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.worldline.appprofiles.wizard.ui.model.AbstractConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ApplicationConfiguration;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.ChoiceConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Contents provider that returns optional configuration items from Wizard
 * Output
 * 
 * @author mvanbesien
 * 
 */
public class ChoiceConfigContentProvider implements IStructuredContentProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ApplicationWizardOutput) {

			// At profile level
			Collection<Object> collection = new ArrayList<Object>();
			ApplicationProfile selectedProfile = ((ApplicationWizardOutput) inputElement)
					.getSelectedApplicationProfile();
			for (ApplicationConfiguration config : selectedProfile.getConfigurations()) {
				for (AbstractConfigurationEntry entry : config.getConfigurationEntries())
					if (entry instanceof ChoiceConfigurationEntry)
						collection.add(entry);
			}

			// At module level
			Collection<ApplicationModule> selectedModules = ((ApplicationWizardOutput) inputElement)
					.getSelectedModules();
			for (ApplicationModule module : selectedProfile.getModules()) {
				if (module.isMandatory() || selectedModules.contains(module)) {
					for (ApplicationConfiguration config : module.getConfigurations()) {
						for (AbstractConfigurationEntry entry : config.getConfigurationEntries())
							if (entry instanceof ChoiceConfigurationEntry)
								collection.add(entry);
					}
				}
			}
			return collection.toArray();
		}
		return new Object[0];
	}

}
