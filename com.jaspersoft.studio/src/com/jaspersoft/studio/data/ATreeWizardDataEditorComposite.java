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
package com.jaspersoft.studio.data;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.data.designer.QueryStatus;
import com.jaspersoft.studio.data.ui.SimpleQueryWizardDataEditorComposite;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.fonts.utils.FontUtils;

/**
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public abstract class ATreeWizardDataEditorComposite extends SimpleQueryWizardDataEditorComposite {

	/* Text area where enter the query */
	protected StyledText queryTextArea;
	/* Tree viewer with the data representation */
	protected TreeViewer treeViewer;
	/* Flag that states if the designer is currently performing a refresh operation */
	protected boolean isRefreshing;
	/* The status bar to show info/error messages */
	private QueryStatus qStatus;
	
	public ATreeWizardDataEditorComposite(Composite parent, WizardPage page, DataAdapterDescriptor dataAdapterDescriptor) {
		super(parent, page, dataAdapterDescriptor);
	}

	@Override
	protected void createCompositeContent() {
		// Create the editor content
		GridLayout cmpGl=new GridLayout(1,true);
		cmpGl.marginWidth=0;
		cmpGl.marginHeight=0;
		setLayout(cmpGl);
		
		Label title = new Label(this,SWT.NONE);
		title.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		title.setText(getTitle());
		
		SashForm sashForm = new SashForm(this, SWT.NONE);
		sashForm.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));

		createTreeViewer(sashForm);
		createQueryTextArea(sashForm);
		
		qStatus=new QueryStatus(this);
		
		// Standard proportions
		sashForm.setWeights(new int[] {30, 70});
		
		refreshTreeViewerContent(getDataAdapterDescriptor());
	}
	
	/**
	 * Creates the text area for the query in its text-form representation
	 * 
	 * @param parent the parent composite for the query text area
	 */
	protected void createQueryTextArea(Composite parent) {
		queryTextArea = new StyledText(parent, SWT.BORDER);
		queryTextArea.setFont(FontUtils.getEditorsFont(getJasperReportsConfiguration()));
		queryTextArea.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				queryTextAreaModified();
			}
		});
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
	 * This method is invoked, via {@link ModifyListener}, when text area is modified
	 * <p>
	 * 
	 * Default implementation keep updated the text of the dataset {@link JRDesignQuery}.
	 * Sub-classes can override this method, but they should always invoke the superclass
	 * implementation.<br>
	 * 
	 */
	protected void queryTextAreaModified(){
		setQueryString(queryTextArea.getText());
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
	
	@Override
	public String getTitle(){
		return Messages.ATreeWizardDataEditorComposite_Title;
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
	 * @return the status bar for setting warning/error/info messages
	 */
	protected QueryStatus getStatusBar(){
		return qStatus;
	}
}
