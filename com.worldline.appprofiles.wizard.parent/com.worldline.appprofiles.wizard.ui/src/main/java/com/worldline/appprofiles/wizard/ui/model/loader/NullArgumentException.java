package com.worldline.appprofiles.wizard.ui.model.loader;

import com.worldline.appprofiles.wizard.ui.ProcessMessages;

public class NullArgumentException extends Exception {

	private static final long serialVersionUID = -3674173092264804620L;
	private String value;
	private String name;

	public NullArgumentException(String name, String value) {
		this.name = name;
		this.value = value;
	}

	@Override
	public String getMessage() {
		return ProcessMessages.EXCEPTION_MESSAGE_NULL_ARGUMENT.value(value, name);
	}

}
