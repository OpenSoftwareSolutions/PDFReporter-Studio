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
package com.jaspersoft.studio.editor.style.wizard;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import com.jaspersoft.studio.messages.Messages;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.xml.JRXmlTemplateWriter;

/**
 * Wizard to export one or more JRStyle as a separate TemplateStyle file
 * 
 * @author Orlandin Marco
 *
 */
public class StyleTemplateImportWizard extends StyleTemplateNewWizard {

	/**
	 * List of the style to export
	 */
	private List<JRStyle> stylesToImport;
	
	/**
	 * Create the wizard
	 * 
	 * @param stylesToImport styles to export
	 */
	public StyleTemplateImportWizard(List<JRStyle> stylesToImport){
		this.stylesToImport = stylesToImport;
	}
	
	/**
	 * We will initialize file contents with the imported styles
	 */
	@Override
	protected InputStream openContentStream() {
		try {
			JRSimpleTemplate tmp = new JRSimpleTemplate();
			if (stylesToImport == null || stylesToImport.isEmpty()){
				JRDesignStyle jrDesignStyle = new JRDesignStyle();
				jrDesignStyle.setName("SimpleStyle"); //$NON-NLS-1$
				tmp.addStyle(jrDesignStyle);
			} else {
				for(JRStyle style : stylesToImport){
					tmp.addStyle(style);
				}
			}
			String contents = JRXmlTemplateWriter.writeTemplate(tmp);
			return new ByteArrayInputStream(contents.getBytes());
		} catch (JRException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Override of add pages to set a different page title\description from the superclass
	 */
	public void addPages() {
		super.addPages();
		step1.setTitle(Messages.StyleTemplateImportWizard_title);
		step1.setDescription(Messages.StyleTemplateImportWizard_description);
	}
}
