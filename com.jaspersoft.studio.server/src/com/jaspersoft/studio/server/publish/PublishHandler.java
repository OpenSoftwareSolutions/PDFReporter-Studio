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
package com.jaspersoft.studio.server.publish;

import java.net.URI;
import java.util.zip.ZipFile;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.internal.core.CompilationUnit;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.handlers.HandlerUtil;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.server.publish.action.JrxmlPublishAction;
import com.jaspersoft.studio.server.publish.wizard.PublishFile2ServerWizard;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class PublishHandler extends AbstractHandler {
	private static IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IFile file = null;
		JasperReportsConfiguration jContext = null;
		ISelection sel = HandlerUtil.getCurrentSelection(event);
		if (sel instanceof StructuredSelection) {
			Object obj = ((StructuredSelection) sel).getFirstElement();
			if (obj instanceof IFile)
				file = (IFile) obj;
			else if (obj instanceof JarPackageFragmentRoot) {
				try {
					ZipFile zf = ((JarPackageFragmentRoot) obj).getJar();
					if (zf != null)
						file = getFileFromURI(new URI(zf.getName()));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (obj instanceof CompilationUnit)
				file = getFileFromURI(((CompilationUnit) obj).getPath().toFile().toURI());
		}
		if (file == null) {
			IEditorInput ei = com.jaspersoft.studio.utils.compatibility.HandlerUtil.getActiveEditorInput(event);
			if (ei instanceof IFileEditorInput) {
				file = ((IFileEditorInput) ei).getFile();
				String ext = file.getFileExtension();
				if (ext.equals(FileExtension.JRXML) || ext.equals(FileExtension.JASPER)) {
					IEditorPart ep = HandlerUtil.getActiveEditor(event);
					if (ep instanceof JrxmlEditor)
						try {
							jContext = ((JrxmlEditor) ep).getJrContext(file);
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			}
		}
		if (file == null)
			UIUtils.showInformation("Please select a file to publish to JasperReports Server");
		String ext = file.getFileExtension();
		if (ext.equals(FileExtension.JRXML) || ext.equals(FileExtension.JASPER)) {
			if (jContext == null) {
				jContext = JasperReportsConfiguration.getDefaultJRConfig(file);
				try {
					JasperDesign jd = new JRXmlLoader(jContext, JasperReportsConfiguration.getJRXMLDigester()).loadXML(new InputSource(file.getContents()));
					jContext.setJasperDesign(jd);
				} catch (Exception e) {
					e.printStackTrace();
					jContext.dispose();
					jContext = null;
				}
			}
			if (jContext != null) {
				JrxmlPublishAction publishAction = new JrxmlPublishAction(1, null);
				publishAction.setJrConfig(jContext);
				publishAction.run();
				return null;
			}
		}
		PublishFile2ServerWizard wizard = new PublishFile2ServerWizard(file, 1);
		WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
		if (dialog.open() == Dialog.OK) {

		}
		return null;
	}

	private IFile getFileFromURI(URI uri) {
		IFile[] res = root.findFilesForLocationURI(uri);
		if (res != null && res.length > 0)
			return res[0];
		return null;
	}
}
