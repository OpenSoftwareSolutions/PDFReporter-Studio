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
package com.jaspersoft.studio.rcp.heartbeat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.rcp.Activator;

public class Heartbeat {
	private static final String UUID_PROPERTY;
	public static final String VERSION;
	private static String version;
	private static String optmsg;

	static {
		UUID_PROPERTY = "UUID";
		String ver = "x.x.x - NOT DETECTED"; // $//$NON-NLS-1$
		try {
			// Get JSS version directly from the plugin one:
			// for sure it will be kept in sync with the product one.
			ver = Activator.getDefault().getBundle().getVersion().toString();
		} catch (Exception ex) {
			// Should never happen...
		} finally {
			VERSION = ver;
		}
	}

	public static void run() {
		final PropertiesHelper ph = PropertiesHelper.getInstance();
		String uuid = ph.getString(UUID_PROPERTY, null);
		int newInstallation = 0;
		if (uuid == null || uuid.length() == 0) {
			newInstallation = 1;
			uuid = UUID.randomUUID().toString();
			ph.setString(UUID_PROPERTY, uuid, InstanceScope.SCOPE);
		}

 		String urlstr = "http://pdfreporterstudio.sf.net/prslastversion.php?version="
				+ VERSION + "&uuid=" + uuid + "&new=" + newInstallation;
 		
		logInfo("Checking for new version at URL: '" + urlstr + "'");
		
		BufferedReader in = null;
		try {
			URL url = new URL(urlstr);
			URLConnection yc = url.openConnection();
			in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

			version = null;
			optmsg = "";
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				logInfo(" -- read line: '" + inputLine + "'");
				if (version == null)
					version = inputLine.trim();
				else
					optmsg += inputLine;
			}
			if (version != null && version.compareTo(VERSION) > 0) {
				logInfo("Comparing '" + version + "' vs '" + VERSION + "'");
				if (ph.getBoolean("show_update_dialog", true)) {
					Display.getDefault().asyncExec(new Runnable() {

						public void run() {
							VersionUpdateDialog ud = new VersionUpdateDialog(
									Display.getDefault().getActiveShell());
							ud.setNewVersion(version);
							ud.setOptionalMessage(optmsg);
							if (ud.open() == Dialog.OK) {
								if (ud.isNotShowAgain()) {
									ph.setBoolean("show_update_dialog", false,
											InstanceScope.SCOPE);
								}
							}
						}
					});
					Thread.sleep(100000);
				}

			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

	}
	
    public static void logInfo(String msg){
        IStatus st = new Status(IStatus.INFO,Activator.PLUGIN_ID, msg);
        Activator.getDefault().getLog().log(st);
    }
    
    public static void logException(String msg, Throwable e){
        IStatus st = new Status(IStatus.ERROR,Activator.PLUGIN_ID, msg, e);
        Activator.getDefault().getLog().log(st);
    }

}
