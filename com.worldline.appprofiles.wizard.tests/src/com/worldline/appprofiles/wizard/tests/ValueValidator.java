package com.worldline.appprofiles.wizard.tests;

import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.model.facade.IValueValidator;

public class ValueValidator implements IValueValidator {

	@Override
	public String errorMessage(ValueConfigurationEntry entry, Object value) {
		return String.format("Value for %s cannot contain spaces.", entry.getMessage());
	}

	@Override
	public boolean validate(Object value) {
		String s = String.valueOf(value);
		return s.indexOf(" ") == -1;
	}

}
