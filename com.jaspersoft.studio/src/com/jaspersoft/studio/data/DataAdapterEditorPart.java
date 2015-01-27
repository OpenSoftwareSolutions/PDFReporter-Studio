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
package com.jaspersoft.studio.data;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.eclipse.builder.Markers;
import net.sf.jasperreports.eclipse.classpath.JavaProjectClassLoader;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.ISaveablePart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.part.FileEditorInput;

import com.jaspersoft.studio.data.storage.FileDataAdapterStorage;
import com.jaspersoft.studio.editor.preview.ABasicEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class DataAdapterEditorPart extends ABasicEditor {
	public static final String ID = "com.jaspersoft.studio.data.DataAdapterEditorPart"; //$NON-NLS-1$
	private DataAdapterDescriptor descriptor;
	private ModelPropertyChangeListener modelListener = new ModelPropertyChangeListener();
	private NameComposite nameComposite;
	private DataAdapterEditor editor;

	public DataAdapterEditorPart() {
		super(true);
	}

	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		InputStream in = null;
		try {
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			in = file.getContents(true);

			descriptor = FileDataAdapterStorage.readDataADapter(in, file.getProject());
			if (descriptor == null)
				throw new PartInitException("Can't find DataAdapter mapping."); //$NON-NLS-1$
		} catch (CoreException e) {
			UIUtils.showError(e);
		} finally {
			FileUtils.closeStream(in);
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		try {
			IResource resource = ((IFileEditorInput) getEditorInput()).getFile();
			IFile file = ((IFileEditorInput) getEditorInput()).getFile();
			descriptor = dacomposite.getDataAdapter();
			dacomposite.performAdditionalUpdates();

			String xml = DataAdapterManager.toDataAdapterFile(descriptor);

			file.setContents(new ByteArrayInputStream(xml.getBytes()), true, true, monitor);
			Markers.deleteMarkers(resource);
		} catch (CoreException e) {
			UIUtils.showError(e);
		}
		isDirty = false;
		firePropertyChange(PROP_DIRTY);
	}

	@Override
	public void doSaveAs() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalFile(((FileEditorInput) getEditorInput()).getFile());
		saveAsDialog.open();
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(path);
			if (file != null) {
				IFileEditorInput modelFile = new FileEditorInput(file);
				setInputWithNotify(modelFile);
				setInput(modelFile);
				setPartName(file.getName());
				IProgressMonitor progressMonitor = getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor();
				doSave(progressMonitor);
			}
		}
	}

	@Override
	public boolean isSaveAsAllowed() {
		return true;
	}

	private final class ModelPropertyChangeListener implements PropertyChangeListener {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
		 */
		public void propertyChange(PropertyChangeEvent evt) {
			getSite().getWorkbenchWindow().getShell().getDisplay().asyncExec(new Runnable() {
				public void run() {
					isDirty = true;
					firePropertyChange(ISaveablePart.PROP_DIRTY);
				}
			});

		}
	}

	@Override
	public void createPartControl(Composite parent) {
		Composite c = new Composite(parent, SWT.NONE);
		RowLayout rowLayout = new RowLayout();
		rowLayout.type = SWT.VERTICAL;
		rowLayout.justify = false;
		rowLayout.pack = true;
		rowLayout.fill = true;
		c.setLayout(rowLayout);
		nameComposite = new NameComposite(c, SWT.NONE, jrContext);
		if (descriptor != null) {
			editor = descriptor.getEditor();
			dacomposite = editor.getComposite(c, SWT.NONE, null, jrContext);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(c, editor.getHelpContextId());
			nameComposite.addModifyListener(modelListener);
			dacomposite.addModifyListener(modelListener);

			editor.setDataAdapter(descriptor);
			nameComposite.setDataAdapter(descriptor);

			final Button btnTest = new Button(c, SWT.PUSH);
			btnTest.setText(Messages.DataAdapterEditorPart_testButton);

			btnTest.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
					try {
						IProject project = ((IFileEditorInput) getEditorInput()).getFile().getProject();
						if(project.hasNature(JavaCore.NATURE_ID)) {
							ClassLoader cl = JavaProjectClassLoader.instance(JavaCore.create(project));
							if (cl != null)
								Thread.currentThread().setContextClassLoader(cl);
						}
						DataAdapterServiceUtil.getInstance(jrContext).getService(editor.getDataAdapter().getDataAdapter()).test();

						MessageBox mb = new MessageBox(btnTest.getShell(), SWT.ICON_INFORMATION | SWT.OK);
						mb.setText(Messages.DataAdapterWizard_testbutton);
						mb.setMessage(Messages.DataAdapterWizard_testsuccesful);
						mb.open();
					} catch (Exception e1) {
						UIUtils.showError(e1);
					} finally {
						Thread.currentThread().setContextClassLoader(oldCL);
					}
				}
			});
		}
	}

	private JasperReportsConfiguration jrContext;
	private ADataAdapterComposite dacomposite;

	protected void getJrContext(IFile file) throws CoreException, JavaModelException {
		if (jrContext == null)
			jrContext = new JasperReportsConfiguration(DefaultJasperReportsContext.getInstance(), file);
	}

	@Override
	public void dispose() {
		if (jrContext != null)
			jrContext.dispose();
		super.dispose();
	}

	@Override
	public void setFocus() {
		nameComposite.setFocus();
	}

}
