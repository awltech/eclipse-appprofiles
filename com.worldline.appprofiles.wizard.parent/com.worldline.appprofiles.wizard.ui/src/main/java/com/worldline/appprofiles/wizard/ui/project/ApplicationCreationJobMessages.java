package com.worldline.appprofiles.wizard.ui.project;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ApplicationCreationJobMessages {
	JOB_NAME, TASK_NAME, SUBTASK1_NAME, SUBTASK2_NAME, CONFIG_APPLICATION_STATUS_MESSAGE
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationCreationJobMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ApplicationCreationJobMessages.resourceBundle == null || !ApplicationCreationJobMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ApplicationCreationJobMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ApplicationCreationJobMessages.resourceBundle == null || !ApplicationCreationJobMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ApplicationCreationJobMessages.resourceBundle.getString(this.name()), args);
	}
	
}
