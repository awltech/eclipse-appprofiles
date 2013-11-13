package com.worldline.appprofiles.wizard.ui.project.pages.components;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;

/**
 * Added Viewer sorter. Lists elements in alphabetical order by default.
 * Maybe changed if necessary, so as to get categories
 * 
 * @author mvanbesien
 *
 */
public class ProfileListViewerSorter extends ViewerSorter {

	@Override
	public int category(Object element) {
		return super.category(element);
	}
	
	@Override
	public void sort(Viewer viewer, Object[] elements) {
		super.sort(viewer, elements);
	}
	
}
