/**
 * $Id: RunApplicationWizardAction.java,v 1.1.1.1 2008/05/07 15:47:00 a125788 Exp $
 * 
 * HISTORY : 
 * -------
 * $Log: RunApplicationWizardAction.java,v $
 * Revision 1.1.1.1  2008/05/07 15:47:00  a125788
 * Initial Revision
 *
 *
 */
package com.worldline.appprofiles.wizard.ui.project;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;

/**
 * Action available in a Toolbar Action set, which role is to launch the
 * Common Maven Project Creation Action
 * 
 * @author Atos Worldline
 * 
 */
public class RunApplicationWizardAction implements IWorkbenchWindowActionDelegate {

	/**
	 * The wizard dialog width
	 */
	private static final int SIZING_WIZARD_WIDTH = 500;

	/**
	 * The wizard dialog height
	 */
	private static final int SIZING_WIZARD_HEIGHT = 500;

	public void dispose() {

	}

	public void init(IWorkbenchWindow window) {
	}

	public void run(IAction action) {

		ApplicationWizard wizard = new ApplicationWizard();
		Shell shell = new Shell((Display.getCurrent() == null ? Display.getDefault() : Display
				.getCurrent()).getActiveShell());
		CenteredWizardDialog dialog = new CenteredWizardDialog(shell, wizard);
		dialog.create();
		dialog.getShell().setSize(Math.max(RunApplicationWizardAction.SIZING_WIZARD_WIDTH, dialog.getShell().getSize().x), Math.max(
				RunApplicationWizardAction.SIZING_WIZARD_HEIGHT, dialog.getShell().getSize().y));
		dialog.open();

	}

	public void selectionChanged(IAction action, ISelection selection) {

	}
	
	private static final class CenteredWizardDialog extends WizardDialog {

		public CenteredWizardDialog(Shell parentShell, IWizard newWizard) {
			super(parentShell, newWizard);
		}
		
		@Override
		protected Rectangle getConstrainedShellBounds(Rectangle preferredSize) {
			Rectangle preferred = super.getConstrainedShellBounds(preferredSize);
			Rectangle envBounds;
			try {
				envBounds = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getShell().getBounds();
			} catch (NullPointerException npe) {
				envBounds = getParentShell().getDisplay().getPrimaryMonitor().getBounds();
			}
			preferred.x = envBounds.x + (envBounds.width - preferred.width) / 2;
			preferred.y = envBounds.y + (envBounds.height - preferred.height) / 2;
			
			return preferred;
		}
	}

}
