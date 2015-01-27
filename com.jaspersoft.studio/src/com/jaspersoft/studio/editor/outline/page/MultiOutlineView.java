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
package com.jaspersoft.studio.editor.outline.page;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IStatusLineManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.Page;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.jaspersoft.studio.editor.IGraphicalEditor;
import com.jaspersoft.studio.editor.IMultiEditor;
import com.jaspersoft.studio.editor.outline.JDReportOutlineView;
import com.jaspersoft.studio.editor.xml.outline.EditorContentOutlinePage;

public class MultiOutlineView extends Page implements IContentOutlinePage, ISelectionProvider,
		ISelectionChangedListener, IAdaptable {
	private PageBook pagebook;
	private ISelection selection;
	private ArrayList<ISelectionChangedListener> listeners;
	private IContentOutlinePage currentPage;
	private IContentOutlinePage emptyPage;
	private IActionBars actionBars;
	private EditorPart editor;

	public MultiOutlineView(EditorPart editor) {
		getListeners();
		this.editor = editor;
	}

	public EditorPart getEditor() {
		return editor;
	}

	private List<ISelectionChangedListener> getListeners() {
		if (listeners == null)
			listeners = new ArrayList<ISelectionChangedListener>();
		return listeners;
	}

	public void addFocusListener(FocusListener listener) {
	}

	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		if (listeners != null)
			listeners.add(listener);
	}

	@Override
	public void createControl(Composite parent) {
		pagebook = new PageBook(parent, SWT.NONE);
		listeners = new ArrayList<ISelectionChangedListener>();
		if (editor instanceof IMultiEditor) {
			IMultiEditor ed = (IMultiEditor) editor;
			IContentOutlinePage cop = (IContentOutlinePage) ed.getActiveEditor().getAdapter(IContentOutlinePage.class);
			if (cop != null)
				setPageActive(cop);
		}
	}

	@Override
	public void dispose() {
		if (pagebook != null && !pagebook.isDisposed())
			pagebook.dispose();
		if (emptyPage != null) {
			emptyPage.dispose();
			emptyPage = null;
		}
		pagebook = null;
		listeners = null;
	}

	public boolean isDisposed() {
		return listeners == null;
	}

	@Override
	public Control getControl() {
		return pagebook;
	}

	public PageBook getPagebook() {
		return pagebook;
	}

	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void makeContributions(IMenuManager menuManager, IToolBarManager toolBarManager,
			IStatusLineManager statusLineManager) {
	}

	public void removeFocusListener(FocusListener listener) {
	}

	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		if (listeners != null)
			listeners.remove(listener);
	}

	public void selectionChanged(SelectionChangedEvent event) {
		setSelection(event.getSelection());
	}

	@Override
	public void setActionBars(IActionBars actionBars) {
		this.actionBars = actionBars;
		registerToolbarActions(actionBars);
		if (currentPage != null){
			setPageActive(currentPage);
		}
	}

	public IActionBars getActionBars() {
		return actionBars;
	}

	@Override
	public void setFocus() {
		try {
			if (currentPage != null)
				currentPage.setFocus();
		} catch (Throwable t) {
		}
	}

	private IContentOutlinePage getEmptyPage() {
		if (emptyPage == null)
			emptyPage = new EmptyOutlinePage();
		return emptyPage;
	}

	private boolean isRefresh = false;

	public void setPageActive(IContentOutlinePage page) {
		if (isRefresh)
			return;
		isRefresh = true;
		if (page == null)
			page = getEmptyPage();
		Control control = page.getControl();
		if (currentPage != null)
			currentPage.removeSelectionChangedListener(this);
		if (getActionBars() != null && getActionBars().getToolBarManager() != null){
			getActionBars().getToolBarManager().removeAll();
			// when the action are cleared reload the ones for the current page
			if (page != null && page instanceof JDReportOutlineView) {
				((JDReportOutlineView) page).registerToolbarAction(getActionBars().getToolBarManager());
			}
		}
		if (getSite() != null && page instanceof JDReportOutlineView) {
			JDReportOutlineView jdoutpage = (JDReportOutlineView) page;
			if (page.getControl() != null && page.getControl().isDisposed()) {
				IGraphicalEditor ed = jdoutpage.getEditor();
				if (ed instanceof IAdaptable)
					page = (IContentOutlinePage) ((IAdaptable) ed).getAdapter(IContentOutlinePage.class);
			}
			jdoutpage.init(getSite());
		}
		page.addSelectionChangedListener(this);
		this.currentPage = page;
		if (pagebook == null) {
			// still not being made
			isRefresh = false;
			return;
		}
		if (control == null || control.isDisposed()) {
			// first time, it will also create the contextual action
			page.createControl(pagebook);
			if (getActionBars() != null){
				page.setActionBars(getActionBars());
			}
			control = page.getControl();
		}
		pagebook.showPage(control);
		if (page instanceof JDReportOutlineView) {
			JDReportOutlineView jdoutpage = (JDReportOutlineView) page;
			jdoutpage.setTreeSelection(selection);
		} else if (page instanceof EditorContentOutlinePage) {
			EditorContentOutlinePage jdoutpage = (EditorContentOutlinePage) page;
			jdoutpage.update();
		} else {
			setSelection(page.getSelection());
		}
		if (getActionBars() != null && getActionBars().getToolBarManager() != null){
			getActionBars().getToolBarManager().update(true);
		}
		isRefresh = false;
	}

	/**
	 * Set the selection.
	 */
	public void setSelection(ISelection selection) {
		this.selection = selection;
		if (listeners == null || selection == null)
			return;
		SelectionChangedEvent e = new SelectionChangedEvent(this, selection);
		for (int i = 0; i < listeners.size(); i++) {
			((ISelectionChangedListener) listeners.get(i)).selectionChanged(e);
		}
	}

	private void registerToolbarActions(IActionBars actionBars) {
		IToolBarManager toolBarManager = actionBars.getToolBarManager();
		if (toolBarManager != null) {
			// toolBarManager.add(new ToggleLinkWithEditorAction(editor));
			// toolBarManager.add(new SortingAction());
		}
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (currentPage instanceof IAdaptable && currentPage != this)
			return ((IAdaptable) currentPage).getAdapter(adapter);
		return null;
	}
}
