package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.ICheckStateProvider;

import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;

/**
 * Check State Provider, which role is to manage the initial values of check
 * boxes.
 * 
 * @author mvanbesien
 * 
 */
public class ModuleListCheckStateProvider implements ICheckStateProvider {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICheckStateProvider#isChecked(java.lang.Object)
	 */
	@Override
	public boolean isChecked(Object element) {
		if (element instanceof ApplicationModule)
			return ((ApplicationModule) element).isMandatory() || ((ApplicationModule) element).isDefaultSelected();
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ICheckStateProvider#isGrayed(java.lang.Object)
	 */
	@Override
	public boolean isGrayed(Object element) {
		if (element instanceof ApplicationModule)
			return ((ApplicationModule) element).isMandatory();
		return false;
	}

}
