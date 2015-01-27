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
package com.jaspersoft.studio.components.map.model.style;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.components.map.model.itemdata.MapDataElementsConfigurationLabelProvider;

/**
 * Cell Editor for the <code>StandardMapComponent.PROPERTY_PATH_STYLE_LIST</code> property
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapStylesCellEditor extends DialogCellEditor {

	private MapDataElementsConfigurationLabelProvider labelProvider;
	
	public MapStylesCellEditor(Composite parent) {
		super(parent);
	}

	public MapStylesCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		MessageDialog.openInformation(UIUtils.getShell(), "Map Paths Cell Editor", "TO BE IMPLEMENTED");
		return getValue();
	}

	@Override
	protected void updateContents(Object value) {
		if(getDefaultLabel()==null) {
			return;
		}
		if(labelProvider==null){
			labelProvider = new MapDataElementsConfigurationLabelProvider("Paths");
		}
		String text = labelProvider.getText(value);
		getDefaultLabel().setText(text);
	}
}
