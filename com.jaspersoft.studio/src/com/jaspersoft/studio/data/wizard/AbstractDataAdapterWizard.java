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
package com.jaspersoft.studio.data.wizard;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.eclipse.classpath.JavaProjectClassLoader;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;
import net.sf.jasperreports.engine.util.CompositeClassloader;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.IPageChangedListener;
import org.eclipse.jface.dialogs.IPageChangingListener;
import org.eclipse.jface.dialogs.PageChangedEvent;
import org.eclipse.jface.dialogs.PageChangingEvent;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.data.wizard.pages.DataAdapterEditorPage;
import com.jaspersoft.studio.data.wizard.pages.DataAdaptersListPage;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.wizards.JSSWizard;

/**
 * Abstract superclass for data adapter wizards. It maintains a list of shared fields and methods, plus the behavior of
 * the button used to test the data adapter.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public abstract class AbstractDataAdapterWizard extends JSSWizard implements SelectionListener {

	protected DataAdapterDescriptor dataAdapter = null;
	protected DataAdapterWizardDialog wizardDialog = null;
	protected DataAdapterFactory selectedFactory = null;
	protected DataAdaptersListPage dataAdapterListPage = null;
	protected DataAdapterEditorPage dataAdapterEditorPage = null;
	protected ADataAdapterStorage storage;

	public AbstractDataAdapterWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	public AbstractDataAdapterWizard(IWizard parentWizard, IWizardPage fallbackPage) {
		super(parentWizard, fallbackPage);
		setNeedsProgressMonitor(true);
	}

	/**
	 * Sets the wizard dialog that is used to display the wizard.
	 * 
	 * @param wizardDialog
	 *          the dialog displaying the wizard
	 */
	public void setWizardDialog(DataAdapterWizardDialog wizardDialog) {
		this.wizardDialog = wizardDialog;
		if (this.wizardDialog != null) {
			this.wizardDialog.addTestListener(this);

			this.wizardDialog.addPageChangingListener(new IPageChangingListener() {

				public void handlePageChanging(PageChangingEvent event) {

					if (event.getCurrentPage() == dataAdapterListPage && event.getTargetPage() == dataAdapterEditorPage) {
						// Update the layout of the editor page with the proper data adapter editor
						// provided by the new data adapter
						DataAdapterFactory factory = dataAdapterListPage.getSelectedFactory();

						java.text.MessageFormat fm = new java.text.MessageFormat(Messages.DataAdapterWizard_newdataadaptername);
						// 1. instance a new dataAdapter using the factory
						DataAdapterDescriptor newDataAdapter = factory.createDataAdapter();
						for (int i = 1; i < 1000; i++) {
							String name = fm.format(new Object[] { (i > 1) ? "(" + i + ")" : "" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

							if (storage.isDataAdapterNameValid(name)) {

								newDataAdapter.getDataAdapter().setName(name);
								break;
							}
						}

						// 2. set in the wizard page the data adapter to edit
						if (selectedFactory != factory) {
							dataAdapterEditorPage.setDataAdapter(newDataAdapter);
							selectedFactory = factory;
						}
					}
				}
			});

			// Enable the test button when the page activated is the dataAdapterEditorPage
			this.wizardDialog.addPageChangedListener(new IPageChangedListener() {

				public void pageChanged(PageChangedEvent event) {
					getWizardDialog().setTestButtonEnabled(event.getSelectedPage() == dataAdapterEditorPage);
				}
			});
		}
	}

	/**
	 * @return the wizard dialog instance
	 */
	public DataAdapterWizardDialog getWizardDialog() {
		return wizardDialog;
	}

	/**
	 * This method is called when the test button is pressed
	 */
	public void widgetSelected(SelectionEvent e) {
		if (getContainer().getCurrentPage() == dataAdapterEditorPage) {
			final DataAdapter da = dataAdapterEditorPage.getDataAdapterEditor().getDataAdapter().getDataAdapter();
			try {
				getContainer().run(true, true, new IRunnableWithProgress() {

					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask("Testing Data Adapter", SWT.INDETERMINATE);
						ClassLoader oldCL = Thread.currentThread().getContextClassLoader();
						try {
							ClassLoader cl = Thread.currentThread().getContextClassLoader();
							IProject[] prjs = ResourcesPlugin.getWorkspace().getRoot().getProjects();
							for (IProject p : prjs) {
								if (ProjectUtil.isOpen(p) && p.getNature(JavaCore.NATURE_ID) != null) {
									if (cl == null)
										cl = JavaProjectClassLoader.instance(JavaCore.create(p));
									else
										cl = new CompositeClassloader(cl, JavaProjectClassLoader.instance(JavaCore.create(p)));
								}
							}

							cl = JaspersoftStudioPlugin.getDriversManager().getClassLoader(cl);
							cl = new CompositeClassloader(cl, getClass().getClassLoader());
							Thread.currentThread().setContextClassLoader(cl);

							getConfig().setClassLoader(cl);

							DataAdapterServiceUtil.getInstance(getConfig()).getService(da).test();
							Display.getDefault().asyncExec(new Runnable() {

								@Override
								public void run() {
									UIUtils.showInformation(Messages.DataAdapterWizard_testbutton,
											Messages.DataAdapterWizard_testsuccesful);
								}
							});
						} catch (Exception e1) {
							UIUtils.showError(e1);
						} finally {
							monitor.done();
							Thread.currentThread().setContextClassLoader(oldCL);
						}
					}
				});
			} catch (InvocationTargetException e1) {
				UIUtils.showError(e1.getCause());
			} catch (InterruptedException e1) {
				UIUtils.showError(e1);
			}
		}
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// Do nothing...
	}

	/**
	 * Returns the new data adapter (or the modified data adapter in case the wizard is used to edit an existing data
	 * adapter). It returns null (or the original data adapter) if the wizard has not been completed. The returned object
	 * is the same used in the constructor in case of editing.
	 * 
	 * @return the data adapter
	 */
	public DataAdapterDescriptor getDataAdapter() {
		return this.dataAdapter;
	}

}
