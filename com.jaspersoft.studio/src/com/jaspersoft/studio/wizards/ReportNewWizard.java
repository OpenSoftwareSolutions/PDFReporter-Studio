/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.wizards;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.gef.EditPart;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.dataset.wizard.WizardDataSourcePage;
import com.jaspersoft.studio.property.dataset.wizard.WizardFieldsGroupByPage;
import com.jaspersoft.studio.property.dataset.wizard.WizardFieldsPage;
import com.jaspersoft.studio.templates.engine.DefaultTemplateEngine;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.templates.ReportBundle;
import com.jaspersoft.templates.TemplateBundle;
import com.jaspersoft.templates.TemplateEngine;

/**
 * Basic wizard to create a new report.
 * 
 * @author gtoffoli
 * 
 */
public class ReportNewWizard extends JSSWizard implements INewWizard {

	public static final String WIZARD_ID = "com.jaspersoft.studio.wizards.ReportNewWizard";
	public static final String NEW_REPORT_JRXML = "report.jrxml";

	private ReportTemplatesWizardPage step0;
	private NewFileCreationWizard step1;
	private WizardDataSourcePage step2;
	private WizardFieldsPage step3;
	private WizardFieldsGroupByPage step4;

	private CongratulationsWizardPage congratulationsStep;

	private boolean showCongratulationsStep = true;

	private ISelection selection;

	/**
	 * Constructor for ReportNewWizard.
	 */
	public ReportNewWizard() {
		super();
		setWindowTitle(Messages.ReportNewWizard_title);
		setNeedsProgressMonitor(true);

		// Attention! This operation should always be performed by
		// the wizard caller, since we are forcing here a new config.
		JasperReportsConfiguration jrConfig = JasperReportsConfiguration.getDefaultJRConfig();
		jrConfig.setJasperDesign(new JasperDesign());

		setConfig(jrConfig);
	}

	public ReportNewWizard(IWizard parentWizard, IWizardPage fallbackPage) {
		super(parentWizard, fallbackPage);
		setWindowTitle(Messages.ReportNewWizard_title);
		setNeedsProgressMonitor(true);
		showCongratulationsStep = false;
	}

	/**
	 * Adding the page to the wizard.
	 */

	@Override
	public void addPages() {
		step0 = new ReportTemplatesWizardPage();
		addPage(step0);

		step1 = new NewFileCreationWizard("newFilePage1", (IStructuredSelection) selection);//$NON-NLS-1$
		addPage(step1);

		step2 = new WizardDataSourcePage();
		addPage(step2);

		step3 = new WizardFieldsPage();
		addPage(step3);

		step4 = new WizardFieldsGroupByPage();
		addPage(step4);

		if (showCongratulationsStep) {
			congratulationsStep = new CongratulationsWizardPage(Messages.CongratulationsWizardPage_title,
					Messages.CongratulationsWizardPage_titleMessage, Messages.CongratulationsWizardPage_label1,
					Messages.CongratulationsWizardPage_label2, Messages.CongratulationsWizardPage_label3);
			addPage(congratulationsStep);
		}
	}

	/**
	 * This method drive the logic to just skip steps.
	 * 
	 * The getNextPage method is generally used to get stuff from a page and configure the next one creating more logic
	 * between pages. This logic has been moved elsewhere: the glue used in JSSWizard is acutally the settings map, which
	 * is passed along the way, since stored inside the wizard. A mechanism to load and store settings allow the pages to
	 * act in a consistent way without having to put any logic here, even if logic can still be added in case of special
	 * behaviours (just like it would be possible to extend the relevant pages).
	 * 
	 * An interesting example is the JSSWizardPage and JSSWizardRunnablePage which provide the base pages to JSS based
	 * wizard. In particular the JSSWizardRunnablePage allows to execute a process on next, which can be used for time
	 * consuming tasks (like read fields).
	 * 
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {

		if (page == step2) {
			if (!getSettings().containsKey(WizardDataSourcePage.DISCOVERED_FIELDS)
					|| ((List<?>) getSettings().get(WizardDataSourcePage.DISCOVERED_FIELDS)).isEmpty()) {
				if (!showCongratulationsStep) {
					// ask for the next page by giving the last page available...
					return super.getNextPage(getPageList().get(getPageList().size() - 1));
				}
				return congratulationsStep;
			}
		}
		return super.getNextPage(page);
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

		TemplateBundle templateBundle = step0.getTemplateBundle();

		JRDesignDataset dataset = WizardUtils.createDataset(getConfig(), true, getSettings());

		templateSettings.put(DefaultTemplateEngine.DATASET, dataset);

		if (getSettings().containsKey(WizardDataSourcePage.DATASET_FIELDS)) {
			templateSettings.put(DefaultTemplateEngine.FIELDS, getSettings().get(WizardDataSourcePage.DATASET_FIELDS));
		}

		if (getSettings().containsKey(WizardDataSourcePage.GROUP_FIELDS)) {
			templateSettings.put(DefaultTemplateEngine.GROUP_FIELDS, getSettings().get(WizardDataSourcePage.GROUP_FIELDS));
		}

		if (getSettings().containsKey(WizardDataSourcePage.ORDER_GROUP)) {
			templateSettings.put(DefaultTemplateEngine.ORDER_GROUP, getSettings().get(WizardDataSourcePage.ORDER_GROUP));
		}

		// If i'm generating a new report for a subreport i add also to the new report parameters the ones defined for the
		// sub report
		if (getSettings().containsKey(WizardDataSourcePage.EXTRA_PARAMETERS)) {
			templateSettings.put(DefaultTemplateEngine.OTHER_PARAMETERS,
					getSettings().get(WizardDataSourcePage.EXTRA_PARAMETERS));
		}

		TemplateEngine templateEngine = templateBundle.getTemplateEngine();
		ByteArrayInputStream stream = null;
		try {
			ReportBundle reportBundle = templateEngine.generateReportBundle(templateBundle, templateSettings, getConfig());

			// Save the data adapter used...
			if (step2.getDataAdapter() != null) {
				Object props = getSettings().get(WizardDataSourcePage.DATASET_PROPERTIES);
				JRPropertiesMap pmap = new JRPropertiesMap();
				if (props != null && props instanceof JRPropertiesMap) {
					pmap = (JRPropertiesMap) props;
				}
				templateEngine.setReportDataAdapter(reportBundle, step2.getDataAdapter(), pmap);

			}

			// Store the report bundle on file system
			IContainer container = (IContainer) resource;
			UIUtils.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					reportFile = step1.createNewFile();// container.getFile(new Path(fileName));
				}
			});

			String repname = reportFile.getName();
			int lindx = repname.lastIndexOf(".");
			if (lindx > 0 && lindx < repname.length() - 1)
				repname = repname.substring(0, lindx);

			reportBundle.getJasperDesign().setName(repname);

			// Save the all the files...
			String contents = JRXmlWriterHelper.writeReport(getConfig(), reportBundle.getJasperDesign(), reportFile, false);
			stream = new ByteArrayInputStream(contents.getBytes());
			try {
				if (reportFile.exists()) {
					reportFile.setContents(stream, true, true, monitor);
				} else {
					reportFile.create(stream, true, monitor);
				}
			} finally {
				FileUtils.closeStream(stream);
			}
			saveReportBundleResources(monitor, reportBundle, container);

			monitor.setTaskName(Messages.ReportNewWizard_5);
			UIUtils.getDisplay().asyncExec(new Runnable() {
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

	private IFile reportFile;

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
		if (selection instanceof StructuredSelection) {
			if (selection.getFirstElement() instanceof IProject || selection.getFirstElement() instanceof IFile
					|| selection.getFirstElement() instanceof IFolder || selection.getFirstElement() instanceof IPackageFragment) {
				this.selection = selection;
				return;
			}
			for (Object obj : selection.toList()) {
				if (obj instanceof EditPart) {
					IEditorInput ein = SelectionHelper.getActiveJRXMLEditor().getEditorInput();
					if (ein instanceof FileEditorInput) {
						this.selection = new TreeSelection(new TreePath(new Object[] { ((FileEditorInput) ein).getFile() }));
						return;
					}
				}
			}
			IProgressMonitor progressMonitor = new NullProgressMonitor();
			IProject[] prjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
			for (IProject p : prjs) {
				try {
					if (ProjectUtil.isOpen(p) && p.getNature(JavaCore.NATURE_ID) != null) {
						p.open(progressMonitor);
						this.selection = new TreeSelection(new TreePath(new Object[] { p.getFile(NEW_REPORT_JRXML) }));
						return;
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			for (IProject p : prjs) {
				try {
					if (ProjectUtil.isOpen(p)) {
						p.open(progressMonitor);
						this.selection = new TreeSelection(new TreePath(new Object[] { p.getFile(NEW_REPORT_JRXML) }));
						return;
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
		this.selection = selection;
	}

	/**
	 * We don't want to finish the wizard at "any" time. This methods allows to decide when we can and when we cannot
	 * finish...
	 * 
	 */
	@Override
	public boolean canFinish() {
		// if (getContainer().getCurrentPage() != congratulationsStep)
		// return false;

		return super.canFinish();
	}

}
