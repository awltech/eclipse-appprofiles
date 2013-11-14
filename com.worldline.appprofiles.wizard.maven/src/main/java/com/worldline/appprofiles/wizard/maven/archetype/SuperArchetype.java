package com.worldline.appprofiles.wizard.maven.archetype;

import java.util.ArrayList;
import java.util.List;

/**
 * Super Archetype definition pojo A Super Archetype is compound of an ID and a
 * list of archetypes. This has to be used in order to generate a group of
 * archetypes at once.
 * 
 * @author mvanbesien
 * 
 */
public class SuperArchetype {

	/*
	 * id
	 */
	private String id;

	/*
	 * list of archetypes
	 */
	private List<Archetype> archetypes = new ArrayList<Archetype>();

	public SuperArchetype(String id, List<Archetype> archetypes) {
		this.id = id;
		this.archetypes = archetypes;
	}

	public String getId() {
		return id;
	}

	public List<Archetype> getArchetypes() {
		return archetypes;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setArchetypes(List<Archetype> archetypes) {
		this.archetypes = archetypes;
	}

}
