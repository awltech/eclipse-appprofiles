package com.worldline.appprofiles.wizard.tests;

import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.worldline.appprofiles.wizard.ui.model.facade.IValueConfigurator;

public class EditableConfigurator2 implements IValueConfigurator {

	@Override
	public IStatus configure(IProject project, String value, IProgressMonitor monitor, Map<String, String> properties) {
		System.out.println("Titi: "+value);
		return Status.OK_STATUS;
	}

}
