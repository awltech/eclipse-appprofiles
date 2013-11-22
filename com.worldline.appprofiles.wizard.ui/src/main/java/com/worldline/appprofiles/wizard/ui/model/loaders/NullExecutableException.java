package com.worldline.appprofiles.wizard.ui.model.loaders;

import com.worldline.appprofiles.wizard.ui.ProcessMessages;

public class NullExecutableException extends Exception {

	private static final long serialVersionUID = -7388320643059281756L;
	private String value;
	private String name;

	public NullExecutableException(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getMessage() {
		return ProcessMessages.EXCEPTION_MESSAGE_NULL_EXECUTABLE.value(value, name);
	}

}
