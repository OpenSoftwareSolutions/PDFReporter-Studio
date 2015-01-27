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
package org.eclipse.babel.editor.i18n.actions;

import org.eclipse.babel.editor.i18n.AbstractI18NEntry;
import org.eclipse.babel.editor.util.UIUtils;
import org.eclipse.jface.action.Action;

/**
 * @author Pascal Essiembre
 * 
 */
public class FoldingAction extends Action {

    private final AbstractI18NEntry i18NEntry;
    private boolean expanded;

    /**
     * 
     */
    public FoldingAction(AbstractI18NEntry i18NEntry) {
        super();
        this.i18NEntry = i18NEntry;
        this.expanded = i18NEntry.getExpanded();
        setText("Collapse");
        setImageDescriptor(UIUtils.getImageDescriptor("minus.gif"));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.jface.action.IAction#run()
     */
    public void run() {
        if (expanded) {
            setImageDescriptor(UIUtils.getImageDescriptor("plus.gif"));
            i18NEntry.setExpanded(false);
        } else {
            setImageDescriptor(UIUtils.getImageDescriptor("minus.gif"));
            i18NEntry.setExpanded(true);
        }
    }

}
