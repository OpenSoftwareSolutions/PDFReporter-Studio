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

import org.eclipse.babel.editor.internal.AbstractMessagesEditor;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.babel.messages.Messages;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * @author Pascal Essiembre
 * 
 */
public abstract class AbstractRenameKeyAction extends AbstractTreeAction {

    public static final String INSTANCE_CLASS = "org.eclipse.babel.editor.tree.actions.RenameKeyAction";

    public AbstractRenameKeyAction(AbstractMessagesEditor editor,
            TreeViewer treeViewer) {
        super(editor, treeViewer);
        setText(Messages.key_rename + " ..."); //$NON-NLS-1$
        setImageDescriptor(UIUtils.getImageDescriptor(UIUtils.IMAGE_RENAME));
    }

    /**
     * @see org.eclipse.jface.action.Action#run()
     */
    @Override
    public abstract void run();
}
