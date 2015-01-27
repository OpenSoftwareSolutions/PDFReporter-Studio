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
package net.sf.jasperreports.eclipse.builder.jdt;

import java.net.URI;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.IURIEditorInput;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.views.properties.PropertySheet;
import org.eclipse.ui.views.properties.PropertyShowInContext;

/**
 * Utility class with JDT related methods.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class JDTUtils {
	
	private JDTUtils(){
		// prevent instantiation...
	}

	/**
	 * Retrieves the current {@link IProject} instance based on the 
	 * currently opened editor. 
	 */
	public static IProject getCurrentProjectForOpenEditor() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null && activeWorkbenchWindow.getActivePage() != null) {
			IEditorPart p = activeWorkbenchWindow.getActivePage().getActiveEditor();
			if(p == null) {
				IWorkbenchPart activePart = activeWorkbenchWindow.getActivePage().getActivePart();
				if(activePart instanceof PropertySheet) {
					ShowInContext showInContext = ((PropertySheet)activePart).getShowInContext();
					if(showInContext instanceof PropertyShowInContext) {
						IWorkbenchPart part = ((PropertyShowInContext)showInContext).getPart();
						if(part instanceof IEditorPart) {
							p = (IEditorPart) part;
						}
						else {
							JasperReportsPlugin.getDefault().logWarning("Unable to retrieve the current project for the open editor.");
							return null;
						}
					}
				}
			}
			IEditorInput editorInput = p.getEditorInput();
			IFile file = getFile(editorInput);
			if(file!=null){
				return file.getProject();
			}
		}
		return null;
	}

    /**
     * @return the IFile corresponding to the given input, or null if none
     */
    public static IFile getFile(IEditorInput editorInput) {
        IFile file= null;

        if (editorInput instanceof IFileEditorInput) {
            IFileEditorInput fileEditorInput= (IFileEditorInput) editorInput;
            file= fileEditorInput.getFile();
        } else if (editorInput instanceof IPathEditorInput) {
            IPathEditorInput pathInput= (IPathEditorInput) editorInput;
            IWorkspaceRoot wsRoot= ResourcesPlugin.getWorkspace().getRoot();
            if (wsRoot.getLocation().isPrefixOf(pathInput.getPath())) {
                file= ResourcesPlugin.getWorkspace().getRoot().getFile(pathInput.getPath());
            } else {
                // Can't get an IFile for an arbitrary file on the file system; return null
            }
        } else if (editorInput instanceof IStorageEditorInput) {
            file= null; // Can't get an IFile for an arbitrary IStorageEditorInput
        } else if (editorInput instanceof IURIEditorInput) {
            IURIEditorInput uriEditorInput= (IURIEditorInput) editorInput;
            IWorkspaceRoot wsRoot= ResourcesPlugin.getWorkspace().getRoot();
            URI uri= uriEditorInput.getURI();
            String path= uri.getPath();
            // Bug 526: uri.getHost() can be null for a local file URL
            if (uri.getScheme().equals("file") && (uri.getHost() == null || uri.getHost().equals("localhost")) && !path.startsWith(wsRoot.getLocation().toOSString())) {
                file= wsRoot.getFile(new Path(path));
            }
        }
        return file;
    }

    /**
     * Formats the specified compilation unit.
     * 
     * @param unit the compilation unit to format
     * @param monitor the monitor for the operation
     * @throws JavaModelException
     */
	public static void formatUnitSourceCode(ICompilationUnit unit,
			IProgressMonitor monitor) throws JavaModelException {
		CodeFormatter formatter = ToolFactory.createCodeFormatter(null);
		ISourceRange range = unit.getSourceRange();
		TextEdit formatEdit = formatter.format(
				CodeFormatter.K_COMPILATION_UNIT, unit.getSource(),
				range.getOffset(), range.getLength(), 0, null);
		if (formatEdit != null && formatEdit.hasChildren()) {
			unit.applyTextEdit(formatEdit, monitor);
		} else {
			monitor.done();
		}
	}
	
	/**
	 * Checks if the object is of the specified class type or 
	 * has an adapter for it.
	 *  
	 * @param adaptable the object to be adapted
	 * @param clazz the class type
	 * @return <code>true</code> if it can be adapted, <code>false</code> otherwise
	 */
	public static boolean isOrCanAdaptTo(IAdaptable adaptable, Class<?> clazz) {
		Assert.isNotNull(clazz);
		if(clazz.isInstance(adaptable)) {
			return true;
		}
		else {
			return (adaptable!=null && adaptable.getAdapter(clazz)!=null);
		}
	}
	
	/**
	 * Gets the adapted object for the specified class type. 
	 *  	
	 * @param adaptable the object to be adapted
	 * @param clazz the class type
	 * @return the adapted object, <code>null</code> if no corresponding adapter exists
	 */
	public static <T> T getAdaptedObject(IAdaptable adaptable, Class<T> clazz) {
		Assert.isNotNull(clazz);
		if(clazz.isInstance(adaptable)) {
			return (T) adaptable;
		}
		else {
			T adaptedObj = null;
			if (adaptable!=null) {
				adaptedObj=(T) adaptable.getAdapter(clazz);
			}
			return adaptedObj;
		}
	}
}
