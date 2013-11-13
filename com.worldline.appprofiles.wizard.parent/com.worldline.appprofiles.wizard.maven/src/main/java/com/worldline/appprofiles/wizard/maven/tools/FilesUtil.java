package com.worldline.appprofiles.wizard.maven.tools;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.worldline.appprofiles.wizard.maven.Activator;


public class FilesUtil {
	public static IStatus remove(String resourceName, IProject project){
		IResource foundMember = project.findMember(resourceName);
		if(foundMember != null){
			try {
				foundMember.delete(true, null);
			}
			catch (CoreException e) {
				return new Status(Status.ERROR, Activator.PLUGIN_ID, 
						"Was not able to remove " + resourceName + " - you can remove it manually", e);
			}
		}
		return Status.OK_STATUS;
	}
}
