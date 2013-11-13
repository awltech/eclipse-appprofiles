package com.worldline.appprofiles.wizard.ui.project;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.ui.IWorkingSet;

import com.worldline.appprofiles.wizard.ui.model.AbstractConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;

/**
 * Class returned from wizard, that contain information for module creation &
 * configuration
 * 
 * @author mvanbesien
 * 
 */
public class ApplicationWizardOutput {

	/**
	 * Project name
	 */
	private String projectName;

	/**
	 * Place where the project needs to be created
	 */
	private IPath projectLocation;

	/**
	 * User-specific Properties of the ApplicationProfile
	 */
	private Map<String, String> properties = new HashMap<String, String>();

	/**
	 * show documentation option
	 */
	private boolean showDocumentation;

	/**
	 * Selected profile
	 */
	private ApplicationProfile selectedApplicationProfile;

	/**
	 * Selected modules
	 */
	private Collection<ApplicationModule> selectedModules = new ArrayList<ApplicationModule>();

	/**
	 * Selected configuration items
	 */
	private Map<AbstractConfigurationEntry, Boolean> choiceSelectedConfigurations = new HashMap<AbstractConfigurationEntry, Boolean>();

	/**
	 * Specific
	 */
	private Map<ValueConfigurationEntry, String> valueSelectedConfigurations = new HashMap<ValueConfigurationEntry, String>();

	private IWorkingSet[] workingSets;

	/**
	 * Resets the application profile and all the modules & configuration
	 * managed earlier
	 * 
	 * @param applicationProfile
	 */
	public void resetApplicationProfile(ApplicationProfile applicationProfile) {
		this.selectedApplicationProfile = applicationProfile;

		this.selectedModules.clear();
		for (ApplicationModule module : this.selectedApplicationProfile.getAllModules())
			if (module.isDefaultSelected())
				this.selectedModules.add(module);

		this.choiceSelectedConfigurations.clear();
		this.valueSelectedConfigurations.clear();
	}

	/**
	 * @return selected application profile
	 */
	public ApplicationProfile getSelectedApplicationProfile() {
		return selectedApplicationProfile;
	}

	/**
	 * @return user-selected optional configuration entries
	 */
	public Map<AbstractConfigurationEntry, Boolean> getChoiceSelectedConfigurations() {
		return choiceSelectedConfigurations;
	}

	/**
	 * @return user selected configuration entries
	 */
	public Collection<ApplicationModule> getSelectedModules() {
		return selectedModules;
	}

	/**
	 * @return user modified value configurations
	 */
	public Map<ValueConfigurationEntry, String> getValueSelectedConfiguration() {
		return valueSelectedConfigurations;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public IPath getProjectLocation() {
		return projectLocation;
	}

	public void setProjectLocation(IPath projectLocation) {
		this.projectLocation = projectLocation;
	}

	// public String getUserArtifactId() {
	// return userArtifactId;
	// }
	//
	// public String getUserDescription() {
	// return userDescription;
	// }
	//
	// public String getUserGroupId() {
	// return userGroupId;
	// }
	//
	// public String getUserPackageName() {
	// return userPackageName;
	// }
	//
	// public String getUserVersion() {
	// return userVersion;
	// }
	//
	// public void setUserArtifactId(String userArtifactId) {
	// this.userArtifactId = userArtifactId;
	// }
	//
	// public void setUserDescription(String userDescription) {
	// this.userDescription = userDescription;
	// }
	//
	// public void setUserGroupId(String userGroupId) {
	// this.userGroupId = userGroupId;
	// }
	//
	// public void setUserPackageName(String userPackageName) {
	// this.userPackageName = userPackageName;
	// }
	//
	// public void setUserVersion(String userVersion) {
	// this.userVersion = userVersion;
	// }

	public String getProperty(String key) {
		return this.properties.get(key);
	}

	public void setProperty(String key, String value) {
		if (this.properties.containsKey(key))
			this.properties.remove(key);
		this.properties.put(key, value);
	}

	public boolean isShowDocumentation() {
		return showDocumentation;
	}

	public void setShowDocumentation(boolean showDocumentation) {
		this.showDocumentation = showDocumentation;
	}

	public void setSelectedWorkingSets(IWorkingSet[] iWorkingSets) {
		this.workingSets = iWorkingSets;
	}

	public IWorkingSet[] getSelectedWorkingSets() {
		return workingSets;
	}

}
