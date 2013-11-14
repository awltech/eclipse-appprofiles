package com.worldline.appprofiles.wizard.ui.model.facade;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface ISelectionConfigurator {
	
	public IStatus configure(IProject project, boolean selection, IProgressMonitor monitor, Map<String, String> properties);

}
