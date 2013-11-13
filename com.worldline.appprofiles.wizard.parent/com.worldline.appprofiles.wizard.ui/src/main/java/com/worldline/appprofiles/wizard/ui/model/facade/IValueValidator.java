package com.worldline.appprofiles.wizard.ui.model.facade;

import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;

public interface IValueValidator {

	public String errorMessage(ValueConfigurationEntry entry, Object value);

	public boolean validate(Object value);

}
