package com.worldline.appprofiles.wizard.ui.model;

import java.util.UUID;

public abstract class AbstractConfigurationEntry {

	private UUID uuid = UUID.randomUUID();

	private String id;
	
	private String variable;

	
	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public String getId() {
		return id;
	}

	public AbstractConfigurationEntry(String id) {
		this.id = id;
	}

	@Override
	public final boolean equals(Object obj) {
		return obj instanceof AbstractConfigurationEntry && ((AbstractConfigurationEntry) obj).uuid.equals(this.uuid);
	}
	
	@Override
	public final int hashCode() {
		return uuid.toString().hashCode();
	}

}
