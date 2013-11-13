package com.worldline.appprofiles.wizard.ui.project;


/**
 * Maven Properties Management. 
 * @author mvanbesien
 *
 */
public class MavenModuleConstants {

	/**
	 * Constant used to identify the user group id property of the maven-based profile
	 */
	public static final String USER_GROUP_ID = "maven.userGroupId";

	/**
	 * Constant used to identify the user artifact id property of the maven-based profile
	 */
	public static final String USER_ARTIFACT_ID = "maven.userArtifactId";

	/**
	 * Constant used to identify the user package name property of the maven-based profile
	 */
	public static final String USER_PACKAGE_NAME = "maven.userPackageName";

	/**
	 * Constant used to identify the user version property of the maven-based profile
	 */
	public static final String USER_VERSION = "maven.userVersion";

	/**
	 * Constant used to identify the user description property of the maven-based profile
	 */
	public static final String USER_DESCRIPTION = "maven.userDescription";

	public static final String UPDATE_FORCE_SNAPSHOTS = "maven.forceSnapshots";
	
	public static final String UPDATE_OFFLINE = "maven.offlineUpdate";
	
	/**
	 * Helping method that sets the user group id property in the provided output object
	 * @param output
	 * @param newValue
	 */
	public static void setUserGroupId(ApplicationWizardOutput output, String newValue) {
		output.setProperty(USER_GROUP_ID, newValue);
	}

	/**
	 * Helping method that sets the user artifact id property in the provided output object
	 * @param output
	 * @param newValue
	 */
	public static void setUserArtifactId(ApplicationWizardOutput output, String newValue) {
		output.setProperty(USER_ARTIFACT_ID, newValue);
	}

	/**
	 * Helping method that sets the user package name property in the provided output object
	 * @param output
	 * @param newValue
	 */
	public static void setUserPackageName(ApplicationWizardOutput output, String newValue) {
		output.setProperty(USER_PACKAGE_NAME, newValue);
	}

	/**
	 * Helping method that sets the user version property in the provided output object
	 * @param output
	 * @param newValue
	 */
	public static void setUserVersion(ApplicationWizardOutput output, String newValue) {
		output.setProperty(USER_VERSION, newValue);
	}

	/**
	 * Helping method that sets the user description property in the provided output object
	 * @param output
	 * @param newValue
	 */
	public static void setUserDescription(ApplicationWizardOutput output, String newValue) {
		output.setProperty(USER_DESCRIPTION, newValue);
	}

	/**
	 * Helping method that returns the user group id property from the provided output object
	 * @param output
	 * @return
	 */
	public static String getUserGroupId(ApplicationWizardOutput output) {
		return output.getProperty(USER_GROUP_ID);
	}

	/**
	 * Helping method that returns the user artifact id property from the provided output object
	 * @param output
	 * @return
	 */
	public static String getUserArtifactId(ApplicationWizardOutput output) {
		return output.getProperty(USER_ARTIFACT_ID);
	}

	/**
	 * Helping method that returns the user package name property from the provided output object
	 * @param output
	 * @return
	 */
	public static String getUserPackageName(ApplicationWizardOutput output) {
		return output.getProperty(USER_PACKAGE_NAME);
	}

	/**
	 * Helping method that returns the user version property from the provided output object
	 * @param output
	 * @return
	 */
	public static String getUserVersion(ApplicationWizardOutput output) {
		return output.getProperty(USER_VERSION);
	}

	/**
	 * Helping method that returns the user description property from the provided output object
	 * @param output
	 * @return
	 */
	public static String getUserDescription(ApplicationWizardOutput output) {
		return output.getProperty(USER_DESCRIPTION);
	}

}
