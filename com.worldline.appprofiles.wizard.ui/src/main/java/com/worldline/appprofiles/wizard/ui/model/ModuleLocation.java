package com.worldline.appprofiles.wizard.ui.model;

import java.util.HashMap;
import java.util.Map;

public abstract class ModuleLocation {

	private Map<String, String> properties = new HashMap<String, String>();

	public Map<String, String> getProperties() {
		return properties;
	}
}
