package com.worldline.appprofiles.wizard.ui.model;

import com.worldline.appprofiles.wizard.ui.model.facade.IValueConfigurator;
import com.worldline.appprofiles.wizard.ui.model.facade.IValueValidator;

public class ValueConfigurationEntry extends AbstractConfigurationEntry {

	private IValueConfigurator configurator;
	
	private String message;
	
	private IValueValidator validator;

	public ValueConfigurationEntry(String id, String message, IValueConfigurator configurator, String defaultValue, IValueValidator validator) {
		super(id);
		this.message = message;
		this.defaultValue = defaultValue;
		this.configurator = configurator;
		this.validator = validator;
	}

	private String defaultValue;

	public String getDefaultValue() {
		return defaultValue;
	}
	
	public IValueConfigurator getConfigurator() {
		return configurator;
	}
	
	public String getMessage() {
		return message;
	}
	
	public IValueValidator getValidator() {
		return validator;
	}

}
