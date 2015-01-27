/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JarEntryEditorInput;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.jaspersoft.studio.editor.gef.parts.ReportPageEditPart;
import com.jaspersoft.studio.editor.outline.page.EmptyOutlinePage;
import com.jaspersoft.studio.editor.outline.page.MultiOutlineView;
import com.jaspersoft.studio.editor.report.ReportContainer;
import com.jaspersoft.studio.editor.xml.XMLEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AMultiEditor extends MultiPageEditorPart implements IResourceChangeListener, IMultiEditor {
	protected JasperReportsConfiguration jrContext;

	public AMultiEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
	}

	@Override
	protected void pageChange(int newPageIndex) {
		if (activePage == 0) {
			if (outlinePage != null)
				tmpselection = outlinePage.getSite().getSelectionProvider().getSelection();
			else
				tmpselection = getActiveEditor().getSite().getSelectionProvider().getSelection();
		}
		switch (newPageIndex) {
		case 0:
			if (activePage == 1 && !xmlFresh) {
				xml2model();
			}
			setModel(model);
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					ISelectionProvider sp = null;
					if (outlinePage != null)
						sp = outlinePage.getSite().getSelectionProvider();
					else
						sp = getActiveEditor().getSite().getSelectionProvider();

					sp.setSelection(tmpselection);
				}
			});
			break;
		case 1:
			if (isDirty()) {
				model2xml();
			}
			break;
		}
		super.pageChange(newPageIndex);
		updateContentOutline(getActivePage());
		activePage = newPageIndex;
	}

	private ISelection tmpselection;
	private int activePage = 0;

	@Override
	public void doSave(IProgressMonitor monitor) {
		isRefresh = true;
		String xml = null;
		if (activePage == 0) {
			xml = model2xml();
		} else if (activePage == 1) {
			xml2model();
		}
		doSaveParticipate(monitor);
		xmlEditor.doSave(monitor);

		if (xml != null) {
			try {
				IFile f = getCurrentFile();
				if (f != null)
					f.setContents(new ByteArrayInputStream(xml.getBytes("UTF-8")), IFile.KEEP_HISTORY | IFile.FORCE, monitor);
			} catch (Throwable e) {
				UIUtils.showError(e);
			}
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				isRefresh = false;
				firePropertyChange(ISaveablePart.PROP_DIRTY);
			}
		});
		xmlFresh = true;
	}

	protected abstract void doSaveParticipate(IProgressMonitor monitor);

	protected void xml2model() {
		IDocumentProvider dp = xmlEditor.getDocumentProvider();
		IDocument doc = dp.getDocument(xmlEditor.getEditorInput());

		InputStream in = new ByteArrayInputStream(doc.get().getBytes());
		xml2model(in);
	}

	protected abstract void xml2model(InputStream in);

	protected String model2xml() {
		try {
			if (model != null) {
				String xml = doModel2xml();
				IDocumentProvider dp = xmlEditor.getDocumentProvider();
				IDocument doc = dp.getDocument(xmlEditor.getEditorInput());
				if (xml != null && !Arrays.equals(doc.get().getBytes(), xml.getBytes()))
					doc.set(xml);
				xmlFresh = true;
				return xml;
			}
		} catch (final Exception e) {
			UIUtils.showError(e);
		}
		return null;
	}

	protected abstract String doModel2xml() throws Exception;

	/**
	 * Closes all project files on project close.
	 * 
	 * @param event
	 *          the event
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		switch (event.getType()) {
		case IResourceChangeEvent.PRE_CLOSE:
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (int i = 0; i < pages.length; i++) {
						if (((FileEditorInput) xmlEditor.getEditorInput()).getFile().getProject().equals(event.getResource())) {
							IEditorPart editorPart = pages[i].findEditor(xmlEditor.getEditorInput());
							pages[i].closeEditor(editorPart, true);
						}
					}
				}
			});
			break;
		case IResourceChangeEvent.PRE_DELETE:
			break;
		case IResourceChangeEvent.POST_CHANGE:
			try {
				DeltaVisitor visitor = new DeltaVisitor(this);
				event.getDelta().accept(visitor);
				if (jrContext != null && getEditorInput() != null)
					jrContext.init(((IFileEditorInput) getEditorInput()).getFile());
			} catch (CoreException e) {
				UIUtils.showError(e);
			}
			break;
		case IResourceChangeEvent.PRE_BUILD:
		case IResourceChangeEvent.POST_BUILD:
			break;
		}
	}

	@Override
	public void doSaveAs() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				IProgressMonitor monitor = getActiveEditor().getEditorSite().getActionBars().getStatusLineManager()
						.getProgressMonitor();
				try {
					file.create(new ByteArrayInputStream("FILE".getBytes("UTF-8")), true, monitor);
					IFileEditorInput modelFile = new FileEditorInput(file);
					setInputWithNotify(modelFile);
					xmlEditor.setInput(modelFile);
					setPartName(file.getName());
					jrContext.init(file);

					doSave(monitor);
				} catch (CoreException e) {
					UIUtils.showError(e);
				} catch (UnsupportedEncodingException e) {
					UIUtils.showError(e);
				}
			}
		}
	}

	public static final String THEEDITOR = "thecurrenteditor";

	protected void getJrContext(IFile file) throws CoreException, JavaModelException {
		if (jrContext == null) {
			jrContext = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), file);
			jrContext.put(THEEDITOR, this);
		}
	}

	@Override
	public void dispose() {
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		setModel(null);
		if (jrContext != null)
			jrContext.dispose();
		super.dispose();
	}

	protected boolean isRefresh = false;

	protected IFile getCurrentFile() {
		if (getEditorInput() instanceof IFileEditorInput)
			return ((IFileEditorInput) getEditorInput()).getFile();
		return null;
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if (closing)
			return;
		NullProgressMonitor monitor = new NullProgressMonitor();
		input = FileUtils.checkAndConvertEditorInput(input, monitor);
		super.init(site, input);
		setSite(site);
		setPartName(input.getName());
		setInput(input);

		InputStream in = null;
		try {
			IFile file = null;
			if (input instanceof IFileEditorInput) {
				file = ((IFileEditorInput) input).getFile();
				if (!file.getProject().isOpen()) {
					file.getProject().open(monitor);
				}
				file.refreshLocal(0, monitor);
				if (!file.exists()) {
					closeEditor();
					return;
				}
				in = file.getContents();
			} else if (input instanceof JarEntryEditorInput) {
				in = ((JarEntryEditorInput) input).getStorage().getContents();
			} else
				throw new PartInitException("Invalid Input: Must be IFileEditorInput or FileStoreEditorInput"); //$NON-NLS-1$
			if (!isRefresh) {
				getJrContext(file);
				xml2model(in);
			}
		} catch (CoreException e) {
			e.printStackTrace();
			throw new PartInitException(e.getMessage(), e);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					setModel(null);
					throw new PartInitException("error closing input stream", e); //$NON-NLS-1$
				}
		}
	}

	boolean closing = false;

	private void closeEditor() {
		IWorkbenchWindow activeWorkbenchWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (activeWorkbenchWindow != null) {
			final IWorkbenchPage apage = activeWorkbenchWindow.getActivePage();
			if (apage != null)
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						closing = true;
						apage.closeEditor(AMultiEditor.this, false);
					}
				});
		}
	}

	/** The model. */
	protected INode model = null;

	public void setModel(INode model) {
		if (model == this.model)
			return;
		if (this.model != null && this.model.getChildren() != null && !this.model.getChildren().isEmpty())
			this.model.getChildren().get(0).getPropertyChangeSupport().addPropertyChangeListener(modelPropertyChangeListener);
		if (model != null && model.getChildren() != null && !model.getChildren().isEmpty())
			model.getChildren().get(0).getPropertyChangeSupport().addPropertyChangeListener(modelPropertyChangeListener);
		this.model = model;
	}

	private ModelPropertyChangeListener modelPropertyChangeListener = new ModelPropertyChangeListener();

	private final class ModelPropertyChangeListener implements PropertyChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			getSite().getWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					firePropertyChange(ISaveablePart.PROP_DIRTY);
				}
			});

		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	@Override
	public IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}

	protected XMLEditor xmlEditor;
	private MultiOutlineView outlinePage;

	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == IContentOutlinePage.class) {
			if (outlinePage == null)
				outlinePage = new MultiOutlineView(this);
			Display.getDefault().syncExec(new Runnable() {
				private boolean isUpdateOutline = false;

				public void run() {
					if (isUpdateOutline) {
						isUpdateOutline = true;
						updateContentOutline(getActivePage());
						isUpdateOutline = false;
					}
				}
			});
			return outlinePage;
		}
		return super.getAdapter(type);
	}

	private void updateContentOutline(int page) {
		if (outlinePage == null)
			return;
		IContentOutlinePage outline = (IContentOutlinePage) getEditor(page).getAdapter(IContentOutlinePage.class);
		if (outline == null)
			outline = new EmptyOutlinePage();
		outlinePage.setPageActive(outline);
	}

	private class StateListener implements IElementStateListener {

		public void elementDirtyStateChanged(Object element, boolean isDirty) {

		}

		public void elementContentAboutToBeReplaced(Object element) {

		}

		public void elementContentReplaced(Object element) {

		}

		public void elementDeleted(Object element) {
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					getSite().getPage().closeEditor(AMultiEditor.this, false);
				}
			});
		}

		public void elementMoved(Object originalElement, Object movedElement) {

		}

	}

	protected boolean xmlFresh = true;

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	protected void createPageXML() {
		try {
			xmlEditor = new XMLEditor(jrContext);
			int index = addPage(xmlEditor, getEditorInput());
			setPageText(index, Messages.common_source);
			xmlEditor.getDocumentProvider().getDocument(xmlEditor.getEditorInput())
					.addDocumentListener(new IDocumentListener() {

						public void documentChanged(DocumentEvent event) {
							xmlFresh = false;
						}

						public void documentAboutToBeChanged(DocumentEvent event) {

						}
					});
			xmlEditor.getDocumentProvider().addElementStateListener(new StateListener());
		} catch (PartInitException e) {
			UIUtils.showError(e);
		}
	}

	public static void refresh(JasperReportsContext jrContext) {
		Object obj = jrContext.getValue(AMultiEditor.THEEDITOR);
		if (obj instanceof JrxmlEditor) {
			ReportContainer rc = (ReportContainer) ((JrxmlEditor) obj).getEditor(JrxmlEditor.PAGE_DESIGNER);
			if (rc != null)
				refresh(rc.getActiveEditor());
		} else if (obj instanceof AMultiEditor) {
			refresh(((AMultiEditor) obj).getActiveEditor());
		}
	}

	public static void refresh(IEditorPart ep) {
		if (ep == null)
			return;
		if (ep instanceof IGraphicalEditor) {
			IGraphicalEditor ige = (IGraphicalEditor) ep;
			GraphicalViewer gv = ige.getGraphicalViewer();
			gv.getContents().refresh();
		}
	}

	/**
	 * Allow the refresh of a specific element of the editor
	 */
	public static void refreshElement(JasperReportsContext jrContext, PropertyChangeEvent event) {
		Object obj = jrContext.getValue(AMultiEditor.THEEDITOR);
		if (obj instanceof JrxmlEditor) {
			ReportContainer rc = (ReportContainer) ((JrxmlEditor) obj).getEditor(JrxmlEditor.PAGE_DESIGNER);
			if (rc != null)
				refreshElement(rc.getActiveEditor(), event);
		} else if (obj instanceof AMultiEditor) {
			refreshElement(((AMultiEditor) obj).getActiveEditor(), event);
		}
	}

	/**
	 * Allow the refresh of a specific element of the editor
	 */
	public static void refreshElement(IEditorPart ep, PropertyChangeEvent event) {
		if (ep == null)
			return;
		if (ep instanceof IGraphicalEditor) {
			IGraphicalEditor ige = (IGraphicalEditor) ep;
			GraphicalViewer gv = ige.getGraphicalViewer();
			EditPart editor = gv.getContents();
			if (editor instanceof ReportPageEditPart) {
				((ReportPageEditPart) editor).propertyChange(event);
			} else
				editor.refresh();
		}
	}
}
