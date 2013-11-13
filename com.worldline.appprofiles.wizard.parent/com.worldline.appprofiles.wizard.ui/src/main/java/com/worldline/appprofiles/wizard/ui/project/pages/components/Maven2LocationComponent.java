/**
 * $Id: Maven2LocationComponent.java,v 1.2 2009/02/27 13:41:54 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: Maven2LocationComponent.java,v $
 * Revision 1.2  2009/02/27 13:41:54  a125788
 * MVA : Moved Message class to upper level
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui.project.pages.components;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.worldline.appprofiles.wizard.ui.project.pages.components.Maven2LocationComponentMessages;

/**
 * @author A&M
 * 
 */
public class Maven2LocationComponent extends Composite {
	private Button workspaceButton;

	private Button locationButton;

	private Text locationText;

	private Label locationLabel;

	private Button browseButton;

	private ModifyListener modifyListener;

	public Maven2LocationComponent(final Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(1, false));

		SelectionAdapter buttonListener = new SelectionAdapter() {
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean shouldEnableLocation = !workspaceButton.getSelection();
				locationLabel.setEnabled(shouldEnableLocation);
				locationText.setEnabled(shouldEnableLocation);
				browseButton.setEnabled(shouldEnableLocation);
				if (modifyListener != null) {
					modifyListener.modifyText(null);
				}
			}
		};

		Group groupBox = new Group(this, SWT.NONE);
		groupBox.setText(Maven2LocationComponentMessages.GROUP_TEXT.value());
		groupBox.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		groupBox.setLayout(new GridLayout(3, false));

		workspaceButton = new Button(groupBox, SWT.RADIO);
		workspaceButton.setText(Maven2LocationComponentMessages.RADIO_CREATE_IN_WS.value());
		workspaceButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false,
				false, 3, 1));
		workspaceButton.addSelectionListener(buttonListener);

		locationButton = new Button(groupBox, SWT.RADIO);
		locationButton.setText(Maven2LocationComponentMessages.RADIO_CREATE_OUT_OF_WS.value());
		locationButton.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false,
				false, 3, 1));
		// locationButton.addSelectionListener( buttonListener );

		locationLabel = new Label(groupBox, SWT.NONE);
		locationLabel.setText(Maven2LocationComponentMessages.LABEL_FOLDERPATH.value());

		locationText = new Text(groupBox, SWT.BORDER);
		locationText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		browseButton = new Button(groupBox, SWT.PUSH);
		browseButton.setText(Maven2LocationComponentMessages.BUTTON_BROWSE.value());
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String directory = new DirectoryDialog(getShell(), SWT.OPEN).open();
				if (directory != null) {
					locationText.setText(directory.trim());
				}
			}
		});

		workspaceButton.setSelection(true);
		locationText.setText("");
		locationLabel.setEnabled(false);
		locationText.setEnabled(false);
		browseButton.setEnabled(false);
	}

	public IPath getLocationPath() {
		if (isLocationInWorkspace()) {
			return Platform.getLocation();
		}

		return Path.fromOSString(locationText.getText().trim());
	}

	public boolean isLocationInWorkspace() {
		return workspaceButton.getSelection();
	}

	public void setModifyListener(ModifyListener modifyListener) {
		this.modifyListener = modifyListener;
		locationText.addModifyListener(modifyListener);
	}

	@Override
	public void dispose() {
		super.dispose();
		if (modifyListener != null) {
			locationText.removeModifyListener(modifyListener);
		}
	}
}
