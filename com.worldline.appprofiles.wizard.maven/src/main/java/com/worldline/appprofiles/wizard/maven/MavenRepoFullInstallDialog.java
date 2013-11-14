/**
 * $Id: MavenRepoFullInstallDialog.java,v 1.2 2009/02/27 13:41:54 a125788 Exp $
 * 
 * HISTORY 
 * -------
 * $Log: MavenRepoFullInstallDialog.java,v $
 * Revision 1.2  2009/02/27 13:41:54  a125788
 * MVA : Moved Message class to upper level
 *
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 */
package com.worldline.appprofiles.wizard.maven;

import java.io.File;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

/**
 * Runnable to display a DialogBox if local repository is not installed
 * 
 * @author mvanbesien
 * @version $Revision: 1.2 $
 * @since $Date: 2009/02/27 13:41:54 $
 * 
 */
public class MavenRepoFullInstallDialog implements Runnable {

	private int button = Dialog.OK;

	public void run() {
		Display d = Display.getCurrent() == null ? Display.getDefault() : Display.getCurrent();
		Shell s = new Shell(d.getActiveShell());
		StringBuffer message = new StringBuffer();
		message.append(AppProfilesWizardMaven.REPO_INSTALL_DIALOG_MESSAGE.value(MavenLocalRepositoryLocator
				.getRepoLocation()));
		MessageDialog dialog = new MessageDialog(s, AppProfilesWizardMaven.REPO_INSTALL_DIALOG_TITLE.value(), null,
				message.toString(), SWT.ICON_QUESTION, new String[] {
						AppProfilesWizardMaven.REPO_INSTALL_DIALOG_OK.value(),
						AppProfilesWizardMaven.REPO_INSTALL_DIALOG_CANCEL.value() }, Dialog.OK);
		s.pack();
		Rectangle bounds;
		try {
			bounds = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().getBounds();
		} catch (NullPointerException npe) {
			bounds = s.getDisplay().getPrimaryMonitor().getBounds();
		}
		Rectangle rect = s.getBounds();
		s.setLocation(bounds.x + (bounds.width - rect.width) / 2, bounds.y + (bounds.height - rect.height) / 2);
		this.button = dialog.open();

	}

	public int getButton() {
		return button;
	}

	public static boolean shouldDisplay() {
		String repoLocation = MavenLocalRepositoryLocator.getRepoLocation();
		return repoLocation == null || !new File(repoLocation).exists() || new File(repoLocation).list().length == 0;
	}

}
