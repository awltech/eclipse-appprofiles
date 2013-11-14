package com.worldline.appprofiles.wizard.ui.model.loader;

public class BundleNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -94181643988206452L;
	
	private String bundleName;

	public BundleNotFoundException(String bundleName) {
		this.bundleName = bundleName;
	}

	@Override
	public String getMessage() {
		return "Could not resolve Bundle with ID: " + this.bundleName;
	}

}
