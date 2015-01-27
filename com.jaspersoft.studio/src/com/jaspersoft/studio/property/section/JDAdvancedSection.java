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
package com.jaspersoft.studio.property.section;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.report.EditorContributor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.internal.IHighlightPropertyWidget;
import com.jaspersoft.studio.properties.internal.IWidgetsProviderSection;
import com.jaspersoft.studio.properties.internal.WidgetDescriptor;
import com.jaspersoft.studio.properties.view.AdvancedPropertySection;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.JRPropertySheetEntry;



/**
 * 
 * This class implements the IWidgetsProvider section, even if it dosen't provide any widgets, but 
 * the implementation allow to return the selected element. It's pretty important that every section
 * could return the selected element
 *
 */
public class JDAdvancedSection extends AdvancedPropertySection implements PropertyChangeListener, IWidgetsProviderSection {
	private EditDomain editDomain;
	private APropertyNode element;
	protected TabbedPropertySheetPage atabbedPropertySheetPage;
	
	/**
	 * The last defined root entry
	 */
	private JRPropertySheetEntry rootEntry = null;
	

	public JDAdvancedSection() {
		super();
	}

	@Override
	public void createControls(Composite parent, final TabbedPropertySheetPage atabbedPropertySheetPage) {
		super.createControls(parent, atabbedPropertySheetPage);

		/*FormData data = new FormData();
		data.left = new FormAttachment(0, 0);
		data.right = new FormAttachment(100, -20);
		data.top = new FormAttachment(0, 0);
		data.bottom = new FormAttachment(100, 0);
		page.getControl().setLayoutData(data);*/
		UpdatePageContent();
	}
	

	private void UpdatePageContent(){
		if (page != null && element != null && getEditDomain() != null){
			page.selectionChanged(getPart(), new StructuredSelection(element));
			//Dispose the previous root entry (if one) before to create the new one
			disposeRootEntry();
			rootEntry = new JRPropertySheetEntry(getEditDomain().getCommandStack(), (ANode) element);
			page.setRootEntry(rootEntry);			
		}
	}
	
	/**
	 * Dispose the root entry if it wasen't already disposed
	 */
	private void disposeRootEntry(){
		if (rootEntry != null){
			rootEntry.dispose();
			rootEntry = null;
		}
	}

	@Override
	public void setInput(IWorkbenchPart part, ISelection selection) {
		setPart(part);
		setSelection(selection);
		if (!(selection.isEmpty()) && selection instanceof IStructuredSelection) {
			EditorContributor provider = (EditorContributor) part.getAdapter(EditorContributor.class);
			if (provider != null)
				setEditDomain(provider.getEditDomain());

			Assert.isTrue(selection instanceof IStructuredSelection);
			Object input = ((IStructuredSelection) selection).getFirstElement();
			Assert.isTrue(input instanceof EditPart);
			Object model = ((EditPart) input).getModel();
			Assert.isTrue(model instanceof APropertyNode);
			this.element = (APropertyNode) model;
			UpdatePageContent();
		}
	}

	public EditDomain getEditDomain() {
		return editDomain;
	}

	public void setEditDomain(EditDomain editDomain) {
		this.editDomain = editDomain;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.view.ITabbedPropertySection#aboutToBeShown()
	 */
	public void aboutToBeShown() {
		super.aboutToBeShown();
		if (getTabbedPropertySheetPage() != null){
			if (getElement() != null)
				getElement().getPropertyChangeSupport().addPropertyChangeListener(this);
			if (atabbedPropertySheetPage != null && atabbedPropertySheetPage.getSite() != null) {
				IActionBars actionBars = atabbedPropertySheetPage.getSite().getActionBars();
				if (actionBars != null)
					actionBars.getToolBarManager().removeAll();
				page.makeContributions(actionBars.getMenuManager(), actionBars.getToolBarManager(),
						actionBars.getStatusLineManager());
				actionBars.updateActionBars();
			}
		}
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.view.ITabbedPropertySection#aboutToBeHidden()
	 */
	public void aboutToBeHidden() {
		if (getTabbedPropertySheetPage() != null){
			if (getElement() != null)
				getElement().getPropertyChangeSupport().removePropertyChangeListener(this);
			if (atabbedPropertySheetPage != null && atabbedPropertySheetPage.getSite() != null) {
				IActionBars actionBars = atabbedPropertySheetPage.getSite().getActionBars();
				if (actionBars != null) {
					actionBars.getToolBarManager().removeAll();
					actionBars.updateActionBars();
				}
			}
		}
	}

	/**
	 * Get the element.
	 * 
	 * @return the element.
	 */
	public APropertyNode getElement() {
		return element;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (getElement() != evt.getSource()) {
			getElement().getPropertyChangeSupport().removePropertyChangeListener(this);
			if (!JSSCompoundCommand.isRefreshEventsIgnored(getElement())) {
				refresh();
			}
			getElement().getPropertyChangeSupport().addPropertyChangeListener(this);
		}
	}

	private boolean isRefreshing = false;

	@Override
	public void refresh() {
		if (isRefreshing)
			return;
		isRefreshing = true;
		if (page != null)
			page.refresh();
		isRefreshing = false;
	}

	@Override
	public Object getSelectedElement() {
		return getElement();
	}

	@Override
	public List<Object> getHandledProperties() {
		return new ArrayList<Object>();
	}

	@Override
	public IHighlightPropertyWidget getWidgetForProperty(Object propertyId) {
		return null;
	}

	@Override
	public WidgetDescriptor getPropertyInfo(Object propertyId) {
		return null;
	}

	@Override
	public void expandForProperty(Object propertyId) {}

}
