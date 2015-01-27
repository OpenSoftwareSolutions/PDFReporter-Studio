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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.inputhistory.InputHistoryCache;

public class SPNumber extends AHistorySPropertyWidget {
	protected Text ftext;

	public SPNumber(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
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

	private Number min;
	private Number max;

	public void setBorders(Number min, Number max) {
		this.min = min;
		this.max = max;
	}

	protected void createComponent(Composite parent) {
		ftext = section.getWidgetFactory().createText(parent, "", SWT.RIGHT);
		autocomplete = new AutoCompleteField(ftext, new TextContentAdapter(), InputHistoryCache.get(getHistoryKey()));
		ftext.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				try {
					if (!isRefresh) {
						Number newValue = null;
						String tmp = ftext.getText();
						if (tmp != null && !tmp.trim().isEmpty()) {
							if (numType == Double.class)
								newValue = new Double(tmp);
							else if (numType == Integer.class)
								newValue = new Integer(tmp);
							else if (numType == Float.class)
								newValue = new Float(tmp);
						}
						if (newValue != null) {
							if (min != null) {
								if (min.doubleValue() > newValue.doubleValue())
									newValue = min;
							}
							if (max != null) {
								if (max.doubleValue() < newValue.doubleValue())
									newValue = max;
							}
						}

						if (!section.changeProperty(pDescriptor.getId(), newValue)) {
							setData(section.getElement(), newValue);
						}
					}
				} catch (NumberFormatException nfe) {
				}

			}
		});
		ftext.setToolTipText(pDescriptor.getDescription());
		setWidth(parent, 6);
	}

	protected void setWidth(Composite parent, int chars) {
		int w = getCharWidth(ftext) * chars;
		if (parent.getLayout() instanceof RowLayout) {
			RowData rd = new RowData();
			rd.width = w;
			ftext.setLayoutData(rd);
		} else if (parent.getLayout() instanceof GridLayout) {
			GridData rd = new GridData();
			rd.widthHint = w;
			ftext.setLayoutData(rd);
		}
	}

	boolean isRefresh = false;
	protected Class<? extends Number> numType;

	public void setNumType(Class<? extends Number> numType) {
		this.numType = numType;
	}

	public void setData(APropertyNode pnode, Object b) {
		ftext.setEnabled(pnode.isEditable());
		Number n = (Number) b;
		isRefresh = true;
		setDataNumber(n);
		isRefresh = false;
		if (n != null)
			numType = n.getClass();
	}

	public void setDataNumber(Number f) {
		if (f != null) {
			int oldpos = ftext.getCaretPosition();
			ftext.setText(f.toString());
			if (f.toString().length() >= oldpos)
				ftext.setSelection(oldpos, oldpos);
		} else
			ftext.setText("");
	}

}
