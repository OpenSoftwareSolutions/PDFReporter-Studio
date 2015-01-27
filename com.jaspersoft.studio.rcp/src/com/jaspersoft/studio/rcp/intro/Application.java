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
package com.jaspersoft.studio.rcp.intro;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.equinox.app.IApplication;
import org.eclipse.equinox.app.IApplicationContext;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.rcp.Activator;
import com.jaspersoft.studio.rcp.OpenDocumentEventProcessor;
import com.jaspersoft.studio.rcp.workspace.PickWorkspaceDialog;

/**
 * This class controls all aspects of the application's execution
 */
public class Application implements IApplication {

    private static final String PROP_EXIT_CODE = "eclipse.exitcode"; //$NON-NLS-1$
	
	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#start(org.eclipse.equinox.app.IApplicationContext)
	 */
	public Object start(IApplicationContext context) throws Exception{
		
	
		OpenDocumentEventProcessor openDocProcessor = new OpenDocumentEventProcessor();
		Display display = PlatformUI.createDisplay();
		display.addListener(SWT.OpenDocument, openDocProcessor);
		
		try {
			Location instanceLoc = Platform.getInstanceLocation(); 
			 
			if(!instanceLoc.allowsDefault() && !instanceLoc.isSet()) {
		        // get the last used workspace location 
		        String lastUsedWs = PickWorkspaceDialog.getLastSetWorkspaceDirectory();
		        // usually do not show pickup dialog at startup
		        boolean showPickupDialog = false;
		 
		        // check to ensure the workspace location is still OK 
	            // if there's any problem whatsoever with the workspace, force a dialog which in its turn will tell them what's bad
	            String ret = PickWorkspaceDialog.checkWorkspaceDirectory(Display.getDefault().getActiveShell(), lastUsedWs, false, false); 
	            if (ret != null) { 
	            	showPickupDialog = true;
	            }
		 
		        if (showPickupDialog) { 
		            PickWorkspaceDialog pwd = new PickWorkspaceDialog(false,Activator.getDefault().getImage("icons/jss_icon_64.png")); 
		            int pick = pwd.open(); 
		 
		            // if the user cancelled, we can't do anything as we need a workspace, so in this case, we tell them and exit 
		            if (pick == Window.CANCEL) { 
			            if (pwd.getSelectedWorkspaceLocation()  == null) { 
			                MessageDialog.openError(display.getActiveShell(), "Error", 
			                    "The application can not start without a workspace root and will now exit."); 
			                try { 
			                PlatformUI.getWorkbench().close(); 
			                } catch (Exception err) { 
			 
			                } 
			                System.exit(0); 
			                return IApplication.EXIT_OK; 
			            } 
		            } 
		            else { 
		            	// tell Eclipse what the selected location was and continue 
		            	instanceLoc.set(new URL("file", null, pwd.getSelectedWorkspaceLocation()), false); 
		            } 
		        } 
		        else { 
		            // set the last used location and continue 
		            instanceLoc.set(new URL("file", null, lastUsedWs), false); 
		        }
			}
			
			int returnCode = PlatformUI.createAndRunWorkbench(display, new ApplicationWorkbenchAdvisor(openDocProcessor));
		    if (returnCode !=  PlatformUI.RETURN_RESTART) return EXIT_OK;
		    return EXIT_RELAUNCH.equals(Integer.getInteger(PROP_EXIT_CODE)) ? EXIT_RELAUNCH : EXIT_RESTART;		} finally {
			display.dispose();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.equinox.app.IApplication#stop()
	 */
	public void stop() {
		final IWorkbench workbench = PlatformUI.getWorkbench();
		if (workbench == null)
			return;
		final Display display = workbench.getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if (!display.isDisposed())
					workbench.close();
			}
		});
	}
}
