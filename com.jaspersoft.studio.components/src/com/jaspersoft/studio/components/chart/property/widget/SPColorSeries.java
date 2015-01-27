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
package com.jaspersoft.studio.components.chart.property.widget;

import java.util.Collection;
import java.util.SortedSet;

import net.sf.jasperreports.engine.base.JRBaseChartPlot.JRBaseSeriesColor;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.property.descriptor.seriescolor.SeriesColorPropertyDescriptor;
import com.jaspersoft.studio.components.chart.property.descriptor.seriescolor.dialog.SeriesColorEditor;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

public class SPColorSeries extends ASPropertyWidget {
	protected Composite composite;

	public SPColorSeries(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return composite;
	}

	protected void createComponent(Composite parent) {
		composite = section.getWidgetFactory().createComposite(parent, SWT.READ_ONLY);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.marginLeft = 1;
		layout.marginRight = 5;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		ftext = section.getWidgetFactory().createText(composite, "", SWT.LEFT);
		ftext.setToolTipText(pDescriptor.getDescription());
		setWidth(composite, 20);

		Button btn = section.getWidgetFactory().createButton(composite, "...", SWT.PUSH);
		btn.setToolTipText(pDescriptor.getDescription());
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				SeriesColorEditor wizard = new SeriesColorEditor();
				wizard.setValue(series);
				WizardDialog dialog = new WizardDialog(composite.getShell(), wizard);
				dialog.create();
				if (dialog.open() == Dialog.OK) {
					handleTextChanged(section, pDescriptor.getId(), wizard.getValue());
				}
			}
		});
	}

	protected void setWidth(Composite parent, int chars) {
		int w = getCharWidth(parent) * chars;
		if (parent.getLayout() instanceof RowLayout) {
			RowData rd = new RowData();
			rd.width = w;
			ftext.setLayoutData(rd);
		} else if (parent.getLayout() instanceof GridLayout) {
			GridData rd = new GridData(GridData.FILL_HORIZONTAL);
			rd.minimumWidth = w;
			ftext.setLayoutData(rd);
		}
	}

	protected void handleTextChanged(final AbstractSection section, final Object property, Collection<?> text) {
		section.changeProperty(property, text);
	}

	private SortedSet<JRBaseSeriesColor> series;
	private Text ftext;

	public void setData(APropertyNode pnode, Object b) {
		series = (SortedSet<JRBaseSeriesColor>) b;
		SeriesColorPropertyDescriptor pd = (SeriesColorPropertyDescriptor) pDescriptor;
		ftext.setText(pd.getLabelProvider().getText(series));
	}

}
