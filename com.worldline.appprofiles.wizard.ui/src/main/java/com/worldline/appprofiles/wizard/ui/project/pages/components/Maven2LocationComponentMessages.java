package com.worldline.appprofiles.wizard.ui.project.pages.components;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum Maven2LocationComponentMessages {
	GROUP_TEXT, RADIO_CREATE_IN_WS, RADIO_CREATE_OUT_OF_WS, LABEL_FOLDERPATH, BUTTON_BROWSE
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("Maven2LocationComponentMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (Maven2LocationComponentMessages.resourceBundle == null || !Maven2LocationComponentMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return Maven2LocationComponentMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (Maven2LocationComponentMessages.resourceBundle == null || !Maven2LocationComponentMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(Maven2LocationComponentMessages.resourceBundle.getString(this.name()), args);
	}
	
}
