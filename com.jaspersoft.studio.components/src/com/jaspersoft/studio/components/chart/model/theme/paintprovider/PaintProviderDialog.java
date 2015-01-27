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

import java.awt.Color;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.jasperreports.chartthemes.simple.ColorProvider;
import net.sf.jasperreports.chartthemes.simple.GradientPaintProvider;
import net.sf.jasperreports.chartthemes.simple.PaintProvider;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class PaintProviderDialog extends Dialog {
	private PaintProvider value;
	private Button bgrad;

	public PaintProviderDialog(Shell parent) {
		super(parent);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Paint Provider");
	}

	public PaintProvider getValue() {
		if (value instanceof ColorProvider)
			value = new ColorProvider(((ColorProvider) value).getColor());
		else if (value instanceof GradientPaintProvider)
			value = new GradientPaintProvider(((GradientPaintProvider) value).getColor1(), ((GradientPaintProvider) value).getColor2());
		return value;
	}

	public void setValue(PaintProvider value) {
		this.value = value;
		if (value instanceof ColorProvider) {
			cp = (ColorProvider) value;
			c1 = cp.getColor();
		} else if (value instanceof GradientPaintProvider) {
			gpp = (GradientPaintProvider) value;
			c1 = gpp.getColor1();
			c2 = gpp.getColor2();
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);
		cmp.setLayout(new GridLayout(2, false));

		new Label(cmp, SWT.NONE).setText("Color");

		cw1 = new ColorWidget(cmp, SWT.NONE);
		cw1.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cw1.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				c1 = Colors.getAWT4SWTRGBColor((AlfaRGB) arg0.getNewValue());
				if (gpp == null && cp == null) {
					cp = new ColorProvider(c1);
					value = cp;
				}
				if (cp != null)
					cp.setColor(c1);
				if (gpp != null)
					gpp.setColor1(c1);
			}
		});

		bgrad = new Button(cmp, SWT.CHECK);
		bgrad.setText("Use Gradient Color");
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bgrad.setLayoutData(gd);

		lbl = new Label(cmp, SWT.NONE);
		lbl.setText("To Color");

		cw2 = new ColorWidget(cmp, SWT.NONE);
		cw2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		cw2.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				c2 = Colors.getAWT4SWTRGBColor((AlfaRGB) arg0.getNewValue());
				gpp.setColor2(c2);
			}
		});

		bgrad.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (bgrad.getSelection()) {
					if (c1 == null)
						c1 = new Color(0, 0, 0);
					if (c2 == null && c1 != null)
						c2 = new Color(c1.getRed(), c1.getGreen(), c1.getBlue());

					if (gpp == null)
						gpp = new GradientPaintProvider(c1, c2);
					else
						gpp.setColor1(c1);
					value = gpp;
				} else {
					if (cp == null)
						cp = new ColorProvider(c1);
					else
						cp.setColor(c1);
					value = cp;
				}
				handleTypeChanged();
			}
		});
		bgrad.setSelection(gpp != null);

		handleTypeChanged();
		return cmp;
	}

	private Color c1;
	private Color c2;
	private ColorProvider cp;
	private GradientPaintProvider gpp;

	private ColorWidget cw1;
	private ColorWidget cw2;
	private Label lbl;

	private void handleTypeChanged() {
		cw1.setColor(Colors.getRGB4AWTColor(c1));
		cw2.setColor(Colors.getRGB4AWTColor(c2));
		cw2.setEnabled(bgrad.getSelection());
		lbl.setEnabled(bgrad.getSelection());
	}
}
