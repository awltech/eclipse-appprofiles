package com.worldline.appprofiles.wizard.ui.model.facade;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface IValueConfigurator {

	public IStatus configure(IProject project, String value, IProgressMonitor monitor, Map<String, String> properties);

}
