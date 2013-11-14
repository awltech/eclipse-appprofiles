package com.worldline.appprofiles.wizard.ui.model;

import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;

public class OptionalConfigurationEntry extends ChoiceConfigurationEntry {

	private IConfigurator configurator;

	public OptionalConfigurationEntry(String id, String message, IConfigurator configurator, boolean defaultValue) {
		super(id, message, defaultValue);
		this.configurator = configurator;
	}

	public IConfigurator getConfigurator() {
		return configurator;
	}
}
