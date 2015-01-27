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
package com.jaspersoft.studio.wizards.dataadapter;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * Wizard page to provide the information for the creation of a 
 * new data adapter plugin project
 * 
 * @author Orlandin Marco
 *
 */
public class DataAdapterInformationPage extends WizardPage {

	/**
	 * Text area for the project name
	 */
	private Text projectName;
	
	/**
	 * Text area for the data adapter name
	 */
	private Text dataAdapterName;
	
	/**
	 * Text area for the data adapter description
	 */
	private Text dataAdapterDescription;
	
	/**
	 * Text area for the main package
	 */
  private Text packageText;
  
  /**
   * Text area for the image path
   */
	private Text imagePath;
	
	/**
	 * Button to browse for an image on the disk
	 */
	private Button openImage;
	
	/**
	 * Aggregation of the information needed to create the plugin project
	 */
	private AdapterInfo resultInfo = null;
	
	/**
	 * Listener added on the field to update the dialog and the result info when
	 * some of them change
	 */
	private ModifyListener fieldModified = new ModifyListener() {
		
			@Override
			public void modifyText(ModifyEvent e) {
				doStatusUpdate();
			}
	};
	
  /**
   * contains the path of the folder in which the resource file will be
   * created
   */
	protected DataAdapterInformationPage() {
		super("dataAdapterInformationPage"); //$NON-NLS-1$
		setTitle(Messages.DataAdapterInformationPage_title);
		setDescription(Messages.DataAdapterInformationPage_description);
	}

	/**
	 * Check if the provided values are valid
	 */
	@Override
	public boolean isPageComplete() {
		if (dataAdapterName.getText().trim().isEmpty() ||	packageText.getText().trim().isEmpty()) {
			setErrorMessage(Messages.DataAdapterInformationPage_errorEmpty);
			return false;
		}
		if (packageText.getText().indexOf(" ") != -1) { //$NON-NLS-1$
			setErrorMessage(Messages.DataAdapterInformationPage_errorInvalidChar);
			return false;
		}
    Path pathContainer = new Path(projectName.getText());
    if (!pathContainer.isValidPath(projectName.getText())) {
    	setErrorMessage(Messages.DataAdapterInformationPage_errorInvalidGeneric);
    	return false;
    }
    if (pathContainer.segmentCount() < 1) {
    	setErrorMessage(Messages.DataAdapterInformationPage_errorEmpty2);
    	return false;
    }
    if (projectExists(pathContainer.segment(0))) {
    	setErrorMessage(Messages.DataAdapterInformationPage_errorProjectExist);
    	return false;
    }
    setErrorMessage(null);
    return true;
	}
	
  /**
   * Checks if there is a Project with the given name in the Package Explorer
   * 
   * @param projectName the name of the project
   * @return true if the project already exist in the workspace, false otherwise
   */
   protected boolean projectExists(String projectName) {
      IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
      Path containerNamePath = new Path("/" + projectName); //$NON-NLS-1$
      IResource resource = root.findMember(containerNamePath);
      if (resource == null) {
          return false;
      }
      return resource.exists();
  }

  /**
   * Update the page complete status and build the AdapterInfo result object
   * with the information provided by the user. If the information are not 
   * valid this object is set to null
   */
 	protected void doStatusUpdate(){
		boolean isPageComplete = isPageComplete();
		if (isPageComplete){
			resultInfo = new AdapterInfo(projectName.getText(), dataAdapterName.getText(), dataAdapterDescription.getText(), packageText.getText(), imagePath.getText());
		} else {
			resultInfo = null;
		}
		setPageComplete(isPageComplete);
	}
 	
 	/**
 	 * Return the current AdapterInfo containing the informations
 	 * provided by the user to create the plugin project
 	 * 
 	 * @return the data adapter info with the correct informations, 
 	 * or null if some information is not valid
 	 */
	public AdapterInfo getAdapterInfo(){
		return resultInfo;
	}
   
	@Override
	public void createControl(Composite parent) {
		initializeDialogUnits(parent);
		Composite composite= new Composite(parent, SWT.NONE);
		composite.setFont(parent.getFont());
		int cols = 4;
		GridLayout layout= new GridLayout();
		layout.numColumns= cols;
		composite.setLayout(layout);
		createProjectControls(composite, cols);
		createPackageControls(composite, cols);
		createAdapterNameControls(composite,cols);
		createAdapterDescriptionControls(composite,cols);
		createImageControls(composite, cols);
		setControl(composite);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(parent,ContextHelpIDs.WIZARD_NEW_FUNCTIONS_LIBRARY);
	}

	// UI related methods
	
	/**
	 * Create the controls for the data adapter name
	 * 
	 * @param parent the parent, must have a grid layout
	 * @param cols the number of columns that the text area should take
	 */
	private void createAdapterNameControls(Composite parent, int cols) {
		Label adapterNameLbl = new Label(parent, SWT.NONE);
		adapterNameLbl.setText(Messages.DataAdapterInformationPage_adapterNameLabel);
		adapterNameLbl.setToolTipText(Messages.DataAdapterInformationPage_adapterNameTooltip);
		adapterNameLbl.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		dataAdapterName = new Text(parent, SWT.BORDER);
		dataAdapterName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		dataAdapterName.addModifyListener(fieldModified);
	}
	
	/**
	 * Create the controls for the data adapter description
	 * 
	 * @param parent the parent, must have a grid layout
	 * @param cols the number of columns that the text area should take
	 */
	private void createAdapterDescriptionControls(Composite parent, int cols) {
		Label adapterDescriptionLbl = new Label(parent, SWT.NONE);
		adapterDescriptionLbl.setText(Messages.DataAdapterInformationPage_adapterDescriptionLabel);
		adapterDescriptionLbl.setToolTipText(Messages.DataAdapterInformationPage_adapterDescriptionTooltip);
		adapterDescriptionLbl.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		dataAdapterDescription = new Text(parent, SWT.BORDER);
		dataAdapterDescription.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,cols-1,1));
		dataAdapterDescription.addModifyListener(fieldModified);
	}
	
	/**
	 * Create the controls for the project name
	 * 
	 * @param parent the parent, must have a grid layout
	 * @param cols the number of columns that the text area should take
	 */
	protected void createProjectControls(Composite parent, int nColumns) {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		label.setText(Messages.DataAdapterInformationPage_pliugnNameLabel);
		label.setToolTipText(Messages.DataAdapterInformationPage_pluginNameTooltip);
		projectName = new Text(parent, SWT.BORDER);
		projectName.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,nColumns-1,1));
		projectName.addModifyListener(fieldModified);
	}

	/**
	 * Create the controls for the package name
	 * 
	 * @param parent the parent, must have a grid layout
	 * @param cols the number of columns that the text area should take
	 */
	protected void createPackageControls(Composite parent, int nColumns) {
		Label label= new Label(parent, SWT.NONE);
		label.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		label.setText(Messages.DataAdapterInformationPage_packageLabel);
		label.setToolTipText(Messages.DataAdapterInformationPage_packageTooltip);
		packageText = new Text(parent, SWT.BORDER);
		packageText.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,nColumns-1,1));
		packageText.addModifyListener(fieldModified);
	}
	
	/**
	 * Create the controls for the data adapter image
	 * 
	 * @param parent the parent, must have a grid layout
	 * @param cols the number of columns that the text area should take
	 */
	protected void createImageControls(Composite parent, int cols) {
		Label adapterNameLbl = new Label(parent, SWT.NONE);
		adapterNameLbl.setText(Messages.DataAdapterInformationPage_imageLabel);
		adapterNameLbl.setToolTipText(Messages.DataAdapterInformationPage_imageTooltip);
		adapterNameLbl.setLayoutData(new GridData(SWT.LEFT,SWT.TOP,false,false));
		imagePath = new Text(parent, SWT.BORDER);
		imagePath.setLayoutData(new GridData(SWT.FILL,SWT.TOP,true,false,cols-2,1));
		imagePath.addModifyListener(fieldModified);
		
		openImage = new Button(parent, SWT.NONE);
		openImage.setText(Messages.common_browse);
		openImage.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		openImage.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				 FileDialog fd = new FileDialog(getShell(), SWT.OPEN);
	       fd.setText(Messages.common_open);
	       String[] filterExt = { "*.jpg", "*.png", ".gif", "*.*" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	       fd.setFilterExtensions(filterExt);
	       String selected = fd.open();
	       if (selected != null){
	      	 imagePath.setText(selected);
	       }
			}
		});
	}
}
