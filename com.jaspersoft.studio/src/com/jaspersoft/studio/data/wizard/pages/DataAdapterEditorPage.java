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
package com.jaspersoft.studio.data.wizard.pages;

import java.text.MessageFormat;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.data.ADataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.WizardEndingStateListener;

public class DataAdapterEditorPage extends WizardPage implements WizardEndingStateListener {

	private DataAdapterEditor dataAdapterEditor = null;
	private String subTitle = ""; //$NON-NLS-1$
	private Composite mainContainer = null;
	private Composite staticContainer = null;
	private Composite customContainer = null;
	private ADataAdapterComposite editorComposite = null;
	private Text textName;
	private boolean editMode = false;
	private ADataAdapterStorage storage;

	public void setStorage(ADataAdapterStorage storage) {
		this.storage = storage;
	}

	private JasperReportsContext jrContext;

	public void setJrContext(JasperReportsContext jrContext) {
		this.jrContext = jrContext;
	}

	/**
	 * Create the wizard.
	 */
	public DataAdapterEditorPage() {
		super("dataAdaptereditorpage"); //$NON-NLS-1$
		setTitle(Messages.DataAdapterEditorPage_2);
		setDescription(Messages.DataAdapterEditorPage_3);
	}

	/**
	 * Create contents of the wizard.
	 * 
	 * @param parent
	 */
	public void createControl(Composite parent) {

		mainContainer = new Composite(parent, SWT.NONE);
		setControl(mainContainer);
		mainContainer.setLayout(new GridLayout(1, false));

		staticContainer = new Composite(mainContainer, SWT.NONE);
		staticContainer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		GridLayout gl_staticContainer = new GridLayout(2, false);
		gl_staticContainer.marginHeight = 0;
		gl_staticContainer.marginWidth = 0;
		staticContainer.setLayout(gl_staticContainer);

		Label lblName = new Label(staticContainer, SWT.NONE);
		lblName.setText(Messages.DataAdapterEditorPage_4);

		textName = new Text(staticContainer, SWT.BORDER);
		textName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Label label = new Label(staticContainer, SWT.SEPARATOR | SWT.HORIZONTAL);
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 2, 1));

		customContainer = new Composite(mainContainer, SWT.NONE);
		customContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		customContainer.setLayout(new GridLayout(1, false));

		textName.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {

				String name = textName.getText().trim();
				boolean b = true;

				if (isEditMode()) { // Edit Data Adapter Mode
					b = isDataAdapterNameValid(name);
				} else if (storage != null) { // Creating New Data Adapter Mode
					b = storage.isDataAdapterNameValid(name);
				}

				setPageComplete(b);

				if (b) {
					setDescription(getSubTitle());
					setMessage(getSubTitle());

					dataAdapterEditor.getDataAdapter().getDataAdapter().setName(name);
				} else {

					if (name.length() > 0) {
						setMessage(MessageFormat.format(Messages.DataAdapterEditorPage_5, new Object[]{name}), ERROR);
					} else {
						setMessage(Messages.DataAdapterEditorPage_7, ERROR);
					}
				}
			}
		});

		setPageComplete(false);
		setMessage(Messages.DataAdapterEditorPage_8, ERROR);
	}

	@Override
	public void performHelp() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelp(dataAdapterEditor.getHelpContextId());
	}

	/*
	 * GETTERS AND SETTERS
	 */
	/**
	 * This method guesses the UI to use to edit the data adapter specified
	 * 
	 * @param newDataAdapter
	 */
	public void setDataAdapter(DataAdapterDescriptor newDataAdapterDescriptor) {

		// ?
		if (newDataAdapterDescriptor.getEditor() == dataAdapterEditor)
			return;

		setSubTitle(DataAdapterManager.findFactoryByDataAdapterClass(
				newDataAdapterDescriptor.getDataAdapter().getClass().getName()).getLabel());
		// 1. get the DataAdapterEditor
		dataAdapterEditor = newDataAdapterDescriptor.getEditor();

		// 2. add the composite from the DataAdapterEditor to the wizard page
		if (editorComposite != null) {
			editorComposite.dispose();
		}
		if (jrContext == null)
			jrContext = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), null);
		editorComposite = dataAdapterEditor.getComposite(customContainer, SWT.NULL, this, jrContext);
		editorComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		// 4. set the new dataAdapter to the DataAdapterEditor
		dataAdapterEditor.setDataAdapter(newDataAdapterDescriptor);

		// 5. fill the name if the new data adapter has one
		textName.setText(Misc.nvl(newDataAdapterDescriptor.getName(), "")); //$NON-NLS-1$

		// 6. resize the dialog properly
		customContainer.layout();

		if (getShell().isVisible()) // If the shell is not yet visible, it will layout the content by itself when displayed.
		{
			Point currentSize = customContainer.getSize();
			Point preferredSize = customContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT);

			Point windowSize = getShell().getSize();
			getShell().layout();

			getShell().setSize(new Point(windowSize.x, windowSize.y + Math.max(0, preferredSize.y - currentSize.y)));
		}
	}

	public DataAdapterDescriptor getDataAdapter() {
		DataAdapterDescriptor da = getDataAdapterEditor().getDataAdapter();
		da.getDataAdapter().setName(textName.getText());
		return da;
	}

	/**
	 * @param subTitle
	 *          the subTitle to set
	 */
	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	/**
	 * @return the subTitle
	 */
	public String getSubTitle() {
		return subTitle;
	}

	/**
	 * Returns the data adapter editor
	 * 
	 * @return dataAdapterEditor
	 */
	public DataAdapterEditor getDataAdapterEditor() {
		return dataAdapterEditor;
	}

	/**
	 * Set the DataAdapterEditorPage mode.<br>
	 * True if modifying an existing Data Adapter.<br>
	 * False if creating an new Data Adapter.
	 * 
	 * @param editMode
	 *          boolean true or false
	 */
	public void setEditMode(boolean editMode) {
		this.editMode = editMode;
	}

	/**
	 * Return the DataAdapterEditorPage mode.<br>
	 * True if modifying an existing Data Adapter.<br>
	 * False if creating an new Data Adapter.
	 * 
	 * @return true or false
	 */
	public boolean isEditMode() {
		return editMode;
	}

	/**
	 * This method is similar as <b>isDataAdapterNameValid()</b> method from {@link DataAdapterManager}. The only
	 * difference is the data adapter currently being edited is excluded from this check.
	 * 
	 * @param dataAdapterName
	 * @return true or false
	 */
	private boolean isDataAdapterNameValid(String dataAdapterName) {

		if (dataAdapterName == null || "".equals(dataAdapterName)) //$NON-NLS-1$
			return false;
		if (storage != null) {
			// remove the currently edited data adapter from the list
			// Collection<DataAdapterDescriptor> dataAdapters = storage.getDataAdapterDescriptors();
			// for (DataAdapterDescriptor dataAdapter : dataAdapters) {
			// if (dataAdapterEditor.getDataAdapter() != dataAdapter && dataAdapter.getName().equals(dataAdapterName))
			// return false;
			// }
		}
		return true;
	}

	@Override
	public void performFinishInvoked() {
		if (editorComposite != null)
			editorComposite.performAdditionalUpdates();
	}

	@Override
	public void performCancelInvoked() {
		// do nothing...
	}

}
