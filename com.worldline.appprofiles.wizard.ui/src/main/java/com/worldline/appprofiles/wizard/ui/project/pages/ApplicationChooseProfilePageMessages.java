package com.worldline.appprofiles.wizard.ui.project.pages;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ApplicationChooseProfilePageMessages {
	PAGE_NAME, PAGE_TITLE, PAGE_DESC, PAGE_LABEL_SELECT_PROFILE, PAGE_LABEL_MODULELIST, PAGE_ERROR_NOPROFILE, PAGE_ERROR_MODULEREQ, PAGE_ERROR_MODULEINC
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationChooseProfilePageMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ApplicationChooseProfilePageMessages.resourceBundle == null || !ApplicationChooseProfilePageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ApplicationChooseProfilePageMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ApplicationChooseProfilePageMessages.resourceBundle == null || !ApplicationChooseProfilePageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ApplicationChooseProfilePageMessages.resourceBundle.getString(this.name()), args);
	}
	
}
