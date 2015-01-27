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
package org.eclipse.babel.editor.tree.actions;

import java.text.MessageFormat;

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.messages.Messages;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * @author Pascal Essiembre
 * 
 */
public class DeleteKeyAction extends AbstractTreeAction {

    /**
     * 
     */
    public DeleteKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
        super(editor, treeViewer);
        setText(Messages.key_delete);
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
        setToolTipText(Messages.key_delete); // TODO put tooltip
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.Action#run()
     */
    public void run() {
        KeyTreeNode node = getNodeSelection();
        String key = node.getMessageKey();
        String msgHead = null;
        String msgBody = null;
        if (getContentProvider().hasChildren(node)) {
            msgHead = Messages.dialog_delete_head_multiple;
            msgBody =  MessageFormat.format(Messages.dialog_delete_body_multiple, new Object[]{key});
        } else {
            msgHead = Messages.dialog_delete_head_single;
            msgBody =  MessageFormat.format(Messages.dialog_delete_body_single, key);
        }
        MessageBox msgBox = new MessageBox(getShell(), SWT.ICON_QUESTION
                | SWT.OK | SWT.CANCEL);
        msgBox.setMessage(msgBody);
        msgBox.setText(msgHead);
        if (msgBox.open() == SWT.OK) {
            MessagesBundleGroup messagesBundleGroup = getBundleGroup();
            KeyTreeNode[] nodesToDelete = getBranchNodes(node);
            for (int i = 0; i < nodesToDelete.length; i++) {
                KeyTreeNode nodeToDelete = nodesToDelete[i];
                messagesBundleGroup
                        .removeMessages(nodeToDelete.getMessageKey());
            }
        }
    }

}
