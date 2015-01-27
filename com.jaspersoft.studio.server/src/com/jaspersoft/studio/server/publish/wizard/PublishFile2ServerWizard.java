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
package com.jaspersoft.studio.server.publish.wizard;

import java.io.File;
import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.part.FileEditorInput;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.server.publish.wizard.page.FileSelectionPage;
import com.jaspersoft.studio.server.publish.wizard.page.RFileLocationPage;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class PublishFile2ServerWizard extends Wizard implements IExportWizard {
	private JasperReportsConfiguration jrConfig;
	private int startPage = 0;
	private IFile file;
	private FileSelectionPage page0;
	private RFileLocationPage page1;

	public PublishFile2ServerWizard() {
		super();
		setWindowTitle("Publish File To The JasperReports Server");
		setNeedsProgressMonitor(true);
	}

	public PublishFile2ServerWizard(IFile file, int page) {
		this();
		this.file = file;
		this.startPage = page;
	}

	private void init() {
		if (file == null && selection != null && selection instanceof IStructuredSelection) {
			Object obj = ((IStructuredSelection) selection).getFirstElement();
			if (obj instanceof IFile)
				file = (IFile) obj;
		}
		if (jrConfig == null)
			if (file != null)
				jrConfig = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), file);
			else
				jrConfig = JasperReportsConfiguration.getDefaultJRConfig();
	}

	@Override
	public void dispose() {
		jrConfig.dispose();
		super.dispose();
	}

	@Override
	public void addPages() {
		init();
		if (file == null) {
			page0 = new FileSelectionPage(jrConfig);
			addPage(page0);
		}
		page1 = new RFileLocationPage(jrConfig);
		addPage(page1);
	}

	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		page1.refreshFile();
		return super.getNextPage(page);
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
						AFileResource fres = page1.getSelectedNode();
						if (fres != null) {
							String ext = file.getFileExtension().toLowerCase();
							ResourceDescriptor rd = fres.getValue();
							if (ext.equalsIgnoreCase("xml"))
								rd.setWsType(ResourceDescriptor.TYPE_XML_FILE);
							else if (ext.equalsIgnoreCase("jar") || ext.equalsIgnoreCase("zip"))
								rd.setWsType(ResourceDescriptor.TYPE_CLASS_JAR);
							else if (ext.equalsIgnoreCase("jrtx"))
								rd.setWsType(ResourceDescriptor.TYPE_STYLE_TEMPLATE);
							else if (ext.equalsIgnoreCase("css"))
								rd.setWsType(ResourceDescriptor.TYPE_CSS_FILE);
							else if (ext.equalsIgnoreCase("properties"))
								rd.setWsType(ResourceDescriptor.TYPE_RESOURCE_BUNDLE);
							else if (ext.equalsIgnoreCase("ttf") || ext.equalsIgnoreCase("eot") || ext.equalsIgnoreCase("woff"))
								rd.setWsType(ResourceDescriptor.TYPE_FONT);
							else if (ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("gif") || ext.equalsIgnoreCase("jpg") || ext.equalsIgnoreCase("jpeg") || ext.equalsIgnoreCase("bmp")
									|| ext.equalsIgnoreCase("tiff"))
								rd.setWsType(ResourceDescriptor.TYPE_IMAGE);
							else if (fres.getParent() instanceof MReportUnit)
								rd.setWsType(ResourceDescriptor.TYPE_RESOURCE_BUNDLE);

							fres.setFile(new File(file.getRawLocationURI()));
							WSClientHelper.save(monitor, fres);

							PublishUtil.savePath(file, fres);
							INode n = fres.getRoot();
							if (n != null && n instanceof MServerProfile) {
								MServerProfile msp = ServerManager.getServerByUrl(((MServerProfile) n).getValue().getUrl());
								ServerManager.selectIfExists(monitor, msp, fres);
							}
						}
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
			return false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public IWizardPage getStartingPage() {
		return getPages()[Math.min(startPage, getPageCount() - 1)];
	}

	private ISelection selection;

	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		if (selection instanceof StructuredSelection) {
			if (selection.getFirstElement() instanceof IProject || selection.getFirstElement() instanceof IFile || selection.getFirstElement() instanceof IFolder) {
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
		}
		this.selection = selection;
	}
}
