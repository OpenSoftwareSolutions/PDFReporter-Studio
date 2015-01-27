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
package com.jaspersoft.studio.data.wizard;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.data.adapter.ImportUtility;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Dialog page that list of the configurations of iReport found into the 
 * default path. It is also possible to select a custom path to a configuration
 * 
 * @author Orlandin Marco
 *
 */
public class ListInstallationPage extends JSSHelpWizardPage {

	/**
	 * The configuration actually selected
	 */
	private IReportDescriptor selectedElement = null;
	
	/**
	 * Last radio button selected
	 */
	private Button lastRadioSelected = null;
	
	/**
	 * RadioButton for the custom configuration path
	 */
	protected Button customRadio;
	
	/**
	 * Class that handle the selection of a radio button that represent
	 * a configuration of iReport into a default location
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class RadioSelection extends SelectionAdapter{
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			selectedElement = (IReportDescriptor)e.widget.getData();
			setPageComplete(true);
			updateRadio((Button)e.widget);
		}
		
	}
	
	/**
	 * Since not all the radio are in the same composite the default deselection 
	 * with another element is selected dosen't work. For this reason when a radio 
	 * is selected the last one is deselected and the last selected variable is 
	 * updated
	 * 
	 * @param actualySelected the radio button that is actually selected
	 */
	private void updateRadio(Button actualySelected){
		if (lastRadioSelected != null && lastRadioSelected != actualySelected){
			lastRadioSelected.setSelection(false);
		}
		lastRadioSelected = actualySelected;
	}
	
	public ListInstallationPage() {
		super("IReportInstallationsList"); //$NON-NLS-1$
		setTitle(Messages.ListInstallationPage_title);
		setDescription(Messages.ListInstallationPage_description);
		setPageComplete(false);
	}
	
	/**
	 * Return the list of the discovered configuration automatically
	 * 
	 * @return a not null list of the discovered configuration
	 */
	protected List<IReportDescriptor> getFoundedConfiguration(){
		return ImportUtility.getIReportConfigurationFolder();
	}

	@Override
	public void createControl(Composite parent) {
		List<IReportDescriptor> list = getFoundedConfiguration();
		
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(GridLayoutFactory.fillDefaults().create());
		
		Label titleLabel = new Label(mainComposite, SWT.NONE);
		titleLabel.setText(Messages.ListInstallationPage_label);
		GridData labelData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		labelData.heightHint = 25;
		titleLabel.setLayoutData(labelData);
		
		
		ScrolledComposite scrollComp = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		//scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		scrollComp.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).create());
		scrollComp.setLayout(new GridLayout(1,false));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		Composite content = new Composite(scrollComp, SWT.NONE);
		scrollComp.setContent(content);
		content.setLayout(new GridLayout(1,false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		RadioSelection selectionListener = new RadioSelection();
		for(IReportDescriptor desc : list){
			Button radio = new Button(content, SWT.RADIO);
			radio.setText(desc.getName() + "  ("+desc.getFile().getAbsolutePath()+")"); //$NON-NLS-1$ //$NON-NLS-2$
			radio.setData(desc);
			radio.addSelectionListener(selectionListener);
		}
		createCustomPathPanel(content);
		scrollComp.setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		if (list.size() == 0) {
			customRadio.setSelection(true);
			customRadio.setVisible(false);
			GridData excludeLayout = new GridData(SWT.FILL, SWT.LEFT, true, false);
			excludeLayout.exclude = true;
			customRadio.setLayoutData(excludeLayout);
			customRadio.getParent().layout();
		}
		
		setControl(mainComposite);
	}
	
	/**
	 * Return a file dialog used to open the configuration file
	 * 
	 */
	protected String getPath(){
		FileDialog fd = new FileDialog(UIUtils.getShell(), SWT.OPEN);
    fd.setText(Messages.ListInstallationPage_openDialog);
    String[] filterExt = { "ireport.properties" }; //$NON-NLS-1$
    fd.setFileName("ireport.properties");//$NON-NLS-1$
    fd.setFilterExtensions(filterExt);
    return fd.open();
	}
	
	/**
	 * Return a file descriptor for the provided path
	 * 
	 * @param path the path
	 * @return a descriptor for the configuration folder in the path
	 */
	protected IReportDescriptor getDescriptor(String path){
		return ImportUtility.GetDescriptor(path);
	}
	
	/**
	 * Create the controls to select a custom path
	 * 
	 * @param mainComposite the composite where to place the control
	 */
	private void createCustomPathPanel(Composite mainComposite){
		final Composite customLocComposite = new Composite(mainComposite, SWT.None);
		GridLayout customLocLayout = new GridLayout(3,false);
		customLocLayout.marginWidth = 0;
		customLocLayout.horizontalSpacing = 2;
		customLocComposite.setLayout(customLocLayout);;
		customLocComposite.setLayoutData(new GridData(SWT.FILL, SWT.LEFT, true, false));
		customRadio = new Button(customLocComposite, SWT.RADIO);
		final Label radioLabel = new Label(customLocComposite, SWT.NONE);
		radioLabel.setText(Messages.ListInstallationPage_customLocation);
		
		Button browse = new Button(customLocComposite, SWT.NONE);
		browse.setText(Messages.ListInstallationPage_browse);
		browse.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
		    String selected = getPath();
		    if (selected != null){
		    	IReportDescriptor desc = getDescriptor(selected);
		    	if (desc != null){
			    	customRadio.setData(desc);
			    	customRadio.setToolTipText(selected);
			    	if (selected.length()>60) selected = selected.substring(0,10)+"...."+selected.substring(selected.length()-60); //$NON-NLS-1$
			    	radioLabel.setText(selected);
			    	customLocComposite.layout();
			    	if (customRadio.getSelection()) setPageComplete(true);
		    	}
		    }
			}
		});
		
		customRadio.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (customRadio.getData() != null){
					selectedElement = (IReportDescriptor)e.widget.getData();
					setPageComplete(true);
				} else setPageComplete(false);
				updateRadio(customRadio);
			}
		});
	}
	
	/**
	 * Return the configuration of iReport actually selected
	 * 
	 * @return a not null configuration of iReport
	 */
	public IReportDescriptor getSelection(){
		return selectedElement;
	}

	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_IMPORT_SELECT_IREPORT;
	}

}
