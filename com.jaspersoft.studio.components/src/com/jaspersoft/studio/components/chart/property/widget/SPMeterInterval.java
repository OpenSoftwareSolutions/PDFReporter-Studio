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

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.util.JRMeterInterval;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptors.JSSTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

/**
 * Widget to show the meter intervals. It show the number of intervals defined and 
 * a button to open the dialog where the user can see\edit\add the intervals
 * 
 * @author Orlandin Marco
 *
 */
public class SPMeterInterval extends ASPropertyWidget {
	
	/**
	 * Text where the number of intervals on the meter chart
	 */
	protected Text ftext;
	
	/**
	 * button to open the dialog where the user can see\edit\add the intervals
	 */
	private Button btn;

	/**
	 * List of the intervals actually defined on the chart
	 */
	private List<JRMeterInterval> intervalsList;
	
	public SPMeterInterval(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return ftext;
	}
	
	public Control getButton(){
		return btn;
	}

	@Override
	public void createComponent(Composite parent) {
		Composite composite = section.getWidgetFactory().createSection(parent, Messages.SPMeterInterval_sectionTitle, true, 3);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		composite.getParent().setLayoutData(data);
		
		btn = section.getWidgetFactory().createButton(composite, "...", SWT.PUSH); //$NON-NLS-1$
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				MeterIntervalsDialog dialog = new MeterIntervalsDialog(btn.getShell(), section, pDescriptor, new ArrayList<JRMeterInterval>(intervalsList));
				if (dialog.open() == Dialog.OK){
					intervalsList = dialog.getIntervalsList();
					section.changeProperty(pDescriptor.getId(), intervalsList);
				}
			}
		});
		
		int style = SWT.NONE;
		if (pDescriptor instanceof JSSTextPropertyDescriptor)
			style = ((JSSTextPropertyDescriptor) pDescriptor).getStyle();
		ftext = section.getWidgetFactory().createText(composite, "", style); //$NON-NLS-1$

		ftext.setToolTipText(pDescriptor.getDescription());
		ftext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ftext.setEnabled(false);

	}

	@Override
	public void setData(APropertyNode pnode, Object b) {
		List<?> ilist = (List<?>) b;
		if (ilist == null) {
			ilist = new ArrayList<JRMeterInterval>();
			intervalsList = new ArrayList<JRMeterInterval>();
		} else {
			intervalsList = new ArrayList<JRMeterInterval>(ilist.size());
			for (Object mi : ilist) {
				if (mi instanceof JRMeterInterval) {
					JRMeterInterval interval = (JRMeterInterval) mi;
					intervalsList.add((JRMeterInterval) interval.clone());
				}
			}
			ilist = intervalsList;
		}
		ftext.setText(MessageFormat.format(Messages.SPMeterInterval_intervalsNumber, ilist.size()));
	}
}
