package com.worldline.appprofiles.wizard.maven.archetype;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IPath;

/**
 * Archetype Application Class Contains archetype definition, and user values
 * used for archetype installation
 * 
 * @author mvanbesien
 * 
 */
public class ArchetypeApplication {

	/*
	 * Archetype used
	 */
	private Archetype archetype;

	/*
	 * User Artifact ID
	 */
	private String userArtifactId;

	/*
	 * User Group ID
	 */
	private String userGroupId;

	/*
	 * User Version
	 */
	private String userVersion;

	/*
	 * user Package Name
	 */
	private String userPackageName;

	/*
	 * Project container Path
	 */
	private IPath projectContainerPath;

	/*
	 * Properties
	 */
	private Map<String, String> userProperties = new HashMap<String, String>();

	/*
	 * User description
	 */
	private String userDescription;
	
	/**
	 * Specify if this archetype needs to be imported in Eclipse after generation 
	 * (typically it's not needed if it's a sub-module)
	 */
	private boolean shouldImport = true;

	public ArchetypeApplication(Archetype archetype, String userGroupId, String userArtifactId, String userVersion,
			String userPackageName, IPath projectContainerPath, String userDescription) {
		super();
		this.archetype = archetype;
		this.userArtifactId = userArtifactId;
		this.userGroupId = userGroupId;
		this.userVersion = userVersion;
		this.userPackageName = userPackageName;
		this.projectContainerPath = projectContainerPath;
		this.userDescription = userDescription;
	}

	public void setProperties(Map<String, String> properties) {
		this.userProperties.clear();
		this.userProperties.putAll(properties);
	}

	public void setProperty(String key, String value) {
		if (this.userProperties.containsKey(key))
			this.userProperties.remove(key);
		this.userProperties.put(key, value);
	}

	public Archetype getArchetype() {
		return archetype;
	}

	public IPath getProjectContainerPath() {
		return projectContainerPath;
	}

	public String getUserArtifactId() {
		return userArtifactId;
	}

	public String getUserDescription() {
		return userDescription;
	}

	public String getUserGroupId() {
		return userGroupId;
	}

	public String getUserVersion() {
		return userVersion;
	}

	public String getUserPackageName() {
		return userPackageName;
	}
	
	public Map<String, String> getUserProperties() {
		return userProperties;
	}

	
	public boolean isShouldImport() {
		return shouldImport;
	}

	
	public void setShouldImport(boolean shouldImport) {
		this.shouldImport = shouldImport;
	}

}
