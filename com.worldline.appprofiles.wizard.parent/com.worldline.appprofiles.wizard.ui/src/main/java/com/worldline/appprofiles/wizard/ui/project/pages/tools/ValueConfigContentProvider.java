package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.worldline.appprofiles.wizard.ui.model.AbstractConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ApplicationConfiguration;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Content provider that fills the value configuration entries table.
 * 
 * @author mvanbesien
 * 
 */
public class ValueConfigContentProvider implements IStructuredContentProvider {

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
			ApplicationWizardOutput output = (ApplicationWizardOutput) inputElement;
			ApplicationProfile profile = output.getSelectedApplicationProfile();
			Collection<Object> entries = new ArrayList<Object>();
			for (ApplicationConfiguration config : profile.getConfigurations())
				for (AbstractConfigurationEntry entry : config.getConfigurationEntries())
					if (entry instanceof ValueConfigurationEntry)
						entries.add(entry);

			for (ApplicationModule module : profile.getModules())
				if (module.isMandatory() || output.getSelectedModules().contains(module)) {
					for (ApplicationConfiguration config : module.getConfigurations())
						for (AbstractConfigurationEntry entry : config.getConfigurationEntries())
							if (entry instanceof ValueConfigurationEntry)
								entries.add(entry);
				}
			return entries.toArray();
		}
		return new Object[0];
	}

}
