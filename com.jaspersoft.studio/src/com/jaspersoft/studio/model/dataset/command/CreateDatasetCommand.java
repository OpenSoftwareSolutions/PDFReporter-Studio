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
package com.jaspersoft.studio.model.dataset.command;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.property.dataset.wizard.DatasetWizard;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * /* link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateDatasetCommand extends Command {

	/** The jr dataset. */
	private JRDesignDataset jrDataset;

	/** The jr design. */
	private JasperDesign jrDesign;
	private JasperReportsConfiguration jConfig;

	/** The index. */
	private int index;

	public CreateDatasetCommand(JasperReportsConfiguration jConfig, JRDesignDataset jrDataset) {
		super();
		this.jrDataset = jrDataset;
		this.jrDesign = jConfig.getJasperDesign();
		this.jConfig = jConfig;
		index = -1;
	}

	/**
	 * Instantiates a new creates the dataset command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateDatasetCommand(MReport destNode, MDataset srcNode, int index) {
		super();
		this.jrDesign = destNode.getJasperDesign();
		this.jConfig = destNode.getJasperConfiguration();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrDataset = (JRDesignDataset) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrDataset != null) {
			try {
				if (index < 0 || index > jrDesign.getDatasetsList().size())
					jrDesign.addDataset(jrDataset);
				else
					jrDesign.addDataset(index, jrDataset);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * If no jrDataset has been set yet, run the Wizard to create a JRDesignDataset...
	 */
	protected void createObject() {
		if (jrDataset == null) {
			DatasetWizard wizard = new DatasetWizard();
			WizardDialog dialog = new WizardDialog(Display.getCurrent().getActiveShell(), wizard);
			wizard.setConfig(jConfig);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				jrDataset = wizard.getDataset();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrDesign.removeDataset(jrDataset);
	}
}
