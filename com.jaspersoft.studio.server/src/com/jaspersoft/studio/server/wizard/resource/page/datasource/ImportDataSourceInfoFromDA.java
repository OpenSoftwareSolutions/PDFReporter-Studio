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

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import net.sf.jasperreports.data.DataAdapter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.server.messages.Messages;

/**
 * Popup dialog that allows to select an existing JSS data adapter to retrieve
 * the information that can be used to create a data source resource on
 * JasperServer.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ImportDataSourceInfoFromDA<T extends DataAdapter> extends Dialog {
	private static final String _DA = "_DA";
	/* selected data adapter */
	private T selectedDA;
	/* text info on data adapter kind */
	private String daKind;
	/* class type for the kind of data adapter(s) we are looking for */
	private Class<T> daClass;

	public ImportDataSourceInfoFromDA(Shell parentShell, String daKind,
			Class<T> daClass) {
		super(parentShell);
		this.daKind = daKind;
		this.daClass = daClass;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.ImportDataSourceInfoFromDA_DialogTitle);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout());

		Label lblInfo = new Label(container, SWT.NONE);
		lblInfo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lblInfo.setText(MessageFormat.format(
				Messages.ImportDataSourceInfoFromDA_InfoLabel,
				new Object[] { daKind }));

		final Combo combo = new Combo(container, SWT.READ_ONLY);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addAdapters(DataAdapterManager.getPreferencesStorage(), combo);
		List<ADataAdapterStorage> das = DataAdapterManager.getProjectStorages();
		for (ADataAdapterStorage d : das) {
			addAdapters(d, combo);
		}
		combo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectedDA = (T) combo.getData(combo.getSelectionIndex() + _DA);
			}
		});
		if (combo.getItemCount() > 0) {
			combo.select(0);
			selectedDA = (T) combo.getData("0" + _DA);
		}

		return container;
	}

	private void addAdapters(ADataAdapterStorage das, Combo combo) {
		Collection<DataAdapterDescriptor> dataAdapterDescriptors = das
				.getDataAdapterDescriptors();
		for (DataAdapterDescriptor da : dataAdapterDescriptors) {
			DataAdapter dataAdapter = da.getDataAdapter();
			if (daClass.isInstance(dataAdapter)) {
				combo.add(das.getLabel(da));
				combo.setData(combo.getItemCount() - 1 + _DA, dataAdapter);
			}
		}
	}

	public T getSelectedDataAdapter() {
		return selectedDA;
	}
}
