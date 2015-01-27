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
package com.jaspersoft.studio.property;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
/*
 * TextChangeHelper notifies the listner of text lifecycle events on behalf of the widget(s) it listens to.
 * 
 */
public abstract class TextChangeHelper implements Listener {

	private boolean nonUserChange;

	/**
	 * Marks the start of a programmatic change to the widget contents. Clients must call startNonUserChange() before
	 * directly setting the widget contents to avoid unwanted lifecycle events.
	 * 
	 * @throws IllegalArgumentException
	 *           if a programmatic change is already in progress.
	 */
	public void startNonUserChange() {
		if (nonUserChange)
			throw new IllegalStateException("we already started a non user change");//$NON-NLS-1$
		nonUserChange = true;
	}

	/**
	 * Clients who call startNonUserChange() should call finishNonUserChange() as soon as possible after the change is
	 * done.
	 * 
	 * @throws IllegalArgumentException
	 *           if no change is in progress.
	 */
	public void finishNonUserChange() {
		if (!nonUserChange)
			throw new IllegalStateException("we are not in a non user change");//$NON-NLS-1$
		nonUserChange = false;
	}

	/**
	 * Returns true if a programmatic change is in progress.
	 */
	public boolean isNonUserChange() {
		return nonUserChange;
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.KeyDown:
			if (event.character == SWT.CR)
				textChanged((Control) event.widget);
			break;
		case SWT.FocusOut:
			textChanged((Control) event.widget);
			break;
		}
	}

	/**
	 * Abstract method notified when a text field has been changed.
	 * 
	 * @param control
	 */
	public abstract void textChanged(Control control);

	/**
	 * Registers this helper with the given control to listen for events which indicate that a change is in progress (or
	 * done).
	 */
	public void startListeningTo(Control control) {
		control.addListener(SWT.FocusOut, this);
		control.addListener(SWT.Modify, this);
	}

	/**
	 * Registers this helper with the given control to listen for the Enter key. When Enter is pressed, the change is
	 * considered done (this is only appropriate for single-line Text widgets).
	 */
	public void startListeningForEnter(Control control) {
		// NOTE: KeyDown rather than KeyUp, because of similar usage in CCombo.
		control.addListener(SWT.KeyDown, this);
	}

	/**
	 * Unregisters this helper from a control previously passed to startListeningTo() and/or startListeningForEnter().
	 */
	public void stopListeningTo(Control control) {
		control.removeListener(SWT.FocusOut, this);
		control.removeListener(SWT.Modify, this);
		control.removeListener(SWT.KeyDown, this);
	}
}
