package com.worldline.appprofiles.wizard.ui.project.pages.components;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkingSet;
import org.eclipse.ui.IWorkingSetManager;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.IWorkingSetSelectionDialog;

import com.worldline.appprofiles.wizard.ui.FormDataBuilder;

/**
 * Some wizard zone in eclipse, in charge of managing working sets
 * @author mvanbesien
 *
 */
public class WorkingSetSelectionComponent extends Composite {

	/**
	 * True if checkbox for "put in Working set" is on
	 */
	private boolean workingSetEnabled = false;

	/**
	 * User-selected working sets
	 */
	private IWorkingSet[] selectedWorkingSets = new IWorkingSet[0];

	/**
	 * Enable WE usage button
	 */
	private final Button enableButton;

	/**
	 * Working set list label
	 */
	private final Label label;

	/**
	 * Working set list selection button
	 */
	private final Button selectButton;

	/**
	 * Working set list value
	 */
	private final Label text;

	/**
	 * Listener that calls parent validation
	 */
	private SelectionListener validationSelectionListener;

	/**
	 * Creates new Working set selection component in parent with style
	 * @param parent
	 * @param style
	 */
	public WorkingSetSelectionComponent(Composite parent, int style) {
		// Initialize composite
		super(parent, style);
		this.setLayout(new FormLayout());

		// We create the parent group
		final Group parentGroup = new Group(this, SWT.NONE);
		parentGroup.setText(WorkingSetSelectionComponentMessages.GROUP_NAME.value());
		new FormDataBuilder().fill().apply(parentGroup);
		parentGroup.setLayout(new FormLayout());

		// We create the checkbox button
		this.enableButton = new Button(parentGroup, SWT.CHECK);
		this.enableButton.setText(WorkingSetSelectionComponentMessages.ENABLE_BUTTON_TEXT.value());
		new FormDataBuilder().top().left().right().apply(this.enableButton);
		this.enableButton.setSelection(this.workingSetEnabled);

		// Here is the label for the WS selection
		this.label = new Label(parentGroup, SWT.NONE);
		this.label.setText(WorkingSetSelectionComponentMessages.SELECTED_WORKING_SETS_LABEL_TEXT.value());
		new FormDataBuilder().top(this.enableButton).left().width(80).apply(this.label);
		this.label.setEnabled(this.workingSetEnabled);

		// Here is the selection button
		this.selectButton = new Button(parentGroup, SWT.PUSH);
		this.selectButton.setText(WorkingSetSelectionComponentMessages.SELECTED_WORKING_SETS_BUTTON_TEXT.value());
		new FormDataBuilder().top(this.enableButton).right().width(60).height(25).apply(this.selectButton);
		this.selectButton.setEnabled(this.workingSetEnabled);

		// And finally, the label that holds the WS name.
		this.text = new Label(parentGroup, SWT.BORDER);
		new FormDataBuilder().top(this.enableButton).left(this.label).right(this.selectButton).apply(this.text);
		this.text.setEnabled(this.workingSetEnabled);

		// Now, we add the listeners to the objects
		this.enableButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (e.widget instanceof Button) {
					WorkingSetSelectionComponent.this.workingSetEnabled = ((Button) e.widget).getSelection();
					WorkingSetSelectionComponent.this.label
							.setEnabled(WorkingSetSelectionComponent.this.workingSetEnabled);
					WorkingSetSelectionComponent.this.label.update();
					WorkingSetSelectionComponent.this.selectButton
							.setEnabled(WorkingSetSelectionComponent.this.workingSetEnabled);
					WorkingSetSelectionComponent.this.selectButton.update();
					WorkingSetSelectionComponent.this.text
							.setEnabled(WorkingSetSelectionComponent.this.workingSetEnabled);
					WorkingSetSelectionComponent.this.text.update();
					WorkingSetSelectionComponent.this.validationSelectionListener.widgetSelected(e);
				}
			}
		});

		this.selectButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final IWorkbench workbench = PlatformUI.getWorkbench();
				final IWorkingSetManager workingSetManager = workbench.getWorkingSetManager();
				final IWorkingSetSelectionDialog createWorkingSetSelectionDialog = workingSetManager
						.createWorkingSetSelectionDialog(new Shell(WorkingSetSelectionComponent.this.getShell()), true);
				if (createWorkingSetSelectionDialog.open() == Window.OK) {
					WorkingSetSelectionComponent.this.selectedWorkingSets = createWorkingSetSelectionDialog
							.getSelection();
					String workingSetsAsString = "";
					for (final IWorkingSet workingSet : WorkingSetSelectionComponent.this.selectedWorkingSets) {
						workingSetsAsString = workingSetsAsString + (workingSetsAsString.length() > 0 ? ", " : "")
								+ workingSet.getLabel();
					}
					WorkingSetSelectionComponent.this.text.setText(workingSetsAsString);
					WorkingSetSelectionComponent.this.text.update();
					WorkingSetSelectionComponent.this.validationSelectionListener.widgetSelected(e);
				}
			}
		});
	}

	/**
	 * True if the user would like to have project in working set.
	 * @return
	 */
	public boolean isWorkingSetEnabled() {
		return this.workingSetEnabled;
	}

	/**
	 * Returns the user selected working sets.
	 * @return
	 */
	public IWorkingSet[] getSelectedWorkingSets() {
		return this.selectedWorkingSets;
	}

	/**
	 * Adds the validation selection listener
	 * @param selectionListener
	 */
	public void setSelectionListener(SelectionListener selectionListener) {
		this.validationSelectionListener = selectionListener;
	}

}
