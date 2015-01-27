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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.dataset.command.DeleteDatasetCommand;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Wizard page to create the domain parameters for the selected dataset, the main dataset
 * and all the dataset run associated with the selected dataset. The parameters are created
 * only where needed
 * 
 * @author Orlandin Marco
 *
 */
public class ConnectToDomainWizardPage extends JSSHelpWizardPage {

	/**
	 * The selected dataset
	 */
	private MDataset connectedDataset;
	
	/**
	 * A list of element that have a dataset run connected to the selected dataset
	 */
	private List<IDatasetContainer> runReferences;
	
	/**
	 * List of needed parameters missing on the main dataset
	 */
	private List<String> missingParamOnMain;
	
	/**
	 * List of needed parameters missing on the selected dataset
	 */
	private List<String> missingParamOnDataset;
	
	/**
	 * Contains every dataset run for the selected dataset (reference of the dataset run uses as key) and 
	 * as value has an info container. The info container basically contains a list of the parameters needed
	 * by the dataset run and the element that contains the dataset run
	 */
	private HashMap<MDatasetRun, InfoContainer> missingParamOnRun;
	
	/**
	 * Inner class used to store some data about a dataset run
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class InfoContainer{
		
		/**
		 * a Dataset run reference
		 */
		private MDatasetRun datasetRun;
		
		/**
		 * List of parameters missing from the referenced dataset run
		 */
		private List<String> missingParameters;
		
		/**
		 * Reference to the element from where the referenced dataset run is taken
		 */
		private IDatasetContainer container;
		
		/**
		 * Create the container 
		 * 
		 * @param missingParameters List of parameters missing from the referenced dataset run
		 * @param container Reference to the element from where the referenced dataset run is taken
		 * @param datasetRun a Dataset run reference
		 */
		public InfoContainer(List<String> missingParameters, IDatasetContainer container, MDatasetRun datasetRun){
			this.missingParameters = missingParameters;
			this.container = container;
			this.datasetRun = datasetRun;
		}
		
		/**
		 * Return the referenced dataset run
		 * 
		 * @return an MDataset run or null
		 */
		public MDatasetRun getRun(){
			return datasetRun;
		}
		
		/**
		 * Return the parameters missing from the dataset run
		 * 
		 * @return list of string containing the name of the parameters missing inside the dataset run or null
		 */
		public List<String> getMissingParameters(){
			return missingParameters;
		}
		
		/**
		 * Return the element that contains the referenced dataset run
		 * 
		 * @return an IDatasetContainer (probably it will be a node) or null
		 */
		public IDatasetContainer getContainer(){
			return container;
		}
	}
	
	/**
	 * Static list of the needed parameters names
	 */
	public static final String[] NEDDED_PARAMETERS = new String[]{"slFactory","slSchema","LoggedInUser","LoggedInUsername"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$

	/**
	 * Create the page 
	 * 
	 * @param connectedDataset Dataset selected by the user
	 */
	public ConnectToDomainWizardPage(MDataset connectedDataset) {
		super("connectionpage"); //$NON-NLS-1$
		setTitle(Messages.ConnectToDomainWizardPage_dialogTitle);
		setDescription(Messages.ConnectToDomainWizardPage_dialogDescription);
		this.connectedDataset = connectedDataset;
	}
	
	@Override
	public void createControl(Composite parent) {
		
		//Calculate which parameters are needed and where they are needed
		String datasetName = (String)connectedDataset.getPropertyActualValue(JRDesignDataset.PROPERTY_NAME);
		runReferences = DeleteDatasetCommand.getDatasetUsage(connectedDataset.getRoot().getChildren(), datasetName);
		missingParamOnMain = getMissingParameterOnMainDataset();
		missingParamOnDataset = getMissingParameterOnDataset();
		missingParamOnRun = getMissingDatasetsRun();
		
		//Create the appropriate controls for this parameters
	  ScrolledComposite scrollComposite = new ScrolledComposite(parent, SWT.V_SCROLL);
	  scrollComposite.setExpandVertical(true);
		Composite mainComposite = new Composite(scrollComposite, SWT.NONE);
		scrollComposite.setContent(mainComposite);
		mainComposite.setLayout(new GridLayout(1,false));
		if (missingParamOnMain.isEmpty() && missingParamOnRun.isEmpty() && missingParamOnRun.isEmpty()){
			new Label(mainComposite, SWT.NONE).setText(Messages.ConnectToDomainWizardPage_noChangesLabel);
		} else createNotEmptyContent(mainComposite);
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		scrollComposite.setMinSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		mainComposite.setSize(mainComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
    UIUtils.resizeAndCenterShell(parent.getShell(), 650, 550);
		setControl(mainComposite);
	}
	
	/**
	 * Create the controls to show the list of parameters that will be created at the end of the wizard
	 * 
	 * @param mainComposite parent control
	 */
	private void createNotEmptyContent(Composite mainComposite){
		//The parameters added for every section (main dataset, selected dataset and dataset runs) are at least one of them
		if (!missingParamOnMain.isEmpty()){
			new Label(mainComposite, SWT.NONE).setText(Messages.ConnectToDomainWizardPage_additionToMaindataset);
			Composite parameterComposite = new Composite(mainComposite, SWT.NONE);
			parameterComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout listLayout = new GridLayout(1, false);
			listLayout.marginLeft = 15;
			parameterComposite.setLayout(listLayout);
			for(String missingParam : missingParamOnMain){
				new Label(parameterComposite, SWT.NONE).setText(missingParam);
			}
		}

		if (!missingParamOnDataset.isEmpty()){
			new Label(mainComposite, SWT.NONE).setText(MessageFormat.format(Messages.ConnectToDomainWizardPage_additionToSelectedDataset, connectedDataset.getValue().getName()));
			Composite parameterComposite = new Composite(mainComposite, SWT.NONE);
			parameterComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout listLayout = new GridLayout(1, false);
			listLayout.marginLeft = 15;
			parameterComposite.setLayout(listLayout);
			for(String missingParam : missingParamOnDataset){
				new Label(parameterComposite, SWT.NONE).setText(missingParam);
			}
		}
		
		for(InfoContainer container : missingParamOnRun.values()){
			IDatasetContainer element = container.getContainer();
			String parentName = null;
			if (element instanceof APropertyNode){
				APropertyNode nodeElement = (APropertyNode)element;
				parentName = nodeElement.getDisplayText();
			}
			Label titleLabel = new Label(mainComposite, SWT.NONE);
			if (parentName != null) titleLabel.setText(MessageFormat.format(Messages.ConnectToDomainWizardPage_additionToDatasetRun, parentName)); 
			else titleLabel.setText(Messages.ConnectToDomainWizardPage_additionToDatasetRun2);
			Composite parameterComposite = new Composite(mainComposite, SWT.NONE);
			parameterComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			GridLayout listLayout = new GridLayout(1, false);
			listLayout.marginLeft = 15;
			parameterComposite.setLayout(listLayout);
			for(String missingParam : container.getMissingParameters()){
					new Label(parameterComposite, SWT.NONE).setText(missingParam);
			}
		}
	}
	
	/**
	 * Return the list (not null) of parameters that need to be added to the main dataset
	 * 
	 * @return a not null list of string, representing the missing parameters name
	 */
	private List<String> getMissingParameterOnMainDataset(){
		List<String> result = new ArrayList<String>();
		JasperDesign design = connectedDataset.getJasperDesign();
		for(String neededParameter : NEDDED_PARAMETERS){
			if (!design.getParametersMap().containsKey(neededParameter)) result.add(neededParameter);
		}
		return result;
	}
	
	/**
	 * Return the list (not null) of parameters that need to be added to the selected dataset
	 * 
	 * @return a not null list of string, representing the missing parameters name
	 */
	private List<String> getMissingParameterOnDataset(){
		List<String> result = new ArrayList<String>();
		Map<String, JRParameter> parametersMap = connectedDataset.getValue().getParametersMap();
		for(String neededParameter : NEDDED_PARAMETERS){
			if (!parametersMap.containsKey(neededParameter)) result.add(neededParameter);
		}
		return result;
	}
	
	/**
	 * Return the list (not null) of parameters that need to be added to the passed dataset run
	 * 
	 * @param the dataset run to check
	 * @return a not null list of string, representing the missing parameters name
	 */
	private List<String> getMissingParameterOnDatasetRun(MDatasetRun datasetRun){
		List<String> result = new ArrayList<String>();
		JRDatasetParameter[] parameters = datasetRun.getValue().getParameters();
		HashSet<String> definedParameters = new HashSet<String>();
		for(JRDatasetParameter parameter : parameters){
			definedParameters.add(parameter.getName());
		}
		for(String neededParameter : NEDDED_PARAMETERS){
			if (!definedParameters.contains(neededParameter)) result.add(neededParameter);
		}
		return result;
	}
	
	/**
	 * Return an hashmap that contains every dataset run for the selected dataset (reference of the dataset run uses as key) and 
	 * as value has an info container. The info container basically contains a list of the parameters needed
	 * by the dataset run and the element that contains the dataset run
	 */
	private HashMap<MDatasetRun, InfoContainer> getMissingDatasetsRun(){
		HashMap<MDatasetRun, InfoContainer> result = new HashMap<MDatasetRun,InfoContainer>();	
		for(IDatasetContainer container : runReferences){
			List<MDatasetRun> runList = container.getDatasetRunList();
			for(MDatasetRun run : runList){
				InfoContainer missingParamters = result.get(run);
				if (missingParamters == null){
					List<String> missingParametersName = getMissingParameterOnDatasetRun(run);
					if (!missingParametersName.isEmpty()) result.put(run, new InfoContainer(missingParametersName, container, run));
				}
			}
		}
		return result;
	}
	
	/**
	 * Add the missing parameters where they are needed
	 */
	public void doAction(){
		JasperDesign design = connectedDataset.getJasperDesign();
		for(String missingParam : missingParamOnMain){
			JRDesignParameter param = new JRDesignParameter();
			param.setName(missingParam);
			param.setValueClassName("java.lang.Object"); //$NON-NLS-1$
			try {
				design.addParameter(param);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		
		JRDesignDataset dataset = connectedDataset.getValue();
		for(String missingParam : missingParamOnDataset){
			JRDesignParameter param = new JRDesignParameter();
			param.setName(missingParam);
			param.setValueClassName("java.lang.Object"); //$NON-NLS-1$
			try {
				dataset.addParameter(param);
			} catch (JRException e) {
				e.printStackTrace();
			}
		}
		
		for(InfoContainer container : missingParamOnRun.values()){
			JRDesignDatasetRun run = container.getRun().getValue();
			for(String missingParam : container.getMissingParameters()){
				JRDesignDatasetParameter param = new JRDesignDatasetParameter();
				param.setName(missingParam);
				JRDesignExpression exp = new JRDesignExpression();
				exp.setText("$P{"+missingParam+"}"); //$NON-NLS-1$ //$NON-NLS-2$
				param.setExpression(exp);
				try {
					run.addParameter(param);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * True if there are parameters to add, false otherwise
	 */
	public boolean canFinish(){
		if (runReferences == null || missingParamOnMain == null || missingParamOnDataset == null) return false;
		if (missingParamOnDataset.isEmpty() && missingParamOnMain.isEmpty() && missingParamOnRun.isEmpty()) return false;
		return true;
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_CONNECT_TO_DOMAIN;
	}
}
