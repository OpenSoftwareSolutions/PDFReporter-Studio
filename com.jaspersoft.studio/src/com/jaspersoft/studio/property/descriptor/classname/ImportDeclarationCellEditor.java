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
package com.jaspersoft.studio.property.descriptor.classname;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.property.descriptor.ATextDialogCellEditor;
import com.jaspersoft.studio.property.descriptor.classname.dialog.ImportDialog;

public class ImportDeclarationCellEditor extends ATextDialogCellEditor {

	public ImportDeclarationCellEditor(Composite parent) {
		super(parent);
	}

	public ImportDeclarationCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		Shell shell = cellEditorWindow.getShell();

		String value = (String) getValue();
		ImportDialog dialog = new ImportDialog(shell, value);
		if (dialog.open() == Dialog.OK)
			return dialog.getImports();

		return null;
	}

}
