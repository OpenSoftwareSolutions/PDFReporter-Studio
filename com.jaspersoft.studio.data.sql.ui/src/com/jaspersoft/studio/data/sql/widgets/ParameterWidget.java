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
package com.jaspersoft.studio.data.sql.widgets;

import java.util.List;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.sql.model.query.operand.ParameterPOperand;

public class ParameterWidget extends AOperandWidget<ParameterPOperand> {
	private Combo params;

	public ParameterWidget(Composite parent, ParameterPOperand operand) {
		super(parent, SWT.NONE, operand);
	}

	@Override
	protected void createWidget(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 2;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 0;
		setLayout(layout);

		params = new Combo(this, SWT.READ_ONLY | SWT.SINGLE);
		params.setItems(getParameterNames());
		params.setToolTipText(getValue().toSQLString());
		params.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		params.select(getParametrNameIndex());
		doSelection();

		params.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doSelection();
			}

		});
	}

	protected void doSelection() {
		JRDesignDataset jd = getValue().getJrDataset();
		JRParameter p = jd.getParametersMap().get(params.getItem(params.getSelectionIndex()));
		getValue().setJrParameter((JRDesignParameter) p, jd);
	}

	private int getParametrNameIndex() {
		List<JRParameter> parametersList = getValue().getJrDataset().getParametersList();
		JRDesignParameter p = getValue().getJrParameter();
		if (p != null)
			return parametersList.indexOf(p);
		return 0;
	}

	private String[] getParameterNames() {
		ParameterPOperand v = getValue();
		if (v != null) {
			List<JRParameter> parametersList = v.getJrDataset().getParametersList();
			String[] res = new String[parametersList.size()];
			for (int i = 0; i < parametersList.size(); i++)
				res[i] = parametersList.get(i).getName();
			return res;
		}
		return new String[0];
	}

}
