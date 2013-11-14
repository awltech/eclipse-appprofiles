package com.worldline.appprofiles.wizard.ui;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ApplicationWizardMessages {
	WINDOW_TITLE, EXCEPTION_OPENING_DOCUMENTATION
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationWizardMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ApplicationWizardMessages.resourceBundle == null || !ApplicationWizardMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ApplicationWizardMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ApplicationWizardMessages.resourceBundle == null || !ApplicationWizardMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ApplicationWizardMessages.resourceBundle.getString(this.name()), args);
	}
	
}
