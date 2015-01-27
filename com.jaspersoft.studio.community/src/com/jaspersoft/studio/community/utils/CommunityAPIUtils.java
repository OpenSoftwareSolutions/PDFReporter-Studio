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
package com.jaspersoft.studio.community.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.zip.ZipOutputStream;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.BundleCommonUtils;
import net.sf.jasperreports.soutils.EnvironmentUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.internal.core.utils.IOUtils2;
import org.eclipse.wb.internal.core.utils.platform.PlatformInfo;
import org.eclipse.wb.internal.core.utils.platform.PluginUtilities;

import com.fasterxml.jackson.core.io.JsonStringEncoder;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.community.JSSCommunityActivator;
import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.zip.ZipEntry;
import com.jaspersoft.studio.utils.BrandingInfo;


/**
 * Generic utility methods for this plug-in.
 * 
 * <b>NOTE</b>: some methods and apis in this class have been adapted from existing code in the
 * <code>org.eclipse.wb.core</code> project belonging to <i>WindowsBuilder</i> plugin.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CommunityAPIUtils {
	// Constants
	private static final String CR = "\n"; //$NON-NLS-1$
	private static final String JSS_PREFS_RELATIVE_LOCATION = 
			".metadata/.plugins/org.eclipse.core.runtime/.settings/com.jaspersoft.studio.prefs"; //$NON-NLS-1$
	private static final String JSS_LOG_RELATIVE_LOCATION = ".metadata/.log"; //$NON-NLS-1$
	
	/**
	 * Creates a ZIP file using the specified zip entries.
	 * 
	 * @param zipEntries the list of entries that will end up in the final zip file
	 * @return the zip file reference
	 * @throws CommunityAPIException
	 */
	public static File createZipFile(List<ZipEntry> zipEntries) throws CommunityAPIException {
		String tmpDirectory = System.getProperty("java.io.tmpdir"); //$NON-NLS-1$
		String zipFileLocation = tmpDirectory;
		if(!(tmpDirectory.endsWith("/") || tmpDirectory.endsWith("\\"))){ //$NON-NLS-1$ //$NON-NLS-2$
			zipFileLocation += System.getProperty("file.separator"); //$NON-NLS-1$
		}
		zipFileLocation += "issueDetails.zip"; //$NON-NLS-1$
		
		try {
			// create byte buffer
			byte[] buffer = new byte[1024];
			// create object of FileOutputStream
			FileOutputStream fout = new FileOutputStream(zipFileLocation);
			// create object of ZipOutputStream from FileOutputStream
			ZipOutputStream zout = new ZipOutputStream(fout);
			
			for(ZipEntry ze : zipEntries){
				//create object of FileInputStream for source file
			    FileInputStream fin = new FileInputStream(ze.getLocation());
			    zout.putNextEntry(new java.util.zip.ZipEntry(ze.getLocation()));
				// After creating entry in the zip file, actually write the file.
				int length;
				while ((length = fin.read(buffer)) > 0) {
					zout.write(buffer, 0, length);
				}
				//close the zip entry and related InputStream
				zout.closeEntry();
			    fin.close();
			}
			//close the ZipOutputStream
			zout.close();
		} catch (IOException e) {
			throw new CommunityAPIException(Messages.CommunityAPIUtils_ZipCreationError, e);
		}
		return new File(zipFileLocation);
	}
	
	/**
	 * @return the location of Jaspersoft Studio preferences file
	 */
	public static String getJaspersoftStudioPrefsLocation(){
		return BundleCommonUtils.getWorkspaceLocation() + "/" + JSS_PREFS_RELATIVE_LOCATION; //$NON-NLS-1$
	}
	
	/**
	 * @return the location of Jaspersoft Studio log file
	 */
	public static String getJaspersoftStudioLogFileLocation(){
		return BundleCommonUtils.getWorkspaceLocation() + "/" + JSS_LOG_RELATIVE_LOCATION; //$NON-NLS-1$
	}
	
	
	/**
	 * @return software and hardware info.
	 */
	public static String getHardwareSoftwareInfo() {
		BrandingInfo currBranding = JaspersoftStudioPlugin.getInstance().getBrandingInformation();
		String c = ""; //$NON-NLS-1$
		c += "Product Name: " + currBranding.getProductName() + CR; //$NON-NLS-1$
		c += "Product Version: " + currBranding.getProductVersion() + CR; //$NON-NLS-1$
		c += "Installation Path: " + getInstallationPath(currBranding.getProductMainBundleID()) + CR; //$NON-NLS-1$
		c += "Eclipse Version: " + PlatformInfo.getEclipseVersion().toString() //$NON-NLS-1$
				+ CR;
		c += "Eclipse Build Name: " + PlatformInfo.getEclipseBuildName() + CR; //$NON-NLS-1$
		c += "Eclipse Build ID: " + PlatformInfo.getEclipseBuildId() + CR; //$NON-NLS-1$
		c += "IDE Name: " + PlatformInfo.getIDEName() + CR; //$NON-NLS-1$
		c += "IDE Version: " + PlatformInfo.getIDEVersionString() + CR; //$NON-NLS-1$
		c += "IDE NL: " + PlatformInfo.getIDENL() + CR; //$NON-NLS-1$
		c += "Eclipse Commands: " //$NON-NLS-1$
				+ StringUtils.replaceChars(
						getSystemProperty("eclipse.commands"), "\n\r", " ") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ CR;
		c += "Eclipse VM: " + getSystemProperty("eclipse.vm") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		c += "Eclipse VM Args: " + getSystemProperty("eclipse.vmargs") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		c += "OS Name: " + getSystemProperty("os.name") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		c += "OS Arch: " + getSystemProperty("os.arch") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		c += "OS Version: " + getSystemProperty("os.version") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		String linuxDescription = getLinuxDescription();
		if (!StringUtils.isEmpty(linuxDescription)) {
			c += "Linux Description: " + linuxDescription + CR; //$NON-NLS-1$
		}
		String m_mozillaResult = tryCreateMozilla();
		if (!StringUtils.isEmpty(m_mozillaResult)) {
			c += "Browser Creation Result: " + m_mozillaResult + CR; //$NON-NLS-1$
		}
		Runtime runtime = Runtime.getRuntime();
		c += "Available Processors: " + runtime.availableProcessors() + CR; //$NON-NLS-1$
		c += "Memory Max: " + runtime.maxMemory() + CR; //$NON-NLS-1$
		c += "Memory Total: " + runtime.totalMemory() + CR; //$NON-NLS-1$
		c += "Memory Free: " + runtime.freeMemory() + CR; //$NON-NLS-1$
		c += "Java Vendor: " + getSystemProperty("java.vendor") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		c += "Java Version: " + getSystemProperty("java.version") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		c += "Java Library Path: " + getSystemProperty("java.library.path") + CR; //$NON-NLS-1$ //$NON-NLS-2$
		return c;
	}

	/*
	 * Returns the installation path of the running product.
	 */
	private static String getInstallationPath(String pluginID) {
		URL installUrl = PluginUtilities.getInstallUrl(pluginID);
		String installationPath = "Unknown"; //$NON-NLS-1$
		try {
			if (installUrl != null) {
				installationPath = FileLocator.toFileURL(installUrl).getPath();
				if (installationPath.length() > 3
						&& installationPath.charAt(0) == '/'
						&& installationPath.charAt(2) == ':') {
					installationPath = installationPath.substring(1);
				}
			}
		} catch (IOException e) {
			JSSCommunityActivator.getDefault().logError(Messages.CommunityAPIUtils_ErrorMsgProductPath , e);
		}
		return installationPath;
	}
	
	/**
	 * Get system property and return empty string if no such property.
	 * 
	 * @param prop
	 *            the property name.
	 */
	private static String getSystemProperty(String prop) {
		String propValue = System.getProperty(prop);
		return propValue == null ? "" : propValue; //$NON-NLS-1$
	}

	/**
	 * Returns the contents of '/etc/lsb-release' (and/or others).
	 */
	private static String getLinuxDescription() {
		StringBuilder result = new StringBuilder();
		if (EnvironmentUtils.IS_LINUX) {
			String[] files = new String[] { "/etc/lsb-release", //$NON-NLS-1$
					"/etc/lsb_release", "/etc/system-release", //$NON-NLS-1$ //$NON-NLS-2$
					"/etc/fedora-release", "/etc/SuSE-release", //$NON-NLS-1$ //$NON-NLS-2$
					"/etc/redhat-release", "/etc/release", //$NON-NLS-1$ //$NON-NLS-2$
					"/proc/version_signature", "/proc/version", "/etc/issue", }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			for (int i = 0; i < files.length; i++) {
				File file = new File(files[i]);
				if (file.exists() && file.canRead()) {
					try {
						String version = IOUtils2.readString(file).trim();
						if (version != null && result.indexOf(version) == -1) {
							result.append(version);
							result.append("\n"); //$NON-NLS-1$
						}
					} catch (IOException e) {
						JSSCommunityActivator.getDefault().logError(
								MessageFormat.format(Messages.CommunityAPIUtils_ErrorMsgReadingFile, new Object[]{file.getAbsolutePath()}) , e);
					}
				}
			}
		}
		return result.toString();
	}

	private static String tryCreateMozilla() {
		if (EnvironmentUtils.IS_LINUX) {
			boolean oldDebug = Device.DEBUG;
			Device.DEBUG = true;
			PrintStream oldOut = System.out;
			Shell shell = null;
			PrintStream newOut = null;
			try {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				newOut = new PrintStream(baos);
				// replace the out since the Mozilla output debug results into
				// stdout.
				System.setOut(newOut);
				shell = new Shell();
				try {
					new Browser(shell, SWT.NONE);
				} catch (Throwable e) {
					UIUtils.showError(e);
				}
				return baos.toString();
			} catch (Throwable e1) {
				// ignore
			} finally {
				if (shell != null) {
					shell.dispose();
				}
				System.setOut(oldOut);
				IOUtils.closeQuietly(newOut);
				Device.DEBUG = oldDebug;
			}
		}
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * Sanitize a string to be used in JSON data.
	 */
	public static String jsonStringSanitize(String inputString) {
		char[] stringChars = JsonStringEncoder.getInstance().quoteAsString(inputString);
		return new String(stringChars);
	}
}
