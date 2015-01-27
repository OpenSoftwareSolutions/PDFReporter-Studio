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
package com.jaspersoft.studio.server.wizard.resource.page.runit;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.utils.UIUtil;

public class ReportUnitInputControlContent extends ReportUnitContent {

	public ReportUnitInputControlContent(ANode parent, MResource resource,
			DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ReportUnitInputControlContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getName() {
		return Messages.RDReportUnitPage_inputcontrols;
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.runit.ic";
	}
	
	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editReportUnitICContent";
	}


	@Override
	public boolean isPageComplete() {
		return true;
	}

	@Override
	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, Messages.RDReportUnitPage_controlslayout);

		Combo cictype = new Combo(composite, SWT.BORDER);
		cictype.setItems(new String[] { Messages.RDReportUnitPage_popupscreen,
				Messages.RDReportUnitPage_separatepage,
				Messages.RDReportUnitPage_topofpage,
				Messages.RDReportUnitPage_inpage });

		UIUtil.createLabel(composite, ""); //$NON-NLS-1$

		Button ispromp = new Button(composite, SWT.CHECK);
		ispromp.setText(Messages.RDReportUnitPage_alwaysprompt);
		ispromp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite,
				Messages.RDReportUnitPage_jsptoruninputcontrol);

		Text jspic = new Text(composite, SWT.BORDER);
		jspic.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		jspic.setToolTipText(Messages.RDReportUnitPage_withintooltip);

		ReportProxy v = getProxy(res.getValue());
		bindingContext.bindValue(
				SWTObservables.observeSingleSelectionIndex(cictype),
				PojoObservables.observeValue(v, "layoutControl")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(jspic, SWT.Modify),
				PojoObservables.observeValue(v, "jspIC")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(ispromp),
				PojoObservables.observeValue(v, "allowPrompt")); //$NON-NLS-1$

		res.getChildren();
		return composite;
	}

}
