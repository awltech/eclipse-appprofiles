package com.worldline.appprofiles.wizard.maven.tools;

import java.util.Iterator;

import org.apache.maven.model.Dependency;
import org.apache.maven.model.Model;
import org.apache.maven.model.Repository;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.MavenModelManager;

import com.worldline.appprofiles.wizard.maven.Activator;

/**
 * Utilitary class that proposes fluent API to add dependency to Maven Project,
 * using M2E API
 * 
 * @author mvanbesien
 * 
 */
public class DependencyAdder {

	/*
	 * pom.xml standard file name
	 */
	private static final String POM_XML = "pom.xml";

	/*
	 * Boolean, which aim is to ensure the apply() method is executed one only.
	 */
	private boolean isAdded = false;

	/*
	 * Internal Dependency Instance
	 */
	public Dependency dependency = new Dependency();
	/*
	 * Internal Repository Instance
	 */
	public Repository repository = new Repository();

	/**
	 * Sets dependency's Group ID
	 * 
	 * @param groupId
	 * @return this
	 */
	public DependencyAdder withGroupId(String groupId) {
		this.dependency.setGroupId(groupId);
		return this;
	}

	/**
	 * Sets dependency's Artifact ID
	 * 
	 * @param artifactId
	 * @return this
	 */
	public DependencyAdder withArtifactId(String artifactId) {
		this.dependency.setArtifactId(artifactId);
		return this;
	}

	/**
	 * Sets dependency's version
	 * 
	 * @param version
	 * @return this
	 */
	public DependencyAdder withVersion(String version) {
		this.dependency.setVersion(version);
		return this;
	}

	/**
	 * Sets dependency's location (repository defined by ID & URL)
	 * 
	 * @param id
	 * @param url
	 * @return this
	 */
	public DependencyAdder withRepository(String id, String url) {
		this.repository.setId(id);
		this.repository.setUrl(url);
		return this;
	}

	/**
	 * Applies dependency addition on project. Status returned informs whether
	 * execution was successful
	 * 
	 * @param project
	 * @return
	 */
	public IStatus apply(IProject project) {
		
		// If already run, returns.
		if (isAdded)
			return Status.CANCEL_STATUS;

		// Get manager to load pom file. Then adds dependency & repo if necessary.
		MavenModelManager mavenModelManager = MavenPlugin.getMavenModelManager();
		IFile pomFile = project.getFile(POM_XML);
		if (pomFile == null || !pomFile.exists())
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Pom file not found in project " + project.getName());

		try {
			Model readMavenModel = mavenModelManager.readMavenModel(pomFile);
			readMavenModel.addDependency(this.dependency);

			boolean hasRepository = false;
			for (Iterator<Repository> iterator = readMavenModel.getRepositories().iterator(); iterator.hasNext()
					&& !hasRepository;) {
				hasRepository = this.repository.getUrl() != null
						&& this.repository.getUrl().equals(iterator.next().getUrl());
			}

			if (!hasRepository)
				readMavenModel.addRepository(this.repository);

			
			// Removes old pom file and writes new model into new one.
			pomFile.delete(true, new NullProgressMonitor());
			mavenModelManager.createMavenModel(pomFile, readMavenModel);
			pomFile.refreshLocal(IResource.DEPTH_ZERO, new NullProgressMonitor());
			this.isAdded = true;
			
		} catch (CoreException e) {
			return new Status(IStatus.ERROR, Activator.PLUGIN_ID,
					"Exception thrown while reading maven pom on project " + project.getName(), e);
		}

		return Status.OK_STATUS;
	}

}
