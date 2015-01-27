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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.jasperserver.ws.examples;

import java.io.IOException;
import java.util.List;

import com.jaspersoft.ireport.jasperserver.ws.JServer;
import com.jaspersoft.ireport.jasperserver.ws.WSClient;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

/**
 * 
 * @author gtoffoli
 */
public class ListRepository {

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		// TODO code application logic here

		JServer server = new JServer();

		server.setUrl("http://localhost:9090/jasperserver-pro/services/repository");
		server.setUsername("jasperadmin|organization_1");
		server.setPassword("jasperadmin");

		WSClient client = new WSClient(server);

		listFolder(client, "/");
	}

	static int depth = 0; // This variable is used to print tabs...

	/**
	 * This function shows how to create a folder in the root directory.
	 * Subfolders can be created just specifying a proper Uri string i.e.
	 * rd.setUriString("/this/is/my/new/folder");
	 * 
	 * @param client
	 * @param folderLabel
	 * @param folderName
	 * @throws IOException
	 */
	public static void listFolder(WSClient client, String folderUri)
			throws Exception {
		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
		rd.setUriString(folderUri);

		List<ResourceDescriptor> children = client.list(rd);

		for (ResourceDescriptor r : children) {
			for (int i = 0; i < depth; ++i)
				System.out.print("  ");
			if (r.getWsType().equals(ResourceDescriptor.TYPE_FOLDER)) {
				System.out.println("[" + r.getLabel() + "]");
				depth++;
				listFolder(client, r.getUriString());
				depth--;
			} else if (r.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT)) {
				r = client.get(r, null);
				listFolder(client, r.getUriString());
			} else {
				System.out.println("" + r.getLabel() + "");
			}
		}
	}

}
