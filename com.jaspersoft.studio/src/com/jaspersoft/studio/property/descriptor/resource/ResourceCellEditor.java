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
package com.jaspersoft.studio.property.descriptor.resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.ATextDialogCellEditor;

public class ResourceCellEditor extends ATextDialogCellEditor {

	public ResourceCellEditor(Composite parent) {
		super(parent);
	}

	public ResourceCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		Shell shell = cellEditorWindow.getShell();
		FilteredResourcesSelectionDialog dialog = new FilteredResourcesSelectionDialog(shell, false, ResourcesPlugin
				.getWorkspace().getRoot(), IResource.FILE);
		dialog.setTitle(Messages.ResourceCellEditor_open_resource);
		dialog.setInitialPattern("*.properties"); //$NON-NLS-1$
		// dialog.setMessage("Please choose the Resource bundle:");
		// dialog.setMessage("Enter the name prefix or pattern (?, *, or camel case)");
		if (dialog.open() == Window.OK) {
			IFile file = (IFile) dialog.getFirstResult();
			if (file != null)
				return convertFile2Value(file);
		}
		return null;
	}

	protected String convertFile2Value(IFile f) {
		return f.getProjectRelativePath().toOSString();
	}
}
