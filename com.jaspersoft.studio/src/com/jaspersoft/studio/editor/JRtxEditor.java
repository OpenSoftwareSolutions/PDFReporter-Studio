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
package com.jaspersoft.studio.editor;

import java.io.InputStream;
import java.util.HashSet;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;
import net.sf.jasperreports.engine.xml.JRXmlTemplateWriter;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.CommonSelectionCacheProvider;
import com.jaspersoft.studio.editor.style.StyleTemplateEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.style.MStylesTemplate;
import com.jaspersoft.studio.model.style.StyleTemplateFactory;

public class JRtxEditor extends AMultiEditor implements CachedSelectionProvider{

	
	/**
	 * The selection cache
	 */
	private CommonSelectionCacheProvider selectionCache;
	
	public JRtxEditor() {
		super();
		selectionCache = new CommonSelectionCacheProvider();
	}

	@Override
	public void doSaveParticipate(IProgressMonitor monitor) {
		styleEditor.doSave(monitor);
	}

	protected void xml2model(InputStream in) {
		JRTemplate jd = JRXmlTemplateLoader.load(in);
		ANode m = new MRoot(null, new JasperDesign());
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		MStylesTemplate ms = new MStylesTemplate(m, file);
		ms.setValue(jd);
		ms.setJasperConfiguration(jrContext);
		StyleTemplateFactory.createTemplate(ms, new HashSet<String>(), true, file, file.getLocation().toFile(),
				(JRSimpleTemplate) jd);
		setModel(m);
	}

	protected String doModel2xml() throws Exception {
		JRSimpleTemplate report = (JRSimpleTemplate) model.getChildren().get(0).getValue();
		IFile file = ((IFileEditorInput) getEditorInput()).getFile();
		String xml = JRXmlTemplateWriter.writeTemplate(report, JRXmlWriterHelper.fixencoding(file.getCharset(true)));
		xml = xml.replaceFirst("<jasperTemplate ", "<!-- Created with Jaspersoft Studio -->\n<jasperTemplate "); //$NON-NLS-1$ //$NON-NLS-2$
		return xml;
	}

	public void setModel(INode model) {
		super.setModel(model);
		if (styleEditor != null)
			styleEditor.setModel(model);
	}

	public INode getModel(){
		return model;
	}
	
	@Override
	protected void createPages() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getContainer(), "com.jaspersoft.studio.doc.editor_jrtx");

		createPage0();
		createPageXML();
	}

	private StyleTemplateEditor styleEditor;

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the font used in page 2.
	 */
	void createPage0() {
		try {
			styleEditor = new StyleTemplateEditor(jrContext);

			int index = addPage(styleEditor, getEditorInput());
			setPageText(index, "Preview");
		} catch (PartInitException e) {
			UIUtils.showError(e);
		}
	}

	@Override
	public CommonSelectionCacheProvider getSelectionCache() {
		return selectionCache;
	}

}
