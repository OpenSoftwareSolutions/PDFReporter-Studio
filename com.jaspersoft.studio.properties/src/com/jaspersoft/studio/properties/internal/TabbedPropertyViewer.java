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
package com.jaspersoft.studio.properties.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.properties.view.ITabDescriptor;

/**
 * Viewer representing the property sheet page. On the left side it contains a
 * list of tabs and on the right side it contains the current selected tab.
 * 
 * @author Anthony Hunter
 */
public class TabbedPropertyViewer extends StructuredViewer {

	protected TabbedPropertyList list;
	protected List<ITabDescriptor> elements;
	protected IWorkbenchPart part;

	/**
	 * Constructor for TabbedPropertyViewer.
	 * 
	 * @param list
	 *          the TabbedPropertyList.
	 */
	public TabbedPropertyViewer(TabbedPropertyList list) {
		this.list = list;
		hookControl(list.getControl());
		elements = new ArrayList<ITabDescriptor>();
	}

	/**
	 * Returns the element with the given index from this list viewer. Returns
	 * <code>null</code> if the index is out of range.
	 * 
	 * @param index
	 *          the zero-based index
	 * @return the element at the given index, or <code>null</code> if the index
	 *         is out of range
	 */
	public ITabDescriptor getElementAt(int index) {
		if (index >= 0 && index < elements.size()) {
			return elements.get(index);
		}
		return null;
	}

	/**
	 * Returns the zero-relative index of the item which is currently selected in
	 * the receiver, or -1 if no item is selected.
	 * 
	 * @return the index of the selected item
	 */
	public int getSelectionIndex() {
		return list.getSelectionIndex();
	}

	protected Widget doFindInputItem(Object element) {
		/* not implemented */
		return null;
	}

	protected Widget doFindItem(Object element) {
		/* not implemented */
		return null;
	}

	protected void doUpdateItem(Widget item, Object element, boolean fullMap) {
		/* not implemented */
	}

	protected List<ITabDescriptor> getSelectionFromWidget() {
		int index = list.getSelectionIndex();
		if (index == TabbedPropertyList.NONE)
			return Collections.emptyList();
		List<ITabDescriptor> result = new ArrayList<ITabDescriptor>(1);
		result.add(getElementAt(index));
		return result;
	}

	protected void internalRefresh(Object element) {
		/* not implemented */
	}

	public void reveal(Object element) {
		/* not implemented */
	}

	/**
	 * We do not consider multiple selections. Only the first element will
	 * represent the selection.
	 */
	protected void setSelectionToWidget(List l, boolean reveal) {
		if (l == null || l.size() == 0) { // clear selection
			list.deselectAll();
		} else {
			Object object = l.get(0);
			int index = -1;
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i) == object) {
					index = i;
				}
			}
			Assert.isTrue(index != -1, "Could not set the selected tab in the tabbed property viewer");//$NON-NLS-1$
			list.select(index);
		}
	}
	
	public void setSelectionToWidget(String id, int defaultIndex) {
		if (elements.size()>0){
			int index = -1;
			for (int i = 0; i < elements.size(); i++) {
				if (elements.get(i).getId().equals(id)) {
					index = i;
					break;
				}
			}
			if (index == -1) index = defaultIndex;
			Assert.isTrue(!(index <0 || index > elements.size()), "Could not set the selected tab in the tabbed property viewer");//$NON-NLS-1$
			list.select(index);
		}
	}

	/**
	 * Force the selection to change on the tab with id equals to the one inside
	 * the Descriptor passed as parameter. If the tab to select is not found a
	 * exception is raised
	 */
	public void forceChangeSelectionToWidget(ITabDescriptor tab) {
		int index = -1;
		for (int i = 0; i < elements.size(); i++) {
			if (elements.get(i).getId().equals(tab.getId())) {
				index = i;
			}
		}
		Assert.isTrue(index != -1, "Could not set the selected tab in the tabbed property viewer");//$NON-NLS-1$
		list.select(index);
	}

	public Control getControl() {
		return list.getControl();
	}

	protected void inputChanged(Object input, Object oldInput) {
		elements.clear();
		Object[] children = getSortedChildren(getRoot());
		list.removeAll();
		for (int i = 0; i < children.length; i++) {
			elements.add((ITabDescriptor) children[i]);
			mapElement(children[i], list.getControl());
		}
		list.setElements(children);
	}

	/**
	 * Set the input for viewer.
	 * 
	 * @param part
	 *          the workbench part.
	 * @param selection
	 *          the selection in the workbench part.
	 */
	public void setInput(IWorkbenchPart part, ISelection selection) {
		this.part = part;
		setInput(selection);
	}

	/**
	 * Get the current workbench part.
	 * 
	 * @return the current workbench part.
	 */
	public IWorkbenchPart getWorkbenchPart() {
		return part;
	}

	/**
	 * Returns the elements in this list viewer.
	 * 
	 * @return the elements in this list viewer.
	 * @since 3.5
	 */
	public List<ITabDescriptor> getElements() {
		return elements;
	}

	@Override
	protected void updateSelection(final ISelection selection) {
		TabbedPropertyViewer.super.updateSelection(selection);
	}
}
