package com.worldline.appprofiles.wizard.maven.archetype;

import org.eclipse.swt.graphics.Image;

/**
 * Archetype Description Pojo
 */
public class Archetype {

	/*
	 * Archetype name
	 */
	private String name;

	/*
	 * Archetype group ID
	 */
	private String groupId;

	/*
	 * Archetype artifact ID
	 */
	private String artifactId;

	/*
	 * Archetype version as defined in extension point
	 */
	private String installedVersion;

	/*
	 * Archetype path to remote repository
	 */
	private String remoteRepository;

	/*
	 * Archetype Icon
	 */
	private Image icon;

	/*
	 * Archetype Description
	 */
	private String description;

	/* URL to documentation */
	private String documentationURL;

	public Archetype(String artifactID, String groupID, String version, String remoteRepositories, String description) {
		this.artifactId = artifactID;
		this.groupId = groupID;
		this.installedVersion = version;
		this.remoteRepository = remoteRepositories;
		this.description = description;
		this.documentationURL = "";
	}

	public Archetype(String artifactID, String groupID, String version, String remoteRepositories, String description,
			String documentationURL) {
		this(artifactID, groupID, version, remoteRepositories, description);
		this.documentationURL = documentationURL;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public void setArtifactId(String artifactId) {
		this.artifactId = artifactId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getDocumentationURL() {
		return documentationURL;
	}

	public void setDocumentationURL(String documentationURL) {
		this.documentationURL = documentationURL;
		if (this.documentationURL == null)
			this.documentationURL = "";
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image icon) {
		this.icon = icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemoteRepository() {
		return remoteRepository;
	}

	public void setRemoteRepository(String remoteRepository) {
		this.remoteRepository = remoteRepository;
	}

	public String getInstalledVersion() {
		return installedVersion;
	}

	public void setInstalledVersion(String version) {
		this.installedVersion = version;
	}

}
