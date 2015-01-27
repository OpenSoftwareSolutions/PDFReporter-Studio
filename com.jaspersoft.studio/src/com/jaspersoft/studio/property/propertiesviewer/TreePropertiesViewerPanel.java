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
package com.jaspersoft.studio.property.propertiesviewer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.dialogs.FilteredTree;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * This panel uses a {@link SashForm} to recreate the dialog UI of the Eclipse Preferences.
 * <p>
 * The main container is split into two separate panels. 
 * The left one will contain a custom {@link FilteredTree} widget for properties set navigation. 
 * The right side will have a title area and a content area contextual to the currently selected tree node.
 * <p>
 * This class is meant to be used in scenarios where there are many properties for a component that
 * can not be easily organized in a simple flat dialog or in a property sheet.
 * For example this custom panel can be easily added to a dedicated dialog to perform
 * complex property editing operations.
 * 
 * @author mrabbi
 *
 */
public class TreePropertiesViewerPanel<T extends IPropertiesViewerNode> extends Composite {

	protected FormToolkit toolkit;
	private List<T> nodes;
	private List<PropertiesNodeChangeListener> nodeChangedListeners;
	protected IPropertiesViewerNode currentSelectedNode;
	
	// Widgets
	protected Composite titleArea;
	protected Composite contentArea;
	private StackLayout contentAreaLayout;
	protected FilteredTree filteredTree;
	protected CLabel titleLabel;
	
	/**
	 * Creates the panel.
	 * 
	 * @param parent the parent container for this custom sashform
	 * @param style the initial style
	 * @param nodes the input list of nodes that will populate the tree
	 */
	public TreePropertiesViewerPanel(Composite parent, int style, List<T> nodes) {
		super(parent, style);
		toolkit = new FormToolkit(parent.getDisplay());
		this.nodes=nodes;
		this.nodeChangedListeners=new ArrayList<PropertiesNodeChangeListener>(1); // usually one listener is enough
		createPanelContent();
	}

	/*
	 * Creates the panel content.
	 */
	private void createPanelContent(){
		GridLayout gl_panel = new GridLayout(1,false);
		gl_panel.marginWidth=0;
		gl_panel.marginHeight=0;
		setLayout(gl_panel);
		
		final SashForm sash=new SashForm(this,SWT.HORIZONTAL);
		GridData gdsash=new GridData(SWT.FILL,SWT.FILL,true,true);
		sash.setLayoutData(gdsash);
		sash.SASH_WIDTH=1;
		
		Composite treeContainer=toolkit.createComposite(sash);
		treeContainer.setLayout(new GridLayout());
		filteredTree=createTreeViewer(treeContainer);
		filteredTree.getViewer().setInput(new Object());
		addListeners(filteredTree.getViewer());
		
		// Creates the panel that will contain the title area, a separator and
		// the content area with currently selected page stuff.
		Composite panelContainer=new Composite(sash, SWT.NONE);
		GridLayout gl_panelContainer = new GridLayout(2,false);
		gl_panelContainer.marginWidth=0;
		gl_panelContainer.marginHeight=0;
		gl_panelContainer.horizontalSpacing=0;
		panelContainer.setLayout(gl_panelContainer);
		panelContainer.setBackground(getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
		
		Label separator=toolkit.createSeparator(panelContainer, SWT.VERTICAL);
		GridData gd_Separator = new GridData(SWT.LEFT,SWT.FILL,false,true,1,3);
		separator.setLayoutData(gd_Separator);
		
		titleArea=createTitleArea(panelContainer);
		
		Label separator2=new Label(panelContainer,SWT.SEPARATOR|SWT.HORIZONTAL);
		GridData gd_Separator2 = new GridData(SWT.FILL,SWT.TOP,true,false,1,1);
		separator2.setLayoutData(gd_Separator2);		

		contentArea=createContentArea(panelContainer);
		contentAreaLayout = new StackLayout();
		contentArea.setLayout(contentAreaLayout);
		
		// Default weights ("percentages" of the total width)
		sash.setWeights(new int[] {25, 75});
	}
	
	/**
	 * Adds the listeners to the viewer control. 
	 * 
	 * @param viewer the tree viewer control
	 */
	protected void addListeners(final TreeViewer viewer) {
		viewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				final Object selection = getSingleSelection(event.getSelection());
				if (selection instanceof IPropertiesViewerNode) {
					showNodeContent((IPropertiesViewerNode)selection);
					notifyPropertiesNodeChanged((IPropertiesViewerNode)selection);
				}
			}
		});
		
		((Tree) viewer.getControl()).addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(final SelectionEvent event) {
				ISelection selection = viewer.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				IPropertiesViewerNode singleSelection = getSingleSelection(selection);
				boolean expanded = viewer.getExpandedState(singleSelection);
				viewer.setExpandedState(singleSelection, !expanded);
			}
		});
	}

	/**
	 * Creates the title area of the right side of the dialog.
	 * 
	 * @param parent the title area container
	 * @return the composite representing the title area
	 */
	protected Composite createTitleArea(Composite parent) {
		Composite titleArea = new Composite(parent, SWT.NONE);
		GridData gd_TitleArea=new GridData(SWT.FILL,SWT.FILL,true,false,1,1);
		titleArea.setLayoutData(gd_TitleArea);
		FillLayout fl_TitleArea = new FillLayout(SWT.VERTICAL);
		fl_TitleArea.marginHeight=5;
		fl_TitleArea.marginWidth=5;
		titleArea.setLayout(fl_TitleArea);
    titleLabel = new CLabel(titleArea, SWT.NONE);
    titleLabel.setFont(JFaceResources.getBannerFont());
		return titleArea;
	}
	
	/**
	 * Creates the content area of the right side of the dialog.
	 * It will be placed on bottom of the title (plus horizontal separator).
	 * 
	 * @param parent the content area container
	 * @return the composite representing the content area
	 */
	protected Composite createContentArea(Composite parent){
	  ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		GridData gd_ContentArea=new GridData(SWT.FILL,SWT.FILL,true,true,1,1);
		sc.setLayoutData(gd_ContentArea);
		Composite cmpContentArea = new Composite(sc, SWT.NONE);
		sc.setContent(cmpContentArea);
		sc.setMinSize(400,400);
		sc.setExpandHorizontal(true);
	  sc.setExpandVertical(true);
		return cmpContentArea;
	}
	
	/**
	 * Creates the filtered tree that will be shown on the left side of dialog.
	 * It uses a label provider which gets the IPropertiesViewerNode.getName()
	 * 
	 * @param parent the widget container
	 * @return the filtered tree widget
	 */
	protected FilteredTree createTreeViewer(Composite parent) {
		FilteredTree filteredTree = new FilteredTree(parent, SWT.SINGLE, new PropertiesPatternFilter(), true);
		filteredTree.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_LIST_BACKGROUND));	
		final TreeViewer viewer = filteredTree.getViewer();
		viewer.setLabelProvider( new LabelProvider() {

			public String getText(Object element) {
		        return ((IPropertiesViewerNode) element).getName();
		    }

		});
		
		viewer.setContentProvider(new PropertiesViewerContentProvider<T>(nodes));		
		return filteredTree;
	}
	
	/*
	 * This method is invoked when a selection event occurs on a tree node.
	 * The main area is updated showing the control related to the selected element.
	 */
	private boolean showNodeContent(IPropertiesViewerNode node) {
		if (node == null) {
			return false;
		}
		
		if(node.getControl()==null){
			node.createControl(contentArea);
		}
		
		if (node.getControl() == null) {
			return false;
		}
		currentSelectedNode=node;

		// Ensure all others controls are not visible
		Control[] children = contentArea.getChildren();
		Control currentControl = node.getControl();
		for (int i = 0; i < children.length; i++) {
			if (children[i] != currentControl) {
				children[i].setEnabled(false);
				children[i].setVisible(false);
			}
		}
		// Make the new page visible
		currentControl.setEnabled(true);
		currentControl.setVisible(true);
		contentAreaLayout.topControl=currentControl;
		contentArea.layout();
		
		// update other stuff
		updateTitleArea();
		
		return true;
	}
	
	/**
	 * This methods is invoked after a node content is shown in the main area.
	 * It should updates all other dialog areas, like for example the title area 
	 * and the additional information panel if present.
	 */
	protected void updateTitleArea(){
		// Update the title label
		titleLabel.setText(currentSelectedNode.getName());
		titleArea.layout();
	}
	
	/*
	 * Returns the single element currently selected in the tree viewer.
	 */
	private IPropertiesViewerNode getSingleSelection(ISelection selection) {
		if (!selection.isEmpty()) {
			IStructuredSelection structured = (IStructuredSelection) selection;
			if (structured.getFirstElement() instanceof IPropertiesViewerNode) {
				return (IPropertiesViewerNode) structured.getFirstElement();
			}
		}
		return null;
	}
	
	protected void checkSubclass () {
		/* Do nothing - Subclassing is allowed */
	}

	@Override
	public void dispose() {
		super.dispose();
		this.toolkit.dispose();
	}
	
	/**
	 * @return the currently selected node
	 */
	public IPropertiesViewerNode getCurrentSelectedNode(){
		return this.currentSelectedNode;
	}
	
	/**
	 * Forces the node selection and the showing of the related content.
	 * 
	 * @param node the node to be selected
	 */
	public void selectPropertiesNode(IPropertiesViewerNode node){
		filteredTree.getViewer().setSelection(new StructuredSelection(node));
	}

	/**
	 * Adds a new {@link PropertiesNodeChangeListener} to the widget current ones.
	 * 
	 * @param l the new listener
	 */
	public void addPropertiesNodeChangedListener(PropertiesNodeChangeListener l){
		nodeChangedListeners.add(l);
	}
	
	/**
	 * Removes a {@link PropertiesNodeChangeListener} from current ones of the widget.
	 * 
	 * @param l the listener to remove
	 */
	public void removePropertiesNodeChangedListener(PropertiesNodeChangeListener l){
		nodeChangedListeners.remove(l);
	}
	
	/*
	 * Notifies to the listeners that a node change event has occurred.
	 */
	private void notifyPropertiesNodeChanged(IPropertiesViewerNode node){
		for(PropertiesNodeChangeListener l : nodeChangedListeners){
			l.nodeChanged(node);
		}
	}

}
