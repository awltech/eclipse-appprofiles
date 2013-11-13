package com.worldline.appprofiles.wizard.tests;

import java.util.Map;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.worldline.appprofiles.wizard.ui.model.facade.IGlobalConfigurator;

public class CommonGlobalConfigurator implements IGlobalConfigurator {

	public CommonGlobalConfigurator() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public IStatus configure(IProgressMonitor monitor, Map<String, String> properties) {
		//System.out.println("Doing some global stuff for "+applicationName+" at "+applicationPath.toString());
		return Status.OK_STATUS;
	}

}
