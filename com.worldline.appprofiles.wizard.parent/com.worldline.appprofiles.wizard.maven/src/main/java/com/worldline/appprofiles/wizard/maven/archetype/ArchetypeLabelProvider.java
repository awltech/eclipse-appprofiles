/**
 * $Id: ArchetypeLabelProvider.java,v 1.2 2009/11/30 16:41:12 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: ArchetypeLabelProvider.java,v $
 * Revision 1.2  2009/11/30 16:41:12  a125788
 * MVA : Added management of SuperArchetype
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven.archetype;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for archetype's list
 * 
 * @author FFO
 */
public class ArchetypeLabelProvider extends LabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	@Override
	public Image getImage(Object element) {
		if (element instanceof Archetype)
			return ((Archetype) element).getIcon();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof Archetype) {
			Archetype a = (Archetype) element;
			if (a.getName() != null && a.getName().length() > 0)
				return a.getName();
			return a.getArtifactId();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang.Object,
	 *      java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO: This method is probably not needed, since archetypes do not
		// change
		return property.equals("artifactId") || property.equals("version");
	}
}