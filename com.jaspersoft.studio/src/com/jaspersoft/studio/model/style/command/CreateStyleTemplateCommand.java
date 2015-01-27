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
package com.jaspersoft.studio.model.style.command;

import java.io.File;

import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignReportTemplate;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import com.jaspersoft.studio.model.style.MStyleTemplate;
import com.jaspersoft.studio.model.style.MStyles;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateStyleTemplateCommand extends Command {

	/** The jr template. */
	private JRDesignReportTemplate jrTemplate;

	/** The jr design. */
	private JasperDesign jrDesign;

	/** The index. */
	private int index;
	
	/**
	 * The configuration of the actual report
	 */
	private JasperReportsConfiguration jConfig;

	/**
	 * Instantiates a new creates the style template command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateStyleTemplateCommand(MStyles destNode, MStyleTemplate srcNode, int index) {
		super();
		this.jrDesign = destNode.getJasperDesign();
		this.jConfig = destNode.getJasperConfiguration();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrTemplate = (JRDesignReportTemplate) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrTemplate != null) {
			if (index < 0 || index > jrDesign.getTemplatesList().size())
				jrDesign.addTemplate(jrTemplate);
			else
				jrDesign.addTemplate(index, jrTemplate);
		}
	}

	private class FilteredHelpDialog extends FilteredResourcesSelectionDialog{

		public FilteredHelpDialog(Shell shell, boolean multi, IContainer container, int typesMask) {
			super(shell, multi, container, typesMask);
		}

		/**
		 * Set the help data that should be seen in this step
		 */
		@Override
		protected void configureShell(Shell shell){
			super.configureShell(shell);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(shell, ContextHelpIDs.WIZARD_STYLE_TEMPLATE_LOAD);
		}
		
	}
	
	/**
	 * This method try to return a relative path for the style from the current opened report. If it isn't
	 * Possible to find a relative path then the absolute one is returned
	 * 
	 * @param styleFile the style file resource
	 * @return and absolute or relative path to the style resource
	 */
	private String getStylePath(IFile styleFile){
		IFile reportFile = (IFile) jConfig.get(FileUtils.KEY_FILE);
		if (reportFile != null){
			if (reportFile.getParent().equals(styleFile.getParent())) return styleFile.getName();
			else if (reportFile.getProject().equals(styleFile.getProject())) return styleFile.getProjectRelativePath().toPortableString();
		}
	 return styleFile.getRawLocation().makeAbsolute().toOSString();
	}
	
	private void createObject() {
		if (jrTemplate == null) {
			FilteredResourcesSelectionDialog fd = new FilteredHelpDialog(Display.getCurrent().getActiveShell(),false, ResourcesPlugin.getWorkspace().getRoot(), IResource.FILE);
			fd.setInitialPattern("*.jrtx");//$NON-NLS-1$
			if (fd.open() == Dialog.OK) {
				IFile file = (IFile) fd.getFirstResult();
				File  fileToBeOpened = file.getRawLocation().makeAbsolute().toFile();
				boolean showErrorMessage = false;
				//Check if the file is a valid template before add it to the model
				if (fileToBeOpened != null && fileToBeOpened.exists() && fileToBeOpened.isFile()) {
					try{
						//Try to load the file to see if it is a valid template
						JRXmlTemplateLoader.load(fileToBeOpened);
						this.jrTemplate = MStyleTemplate.createJRTemplate();
						JRDesignExpression jre = new JRDesignExpression();
						jre.setText("\"" + getStylePath(file) + "\"");//$NON-NLS-1$ //$NON-NLS-2$
						((JRDesignReportTemplate) jrTemplate).setSourceExpression(jre);
					} catch(Exception ex){
						showErrorMessage = true;
					}
				} else {
					showErrorMessage = true;
				}
				if (showErrorMessage){
					MessageDialog.open(MessageDialog.ERROR, Display.getCurrent().getActiveShell(), Messages.UIUtils_ExceptionTitle, Messages.CreateStyleTemplateCommand_loadStyleError, SWT.NONE);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrDesign.removeTemplate(jrTemplate);
	}
}
