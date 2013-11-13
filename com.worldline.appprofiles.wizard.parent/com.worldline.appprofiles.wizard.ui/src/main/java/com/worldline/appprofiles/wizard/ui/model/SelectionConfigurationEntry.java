package com.worldline.appprofiles.wizard.ui.model;

import com.worldline.appprofiles.wizard.ui.model.facade.ISelectionConfigurator;

public class SelectionConfigurationEntry extends ChoiceConfigurationEntry {

	private ISelectionConfigurator configurator;

	public SelectionConfigurationEntry(String id, String message, ISelectionConfigurator configurator,
			boolean defaultValue) {
		super(id, message, defaultValue);
		this.configurator = configurator;
	}

	public ISelectionConfigurator getConfigurator() {
		return configurator;
	}
	
}
