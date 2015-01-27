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
package com.jaspersoft.studio.server.dnd;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jface.util.TransferDropTargetListener;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.widgets.TreeItem;

import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.utils.Misc;

/**
 * A target drop listener that creates a generic file resource element when something
 * is dropped on the JRS repository tree.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class RepositoryFileResourceDropTargetListener implements TransferDropTargetListener {
	
	private Transfer transfer;
	
	public RepositoryFileResourceDropTargetListener(Transfer transfer){
		this.transfer = transfer;
	}

	@Override
	public void dragEnter(DropTargetEvent event) {
		// Do nothing
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		// Do nothing
	}

	@Override
	public void dragOperationChanged(DropTargetEvent event) {

	}

	@Override
	public void dragOver(DropTargetEvent event) {
		// select, expand and scroll the tree node
		event.feedback = DND.FEEDBACK_SELECT | DND.FEEDBACK_EXPAND | DND.FEEDBACK_SCROLL;
	}

	@Override
	public void drop(DropTargetEvent event) {
		// Drop only the allowed items
		String[] items = (String[])event.data;
		for(String item : items){
			String fileExt = Misc.nvl(FilenameUtils.getExtension(item).toLowerCase());
			if(RepositoryDNDHelper.isDropOperationAllowed(fileExt) && 
					event.item!=null && event.item.getData() instanceof MResource && 
					event.data instanceof String[]){
				MResource targetParentResource = (MResource) event.item.getData();
				RepositoryDNDHelper.performDropOperation(targetParentResource, item);
			} 
		}
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// Do nothing
	}

	@Override
	public Transfer getTransfer() {
		return this.transfer;
	}

	@Override
	public boolean isEnabled(DropTargetEvent event) {
		if(event.item instanceof TreeItem){
			TreeItem item = (TreeItem) event.item;
			if(item.getData() instanceof MFolder ||
					item.getData() instanceof MReportUnit) {
				return true;
			}
		}
		return false;
	}

}
