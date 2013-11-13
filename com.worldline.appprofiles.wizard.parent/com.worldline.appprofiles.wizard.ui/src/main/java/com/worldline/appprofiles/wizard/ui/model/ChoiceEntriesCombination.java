package com.worldline.appprofiles.wizard.ui.model;

import java.util.HashSet;
import java.util.Set;

import com.worldline.appprofiles.wizard.ui.model.facade.IConfigurator;

public class ChoiceEntriesCombination {

	private IConfigurator configurator;

	private String variable;
	
	private Set<ChoiceConfigurationEntry> optionalConfigurationEntries = new HashSet<ChoiceConfigurationEntry>();

	public String getVariable() {
		return variable;
	}
	
	public void setVariable(String variable) {
		this.variable = variable;
	}
	
	public ChoiceEntriesCombination(IConfigurator configurator) {
		this.configurator = configurator;
	}
	
	public IConfigurator getConfigurator() {
		return configurator;
	}
	
	public Set<ChoiceConfigurationEntry> getOptionalConfigurationEntries() {
		return optionalConfigurationEntries;
	}
	
}
