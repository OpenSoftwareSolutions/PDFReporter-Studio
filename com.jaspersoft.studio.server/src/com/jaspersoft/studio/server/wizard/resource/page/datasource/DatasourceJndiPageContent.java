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
package com.jaspersoft.studio.server.wizard.resource.page.datasource;

import net.sf.jasperreports.data.jndi.JndiDataAdapter;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.UIUtil;

public class DatasourceJndiPageContent extends APageContent {

	private Text tname;

	public DatasourceJndiPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceJndiPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.jndi";
	}

	@Override
	public String getName() {
		return Messages.RDDatasourceJNDIPage_DatasourceTabItem;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, Messages.RDDatasourceJNDIPage_JNDIName);

		tname = new Text(composite, SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		TimeZoneProperty.addTimeZone(res, composite);

		Button importDA = new Button(composite, SWT.NONE);
		importDA.setText(Messages.RDDatasourceJNDIPage_ImportButton);
		importDA.setToolTipText(Messages.RDDatasourceJNDIPage_ImportButtonTooltip);
		importDA.setLayoutData(new GridData(SWT.RIGHT, SWT.TOP, true, false, 2, 1));
		importDA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ImportDataSourceInfoFromDA<JndiDataAdapter> dialog = new ImportDataSourceInfoFromDA<JndiDataAdapter>(Display.getDefault().getActiveShell(), "JNDI", JndiDataAdapter.class); //$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					// get information from the selected DA
					JndiDataAdapter da = dialog.getSelectedDataAdapter();
					if (da != null) {
						tname.setText(da.getDataSourceName());
					} else {
						tname.setText(""); //$NON-NLS-1$
					}
				}
			}
		});
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(res.getValue(), "jndiName")); //$NON-NLS-1$
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.adapter_jndi";
	}
}
