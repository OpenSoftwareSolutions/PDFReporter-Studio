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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.dataset.command.DeleteDatasetCommand;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.property.IPostSetValue;
import com.jaspersoft.studio.property.descriptor.parameter.dialog.ParameterDTO;

/**
 * Class used when a property is changed. Check if the changed property is a name of a dataset parameter and in this
 * case search all the associated dataset run and check if they are using that parameter. If it is so then also the
 * parameter name into the dataset run is changed
 * 
 * @author Orlandin Marco
 */
public class PostSetParameterName implements IPostSetValue {

	/**
	 * Command to change a dataset parameter name, support the undo operation
	 */
	private class SetParameterName extends Command {

		/**
		 * Dataset run with the parameter that will be changed
		 */
		private MDatasetRun element;

		/**
		 * Name of the dataset parameter before the change, stored to allow the undo operation
		 */
		private String oldName;

		/**
		 * The name that will be assigned to the dataset parameter
		 */
		private String newName;

		/**
		 * Boolean flag used to know if the MDatasetRun element has inside the parameter that has been renamed. This is used
		 * for the undo operation
		 */
		private boolean parameterFound = false;

		/**
		 * Create the command
		 * 
		 * @param element
		 *          the dataset run that can contains the parameter. If the dataset dosen't contains the searched parameter
		 *          then the command and its undo does nothing
		 * @param oldName
		 *          The name of the parameter renamed
		 * @param newName
		 *          The new name of the parameter
		 */
		public SetParameterName(MDatasetRun element, String oldName, String newName) {
			this.element = element;
			this.oldName = oldName;
			this.newName = newName;
		}

		@Override
		public void execute() {
			ParameterDTO parameters = (ParameterDTO) element.getPropertyValue(JRDesignDatasetRun.PROPERTY_PARAMETERS);
			List<JRDatasetParameter> lst = new ArrayList<JRDatasetParameter>(Arrays.asList(parameters.getValue()));
			for (JRDatasetParameter param : lst) {
				if (param.getName() != null && param.getName().equals(oldName)) {
					try {
						// The parameter is removed and readded to have JR to update its internal structure (list and map)
						element.getValue().removeParameter(param);
						((JRDesignDatasetParameter) param).setName(newName);
						element.getValue().addParameter(param);
						parameterFound = true;
					} catch (JRException e) {
						e.printStackTrace();
					}
					// element.setPropertyValue(JRDesignDatasetRun.PROPERTY_PARAMETERS, parameters);
					// The parameter was found, i mark the flag to support the undo and exit the cycle when the parameter is found
					// since the aren't two parameters with the same name
					break;
				}
			}
		}

		@Override
		public void undo() {
			if (parameterFound) {
				ParameterDTO parameters = (ParameterDTO) element.getPropertyValue(JRDesignDatasetRun.PROPERTY_PARAMETERS);
				List<JRDatasetParameter> lst = new ArrayList<JRDatasetParameter>(Arrays.asList(parameters.getValue()));
				for (JRDatasetParameter param : lst) {
					if (param.getName() != null && param.getName().equals(newName)) {
						try {
							// The parameter is removed and readded to have JR to update its internal structure (list and map)
							element.getValue().removeParameter(param);
							((JRDesignDatasetParameter) param).setName(oldName);
							element.getValue().addParameter(param);
						} catch (JRException e) {
							e.printStackTrace();
						}
						parameterFound = false;
						break;
					}
				}
			}
		}
	}

	/**
	 * Get a list of all the datasets run used by every element, and if one or more of this are references to the dataset
	 * with the changed parameter search create a command that search inside them the reference to the parameter and if
	 * found rename it
	 */
	@Override
	public Command postSetValue(IPropertySource target, Object prop, Object newValue, Object oldValue) {
		JSSCompoundCommand c = new JSSCompoundCommand(null);
		// Check if the updated element is a dataset and the updated property is the name
		if (target instanceof MParameter && prop.equals(JRDesignParameter.PROPERTY_NAME)) {
			MParameter mprm = (MParameter) target;
			if (mprm.getParent() != null && mprm.getParent().getParent() != null) {
				// Get all the references to this dataset
				ANode parentElement = mprm.getParent().getParent();
				c.setReferenceNodeIfNull(parentElement);
				if (parentElement instanceof MDataset) {
					MDataset parentDataset = (MDataset) parentElement;
					List<IDatasetContainer> references = DeleteDatasetCommand.getDatasetUsage(parentDataset.getRoot()
							.getChildren(), parentDataset.getPropertyActualValue(JRDesignDataset.PROPERTY_NAME).toString());
					for (IDatasetContainer datasetRun : references) {
						List<MDatasetRun> datasetList = datasetRun.getDatasetRunList();
						for (MDatasetRun actualDataset : datasetList) {
							c.add(new SetParameterName(actualDataset, oldValue.toString(), newValue.toString()));
						}
					}
				}
			}
		}
		return c;
	}

}
