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
package com.jaspersoft.studio.editor.preview.view;

import java.util.LinkedHashMap;
import java.util.Set;

import com.jaspersoft.studio.editor.preview.view.report.file.CSVMetadataViewer;
import com.jaspersoft.studio.editor.preview.view.report.file.CSVViewer;
import com.jaspersoft.studio.editor.preview.view.report.file.TXTViewer;
import com.jaspersoft.studio.editor.preview.view.report.file.XMLImagesViewer;
import com.jaspersoft.studio.editor.preview.view.report.file.XMLViewer;
import com.jaspersoft.studio.editor.preview.view.report.html.HTMLViewer;
import com.jaspersoft.studio.editor.preview.view.report.html.LayeredHTMLViewer;
import com.jaspersoft.studio.editor.preview.view.report.html.XHTMLViewer;
import com.jaspersoft.studio.editor.preview.view.report.swt.SWTViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.DocxViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.ExcelAPIViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.OdsViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.OdtViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.PdfViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.PowerPointViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.RTFViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.XlsMetadataViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.XlsViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.XlsxViewer;

public class ViewsFactory extends AViewsFactory {
	public static final String VIEWER_JAVA = "Java";
	private static LinkedHashMap<String, Class<? extends APreview>> pcmap = new LinkedHashMap<String, Class<? extends APreview>>();
	static {
		pcmap.put(VIEWER_JAVA, SWTViewer.class);

		pcmap.put("SEPARATOR1", null);

//		pcmap.put("Layered HTML", LayeredHTMLViewer.class);
//		pcmap.put("HTML", HTMLViewer.class);
//		pcmap.put("xHTML", XHTMLViewer.class);
//		pcmap.put("SEPARATOR1", null);
//		pcmap.put("SEPARATOR1", null);
		pcmap.put("PDF", PdfViewer.class);

//		pcmap.put("SEPARATOR2", null);
//
//		pcmap.put("RTF", RTFViewer.class);
//		pcmap.put("DOCx", DocxViewer.class);
//		pcmap.put("ODT", OdtViewer.class);
//		pcmap.put("ODS", OdsViewer.class);
//		pcmap.put("PPTx", PowerPointViewer.class);
//		pcmap.put("Text", TXTViewer.class);
//
//		pcmap.put("SEPARATOR3", null);
//
//		pcmap.put("XLS", XlsViewer.class);
//		pcmap.put("XLS Metadata", XlsMetadataViewer.class);
//		pcmap.put("XLSx", XlsxViewer.class);
//		pcmap.put("ExcelAPI", ExcelAPIViewer.class);
//		pcmap.put("CSV", CSVViewer.class);
//		pcmap.put("CSV Metadata", CSVMetadataViewer.class);
//
//		pcmap.put("SEPARATOR4", null);
//
//		pcmap.put("XML", XMLViewer.class);
//		pcmap.put("XML With Images", XMLImagesViewer.class);
	}

	/**
	 * Return the available keys for the preview area, may contains separator
	 */
	public Set<String> getKeys() {
		return pcmap.keySet();
	}

	@Override
	protected LinkedHashMap<String, Class<? extends APreview>> getMap() {
		return pcmap;
	}

}
