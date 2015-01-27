/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.builder.JasperReportsBuilder;
import net.sf.jasperreports.eclipse.builder.Markers;
import net.sf.jasperreports.eclipse.builder.jdt.JRErrorHandler;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpressionCollector;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.internal.resources.ResourceException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.jdt.internal.ui.javaeditor.JarEntryEditorInput;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
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
import org.eclipse.ui.editors.text.IStorageDocumentProvider;
import org.eclipse.ui.ide.FileStoreEditorInput;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.IElementStateListener;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.wb.swt.Keyboard;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.ExternalStylesManager;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.editor.expression.ExpressionEditorSupportUtil;
import com.jaspersoft.studio.editor.outline.page.EmptyOutlinePage;
import com.jaspersoft.studio.editor.outline.page.MultiOutlineView;
import com.jaspersoft.studio.editor.preview.PreviewContainer;
import com.jaspersoft.studio.editor.preview.view.control.VErrorPreview;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.editor.report.CachedSelectionProvider;
import com.jaspersoft.studio.editor.report.CommonSelectionCacheProvider;
import com.jaspersoft.studio.editor.report.ReportContainer;
import com.jaspersoft.studio.editor.xml.XMLEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.utils.AContributorAction;
import com.jaspersoft.studio.utils.Console;
import com.jaspersoft.studio.utils.JRXMLUtils;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.SyncDatasetRunParameters;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * An example showing how to create a multi-page editor. This example has 3 pages: <ul> <li>page 0 contains a nested
 * text editor. <li>page 1 allows you to change the font used in page 2 <li>page 2 shows the words in page 0 in sorted
 * order </ul>
 */
public class JrxmlEditor extends MultiPageEditorPart implements IResourceChangeListener, IGotoMarker, IJROBjectEditor,
		IMultiEditor, CachedSelectionProvider {

	private class StateListener implements IElementStateListener {

		public void elementDirtyStateChanged(Object element, boolean isDirty) {

		}

		public void elementContentAboutToBeReplaced(Object element) {

		}

		public void elementContentReplaced(Object element) {

		}

		public void elementDeleted(Object element) {
			IFile resource = getCurrentFile();
			String path = resource.getRawLocation().toOSString();
			DefaultManager.INSTANCE.removeDefaultFile(path);

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					getSite().getPage().closeEditor(JrxmlEditor.this, false);
				}
			});
		}

		public void elementMoved(Object originalElement, Object movedElement) {

		}

	}

	private final class PreviewEditor extends PreviewContainer {
		public PreviewEditor(boolean listenResource, JasperReportsConfiguration jrContext) {
			super(listenResource, jrContext);
		}

		@Override
		public void runReport(com.jaspersoft.studio.data.DataAdapterDescriptor myDataAdapterDesc) {
			boolean shiftPressed = JasperReportsPlugin.isPressed(SWT.SHIFT);
			if (!shiftPressed) {
				if (myDataAdapterDesc != null) {
					JasperDesign jasperDesign = getJasperDesign();
					String oldp = jasperDesign.getProperty(DataQueryAdapters.DEFAULT_DATAADAPTER);
					if (oldp == null || (oldp != null && !oldp.equals(myDataAdapterDesc.getName()))) {
						getMReport().putParameter(DataQueryAdapters.DEFAULT_DATAADAPTER, myDataAdapterDesc);
						jasperDesign.setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, myDataAdapterDesc.getName());
						setDirty(true);
					}
				}
				super.runReport(myDataAdapterDesc);
			}
		}

		/**
		 * Set the dirty flag of the preview area, but only if it isn't refreshing
		 */
		public void setDirty(boolean dirty) {
			if (!isRefresh) {
				super.setDirty(dirty);
			}
		}
	}

	/** The Constant PAGE_DESIGNER. */
	public static final int PAGE_DESIGNER = 0;

	/** The Constant PAGE_XMLEDITOR. */
	public static final int PAGE_XMLEDITOR = 1;

	/** The Constant PAGE_PREVIEW. */
	public static final int PAGE_PREVIEW = 2;

	/** The model. */
	private INode model = null;

	/** The text editor used in page 0. */
	private ReportContainer reportContainer;
	/** Xml editor used in page 1. */
	private XMLEditor xmlEditor;

	private JasperReportsConfiguration jrContext;

	/**
	 * Listener to execute the zoom in or zoomout operation when requested. It is static becuase it is placed on the
	 * display, so one get all the events
	 */
	private static Listener mouseWheelListener = new Listener() {

		/**
		 * Since the action is triggered more times (once for every control) the trigger time is used to repeat many times
		 * and actions that was actually requested once. So from the action with the same trigger time only one is executed.
		 * (it would me more correct consider a time interval, but essentially the trigger is so fast that they have the
		 * same trigger time).
		 */
		private int lastTime = -1;

		/**
		 * Execute the zoom in action if they can be retrieved, if the preview page is visible and if the executed action is
		 * enabled, otherwise it dosen't do nothing
		 * 
		 * @param event
		 */
		@Override
		public void handleEvent(Event event) {
			// System.out.println("KeyCode: " + event.character + " | " + event.keyCode + " " + event.toString());
			if (event.time != lastTime && JasperReportsPlugin.isPressed(Keyboard.getCtrlKey())) {
				IEditorPart currentEditor = SelectionHelper.getActiveJRXMLEditor();
				if (currentEditor != null && currentEditor instanceof JrxmlEditor) {
					JrxmlEditor jrxmlEditor = (JrxmlEditor) currentEditor;
					if (jrxmlEditor.getActivePage() == PAGE_PREVIEW) {
						lastTime = event.time;
					} else if (jrxmlEditor.getActivePage() == PAGE_DESIGNER && (event.character == '0' || event.keyCode == 48)) {
						IEditorPart editor = ((ReportContainer) jrxmlEditor.getEditor(PAGE_DESIGNER)).getActiveEditor();
						if (editor instanceof AbstractVisualEditor) {
							IAction action = ((AbstractVisualEditor) editor).getActionRegistry().getAction(ZoomActualAction.ID);
							if (action != null)
								action.run();
						}
					} else if (jrxmlEditor.getActivePage() == PAGE_DESIGNER
							&& (event.keyCode == '=' || event.keyCode == SWT.KEYPAD_ADD)) {
						IEditorPart editor = ((ReportContainer) jrxmlEditor.getEditor(PAGE_DESIGNER)).getActiveEditor();
						if (editor instanceof AbstractVisualEditor) {
							IAction action = ((AbstractVisualEditor) editor).getActionRegistry()
									.getAction(GEFActionConstants.ZOOM_IN);
							if (action != null)
								action.run();
						}
					}
				}
			}
		}
	};

	/**
	 * Creates a multi-page editor example.
	 */
	public JrxmlEditor() {
		super();
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this,
				IResourceChangeEvent.PRE_CLOSE | IResourceChangeEvent.PRE_DELETE | IResourceChangeEvent.POST_CHANGE);
		ExternalStylesManager.initListeners();
		JasperReportsPlugin.initializeKeyListener();
	}

	/**
	 * Bind some key combination to specific, it remove eventually the old ones to assure to have only on filter
	 */
	private void bindActionToKeys() {
		getContainer().getDisplay().removeFilter(org.eclipse.swt.SWT.MouseWheel, mouseWheelListener);
		getContainer().getDisplay().removeFilter(org.eclipse.swt.SWT.KeyDown, mouseWheelListener);
		getContainer().getDisplay().addFilter(org.eclipse.swt.SWT.MouseWheel, mouseWheelListener);
		getContainer().getDisplay().addFilter(org.eclipse.swt.SWT.KeyDown, mouseWheelListener);
	}

	/**
	 * Creates page 1 of the multi-page editor, which allows you to change the font used in page 2.
	 */
	void createPage0() throws PartInitException {
		reportContainer = new ReportContainer(this, jrContext);
		reportContainer.addPageChangedListener(new IPageChangedListener() {

			public void pageChanged(PageChangedEvent event) {
				updateContentOutline(getActivePage());
			}
		});

		int index = addPage(reportContainer, getEditorInput());
		setPageText(index, Messages.JrxmlEditor_design);
	}

	public ReportContainer getReportContainer() {
		return reportContainer;
	}

	/**
	 * Creates page 0 of the multi-page editor, which contains a text editor.
	 */
	void createPage1() throws PartInitException {
		xmlEditor = new XMLEditor(jrContext);

		int index = addPage(xmlEditor, getEditorInput());
		setPageText(index, Messages.common_source);
		IDocument doc = xmlEditor.getDocumentProvider().getDocument(xmlEditor.getEditorInput());
		doc.addDocumentListener(new IDocumentListener() {

			public void documentChanged(DocumentEvent event) {
				xmlFresh = false;
				previewEditor.setDirty(true);
			}

			public void documentAboutToBeChanged(DocumentEvent event) {

			}
		});
	}

	/**
	 * Creates page 2 of the multi-page editor, which shows the sorted text.
	 */
	void createPage2() throws PartInitException {
		previewEditor = new PreviewEditor(false, jrContext);

		int index = addPage(previewEditor, getEditorInput());
		setPageText(index, Messages.JrxmlEditor_preview);

		xmlEditor.getDocumentProvider().addElementStateListener(new StateListener());
	}

	/**
	 * Creates the pages of the multi-page editor.
	 */
	@Override
	protected void createPages() {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(getContainer(), "com.jaspersoft.studio.doc.editor_jrxml");
		if (jrContext != null)
			try {
				createPage0();
				createPage1();
				Display.getDefault().asyncExec(new Runnable() {

					@Override
					public void run() {
						try {
							createPage2();
							bindActionToKeys();
						} catch (PartInitException e) {
							UIUtils.showError(new Exception(Messages.common_error_creating_nested_visual_editor));
						}
					}
				});
			} catch (PartInitException e) {
				UIUtils.showError(new Exception(Messages.common_error_creating_nested_visual_editor));
			} catch (Throwable e) {
				closeEditor();
				JaspersoftStudioPlugin.getInstance().logError(e);
			}
	}

	@Override
	public IEditorPart getActiveEditor() {
		return super.getActiveEditor();
	}

	public IEditorPart getActiveEditor2() {
		IEditorPart iep = getActiveEditor();
		if (iep instanceof ReportContainer)
			return ((ReportContainer) iep).getActiveEditor();
		return iep;
	}

	@Override
	public IEditorPart getEditor(int pageIndex) {
		if (getContainer().isDisposed() || getPageCount() <= pageIndex)
			return null;
		return super.getEditor(pageIndex);
	}

	private MultiOutlineView outlinePage;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette#getAdapter(java.lang.Class)
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public Object getAdapter(Class type) {
		if (type == JasperReportsContext.class)
			return jrContext;
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

	/**
	 * The <code>MultiPageEditorPart</code> implementation of this <code>IWorkbenchPart</code> method disposes all nested
	 * editors. Subclasses may extend.
	 */
	@Override
	public void dispose() {
		// getContainer().getDisplay().removeFilter(org.eclipse.swt.SWT.MouseWheel, mouseWheelListener);
		// getContainer().getDisplay().removeFilter(org.eclipse.swt.SWT.KeyDown, mouseWheelListener);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
		setModel(null);
		if (jrContext != null)
			jrContext.dispose();
		super.dispose();
	}

	boolean isRefresh = false;

	/**
	 * Saves the multi-page editor's document.
	 * 
	 * @param monitor
	 *          the monitor
	 */
	@Override
	public void doSave(final IProgressMonitor monitor) {
		try {
			isRefresh = true;

			// Check for function library static imports (see issue #0005771)
			// It's better to put the check here instead on the JRExpressionEditor dialog close.
			// This allow for example to "fix" the report, depending on the preference setting,
			// also when simply saving the JRXML file without having edited an expression.
			JasperDesign jd = getJasperDesign();
			if (jd != null)
				ExpressionEditorSupportUtil.updateFunctionsLibraryImports(jd, jrContext);

			final IFile resource = getCurrentFile();
			if (resource == null)
				return;
			try {
				if (!resource.exists())
					resource.create(new ByteArrayInputStream("FILE".getBytes("UTF-8")), true, monitor);

				resource.setCharset("UTF-8", monitor);
				((IStorageDocumentProvider) xmlEditor.getDocumentProvider()).setEncoding(getEditorInput(), "UTF-8");
			} catch (CoreException e) {
				UIUtils.showError(e);
			} catch (UnsupportedEncodingException e) {
				UIUtils.showError(e);
			}
			if ((!xmlEditor.isDirty() && reportContainer.isDirty()) || getActiveEditor() != xmlEditor) {
				version = JRXmlWriterHelper.getVersion(resource, jrContext, true);
				model2xml(version);
			} else {
				IDocumentProvider dp = xmlEditor.getDocumentProvider();
				IDocument doc = dp.getDocument(xmlEditor.getEditorInput());
				try { // just go thru the model, to look what happend with our markers
					Markers.deleteMarkers(resource);

					xml2model();
				} catch (Throwable e) {
					Markers.addMarker(resource, e);
					doSaveEditors(monitor);// on eclipse 4.2.1 on first first save, for some reasons save is not working .., so
																	// we'll do it manually
					resource.setContents(new ByteArrayInputStream(doc.get().getBytes("UTF-8")), IFile.KEEP_HISTORY | IFile.FORCE,
							monitor);
					finishSave(resource);
					return;
				}
			}
			if (getFileExtension(getEditorInput()).equals("")) { //$NON-NLS-1$
				// save binary
				try {
					new JasperReportsBuilder().compileJRXML(resource, monitor);
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			UIUtils.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					if (isDirty())
						JaspersoftStudioPlugin.getExtensionManager().onSave(jrContext, monitor);
					try {
						String xml = model2xml(version);
						doSaveEditors(monitor);
						// on eclipse 4.2.1 on first first save, for some reasons save is not working .., so we'll do it manually
						resource.setContents(new ByteArrayInputStream(xml.getBytes("UTF-8")), IFile.KEEP_HISTORY | IFile.FORCE,
								monitor);
						finishSave(resource);
					} catch (Throwable e) {
						UIUtils.showError(e);
					}
				}
			});
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private IFile getCurrentFile() {
		if (getEditorInput() instanceof IFileEditorInput)
			return ((IFileEditorInput) getEditorInput()).getFile();
		return null;
	}

	private void doSaveEditors(final IProgressMonitor monitor) {
		xmlEditor.doSave(monitor);
		reportContainer.doSave(monitor);
		previewEditor.doSave(monitor);

		xmlEditor.isDirty();
		reportContainer.isDirty();
		previewEditor.isDirty();

		xmlFresh = true;
	}

	protected void finishSave(IFile resource) {
		String resourceAbsolutePath = resource.getRawLocation().toOSString();
		if (DefaultManager.INSTANCE.isCurrentDefault(resourceAbsolutePath)) {
			DefaultManager.INSTANCE.reloadCurrentDefault();
		}
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				isRefresh = false;
				firePropertyChange(ISaveablePart.PROP_DIRTY);
			}
		});
	}

	/**
	 * Saves the multi-page editor's document as another file. Also updates the text for page 0's tab, and updates this
	 * multi-page editor's input to correspond to the nested editor's.
	 */
	@Override
	public void doSaveAs() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalFile(((FileEditorInput) getEditorInput()).getFile());
		if (saveAsDialog.open() == Dialog.OK) {
			IPath path = saveAsDialog.getResult();
			if (path != null) {
				IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
				if (file != null) {
					IProgressMonitor monitor = getActiveEditor().getEditorSite().getActionBars().getStatusLineManager()
							.getProgressMonitor();

					try {
						if (!file.exists())
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
	}

	@Override
	public String getTitleToolTip() {
		return JaspersoftStudioPlugin.getExtensionManager().getTitleToolTip(jrContext, super.getTitleToolTip());
	}

	/**
	 * The <code>MultiPageEditorExample</code> implementation of this method checks that the input is an instance of
	 * <code>IFileEditorInput</code>.
	 * 
	 * @param site
	 *          the site
	 * @param editorInput
	 *          the editor input
	 * @throws PartInitException
	 *           the part init exception
	 */
	@Override
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		if (closing)
			return;
		// FIXME: THIS IS NOT THE RIGHT PLACE TO LOAD MODEL, WE SHOULD LOAD FROM
		// TEXT EDITOR TO AVOID 2 TIME READING THE FILE
		NullProgressMonitor monitor = new NullProgressMonitor();
		editorInput = FileUtils.checkAndConvertEditorInput(editorInput, monitor);
		super.init(site, editorInput);
		setPartName(editorInput.getName());
		InputStream in = null;
		IFile file = null;
		try {
			if (editorInput instanceof IFileEditorInput) {
				file = ((IFileEditorInput) editorInput).getFile();
				if (!file.getProject().isOpen()) {
					file.getProject().open(monitor);
				}
				file.refreshLocal(0, monitor);
				if (!file.exists()) {
					closeEditor();
					return;
				}

				in = file.getContents();
			} else if (editorInput instanceof JarEntryEditorInput) {
				in = ((JarEntryEditorInput) editorInput).getStorage().getContents();
			} else
				throw new PartInitException("Invalid Input: Must be IFileEditorInput or FileStoreEditorInput"); //$NON-NLS-1$

			getJrContext(file);
			if (!isRefresh) {
				final InputStream inp = in;
				final IFile ifile = file;
				// Job job = new Job("Initialising " + getPartName()) {
				// @Override
				// protected IStatus run(IProgressMonitor monitor) {
				// monitor.beginTask("Initialising " + getPartName(), IProgressMonitor.UNKNOWN);
				// try {
				doInitModel(monitor, getEditorInput(), inp, ifile);

				// } finally {
				// monitor.done();
				// }
				// return Status.OK_STATUS;
				// }
				// };
				// job.setPriority(Job.LONG);
				// job.schedule();
			}
		} catch (Exception e) {
			setModel(null);
			handleJRException(editorInput, e, false);
		}
	}

	protected void doInitModel(IProgressMonitor monitor, IEditorInput editorInput, InputStream in, IFile file) {
		try {
			in = getXML(jrContext, editorInput, file.getCharset(true), in, version);

			JasperDesign jd = new JRXmlLoader(jrContext, JasperReportsConfiguration.getJRXMLDigester())
					.loadXML(new InputSource(in));
			JaspersoftStudioPlugin.getExtensionManager().onLoad(jd, this);
			// NO LONGER AVAILABLE IN GLOBAL TOOLBAR SINCE
			// THEY WILL BE VISIBLE IN THE ReportContainer toolbar.
			// editorActions = JaspersoftStudioPlugin.getExtensionManager().getActions();
			// for (AContributorAction a : editorActions) {
			// a.setJrConfig(jrContext);
			// ((JrxmlEditorContributor) getEditorSite().getActionBarContributor()).addGlobaRetargetAction(a);
			// }

			jrContext.setJasperDesign(jd);

			UIUtils.getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					setModel(ReportFactory.createReport(jrContext));
					MReport report = getMReport();
					if (report != null) {
						SyncDatasetRunParameters.sync(report);
					}
				}
			});
		} catch (ResourceException e) {
			if (e.getMessage().startsWith("File not found")) {
				closeEditor();
			} else {
				setModel(null);
				handleJRException(editorInput, e, false);
			}
		} catch (Exception e) {
			setModel(null);
			handleJRException(editorInput, e, false);
		} finally {
			FileUtils.closeStream(in);
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
						apage.closeEditor(JrxmlEditor.this, false);
					}
				});
		}
	}

	public JasperReportsConfiguration getJrContext(IFile file) {
		if (jrContext == null) {
			jrContext = JasperReportsConfiguration.getDefaultJRConfig(file);
			jrContext.put(AMultiEditor.THEEDITOR, this);
			jrContext.setJRParameters(new HashMap<String, Object>());
		}
		return jrContext;
	}

	public static String getFileExtension(IEditorInput editorInput) {
		String fileExtention = ""; //$NON-NLS-1$
		if (editorInput instanceof FileStoreEditorInput) {
			String path = ((FileStoreEditorInput) editorInput).getURI().getPath();
			fileExtention = path.substring(path.lastIndexOf(".") + 1, path.length()); //$NON-NLS-1$
		} else if (editorInput instanceof IFileEditorInput) {
			fileExtention = ((IFileEditorInput) editorInput).getFile().getFileExtension();
		} else if (editorInput instanceof JarEntryEditorInput) {
			fileExtention = ((JarEntryEditorInput) editorInput).getStorage().getFullPath().getFileExtension();
		}
		return fileExtention;
	}

	public static InputStream getXML(JasperReportsConfiguration jrContext, IEditorInput editorInput, String encoding,
			InputStream in, String version) throws JRException {
		String fileExtension = getFileExtension(editorInput);
		InputStream jrxmlInputStream = JRXMLUtils.getJRXMLInputStream(jrContext, in, fileExtension, encoding, version);
		return jrxmlInputStream != null ? jrxmlInputStream : in;
	}

	/**
	 * Handle jr exception.
	 * 
	 * @param editorInput
	 *          the editor input
	 * @param e
	 *          the e
	 * @param mute
	 *          the mute
	 */
	public void handleJRException(IEditorInput editorInput, final Exception e, boolean mute) {
		if (!mute)
			UIUtils.showError(e);
		try {
			isRefresh = true;
			if (editorInput instanceof IFileEditorInput) {
				IResource resource = ((IFileEditorInput) editorInput).getFile();
				if (!resource.exists())
					return;
				Markers.deleteMarkers(resource);

				final IMarker marker = Markers.addMarker(resource, e);
				marker.setAttribute(IMarker.TRANSIENT, true);

				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						gotoMarker(marker);
						toXML = true;
						setActivePage(PAGE_XMLEDITOR);
						isRefresh = false;
					}
				});
			}
		} catch (CoreException e1) {
			e1.printStackTrace();
		}
	}

	@Override
	protected void handlePropertyChange(int propertyId) {
		if (!isRefresh) {
			if (propertyId == ISaveablePart.PROP_DIRTY && previewEditor != null)
				previewEditor.setDirty(true);
			super.handlePropertyChange(propertyId);
		}
	}

	/*
	 * (non-Javadoc) Method declared on IEditorPart.
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	/** The xml fresh. */
	private boolean xmlFresh = true;

	private PreviewEditor previewEditor;
	private boolean toXML = false;

	/**
	 * Set the current preview type
	 * 
	 * @param viewerKey
	 *          key of the type to show
	 * @param refresh
	 *          flag to set if the preview should also be refreshed
	 */
	public void setPreviewOutput(String key, boolean refresh) {
		previewEditor.setCurrentViewer(key, refresh);
	}

	/**
	 * Return the actual preview type key on the preview editor
	 * 
	 * @return String representing the actual output of the preview editor
	 */
	public String getDefaultViewerKey() {
		return previewEditor.getDefaultViewerKey();
	}

	/**
	 * Set the preview editor to dirty, this will refresh the preview when switching into it
	 * 
	 * @param dirty
	 *          true to set the editor dirty, false otherwise
	 */
	public void setPreviewDirty(boolean dirty) {
		previewEditor.setDirty(dirty);
	}

	/**
	 * Calculates the contents of page 2 when the it is activated.
	 * 
	 * @param newPageIndex
	 *          the new page index
	 */
	@Override
	protected void pageChange(final int newPageIndex) {
		if (newPageIndex == PAGE_DESIGNER || newPageIndex == PAGE_XMLEDITOR || newPageIndex == PAGE_PREVIEW) {
			if (activePage == PAGE_DESIGNER) {
				if (outlinePage != null)
					tmpselection = outlinePage.getSite().getSelectionProvider().getSelection();
				else
					tmpselection = reportContainer.getActiveEditor().getSite().getSelectionProvider().getSelection();
			}
			String ver = JRXmlWriterHelper.getVersion(getCurrentFile(), jrContext, false);
			switch (newPageIndex) {
			case PAGE_DESIGNER:
				if (activePage == PAGE_XMLEDITOR && !xmlFresh) {
					try {
						xml2model();
					} catch (Exception e) {
						toXML = true;
						handleJRException(getEditorInput(), e, false);
					}
					updateVisualView();
				} else if (activePage == PAGE_DESIGNER) {
					updateVisualView();
				} else {
					// stop running reports
					previewEditor.getReportControler().stop();
				}
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						ISelectionProvider sp = null;
						if (outlinePage != null)
							sp = outlinePage.getSite().getSelectionProvider();
						else
							sp = reportContainer.getActiveEditor().getSite().getSelectionProvider();

						sp.setSelection(tmpselection);
					}
				});
				break;
			case PAGE_XMLEDITOR:
				// if (reportContainer.isDirty())
				if (toXML)
					toXML = false;
				else {
					model2xml(ver);
				}
				break;
			case PAGE_PREVIEW:
				if (activePage == PAGE_XMLEDITOR && !xmlFresh)
					try {
						xml2model();
					} catch (Exception e) {
						handleJRException(getEditorInput(), e, false);
					}
				else if (reportContainer.isDirty()) {
					isRefresh = true;
					model2xml(ver);
					isRefresh = false;
				}
				Job job = new Job("Switching to Preview") {
					@Override
					protected IStatus run(final IProgressMonitor monitor) {
						monitor.beginTask("Compiling subreport", IProgressMonitor.UNKNOWN);
						if (jrContext.getPropertyBoolean(DesignerPreferencePage.P_SAVE_ON_PREVIEW, Boolean.FALSE)) {
							monitor.subTask("Saving Report");
							UIUtils.getDisplay().syncExec(new Runnable() {

								@Override
								public void run() {
									doSave(monitor);
								}
							});
						}
						UIUtils.getDisplay().syncExec(new Runnable() {
							@Override
							public void run() {
								model2preview();
								JrxmlEditor.super.pageChange(newPageIndex);
								updateContentOutline(getActivePage());
								activePage = newPageIndex;
							}
						});
						return Status.OK_STATUS;
					}
				};
				job.setSystem(true);
				job.schedule();
				return;
			}
		}
		super.pageChange(newPageIndex);
		updateContentOutline(getActivePage());
		activePage = newPageIndex;
	}

	private ISelection tmpselection;
	private int activePage = 0;

	private String version = "last";

	private List<AContributorAction> editorActions;

	/**
	 * Xml2model.
	 * 
	 * @throws JRException
	 *           the jR exception
	 */
	private void xml2model() throws Exception {
		InputStream in = null;
		try {
			IDocumentProvider dp = xmlEditor.getDocumentProvider();
			IDocument doc = dp.getDocument(xmlEditor.getEditorInput());
			in = new ByteArrayInputStream(doc.get().getBytes("UTF-8"));

			JasperDesign jd = new JRXmlLoader(jrContext, JasperReportsConfiguration.getJRXMLDigester()).loadXML(in);
			jrContext.setJasperDesign(jd);
			JaspersoftStudioPlugin.getExtensionManager().onLoad(jd, this);
			setModel(ReportFactory.createReport(jrContext));
		} finally {
			FileUtils.closeStream(in);
		}
	}

	private void model2xml() {
		model2xml("last");
	}

	/**
	 * Model2xml.
	 */
	private String model2xml(String version) {
		String xml = null;
		try {
			JasperDesign report = null;
			MReport mReport = getMReport();
			if (mReport != null) {
				report = mReport.getJasperDesign();
				Object obj = mReport.getParameter(DataQueryAdapters.DEFAULT_DATAADAPTER);
				if (obj != null && obj instanceof DataAdapterDescriptor) {
					String dataAdapterDesc = previewEditor.getDataAdapterDesc().getName();
					report.removeProperty(DataQueryAdapters.DEFAULT_DATAADAPTER);
					report.setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, dataAdapterDesc);
				}
			}

			xml = JRXmlWriterHelper.writeReport(jrContext, report, "UTF-8", version);
			IDocumentProvider dp = xmlEditor.getDocumentProvider();
			IDocument doc = dp.getDocument(xmlEditor.getEditorInput());
			if (xml != null && !Arrays.equals(doc.get().getBytes(), xml.getBytes())) {
				doc.set(xml);
			}
			xmlFresh = true;
		} catch (Throwable e) {
			UIUtils.showError(e);
		}
		return xml;
	}

	protected JasperDesign getJasperDesign() {
		MReport mreport = getMReport();
		if (mreport != null)
			return mreport.getValue();
		return null;
	}

	/**
	 * Model2xml.
	 */
	private void model2preview() {
		previewEditor.setJasperDesign(jrContext);
	}

	/**
	 * Closes all project files on project close.
	 * 
	 * @param event
	 *          the event
	 */
	public void resourceChanged(final IResourceChangeEvent event) {
		if (isRefresh)
			return;
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

	/**
	 * Sets the model.
	 * 
	 * @param model
	 *          the new model
	 */
	public void setModel(INode model) {
		this.model = model;
		updateVisualView();
		if (jrContext != null)
			jrContext.setJasperDesign(getJasperDesign());
	}

	private MReport getMReport() {
		if (model != null)
			return (MReport) model.getChildren().get(0);
		return null;
	}

	/**
	 * Gets the model.
	 * 
	 * @return the model
	 */
	public INode getModel() {
		return model;
	}

	/**
	 * Update visual view.
	 */
	public void updateVisualView() {
		if (reportContainer != null)
			reportContainer.setModel(getModel());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ide.IGotoMarker
	 */
	public void gotoMarker(IMarker marker) {
		if (activePage == PAGE_DESIGNER) {
			try {
				Object expr = marker.getAttribute(JRErrorHandler.MARKER_ERROR_JREXPRESSION);
				if (expr != null && expr instanceof String) {
					JRDesignExpression expression = new JRDesignExpression();
					expression.setId(new Integer((String) expr));
					JasperDesign jd = getJasperDesign();
					JRExpressionCollector rc = JRExpressionCollector.collector(jrContext, jd);
					if (!VErrorPreview.openExpressionEditor(jrContext, rc, (JRDesignDataset) jd.getMainDataset(), expression))
						for (JRDataset d : jd.getDatasetsList())
							if (VErrorPreview.openExpressionEditor(jrContext, rc, (JRDesignDataset) d, expression))
								return;
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
		setActivePage(PAGE_XMLEDITOR);
		IDE.gotoMarker(xmlEditor, marker);
	}

	public void openEditor(Object obj, ANode node) {
		reportContainer.openEditor(obj, node);
	}

	/**
	 * Return the console area if available, null otherwise
	 */
	public Console getConsole() {
		if (previewEditor != null) {
			return previewEditor.getConsole();
		}
		return null;
	}

	public void refreshExternalStyles(HashSet<String> removedStyles) {
		// Very very heavy method, leave commented for future improovments
		/*
		 * JasperDesign jrDesign = getMReport().getJasperDesign(); for(JRDesignElement element :
		 * ModelUtils.getAllElements(jrDesign)){ if (element.getStyleNameReference() != null &&
		 * removedStyles.contains(element.getStyleNameReference())){ String styleName = element.getStyleNameReference();
		 * element.setStyleNameReference(null); element.setStyleNameReference(styleName); } } StyleHandlingReportConverter
		 * reportConverter =
		 * ((AEditPartFactory)reportContainer.getMainEditor().getGraphicalViewer().getEditPartFactory()).getReportConverter
		 * (); if (reportConverter != null) reportConverter.resetStyles(jrDesign);
		 */
	}

	@Override
	public CommonSelectionCacheProvider getSelectionCache() {
		return reportContainer.getSelectionCache();
	}

}
