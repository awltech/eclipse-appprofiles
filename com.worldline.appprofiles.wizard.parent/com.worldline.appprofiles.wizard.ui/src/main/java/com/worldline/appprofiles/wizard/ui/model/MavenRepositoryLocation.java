package com.worldline.appprofiles.wizard.ui.model;

public class MavenRepositoryLocation extends ModuleLocation {

	private String groupId;

	private String artifactId;

	private String version;

	private String repository;

	public MavenRepositoryLocation(String groupId, String artifactId, String version, String repository) {
		this.groupId = groupId;
		this.artifactId = artifactId;
		this.version = version;
		this.repository = repository;
	}

	public String getArtifactId() {
		return artifactId;
	}

	public String getGroupId() {
		return groupId;
	}

	public String getRepository() {
		return repository;
	}

	public String getVersion() {
		return version;
	}

}
