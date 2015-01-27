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
package com.jaspersoft.studio.server.wizard.imp;

import net.sf.jasperreports.eclipse.ui.validator.NotEmptyFileValidator;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.Misc;

public class ImportMetadataPage extends WizardPage {

	private Button bupdate;
	private Button bSkipUpd;
	private Button bIncAuditEvt;
	private Button bIncAccEvt;
	private Button bIncMonEvt;
	private Button bIncSrvSettings;
	private Text tfile;

	private DataBindingContext bindingContext;
	private ImportOptions value = new ImportOptions();

	protected ImportMetadataPage() {
		super("importmetadata"); //$NON-NLS-1$
		setTitle(Messages.ImportMetadataPage_0);
		setDescription(Messages.ImportMetadataPage_1);
		bindingContext = new DataBindingContext();
	}

	@Override
	public void createControl(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		setControl(cmp);

		Label lbl = new Label(cmp, SWT.NONE);
		lbl.setText(Messages.ImportMetadataPage_2);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		tfile = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		tfile.setLayoutData(gd);

		Button bfile = new Button(cmp, SWT.PUSH);
		bfile.setText(Messages.common_browse);
		bfile.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
				// fd.setFileName(textFileName.getText());
				fd.setFilterPath(root.getLocation().toOSString());
				fd.setFilterExtensions(new String[] { "*.zip", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$  
				String selection = fd.open();
				tfile.setText(Misc.nvl(selection));
			}
		});

		lbl = new Label(cmp, SWT.NONE);
		lbl.setText(Messages.ImportMetadataPage_3);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		bupdate = new Button(cmp, SWT.CHECK);
		bupdate.setText(Messages.ImportMetadataPage_4);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bupdate.setLayoutData(gd);
		bupdate.setSelection(true);

		bSkipUpd = new Button(cmp, SWT.CHECK);
		bSkipUpd.setText(Messages.ImportMetadataPage_5);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		gd.horizontalIndent = 20;
		bSkipUpd.setLayoutData(gd);
		bupdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				bSkipUpd.setEnabled(bupdate.getSelection());
			}
		});

		bIncAuditEvt = new Button(cmp, SWT.CHECK);
		bIncAuditEvt.setText(Messages.ImportMetadataPage_6);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bIncAuditEvt.setLayoutData(gd);
		bIncAuditEvt.setSelection(true);

		bIncAccEvt = new Button(cmp, SWT.CHECK);
		bIncAccEvt.setText(Messages.ImportMetadataPage_7);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bIncAccEvt.setLayoutData(gd);
		bIncAccEvt.setSelection(true);

		bIncMonEvt = new Button(cmp, SWT.CHECK);
		bIncMonEvt.setText(Messages.ImportMetadataPage_8);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bIncMonEvt.setLayoutData(gd);
		bIncMonEvt.setSelection(true);

		bIncSrvSettings = new Button(cmp, SWT.CHECK);
		bIncSrvSettings.setText(Messages.ImportMetadataPage_9);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bIncSrvSettings.setLayoutData(gd);

		Binding binding = bindingContext.bindValue(SWTObservables.observeText(tfile, SWT.Modify), PojoObservables.observeValue(value, "file"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new NotEmptyFileValidator(null)), null);
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT, null, new ControlDecorationUpdater());

		bindingContext.bindValue(SWTObservables.observeSelection(bupdate), PojoObservables.observeValue(value, "update")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bSkipUpd), PojoObservables.observeValue(value, "skipUserUpdates")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bIncAuditEvt), PojoObservables.observeValue(value, "inclAuditEvents")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bIncAccEvt), PojoObservables.observeValue(value, "inclAccessEvents")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bIncMonEvt), PojoObservables.observeValue(value, "inclMonitorEvents")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(bIncSrvSettings), PojoObservables.observeValue(value, "inclSrvSettings")); //$NON-NLS-1$ 
	}

	public ImportOptions getValue() {
		return value;
	}
}
