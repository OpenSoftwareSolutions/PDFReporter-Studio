/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.editor;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.FileEditorInput;

public class DeltaVisitor implements IResourceDeltaVisitor {
	private EditorPart part;

	public DeltaVisitor(EditorPart part) {
		this.part = part;
	}

	public boolean visit(IResourceDelta delta) {
		if (delta == null || delta.getResource() == null || part == null || part.getEditorInput() == null
				|| (((IFileEditorInput) part.getEditorInput()).getFile()) == null)
			return true;
		if (!delta.getResource().equals((((IFileEditorInput) part.getEditorInput()).getFile())))
			return true;
		switch (delta.getKind()) {
		case IResourceDelta.ADDED:
			break;
		case IResourceDelta.REMOVED:
			if ((IResourceDelta.MOVED_TO & delta.getFlags()) == 0) {
				// file removed
				UIUtils.getDisplay().asyncExec(new Runnable() {
					public void run() {
						part.getSite().getPage().closeEditor(part, false);
					}
				});
			} else
				changeInput(delta.getMovedToPath());
			break;
		case IResourceDelta.CHANGED:
			if ((delta.getFlags() & IResourceDelta.CONTENT) != 0 || (delta.getFlags() & IResourceDelta.SYNC) != 0
					|| (delta.getFlags() & IResourceDelta.REPLACED) != 0)
				changeInput(delta.getFullPath());
			break;
		}
		return true;
	}

	private void changeInput(IPath toPath) {
		final IFile newFile = ResourcesPlugin.getWorkspace().getRoot().getFile(toPath);
		UIUtils.getDisplay().syncExec(new Runnable() {
			public void run() {
				try {
					ISelectionProvider selectionProvider = part.getSite().getSelectionProvider();
					part.init(part.getEditorSite(), new FileEditorInput(newFile));
					if (selectionProvider != null)
						part.getSite().setSelectionProvider(selectionProvider);
				} catch (PartInitException e) {
					UIUtils.showError(e);
				}
			}
		});
	}
}
