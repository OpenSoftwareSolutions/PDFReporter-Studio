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
package com.jaspersoft.studio.rcp.workspace;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.rcp.messages.Messages;

/**
 * Action for workspace switching.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ActionSwitchWorkspace extends Action { 
 
	public static final String ID = "com.jaspersoft.studio.rcp.switchworkspace"; //$NON-NLS-1$
	private Image titleImage; 
 
    public ActionSwitchWorkspace(Image titleImage) { 
        super(Messages.ActionSwitchWorkspace_ActionLabel);
		setId(ID);
        this.titleImage = titleImage; 
    } 
 
    @Override 
    public void run() { 
        PickWorkspaceDialog pwd = new PickWorkspaceDialog(true, titleImage); 
        int pick = pwd.open(); 
        if (pick == Dialog.CANCEL) 
            return; 
 
        MessageDialog.openInformation(
        		UIUtils.getShell(), Messages.ActionSwitchWorkspace_DialogTitle, Messages.ActionSwitchWorkspace_RestartMsg); 
 
        PlatformUI.getWorkbench().restart(); 
    } 
    
} 
