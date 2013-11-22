package com.worldline.appprofiles.wizard.ui.model.loaders;

import com.worldline.appprofiles.wizard.ui.ProcessMessages;

public class ApplicationConfigurationNotFoundException extends Exception {

	private static final long serialVersionUID = -1797069968731852993L;
	private String configId;
	private String profileId;

	public ApplicationConfigurationNotFoundException(String profileId, String configId) {
		this.profileId = profileId;
		this.configId = configId;
	}

	@Override
	public String getMessage() {
		return ProcessMessages.EXCEPTION_MESSAGE_CONFIGURATION_NOT_FOUND.value(configId, profileId);
	}

}
