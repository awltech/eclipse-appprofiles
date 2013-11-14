
package com.worldline.appprofiles.wizard.maven.archetype.goals;

import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.worldline.appprofiles.wizard.maven.IMavenGoal;
import com.worldline.appprofiles.wizard.maven.MavenLocalRepositoryLocator;
import com.worldline.appprofiles.wizard.maven.embedder.MavenManager;

/**
 * Generate a maven goal of a pom file
 * @author A159123
 *
 */
public class GenerateMavenGoal implements IMavenGoal {

	/**
	 * Project Path
	 */
	private IPath baseDir;

	/**
	 * Project Name
	 */
	private Properties properties;
	
	/**
	 * Maven target
	 */
	private String goal;
	
	/**
	 * Pom on which to execute the maven operation
	 */
	private String pomFile;

	public GenerateMavenGoal(IPath baseDir, String goal, String pomFile) {
		this.baseDir = baseDir;
		this.goal = goal;
		this.pomFile = pomFile;

		// Set all the properties required to execute the maven goal
		Properties properties = new Properties();
		properties.setProperty("basedir", baseDir.makeAbsolute().toOSString());
		properties.setProperty("interactiveMode", "false");
		properties.setProperty("settings.localRepository", MavenLocalRepositoryLocator.getRepoLocation());
			
		this.properties = properties;
	}

	
	public IStatus executeGoal() {
		return MavenManager.getInstance().getEclipseMaven().executeGoal(
				this.baseDir,
				this.goal,
				this.pomFile,
				this.properties,
				new NullProgressMonitor());
	}
}
