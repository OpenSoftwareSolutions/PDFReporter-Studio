/*******************************************************************************
 * Copyright (c) 2012 Alexej Strelzow.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Alexej Strelzow - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.refactoring;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.manager.RBManager;
import org.eclipse.babel.core.message.tree.internal.AbstractKeyTreeModel;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Handler for the key binding M1 (= Ctrl) + R. This handler triggers the
 * refactoring process.
 *
 * @author Alexej Strelzow
 */
public class RefactoringHandler extends AbstractHandler {

    /**
     * Gets called if triggered
     *
     * @param event
     *            The {@link ExecutionEvent}
     */
    public Object execute(ExecutionEvent event) throws ExecutionException {

        Event e = ((Event) event.getTrigger());
        Widget widget = e.widget;

        ISelectionService selectionService = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getSelectionService();
        ISelection selection = selectionService.getSelection();

        if (selection instanceof TextSelection && widget instanceof StyledText) { // Java-File
            TextSelection txtSel = (TextSelection) selection;
            IEditorPart activeEditor = PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getActivePage()
                    .getActiveEditor();
            FileEditorInput input = (FileEditorInput) activeEditor
                    .getEditorInput();
            IFile file = input.getFile();

            RBManager.getRefactorService().openRefactorDialog(file,
                    txtSel.getOffset());
        }

        if (widget != null && widget instanceof Tree) { // Messages-Editor or
                                                        // TapiJI-View
            Tree tree = (Tree) widget;
            TreeItem[] treeItems = tree.getSelection();
            if (treeItems.length == 1) {
                TreeItem item = treeItems[0];
                Object data = item.getData();
                String oldKey = item.getText();
                if (data != null && data instanceof KeyTreeNode) {
                	oldKey = ((KeyTreeNode)data).getMessageKey();
                }
                if (tree.getData() instanceof AbstractKeyTreeModel) {
                    AbstractKeyTreeModel model = (AbstractKeyTreeModel) tree
                            .getData();
                    MessagesBundleGroup messagesBundleGroup = model
                            .getMessagesBundleGroup();
                    String projectName = messagesBundleGroup.getProjectName();
                    String resourceBundleId = messagesBundleGroup
                            .getResourceBundleId();

                    RBManager.getRefactorService().openRefactorDialog(
                            projectName, resourceBundleId, oldKey, null);

                }
            }
        }

        return null;
    }

}
