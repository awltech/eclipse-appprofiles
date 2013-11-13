package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.jface.viewers.ContentViewer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.widgets.Label;

import com.worldline.appprofiles.wizard.ui.model.ApplicationProfile;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;

/**
 * Listener in charge of updating modules table when user changes profile
 * selection
 * 
 * @author mvanbesien
 * 
 */
public abstract class ProfileListSelectionChangedListener implements ISelectionChangedListener {

	/**
	 * Table Viewer to update
	 */
	private ContentViewer viewer;

	/**
	 * Output object to update
	 */
	private ApplicationWizardOutput applicationWizardOutput;

	/**
	 * Label containing profile information
	 */
	private Label descriptionLabel;

	/**
	 * Creates new listener
	 * 
	 * @param viewer
	 * @param applicationWizardOutput
	 * @param descriptionLabel 
	 */
	public ProfileListSelectionChangedListener(ContentViewer viewer, ApplicationWizardOutput applicationWizardOutput, Label descriptionLabel) {
		this.viewer = viewer;
		this.applicationWizardOutput = applicationWizardOutput;
		this.descriptionLabel = descriptionLabel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(
	 * org.eclipse.jface.viewers.SelectionChangedEvent)
	 */
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		if (event.getSelection() instanceof IStructuredSelection) {
			Object o = ((IStructuredSelection) event.getSelection()).getFirstElement();
			if (o instanceof ApplicationProfile && this.viewer != null) {
				ApplicationProfile ap = (ApplicationProfile) o;
				this.viewer.setInput(ap);
				String description = ap.getDescription();
				this.descriptionLabel.setText(description == null ? "" : description);
				this.descriptionLabel.update();
				this.applicationWizardOutput.resetApplicationProfile(ap);
			}
		}
		validate();
	}

	protected abstract void validate();
	
}
