package com.worldline.appprofiles.wizard.maven;

import java.util.logging.Logger;

import net.atos.xa.common.resources.logging.XAConsoleManager;

public class AppProfilesWizardMavenLogger {

	public static final Logger logger = Logger.getLogger("AppProfilesWizardMaven");

	static {
		// TODO Implement here initialization of the logger.
		XAConsoleManager.getInstance().register(logger);
	}

}
