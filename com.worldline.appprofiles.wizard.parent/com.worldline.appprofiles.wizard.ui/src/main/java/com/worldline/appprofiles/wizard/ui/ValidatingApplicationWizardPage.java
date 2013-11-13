/**
 * $Id: ValidatingApplicationWizardPage.java,v 1.2 2008/07/03 13:32:06 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: ValidatingApplicationWizardPage.java,v $
 * Revision 1.2  2008/07/03 13:32:06  a125788
 * MVA : Wizard is finishable only when last page is displayed at least once
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.wizard.WizardPage;

import com.worldline.appprofiles.wizard.ui.project.ApplicationWizard;

public abstract class ValidatingApplicationWizardPage extends WizardPage {
	private String errorString;

	public ValidatingApplicationWizardPage(String name) {
		super(name);
		this.setImageDescriptor(Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID,
				"/icons/mvn_proj_wizban.png"));
	}

	public String getError() {
		return errorString;
	}

	protected void setError(String errorString) {
		this.errorString = errorString;
		setErrorMessage(getError());
	}
	
	@Override
	public void setMessage(String newMessage, int newType) {
		super.setMessage(newMessage, newType);
	}

	protected boolean validate() {
		ApplicationWizardPageStatus status = isPageValid();
		if (status.isError()) {
			setErrorMessage(status.getMessage());
			setMessage(null, IMessageProvider.NONE);
		} else {
			setErrorMessage(null);
			setMessage(status.getMessage(), status.getType());
		}

		if (status.isValid())
			onPageValidated();
		
		setPageComplete(status.isValid());

		return status.isValid();
	}

	protected ApplicationWizardPageStatus isPageValid() {
		return ApplicationWizardPageStatus.ok();
	}

	protected void onPageValidated() {
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible && this.getWizard() instanceof ApplicationWizard)
			((ApplicationWizard) this.getWizard()).setPageAsVisited(this);
		super.setVisible(visible);
	}
}