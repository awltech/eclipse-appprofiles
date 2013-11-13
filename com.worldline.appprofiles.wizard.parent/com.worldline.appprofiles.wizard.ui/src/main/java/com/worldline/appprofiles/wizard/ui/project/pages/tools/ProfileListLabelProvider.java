package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;

/**
 * Label provider applied on wizard profile selection combo. Current
 * implementation returns the name of the profile as per the extension point.
 * 
 * @author mvanbesien
 * 
 */
public class ProfileListLabelProvider extends LabelProvider {

	private static final String ICONS_APPLICATION_GIF = "/icons/application.gif";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		return Activator.getDefault().getImage(ICONS_APPLICATION_GIF);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof ApplicationProfile)
			return ((ApplicationProfile) element).getName();
		return String.valueOf(element);
	}

}
