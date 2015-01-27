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
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FontFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.property.section.report.util.Unit;

/*
 * 
 */
public class DesignerPreferencePage extends FieldEditorOverlayPage {

	public static final String DEFAULT_BORDERSTYLE = "shadow"; //$NON-NLS-1$
	public static final String DEFAULT_MARGINCOLOR = "170,168,255"; //$NON-NLS-1$
	public static final String DEFAULT_PAGE_BACKGROUND = "255,255,255"; //$NON-NLS-1$

	/**
	 * Key of the behavior used when a field is dragged from the outline into the detail band
	 */
	public static final String BEHAVIOR_ON_FIELD_DROP = "fieldDrop"; //$NON-NLS-1$

	/**
	 * This behavior specify to create a label aligned with the new filed, into the column header band if there is enough
	 * space, otherwise in the detail band.
	 */
	public static final String BEHAVIOR_CREATE_LABEL = "create_label"; //$NON-NLS-1$

	/**
	 * This behavior specify to create only the text field when a field is dragged from the outline into the detail band
	 */
	public static final String BEHAVIOR_DO_NOTHING = "do_nothing"; //$NON-NLS-1$

	/**
	 * This behavior specify to ask to the user what to do when a field is dragged from the outline into the detail band
	 */
	public static final String BEHAVIOR_ASK_EVERYTIME = "ask_everytime"; //$NON-NLS-1$

	/**
	 * This constants specify the default behavior when a field is dragged from the outline into the detail band
	 */
	public static final String DEFAULT_BEHAVIOR = BEHAVIOR_CREATE_LABEL; //$NON-NLS-1$

	public static final String DEFAULT_ELEMENT_DESIGN_BORDER_COLOR = "0,0,0"; //$NON-NLS-1$
	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.DesignerPreferencePage.property"; //$NON-NLS-1$

	public static final String P_ELEMENT_DESIGN_BORDER_STYLE = "elementDesignBorderStyle"; //$NON-NLS-1$
	public static final String P_PAGE_DESIGN_BORDER_STYLE = "pageDesignBorderStyle"; //$NON-NLS-1$
	public static final String P_PAGE_DEFAULT_UNITS = "pageDEFAULTUNITS"; //$NON-NLS-1$
	public static final String P_SHOW_REPORT_BAND_NAMES = "showReportBandNames"; //$NON-NLS-1$
	public static final String P_SAVE_ON_PREVIEW = "saveOnPreview"; //$NON-NLS-1$
	public static final String P_CENTER_SELECTION = "centerSelectedElement"; //$NON-NLS-1$
	public static final String P_RESIZE_CONTAINER = "resizeParentContainer"; //$NON-NLS-1$
	public static final String P_CONTAINER_MARGIN_COLOR = "containerMarginColor"; //$NON-NLS-1$
	public static final String P_PAGE_MARGIN_COLOR = "pageMarginColor"; //$NON-NLS-1$
	public static final String P_PAGE_BACKGROUND = "pageBackground"; //$NON-NLS-1$

	public static final String P_ELEMENT_DESIGN_BORDER_COLOR = "elementDesignBorderColor"; //$NON-NLS-1$

	// editors font information
	public static final String P_INTERNAL_EDITORS_FONT = "internalEditorsFont"; //$NON-NLS-1$
	// specify whether to use the description or the name when dropping a field in the detail band	
	public static final String P_USE_FIELD_DESCRIPTION = "useDescriptionOnFieldDrop"; //$NON-NLS-1$

	public DesignerPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.DesignerPreferencePage_description);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		addField(new ComboFieldEditor(
				P_ELEMENT_DESIGN_BORDER_STYLE,
				Messages.DesignerPreferencePage_element_design_border_style,
				new String[][] {
						{ Messages.DesignerPreferencePage_corners, "corners" }, { Messages.common_rectangle, "rectangle" } }, getFieldEditorParent())); //$NON-NLS-1$ //$NON-NLS-2$
		addField(new ColorFieldEditor(P_ELEMENT_DESIGN_BORDER_COLOR, Messages.DesignerPreferencePage_elementbordercolor,
				getFieldEditorParent()));

		addField(new ComboFieldEditor(P_PAGE_DESIGN_BORDER_STYLE, Messages.DesignerPreferencePage_page_border_style,
				new String[][] { { Messages.DesignerPreferencePage_fancy_shadow, DEFAULT_BORDERSTYLE },
						{ Messages.DesignerPreferencePage_simple_shadow, "rectangle" } }, getFieldEditorParent())); //$NON-NLS-1$ //$NON-NLS-2$

		addField(new ComboFieldEditor(P_PAGE_DEFAULT_UNITS, Messages.DesignerPreferencePage_unit, Unit.getUnits2(),
				getFieldEditorParent()));

		addField(new BooleanFieldEditor(P_SHOW_REPORT_BAND_NAMES, Messages.DesignerPreferencePage_show_band_names,
				getFieldEditorParent()));
		addField(new ColorFieldEditor(P_CONTAINER_MARGIN_COLOR, Messages.DesignerPreferencePage_common_bandmargincolor,
				getFieldEditorParent()));
		addField(new ColorFieldEditor(P_PAGE_MARGIN_COLOR, Messages.DesignerPreferencePage_pageprintmargincolor,
				getFieldEditorParent()));
		addField(new ColorFieldEditor(P_PAGE_BACKGROUND, Messages.DesignerPreferencePage_pagebackground,
				getFieldEditorParent()));
		addField(new FontFieldEditor(P_INTERNAL_EDITORS_FONT, Messages.DesignerPreferencePage_InternalEditorsFont,
				getFieldEditorParent()));

		addField(new ComboFieldEditor(BEHAVIOR_ON_FIELD_DROP, Messages.DesignerPreferencePage_field_behavior,
				new String[][] { { Messages.DesignerPreferencePage_field_behavior_label, BEHAVIOR_CREATE_LABEL },
						{ Messages.DesignerPreferencePage_field_behavior_nothing, BEHAVIOR_DO_NOTHING },
						{ Messages.DesignerPreferencePage_field_behavior_ask, BEHAVIOR_ASK_EVERYTIME } }, getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_USE_FIELD_DESCRIPTION, Messages.DesignerPreferencePage_UseDescriptionForLabelText,
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_SAVE_ON_PREVIEW, Messages.DesignerPreferencePage_savereportonpreview, getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_CENTER_SELECTION, Messages.DesignerPreferencePage_centerEditorOption, getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_RESIZE_CONTAINER, Messages.DesignerPreferencePage_autoresizeBand, getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(P_PAGE_DESIGN_BORDER_STYLE, DEFAULT_BORDERSTYLE);
		store.setDefault(P_ELEMENT_DESIGN_BORDER_COLOR, DEFAULT_ELEMENT_DESIGN_BORDER_COLOR);
		store.setDefault(P_ELEMENT_DESIGN_BORDER_STYLE, "rectangle"); //$NON-NLS-1$
		store.setDefault(P_PAGE_DEFAULT_UNITS, "px"); //$NON-NLS-1$
		store.setDefault(P_CONTAINER_MARGIN_COLOR, DEFAULT_MARGINCOLOR);
		store.setDefault(P_PAGE_MARGIN_COLOR, DEFAULT_MARGINCOLOR);
		store.setDefault(P_SHOW_REPORT_BAND_NAMES, true);
		store.setDefault(P_PAGE_BACKGROUND, DEFAULT_PAGE_BACKGROUND);
		store.setDefault(BEHAVIOR_ON_FIELD_DROP, DEFAULT_BEHAVIOR);
		store.setDefault(P_USE_FIELD_DESCRIPTION, false);
		PreferenceConverter.setDefault(store, P_INTERNAL_EDITORS_FONT, FontUtils.getTextEditorFontData());
		store.setDefault(P_SAVE_ON_PREVIEW, false);
		store.setDefault(P_CENTER_SELECTION, true);
		store.setDefault(P_RESIZE_CONTAINER, true);
	}

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}
}
