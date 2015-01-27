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
package com.jaspersoft.studio.model.band;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.dataset.command.DeleteDatasetCommand;
import com.jaspersoft.studio.property.IPostSetValue;

/**
 * Class used when a property is changed. Check if the changed property is a name 
 * of a dataset and in this case ask if the name should be updated in all its references
 * 
 * @author Orlandin Marco
 */
public class PostSetDatasetName implements IPostSetValue {

	/**
	 * Command to change the dataset name, support the undo operation
	 */
	private class SetDatasetRunName extends Command{
		
		/**
		 * Element where the name will be changed
		 */
		private MDatasetRun element;
		
		/**
		 * Name of the dataset before the change, stored to allow the undo operation
		 */
		private String oldName;
		
		/**
		 * The name that will be assigned to the dataset
		 */
		private String newName;
		
		/**
		 * 
		 */
		public SetDatasetRunName(MDatasetRun element, String oldName, String newName){
			this.element = element;
			this.oldName = oldName;
			this.newName = newName;
		}
		
		@Override
		public void execute() {
			element.setPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME, newName);
		}
		
		@Override
		public void undo() {
			element.setPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME, oldName);
		}
		
	}
	
	/**
	 * Get a list of all the datasets used by every element, and if one or more of this are references to the dataset 
	 * with the changed name ask if the user want to refactor the name inside the project
	 */
	@Override
	public Command postSetValue(IPropertySource target, Object prop, Object newValue, Object oldValue) {
		JSSCompoundCommand c = new JSSCompoundCommand(null);
		c.setReferenceNodeIfNull(target);
		//Check if the updated element is a dataset and the updated property is the name
		if (target instanceof MDataset && prop.equals(JRDesignDataset.PROPERTY_NAME)) {
			//Get all the references to this dataset
			List<IDatasetContainer> references = DeleteDatasetCommand.getDatasetUsage(((MDataset)target).getRoot().getChildren(), oldValue.toString());
			if (references.size()>0){
				boolean selectedYes = UIUtils.showConfirmation(Messages.PostSetDatasetName_title, Messages.PostSetDatasetName_message);
				if (selectedYes){
					for(IDatasetContainer datasetRun : references){
						List<MDatasetRun> datasetList = datasetRun.getDatasetRunList();
						for (MDatasetRun actualDataset : datasetList){
							if (actualDataset != null && oldValue.toString().equals(actualDataset.getPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME)))
								c.add(new SetDatasetRunName(actualDataset, oldValue.toString(), newValue.toString()));
						}
					}
				}
			}
		}
		return c;
	}

}
