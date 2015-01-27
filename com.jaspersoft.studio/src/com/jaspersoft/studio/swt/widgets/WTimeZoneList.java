/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.swt.widgets;

import java.util.Arrays;
import java.util.TimeZone;

import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

public class WTimeZoneList extends Composite {
	
	private String[] timeZonesIDs;
	private ListViewer listViewer;
	private List list;

	/**
	 * Create an instance of WTimeZoneList, a single select list of available time zones.
	 * @param parent
	 * @param style
	 */
	public WTimeZoneList(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		
		listViewer = new ListViewer(this, SWT.BORDER | SWT.V_SCROLL);
		list = listViewer.getList();
		timeZonesIDs = TimeZone.getAvailableIDs();
		initList();
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private void initList() {
		
		// sort time zones...
		Arrays.sort(timeZonesIDs);
		
		// ...then fill the list
		for (String timeZonesID : timeZonesIDs) {
			list.add(timeZonesID + " (" + TimeZone.getTimeZone(timeZonesID).getDisplayName() + ")");
		}
	}
	
	/**
	 * Set the selection to a given timeZone.
	 * If timeZone is null or the list does not contain it,
	 * the default timeZone value is selected.
	 * @param timeZone
	 */
	public void setSelection(TimeZone timeZone) {
		
		int index;
		
		if (timeZone == null) {
			index = getIndexFromTimeZone(TimeZone.getDefault());
		} else {
			index = getIndexFromTimeZone(timeZone);
		}
		
		list.setSelection(index);
	}

	/**
	 * This returns the list index for a given timeZone.
	 * @param timeZone
	 * @return int index
	 */
	private int getIndexFromTimeZone(TimeZone timeZone) {
		
		int returnedIndex = -1;
		
		if (timeZone != null) {
			for (int i = 0; i < timeZonesIDs.length; i++) {
				if(timeZonesIDs[i].equals(timeZone.getID())) {
					returnedIndex = i;
				}
			}
		}
		
		return returnedIndex;
	}
	
	/**
	 * Return the selected timeZone from the list.
	 * If the list has no selected timeZone,
	 * it returns the default timeZone.
	 * @return timeZone
	 */
	public TimeZone getSelectedTimeZone(){
		
		int selectionIndex = list.getSelectionIndex();
		if (selectionIndex < 0) {
			return TimeZone.getDefault();
		} else {
			return TimeZone.getTimeZone(timeZonesIDs[selectionIndex]);
		}
	}
	
	/**
	 * Check if the list contains the given timeZone: true if it does
	 * and false if not.
	 * @param timeZone
	 * @return true or false
	 */
	public boolean contains(TimeZone timeZone) {
		String ID = timeZone.getID();
		if (ID != null && ID.length() > 0) {
			for (String timeZonesID : timeZonesIDs) {
				if (timeZonesID.equals(ID)) return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if the list has a selected value.
	 * @return true or false
	 */
	public boolean hasSelectedValue() {
		if (list.getSelection().length > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Set the list selection listeners
	 * @param selectionAdapter
	 */
	public void setListSelectionListener(SelectionAdapter selectionAdapter) {
		list.addSelectionListener(selectionAdapter);
	}
}
