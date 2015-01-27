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
package com.jaspersoft.studio.editor.defaults;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.templates.StudioTemplateManager;
import com.jaspersoft.studio.templates.engine.DefaultTemplateEngine;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.NewFileCreationWizard;
import com.jaspersoft.templates.ReportBundle;
import com.jaspersoft.templates.TemplateBundle;
import com.jaspersoft.templates.TemplateEngine;

/**
 * Wizard to create a new template set, it require only the location and 
 * the name of the template set
 * 
 * @author Orlandin Marco
 * 
 */
public class DefaultNewWizard extends JSSWizard implements INewWizard {

	/**
	 * Set to select the template set location
	 */
	private NewFileCreationWizard step1;
	
	/**
	 * The generated resource at the end of the wizard
	 */
	private IFile reportFile;

	/**
	 * Constructor for DefaultNewWizard.
	 */
	public DefaultNewWizard() {
		super();
		setWindowTitle(Messages.DefaultNewWizard_title);
		setNeedsProgressMonitor(true);

		// Attention! This operation should always be performed by
		// the wizard caller, since we are forcing here a new config.
		JasperReportsConfiguration jrConfig = JasperReportsConfiguration.getDefaultJRConfig();
		jrConfig.setJasperDesign(new JasperDesign());

		setConfig(jrConfig);
	}

	/**
	 * Adding the page to the wizard.
	 */
	@Override
	public void addPages() {
		step1 = new NewFileCreationWizard("newFilePage1", StructuredSelection.EMPTY);//$NON-NLS-1$
		step1.setBaseName("template_set.jrxml"); //$NON-NLS-1$
		addPage(step1);

	}

	/**
	 * This method is called when 'Finish' button is pressed in the wizard. We will create an operation and run it using
	 * wizard as execution context.
	 */
	@Override
	public boolean performFinish() {

		final String containerName = step1.getContainerFullPath().toPortableString();
		final String fileName = step1.getFileName();

		try {
			getContainer().run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.ReportNewWizard_2, IProgressMonitor.UNKNOWN);
					try {
						doFinish(containerName, fileName, monitor);
					} catch (Exception e) {
						UIUtils.showError(e);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
		} catch (InterruptedException e) {
			UIUtils.showError(e.getCause());
		}
		return super.performFinish();
	}

	/**
	 * The worker method. It will find the container, create the file if missing or just replace its contents, and open
	 * the editor on the newly created file.
	 */
	private void doFinish(String containerName, String fileName, IProgressMonitor monitor) throws CoreException {
		monitor.beginTask(Messages.ReportNewWizard_3 + fileName, 2);

		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IResource resource = root.findMember(new Path(containerName));
		if (!resource.exists() || !(resource instanceof IContainer)) {
			throwCoreException(String.format(Messages.ReportNewWizard_4, containerName));
		}

		Map<String, Object> templateSettings = new HashMap<String, Object>();
		//Uses the empty template bundle
		TemplateBundle templateBundle =  StudioTemplateManager.getInstance().getTemplateBundles().get(0);


		templateSettings.put(DefaultTemplateEngine.DATASET, null);
		templateSettings.put(DefaultTemplateEngine.ORDER_GROUP, false);

		TemplateEngine templateEngine = templateBundle.getTemplateEngine();
		ByteArrayInputStream stream = null;
		try {
			ReportBundle reportBundle = templateEngine.generateReportBundle(templateBundle, templateSettings, getConfig());
			JasperDesign jd = reportBundle.getJasperDesign();
			jd.setColumnFooter(null);
			jd.setColumnHeader(null);
			jd.setPageFooter(null);
			jd.setPageHeader(null);
			jd.setSummary(null);
			jd.setBackground(null);
			jd.setLeftMargin(0);
			jd.setRightMargin(0);
			jd.setTopMargin(0);
			jd.setBottomMargin(0);
			JRDesignStaticText helpText = new JRDesignStaticText();
			helpText.setText(Messages.DefaultNewWizard_defaultsHint);

			JRDesignBand title = (JRDesignBand)jd.getTitle();
			title.addElement(helpText);
			title.setHeight(150);
			helpText.setWidth(jd.getPageWidth() - 10);
			helpText.setHeight(title.getHeight()-10);
			helpText.setX(5);
			helpText.setY(5);
			
			
			((JRDesignBand)jd.getDetailSection().getBands()[0]).setHeight(jd.getPageHeight()-title.getHeight());
			
			
			
			// Store the report bundle on file system
			IContainer container = (IContainer) resource;
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					reportFile = step1.createNewFile();// container.getFile(new Path(fileName));
				}
			});

			String repname = reportFile.getName();
			int lindx = repname.lastIndexOf("."); //$NON-NLS-1$
			if (lindx > 0 && lindx < repname.length() - 1)
				repname = repname.substring(0, lindx);

			reportBundle.getJasperDesign().setName(repname);

			// Save the all the files...
			String contents = JRXmlWriterHelper.writeReport(getConfig(), reportBundle.getJasperDesign(), reportFile, false);
			stream = new ByteArrayInputStream(contents.getBytes());

			if (reportFile.exists()) {
				reportFile.setContents(stream, true, true, monitor);
			} else {
				reportFile.create(stream, true, monitor);
			}
			FileUtils.closeStream(stream);
			saveReportBundleResources(monitor, reportBundle, container);

			monitor.setTaskName(Messages.ReportNewWizard_5);
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
					try {
						IDE.openEditor(page, reportFile, true);
					} catch (PartInitException e) {
						e.printStackTrace();
					}
				}
			});
		} catch (Exception e) {
			UIUtils.showError(e);
		} finally {
			FileUtils.closeStream(stream);
		}
	}

	/**
	 * Return the generated resource at the end of the wizard
	 * 
	 * @return the generated resource or null if the wizard was aborded
	 */
	public IFile getReportFile() {
		return reportFile;
	}
	
	/**
	 * Store all the resources provided by the report bundle in the same folder as the new report.
	 * 
	 * @param monitor
	 * @param reportBundle
	 * @param container
	 */
	private void saveReportBundleResources(final IProgressMonitor monitor, ReportBundle reportBundle, IContainer container) {
		monitor.subTask(Messages.ReportNewWizard_6);

		List<String> resourceNames = reportBundle.getResourceNames();

		for (String resourceName : resourceNames) {
			IFile resourceFile = container.getFile(new Path(resourceName));
			InputStream is = null;
			try {
				if (!resourceFile.exists()) {
					is = reportBundle.getResource(resourceName);
					if (is != null) {
						resourceFile.create(is, true, monitor);
					}
				}
			} catch (Exception e) {
				UIUtils.showError(e);
			} finally {
				FileUtils.closeStream(is);
			}
		}
		monitor.done();
	}

	private void throwCoreException(String message) throws CoreException {
		IStatus status = new Status(IStatus.ERROR, "com.jaspersoft.studio", IStatus.OK, message, null); //$NON-NLS-1$
		throw new CoreException(status);
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	public void init(IWorkbench workbench, IStructuredSelection selection) {

	}

}
