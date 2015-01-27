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
package com.jaspersoft.studio.components.chart.editor.action;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.XmlChartThemeExtensionsRegistryFactory;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.SaveAsDialog;

import com.jaspersoft.studio.components.chart.editor.ChartThemeEditor;

public class ExportJar extends Action {
	public static final String ID = "EXPORTCHARTHEMEJAR";
	private ChartThemeEditor editor;

	public ExportJar(ChartThemeEditor editor) {
		super();
		setId(ID);
		this.editor = editor;
	}

	private boolean addtoclasspath = true;
	private String name;

	@Override
	public void run() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(Display.getDefault().getActiveShell()) {
			@Override
			protected Control createDialogArea(Composite parent) {
				Composite cmp = (Composite) super.createDialogArea(parent);

				Composite composite = new Composite(cmp, SWT.NONE);
				GridLayout layout = new GridLayout(2, false);
				layout.marginWidth = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_MARGIN);
				layout.horizontalSpacing = convertHorizontalDLUsToPixels(IDialogConstants.HORIZONTAL_SPACING);
				composite.setLayout(layout);
				composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

				new Label(composite, SWT.NONE).setText("Theme Name");

				final Text txt = new Text(composite, SWT.BORDER);
				txt.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
				txt.addModifyListener(new ModifyListener() {

					@Override
					public void modifyText(ModifyEvent e) {
						name = txt.getText();
					}
				});
				txt.setText(getChartThemeName());

				final Button btn = new Button(composite, SWT.CHECK);
				btn.setText("Add the jar to the CLASSPATH to use the theme in the report designer.");
				btn.setSelection(true);
				btn.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						addtoclasspath = btn.getSelection();
					}
				});
				GridData gd = new GridData();
				gd.horizontalSpan = 2;
				btn.setLayoutData(gd);
				return cmp;
			}
		};
		saveAsDialog.setOriginalName(editor.getEditorInput().getName().replaceFirst(FileExtension.PointJRCTX, ".jar"));
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				IProgressMonitor monitor = editor.getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor();
				monitor.beginTask("Exporting Chart Themes to a JAR", IProgressMonitor.UNKNOWN);
				try {
					ChartThemeSettings cts = editor.getChartThemeSettings();
					File f = new File(file.getRawLocationURI());
					f.createNewFile();
					XmlChartThemeExtensionsRegistryFactory.saveToJar(cts, name, f);
					if (addtoclasspath)
						ProjectUtil.addFileToClasspath(monitor, file);
					UIUtils.showInformation("Chart Theme was generated");
					file.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
				} catch (UnsupportedEncodingException e) {
					UIUtils.showError(e);
				} catch (IOException e) {
					UIUtils.showError(e);
				} catch (CoreException e) {
					UIUtils.showError(e);
				}
				monitor.done();
			}
		}

	}

	protected String getChartThemeName() {
		return editor.getEditorInput().getName().replaceAll(FileExtension.PointJRCTX, "");
	}
}
