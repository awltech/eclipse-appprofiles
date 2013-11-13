package com.worldline.appprofiles.wizard.maven;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum AppProfilesWizardMaven {
	PROJ_CREATION_1, PROJ_CREATION_ERROR, PROJ_CREATION_ERROR_2, PROJ_CREATION_2, PROJ_CREATION_3, PROJ_CREATION_4, PROJ_CREATION_5, REPO_INSTALL_DIALOG_MESSAGE, REPO_INSTALL_DIALOG_TITLE, REPO_INSTALL_DIALOG_OK, REPO_INSTALL_DIALOG_CANCEL, PROJ_CREATION_6, PACK_CREATION_ERROR, DIALOG_ERROR_TITLE, DIALOG_ERROR_MESSAGE, MAVEN_ERROR_IMPORT, MAVEN_ERROR_UPDATE, PROJ_CREATION_7
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("AppProfilesWizardMaven");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (AppProfilesWizardMaven.resourceBundle == null || !AppProfilesWizardMaven.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return AppProfilesWizardMaven.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (AppProfilesWizardMaven.resourceBundle == null || !AppProfilesWizardMaven.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(AppProfilesWizardMaven.resourceBundle.getString(this.name()), args);
	}
	
}
