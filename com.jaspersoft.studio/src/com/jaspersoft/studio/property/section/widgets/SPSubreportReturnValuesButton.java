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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.JReportsDTO;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.property.descriptor.subreport.returnvalue.dialog.RVPropertyEditor;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * A button that when clicked open the edit query dialog
 * 
 * @author Orlandin Marco
 * 
 */
public class SPSubreportReturnValuesButton extends ASPropertyWidget {

	/**
	 * The button control
	 */
	private Button button;

	/**
	 * The query of the report
	 */
	private JReportsDTO dto;

	/**
	 * The main dataset of the report
	 */
	private MSubreport msubreport;

	/**
	 * 
	 * @param parent
	 * @param section
	 * @param pDescriptor
	 * @param buttonText
	 *          text on the button
	 */
	public SPSubreportReturnValuesButton(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor,
			String buttonText) {
		super(parent, section, pDescriptor);
		createButton(parent, buttonText);
	}

	@Override
	protected void createComponent(Composite parent) {
	}

	/**
	 * Build the button
	 * 
	 * @param parent
	 *          composite where is placed
	 * @param buttonText
	 *          text on the button
	 */
	protected void createButton(Composite parent, String buttonText) {
		button = section.getWidgetFactory().createButton(parent, buttonText, SWT.PUSH);
		button.setToolTipText(pDescriptor.getDescription());
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				RVPropertyEditor wizard = new RVPropertyEditor();
				JReportsDTO value = new JReportsDTO();
				value.setjConfig(dto.getjConfig());
				value.setProp1(dto.getProp1());
				value.setValue(dto.getValue());
				wizard.setValue(value);
				WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
				dialog.create();
				if (dialog.open() == Dialog.OK)
					section.changeProperty(pDescriptor.getId(), wizard.getValue());
			}
		});
	}

	@Override
	public void setData(APropertyNode pnode, Object value) {
		if (pnode instanceof MSubreport)
			msubreport = (MSubreport) pnode;
		if (value instanceof JReportsDTO)
			dto = (JReportsDTO) value;
	}

	@Override
	public Control getControl() {
		return button;
	}

}
