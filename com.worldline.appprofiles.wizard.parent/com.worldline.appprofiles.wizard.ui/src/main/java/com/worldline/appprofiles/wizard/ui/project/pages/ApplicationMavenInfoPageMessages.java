package com.worldline.appprofiles.wizard.ui.project.pages;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ApplicationMavenInfoPageMessages {
	PAGE_NAME, PAGE_TITLE, PAGE_DESC, PAGE_GROUP_TITLE, PAGE_LABEL_GROUPID, PAGE_LABEL_ARTFID, PAGE_LABEL_PACKNAME, PAGE_LABEL_VERSION, PAGE_LABEL_DESCRIPTION, PAGE_LABEL_OPENDOC, PAGE_ERROR_INVALID_GROUPID, PAGE_ERROR_INVALID_PACKNAME, PAGE_ERROR_EMPTY_PACKNAME, PAGE_ERROR_EMPTY_ARTFID, PAGE_ERROR_EMPTY_GROUPID
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationMavenInfoPageMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ApplicationMavenInfoPageMessages.resourceBundle == null || !ApplicationMavenInfoPageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ApplicationMavenInfoPageMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ApplicationMavenInfoPageMessages.resourceBundle == null || !ApplicationMavenInfoPageMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ApplicationMavenInfoPageMessages.resourceBundle.getString(this.name()), args);
	}
	
}
