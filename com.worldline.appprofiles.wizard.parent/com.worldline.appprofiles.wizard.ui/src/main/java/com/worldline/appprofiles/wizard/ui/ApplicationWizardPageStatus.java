package com.worldline.appprofiles.wizard.ui;

import org.eclipse.jface.dialogs.IMessageProvider;

/**
 * Status Object that contains information about the user input data validity
 * 
 * @author mvanbesien
 * 
 */
public class ApplicationWizardPageStatus {

	/**
	 * Message of the status
	 */
	private String message;

	/**
	 * True if should be possible to go to next page, false otherwise
	 */
	private boolean isValid;

	/**
	 * Type of message. See IMessageProvider constants
	 */
	private int type;

	/**
	 * True if should user Error Message, false if std message is used.
	 */
	private boolean isError;

	/**
	 * Static instance of OK status
	 */
	private static final ApplicationWizardPageStatus OK_STATUS = new ApplicationWizardPageStatus(null, true,
			IMessageProvider.NONE, false);

	/**
	 * Pattern used in toString overridden method.
	 */
	private static final String TOSTRINGPATTERN = "ApplicationWizardPageStatus[message=%s, isValid=%s, type=%s, isError=%s]";

	/**
	 * Returns status message
	 * 
	 * @return
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * True if allows to go to next page, false otherwise
	 * 
	 * @return
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Type of status. See IMessageProvider constants values
	 * 
	 * @return
	 */
	public int getType() {
		return type;
	}

	/**
	 * returns true if should use errorMessage. False uses standard message
	 * 
	 * @return
	 */
	public boolean isError() {
		return isError;
	}

	/*
	 * Creates instance
	 */
	private ApplicationWizardPageStatus(String message, boolean isValid, int type, boolean isError) {
		this.message = message;
		this.isValid = isValid;
		this.type = type;
		this.isError = isError;
	}

	/**
	 * @return OK status
	 */
	public static ApplicationWizardPageStatus ok() {
		return OK_STATUS;
	}

	/**
	 * Information status
	 * 
	 * @param message
	 *            Message to Display
	 * @param isValid
	 *            true to allow user to go to next page, false otherwise
	 * @return
	 */
	public static ApplicationWizardPageStatus info(String message, boolean isValid) {
		return new ApplicationWizardPageStatus(message, isValid, IMessageProvider.INFORMATION, false);
	}

	/**
	 * Error status
	 * 
	 * @param message
	 *            message to display
	 * @return
	 */
	public static ApplicationWizardPageStatus error(String message) {
		return new ApplicationWizardPageStatus(message, false, IMessageProvider.ERROR, false);
	}

	/**
	 * Warning status
	 * 
	 * @param message
	 *            message to display
	 * @param isValid
	 *            true to allow user to go to next page, false otherwise
	 * @return
	 */
	public static ApplicationWizardPageStatus warn(String message, boolean isValid) {
		return new ApplicationWizardPageStatus(message, isValid, IMessageProvider.WARNING, false);
	}

	@Override
	public String toString() {
		return String.format(TOSTRINGPATTERN, message, isValid, type, isError);
	}
}
