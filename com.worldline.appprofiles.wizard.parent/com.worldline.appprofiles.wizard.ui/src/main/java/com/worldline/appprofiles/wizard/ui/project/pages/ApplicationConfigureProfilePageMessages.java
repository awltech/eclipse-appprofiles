package com.worldline.appprofiles.wizard.ui.project.pages;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ApplicationConfigureProfilePageMessages {
	PAGE_NAME, PAGE_TITLE, PAGE_DESC, PAGE_LABEL_OPTENTRIES_LIST, PAGE_LABEL_VALENTRIES_LIST, PAGE_LABEL_VALENTRIES_VALLABEL, PAGE_LABEL_SELECTEDPROFILE
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationConfigureProfilePageMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ApplicationConfigureProfilePageMessages.resourceBundle == null || !ApplicationConfigureProfilePageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ApplicationConfigureProfilePageMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ApplicationConfigureProfilePageMessages.resourceBundle == null || !ApplicationConfigureProfilePageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ApplicationConfigureProfilePageMessages.resourceBundle.getString(this.name()), args);
	}
	
}
