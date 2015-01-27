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
package com.jaspersoft.studio.wizards.obj2text;

import net.sf.jasperreports.engine.type.CalculationEnum;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.EnumHelper;

public class Obj2TextPage extends WizardPage {
	private CalculationEnum calculation;
	private String[] names;

	public Obj2TextPage(String[] names) {
		super("obj2text"); //$NON-NLS-1$
		setTitle(Messages.Obj2TextPage_title);
		setDescription(Messages.Obj2TextPage_description);
		this.names = names;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);
		setControl(composite);

		final List lst = new List(composite, SWT.BORDER);
		lst.setItems(names);
		lst.setLayoutData(new GridData(GridData.FILL_BOTH));
		lst.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int sel = lst.getSelectionIndex();
				// recall that we are using TRANSLATED names!
				calculation = 
						(CalculationEnum) EnumHelper.getEnumByTranslatedName(CalculationEnum.values(), names[sel]);
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		lst.setSelection(0);

		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard");//$NON-NLS-1$
	}

	public CalculationEnum getCalculation() {
		return calculation;
	}

}
