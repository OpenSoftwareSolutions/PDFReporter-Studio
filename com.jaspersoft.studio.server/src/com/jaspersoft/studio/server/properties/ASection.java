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
package com.jaspersoft.studio.server.properties;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.properties.view.AbstractPropertySection;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.properties.action.EditCancelAction;
import com.jaspersoft.studio.server.properties.action.EditOkAction;
import com.jaspersoft.studio.server.properties.action.EditPropertyAction;

public abstract class ASection extends AbstractPropertySection {
	public ASection() {
		super();
	}

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setBackground(parent.getDisplay().getSystemColor(
				SWT.COLOR_WHITE));

		createSectionControls(composite, aTabbedPropertySheetPage);
		createActions(aTabbedPropertySheetPage);
		bindingContext = new DataBindingContext();
	}

	protected IToolBarManager tb;
	private EditPropertyAction editAction;
	private EditOkAction saveAction;
	private EditCancelAction cancelAction;

	protected void createActions(
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		tb = aTabbedPropertySheetPage.getSite().getActionBars()
				.getToolBarManager();
		editAction = (EditPropertyAction) tb.find(EditPropertyAction.ID);
		if (editAction == null) {
			editAction = new EditPropertyAction();
			tb.add(editAction);
		}
		editAction.addSection(this);

		cancelAction = (EditCancelAction) tb.find(EditCancelAction.ID);
		if (cancelAction == null)
			cancelAction = new EditCancelAction();
		cancelAction.addSection(this);

		saveAction = (EditOkAction) tb.find(EditOkAction.ID);
		if (saveAction == null)
			saveAction = new EditOkAction();
		saveAction.addSection(this);
	}

	protected void removeActions() {
		tb.remove(EditPropertyAction.ID);
		tb.remove(EditCancelAction.ID);
		tb.remove(EditOkAction.ID);
	}

	@Override
	public void aboutToBeHidden() {
		removeActions();
		super.aboutToBeHidden();
	}

	protected abstract void createSectionControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage);

	public abstract void enableFields(boolean enable);

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		super.setInput(part, selection);
		Assert.isTrue(selection instanceof IStructuredSelection);
		Object input = ((IStructuredSelection) selection).getFirstElement();
		Assert.isTrue(input instanceof MResource);
		this.res = (MResource) input;
		rebind();
		setEditMode(res.isEditMode());
	}

	protected void rebind() {
		Object[] bds = bindingContext.getBindings().toArray();
		for (Object obj : bds) {
			Binding b = (Binding) obj;
			bindingContext.removeBinding(b);
			b.dispose();
		}
		bind();
	}

	protected abstract void bind();

	@Override
	public void refresh() {
		bindingContext.updateTargets();
	}

	protected MResource res;
	protected DataBindingContext bindingContext;

	@Override
	public void aboutToBeShown() {
		if (res != null)
			setEditMode(res.isEditMode());
		super.aboutToBeShown();
	}

	public void editProperties() {
		setEditMode(true);
	}

	public void cancelEditProperties() {
		setEditMode(false);
	}

	public void saveProperties() {
		try {
			ProgressMonitorDialog pm = new ProgressMonitorDialog(Display
					.getDefault().getActiveShell());
			try {
				pm.run(true, true, new IRunnableWithProgress() {
					public void run(IProgressMonitor monitor)
							throws InvocationTargetException,
							InterruptedException {
						try {
							WSClientHelper.saveResource(res, monitor);

							Display.getDefault().asyncExec(new Runnable() {

								public void run() {
									setEditMode(false);
								}
							});
						} catch (Throwable e) {
							throw new InvocationTargetException(e);
						} finally {
							monitor.done();
						}
					}

				});
			} catch (InvocationTargetException e) {
				UIUtils.showError(e);
			} catch (InterruptedException e) {
				UIUtils.showError(e);
			}

		} catch (Exception e) {
			UIUtils.showError(e);
		}
	}

	protected void setEditMode(boolean edit) {
		removeActions();
		if (edit) {
			tb.add(cancelAction);
			tb.add(saveAction);
		} else {
			tb.add(editAction);
		}
		tb.update(true);
		enableFields(edit);
		res.setEditMode(edit);
	}
}
