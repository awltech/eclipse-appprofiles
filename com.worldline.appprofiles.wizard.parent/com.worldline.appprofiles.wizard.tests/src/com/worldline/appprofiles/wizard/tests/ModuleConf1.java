package com.worldline.appprofiles.wizard.tests;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;

public class ModuleConf1 implements IConfigurator {

	public ModuleConf1() {
	}

	@Override
	public IStatus configure(IProject project, IProgressMonitor monitor, Map<String, String> properties) {
		System.out.println("Yay ! Configuration executed on project "+project.getName());
		return Status.OK_STATUS;
	}

}
