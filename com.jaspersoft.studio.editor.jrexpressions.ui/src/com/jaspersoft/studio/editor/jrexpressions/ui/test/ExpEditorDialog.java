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
package com.jaspersoft.studio.editor.jrexpressions.ui.test;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.xtext.grammaranalysis.impl.GrammarElementTitleSwitch;
import org.eclipse.xtext.nodemodel.BidiIterable;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.HiddenLeafNode;
import org.eclipse.xtext.nodemodel.impl.RootNode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.parser.ParseException;

import com.google.inject.Injector;
import com.jaspersoft.studio.editor.jrexpressions.ui.JRExpressionsActivator;

import de.itemis.xtext.utils.jface.viewers.StyledTextXtextAdapter;

public class ExpEditorDialog extends Dialog {

	private StyledTextXtextAdapter xtextAdapter;
	private TreeViewer debugtree;
	private StyledText editorArea;
	private CaretListener editorCaretListener;
	private SelectionListener editorSelectionListener;
	private SelectionListener treeSelectionListener;
	private ModifyListener editorModifyListener;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public ExpEditorDialog(Shell parentShell) {
		super(parentShell);
		setShellStyle(SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.TITLE | SWT.APPLICATION_MODAL);
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		container.setLayout(new GridLayout(1, false));

		// Editor "Area"
		createEditorArea(container);
        
        // Tree viewer 
        createDebugTreeViewer(container);
        
        // Creates widget listeners
        createListeners();
        
        // Add listeners
        addTextEditorListeners();
        addTreeListeners();
        
		return container;
	}
	
	private void addTreeListeners(){
		debugtree.getTree().addSelectionListener(treeSelectionListener);		
	}
	
	private void addTextEditorListeners(){
		editorArea.addSelectionListener(editorSelectionListener);
		editorArea.addCaretListener(editorCaretListener);
		editorArea.addModifyListener(editorModifyListener);
	}
	
	private void removeTreeListeners(){
		debugtree.getTree().removeSelectionListener(treeSelectionListener);
	}
	
	private void removeTextEditorListeners(){
		editorArea.removeSelectionListener(editorSelectionListener);
		editorArea.removeCaretListener(editorCaretListener);
		editorArea.removeModifyListener(editorModifyListener);
	}

	private void createListeners() {
		editorSelectionListener = new SelectionListener() {
			 
			public void widgetSelected(SelectionEvent e) {
				removeTreeListeners();
				TreeItem item=findTreeItemForNode(debugtree.getTree().getItems(),e.x,e.y);
				if(item!=null){
					debugtree.getTree().select(item);
				}
				else{
					debugtree.setSelection(null);
				}
				addTreeListeners();
			}
			 
			public void widgetDefaultSelected(SelectionEvent e) {
				// NOT CALLED FOR STYLED TEXT
			}
		};
		editorCaretListener = new CaretListener() {
			 
			public void caretMoved(CaretEvent event) {
				removeTreeListeners();
				TreeItem item=findTreeItemForNode(debugtree.getTree().getItems(),event.caretOffset,event.caretOffset);
				if(item!=null){
					debugtree.getTree().select(item);
				}
				else{
					debugtree.setSelection(null);
				}
				addTreeListeners();
			}
		};
        treeSelectionListener = new SelectionListener() {
			 
			public void widgetSelected(SelectionEvent e) {
				removeTextEditorListeners();
				
				TreeItem[] selection = ((Tree)e.getSource()).getSelection();
				if(selection!=null && selection.length==1){
					INode iNode = (INode)selection[0].getData();
					if(iNode!=null){
						int nodeOffset = iNode.getOffset();
						int nodeLength = iNode.getLength();
						editorArea.setSelection(nodeOffset, nodeOffset+nodeLength);
					}
				}
				
				addTextEditorListeners();
			}
			 
			public void widgetDefaultSelected(SelectionEvent e) {
			}
		};
		editorModifyListener = new ModifyListener() {
			 
			public void modifyText(ModifyEvent e) {
				try {
					IParseResult xtextParseResult = xtextAdapter.getXtextParseResult();
					if(xtextParseResult!=null){
						ICompositeNode rootNode=xtextParseResult.getRootNode();
						debugtree.setInput(rootNode);
						debugtree.expandAll();
					}
				} catch (ParseException ex) {
					// Unable to parse the text edited
					// ...
					// TODO - HANDLE SOMEHOW ?!
					debugtree.getTree().removeAll();
				}
			}
			
		};
	}

	private TreeItem findTreeItemForNode(TreeItem items[],int selectionStart,int selectionEnd){
		for(TreeItem item : items){
			TreeItem found=null;
			
			if (item.getData() instanceof INode){
				INode currNode=(INode)item.getData();
				int currOffset=currNode.getOffset();
				int currLength=currNode.getLength();
				int selectionLength=selectionEnd-selectionStart;
				
				// Handle the different cases:
				// 1) Perfect match 2) Beginning of the token
				if(currOffset==selectionStart &&
						(currLength==selectionLength || selectionLength==0)){
					found=item;	
				}

				// Always check children, we want to select a most precise and nested node!
				if(item.getItemCount()>0){
					TreeItem foundChildren=findTreeItemForNode(item.getItems(),selectionStart,selectionEnd);
					if(foundChildren!=null){
						// Replace with a more precise selection
						found=foundChildren;
					}
				}
								
			}
			
			if(found!=null)
				return found;
		}
		
		return null;
	}
	
	private void createEditorArea(Composite parent) {
		Composite editorContainer=new Composite(parent, SWT.NONE);
		GridData layoutData = new GridData(SWT.FILL, SWT.FILL, true, false);
		layoutData.heightHint=70;
		editorContainer.setLayoutData(layoutData);
		GridLayout layout = new GridLayout(1,false);
		layout.marginWidth=0;
		editorContainer.setLayout(layout);

		boolean test= 56 > 7;
		
//		int test=(int) 'c';
//		String testss=(String) "ddddd";
//		int testsss[][]=new int[][]{new int[]{5}};
//		
//		boolean d=testsss instanceof int[][];
		
		editorArea = new StyledText(editorContainer, SWT.BORDER | SWT.BORDER_SOLID | SWT.SINGLE);
		editorArea.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
        xtextAdapter = new StyledTextXtextAdapter(getInjector());
        xtextAdapter.adapt(editorArea);
	}
	
	private void createDebugTreeViewer(Composite container) {
        debugtree = new TreeViewer(container,SWT.SINGLE | SWT.BORDER | SWT.BORDER_SOLID);
        debugtree.getTree().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,2));
        debugtree.setContentProvider(new ITreeContentProvider() {
			 
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			}
			 
			public void dispose() {
			}
			 
			public boolean hasChildren(Object element) {
				if(element!=null && element instanceof ICompositeNode){
					return ((ICompositeNode)element).hasChildren();
				}
				return false;
			}
			 
			public Object getParent(Object element) {
				if(element instanceof RootNode){
					return null;
				}
				if(element !=null && element instanceof INode){
					return ((INode)element).getParent();
				}
				return null;
			}
			 
			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}
			 
			public Object[] getChildren(Object parentElement) {
				List<INode> childrenList=new ArrayList<INode>();
				if(parentElement !=null && 
						parentElement instanceof ICompositeNode){
					BidiIterable<INode> children = ((ICompositeNode) parentElement).getChildren();
					for (INode child : children){
						if(!(child instanceof HiddenLeafNode)){
							childrenList.add(child);
						}
					}
				}
				return childrenList.toArray(new INode[childrenList.size()]);
			}
		});
        
        debugtree.setLabelProvider(new LabelProvider(){

			@Override
			public String getText(Object element) {
				if(element!=null && element instanceof INode){
					EObject grammarElement = ((INode)element).getGrammarElement();
			        GrammarElementTitleSwitch testSwitch=new GrammarElementTitleSwitch();
			        String result = testSwitch.doSwitch(grammarElement);
					String nodetext = ((INode) element).getText();
					nodetext=nodetext.replace("\n", "\\n"); //$NON-NLS-1$ //$NON-NLS-2$
					if(result != null){
						return result + " | [" + nodetext + "]"; //$NON-NLS-1$ //$NON-NLS-2$
					}
					else{
						return "[" + nodetext + "]"; //$NON-NLS-1$ //$NON-NLS-2$
					}
				}
				return super.getText(element);
			}
        	
        });
		
	}

	private Injector getInjector(){
        JRExpressionsActivator activator = JRExpressionsActivator.getInstance();
        return activator.getInjector(JRExpressionsActivator.COM_JASPERSOFT_STUDIO_EDITOR_JREXPRESSIONS_JAVAJREXPRESSION);
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(600,800);
	}

}
