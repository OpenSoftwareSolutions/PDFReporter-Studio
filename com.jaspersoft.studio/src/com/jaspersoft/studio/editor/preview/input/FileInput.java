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
package com.jaspersoft.studio.editor.preview.input;

import java.io.File;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.messages.Messages;

public class FileInput extends ADataInput {
	private Button btn;

	public boolean isForType(Class<?> valueClass) {
		return File.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter param, final Map<String, Object> params) {
		super.createInput(parent, param, params);
		if (isForType(param.getValueClass())) {
			btn = new Button(parent, SWT.PUSH);
			btn.setText(Messages.FileInput_selectfile);
			btn.setToolTipText(param.getDescription());
			btn.addFocusListener(focusListener);
			btn.setAlignment(SWT.LEFT);
			GridData gd = new GridData();
			gd.heightHint = 70;
			btn.setLayoutData(gd);
			btn.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					FilteredResourcesSelectionDialog fd = new FilteredResourcesSelectionDialog(Display.getCurrent()
							.getActiveShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
					if (fd.open() == Dialog.OK) {
						IFile file = (IFile) fd.getFirstResult();
						updateModel(new File(file.getLocationURI()));
					}
				}

			});
			updateInput();
			setNullable(param, btn);
		}
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null && value instanceof File)
			btn.setToolTipText(((File) value).getAbsolutePath());
		setDecoratorNullable(param);
	}

}
