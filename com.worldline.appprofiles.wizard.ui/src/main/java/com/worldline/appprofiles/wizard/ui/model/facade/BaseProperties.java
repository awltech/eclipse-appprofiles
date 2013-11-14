package com.worldline.appprofiles.wizard.ui.model.facade;

/**
 * List of default properties' keys, used in project creation.
 * 
 * @author mvanbesien
 * 
 */
public class BaseProperties {

	/**
	 * Name of the application, as set by the user in the wizard
	 */
	public static final String APPLICATION_NAME = "userApplicationName";

	/**
	 * GroupId of the application, as set by the user in the wizard
	 */
	public static final String APPLICATION_GROUPID = "userApplicationGroupId";

	/**
	 * ArtifactId of the application, as set by the user in the wizard
	 */
	public static final String APPLICATION_ARTIFACTID = "userApplicationArtifactId";

	/**
	 * Version of the application, as set by the user in the wizard
	 */
	public static final String APPLICATION_VERSION = "userApplicationVersion";

	/**
	 * Place where the application will be created, as set by the user in the
	 * wizard
	 */
	public static final String APPLICATION_LOCATION = "userApplicationLocation";

	/**
	 * Default package within the application, as set by the user in the wizard
	 */
	public static final String APPLICATION_PACKAGE = "userApplicationPackage";

	/**
	 * GroupId of the currently processed module, as computed from profile
	 * definition & user input from wizard
	 */
	public static final String MODULE_GROUPID = "userModuleGroupId";

	/**
	 * ArtifactId of the currently processed module, as computed from profile
	 * definition & user input from wizard
	 */
	public static final String MODULE_ARTIFACTID = "userModuleArtifactId";

	/**
	 * Version of the currently processed module, as computed from profile
	 * definition & user input from wizard
	 */
	public static final String MODULE_VERSION = "userModuleVersion";

	/**
	 * Place where of the currently processed module will be created, as
	 * computed from profile definition & user input from wizard
	 */
	public static final String MODULE_LOCATION = "userModuleLocation";

	/**
	 * Default package within the currently processed module, as computed from
	 * profile definition & user input from wizard
	 */
	public static final String MODULE_PACKAGE = "userModulePackage";

}
