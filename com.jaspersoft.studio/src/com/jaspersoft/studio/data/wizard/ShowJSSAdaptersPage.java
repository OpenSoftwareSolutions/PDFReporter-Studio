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
package com.jaspersoft.studio.data.wizard;

import java.io.StringReader;
import java.util.Properties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * Show a list of checkboxes where every box is a data adapter found inside a configuration
 * file of JSS in another workspace
 * 
 * @author Orlandin Marco
 *
 */
public class ShowJSSAdaptersPage extends ShowAdaptersPage {

	
	protected ShowJSSAdaptersPage() {
		super(); 
		setTitle(Messages.ShowAdaptersPage_title);
		setDescription(Messages.ShowAdaptersPage_description);
	}
	
	
	protected void createCheckboxes(Properties prop){
		String connectionXML = prop.getProperty("dataAdapters"); //$NON-NLS-1$
		Document document;
		try {
			document = JRXmlUtils.parse(new InputSource(new StringReader(connectionXML)));
			Node actualNode = document.getFirstChild();
			if (actualNode.hasChildNodes()) actualNode = actualNode.getFirstChild();
			else actualNode = null;
			while(actualNode != null){
				if (actualNode.getAttributes() != null){
					String name = actualNode.getChildNodes().item(0).getTextContent();
					String type = actualNode.getAttributes().getNamedItem("class").getTextContent();
					type = type.substring(type.lastIndexOf(".")+1);
					Button checkButton = new Button(content, SWT.CHECK);
					checkButton.setText(name+" ("+ type + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					checkButton.setData(actualNode);
					selectedElements.add(checkButton);
				}
				actualNode = actualNode.getNextSibling();
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_IMPORT_SELECT_ADAPTERS;
	}

}
