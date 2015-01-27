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

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.command.CreateDatasetCommand;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardPageChangeEvent;
import com.jaspersoft.studio.wizards.WizardUtils;

/**
 * A Wizard to create an Dataset. The dataset can be get from the wizard using {@link #getDataset() getDataset} which
 * returns a {@link com.jaspersoft.studio.model.dataset.MDataset}.
 * 
 * 
 * @author gtoffoli
 * 
 */
public class DatasetWizard extends JSSWizard {

	/**
	 * Key used to store the wizard (MDataset) inside the wizard settings...
	 */
	public static final String DATASET = "datasetnamewizard"; //$NON-NLS-1$

	// Steps involved in the creation of a simple dataset
	private WizardDatasetNewPage step1;
	private WizardDataSourcePage step2;
	private WizardFieldsPage step3;
	private WizardFieldsGroupByPage step4;

	public DatasetWizard() {
		super();
		setWindowTitle(Messages.common_dataset);
		setNeedsProgressMonitor(true);
	}

	public DatasetWizard(IWizard parentWizard, IWizardPage fallbackPage) {
		super(parentWizard, fallbackPage);
		setWindowTitle(Messages.common_dataset);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {

		step1 = new WizardDatasetNewPage();
		addPage(step1);

		// FIXME: wizard dataset broken after wizard changes
		step2 = new WizardDataSourcePage(); // getConfig());
		addPage(step2);

		step3 = new WizardFieldsPage();
		addPage(step3);

		step4 = new WizardFieldsGroupByPage();
		addPage(step4);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		if (page == step1) {

			// Go directly to the end if the user asks to create an empty dataste...
			if (step1.isEmptyDataset()) {
				page = step4;
			}
		}

		return super.getNextPage(page);
	}

	/**
	 * This is the method that builds a JRDesignDataset from the settings get during the wizard steps. This method returns
	 * a new dataset instance each time, there is no cached object.
	 * 
	 * @return
	 * @throws JRException
	 */
	public JRDesignDataset getDataset() {
		JRDesignDataset ds = WizardUtils.createDataset(getConfig(), false, getSettings());
		if (step2 != null && step2.getDataAdapter() != null) {
			// Save the information on the default data adapter
			// to propose selected for this specific dataset
			ds.getPropertiesMap().setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, step2.getDataAdapter().getName());
		}
		return ds;
	}

	/**
	 * This method force the user to go next and not finish if the selection in the first step is not an empty dataset...
	 */
	@Override
	public boolean canFinish() {

		if (getContainer().getCurrentPage() == step1) {
			if (step1.isPageComplete() && step1.isEmptyDataset()) {
				return true;
			}
			return false;
		}

		return super.canFinish();
	}

	/**
	 * Get the dataset, and add the command to add the dataset to the model to set stack of commands of this wizard.
	 * 
	 * The user will need to invoke the commands queue in order to actually add the dataset to the document.
	 * 
	 */
	@Override
	public boolean performFinish() {
		addCommand(new CreateDatasetCommand(getConfig(), getDataset()));
		return super.performFinish();
	}

	@Override
	public void pageChanged(JSSWizardPageChangeEvent event) {

		if (event.getPage() == step2) {

			// If there are no fields, remove next step...
			if (step2.requireElaboration() == false) {
				removePage(step3);
				removePage(step4);
			} else {
				if (!getPageList().contains(step3)) {
					addPage(step3);
					addPage(step4);
				}
			}

			event.getPage().setPageComplete(event.getPage().isPageComplete());

		}
	}

}
