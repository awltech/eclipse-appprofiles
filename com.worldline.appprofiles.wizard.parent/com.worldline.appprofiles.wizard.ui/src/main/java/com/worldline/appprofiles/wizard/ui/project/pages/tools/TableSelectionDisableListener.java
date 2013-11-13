package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

/**
 * Listener only accessible through static method.
 * Purpose is to disable row/cell selection on table. 
 * 
 * @author mvanbesien
 *
 */
public class TableSelectionDisableListener implements Listener {

	/*
	 * Made private...
	 */
	private TableSelectionDisableListener() {
	}

	/**
	 * Disable selection on table.
	 * @param table
	 */
	public static void disableSelection(Table table) {
		table.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				Widget item = event.item;
				if (item instanceof TableItem) {
					TableItem tableItem = (TableItem) item;
					if (tableItem.getParent() != null)
						tableItem.getParent().setSelection(-1);
				}
				
			}
		});
		// By passed, because seems that it is not working with WinXP & Unix
		//TableSelectionDisableListener listener = new TableSelectionDisableListener();
		//table.addListener(SWT.EraseItem, listener);
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	@Override
	public void handleEvent(Event event) {
		if ((event.detail & SWT.SELECTED) != 0) {
			event.detail &= ~SWT.SELECTED;
		}
	}

}
