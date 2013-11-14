/**
 * $Id: ApplicationProjectBasePage.java,v 1.5 2009/02/27 13:41:54 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: ApplicationProjectBasePage.java,v $
 * Revision 1.5  2009/02/27 13:41:54  a125788
 * MVA : Moved Message class to upper level
 *
 * Revision 1.4  2008/06/23 06:43:50  a125788
 * MVA : Removed all unwanted messages
 *
 * Revision 1.3  2008/06/20 09:44:51  a125788
 * MVA : Added another test for project name length
 *
 * Revision 1.2  2008/06/20 08:57:19  a125788
 * MVA : No error displayed when project message length is equal to 0
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.ui.project.pages;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkingSet;

import com.worldline.appprofiles.wizard.ui.Activator;
import com.worldline.appprofiles.wizard.ui.ApplicationWizardPageStatus;
import com.worldline.appprofiles.wizard.ui.ValidatingApplicationWizardPage;
import com.worldline.appprofiles.wizard.ui.project.ApplicationWizardOutput;
import com.worldline.appprofiles.wizard.ui.project.pages.components.Maven2LocationComponent;
import com.worldline.appprofiles.wizard.ui.project.pages.components.WorkingSetSelectionComponent;

public class ApplicationProjectBasePage extends ValidatingApplicationWizardPage {
	private Text projectNameText;

	private Maven2LocationComponent locationComponent;

	private WorkingSetSelectionComponent workingSetSelectionComponent;

	private ApplicationWizardOutput applicationWizardOutput;

	public ApplicationProjectBasePage(ApplicationWizardOutput output) {
		super(ApplicationProjectBasePageMessages.PAGE_NAME.value());
		this.applicationWizardOutput = output;
		setTitle(ApplicationProjectBasePageMessages.PAGE_TITLE.value());
		setDescription(ApplicationProjectBasePageMessages.PAGE_DESC.value());
		setPageComplete(true);
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		container.setLayout(new GridLayout(2, false));

		ModifyListener modifyListener = new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validate();
			}
		};

		SelectionListener selectionListener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				validate();
			}
		};
		// project name
		Label label = new Label(container, SWT.NULL);
		label.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, false, false));
		label.setText(ApplicationProjectBasePageMessages.PAGE_LABEL_APPNAME.value());
		projectNameText = new Text(container, SWT.BORDER | SWT.SINGLE);
		projectNameText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		projectNameText.addModifyListener(modifyListener);

		// location group
		locationComponent = new Maven2LocationComponent(container, SWT.NONE);
		locationComponent.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		locationComponent.setModifyListener(modifyListener);

		workingSetSelectionComponent = new WorkingSetSelectionComponent(container, SWT.NONE);
		workingSetSelectionComponent.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		workingSetSelectionComponent.setSelectionListener(selectionListener);
		projectNameText.setFocus();

		validate();

		setControl(container);
	}

	@Override
	protected ApplicationWizardPageStatus isPageValid() {
		String name = projectNameText.getText().trim();
		IWorkspace workspace = ResourcesPlugin.getWorkspace();

		// Check if project name is valid
		if (isProjectNameValid(workspace, name)) {
			// if project should be in workspace
			if (locationComponent.isLocationInWorkspace()) {
				// Check if an existing project with the same name is already in
				// workspace
				if (!isProjectInWorkspace(workspace, name)) {
					if (!isFolderInWorkspace(workspace, name))
						return ApplicationWizardPageStatus.ok();
					else {
						return ApplicationWizardPageStatus
								.error(ApplicationProjectBasePageMessages.PAGE_LABEL_ERROR_FSFOLDEREXISTS.value());
					}
				} else {
					return ApplicationWizardPageStatus
							.error(ApplicationProjectBasePageMessages.PAGE_LABEL_ERROR_PROJ_OR_APP_EXISTS.value());
				}
			} else {
				IStatus directoryValid = isDirectoryValid(workspace, locationComponent.getLocationPath(), name);
				if (directoryValid.getSeverity() == IStatus.WARNING) {
					return ApplicationWizardPageStatus.warn(directoryValid.getMessage(), false);
				} else if (directoryValid.getSeverity() == IStatus.ERROR) {
					return ApplicationWizardPageStatus.error(directoryValid.getMessage());
				} else {
					return ApplicationWizardPageStatus.ok();
				}
			}
		} else if (workingSetSelectionComponent.isWorkingSetEnabled()
				&& workingSetSelectionComponent.getSelectedWorkingSets().length == 0) {
			return ApplicationWizardPageStatus.error("At least one working set should be enabled.");
		} else {
			if (name.trim().length() > 0)
				return ApplicationWizardPageStatus
						.error(ApplicationProjectBasePageMessages.PAGE_LABEL_ERROR_APPNAME_INVALID.value());
			else
				return ApplicationWizardPageStatus.info("Application should have a name.", false);
		}
	}

	@Override
	protected void onPageValidated() {
		applicationWizardOutput.setProjectName(projectNameText.getText().trim());

		if (!locationComponent.isLocationInWorkspace()) {
			applicationWizardOutput.setProjectLocation(locationComponent.getLocationPath());
		} else {
			IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
			applicationWizardOutput.setProjectLocation(root.getLocation());
		}

		applicationWizardOutput
				.setSelectedWorkingSets(workingSetSelectionComponent.isWorkingSetEnabled() ? workingSetSelectionComponent
						.getSelectedWorkingSets() : new IWorkingSet[0]);
	}

	private static boolean isProjectInWorkspace(IWorkspace workspace, String name) {
		return workspace.getRoot().getProject(name).exists();
	}

	private static boolean isFolderInWorkspace(IWorkspace workspace, String name) {
		return workspace.getRoot().getLocation().append(name).toFile().exists();
	}

	private static IStatus isDirectoryValid(IWorkspace workspace, IPath directory, String projectName) {
		if (directory.isEmpty())
			return new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Path must not be empty.");
		return workspace.validateProjectLocation(workspace.getRoot().getProject(projectName), directory);
	}

	private static boolean isProjectNameValid(IWorkspace workspace, String name) {
		String projectName = name.trim();
		char[] chars = projectName.toCharArray();
		for (char c : chars) {
			if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= '0' && c <= '9') || c == '-' || c == '_' || c == '.'))
				return false;
		}
		return (projectName.trim().length() > 0) && (workspace.validateName(projectName, IResource.PROJECT).isOK());
	}
}
