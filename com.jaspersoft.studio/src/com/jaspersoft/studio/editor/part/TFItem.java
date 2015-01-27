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
package com.jaspersoft.studio.editor.part;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;

public class TFItem extends Item {
	private TFContainer parent;
	private Control control; // the tab page

	public TFItem(TFContainer parent, int style) {
		this(parent, style, parent.getItemCount());
	}

	public TFItem(TFContainer parent, int style, int index) {
		super(parent, style);
		this.parent = parent;
		parent.createItem(this, index);
	}

	public Control getControl() {
		checkWidget();
		return control;
	}

	@Override
	public void setText(String string) {
		super.setText(string);
		parent.update(this);
	}

	@Override
	public void setImage(Image image) {
		super.setImage(image);
		parent.update(this);
	}

	public void setControl(Control control) {
		checkWidget();
		if (control != null) {
			if (control.isDisposed())
				SWT.error(SWT.ERROR_INVALID_ARGUMENT);
			if (control.getParent() != parent.getContent())
				SWT.error(SWT.ERROR_INVALID_PARENT);
		}
		if (this.control != null && !this.control.isDisposed())
			this.control.setVisible(false);

		((StackLayout) parent.getContent().getLayout()).topControl = control;

		this.control = control;
		if (this.control != null) {
			int index = parent.indexOf(this);
			if (index == parent.getSelectionIndex()) {
				this.control.setBounds(parent.getContent().getClientArea());
				this.control.setVisible(true);
			} else {
				int selectedIndex = parent.getSelectionIndex();
				Control selectedControl = null;
				if (selectedIndex != -1) {
					selectedControl = parent.getItem(selectedIndex).getControl();
				}
				if (this.control != selectedControl) {
					this.control.setVisible(false);
				}
			}
		}
		parent.getContent().layout(true);
	}
	@Override
	public void dispose() {
		parent.removeItem(this);
		super.dispose();
	}
}
