/**
 * $Id: ApplicationMavenInfoPage.java,v 1.2 2009/02/27 13:41:54 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: ApplicationMavenInfoPage.java,v $
 * Revision 1.2  2009/02/27 13:41:54  a125788
 * MVA : Moved Message class to upper level
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui.project.pages;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.ApplicationWizardPageStatus;
import com.worldline.appprofiles.wizard.ui.ValidatingApplicationWizardPage;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;
import com.worldline.appprofiles.wizard.ui.project.MavenModuleConstants;

public class ApplicationMavenInfoPage extends ValidatingApplicationWizardPage {
	public static String DEFAULT_VERSION = "1.0.0-SNAPSHOT";

	public static String DEFAULT_PACKAGING = "jar";

	private Text groupIDText;

	private Text artifactIDText;

	private Text packageNameText;

	private Text versionText;

	private Text descriptionText;

	private Button showDoc;

	private boolean showDocValue;
	
	private boolean offlineUpdateValue;
	
	private boolean updateSnapshotsValue;

	private ApplicationWizardOutput applicationWizardOutput;

	private String documentationKey = Activator.PLUGIN_ID + ".showDocumentation";
	
	private String offlineUpdateKey = Activator.PLUGIN_ID + ".offlineUpdate";
	
	private String updateSnapshotsKey = Activator.PLUGIN_ID + ".showUpdateSnapshots";

	private Button offlineUpdate;

	private Button updateSnapshotValues;

	public ApplicationMavenInfoPage(ApplicationWizardOutput output) {
		super(ApplicationMavenInfoPageMessages.PAGE_NAME.value());
		this.applicationWizardOutput = output;
		setTitle(ApplicationMavenInfoPageMessages.PAGE_TITLE.value());
		setDescription(ApplicationMavenInfoPageMessages.PAGE_DESC.value());
		setPageComplete(false);
		showDocValue = Activator.getDefault().getPreferenceStore().getBoolean(documentationKey);
		offlineUpdateValue = Activator.getDefault().getPreferenceStore().getBoolean(documentationKey);
		updateSnapshotsValue = Activator.getDefault().getPreferenceStore().getBoolean(documentationKey);
	}

	public void createControl(Composite root) {
		Composite parent = new Composite(root, SWT.NULL);
		parent.setLayout(new GridLayout());

		Group group = new Group(parent, SWT.NONE);
		group.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		group.setLayout(new GridLayout(2, false));
		group.setText(ApplicationMavenInfoPageMessages.PAGE_GROUP_TITLE.value());

		ModifyListener modifyingListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		};

		/**
		 * This listener is a able to store the previous groupId value in order
		 * to determine whether the packageName is a custom or a generated value
		 */
		ModifyListener modifyingGroupIDListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				String previousGroupIDText = (String) e.widget.getData();
				String previousDefaultPackageNameText = previousGroupIDText + "." + artifactIDText.getText();
				// Considering the expected generated packageName value, is the
				// current packageName a custom value or not ?
				if (packageNameText.getText().equals(previousDefaultPackageNameText)) {
					String temp = groupIDText.getText() + "." + artifactIDText.getText();
					packageNameText.setText(temp.toLowerCase());
				}
				// Updating the last groupId value
				e.widget.setData(groupIDText.getText());
				validate();
			}
		};

		Label groupIDLabel = new Label(group, SWT.NULL);
		groupIDLabel.setText(ApplicationMavenInfoPageMessages.PAGE_LABEL_GROUPID.value());

		groupIDText = new Text(group, SWT.BORDER | SWT.SINGLE);
		groupIDText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		groupIDText.addModifyListener(modifyingGroupIDListener);

		Label artifactIDLabel = new Label(group, SWT.NULL);
		artifactIDLabel.setText(ApplicationMavenInfoPageMessages.PAGE_LABEL_ARTFID.value());

		artifactIDText = new Text(group, SWT.BORDER | SWT.SINGLE);
		artifactIDText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		artifactIDText.addModifyListener(modifyingListener);
		artifactIDText.setEnabled(false);

		Label packageNameLabel = new Label(group, SWT.NULL);
		packageNameLabel.setText(ApplicationMavenInfoPageMessages.PAGE_LABEL_PACKNAME.value());

		packageNameText = new Text(group, SWT.BORDER | SWT.SINGLE);
		packageNameText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		packageNameText.addModifyListener(modifyingListener);

		Label versionLabel = new Label(group, SWT.NULL);
		versionLabel.setText(ApplicationMavenInfoPageMessages.PAGE_LABEL_VERSION.value());

		versionText = new Text(group, SWT.BORDER | SWT.SINGLE);
		versionText.setText(DEFAULT_VERSION);
		versionText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, false));
		versionText.addModifyListener(modifyingListener);
		
		Label descriptionLabel = new Label(group, SWT.NULL);
		descriptionLabel.setText(ApplicationMavenInfoPageMessages.PAGE_LABEL_DESCRIPTION.value());
		descriptionLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.BEGINNING, false, false));

		descriptionText = new Text(group, SWT.BORDER | SWT.MULTI);
		descriptionText.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
		descriptionText.addModifyListener(modifyingListener);
		
		showDoc = new Button(parent, SWT.CHECK);
		showDoc.setText(ApplicationMavenInfoPageMessages.PAGE_LABEL_OPENDOC.value());
		showDoc.setSelection(showDocValue);
		showDoc.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		});

		offlineUpdate = new Button(parent, SWT.CHECK);
		offlineUpdate.setText("Offline update");
		offlineUpdate.setSelection(offlineUpdateValue);
		
		updateSnapshotValues = new Button(parent, SWT.CHECK);
		updateSnapshotValues.setText("Update snapshots");
		updateSnapshotValues.setSelection(updateSnapshotsValue);
		
		validate();

		setControl(parent);
	}

	@Override
	public void setVisible(boolean visible) {
		if (visible) {
			groupIDText.setText("com.worldline");
			artifactIDText.setText(applicationWizardOutput.getProjectName());
			String packageName = groupIDText.getText() + "."
					+ applicationWizardOutput.getProjectName();
			char[] chars = packageName.trim().toCharArray();
			for (int i = 0; i < chars.length; i++) {
				char c = chars[i];
				if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '_' || c == '.'))
					chars[i] = '_';
			}
			packageNameText.setText(new String(chars).toLowerCase());
		}

		super.setVisible(visible);
		groupIDText.setFocus();
	}

	@Override
	protected void onPageValidated() {
		MavenModuleConstants.setUserGroupId(applicationWizardOutput, groupIDText.getText().trim());
		MavenModuleConstants.setUserArtifactId(applicationWizardOutput, artifactIDText.getText().trim());
		MavenModuleConstants.setUserPackageName(applicationWizardOutput, packageNameText.getText().trim());
		MavenModuleConstants.setUserVersion(applicationWizardOutput, versionText.getText().trim());
		MavenModuleConstants.setUserDescription(applicationWizardOutput, descriptionText.getText().trim());
		
		applicationWizardOutput.setShowDocumentation(showDoc.getSelection());
		applicationWizardOutput.setProperty(MavenModuleConstants.UPDATE_FORCE_SNAPSHOTS, String.valueOf(updateSnapshotValues.getSelection()));
		applicationWizardOutput.setProperty(MavenModuleConstants.UPDATE_OFFLINE, String.valueOf(offlineUpdate.getSelection()));
		
		Activator.getDefault().getPreferenceStore().putValue(documentationKey, String.valueOf(showDoc.getSelection()));
		Activator.getDefault().getPreferenceStore().putValue(updateSnapshotsKey, String.valueOf(updateSnapshotValues.getSelection()));
		Activator.getDefault().getPreferenceStore().putValue(offlineUpdateKey, String.valueOf(offlineUpdate.getSelection()));
	}

	@Override
	protected ApplicationWizardPageStatus isPageValid() {
		char[] chars = groupIDText.getText().trim().toCharArray();
		for (char c : chars) {
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '_' || c == '.')) {
				ApplicationWizardPageStatus.error(ApplicationMavenInfoPageMessages.PAGE_ERROR_INVALID_GROUPID.value());
			}
		}

		if (packageNameText.getText().trim().length() > 0) {
			IStatus status = JavaConventions.validatePackageName(packageNameText.getText().trim(), "5.0", "5.0");
			if (status.getCode() != IStatus.OK) {
				ApplicationWizardPageStatus.error(ApplicationMavenInfoPageMessages.PAGE_ERROR_INVALID_PACKNAME.value());
			}
		}

		if (packageNameText.getText().trim().length() == 0) {
			ApplicationWizardPageStatus.error(ApplicationMavenInfoPageMessages.PAGE_ERROR_EMPTY_PACKNAME.value());
		}
		if (groupIDText.getText().trim().length() > 0) {
			if (artifactIDText.getText().trim().length() > 0) {
				return ApplicationWizardPageStatus.ok();
			} else {
				return ApplicationWizardPageStatus.error(ApplicationMavenInfoPageMessages.PAGE_ERROR_EMPTY_ARTFID.value());
			}
		} else {
			return ApplicationWizardPageStatus.error(ApplicationMavenInfoPageMessages.PAGE_ERROR_EMPTY_GROUPID.value());
		}
	}

}