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
package com.jaspersoft.studio.editor.preview.view.control;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.editor.preview.inputs.dialog.SortFieldSection;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class VSorting extends APreview {

	private SortFieldSection sortField;
	private Composite composite;
	private ScrolledComposite scompo;

	public VSorting(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
	}

	@Override
	protected Control createControl(Composite parent) {
		scompo = new ScrolledComposite(parent, SWT.V_SCROLL | SWT.H_SCROLL);
		scompo.setExpandHorizontal(true);
		scompo.setExpandVertical(true);
		scompo.setAlwaysShowScrollBars(false);
		scompo.setMinSize(100, 100);

		composite = new Composite(scompo, SWT.NONE);
		composite.setBackground(parent.getBackground());
		GridLayout layout = new GridLayout();
		layout.marginBottom = 20;
		composite.setLayout(layout);
		scompo.setContent(composite);

		composite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				int h = composite.getSize().y;
				composite.setSize(composite.computeSize(SWT.DEFAULT, h, true));
				composite.layout();
				scompo.setMinSize(composite.getSize());
			}

			@Override
			public void controlMoved(ControlEvent e) {

			}
		});
		return scompo;
	}

	public SortFieldSection getSortField() {
		if (sortField == null)
			sortField = new SortFieldSection();
		return sortField;
	}

	public void setJasperReports(JasperDesign jDesign, List<JRParameter> prompts, Map<String, Object> params) {
		for (Control c : composite.getChildren())
			c.dispose();

		sortField = getSortField();
		sortField.fillTable(composite, jDesign, prompts, params);
		composite.pack();
		scompo.setMinSize(composite.getSize());
	}

}
