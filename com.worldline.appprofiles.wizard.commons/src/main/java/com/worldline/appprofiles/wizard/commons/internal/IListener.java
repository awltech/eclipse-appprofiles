package com.worldline.appprofiles.wizard.commons.internal;

/**
 * Generic interface for listener systems
 * 
 * @author mvanbesien/ahavez
 * 
 */
public interface IListener<E extends IEvent> {

	/**
	 * Called when a specific action is executed.
	 * 
	 * @param event
	 */
	public void onEvent(E event);

}
