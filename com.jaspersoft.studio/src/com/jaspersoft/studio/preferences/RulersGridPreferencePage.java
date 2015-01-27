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
package com.jaspersoft.studio.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;

/*
 * 
 */
public class RulersGridPreferencePage extends FieldEditorOverlayPage {

	public static final String DEFAULT_GRIDCOLOR = "192,192,192"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_SHOWRULER = "pageRulerGrid_SHOWRULER"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_SNAPTOGUIDES = "pageRulerGrid_SNAPTOGUIDES"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_SHOWGRID = "pageRulerGrid_SHOWGRID"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_SNAPTOGRID = "pageRulerGrid_SNAPTOGRID"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_SNAPTOGEOMETRY = "pageRulerGrid_SNAPTOGEOMETRY"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_GRIDSPACEX = "pageRulerGrid_GRIDSPACEX"; //$NON-NLS-1$
	public static final String P_PAGE_RULERGRID_GRIDSPACEY = "pageRulerGrid_GRIDSPACEY"; //$NON-NLS-1$
	public static final String P_PAGE_GRID_COLOR = "gridColor"; //$NON-NLS-1$

	public RulersGridPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.RulersGridPreferencePage_description);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		Composite fieldEditorParent = getFieldEditorParent();
		Group group = new Group(fieldEditorParent, SWT.NONE);
		group.setText(Messages.RulersGridPreferencePage_ruler_options);
		group.setLayout(new GridLayout(2, false));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);

		addField(new BooleanFieldEditor(P_PAGE_RULERGRID_SHOWRULER, Messages.RulersGridPreferencePage_show_rulers, group));
		addField(new BooleanFieldEditor(P_PAGE_RULERGRID_SNAPTOGUIDES, Messages.common_snap_to_guides, group));

		group = new Group(fieldEditorParent, SWT.NONE);
		group.setText(Messages.RulersGridPreferencePage_grid_options);
		group.setLayout(new GridLayout(2, false));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.horizontalSpan = 2;
		group.setLayoutData(gridData);

		addField(new BooleanFieldEditor(P_PAGE_RULERGRID_SHOWGRID, Messages.RulersGridPreferencePage_show_grid, group));
		addField(new BooleanFieldEditor(P_PAGE_RULERGRID_SNAPTOGRID, Messages.common_snap_to_grid, group));
		addField(new BooleanFieldEditor(P_PAGE_RULERGRID_SNAPTOGEOMETRY, Messages.common_snap_to_geometry, group));
		IntegerFieldEditor spaceX = new IntegerFieldEditor(P_PAGE_RULERGRID_GRIDSPACEX,
				Messages.RulersGridPreferencePage_grid_spacing_x, group);
		spaceX.setValidRange(1, 500);
		addField(spaceX);

		IntegerFieldEditor spaceY = new IntegerFieldEditor(P_PAGE_RULERGRID_GRIDSPACEY,
				Messages.RulersGridPreferencePage_grid_spacing_y, group);
		spaceY.setValidRange(1, 500);
		addField(spaceY);

		addField(new ColorFieldEditor(P_PAGE_GRID_COLOR, Messages.RulersGridPreferencePage_common_gridcolor, group));

	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(P_PAGE_RULERGRID_SHOWRULER, new Boolean(true));
		store.setDefault(P_PAGE_RULERGRID_SNAPTOGUIDES, new Boolean(true));

		store.setDefault(P_PAGE_RULERGRID_SHOWGRID, new Boolean(true));
		store.setDefault(P_PAGE_RULERGRID_SNAPTOGRID, new Boolean(true));
		store.setDefault(P_PAGE_RULERGRID_SNAPTOGEOMETRY, new Boolean(true));
		store.setDefault(P_PAGE_RULERGRID_GRIDSPACEX, new Integer(10));
		store.setDefault(P_PAGE_RULERGRID_GRIDSPACEY, new Integer(10));
		store.setDefault(P_PAGE_GRID_COLOR, DEFAULT_GRIDCOLOR); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.RulersGridPreferencePage.property"; //$NON-NLS-1$

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}
}
