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
package com.jaspersoft.studio.outline.action.compile;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Iterator;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;

import com.jaspersoft.studio.editor.action.CompileAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.templates.JrxmlTemplateBundle;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Action to compile one or more jrxml file selected in the outline, without 
 * opening them
 * 
 * @author Orlandin Marco
 *
 */
public class CompileHandler implements IHandler {

	/**
	 * Check if the element is a jrxml file, in this case it is added to the founded
	 * reports (if it wasen't added before). Otherwise if it is a folder or an opened project
	 * then the search is done recursively inside it.
	 * 
	 * @param element a resource
	 * @param foundReports a Map with the JasperReports configuration of the founded reports. The key
	 * is the path to the report jrxml itself and it is used to avoid to add more then on time the same 
	 * reports (for example because the jrxml and it's parent folder are both in the selection set)
	 */
	private void evaluateChild(HashMap<String, JasperReportsConfiguration> foundReports, Object element){
		if (element instanceof IFolder){
			IFolder folder = (IFolder)element;
			try {
				for(IResource resource : folder.members()){
					evaluateChild(foundReports, resource);
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else if (element instanceof IFile){
			IFile file = (IFile)element;
			String extension = file.getFileExtension();
			if (extension != null && extension.toLowerCase().equals("jrxml")){ //$NON-NLS-1$
				String key = file.getRawLocation().toPortableString();
				if (!foundReports.containsKey(key)){
					JasperReportsConfiguration jConfig = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), file);
					foundReports.put(key, jConfig);
				}
			}
		} else if (element instanceof IProject){
			IProject project = (IProject)element;
			if (project.isOpen()){
				try {
					for(IResource resource : project.members()){
						evaluateChild(foundReports, resource);
					}
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Return an hasmhap of file to compile where the key is the absolute path to the file and the
	 * value is the jasper configuration of the file to compile
	 * 
	 * @param event
	 * @return a not null map of report that must be compiled. The map has no duplicates so the 
	 * reports must be all compiled
	 */
	private HashMap<String, JasperReportsConfiguration> getOperationSet(ExecutionEvent event){
		HashMap<String, JasperReportsConfiguration> result = new HashMap<String, JasperReportsConfiguration>();
	  ISelection selection = HandlerUtil.getActiveWorkbenchWindow(event).getActivePage().getSelection();
		if (selection instanceof IStructuredSelection) {
			for (Iterator<?> it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();
				evaluateChild(result, element);
			}
		}
		return result;
	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		final HashMap<String, JasperReportsConfiguration> reportsToCompile = getOperationSet(event);
		ProgressMonitorDialog dialog = new ProgressMonitorDialog(UIUtils.getShell());

		IRunnableWithProgress compileProcess = new IRunnableWithProgress() {
			
			@Override
			public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				monitor.beginTask(Messages.CompileHandler_compilingStart, reportsToCompile.size());
				for (JasperReportsConfiguration jrConfig : reportsToCompile.values()) {
					IFile mfile = (IFile) jrConfig.get(FileUtils.KEY_FILE);
					monitor.setTaskName(Messages.CompileHandler_compilingReport + mfile.getName());
					try{
						JrxmlTemplateBundle bundle = new JrxmlTemplateBundle(mfile.getLocationURI().toURL(), true, jrConfig);
						jrConfig.setJasperDesign(bundle.getJasperDesign());
						CompileAction.doRun(jrConfig, monitor, true);
					} catch (Exception ex){
						ex.printStackTrace();
					}
					monitor.worked(1);
					if (monitor.isCanceled()) break;
				}
				monitor.done();
			}
		};
		try {
			dialog.run(true, true, compileProcess);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
	}
	
	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
	}

	@Override
	public void dispose() {
	}
	

}
