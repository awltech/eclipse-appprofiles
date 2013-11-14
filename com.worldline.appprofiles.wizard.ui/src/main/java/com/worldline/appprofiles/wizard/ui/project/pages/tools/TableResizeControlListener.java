package com.worldline.appprofiles.wizard.ui.project.pages.tools;

import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * Class that resizes a table column each time a table is resized. Resizing
 * process only applied on one column. Size is computed according to other
 * columns size.
 * 
 * @author mvanbesien
 * 
 */
public class TableResizeControlListener implements ControlListener {

	/**
	 * Table being resized
	 */
	private Table table;

	/**
	 * Column to resize
	 */
	private TableColumn column;

	/**
	 * Creates Control listener that resizes the column provided according to
	 * the size to the table.
	 * 
	 * @param table
	 * @param column
	 */
	public TableResizeControlListener(Table table, TableColumn column) {
		this.table = table;
		this.column = column;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlResized(org.eclipse.swt
	 * .events.ControlEvent)
	 */
	@Override
	public void controlResized(ControlEvent e) {
		int otherColumnsWidth = 0;
		for (TableColumn c : table.getColumns()) {
			if (c != column)
				otherColumnsWidth += c.getWidth();
		}
		column.setWidth(table.getBounds().width - otherColumnsWidth - 20);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.swt.events.ControlListener#controlMoved(org.eclipse.swt.events
	 * .ControlEvent)
	 */
	@Override
	public void controlMoved(ControlEvent e) {
	}

}
