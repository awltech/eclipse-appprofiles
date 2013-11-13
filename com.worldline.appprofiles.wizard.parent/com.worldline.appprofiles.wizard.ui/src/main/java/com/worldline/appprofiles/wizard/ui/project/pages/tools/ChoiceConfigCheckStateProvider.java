package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.ICheckStateProvider;

import com.worldline.appprofiles.wizard.ui.model.ChoiceConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Config Check State provider. Such provider is in charge of setting initially
 * checked elements when input is given to viewer.
 * 
 * @author mvanbesien
 * 
 */
public class ChoiceConfigCheckStateProvider implements ICheckStateProvider {

	/**
	 * Output instance
	 */
	private ApplicationWizardOutput output;

	/**
	 * Create new check state provider based on provided output
	 * 
	 * @param output
	 */
	public ChoiceConfigCheckStateProvider(ApplicationWizardOutput output) {
		this.output = output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ICheckStateProvider#isChecked(java.lang.Object)
	 */
	@Override
	public boolean isChecked(Object element) {
		if (element instanceof ChoiceConfigurationEntry) {
			ChoiceConfigurationEntry entry = (ChoiceConfigurationEntry) element;
			if (output.getChoiceSelectedConfigurations().containsKey(entry))
				return output.getChoiceSelectedConfigurations().get(entry);
			return entry.getDefaultValue();
		}
		return false;
	}

	@Override
	public boolean isGrayed(Object element) {
		return false;
	}

}
