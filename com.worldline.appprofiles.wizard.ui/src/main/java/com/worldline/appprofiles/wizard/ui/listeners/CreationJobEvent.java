package com.worldline.appprofiles.wizard.ui.listeners;

import com.worldline.appprofiles.wizard.commons.listeners.internal.IEvent;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * 
 * Event implementation, used when wizard is created
 * 
 * @author ahavez
 * @version 1.0
 * 
 */
public class CreationJobEvent implements IEvent{
	// TO BE COMPLETED WITH OTHER INFORMATION IF NEEDED
	private ApplicationWizardOutput outPut;
	
	protected ApplicationWizardOutput getOutput() {
		return this.outPut;
	}
	public CreationJobEvent(ApplicationWizardOutput outPut){
		this.outPut = outPut;
	}
}
