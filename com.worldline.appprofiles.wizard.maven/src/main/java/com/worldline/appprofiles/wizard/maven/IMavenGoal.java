/**
 * $Id: IMavenGoal.java,v 1.2 2009/02/27 12:44:24 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: IMavenGoal.java,v $
 * Revision 1.2  2009/02/27 12:44:24  a125788
 * MVA : Reusing status from goal execution to get real error when occurs
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven;

import org.eclipse.core.runtime.IStatus;

/**
 * 
 * Interface for defining a Maven goal execution action
 * 
 * @author mvanbesien
 * @version $Revision: 1.2 $
 * @since $Date: 2009/02/27 12:44:24 $
 * 
 */
public interface IMavenGoal {

	/**
	 * Executes the goal
	 * 
	 */
	public IStatus executeGoal();

}
