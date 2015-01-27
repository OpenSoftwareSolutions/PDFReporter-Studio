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
package com.jaspersoft.studio.doc.samples.handlers;


import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.internal.ui.javaeditor.EditorUtility;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.doc.handlers.AsyncAction;
import com.jaspersoft.studio.doc.samples.messages.Messages;


/**
 * Action for the cheatsheets to open in the editor a report with a fixed name
 * 
 * @author Orlandin Marco
 *
 */
public class OpenReportHandler extends Action {
	
	private String exampleReportName = "ImagesReport.jrxml"; //$NON-NLS-1$
	
	private String exampleReportPath = "/JasperReportsSamples/images/reports/"; //$NON-NLS-1$
	
	@Override
	public void run() {
		IPath path = new Path(exampleReportPath.concat(exampleReportName));
		IFile sampleFile =  ResourcesPlugin.getWorkspace().getRoot().getFile(path);
		if (sampleFile.exists()){
			try {
				EditorUtility.openInEditor(sampleFile, true);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}  else {
			String message =  Messages.OpenReportHandler_warningmessage_text1.concat(exampleReportName).concat(Messages.OpenReportHandler_warningmessage_text2);
			MessageDialog.openWarning(PlatformUI.getWorkbench().getDisplay().getActiveShell(), Messages.OpenReportHandler_warningmessage_title,message);
		}
	}
	
}
