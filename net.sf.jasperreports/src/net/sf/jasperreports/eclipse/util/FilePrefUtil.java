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
package net.sf.jasperreports.eclipse.util;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;

public class FilePrefUtil {
	private static final String PREFIX = "JRPROPERTIESPREFIX.";

	public static void savePreferences(IFile f, String qualifier, Properties props) {
		for (String key : props.stringPropertyNames()) {
			try {
				String value = props.getProperty(key);
				if (key.equals(NET_SF_JASPERREPORTS_JRPROPERTIES)) {
					Properties ps = FileUtils.load(value);
					for (String k : ps.stringPropertyNames())
						f.setPersistentProperty(new QualifiedName(qualifier, PREFIX + k), ps.getProperty(k));
				} else
					f.setPersistentProperty(new QualifiedName(qualifier, key), value);
			} catch (CoreException e) {
				JasperReportsPlugin.getDefault().logError(e);
			} catch (IOException e) {
				JasperReportsPlugin.getDefault().logError(e);
			}
		}
	}

	public static Properties loadPreferences(IFile f) throws CoreException {
		int PLENGHT = PREFIX.length();
		Properties props = new Properties();
		String p = "";
		Map<QualifiedName, String> map = f.getPersistentProperties();
		for (QualifiedName qn : map.keySet()) {
			String key = qn.getLocalName();
			String value = map.get(qn);
			if (key.startsWith(PREFIX))
				p += key.substring(PLENGHT) + "=" + value + "\n";
			else
				props.put(key, value);
		}
		if (!p.isEmpty())
			props.put(NET_SF_JASPERREPORTS_JRPROPERTIES, p);
		return props;
	}

	public static final String NET_SF_JASPERREPORTS_JRPROPERTIES = "net.sf.jasperreports.JRPROPERTIES"; //$NON-NLS-1$
}
