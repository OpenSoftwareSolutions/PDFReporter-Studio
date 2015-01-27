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
package com.jaspersoft.studio.wizards;

import java.io.File;
import java.text.MessageFormat;
import java.util.Map;

import net.sf.jasperreports.eclipse.util.FileExtension;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.templates.TemplateBundle;

public class NewFileCreationWizard extends WizardNewFileCreationPage implements ContextData {

	protected IStructuredSelection currentSelection = null;
	
	/**
	 * Base name for the new file
	 */
	private String baseName = ReportNewWizard.NEW_REPORT_JRXML;

	/**
	 * This variable is used to load default file name in case this page is shown for the first time, otherwise the page
	 * is left as it is.
	 */
	// boolean firstLoad = true;

	public NewFileCreationWizard(String pageName, IStructuredSelection selection) {
		super(pageName, selection);

		this.currentSelection = selection;

		setTitle(Messages.ReportNewWizard_0);
		setDescription(Messages.ReportNewWizardPage_description);
		setFileExtension(FileExtension.JRXML);//$NON-NLS-1$
	}

	/**
	 * Set and show the help data
	 */
	@Override
	public void performHelp() {
		PlatformUI.getWorkbench().getHelpSystem().displayHelp(ContextHelpIDs.WIZARD_NEW_FILE);
	}

	/**
	 * Set the help data that should be seen in this step
	 */
	@Override
	public void setHelpData() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), ContextHelpIDs.WIZARD_NEW_FILE);
	}

	@Override
	protected void setControl(Control newControl) {
		super.setControl(newControl);
		newControl.addListener(SWT.Help, new Listener() {
			@Override
			public void handleEvent(Event event) {
				performHelp();
			}
		});
		setHelpData();
	};

	/**
	 * Add an extra check to validate if the directory inside the project exists or not. We don't want to create a new
	 * directory for the user...
	 * 
	 */
	@Override
	public boolean validatePage() {

		boolean valid = super.validatePage();

		if (valid) {
			// We need to check that the selected directory does exist, otherwise we need to set an error...
			IResource r = ResourcesPlugin.getWorkspace().getRoot().findMember(getContainerFullPath());

			if (r == null || !r.exists() || (r.getType() & IResource.FILE) != 0) {
				setMessage("The directory specified does not exist or is not a valid folder", DialogPage.ERROR);
				valid = false;
			}
		}
		return valid;

	}

	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);

		if (visible == true) {
			// check for a better file name...
			loadSettings();
		}

		validatePage();
	}
	
	/**
	 * Set the base name for the file that will be created
	 */
	public void setBaseName(String newBaseName){
		this.baseName = newBaseName;
	}

	/**
	 * This procedure look if a file name has been set already for this dialog page. If not, the dialog page will try to
	 * load default settings...
	 * 
	 * 
	 */
	public void loadSettings() {

		// if (!firstLoad)
		// return;

		// firstLoad = false;
		// If a template has been selected, let's try use its name as file name...
		if (getWizard() != null && getWizard() instanceof JSSWizard) {
			JSSWizard jssw = (JSSWizard) getWizard();
			if (jssw.getSettings() != null && jssw.getSettings().get("template") != null) {
				try {
					TemplateBundle tb = (TemplateBundle) jssw.getSettings().get("template");
					baseName = tb.getLabel();
					// Sanityze the file name...
					baseName = baseName.replace(File.separator, "_");
					baseName = baseName.replace(" ", "_");
				} catch (Exception ex) {

				}
			}
		}

		String filename = baseName;
		if (!filename.endsWith(FileExtension.PointJRXML))
			filename += FileExtension.PointJRXML;

		if (this.currentSelection != null) {
			if (this.currentSelection instanceof TreeSelection) {
				TreeSelection s = (TreeSelection) this.currentSelection;
				if (s.getFirstElement() instanceof IFile) {
					IFile file = (IFile) s.getFirstElement();

					filename = getValidFileName(file.getProject(), file.getProjectRelativePath().removeLastSegments(1)
							.toOSString(), baseName);
				} else if (s.getFirstElement() instanceof IProject) {
					IProject prj = (IProject) s.getFirstElement();
					filename = getValidFileName(prj, Messages.ReportNewWizard_11, baseName);
				}
			}
			setFileName(filename);
		}
	}

	/**
	 * 
	 * Find the first not existing file in the given project path with the name:
	 * 
	 * basename.jrxml
	 * 
	 * If that file exists, the first not exising file named basename_x.jrxml is return.
	 * 
	 * @param prj
	 * @param prjPath
	 * @param basename
	 * @return the first valid file name
	 */
	private String getValidFileName(IProject prj, String prjPath, String basename) {
		if (!basename.endsWith(FileExtension.PointJRXML))
			basename = basename.replaceAll(FileExtension.PointJRXML + "$", "");
		String filename = basename + FileExtension.PointJRXML;
		String pattern = basename + "_{0}" + FileExtension.PointJRXML;

		// Initial name...
		String f = prjPath + File.separator + filename;

		int i = 1;
		while (prj.getFile(f).exists()) {
			filename = MessageFormat.format(pattern, new Object[] { i });
			f = prjPath + File.separator + filename;
			i++;
		}
		return filename;
	}

	/**
	 * We use the setPageComplete to run the code which will store in the settings what has been selected by the user (or
	 * reset what has been stored if complete is false).
	 */
	public void setPageComplete(boolean complete) {
		super.setPageComplete(complete);

		// Store the user selection in the settings...
		storeSettings();

	}

	/**
	 * Store inside the wizard settings the user selection.
	 */
	public void storeSettings() {
		if (getWizard() instanceof JSSWizard && getWizard() != null) {
			Map<String, Object> settings = ((JSSWizard) getWizard()).getSettings();

			if (settings == null)
				return;
			IFile file = null;
			if (isPageComplete()) {
				IPath path = getContainerFullPath();
				String fname = getFileName();

				settings.put(JSSWizard.FILE_PATH, path); // the type is IPath
				settings.put(JSSWizard.FILE_NAME, fname);

				IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
				IResource resource = root.findMember(path);
				file = ((IContainer) resource).getFile(new Path(fname));
			} else {
				settings.remove(JSSWizard.FILE_PATH);
				settings.remove(JSSWizard.FILE_NAME);
			}
			JasperReportsConfiguration jConfig = (JasperReportsConfiguration) settings
					.get(JSSWizard.JASPERREPORTS_CONFIGURATION);
			jConfig.init(file);
		}
	}

}
