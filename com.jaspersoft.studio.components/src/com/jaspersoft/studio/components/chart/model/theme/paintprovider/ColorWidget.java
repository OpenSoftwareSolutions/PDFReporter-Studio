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
package com.jaspersoft.studio.components.chart.model.theme.paintprovider;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.property.color.chooser.ColorDialog;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorLabelProvider;
import com.jaspersoft.studio.utils.AlfaRGB;

public class ColorWidget extends Composite {
	public static ColorLabelProvider cprovider = new ColorLabelProvider(NullEnum.NULL);
	private Text tcolor;
	private Button b;
	private RGB color;

	public ColorWidget(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);

		tcolor = new Text(this, SWT.BORDER | SWT.READ_ONLY | SWT.CENTER);
		tcolor.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tcolor.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!isRefresh)
					return;
				// TODO parse the value, rgb, hexa, etc.
				// convert to RGB color
				// RGB newColor = null;
				// RGB oldColor = color;
				// color = newColor;
				// setupWidgets(newColor);
				// psuport.firePropertyChange("color", oldColor, newColor);
			}
		});

		b = new Button(this, SWT.PUSH);
		b.setImage(cprovider.getImage(null));
		b.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				ColorDialog cd = new ColorDialog(b.getShell());
				cd.setText(Messages.common_color);
				cd.setRGB(color);
				RGB newColor = cd.openRGB();
				if (newColor != null) {
					RGB oldColor = color;
					color = newColor;
					setupWidgets(newColor);
					psuport.firePropertyChange("color", oldColor, new AlfaRGB(newColor, 255));
				}
			}
		});
	}

	private boolean isRefresh = false;

	private void setupWidgets(RGB rgb) {
		if (!isRefresh) {
			isRefresh = true;
			tcolor.setText(rgb == null ? "" : cprovider.getText(rgb));
			b.setImage(cprovider.getImage(rgb));
			isRefresh = false;
		}
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		tcolor.setEnabled(enabled);
		b.setEnabled(enabled);

		setupWidgets(enabled ? color : null);
	}

	public void setColor(RGB color) {
		this.color = color;
		setupWidgets(color);
	}

	public RGB getColor() {
		return color;
	}

	private PropertyChangeSupport psuport = new PropertyChangeSupport(this);

	public void addPropertyChangeListener(PropertyChangeListener l) {
		psuport.addPropertyChangeListener(l);
	}

	public void removePropertyChangeListener(PropertyChangeListener l) {
		psuport.removePropertyChangeListener(l);
	}

	@Override
	public void dispose() {
		for (PropertyChangeListener l : psuport.getPropertyChangeListeners())
			psuport.removePropertyChangeListener(l);
		super.dispose();
	}
}
