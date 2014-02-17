/**
 * $Id: MavenLocalRepositoryLocator.java,v 1.2 2009/02/27 13:41:54 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: MavenLocalRepositoryLocator.java,v $
 * Revision 1.2  2009/02/27 13:41:54  a125788
 * MVA : Moved Message class to upper level
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven;

import java.io.File;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * This tool retrieves the Maven Default local repository location, and manages
 * this value according to Eclipse ClassPath Variables
 * 
 * @author mvanbesien
 * @version $Revision: 1.2 $
 * @since $Date: 2009/02/27 13:41:54 $
 * 
 */
public class MavenLocalRepositoryLocator {

	/**
	 * Constant for the maven directory name for the local repository
	 */
	private static final String MVNREPO_ROOT_DIRECTORY = ".m2";

	/**
	 * Constant
	 */
	private static final String MVNREPO_NAME = "M2_REPO";

	/**
	 * Constant
	 */
	private static final String USERPROFILE_PROPERTY = "USERPROFILE";

	/**
	 * Constant
	 */
	private static final String MVNREPO_PATH_SEGMENT2 = "repository";

	/**
	 * @return configured location of Maven repository
	 */
	public static String getRepoLocation() {
		IPath iPath = JavaCore.getClasspathVariable(MavenLocalRepositoryLocator.MVNREPO_NAME);
		return iPath != null ? iPath.toOSString() : null;
	}

	/**
	 * Retrieves the Maven default Repository Location. If this location cannot
	 * be computed, the default path passed as parameter is used.
	 * 
	 * @param defaultPath
	 *            : Default path
	 * @throws JavaModelException
	 *             : Thrown when classpath variable definition fails
	 */
	public static void setRepoLocation(String mavenSettingsLocalRepo, boolean force) throws JavaModelException {

		if (getRepoLocation() == null || force) {

			String pathVar = System.getenv(MavenLocalRepositoryLocator.MVNREPO_NAME);

			if (mavenSettingsLocalRepo != null && new File(mavenSettingsLocalRepo).exists())
				// Settings from Maven settings
				JavaCore.setClasspathVariable(MVNREPO_NAME, new Path(mavenSettingsLocalRepo), new NullProgressMonitor());
			else if (pathVar != null) {
				// Setting from variable system
				org.eclipse.jdt.core.JavaCore.setClasspathVariable(MavenLocalRepositoryLocator.MVNREPO_NAME, new Path(
						pathVar), new NullProgressMonitor());

			} else {
				String userLocation = System.getenv(MavenLocalRepositoryLocator.USERPROFILE_PROPERTY);
				if (userLocation != null) {
					// Default setting
					IPath userPath = new Path(userLocation);
					userPath = userPath.append(MavenLocalRepositoryLocator.MVNREPO_ROOT_DIRECTORY);
					userPath = userPath.append(MavenLocalRepositoryLocator.MVNREPO_PATH_SEGMENT2);
					JavaCore.setClasspathVariable(MavenLocalRepositoryLocator.MVNREPO_NAME, userPath,
							new NullProgressMonitor());
				}
			}
		}
	}
}
