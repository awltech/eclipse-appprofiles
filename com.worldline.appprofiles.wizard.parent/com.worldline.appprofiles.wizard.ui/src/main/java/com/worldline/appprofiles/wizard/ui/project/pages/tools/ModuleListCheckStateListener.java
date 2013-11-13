package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;

import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Check State Listener in charge of updating selection when management of
 * mandatory/requirements/dependencies
 * 
 * @author mvanbesien
 * 
 */
public abstract class ModuleListCheckStateListener implements ICheckStateListener {

	private ApplicationWizardOutput applicationWizardOutput;

	public ModuleListCheckStateListener(ApplicationWizardOutput applicationWizardOutput) {
		this.applicationWizardOutput = applicationWizardOutput;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ICheckStateListener#checkStateChanged(org.eclipse
	 * .jface.viewers.CheckStateChangedEvent)
	 */
	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		if (event.getElement() instanceof ApplicationModule) {

			ApplicationModule module = (ApplicationModule) event.getElement();
			// Manage the fact we cannot un-check the mandatory elements.
			if (module.isMandatory())
				event.getCheckable().setChecked(module, true);
			else {
				if (event.getChecked())
					applicationWizardOutput.getSelectedModules().add(module);
				else
					applicationWizardOutput.getSelectedModules().remove(module);
			}
		}
		validate();
	}
	
	protected abstract void validate();
}
