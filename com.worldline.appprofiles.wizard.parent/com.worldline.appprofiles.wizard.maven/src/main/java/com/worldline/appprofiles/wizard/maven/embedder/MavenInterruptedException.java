/**
 * $Id: MavenInterruptedException.java,v 1.1.1.1 2008/05/07 15:47:00 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: MavenInterruptedException.java,v $
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.embedder;

/**
 * Exception thrown when a Maven execution job is interrupted.
 */
public class MavenInterruptedException extends RuntimeException {

	private static final long serialVersionUID = -4253194743593618184L;

	public MavenInterruptedException() {
		super();
	}

	public MavenInterruptedException(String message) {
		super(message);
	}
}