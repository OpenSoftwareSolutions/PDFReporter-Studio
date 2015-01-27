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
package com.jaspersoft.studio.editor.preview;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.part.EditorPart;

import com.jaspersoft.studio.editor.DeltaVisitor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class ABasicEditor extends EditorPart {
	protected boolean listenResource;

	public ABasicEditor(boolean listenResource) {
		this.listenResource = listenResource;
		if (listenResource) {
			partListener = new ResourcePartListener();
			resourceListener = new ResourceTracker();
		}
	}

	class ResourceTracker implements IResourceChangeListener {
		public void resourceChanged(final IResourceChangeEvent event) {
			switch (event.getType()) {
			case IResourceChangeEvent.PRE_DELETE:
				break;
			case IResourceChangeEvent.POST_CHANGE:
				try {
					DeltaVisitor visitor = new DeltaVisitor(ABasicEditor.this);
					event.getDelta().accept(visitor);
				} catch (CoreException e) {
					UIUtils.showError(e);
				}
				break;
			case IResourceChangeEvent.PRE_BUILD:
			case IResourceChangeEvent.POST_BUILD:
				break;
			}
		}
	}

	class ResourcePartListener implements IPartListener {
		// If an open, unsaved file was deleted, query the user to either do a "Save As"
		// or close the editor.
		public void partActivated(IWorkbenchPart part) {
			if (part != ABasicEditor.this)
				return;
			if (!((IFileEditorInput) getEditorInput()).getFile().exists())
				getSite().getPage().closeEditor(ABasicEditor.this, false);
		}

		public void partBroughtToTop(IWorkbenchPart part) {
		}

		public void partClosed(IWorkbenchPart part) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			if (!file.exists()) {
				try {
					file.delete(true, new NullProgressMonitor());
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		}

		public void partDeactivated(IWorkbenchPart part) {
		}

		public void partOpened(IWorkbenchPart part) {
		}
	};

	private IPartListener partListener;
	private ResourceTracker resourceListener;

	@Override
	public void dispose() {
		if (partListener != null)
			getSite().getWorkbenchWindow().getPartService().removePartListener(partListener);
		partListener = null;
		if (resourceListener != null)
			((IFileEditorInput) getEditorInput()).getFile().getWorkspace().removeResourceChangeListener(resourceListener);
		if (jrContext != null)
			jrContext.dispose();
		super.dispose();
	}

	@Override
	protected void setInput(IEditorInput input) {
		// The workspace never changes for an editor. So, removing and re-adding the
		// resourceListener is not necessary. But it is being done here for the sake
		// of proper implementation. Plus, the resourceListener needs to be added
		// to the workspace the first time around.
		if (resourceListener != null && getEditorInput() != null) {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			file.getWorkspace().removeResourceChangeListener(resourceListener);
		}

		super.setInput(input);

		if (resourceListener != null && getEditorInput() != null) {
			if (getEditorInput() instanceof IFileEditorInput) {
				IFile file = ((IFileEditorInput) getEditorInput()).getFile();
				file.getWorkspace().addResourceChangeListener(resourceListener,
						IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
				setPartName(file.getName());
			} else if (getEditorInput() instanceof FileStoreEditorInput) {
			}
		}
	}

	@Override
	protected void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
		if (partListener != null)
			getSite().getWorkbenchWindow().getPartService().addPartListener(partListener);
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		IFile file = null;
		if (input instanceof FileStoreEditorInput) {
			input = FileUtils.checkAndConvertEditorInput(input, new NullProgressMonitor());
			init(site, input);
			return;
		} else if (input instanceof IFileEditorInput) {
			file = ((IFileEditorInput) input).getFile();
		}

		try {
			getJrContext(file);
			setSite(site);
			setPartName(input.getName());
			setInput(input);
		} catch (Exception e) {
			throw new PartInitException(e.getMessage(), e);
		}
	}

	protected JasperReportsConfiguration jrContext;

	protected void getJrContext(IFile file) throws CoreException, JavaModelException {
		if (jrContext == null)
			jrContext = JasperReportsConfiguration.getDefaultJRConfig(file);
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == JasperReportsContext.class)
			return jrContext;
		return super.getAdapter(adapter);
	}

	protected boolean isDirty = false;

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void doSaveAs() {
		isDirty = false;
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		isDirty = false;
	}
}
