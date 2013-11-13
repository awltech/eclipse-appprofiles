/**
 * $Id: FilesRemover.java,v 1.2 2008/05/15 06:43:13 tmpffoucart Exp $
 * 
 * HISTORY 
 * -------
 * $Log: FilesRemover.java,v $
 * Revision 1.2  2008/05/15 06:43:13  tmpffoucart
 * FFo: Remove the fetch dependencies task. Dependencies are handled with m2 plugin
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.workarounds;

import java.io.File;

import org.eclipse.core.runtime.IPath;

/**
 * This class is a workaround to a maven-archetype-plugin bug.
 * 
 * Actually, it is not possible to generate empty folders from an Archetype. So,
 * this class parses the file contained under a container folder, and remove all
 * the Archetype ".touch" files.
 * 
 * @see http://jira.codehaus.org/browse/ARCHETYPE-57 for more information about
 *      this bug.
 * 
 */
public class FilesRemover {

	private static final String TOUCH_FILE_NAME = ".touch";

	/**
	 * Remove the .touch file comin form the archetypes.
	 * 
	 * @param container
	 */
	public static void removeTouchFiles(File container) {
		if (container.exists() && container.isDirectory())
			for (File file : container.listFiles()) {
				if (file.isDirectory()) {
					removeTouchFiles(file);
				} else if (file.isFile()) {
					if (file.getName().equals(TOUCH_FILE_NAME))
						file.delete();
				}
			}
	}

	public static void removeTouchFiles(IPath path) {
		removeTouchFiles(new File(path.toOSString()));
	}

}
