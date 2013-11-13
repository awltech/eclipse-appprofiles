package com.worldline.appprofiles.wizard.ui.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ApplicationConfiguration {

	private String id;

	private String name;

	private String version;

	private Map<String, AbstractConfigurationEntry> configurationEntries = new LinkedHashMap<String, AbstractConfigurationEntry>();

	private Collection<ChoiceEntriesCombination> combinations = new ArrayList<ChoiceEntriesCombination>();
	
	public ApplicationConfiguration(String id, String name, String version) {
		this.id = id;
		this.name = name;
		this.version = version;
	}

	public void addConfigurationEntry(AbstractConfigurationEntry entry) {
		this.configurationEntries.put(entry.getId(), entry);
	}

	public Collection<AbstractConfigurationEntry> getConfigurationEntries() {
		return configurationEntries.values();
	}

	public AbstractConfigurationEntry getConfigurationEntry(String id) {
		return configurationEntries.get(id);
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}
	
	public Collection<ChoiceEntriesCombination> getOptionalEntriesCombinations() {
		return combinations;
	}

}
