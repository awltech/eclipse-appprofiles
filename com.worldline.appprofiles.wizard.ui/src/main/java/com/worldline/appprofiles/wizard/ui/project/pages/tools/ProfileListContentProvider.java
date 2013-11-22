package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.model.loaders.ApplicationProfilesLoader;

/**
 * Implementation of Content Provider for wizard combo that displays profiles.
 * Current implementation retrieves ApplicationProfiles from Extension point,
 * according to an application nature.
 * 
 * @author mvanbesien
 * 
 */
public class ProfileListContentProvider implements IStructuredContentProvider {

	/**
	 * Nature of the application
	 */
	private String applicationKind;

	/**
	 * Creates Profiles List content provider that retrieves, from Extension
	 * Point, all the profile applications that have this application nature.
	 * 
	 * If no application nature is provided, it returns all application profiles
	 * it can found
	 * 
	 * @param applicationKind
	 */
	public ProfileListContentProvider(String applicationKind) {
		this.applicationKind = applicationKind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface
	 * .viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java
	 * .lang.Object)
	 */
	public Object[] getElements(Object inputElement) {
		List<ApplicationProfile> validApplicationProfiles = new ArrayList<ApplicationProfile>();
		for (ApplicationProfile applicationProfile : ApplicationProfilesLoader.getInstance().getApplicationProfiles()) {
			if (applicationKind == null || applicationKind.equalsIgnoreCase(applicationProfile.getKind().getKey()))
				validApplicationProfiles.add(applicationProfile);
		}
		return validApplicationProfiles.toArray();
	}

}
