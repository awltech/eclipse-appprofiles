package com.worldline.appprofiles.wizard.ui.model;

import java.util.ArrayList;
import java.util.List;

public class ApplicationModule {

	private String id;

	private String name;

	private String projectNamePrefix = "";

	private String projectNameSuffix = "";

	private String packageNameSuffix = "";

	private boolean mandatory;

	private List<ApplicationModule> subModules = new ArrayList<ApplicationModule>();
	
	private List<ApplicationModule> requirements = new ArrayList<ApplicationModule>();

	private List<ApplicationModule> incompatibilities = new ArrayList<ApplicationModule>();

	private List<ApplicationConfiguration> configurations = new ArrayList<ApplicationConfiguration>();

	private ModuleLocation location;

	//Only one of these must be non-null:
	private ApplicationProfile parentProfile;
	private ApplicationModule parentModule;

	private boolean defaultSelected;

	public ApplicationModule(String id, String name, boolean mandatory, ModuleLocation location, boolean defaultSelected) {
		this.id = id;
		this.name = name;
		this.mandatory = mandatory;
		this.location = location;
		this.defaultSelected = defaultSelected;
	}
	

	public void addRequirement(ApplicationModule module) {
		this.requirements.add(module);
	}

	public void addIncompatibility(ApplicationModule module) {
		this.incompatibilities.add(module);
	}

	public String getId() {
		return id;
	}

	public List<ApplicationModule> getRequirements() {
		return requirements;
	}

	public List<ApplicationModule> getIncompatibilities() {
		return incompatibilities;
	}

	public ModuleLocation getLocation() {
		return location;
	}

	public String getName() {
		return name;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	/**
	 * 
	 * @return the sub-modules directly defined in this module
	 */
	public List<ApplicationModule> getSubModules() {
		return subModules;
	}
	
	public void setSubModules(List<ApplicationModule> modules) {
		this.subModules = modules;
	}

	public ApplicationProfile getParentProfile() {
		return parentProfile;
	}
	
	public void setParentProfile(ApplicationProfile parentProfile) {
		this.parentProfile = parentProfile;
	}

	public ApplicationModule getParentModule() {
		return parentModule;
	}

	
	public void setParentModule(ApplicationModule parentModule) {
		this.parentModule = parentModule;
	}

	public String getProjectNamePrefix() {
		return projectNamePrefix;
	}

	public String getProjectNameSuffix() {
		return projectNameSuffix;
	}

	public void setProjectNamePrefix(String projectNamePrefix) {
		this.projectNamePrefix = projectNamePrefix;
	}

	public void setProjectNameSuffix(String projectNameSuffix) {
		this.projectNameSuffix = projectNameSuffix;
	}

	public void addConfiguration(ApplicationConfiguration configuration) {
		this.configurations.add(configuration);
	}

	public List<ApplicationConfiguration> getConfigurations() {
		return configurations;
	}

	public void setPackageNameSuffix(String packageNameSuffix) {
		this.packageNameSuffix = packageNameSuffix;
	}

	public String getPackageNameSuffix() {
		return packageNameSuffix;
	}

	public boolean isDefaultSelected() {
		return defaultSelected;
	}
	
	/**
	 * 
	 * @return the sub-modules defined in the module, and the sub-sub-modules recursively
	 */
	public  List<ApplicationModule> getAllSubModules() {
		List<ApplicationModule> allModules = new ArrayList<ApplicationModule>(subModules);
		for (ApplicationModule subModule : subModules) {
			allModules.addAll(subModule.getAllSubModules());
		}
		return allModules;
	}
	
	@Override
	public String toString() {
		return name;
	}

}
