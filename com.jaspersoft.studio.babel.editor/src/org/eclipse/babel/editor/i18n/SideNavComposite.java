/*******************************************************************************
 * Copyright (c) 2007 Pascal Essiembre.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Pascal Essiembre - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.i18n;

import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.tree.actions.CollapseAllAction;
import org.eclipse.babel.editor.tree.actions.ExpandAllAction;
import org.eclipse.babel.editor.tree.actions.FlatModelAction;
import org.eclipse.babel.editor.tree.actions.TreeModelAction;
import org.eclipse.babel.editor.tree.internal.KeyTreeContributor;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

/**
 * Tree for displaying and navigating through resource bundle keys.
 * 
 * @author Pascal Essiembre
 */
public class SideNavComposite extends Composite {

    /** Key Tree Viewer. */
    private TreeViewer treeViewer;

    private AbstractMessagesEditor editor;

    private SideNavTextBoxComposite textBoxComp;
    
    private KeyTreeContributor treeContributor; 
 
    private Text filterTextBox;
    
    /**
     * Constructor.
     * 
     * @param parent
     *            parent composite
     * @param keyTree
     *            key tree
     */
    public SideNavComposite(Composite parent, final AbstractMessagesEditor editor) {
        super(parent, SWT.BORDER);
        this.editor = editor;
        
        setLayout(new GridLayout(1, false));
        
        Composite toolBarComposite = new Composite(this, SWT.NONE);
        GridLayout toolbarLayout = new GridLayout(2, false);
        toolbarLayout.marginWidth = 0;
        toolBarComposite.setLayout(toolbarLayout);
        toolBarComposite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        
        //Create the filter box
		filterTextBox = new Text(toolBarComposite, SWT.BORDER);
		filterTextBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		filterTextBox.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				treeContributor.filterKeyItems(filterTextBox.getText());
				treeViewer.getControl().setRedraw(false);
				treeViewer.refresh();
				if (!filterTextBox.getText().isEmpty())
					treeViewer.expandAll();
				treeViewer.getControl().setRedraw(true);
			}
		});
        
        // Create a toolbar.
        ToolBarManager toolBarMgr = new ToolBarManager(SWT.FLAT);
        ToolBar toolBar = toolBarMgr.createControl(toolBarComposite);

        this.treeViewer = new TreeViewer(this, SWT.SINGLE | SWT.BORDER| SWT.V_SCROLL | SWT.H_SCROLL);

        GridData gid;

        gid = new GridData();
        gid.horizontalAlignment = GridData.END;
        gid.verticalAlignment = GridData.BEGINNING;
        toolBar.setLayoutData(gid);
        toolBarMgr.add(new TreeModelAction(editor, treeViewer));
        toolBarMgr.add(new FlatModelAction(editor, treeViewer));
        toolBarMgr.add(new Separator());
        toolBarMgr.add(new ExpandAllAction(editor, treeViewer));
        toolBarMgr.add(new CollapseAllAction(editor, treeViewer));
        toolBarMgr.update(true);

        // TODO have two toolbars, one left-align, and one right, with drop
        // down menu
        // initListener();

        createKeyTree();
        textBoxComp = new SideNavTextBoxComposite(this, editor);
    }


    /**
     * Gets the tree viewer.
     * 
     * @return tree viewer
     */
    public TreeViewer getTreeViewer() {
        return treeViewer;
    }

    /**
     * @see org.eclipse.swt.widgets.Widget#dispose()
     */
    public void dispose() {
    	treeContributor.dispose();
        super.dispose();
    }

    /**
     * Creates the middle (tree) section of this composite.
     */
    private void createKeyTree() {

        GridData gridData = new GridData();
        gridData.verticalAlignment = GridData.FILL;
        gridData.grabExcessVerticalSpace = true;
        gridData.horizontalAlignment = GridData.FILL;
        gridData.grabExcessHorizontalSpace = true;

        treeContributor = new KeyTreeContributor(editor);
        treeContributor.contribute(treeViewer);
        treeViewer.getTree().setLayoutData(gridData);

    }

    public SideNavTextBoxComposite getSidNavTextBoxComposite() {
        return textBoxComp;
    }
}
