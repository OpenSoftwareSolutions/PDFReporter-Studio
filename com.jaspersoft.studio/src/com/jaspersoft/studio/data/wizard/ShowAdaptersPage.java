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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.data.adapter.ImportUtility;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Show a list of checkboxes where every box is a data adapter found inside a configuration
 * file of iReport
 * 
 * @author Orlandin Marco
 *
 */
public class ShowAdaptersPage extends JSSHelpWizardPage {

	/**
	 * Configuration of iReport where the data adapters are searched
	 */
	protected IReportDescriptor selectedInstallation;
	
	/**
	 * List of all the checkboxes
	 */
	protected List<Button> selectedElements;
	
	/**
	 * composite where the checkboxes are placed
	 */
	protected Composite content;
	
	/**
	 * Label shown where there aren't element that could be imported
	 */
	protected Label noElementLabel = null;
	
	protected ShowAdaptersPage() {
		super("IReportDatasourceList"); //$NON-NLS-1$
		selectedElements = new ArrayList<Button>();
		setTitle(Messages.ShowAdaptersPage_title);
		setDescription(Messages.ShowAdaptersPage_description);
	}
	
	
	protected void createCheckboxes(Properties prop){
		Integer connectionIndex = 0;
		String connectionXML = prop.getProperty("connection." + connectionIndex); //$NON-NLS-1$
		while(connectionXML != null){
			try {
				Document document = JRXmlUtils.parse(new InputSource(new StringReader(connectionXML)));
				NamedNodeMap rootAttributes = document.getChildNodes().item(0).getAttributes();
				String connectionName = rootAttributes.getNamedItem("name").getTextContent(); //$NON-NLS-1$
				String connectionClass = rootAttributes.getNamedItem("connectionClass").getTextContent(); //$NON-NLS-1$
				if (ImportUtility.hasAdapter(connectionClass)){
					Button checkButton = new Button(content, SWT.CHECK);
					String type = connectionClass.substring(connectionClass.lastIndexOf(".")+1); //$NON-NLS-1$
					checkButton.setText(connectionName+" ("+ type + ")"); //$NON-NLS-1$ //$NON-NLS-2$
					checkButton.setData(document);
					selectedElements.add(checkButton);
				}
			} catch (JRException e) {}
			connectionIndex++;
			connectionXML = prop.getProperty("connection." + connectionIndex); //$NON-NLS-1$
		}
	}
	
	/**
	 * When the page became visible the configuration is read from the previous page and the 
	 * checkboxes are created
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible){
			selectedInstallation = ((ListInstallationPage)getPreviousPage()).getSelection();
			//Clear the old elements if someone is doing back and the next
			for(Button button : selectedElements){
				button.dispose();
			}
			selectedElements.clear();
			if (noElementLabel != null) noElementLabel.dispose();
			content.layout();
			
			Properties prop = selectedInstallation.getConfiguration();
	
			if (prop != null){
				createCheckboxes(prop);
			}
			if (selectedElements.isEmpty()){
				noElementLabel = new Label(content, SWT.NONE);
				noElementLabel.setText(Messages.ShowAdaptersPage_noElementLabel);
			}
			content.layout();
			((ScrolledComposite)content.getParent()).setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
	}
	
	/**
	 * Return a list of xml definitions of the selected data adapters 
	 * 
	 * @return a not null list of the xml definition of iReport data adapters
	 */
	public List<?> getSelectedAdapter(){
		List<Object> result = new ArrayList<Object>();
		for(Button element : selectedElements){
			if (element.getSelection()) result.add(element.getData());
		}
		return result;
	}
	
	
	@Override
	public void createControl(Composite parent) {	
		
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(GridLayoutFactory.fillDefaults().create());

		
		Label titleLabel = new Label(mainComposite, SWT.NONE);
		titleLabel.setText(Messages.ShowAdaptersPage_label);
		ScrolledComposite scrollComp = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrollComp.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).create());
		scrollComp.setLayout(new GridLayout(1,false));
		scrollComp.setExpandHorizontal(true);
		scrollComp.setExpandVertical(true);
		content = new Composite(scrollComp, SWT.NONE);
		scrollComp.setContent(content);
		content.setLayout(new GridLayout(1,false));
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		setControl(mainComposite);
	}

	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_IMPORT_SELECT_ADAPTERS;
	}

}
