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
package com.jaspersoft.studio.data.designer.tree;

import java.util.List;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.designer.AQueryDesigner;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * A generic abstract query designer that allows to represent
 * data in a tree-form way.
 * <p>
 * 
 * Besides the common query text area, a tree viewer is shown in order
 * to visualize the data adapter information.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public abstract class TreeBasedQueryDesigner extends AQueryDesigner {
	
	/* Main control of the designer */
	protected Composite control;
	/* Text area where enter the query */
	protected StyledText queryTextArea;
	/* Tree viewer with the data representation */
	protected TreeViewer treeViewer;
	/* Flag that states if the designer is currently performing a refresh operation */
	protected boolean isRefreshing;
	
	public TreeBasedQueryDesigner(){
	}

	public Control getControl() {
		return control;
	}

	public Control createControl(Composite parent) {
		control=new Composite(parent, SWT.NONE);
		GridLayout controlGl=new GridLayout(1,true);
		controlGl.marginWidth=0;
		controlGl.marginHeight=0;
		control.setLayout(controlGl);
		
		createTitleBar(control);
		
		SashForm sashForm = new SashForm(control, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		createTreeViewer(sashForm);
		
		createQueryTextArea(sashForm);
		
		// Standard proportions
		sashForm.setWeights(new int[] {30, 70});
		return control;
	}
	
	/**
	 * Creates the text area for the query in its text-form representation
	 * 
	 * @param parent the parent composite for the query text area
	 */
	protected void createQueryTextArea(Composite parent) {
		queryTextArea = new StyledText(parent, SWT.BORDER);
		queryTextArea.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				queryTextAreaModified();
			}
		});
		queryTextArea.setFont(FontUtils.getEditorsFont(jConfig));
	}
	
	/**
	 * This method is invoked, via {@link ModifyListener}, when text area is modified
	 * <p>
	 * 
	 * Default implementation keep updated the text of the dataset {@link JRDesignQuery}.
	 * Sub-classes can override this method, but they should always invoke the superclass
	 * implementation.<br>
	 * 
	 */
	protected void queryTextAreaModified(){
		// keep the query info updated
		((JRDesignQuery) jDataset.getQuery()).setText(queryTextArea.getText());
		// invoke decoration if any
		decorateTreeUsingQueryText();
	}

	/**
	 * This method in invoked every time the query text area is modified.
	 * It should decorate the tree on the viewer based on the current query string.
	 * <p>
	 * 
	 * Clients should override this default (empty) implementation, to provide
	 * custom behavior like for example render in bold the tree nodes selected
	 * by the specified query. 
	 * 
	 * @see #queryTextAreaModified()
	 */
	protected void decorateTreeUsingQueryText(){
		// DO NOTHING
	}
	
	/**
	 * Creates a title bar for the main control.
	 * It should be a short description, or some ready-to-use
	 * instructions for the query designer user.
	 * 
	 * @param parent the parent composite for the title bar
	 */
	protected void createTitleBar(Composite parent) {
		// Standard implementation: empty
	}
	
	/**
	 * Creates the tree viewer for the current designer.
	 *  
	 * @param parent the parent composite for the tree viewer
	 */
	protected void createTreeViewer(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.BORDER);
		treeViewer.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		treeViewer.setContentProvider(getTreeContentProvider());
		treeViewer.setLabelProvider(getTreeLabelProvider());
	}

	/**
	 * @return the label provider for the tree viewer
	 */
	protected abstract IBaseLabelProvider getTreeLabelProvider();

	/**
	 * @return the content provider for the tree viewer
	 */
	protected abstract IContentProvider getTreeContentProvider() ;
	
	/**
	 * Refreshes the tree data using the dataAdapter information as input.
	 * 
	 * @param 
	 * 		dataAdapter the data adapter with information on the data to be visualized
	 */
	protected abstract void refreshTreeViewerContent(DataAdapterDescriptor dataAdapter);
	
	/**
	 * Creates a new JRField and adds it the fields table.
	 * 
	 * @param the node to be converted/created as {@link JRDesignField}
	 */
	public void createField(ANode node) {
		List<JRDesignField> currentFields = this.container.getCurrentFields();
		JRDesignField field = (JRDesignField)node.getAdapter(JRDesignField.class);
		field.setName(ModelUtils.getNameForField(currentFields, field.getName()));
		currentFields.add(field);
		this.container.setFields(currentFields);
	}

	@Override
	public void setQuery(JasperDesign jDesign, JRDataset jDataset, JasperReportsConfiguration jConfig) {
		super.setQuery(jDesign, jDataset, jConfig);
		queryTextArea.setText(jDataset.getQuery().getText());
	}
	
	public Control getToolbarControl() {
		return null;
	}

	public Control createToolbar(Composite parent) {
		return null;
	}

	public void dispose() {
	}

	public void setDataAdapter(DataAdapterDescriptor da) {
		refreshTreeViewerContent(da);
	}
	
}
