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
package com.jaspersoft.studio.server.editor;

import java.util.LinkedHashMap;
import java.util.Set;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.AViewsFactory;
import com.jaspersoft.studio.editor.preview.view.report.file.CSVViewer;
import com.jaspersoft.studio.editor.preview.view.report.file.TextFileViewer;
import com.jaspersoft.studio.editor.preview.view.report.file.XMLViewer;
import com.jaspersoft.studio.editor.preview.view.report.html.ABrowserViewer;
import com.jaspersoft.studio.editor.preview.view.report.swt.SWTViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.DocxViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.OdsViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.PdfViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.RTFViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.XlsViewer;
import com.jaspersoft.studio.editor.preview.view.report.system.XlsxViewer;

public class ReportUnitViewsFactory extends AViewsFactory {
	public static final String DEFAULT = Argument.RUN_OUTPUT_FORMAT_HTML;

	private static LinkedHashMap<String, Class<? extends APreview>> pcmap = new LinkedHashMap<String, Class<? extends APreview>>();
	static {
		pcmap.put(Argument.RUN_OUTPUT_FORMAT_JRPRINT, SWTViewer.class);

		pcmap.put("SEPARATOR1", null);

		pcmap.put(Argument.RUN_OUTPUT_FORMAT_HTML, ABrowserViewer.class);
		pcmap.put("SEPARATOR1", null);
		pcmap.put(Argument.RUN_OUTPUT_FORMAT_PDF, ABrowserViewer.class);

		pcmap.put("SEPARATOR2", null);

		pcmap.put(Argument.RUN_OUTPUT_FORMAT_RTF, ABrowserViewer.class);
		pcmap.put(Argument.RUN_OUTPUT_FORMAT_DOCX, ABrowserViewer.class);
		// pcmap.put("ODT", OdtViewer.class);
		pcmap.put(Argument.RUN_OUTPUT_FORMAT_ODS, ABrowserViewer.class);
		// pcmap.put("PPTx", PowerPointViewer.class);
		// pcmap.put("Text", TXTViewer.class);

		pcmap.put("SEPARATOR3", null);

		pcmap.put(Argument.RUN_OUTPUT_FORMAT_XLS, ABrowserViewer.class);
		pcmap.put(Argument.RUN_OUTPUT_FORMAT_XLSX, ABrowserViewer.class);
		pcmap.put(Argument.RUN_OUTPUT_FORMAT_CSV, TextFileViewer.class);

		pcmap.put("SEPARATOR4", null);

		pcmap.put(Argument.RUN_OUTPUT_FORMAT_XML, TextFileViewer.class);
	}

	@Override
	public String getLabel(String key) {
		if (key.equals(Argument.RUN_OUTPUT_FORMAT_JRPRINT))
			return "Java";
		return super.getLabel(key);
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
