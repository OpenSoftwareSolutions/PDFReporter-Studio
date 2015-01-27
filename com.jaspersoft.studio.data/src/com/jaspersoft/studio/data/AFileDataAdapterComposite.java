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
package com.jaspersoft.studio.data;

import java.io.File;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.eclipse.ui.validator.NotEmptyFileValidator;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationSupport;
import org.eclipse.jface.databinding.fieldassist.ControlDecorationUpdater;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.messages.Messages;

public abstract class AFileDataAdapterComposite extends ADataAdapterComposite {
	protected Text textFileName;

	public AFileDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {
		super(parent, style, jrContext);
	}

	protected void createFileNameWidgets(Composite parent) {
		Label lblNewLabel = new Label(parent, SWT.NONE);
		lblNewLabel.setText(Messages.XLSXDataAdapterComposite_0);

		textFileName = new Text(parent, SWT.BORDER);
		GridData gd = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd.horizontalIndent = 8;
		textFileName.setLayoutData(gd);

		Button btnBrowse = new Button(parent, SWT.NONE);
		GridData gd_btnBrowse = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnBrowse.widthHint = 100;
		btnBrowse.setLayoutData(gd_btnBrowse);
		btnBrowse.setText(Messages.XLSXDataAdapterComposite_1);

		/*
		 * UI ELEMENTS LISTENERS
		 */
		// browse and select the Excel file
		btnBrowse.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				FileDialog fd = new FileDialog(Display.getDefault().getActiveShell());
				fd.setFileName(textFileName.getText());
				fd.setFilterPath(root.getLocation().toOSString());
				fd.setFilterExtensions(getFileExtensions()); //$NON-NLS-1$ //$NON-NLS-2$
				String selection = fd.open();
				if (selection != null) {
					IFile contextfile = (IFile) getJrContext().getValue(FileUtils.KEY_FILE);

					IFile[] resource = root.findFilesForLocationURI(new File(selection).toURI());
					if (contextfile != null && resource != null && resource.length > 0 && contextfile.getProject().equals(resource[0].getProject()))
						selection = resource[0].getProjectRelativePath().toOSString();
					textFileName.setText(selection);
				}
			}
		});
	}

	protected abstract String[] getFileExtensions();

	protected void doBindFileNameWidget(DataAdapter dataAdapter) {
		Binding binding = bindingContext.bindValue(SWTObservables.observeText(textFileName, SWT.Modify), PojoObservables.observeValue(dataAdapter, "fileName"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new NotEmptyFileValidator(getJrContext())), null);
		ControlDecorationSupport.create(binding, SWT.TOP | SWT.LEFT, null, new ControlDecorationUpdater());
	}
}
