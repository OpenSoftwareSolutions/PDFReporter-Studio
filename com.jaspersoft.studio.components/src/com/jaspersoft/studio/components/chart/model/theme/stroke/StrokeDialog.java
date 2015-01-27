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
package com.jaspersoft.studio.components.chart.model.theme.stroke;

import java.awt.BasicStroke;
import java.awt.Stroke;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class StrokeDialog extends Dialog {
	private BasicStroke value;
	private Text twidth;
	private Text tdash;
	private Text tdashphase;
	private StrokeWidget sw;

	public StrokeDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Stroke");
	}

	public Stroke getValue() {
		return value;
	}

	public void setValue(Stroke value) {
		this.value = (BasicStroke) value;
	}

	private void setValidator(Text txt) {
		txt.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				try {
					if (!e.text.trim().isEmpty())
						new Float(e.text);
				} catch (Exception ex) {
					e.doit = false;
				}
			}
		});
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);
		cmp.setLayout(new GridLayout(3, false));

		new Label(cmp, SWT.NONE).setText("Line Width");

		twidth = new Text(cmp, SWT.BORDER | SWT.RIGHT);
		setValidator(twidth);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 100;
		twidth.setLayoutData(gd);
		twidth.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					float w = new Float(twidth.getText());
					float[] df = value != null ? value.getDashArray() : new float[] { 1f };
					float dph = value != null ? value.getDashPhase() : 1f;

					value = new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, df, dph);
					handleValueChanged();
				} catch (NumberFormatException ex) {
				}
			}
		});

		sw = new StrokeWidget(cmp, SWT.NONE);
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 3;
		gd.minimumHeight = 200;
		gd.minimumWidth = 200;
		// gd.heightHint = 200;
		// gd.widthHint = 200;
		sw.setLayoutData(gd);

		Label lbl = new Label(cmp, SWT.NONE);
		lbl.setText("Dash Size");
		lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		tdash = new Text(cmp, SWT.BORDER | SWT.RIGHT);
		setValidator(tdash);
		tdash.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		tdash.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					float w = value != null ? value.getLineWidth() : 1f;
					float df = new Float(tdash.getText());
					float dph = value != null ? value.getDashPhase() : 1f;

					value = new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[] { df }, dph);
					handleValueChanged();
				} catch (NumberFormatException ex) {
				}
			}
		});

		lbl = new Label(cmp, SWT.NONE);
		lbl.setText("Dash Phase");
		lbl.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		tdashphase = new Text(cmp, SWT.BORDER | SWT.RIGHT);
		setValidator(tdashphase);
		tdashphase.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		tdashphase.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				try {
					float w = value != null ? value.getLineWidth() : 1f;
					float[] df = value != null ? value.getDashArray() : new float[] { 1f };
					float dph = new Float(tdashphase.getText());

					value = new BasicStroke(w, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, df, dph);
					handleValueChanged();
				} catch (NumberFormatException ex) {
				}
			}
		});

		if (value != null) {
			twidth.setText(new Float(value.getLineWidth()).toString());
			float[] d = value.getDashArray();
			if (d != null && d.length > 0)
				tdash.setText(new Float(d[0]).toString());
			tdashphase.setText(new Float(value.getDashPhase()).toString());
		}
		handleValueChanged();
		return cmp;
	}

	private void handleValueChanged() {
		if (value == null)
			return;
		sw.setStroke(value);
		sw.setTBounds();
	}

}
