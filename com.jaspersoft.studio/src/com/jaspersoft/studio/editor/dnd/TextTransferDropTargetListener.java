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
package com.jaspersoft.studio.editor.dnd;

import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.swt.dnd.Transfer;
/*
 * The listener interface for receiving textTransferDropTarget events. The class that is interested in processing a
 * textTransferDropTarget event implements this interface, and the object created with that class is registered with a
 * component using the component's <code>addTextTransferDropTargetListener<code> method. When
 * the textTransferDropTarget event occurs, that object's appropriate
 * method is invoked.
 * 
 * @see TextTransferDropTargetEvent
 */
public class TextTransferDropTargetListener extends AbstractTransferDropTargetListener {

	/**
	 * Instantiates a new text transfer drop target listener.
	 * 
	 * @param viewer
	 *          the viewer
	 * @param xfer
	 *          the xfer
	 */
	public TextTransferDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#createTargetRequest()
	 */
	protected Request createTargetRequest() {
		return new NativeDropRequest();
	}

	/**
	 * Gets the native drop request.
	 * 
	 * @return the native drop request
	 */
	protected NativeDropRequest getNativeDropRequest() {
		return (NativeDropRequest) getTargetRequest();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetRequest()
	 */
	protected void updateTargetRequest() {
		getNativeDropRequest().setData(getCurrentEvent().data);
	}

}
