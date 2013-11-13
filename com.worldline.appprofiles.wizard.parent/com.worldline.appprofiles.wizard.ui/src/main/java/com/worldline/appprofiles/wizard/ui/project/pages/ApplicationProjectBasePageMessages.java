package com.worldline.appprofiles.wizard.ui.project.pages;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ApplicationProjectBasePageMessages {
	PAGE_NAME, PAGE_TITLE, PAGE_DESC, PAGE_LABEL_APPNAME, PAGE_LABEL_ERROR_FSFOLDEREXISTS, PAGE_LABEL_ERROR_PROJ_OR_APP_EXISTS, PAGE_LABEL_ERROR_PATH_INVALID, PAGE_LABEL_ERROR_APPNAME_INVALID
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationProjectBasePageMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ApplicationProjectBasePageMessages.resourceBundle == null || !ApplicationProjectBasePageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ApplicationProjectBasePageMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ApplicationProjectBasePageMessages.resourceBundle == null || !ApplicationProjectBasePageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ApplicationProjectBasePageMessages.resourceBundle.getString(this.name()), args);
	}
	
}
