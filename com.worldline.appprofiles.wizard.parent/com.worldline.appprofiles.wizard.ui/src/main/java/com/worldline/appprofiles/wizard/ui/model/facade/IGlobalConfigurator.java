package com.worldline.appprofiles.wizard.ui.model.facade;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;

public interface IGlobalConfigurator {

	public IStatus configure(IProgressMonitor monitor, Map<String, String> properties);

}
