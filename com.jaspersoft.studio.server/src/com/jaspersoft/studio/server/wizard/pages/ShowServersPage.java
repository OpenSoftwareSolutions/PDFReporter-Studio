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
package com.jaspersoft.studio.server.wizard.pages;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.data.wizard.ListInstallationPage;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.utils.Encrypter;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

/**
 * Show a list of checkboxes where every box is a server connection found inside a server 
 * configuration file of iReport
 * 
 * @author Orlandin Marco
 *
 */
public class ShowServersPage extends JSSHelpWizardPage {

	/**
	 * ID of the contextual help
	 */
	private static final String CONTEX_HELP_ID = "com.jaspersoft.studio.doc.wizard_import_select_serverconnection"; //$NON-NLS-1$
	
	/**
	 * Configuration of iReport where the servers are searched
	 */
	private IReportDescriptor selectedInstallation;
	
	/**
	 * List of all the checkboxes
	 */
	private List<Button> selectedElements;
	
	/**
	 * composite where the checkboxes are placed
	 */
	private Composite content;
	
	/**
	 * Label shown where there aren't element that could be imported
	 */
	private Label noElementLabel = null;
	
	public ShowServersPage() {
		super("IReportDatasourceList"); //$NON-NLS-1$
		selectedElements = new ArrayList<Button>();
		setTitle(Messages.ShowServersPage_title);
		setDescription(Messages.ShowServersPage_description);
	}
	
	protected List<ServerProfile> createCheckBoxData(Properties prop){
		List<ServerProfile> result = new ArrayList<ServerProfile>();
		Integer connectionIndex = 0;
		Encrypter enc = new Encrypter("54fj245vn3vfdsmce4mg0jvs"); //$NON-NLS-1$
		String connectionString = prop.getProperty("server." + connectionIndex + ".url"); //$NON-NLS-1$ //$NON-NLS-2$
		while(connectionString != null){
			ServerProfile srv = new ServerProfile();
			
			if (connectionString.endsWith("/services/repository")){ //$NON-NLS-1$
				connectionString = connectionString.substring(0, connectionString.lastIndexOf("services/repository")); //$NON-NLS-1$
			}
			srv.setUrl(connectionString); 

			String name = prop.getProperty("server." + connectionIndex + ".name"); //$NON-NLS-1$ //$NON-NLS-2$
			srv.setName(name);
			String username = prop.getProperty("server." + connectionIndex + ".username"); //$NON-NLS-1$ //$NON-NLS-2$
			srv.setUser(username); 
			srv.setSupportsDateRanges(true);
			
			String password = prop.getProperty("server." + connectionIndex + ".password.enc"); //$NON-NLS-1$ //$NON-NLS-2$
			//getBrandingProperties().getProperty("irplugin.encrypt.passwords.key", "54fj245vn3vfdsmce4mg0jvs")
			password = enc.decrypt(password);
			srv.setPass(password);
			
			result.add(srv);
			
			connectionIndex++;
			connectionString = prop.getProperty("server." + connectionIndex + ".url"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return result;
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
			
			Properties prop = selectedInstallation.getServerConnection();
	
			if (prop != null){
				List<ServerProfile> checkBoxData = createCheckBoxData(prop);
				for(ServerProfile srv : checkBoxData)
				{
					Button checkButton = new Button(content, SWT.CHECK);
					checkButton.setText(srv.getName() + " ("+srv.getUrl()+")"); //$NON-NLS-1$ //$NON-NLS-2$
					checkButton.setData(srv);
					selectedElements.add(checkButton);
				}
			}
			if (selectedElements.isEmpty()){
				noElementLabel = new Label(content, SWT.NONE);
				noElementLabel.setText(Messages.ShowServersPage_noElementsLabel);
			}
			content.layout();
			((ScrolledComposite)content.getParent()).setMinSize(content.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}
	}
	
	/**
	 * Return a list of the selected servers connection
	 * 
	 * @return a not null list of ServerProfile, every one represent an element to import
	 */
	public List<ServerProfile> getSelectedServers(){
		List<ServerProfile> result = new ArrayList<ServerProfile>();
		for(Button element : selectedElements){
			if (element.getSelection()) result.add((ServerProfile)element.getData());
		}
		return result;
	}
	
	
	@Override
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(GridLayoutFactory.fillDefaults().create());
		
		Label titleLabel = new Label(mainComposite, SWT.NONE);
		titleLabel.setText(Messages.ShowServersPage_label);
		
		ScrolledComposite scrollComp = new ScrolledComposite(mainComposite, SWT.V_SCROLL | SWT.H_SCROLL);
		scrollComp.setLayoutData(GridDataFactory.fillDefaults().grab(true, true).hint(SWT.DEFAULT, 200).create());
		scrollComp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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
		return CONTEX_HELP_ID;
	}

}
