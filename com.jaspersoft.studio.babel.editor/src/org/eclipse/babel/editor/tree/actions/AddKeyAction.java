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

import org.eclipse.babel.core.message.internal.MessagesBundleGroup;
import org.eclipse.babel.core.message.tree.internal.KeyTreeNode;
import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.messages.Messages;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;

/**
 * @author Pascal Essiembre
 * 
 */
public class AddKeyAction extends AbstractTreeAction {

    /**
     * 
     */
    public AddKeyAction(AbstractMessagesEditor editor, TreeViewer treeViewer) {
        super(editor, treeViewer);
        setText(Messages.key_add + " ..."); 
        setImageDescriptor(UIUtils.getImageDescriptor(UIUtils.IMAGE_ADD));
        setToolTipText(Messages.key_add + " ..."); 
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public void run() {
        KeyTreeNode node = getNodeSelection();
        String key = node != null ? node.getMessageKey() : "new_key";
        String msgHead = Messages.dialog_add_head;
        String msgBody = Messages.dialog_add_body;
        InputDialog dialog = new InputDialog(getShell(), msgHead, msgBody, key,
                new IInputValidator() {
                    public String isValid(String newText) {
                        if (getBundleGroup().isMessageKey(newText)) {
                            return Messages.dialog_error_exists;
                        }
                        return null;
                    }
                });
        dialog.open();
        if (dialog.getReturnCode() == Window.OK) {
            String inputKey = dialog.getValue();
            MessagesBundleGroup messagesBundleGroup = getBundleGroup();
            messagesBundleGroup.addMessages(inputKey);
        }
    }

}
