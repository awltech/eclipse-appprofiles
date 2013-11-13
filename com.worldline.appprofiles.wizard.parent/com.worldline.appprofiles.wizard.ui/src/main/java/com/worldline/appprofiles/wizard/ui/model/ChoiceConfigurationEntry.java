package com.worldline.appprofiles.wizard.ui.model;

public abstract class ChoiceConfigurationEntry extends AbstractConfigurationEntry {

	private String message;

	public ChoiceConfigurationEntry(String id, String message, boolean defaultValue) {
		super(id);
		this.message = message;
		this.defaultValue = defaultValue;
	}

	protected boolean defaultValue;

	public boolean getDefaultValue() {
		return this.defaultValue;
	}

	public String getMessage() {
		return message;
	}

}
