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
package com.jaspersoft.studio.property.dataset.wizard;

import java.text.MessageFormat;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardPage;

/**
 * This is the first page of a typical wizard to build a new dataset, and it asks for a name and for the method to
 * create the dataset (i.e. by using a data adapter or just create an empty dataset).
 * 
 * The method set the settings property {@link DatasetWizard.DATASET_NAME DATASET_NAME}
 * 
 * The wizard that uses this page should check if the user has selected to create an empty dataset or a new one in order
 * to provide correct feedback on the canFinish method.
 * 
 * @author gtoffoli
 * 
 */
public class WizardDatasetNewPage extends JSSWizardPage {

	/**
	 * Key used to store the name of the new dataset
	 */
	public static final String DATASET_NAME = "wizard_dataset_name"; //$NON-NLS-1$

	/**
	 * Key used to store a boolean value which says if the user decided to use an empty dataset or not
	 */
	public static final String DATASET_EMPTY = "wizard_dataset_empty"; //$NON-NLS-1$

	private boolean emptyDataset = false;

	private Text dsname;

	// public void setDataSet(MDataset dataset) {
	// this.dataset = dataset;
	// JRDesignDataset ct = (JRDesignDataset) dataset.getValue();
	// if (ct == null)
	// dataset.setValue(new JRDesignDataset(false));
	// }

	// public MDataset getDataSet() {
	// return dataset;
	// }

	/**
	 * Look for a JasperDesign available in the JasperReports configuration (if available)
	 * 
	 * 
	 * @return the JasperDesign being edited
	 */
	public JasperDesign getJasperDesign() {
		// Look for a jasperdesign inside the wizard JasperReports configuration...
		JasperReportsConfiguration config = (JasperReportsConfiguration) getSettings().get(
				JSSWizard.JASPERREPORTS_CONFIGURATION);

		if (config != null) {
			return config.getJasperDesign();
		}

		return null;
	}

	public boolean isEmptyDataset() {
		return emptyDataset;
	}

	public WizardDatasetNewPage() {
		super("datasetpage"); //$NON-NLS-1$
		setTitle(Messages.common_dataset);
		setImageDescriptor(MDataset.getIconDescriptor().getIcon32());
		setDescription(Messages.WizardDatasetNewPage_description);
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_DATASET_NAME;
	}

	/**
	 * Create the Page UI
	 */
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new FormLayout());

		Label lbl = new Label(composite, SWT.NONE);
		FormData fd_lbl = new FormData();
		fd_lbl.left = new FormAttachment(0, 5);
		lbl.setLayoutData(fd_lbl);
		lbl.setText(Messages.WizardDatasetNewPage_dataset_name + ":"); //$NON-NLS-1$

		dsname = new Text(composite, SWT.BORDER);
		FormData fd_dsname = new FormData();
		fd_dsname.top = new FormAttachment(0, 0);
		fd_dsname.right = new FormAttachment(100, 0);
		fd_lbl.bottom = new FormAttachment(dsname, 0, SWT.BOTTOM);
		fd_dsname.left = new FormAttachment(lbl, 5);
		dsname.setLayoutData(fd_dsname);

		// if (dataset != null) {
		// String str = (String) dataset.getPropertyValue(JRDesignDataset.PROPERTY_NAME);
		// dsname.setText(str);
		// } else

		// Look for a default dataset name...
		JasperDesign jd = getJasperDesign();
		if (jd != null) {
			dsname.setText(ModelUtils.getDefaultName(jd.getDatasetMap(), "Dataset")); //$NON-NLS-1$
		}

		dsname.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String dstext = dsname.getText();

				JasperDesign jd = getJasperDesign();

				if (dstext == null || dstext.trim().equals("")) {//$NON-NLS-1$
					setErrorMessage(Messages.WizardDatasetNewPage_validation_not_null);
					setPageComplete(false);
				} else if (jd != null && jd.getDatasetMap().get(dstext) != null) {

					String message = MessageFormat.format(Messages.WizardDatasetNewPage_name_already_exists, new Object[] { dstext });

					setErrorMessage(message); //$NON-NLS-1$ //$NON-NLS-2$
					setPageComplete(false);
				} else {
					setPageComplete(true);
					setErrorMessage(null);
					setMessage(getDescription());

					// Update dataset name...
					// dataset.setPropertyValue(JRDesignDataset.PROPERTY_NAME, dstext);
					storeSettings();
				}
			}
		});

		final Button fromConn = new Button(composite, SWT.RADIO);
		FormData fd_fromConn = new FormData();
		fd_fromConn.left = new FormAttachment(lbl, 0, SWT.LEFT);
		fromConn.setLayoutData(fd_fromConn);
		fromConn.setText(Messages.WizardDatasetNewPage_create_new_dataset);
		fromConn.setSelection(true);
		fromConn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				emptyDataset = !fromConn.getSelection();
				storeSettings();
				getWizard().getContainer().updateButtons();
			}

		});

		final Button empty = new Button(composite, SWT.RADIO);
		fd_fromConn.bottom = new FormAttachment(empty, -30);
		FormData fd_empty = new FormData();
		fd_empty.top = new FormAttachment(0, 120);
		fd_empty.left = new FormAttachment(lbl, 0, SWT.LEFT);
		empty.setLayoutData(fd_empty);
		empty.setText(Messages.WizardDatasetNewPage_create_an_empty_dataset);
		empty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				emptyDataset = !fromConn.getSelection();
				storeSettings();
				getWizard().getContainer().updateButtons();
			}

		});

		//PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), "Jaspersoft.wizard"); //$NON-NLS-1$

		storeSettings();
	}

	//
	// @Override
	// public boolean canFlipToNextPage() {
	// return isPageComplete() && !emptyDataset;
	// }

	/**
	 * Save the name of the dataset selected by the user. If there is a JasperDesign set in the JasperReports
	 * configuration, the name is also validated.
	 */
	public void storeSettings() {
		if (getSettings() == null)
			return;
		getSettings().put(DATASET_NAME, dsname.getText());
		getSettings().put(DATASET_EMPTY, emptyDataset);
	}

}
