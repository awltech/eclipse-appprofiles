package com.worldline.appprofiles.wizard.maven;

import java.util.logging.Logger;

import com.worldline.appprofiles.wirzard.commons.logging.AppProfilesConsoleManager;


public class AppProfilesWizardMavenLogger {

	public static final Logger logger = Logger.getLogger("AppProfilesWizardMaven");

	static {
		// TODO Implement here initialization of the logger.
		AppProfilesConsoleManager.getInstance().register(logger);
	}

}
