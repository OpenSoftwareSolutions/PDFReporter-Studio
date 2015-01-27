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
package com.jaspersoft.translation.wizard;

import org.eclipse.babel.editor.wizards.internal.ResourceBundleNewWizardPage;
import org.eclipse.babel.messages.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.translation.action.NatureTranslationTester;

/**
 * Wizard step where the user could define one or more languages for the translation
 * and the destination project of the new resources file. In this wizard the input 
 * of the filename is disabled since the new files will have the same name of the imported 
 * ones plus the locale.
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationBundleNewWizardPage extends ResourceBundleNewWizardPage {

	/**
	 * Flag to auto add the default locale to the selected locales list when the selection
	 * is shown
	 */
	protected boolean autoAddFefaultLocale = false;
	
	/**
	 * Boolean flag that is used to know if the new file should be saved into 
	 * an appropriate folder structure (created if not present) or just inside the project
	 */
	private boolean createFolder = false;
	
	public TranslationBundleNewWizardPage(ISelection selection) {
		super(selection);
	}
	
	/**
	 * In this wizard the name of the properties files can not be changed
	 */
	@Override
	protected void createBundleNameArea(Composite container) {
	}
	
	/**
	 * Creates the top part of this wizard, which is the bundle location 
	 * and the checkbox to create the folder structure
	 * 
	 * @param parent parent container
	 */
	protected void createTopComposite(Composite parent) {
		super.createTopComposite(parent);
		
		Composite container = new Composite(parent, SWT.NULL);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 1;
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		container.setLayoutData(gd);
		
		// folder structure flag
		if (getWizard() instanceof TranslateBundleWizard) {
			final Button createFolderButton = new Button(container, SWT.CHECK);
			createFolderButton.setSelection(true);
			createFolderButton.setText(Messages.editor_wiz_createFolder);
			createFolder = true;
			createFolderButton.addSelectionListener(new SelectionAdapter(){
				@Override
				public void widgetSelected(SelectionEvent e) {
					createFolder = createFolderButton.getSelection();
				}
			});	
		}
	}
	
	@Override
	protected void createBottomComposite(Composite parent) {
		super.createBottomComposite(parent);
		bundleLocalesList.removeAll();
	}
	
	/**
	 * Remove the protected restriction from the super method
	 * 
	 * @return return the locale strings selected
	 */
	public String[] getLocaleStrings(){
		return super.getLocaleStrings();
	}
	
	/**
	 * Return a void filename since it is unused in this case, it not void or null
	 * to avoid the control on the file name
	 */
	public String getFileName() {
		return " ";
	}
	
	/**
	 * Boolean flag that is used to know if the new file should be saved into 
	 * an appropriate folder structure (created if not present) or just inside the project
	 * 
	 * @return true if the folder structure should be created, false otherwise
	 */
	public boolean needToCreateFolder(){
		return createFolder;
	}
	
	/**
	 * Check if the container project exist. If it exist it must be a translation project, otherwise the 
	 * user can not proceed with the wizard. If it dosen't exist the user can proceed and he is advised that
	 * a new project with the inserted name will be created
	 */
	protected void dialogChanged() {
		String container = getContainerName();
		String fileName = getFileName();
		if (!isCurrentPage()){
			//Don't allow to finish if this isn't the current page
			updateStatus(null, IMessageProvider.ERROR);
			return;
		}
		if (container.length() == 0) {
			updateStatus(Messages.editor_wiz_error_container, IMessageProvider.ERROR); //$NON-NLS-1$
			return;
		}
		if (fileName.length() == 0) {
			updateStatus(Messages.editor_wiz_error_bundleName, IMessageProvider.ERROR); //$NON-NLS-1$
			return;
		}
		int dotLoc = fileName.lastIndexOf('.');
		if (dotLoc != -1) {
			updateStatus(Messages.editor_wiz_error_extension, IMessageProvider.ERROR); //$NON-NLS-1$
			return;
		}
		if (getLocaleStrings().length==0) {
			updateStatus(Messages.editor_wiz_error_noLocale, IMessageProvider.ERROR);  //$NON-NLS-1$
			return;
		}
		IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject(container);
		if (!prj.exists()){
			updateStatus(Messages.editor_wiz_error_noPrject, IMessageProvider.INFORMATION); 
			return;
		} else if (!NatureTranslationTester.evaluateElementNature(prj)){
			updateStatus(Messages.editor_wiz_error_wrongType, IMessageProvider.ERROR); 
			return;
		}
		updateStatus(null, IMessageProvider.NONE);
	}
	
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible) dialogChanged();
	}
	
	@Override
	protected String getContextName() {
		return "com.jaspersoft.studio.babel.editor.defineStructureHelp";
	}
	
	/**
	 * Add the default locale to the selection list if it has a language different
	 * from every entry of an exclusion set, but only if the associated 
	 * flag is eneabled
	 * 
	 * @param exclusionLanguages language code exclude by the locale
	 */
	public void addDefaultLocale(String[] exclusionLanguages){
		if (autoAddFefaultLocale) addDefaultLocale(exclusionLanguages);
	}
	
	/**
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible && getWizard() instanceof TranslateBundleWizard) {
			TranslateBundleWizard hostWizard = (TranslateBundleWizard) getWizard();
			String fileName = hostWizard.getSelectedResource().getFileName();
			if (fileName.endsWith(".properties"))
				fileName = fileName.substring(0,
						fileName.indexOf(".properties"));
			fileText.setText(fileName);
		}
	}
	*/

}
