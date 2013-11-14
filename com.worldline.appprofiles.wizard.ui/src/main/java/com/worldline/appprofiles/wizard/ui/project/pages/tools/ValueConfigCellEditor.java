package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.widgets.Composite;

import com.worldline.appprofiles.wizard.ui.model.ValueConfigurationEntry;

/**
 * Extended Text Cell Modifier. Extension done so external element can notify
 * about the modified ValueConfigurationEntry, so it can be validated from
 * validator brought in extension point.
 * 
 * @author mvanbesien
 * 
 */
public abstract class ValueConfigCellEditor extends TextCellEditor {

	/**
	 * * @see TextCellEditor()
	 */
	public ValueConfigCellEditor() {
		super();
	}

	/**
	 * @see TextCellEditor(Composite)
	 * @param composite
	 */
	public ValueConfigCellEditor(Composite composite) {
		super(composite);
	}

	/**
	 * @see TextCellEditor(Composite, int)
	 * @param composite
	 * @param style
	 */
	public ValueConfigCellEditor(Composite composite, int style) {
		super(composite, style);
	}

	/**
	 * Entry being modified
	 */
	private ValueConfigurationEntry objectModified = null;

	/**
	 * Update the modified object
	 * 
	 * @param objectModified
	 */
	public void notifyObjectModified(ValueConfigurationEntry objectModified) {
		this.objectModified = objectModified;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#isCorrect(java.lang.Object)
	 */
	@Override
	protected boolean isCorrect(Object value) {
		if (objectModified == null || objectModified.getValidator() == null)
			return super.isCorrect(value);

		if (!objectModified.getValidator().validate(value)) {
			setErrorMessage(objectModified.getValidator().errorMessage(objectModified, value));
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.TextCellEditor#editOccured(org.eclipse.swt.
	 * events.ModifyEvent)
	 */
	@Override
	protected void editOccured(ModifyEvent e) {
		super.editOccured(e);
		runValidationCallback();
	}

	/**
	 * Callback created so the caller can run the validation and display
	 * message.
	 */
	public abstract void runValidationCallback();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellEditor#focusLost()
	 */
	@Override
	protected void focusLost() {
		// If validation fails & focus lost, value will not be saved. Hence, it
		// comes back to initial value and is not on error anymore.
		setErrorMessage(null);
		runValidationCallback();
	}

}
