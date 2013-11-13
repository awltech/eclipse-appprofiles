package com.worldline.appprofiles.wizard.ui.model.facade;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface IConfigurator {
	
	public IStatus configure(IProject project, IProgressMonitor monitor, Map<String, String> properties);
}
