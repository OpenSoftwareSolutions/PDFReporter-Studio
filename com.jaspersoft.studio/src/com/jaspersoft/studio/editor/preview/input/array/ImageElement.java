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
package com.jaspersoft.studio.editor.preview.input.array;

import java.awt.Image;

import javax.imageio.ImageIO;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.editor.preview.input.ImageInput;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.Misc;

public class ImageElement extends AWElement {

	private Button bbuton;

	@Override
	public Class<?> getSupportedType() {
		return Image.class;
	}

	@Override
	public Control createControl(Composite parent) {
		bbuton = new Button(parent, SWT.PUSH);
		bbuton.setText(Messages.ImageInput_selectimage);
		bbuton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FilteredResourcesSelectionDialog fd = new FilteredResourcesSelectionDialog(Display.getCurrent()
						.getActiveShell(), false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
				fd.setInitialPattern("*.png");//$NON-NLS-1$
				if (fd.open() == Dialog.OK) {
					IFile file = (IFile) fd.getFirstResult();
					Image image;
					try {
						image = ImageIO.read(file.getContents());
						setValue(image);
						updateLabel();
					} catch (Exception e1) {
						UIUtils.showError(e1);
					}
				}
			}
		});
		if (getValue() != null && getValue() instanceof Boolean)
			bbuton.setSelection((Boolean) Misc.nvl(getValue(), Boolean.FALSE));
		updateLabel();
		return bbuton;
	}

	private void updateLabel() {
		Object v = getValue();
		if (v != null && v instanceof Image)
			ImageInput.setButtonImage(bbuton, (Image) v);
		bbuton.getParent().layout();
	}

}
