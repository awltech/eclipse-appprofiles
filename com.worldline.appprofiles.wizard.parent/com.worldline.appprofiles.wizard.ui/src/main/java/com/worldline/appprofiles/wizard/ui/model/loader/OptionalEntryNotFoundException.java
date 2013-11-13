package com.worldline.appprofiles.wizard.ui.model.loader;

import com.worldline.appprofiles.wizard.ui.ProcessMessages;

public class OptionalEntryNotFoundException extends Exception {

	private static final long serialVersionUID = -1001423877317042968L;
	private String referenceID;
	private String configurationID;

	public OptionalEntryNotFoundException(String referenceID, String configurationID) {
		this.referenceID = referenceID;
		this.configurationID = configurationID;
	}

	@Override
	public String getMessage() {
		return ProcessMessages.OPTIONAL_ENTRY_NOT_FOUND_EXCEPTION.value(referenceID, configurationID);
	}

}
