package com.worldline.appprofiles.wizard.ui.model;

import java.util.ArrayList;
import java.util.List;

import com.worldline.appprofiles.wizard.ui.model.facade.IGlobalConfigurator;

public class ApplicationProfile {

	private String id;
	
	private String name;
	
	private String description;
	
	private ApplicationProfileKind kind;
	
	private List<ApplicationModule> modules = new ArrayList<ApplicationModule>();
	
	private List<ApplicationConfiguration> configurations = new ArrayList<ApplicationConfiguration>();

	private List<IGlobalConfigurator> globalConfigurators = new ArrayList<IGlobalConfigurator>();
	
	private String documentationURL = "";
	
	public ApplicationProfile(String id, String name, String kind) {
		this.id = id;
		this.name = name;
		this.kind = ApplicationProfileKind.fromKey(kind);
	}
	
	public void addModule(ApplicationModule module) {
		this.modules.add(module);
	}
	
	public void addConfiguration(ApplicationConfiguration configuration) {
		this.configurations.add(configuration);
	}
	
	public List<ApplicationConfiguration> getConfigurations() {
		return configurations;
	}
	
	public String getId() {
		return id;
	}
	
	public ApplicationProfileKind getKind() {
		return kind;
	}
	
	/**
	 * 
	 * @return the modules directly defined in the Profile
	 */
	public List<ApplicationModule> getModules() {
		return modules;
	}
	
	public String getName() {
		return name;
	}

	public void setDocumentationURL(String documentationURL) {
		this.documentationURL = documentationURL;
	}
	
	public String getDocumentationURL() {
		return this.documentationURL;
	}
	
	public List<IGlobalConfigurator> getGlobalConfigurators() {
		return globalConfigurators;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	/**
	 * 
	 * @return the modules defined in the profile, and the sub-modules
	 */
	public  List<ApplicationModule> getAllModules() {
		List<ApplicationModule> allModules = new ArrayList<ApplicationModule>(modules);
		for (ApplicationModule subModule : modules) {
			allModules.addAll(subModule.getAllSubModules());
		}
		return allModules;
	}
}
