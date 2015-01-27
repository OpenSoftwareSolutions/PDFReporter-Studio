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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.MessageDialog;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteDatasetCommand extends Command {

	/** The jr design. */
	private JasperDesign jrDesign;

	/** The jr dataset. */
	private JRDesignDataset jrDataset;
	
	private MReport destNode;
	
	private MDataset srcNode;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete dataset command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteDatasetCommand(MReport destNode, MDataset srcNode) {
		super();
		this.jrDesign = srcNode.getJasperDesign();
		this.jrDataset = (JRDesignDataset) srcNode.getValue();
		this.destNode = destNode;
		this.srcNode = srcNode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrDesign.getDatasetsList().indexOf(jrDataset);
		//Check if the dataset is used somewhere and in that case show a warning message
		int selection = 0;
		List<IDatasetContainer> nodeUsingDataset = getDatasetUsage(destNode.getChildren(), srcNode.getPropertyValue(JRDesignDataset.PROPERTY_NAME).toString());
		if (nodeUsingDataset.size() > 0){
			MessageDialog dialog = new MessageDialog(UIUtils.getShell(), Messages.DeleteDatasetCommand_title, null,
					Messages.DeleteDatasetCommand_message, MessageDialog.WARNING, new String[] { Messages.DeleteDatasetCommand_yesOption,Messages.DeleteDatasetCommand_noOption}, 1); 
			selection = dialog.open();
		}
		if (selection == 0)
			jrDesign.removeDataset(jrDataset);
	}

	/**
	 * Return a not null list of elements that are using the dataset
	 */
	public static List<IDatasetContainer> getDatasetUsage(List<INode> children, String datasetName){
		List<IDatasetContainer> result = new ArrayList<IDatasetContainer>();
		if (datasetName != null){
			for(INode child : children){
				if (child instanceof IDatasetContainer){
					List<MDatasetRun> datasets = ((IDatasetContainer)child).getDatasetRunList();
					MDatasetRun dataset = checkContains(datasets, datasetName);
					if (dataset != null)
						result.add((IDatasetContainer)child);
				} else if (child instanceof IContainer){
					result.addAll(getDatasetUsage(child.getChildren(), datasetName));
				}
			}
		}
		return result;
	}
	
	public static MDatasetRun checkContains(List<MDatasetRun> availabeDatasets, String searchedName){
		if (availabeDatasets != null && !availabeDatasets.isEmpty()){
			for (MDatasetRun actualDataset : availabeDatasets){
				if (actualDataset != null && searchedName != null && searchedName.equals(actualDataset.getPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME))){
					return actualDataset;
				}
			}
		}
		return null;
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrDesign == null || jrDataset == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		try {
			if (elementPosition < 0 || elementPosition > jrDesign.getDatasetsList().size())
				jrDesign.addDataset(jrDataset);
			else
				jrDesign.addDataset(elementPosition, jrDataset);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
