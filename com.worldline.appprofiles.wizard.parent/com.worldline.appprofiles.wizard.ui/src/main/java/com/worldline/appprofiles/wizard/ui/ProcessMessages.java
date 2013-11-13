package com.worldline.appprofiles.wizard.ui;

import java.util.ResourceBundle;

import java.text.MessageFormat;

public enum ProcessMessages {
	LOAD_PROCESS_STARTED, INCOMPATIBILITY_REMATCHED, INCOMPATIBILITY_EXCEPTION, REQUIREMENT_REMATCHED, REQUIREMENT_EXCEPTION, PROFILE_CREATED, PROFILE_EXCEPTION, PROFILE_EXCEPTION2, CONF_REFERENCE_CREATED, CONF_REFERENCE_EXCEPTION, MODULE_CREATED, MODULE_CREATED2, MODULE_EXCEPTION, LOCATION_LOADED, NO_LOCATION, REQUIREMENT_PRELOADED, REQUIREMENT_PRELOAD_ERROR, INCOMPATIBILITY_PRELOADED, INCOMPATIBILITY_PRELOAD_ERROR, CONFIGURATION_CREATED, CHOICE_CONFENTRY_LOADED, VALUE_CONFENTRY_LOADED, MANDATORY_CONFENTRY_LOADED, CONFIGURATION_EXCEPTION, EXCEPTION_MESSAGE_CONFIGURATION_NOT_FOUND, EXCEPTION_MESSAGE_MODULE_NOT_FOUND, EXCEPTION_MESSAGE_NULL_ARGUMENT, EXCEPTION_MESSAGE_NULL_EXECUTABLE, MODULE_PREFIX_STORED, MODULE_SUFFIX_STORED, ERROR_LOCATION_NOTSUPPORTED, ERROR_CONFIGURATION_NOTSUPPORTED, PROFILE_DOCUMENTATION, OPTIONAL_ENTRY_NOT_FOUND_EXCEPTION, OPTIONAL_ENTRIES_COMBINATION_CREATED, PACK_SUFFIX_STORED, GLOBAL_CONFIGURATOR, VERSION_RESOLVED
	;
	
	/*
	 * ResourceBundle instance
	 */
	private static ResourceBundle resourceBundle = ResourceBundle.getBundle("ProcessMessages");
	
	/*
	 * Returns value of the message
	 */ 
	public String value() {
		if (ProcessMessages.resourceBundle == null || !ProcessMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return ProcessMessages.resourceBundle.getString(this.name());
	}
	
	/*
	 * Returns value of the formatted message
	 */ 
	public String value(Object... args) {
		if (ProcessMessages.resourceBundle == null || !ProcessMessages.resourceBundle.containsKey(this.name()))
			return "!!"+this.name()+"!!";
		
		return MessageFormat.format(ProcessMessages.resourceBundle.getString(this.name()), args);
	}
	
}
