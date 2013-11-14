package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.worldline.appprofiles.wizard.ui.model.ApplicationModule;
import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;

/**
 * Content provider that fills checkable table when profile is selected. Current
 * selection lists all the modules of the selected Application profile.
 * 
 * @author mvanbesien
 * 
 */
public class ModuleListContentProvider implements IStructuredContentProvider, ITreeContentProvider {

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
	 */
	@Override
	public Object[] getElements(Object inputElement) {
		if (inputElement instanceof ApplicationProfile){
			return ((ApplicationProfile) inputElement).getModules().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof ApplicationProfile){
			return ((ApplicationProfile) parentElement).getModules().toArray();
		}
		else if(parentElement instanceof ApplicationModule){
			return ((ApplicationModule) parentElement).getSubModules().toArray();
		}
		return new Object[0];
	}

	@Override
	public Object getParent(Object element) {
//		if (element instanceof ApplicationProfile){
//			return null;
//		}
//		else if(element instanceof ApplicationModule){
//			ApplicationProfile parentProfile = ((ApplicationModule) element).getParentProfile();
//			
//			if(parentProfile == null){
//				return ((ApplicationModule) element).getParentModule();
//			}
//			return parentProfile;
//		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if (element instanceof ApplicationProfile){
			
		}
		else if(element instanceof ApplicationModule){
			ApplicationModule module = (ApplicationModule) element;
			List<ApplicationModule> subModules = module.getSubModules();
			return subModules != null && subModules.size() > 0;
		}
		return false;
	}

}
