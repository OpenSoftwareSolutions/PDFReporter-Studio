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
package com.jaspersoft.studio.server.wizard.resource;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.IInputControlsContainer;
import com.jaspersoft.studio.server.model.MResource;

public class ResourceWizard extends Wizard {
	private boolean skipFirstPage = false;
	private boolean nested = false;

	public void setNested(boolean nested) {
		this.nested = nested;
	}

	public ResourceWizard(ANode parent, MResource resource, boolean skipFirstPage, boolean nested) {
		this(parent, resource, skipFirstPage);
		setNested(nested);
	}

	public ResourceWizard(ANode parent, MResource resource, boolean skipFirstPage) {
		this(parent, resource);
		this.skipFirstPage = skipFirstPage;
	}

	public ResourceWizard(ANode parent, MResource resource) {
		super();
		setWindowTitle(Messages.ResourceWizard_windowtitle);
		setNeedsProgressMonitor(true);
		this.resource = resource;
		this.parent = parent;
	}

	@Override
	public IWizardPage getStartingPage() {
		IWizardPage[] pages = getPages();
		if (skipFirstPage && pages.length > 1)
			return pages[1];
		else if (pages.length == 1 && pages[0] instanceof EditResourcePage)
			((EditResourcePage) pages[0]).setFirstPage(1);
		return super.getStartingPage();
	}

	private ResourceFactory rfactory = new ResourceFactory();

	@Override
	public void addPages() {
		IWizardPage[] pages = rfactory.getResourcePage(parent, resource);
		for (IWizardPage p : pages)
			addPage(p);
	}

	private ANode parent;

	private MResource resource;

	@Override
	public boolean performFinish() {
		if (nested)
			return true;
		try {
			getContainer().run(false, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Saving", IProgressMonitor.UNKNOWN);
					try {
						ResourceDescriptor rd = resource.getValue();
						rd.fixResourceMap();
						WSClientHelper.saveResource(resource, monitor);
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
			UIUtils.showError(e);
			return false;
		}
		return true;
	}

	@Override
	public boolean performCancel() {
		if (nested)
			return true;
		try {
			getContainer().run(false, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Canceling", IProgressMonitor.UNKNOWN);
					try {
						if (resource.getParent() instanceof IInputControlsContainer)
							WSClientHelper.refreshContainer((MResource) resource.getParent(), monitor);
						else
							WSClientHelper.refreshResource(resource, monitor);
					} catch (Exception e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}
			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
		} catch (InterruptedException e) {
			UIUtils.showError(e);
		}
		return true;
	}
}
