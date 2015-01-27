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
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Command to copy a dataset inside a specific jrDesign
 * 
 * @author Orlandin Marco
 * 
 */
public class CopyDatasetCommand extends Command {

	/** The jr dataset. */
	private JRDesignDataset originalDataset;

	/** The jr dataset. */
	private JRDesignDataset newDataset = null;

	/** The jr design. */
	private JasperDesign jrDesign;

	/**
	 * Create the command, the dataset will be created in the same design of the original dataset
	 * 
	 * @param originalDataset
	 *          the original dataset
	 */
	public CopyDatasetCommand(MDataset originalDataset) {
		super();
		this.originalDataset = originalDataset.getValue();
		jrDesign = originalDataset.getJasperDesign();
	}

	/**
	 * Create the command, the dataset will be created in the specified design
	 * 
	 * @param originalDataset
	 *          the original dataset
	 * @param design
	 *          where the dataset will be created, if null the one from the original dataset will be used
	 */
	public CopyDatasetCommand(MDataset originalDataset, JasperDesign parentDesign) {
		super();
		this.originalDataset = originalDataset.getValue();
		if (parentDesign != null)
			jrDesign = parentDesign;
		else
			jrDesign = originalDataset.getJasperDesign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		newDataset = (JRDesignDataset) originalDataset.clone();
		boolean operationAborted = false;
		try {
			while (jrDesign.getDatasetMap().containsKey(newDataset.getName()) && !operationAborted) {
				String defaultName = ModelUtils.getDefaultName(jrDesign.getDatasetMap(), "CopyOfDataset_"); //$NON-NLS-1$
				InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
						Messages.CreateFieldCommand_field_name, Messages.CreateFieldCommand_field_name_text_dialog, defaultName,
						null);
				if (dlg.open() == InputDialog.OK) {
					newDataset.setName(dlg.getValue());
				} else
					operationAborted = true;
			}
			if (!operationAborted)
				jrDesign.addDataset(newDataset);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean canExecute() {
		return (originalDataset != null && jrDesign != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return (newDataset != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrDesign.removeDataset(newDataset);
	}
}
