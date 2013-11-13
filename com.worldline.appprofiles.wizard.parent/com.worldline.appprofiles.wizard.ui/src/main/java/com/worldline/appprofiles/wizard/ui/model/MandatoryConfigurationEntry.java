package com.worldline.appprofiles.wizard.ui.model;

import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;

public class MandatoryConfigurationEntry extends AbstractConfigurationEntry {

	public MandatoryConfigurationEntry(String id, IConfigurator configurator) {
		super(id);
		this.configurator = configurator;
	}

	protected IConfigurator configurator;

	public IConfigurator getConfigurator() {
		return configurator;
	}

}
