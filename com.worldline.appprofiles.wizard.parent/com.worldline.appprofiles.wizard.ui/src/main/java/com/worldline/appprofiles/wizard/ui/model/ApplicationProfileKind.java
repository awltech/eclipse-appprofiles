package com.worldline.appprofiles.wizard.ui.model;

/**
 * Contains all the possible application profile kinds, as defined in the
 * extension point possible values.
 * 
 * @author mvanbesien
 * 
 */
public enum ApplicationProfileKind {

	STANDARD("standard"), SAMPLE("sample");

	private String key;

	private ApplicationProfileKind(String key) {
		this.key = key;
	}

	public static ApplicationProfileKind fromKey(String key) {
		if (key == null)
			return null;
		if (key.equalsIgnoreCase(STANDARD.key))
			return STANDARD;
		if (key.equalsIgnoreCase(SAMPLE.key))
			return SAMPLE;
		return null;
	}
	
	public String getKey() {
		return key;
	}
}
