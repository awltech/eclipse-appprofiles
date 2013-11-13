package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Item;

import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Implementation of cell editor, in charge of managing user changed for Text
 * Table editor
 * 
 * @author mvanbesien
 * 
 */
public class ValueConfigCellModifier implements ICellModifier {

	/**
	 * Output to manage
	 */
	private ApplicationWizardOutput output;

	/**
	 * Viewer to refresh after use
	 */
	private TableViewer viewer;

	private ValueConfigCellEditor cellEditor;

	/**
	 * Key corresponding to the name of editable column
	 */
	private static final String VALUE_COLUMN_PROPERTY = "value";

	/**
	 * Creates new Cell Editor for output and table viewer.
	 * 
	 * @param output
	 * @param viewer
	 */
	public ValueConfigCellModifier(ApplicationWizardOutput output, TableViewer viewer) {
		this.output = output;
		this.viewer = viewer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#modify(java.lang.Object,
	 * java.lang.String, java.lang.Object)
	 */
	@Override
	public void modify(Object element, String property, Object value) {
		if (element instanceof Item)
			element = ((Item) element).getData();
		if (VALUE_COLUMN_PROPERTY.equals(property) && element instanceof ValueConfigurationEntry) {
			if (value != null) {
				ValueConfigurationEntry entry = (ValueConfigurationEntry) element;

				if (output.getValueSelectedConfiguration().containsKey(entry)) {
					if (value.equals(output.getValueSelectedConfiguration().get(entry)))
						return;
					output.getValueSelectedConfiguration().remove(entry);
				}
				output.getValueSelectedConfiguration().put(entry, String.valueOf(value));
				viewer.refresh(true);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#getValue(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public Object getValue(Object element, String property) {
		if (VALUE_COLUMN_PROPERTY.equals(property) && element instanceof ValueConfigurationEntry) {
			ValueConfigurationEntry entry = (ValueConfigurationEntry) element;
			String userValue = output.getValueSelectedConfiguration().get(element);
			if (cellEditor != null)
				cellEditor.notifyObjectModified(entry);
			return userValue != null ? userValue : entry.getDefaultValue();
			
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellModifier#canModify(java.lang.Object,
	 * java.lang.String)
	 */
	@Override
	public boolean canModify(Object element, String property) {
		return VALUE_COLUMN_PROPERTY.equals(property);
	}

	public void setCellEditor(ValueConfigCellEditor cellEditor) {
		this.cellEditor = cellEditor;
	}

}
