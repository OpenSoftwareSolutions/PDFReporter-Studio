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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.babel.messages.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

import com.jaspersoft.translation.resources.AbstractResourceDefinition;
import com.jaspersoft.translation.resources.IResourcesInput;

/**
 * Wizard page where all the contributed resources available for a translation are 
 * shown. the user can select one or more of them to create the right folder structure\
 * project to easily translate them
 * 
 * @author Orlandin Marco
 *
 */
public class GetinputNewWizardPage extends HelpWizardPage {

	/**
	 * A treeview where all the resource available are shown, they are divided
	 * by the plugin name where are placed
	 */
	private TreeViewer resourceList;

	/**
	 * A list of resource selected by the user from the tree
	 */
	private List<AbstractResourceDefinition> selectedElement = new ArrayList<AbstractResourceDefinition>();

	/**
	 * Label where the package of a clicked resource is shown
	 */
	private Label packageLbl;

	/**
	 * Label where the plugin name of a clicked resource is shown
	 */
	private Label pluginLbl;

	/**
	 * Label where the filename of a clicked resource is shown
	 */
	private Label fileNameLbl;

	/**
	 * Label where the description of a clicked resource is shown
	 */
	private Label descriptionLbl;
	
	/**
	 * Main control of the page
	 */
	private Composite parentControl;
	
	/**
	 * List of the contributed resources, they are cached after the first read
	 */
	private static List<IResourcesInput> loadedElementsList = null;
	
	private final static String HELP_ID = "com.jaspersoft.studio.babel.editor.newResourceHelp";
	
	/**
	 * View content provider for the tree
	 * 
	 * @author Orlandin Marco
	 *
	 */
	protected class ViewContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer v, Object oldInput, Object newInput) {
		}

		@Override
		public void dispose() {
		}

		@Override
		public Object[] getElements(Object inputElement) {
			return ((Collection<?>) inputElement).toArray();
		}

		@Override
		public Object[] getChildren(Object parentElement) {
			if (parentElement instanceof Collection)
				return ((Collection<?>) parentElement).toArray();
			else
				return ((IResourcesInput) parentElement).getResourcesElements().toArray();
		}

		@Override
		public Object getParent(Object element) {
			if (element instanceof AbstractResourceDefinition) return ((AbstractResourceDefinition) element).getPluginName();
			return null;
		}

		@Override
		public boolean hasChildren(Object element) {
			return (element instanceof IResourcesInput || element instanceof Collection);
		}

	}

	/**
	 *Label provider for the tree
	 * 
	 * @author Orlandin Marco
	 *
	 */
	protected class ViewLabelProvider extends LabelProvider {
		@Override
		public String getText(Object element) {
			if (element instanceof String) return element.toString();
			else if (element instanceof AbstractResourceDefinition) {
				AbstractResourceDefinition resource = (AbstractResourceDefinition) element;
				String packageName = resource.getPackageName() != null ? resource.getPackageName() + ":" : ""; 
				return packageName + resource.getFileName();
			}
			else if (element instanceof IResourcesInput) return ((IResourcesInput)element).getPluginName();
			else return null;
		}

		public Image getImage(Object obj) {
			return null;
		}
	}

	protected GetinputNewWizardPage(String pageName) {
		super(pageName);
		setTitle(Messages.resource_wiz_title);
		setDescription(Messages.resource_wiz_description);
	}

	/**
	 * Read the provided resources from the extension point and initialize the cache
	 */
	private void getResourceInput() {
		IConfigurationElement[] elements = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio.babel.editor.resourceInput");
	    loadedElementsList = new ArrayList<IResourcesInput>();
		for (IConfigurationElement element : elements) {
			Object input;
			try {
				input = element.createExecutableExtension("class");
				if (input instanceof IResourcesInput) {
					IResourcesInput parent = (IResourcesInput) input;
					loadedElementsList.add(parent);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		Collections.sort(loadedElementsList);
	}
	
	@Override
	public void createControl(Composite parent) {
		if (loadedElementsList == null) getResourceInput();
		parentControl = parent;
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		container.setLayout(new GridLayout(2, false));

		resourceList = new TreeViewer(container);
		resourceList.setContentProvider(new ViewContentProvider());
		resourceList.setLabelProvider(new ViewLabelProvider());
		resourceList.setInput(loadedElementsList);
		GridData treeData = new GridData(GridData.FILL_BOTH);
		treeData.widthHint = 350;
		resourceList.getControl().setLayoutData(treeData);

		Group infoGroup = new Group(container, SWT.NONE);
		infoGroup.setText(Messages.resource_wiz_information_title);
		infoGroup.setLayout(new GridLayout(2, false));
		GridData infoData = new GridData(GridData.FILL_BOTH);
		infoData.widthHint = 300;
		infoGroup.setLayoutData(infoData);

		pluginLbl = createInfoElement(infoGroup, Messages.resource_wiz_information_plugin, "");
		packageLbl = createInfoElement(infoGroup, Messages.resource_wiz_information_package, "");
		fileNameLbl = createInfoElement(infoGroup, Messages.resource_wiz_information_name, "");
		descriptionLbl = createInfoElement(infoGroup, Messages.resource_wiz_information_description, "");

		Tree tree = (Tree) resourceList.getControl();
		tree.addSelectionListener(new SelectionAdapter() {
		  @Override
		  public void widgetSelected(SelectionEvent e) {
				updateDescription((TreeItem)e.item);
		    }
		}); 
		
		setControl(container);
	}
	
	/**
	 * Return an array of the TreeItem selected in the tree 
	 * 
	 * @return a not null array of the selected element
	 */
	private TreeItem[] getSelectedElements(){
		Tree tree = (Tree) resourceList.getControl();
		return tree.getSelection();
	}

	/**
	 * Update the informations area with the informations of the 
	 * last clicked element, if it is a resource descriptor
	 * 
	 * @param item the last clicked element
	 */
	private void updateDescription(TreeItem item) {
		if (item != null){
			if (item.getData() instanceof AbstractResourceDefinition){
				AbstractResourceDefinition resource = (AbstractResourceDefinition)item.getData();
				pluginLbl.setText(resource.getPluginName());
				fileNameLbl.setText(resource.getFileName());
				descriptionLbl.setText(resource.getDescription());
				if (resource.getPackageName() != null) packageLbl.setText(resource.getPackageName());
				else packageLbl.setText(Messages.resource_wiz_information_noPackage);
				parentControl.layout(true,true);
				if (item.getParent() != null) getHelpData(item.getParentItem().getData());
			} else getHelpData(item.getData());
		}
		setPageComplete(canFlipToNextPage());
	}
	
	private void getHelpData(Object item){
		if (getSelectedElements().length == 0){
			setContextName(HELP_ID);
		} else if (item instanceof IResourcesInput){
			String heldId = ((IResourcesInput)item).getContextId();
			if (heldId != null) setContextName(heldId);
			else setContextName(HELP_ID);
		}
	}

	/**
	 * Create a pair of labels, one as title and the other for the value, then return
	 * the second one 
	 * 
	 * @param parent container of the labels
	 * @param label text of the title label
	 * @param value text of the value label
	 * @return the value label
	 */
	private Label createInfoElement(Composite parent, String label, String value) {
		Label desc = new Label(parent, SWT.NONE);
		desc.setText(label);
		GridData titleData = new GridData();
		titleData.verticalAlignment = SWT.TOP;
		desc.setLayoutData(titleData);

		Label valLabel = new Label(parent, SWT.WRAP);
		valLabel.setText(value);
		valLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		return valLabel;
	}
	
	/**
	 * build a list of the selected tree item avoiding duplicated element if both the parent
	 * and one of its children is selected. this because it assumed that if the parent is 
	 * selected also all the children are considered inside the selection
	 * 
	 * @param store Node already inside the selection
	 * @param actualElement node actually considered
	 */
	private void buildSelectedItemSet(HashMap<String, AbstractResourceDefinition> store, TreeItem actualElement){
		if (actualElement.getData() instanceof IResourcesInput){
			IResourcesInput resourcesContainer = (IResourcesInput)actualElement.getData();
			for(AbstractResourceDefinition resource : resourcesContainer.getResourcesElements()){
				String resourceId = resource.getUniqueId();
				if (!store.containsKey(resourceId)) store.put(resourceId, resource);
			}
		} else if (actualElement.getData() instanceof AbstractResourceDefinition){
			AbstractResourceDefinition resource = (AbstractResourceDefinition)actualElement.getData();
			String resourceId = resource.getUniqueId();
			if (!store.containsKey(resourceId)) store.put(resourceId, resource);
		}
	}

	/**
	 * When the next page is requested the set of selected element is builded
	 */
	@Override
	public IWizardPage getNextPage() {
		HashMap<String, AbstractResourceDefinition> selectedResources = new HashMap<String, AbstractResourceDefinition>();
		for(TreeItem item : getSelectedElements()){
			buildSelectedItemSet(selectedResources,item);
		}
		selectedElement.clear();
		selectedElement = new ArrayList<AbstractResourceDefinition>(selectedResources.values());
		return super.getNextPage();
	}

	/**
	 * Return the selected resources 
	 * 
	 * @return a not null array of all the selected resources at the end of this wizard step
	 */
	public List<AbstractResourceDefinition> getSelectedResource() {
		return selectedElement;
	}

	/**
	 * Can go to the next page if at least a resource is selected
	 */
	@Override
	public boolean canFlipToNextPage() {
		return (getSelectedElements().length>0);
	}
	

	@Override
	protected String getContextName() {
		return HELP_ID;
	}

}
