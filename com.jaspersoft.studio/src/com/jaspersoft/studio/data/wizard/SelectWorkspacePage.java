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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.data.adapter.JSSDescriptor;
import com.jaspersoft.studio.messages.Messages;

/**
 * Dialog page that allow to choose a workspace folder from where the configuration
 * of Jaspersoft Studio will be read
 * 
 * @author Orlandin Marco
 *
 */
public class SelectWorkspacePage extends ListInstallationPage {

	/**
	 * Filter to search a .metadata folder inside another folder
	 */
	private static FilenameFilter metaDataFilter = new FilenameFilter() {
		
		@Override
		public boolean accept(File dir, String name) {
			if (name.equals(".metadata")) return true; //$NON-NLS-1$
			return false;
		}
	};
	
	public SelectWorkspacePage() {
		super();
		setTitle(Messages.SelectWorkspacePage_title);
		setDescription(Messages.SelectWorkspacePage_description);
		setPageComplete(false);
	}
	

	/**
	 * This dialog dosen't provide a list of location automatically discoverd, so it return a void list
	 */
	protected List<IReportDescriptor> getFoundedConfiguration(){
		return new ArrayList<IReportDescriptor>();
	}

	
	/**
	 * Return a file dialog used to open the .metadata folder
	 * 
	 */
	protected String getPath(){
		DirectoryDialog  fd = new DirectoryDialog(UIUtils.getShell(), SWT.OPEN);
    fd.setText(Messages.SelectWorkspacePage_openTitle);
    fd.setFilterPath(".metadata"); //$NON-NLS-1$
    return fd.open();
	}
	
	/**
	 * Return a file descriptor for the provided path
	 * 
	 * @param path the path to the folder called .medatada
	 * @return a JSSDescriptor for the configuration folder in the path
	 */
	protected IReportDescriptor getDescriptor(String path){
		File newFile = new File(path);
		if (newFile.exists()){
			if (newFile.getName().equals(".metadata")) return new JSSDescriptor(newFile); //$NON-NLS-1$
			else {
				File[] children = newFile.listFiles(metaDataFilter);
				if (children.length>0) return  new JSSDescriptor(children[0]);
			}
		}
		return null;
	}
	
	/**
	 * Return the configuration container of the JSS in the workspace actually selected
	 * 
	 * @return a not null configuration of iReport
	 */
	public IReportDescriptor getSelection(){
		return (IReportDescriptor)customRadio.getData();
	}

	@Override
	protected String getContextName() {
		return null;
	}

}
