package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Content provider that gives labels for the value configuration entries table.
 * 
 * @author mvanbesien
 * 
 */
public class ValueConfigLabelProvider implements ITableLabelProvider {

	private static final String ICONS_VALUE_CONFIG_GIF = "/icons/valueConfig.gif";
	private ApplicationWizardOutput output;

	public ValueConfigLabelProvider(ApplicationWizardOutput output) {
		this.output = output;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#addListener(org.eclipse.
	 * jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void addListener(ILabelProviderListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.IBaseLabelProvider#dispose()
	 */
	@Override
	public void dispose() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#isLabelProperty(java.lang
	 * .Object, java.lang.String)
	 */
	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.IBaseLabelProvider#removeListener(org.eclipse
	 * .jface.viewers.ILabelProviderListener)
	 */
	@Override
	public void removeListener(ILabelProviderListener listener) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang
	 * .Object, int)
	 */
	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return columnIndex == 0 ? Activator.getDefault().getImage(ICONS_VALUE_CONFIG_GIF): null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang
	 * .Object, int)
	 */
	@Override
	public String getColumnText(Object element, int columnIndex) {
		if (element instanceof ValueConfigurationEntry) {
			ValueConfigurationEntry entry = (ValueConfigurationEntry) element;
			if (columnIndex == 0)
				return entry.getMessage();
			if (columnIndex == 1) {
				return output.getValueSelectedConfiguration().containsKey(entry) ? output
						.getValueSelectedConfiguration().get(entry) : entry.getDefaultValue();
			}
		}
		return null;
	}
}
