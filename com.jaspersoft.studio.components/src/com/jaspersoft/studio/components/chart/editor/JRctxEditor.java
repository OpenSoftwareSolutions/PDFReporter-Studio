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
package com.jaspersoft.studio.components.chart.editor;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.charts.ChartThemeBundle;
import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.chartthemes.simple.FileImageProvider;
import net.sf.jasperreports.chartthemes.simple.ImageProvider;
import net.sf.jasperreports.chartthemes.simple.XmlChartTheme;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.components.chart.model.theme.ChartSettingsFactory;
import com.jaspersoft.studio.editor.AMultiEditor;
import com.jaspersoft.studio.jasper.CachedImageProvider;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;

public class JRctxEditor extends AMultiEditor {

	public JRctxEditor() {
		super();
	}

	@Override
	public void doSaveParticipate(IProgressMonitor monitor) {
		ctEditor.doSave(monitor);
	}

	protected void xml2model(InputStream in) {
		ChartThemeSettings cts = XmlChartTheme.loadSettings(in);
		MRoot root = ChartSettingsFactory.createModel(cts);
		root.setJasperConfiguration(jrContext);
		List<ChartThemeBundle> lst = new ArrayList<ChartThemeBundle>();
		lst.add(new JRCTXExtensionsRegistryFactory(cts));
		jrContext.setExtensions(ChartThemeBundle.class, lst);
		setModel(root);
	}

	private ImageProvider getImageProvider(ImageProvider ip) {
		if (ip != null)
			if (ip instanceof CachedImageProvider)
				return new FileImageProvider(
						((CachedImageProvider) ip).getFile());
		return ip;
	}

	protected String doModel2xml() throws Exception {
		ChartThemeSettings cts = (ChartThemeSettings) model.getChildren()
				.get(0).getValue();

		cts.getChartSettings().setBackgroundImage(
				getImageProvider(cts.getChartSettings().getBackgroundImage()));
		cts.getPlotSettings().setBackgroundImage(
				getImageProvider(cts.getPlotSettings().getBackgroundImage()));

		String xml = XmlChartTheme.saveSettings(cts);
		xml = xml
				.replaceFirst(
						"<chart-theme>", "<!-- Created with Jaspersoft Studio -->\n<chart-theme>"); //$NON-NLS-1$ //$NON-NLS-2$
		return xml;
	}

	public void setModel(INode model) {
		super.setModel(model);
		if (ctEditor != null)
			ctEditor.setModel(model);
	}
	
	public INode getModel(){
		return model;
	}

	@Override
	protected void createPages() {
		PlatformUI
				.getWorkbench()
				.getHelpSystem()
				.setHelp(getContainer(),
						"com.jaspersoft.studio.doc.editor_jrctx");

		createPage0();
		createPageXML();
	}

	private ChartThemeEditor ctEditor;

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the
	 * font used in page 2.
	 */
	void createPage0() {
		try {
			ctEditor = new ChartThemeEditor(jrContext);

			int index = addPage(ctEditor, getEditorInput());
			setPageText(index, "Preview");
		} catch (PartInitException e) {
			UIUtils.showError(e);
		}
	}

}
