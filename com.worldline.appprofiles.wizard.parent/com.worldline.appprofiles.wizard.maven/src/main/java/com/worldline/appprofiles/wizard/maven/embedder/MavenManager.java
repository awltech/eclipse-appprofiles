/**
 * $Id: MavenManager.java,v 1.4 2009/02/09 09:49:57 fr20349 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: MavenManager.java,v $
 * Revision 1.4  2009/02/09 09:49:57  fr20349
 * FFO: Move back to the 0.9.4 m2Eclipse due to quality issues.
 *
 * Revision 1.2  2008/10/07 15:39:14  fr20349
 * FFo: Same maven embedder as m2
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.embedder;

import org.eclipse.m2e.core.MavenPlugin;
import org.eclipse.m2e.core.embedder.IMaven;

import com.worldline.appprofiles.wizard.maven.Activator;
import com.worldline.appprofiles.wizard.maven.embedder.internal.EclipseMaven;

/**
 * Utility class for accessing the maven objects.
 * 
 * This class is a thin wrapper / shortcut for the methods in {@link Activator},
 * and it is the preferred way for accessing the maven objects.
 * 
 * @author A&M
 */
public class MavenManager {

	private static final class SingletonHolder {
		static MavenManager instance = new MavenManager();
	}

	private MavenManager() {
	}

	public static MavenManager getInstance() {
		return SingletonHolder.instance;
	}

	private EclipseMaven mavenInstance = new EclipseMaven();

	/**
	 * Get the active maven instance.
	 * 
	 * @return the maven instance.
	 */
	public static IMaven getMavenEmbedder() {
		return MavenPlugin.getMaven();
	}

	/**
	 * @return
	 */
	public EclipseMaven getEclipseMaven() {
		return this.mavenInstance;
	}

}
