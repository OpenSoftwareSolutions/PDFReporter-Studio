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
package com.jaspersoft.studio.property.descriptor.subreport.parameter;

import net.sf.jasperreports.engine.JRSubreportParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.property.descriptor.subreport.parameter.dialog.SubreportPropertyEditor;

public class SubreportPropertiesCellEditor extends DialogCellEditor {

	public SubreportPropertiesCellEditor(Composite parent) {
		super(parent);
	}

	public SubreportPropertiesCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	private MSubreport msubreport;

	public void init(MSubreport msubreport) {
		this.msubreport = msubreport;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		SubreportPropertyEditor wizard = new SubreportPropertyEditor();
		wizard.setValue((JRSubreportParameter[]) getValue(), msubreport);
		WizardDialog dialog = new WizardDialog(cellEditorWindow.getShell(), wizard);
		dialog.create();
		if (dialog.open() == Dialog.OK) {
			return wizard.getValue();
		}
		return null;
	}

	private LabelProvider labelProvider;

	@Override
	protected void updateContents(Object value) {
		if (getDefaultLabel() == null) {
			return;
		}
		if (labelProvider == null)
			labelProvider = new SubreportPropertiesLabelProvider();
		String text = labelProvider.getText(value);
		getDefaultLabel().setText(text);
	}
}
