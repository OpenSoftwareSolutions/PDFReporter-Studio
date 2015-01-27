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

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.jaspersoft.ireport.jasperserver.ws.FileContent;
import com.jaspersoft.ireport.jasperserver.ws.JServer;
import com.jaspersoft.ireport.jasperserver.ws.WSClient;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

/**
 * 
 * @author gtoffoli
 */
public class RunReportUnit {

	/**
	 * @param args
	 *          the command line arguments
	 */
	public static void main(String[] args) throws Exception {
		// TODO code application logic here

		JServer server = new JServer();

		server.setUrl("http://build-master.jaspersoft.com:5980/jrs-pro-trunk/");
		server.setUsername("superuser");
		server.setPassword("superuser");

		WSClient client = new WSClient(server);

		runReportUnit(client, "/public/Samples/Reports/01._Geographic_Results_by_Segment_Report", "");
	}

	/**
	 * Run a report and save the output in PDF and HTML
	 * 
	 * @param client
	 * @param reportUri
	 * @param parameter1
	 * @throws Exception
	 */
	public static void runReportUnit(WSClient client, String reportUri, String parameter1) throws Exception {

		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setUriString(reportUri);

		Map parameters = new HashMap();
		// parameters.put("parameter1", "A");

		List arguments = new ArrayList();
		arguments.add(new Argument(Argument.RUN_OUTPUT_FORMAT, Argument.RUN_OUTPUT_FORMAT_PDF));

		Map files = client.runReport(rd, parameters, arguments);

		FileContent fc = (FileContent) files.get("report");

		FileOutputStream pdfFile = new FileOutputStream("/tmp/myreport.pdf");
		pdfFile.write(fc.getData());
		pdfFile.close();

		System.out.println("PDF file saved to: c:\\myreport.pdf");

		arguments.clear();
		arguments.add(new Argument(Argument.RUN_OUTPUT_FORMAT, Argument.RUN_OUTPUT_FORMAT_HTML));

		files = client.runReport(rd, parameters, arguments);

		Iterator iter = files.keySet().iterator();
		while (iter.hasNext()) {
			String key = (String) iter.next();
			fc = (FileContent) files.get(key);

			if (key.equals("report")) {
				FileOutputStream htmlFile = new FileOutputStream("/tmp/myreport.html");
				htmlFile.write(fc.getData());
				htmlFile.close();
			} else {
				File f = new File("/tmp/images");
				if (!f.exists())
					f.mkdirs();

				FileOutputStream imageFile = new FileOutputStream("/tmp/images/" + key);
				imageFile.write(fc.getData());
				imageFile.close();
			}

		}

		System.out.println("Html file saved to: c:\\myreport.html");

	}

}
