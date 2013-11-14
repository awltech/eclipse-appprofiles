package com.worldline.appprofiles.wizard.ui.project.pages.components;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum WorkingSetSelectionComponentMessages {
	GROUP_NAME, ENABLE_BUTTON_TEXT, SELECTED_WORKING_SETS_LABEL_TEXT, SELECTED_WORKING_SETS_BUTTON_TEXT
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("WorkingSetSelectionComponentMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (WorkingSetSelectionComponentMessages.resourceBundle == null || !WorkingSetSelectionComponentMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return WorkingSetSelectionComponentMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (WorkingSetSelectionComponentMessages.resourceBundle == null || !WorkingSetSelectionComponentMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(WorkingSetSelectionComponentMessages.resourceBundle.getString(this.name()), args);
	}
	
}
