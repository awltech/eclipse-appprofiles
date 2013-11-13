package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.ICheckStateListener;

import com.worldline.appprofiles.wizard.ui.model.ChoiceConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Check state listener, in charge of detecting when user selects element in
 * Optional Configuration Entry.
 * 
 * @author mvanbesien
 * 
 * 
 */
public class ChoiceConfigCheckStateListener implements ICheckStateListener {

	/**
	 * Wizard Output instance
	 */
	private ApplicationWizardOutput output;

	/**
	 * Creates new check state listener for optional config entry of the output
	 * provided
	 * 
	 * @param output
	 */
	public ChoiceConfigCheckStateListener(ApplicationWizardOutput output) {
		this.output = output;
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
		if (event.getElement() instanceof ChoiceConfigurationEntry) {
			ChoiceConfigurationEntry entry = (ChoiceConfigurationEntry) event.getElement();

			if (output.getChoiceSelectedConfigurations().containsKey(entry))
				output.getChoiceSelectedConfigurations().remove(entry);

			output.getChoiceSelectedConfigurations().put(entry, event.getChecked());
		}
	}
}
