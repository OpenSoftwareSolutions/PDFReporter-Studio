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
package com.jaspersoft.studio.style.view;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.IContributedContentsView;
import org.eclipse.ui.part.ViewPart;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.storage.PreferencesTemplateStylesStorage;
import com.jaspersoft.studio.editor.style.TemplateStyle;

/**
 * View to show the template styles of specific types. It can be contributed, and allow
 * drag and drop operations.
 * 
 * @author Orlandin Marco
 *
 */
public class TemplateStyleView extends ViewPart implements IContributedContentsView {
	
	/**
	 * Build the class that handle the storage of the TemplateStyles saved in the properties
	 */
	private static PreferencesTemplateStylesStorage savedStylesStorage = new PreferencesTemplateStylesStorage();

	/**
	 * Main tab container, for every contributor it will be created a tab inside this container
	 */
	private CTabFolder folder;
	
	/**
	 * The list of the contributed providers
	 */
	private List<TemplateViewProvider> viewProviders;
	
	/**
	 * Return the Style storage
	 * 
	 * @return and instance of the style storage, can't be null
	 */
	public static PreferencesTemplateStylesStorage getTemplateStylesStorage(){
		return savedStylesStorage;
	}
		
	@Override
	public IWorkbenchPart getContributingPart() {
		  return getSite().getPage().getActiveEditor();
	}

	@Override
	public void createPartControl(Composite parent) {
		folder = new CTabFolder(parent, SWT.BORDER);
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		folder.setSimple(false);
		folder.setMinimizeVisible(false);
		folder.setMaximizeVisible(false);
		folder.setBorderVisible(true);
		
		
		
		viewProviders = JaspersoftStudioPlugin.getExtensionManager().getStylesViewProvider();
		Collection<TemplateStyle> savedStyles = savedStylesStorage.getStylesDescriptors();
		for(TemplateViewProvider viewProvider : viewProviders){
			createTab(viewProvider);
			viewProvider.fillStyles(savedStyles);
		}
		
		savedStylesStorage.addPropertyChangeListener(PreferencesTemplateStylesStorage.PROPERTY_CHANGE_NAME, new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				for(TemplateViewProvider viewProvider : viewProviders){
					viewProvider.notifyChange(arg0);
				}
			}
		});
		
		if (folder.getSelectionIndex() == -1) folder.setSelection(0);
	}
	
	/**
	 * Create a tab for a contributor 
	 * 
	 * @param provider a contributor
	 */
	private void createTab(TemplateViewProvider provider){
		CTabItem tableItem = new CTabItem(folder, SWT.NONE);
		tableItem.setText(provider.getTabName());
		tableItem.setImage(provider.getTabImage());
		Composite tableComposite = new Composite(folder, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1,false));
		provider.createControls(tableComposite);
		tableItem.setControl(tableComposite);
	}
	

	@Override
	public void setFocus() {
		folder.setFocus();
	}
}
