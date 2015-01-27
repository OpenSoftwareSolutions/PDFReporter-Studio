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
package com.jaspersoft.studio.undo;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.operations.AbstractOperation;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.jaspersoft.studio.messages.Messages;
/*/*
 * The Class PageWidthUndoableOperation.
 */
public class PageWidthUndoableOperation extends AbstractOperation {

	/** The jd. */
	JasperDesign jd=null;
	
	/** The old width. */
	int oldWidth=0;
	
	/** The new width. */
	int newWidth=0;
	
	/**
	 * Instantiates a new page width undoable operation.
	 * 
	 * @param jd
	 *          the jd
	 * @param oldWidth
	 *          the old width
	 * @param newWidth
	 *          the new width
	 */
	public PageWidthUndoableOperation(JasperDesign jd, int oldWidth, int newWidth)
	{
		super(Messages.PageWidthUndoableOperation_change_page_width);
		this.jd=jd;
		this.oldWidth=oldWidth;
		this.newWidth=newWidth;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.operations.AbstractOperation#execute(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public IStatus execute(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		jd.setPageWidth(newWidth);
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.operations.AbstractOperation#redo(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public IStatus redo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		// TODO Auto-generated method stub
		jd.setPageWidth(newWidth);
		return Status.OK_STATUS;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.operations.AbstractOperation#undo(org.eclipse.core.runtime.IProgressMonitor, org.eclipse.core.runtime.IAdaptable)
	 */
	@Override
	public IStatus undo(IProgressMonitor monitor, IAdaptable info)
			throws ExecutionException {
		// TODO Auto-generated method stub
		jd.setPageWidth(oldWidth);
		return Status.OK_STATUS;
	}


}
