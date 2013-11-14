/**
 * $Id: GenerateArchetypeGoal.java,v 1.5 2009/11/30 16:41:12 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: GenerateArchetypeGoal.java,v $
 * Revision 1.5  2009/11/30 16:41:12  a125788
 * MVA : Added management of SuperArchetype
 *
 * Revision 1.4  2009/02/27 12:44:24  a125788
 * MVA : Reusing status from goal execution to get real error when occurs
 *
 * Revision 1.3  2008/10/07 15:38:51  fr20349
 * FFo: Same maven embedder as m2 for executeGoal()
 *
 * Revision 1.2  2008/05/15 06:41:35  tmpffoucart
 * FFo: Change JavaDoc
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.archetype.goals;

import java.util.Map;
import java.util.Properties;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.worldline.appprofiles.wizard.maven.IMavenGoal;
import com.worldline.appprofiles.wizard.maven.MavenLocalRepositoryLocator;
import com.worldline.appprofiles.wizard.maven.archetype.Archetype;
import com.worldline.appprofiles.wizard.maven.embedder.MavenManager;

public class GenerateArchetypeGoal implements IMavenGoal {

	/**
	 * Project Path
	 */
	private IPath baseDir;

	/**
	 * Project Name
	 */
	private Properties properties;

	/**
	 * GroupId Constant for the archetype execution
	 */
	private final static String ARCHETYPE_GROUPID = "org.apache.maven.plugins";

	/**
	 * ArtifactId Constant for the archetype execution
	 */
	private final static String ARCHETYPE_ARTIFACTID = "maven-archetype-plugin";

	/**
	 * Version Constant for the archetype execution
	 */
	private final static String ARCHETYPE_VERSION = "2.2";

	/**
	 * Goal Constant for the archetype execution
	 */
	private final static String ARCHETYPE_GOAL = "generate";

	public GenerateArchetypeGoal(Archetype archetype, IPath baseDir, String groupId, String artifactId, String version,
			String packageName, String description, Map<String, String> userProperties) {
		this.baseDir = baseDir;

		// Set all the properties required to execute an archetype creation
		Properties properties = new Properties();
		properties.setProperty("groupId", groupId);
		properties.setProperty("artifactId", artifactId);
		properties.setProperty("version", version);
		properties.setProperty("theDescription", description);
		/*
		 * TODO Need to find the way to fetch the description value in the
		 * pom.xml template located in archetype
		 */
		properties.setProperty("package", packageName);
		properties.setProperty("archetypeArtifactId", archetype.getArtifactId());
		properties.setProperty("archetypeGroupId", archetype.getGroupId());
		properties.setProperty("basedir", baseDir.makeAbsolute().toOSString());
		// Useful for the archetype plugin version alpha-2.0.3
		properties.setProperty("interactiveMode", "false");

		properties.setProperty("settings.localRepository", MavenLocalRepositoryLocator.getRepoLocation());
		properties.setProperty("archetypeRepository", archetype.getRemoteRepository());

		// set up proxy
		ProxyHelper.applyEclipseProxySettingsForURL(archetype.getRemoteRepository());

		if (archetype.getInstalledVersion().length() > 0) {
			properties.setProperty("archetypeVersion", archetype.getInstalledVersion());
		}

		if (archetype.getRemoteRepository().length() > 0) {
			properties.setProperty("remoteRepositories", archetype.getRemoteRepository());
		}

		if (userProperties != null) {
			for (String userPropertiesKey : userProperties.keySet()) {
				properties.setProperty(userPropertiesKey, String.valueOf(userProperties.get(userPropertiesKey)));
			}

		}
		this.properties = properties;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.atos.xa.common.maven.archetype.goals.IMavenGoal#executeGoal()
	 */
	public IStatus executeGoal() {
		return MavenManager
				.getInstance()
				.getEclipseMaven()
				.executeGoal(
						this.baseDir,
						GenerateArchetypeGoal.ARCHETYPE_GROUPID + ":" + GenerateArchetypeGoal.ARCHETYPE_ARTIFACTID
								+ ":" + GenerateArchetypeGoal.ARCHETYPE_VERSION + ":"
								+ GenerateArchetypeGoal.ARCHETYPE_GOAL, this.properties, new NullProgressMonitor());

	}

}
