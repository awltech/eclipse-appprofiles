package com.worldline.appprofiles.wizard.ui.model.loaders;

import com.worldline.appprofiles.wizard.ui.ProcessMessages;

public class ApplicationModuleNotFoundException extends Exception {

	private String moduleId;

	public ApplicationModuleNotFoundException(String moduleId) {
		this.moduleId = moduleId;
	}

	private static final long serialVersionUID = -2466782244166609879L;

	@Override
	public String getMessage() {
		return ProcessMessages.EXCEPTION_MESSAGE_MODULE_NOT_FOUND.value(moduleId);
	}
	
}
