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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptors.JSSTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.inputhistory.InputHistoryCache;

public class SPText extends AHistorySPropertyWidget {
	protected Text ftext;

	/**
	 * Flag used to avoid that the handletextchanged is called twice when CR is pressed (because the CR made the control
	 * to loose the focus)
	 */
	protected boolean disableFocusLost = false;

	public SPText(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return ftext;
	}

	@Override
	protected Text getTextControl() {
		return ftext;
	}

	protected void createComponent(Composite parent) {
		int style = SWT.NONE;
		if (pDescriptor instanceof JSSTextPropertyDescriptor)
			style = ((JSSTextPropertyDescriptor) pDescriptor).getStyle();
		ftext = section.getWidgetFactory().createText(parent, "", style);
		autocomplete = new AutoCompleteField(ftext, new TextContentAdapter(), InputHistoryCache.get(getHistoryKey()));
		// ftext.addModifyListener(new ModifyListener() {
		// public void modifyText(ModifyEvent e) {
		// handleTextChanged(section, pDescriptor.getId(), ftext.getText());
		// }
		// });
		ftext.addKeyListener(new KeyListener() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == SWT.CR) {
					disableFocusLost = true;
					handleTextChanged(section, pDescriptor.getId(), ftext.getText());
					disableFocusLost = false;
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {

			}
		});
		ftext.setToolTipText(pDescriptor.getDescription());
		setWidth(parent, 15);
	}

	protected void setWidth(Composite parent, int chars) {
		int w = getCharWidth(ftext) * chars;
		if (parent.getLayout() instanceof RowLayout) {
			RowData rd = new RowData();
			rd.width = w;
			ftext.setLayoutData(rd);
		} else if (parent.getLayout() instanceof GridLayout) {
			GridData rd = new GridData(GridData.FILL_HORIZONTAL);
			rd.minimumWidth = w;
			rd.widthHint = w;
			ftext.setLayoutData(rd);
		}
	}

	@Override
	protected void handleFocusLost() {
		if (!disableFocusLost) {
			String v = getCurrentValue();
			if (!(v != null && v.equals(ftext.getText())))
				handleTextChanged(section, pDescriptor.getId(), ftext.getText());
			super.handleFocusLost();
		}
	}

	protected String getCurrentValue() {
		return (String) section.getElement().getPropertyValue(pDescriptor.getId());
	}

	protected void handleTextChanged(final AbstractSection section, final Object property, String text) {
		section.changeProperty(property, text);
	}

	protected APropertyNode pnode;

	public void setData(APropertyNode pnode, Object b) {
		this.pnode = pnode;
		ftext.setEnabled(pnode.isEditable());
		if (b != null) {
			int oldpos = ftext.getLocation().x;
			ftext.setText(b.toString());
			if (b.toString().length() >= oldpos)
				ftext.setSelection(oldpos, oldpos);
		} else {
			ftext.setText("");
		}
	}

}
