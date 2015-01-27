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
package com.jaspersoft.studio.server.protocol;

import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo.ServerEdition;
import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;

public class Version {
	public static boolean isPro(ServerInfo si) {
		return si.getEdition() == ServerEdition.PRO;
	}

	public static String setJRVersion(ServerInfo si) {
		for (String av : JRXmlWriterHelper.getVersionsSet()) {
			if (av.equals(si.getVersion()))
				return av;
		}
		return JRXmlWriterHelper.LAST_VERSION;
	}

	public static boolean isEstimated(ServerInfo si) {
		for (String av : JRXmlWriterHelper.getVersionsSet()) {
			if (av.equals(si.getVersion()))
				return false;
		}
		return true;
	}

	public static boolean isDateRangeSupported(ServerInfo si) {
		return si.getVersion().startsWith("5.");
	}

	public static boolean isXMLACoonnectionSupported(IConnection c) {
		if (c.isSupported(Feature.SEARCHREPOSITORY)) {
			ServerInfo si = c.getServerInfo();
			return si.getVersion().startsWith("5.5") || si.getVersion().startsWith("5.6");
		}
		return true;
	}

	public static boolean isGreaterThan(ServerInfo si, String version) {
		return si.getVersion().compareTo(version) >= 0;
	}
}
