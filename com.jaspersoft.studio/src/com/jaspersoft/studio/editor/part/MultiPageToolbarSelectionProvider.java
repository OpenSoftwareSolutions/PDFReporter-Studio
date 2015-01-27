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
package com.jaspersoft.studio.editor.part;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.ListenerList;
import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.viewers.IPostSelectionProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;

public class MultiPageToolbarSelectionProvider implements IPostSelectionProvider {

	/**
	 * Registered selection changed listeners (element type: <code>ISelectionChangedListener</code>).
	 */
	private ListenerList listeners = new ListenerList();

	/**
	 * Registered post selection changed listeners.
	 */
	private ListenerList postListeners = new ListenerList();

	/**
	 * The multi-page editor.
	 */
	private MultiPageToolbarEditorPart multiPageEditor;

	/**
	 * Creates a selection provider for the given multi-page editor.
	 * 
	 * @param multiPageEditor
	 *          the multi-page editor
	 */
	public MultiPageToolbarSelectionProvider(MultiPageToolbarEditorPart multiPageEditor) {
		Assert.isNotNull(multiPageEditor);
		this.multiPageEditor = multiPageEditor;
	}

	/*
	 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
	 */
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.add(listener);
	}

	/**
	 * Adds a listener for post selection changes in this multi page selection provider.
	 * 
	 * @param listener
	 *          a selection changed listener
	 * @since 3.2
	 */
	public void addPostSelectionChangedListener(ISelectionChangedListener listener) {
		postListeners.add(listener);
	}

	/**
	 * Notifies all registered selection changed listeners that the editor's selection has changed. Only listeners
	 * registered at the time this method is called are notified.
	 * 
	 * @param event
	 *          the selection changed event
	 */
	public void fireSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = this.listeners.getListeners();
		fireEventChange(event, listeners);
	}

	/**
	 * Notifies all post selection changed listeners that the editor's selection has changed.
	 * 
	 * @param event
	 *          the event to propogate.
	 * @since 3.2
	 */
	public void firePostSelectionChanged(final SelectionChangedEvent event) {
		Object[] listeners = postListeners.getListeners();
		fireEventChange(event, listeners);
	}

	private void fireEventChange(final SelectionChangedEvent event, Object[] listeners) {
		for (int i = 0; i < listeners.length; ++i) {
			final ISelectionChangedListener l = (ISelectionChangedListener) listeners[i];
			SafeRunner.run(new SafeRunnable() {
				public void run() {
					l.selectionChanged(event);
				}
			});
		}
	}

	/**
	 * Returns the multi-page editor.
	 * 
	 * @return the multi-page editor.
	 */
	public MultiPageToolbarEditorPart getMultiPageEditor() {
		return multiPageEditor;
	}

	/*
	 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
	 */
	public ISelection getSelection() {
		IEditorPart activeEditor = multiPageEditor.getActiveEditor();
		if (activeEditor != null) {
			ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
			if (selectionProvider != null) {
				return selectionProvider.getSelection();
			}
		}
		return StructuredSelection.EMPTY;
	}

	/*
	 * (non-JavaDoc) Method declaed on <code>ISelectionProvider</code>.
	 */
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Removes a listener for post selection changes in this multi page selection provider.
	 * 
	 * @param listener
	 *          a selection changed listener
	 * @since 3.2
	 */
	public void removePostSelectionChangedListener(ISelectionChangedListener listener) {
		postListeners.remove(listener);
	}

	/*
	 * (non-Javadoc) Method declared on <code>ISelectionProvider</code>.
	 */
	public void setSelection(ISelection selection) {
		IEditorPart activeEditor = multiPageEditor.getActiveEditor();
		if (activeEditor != null) {
			ISelectionProvider selectionProvider = activeEditor.getSite().getSelectionProvider();
			if (selectionProvider != null) {
				selectionProvider.setSelection(selection);
			}
		}
	}
}
