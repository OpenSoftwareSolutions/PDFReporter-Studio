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
package com.jaspersoft.studio.editor.gef.parts;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;

import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.utils.SelectionHelper;

/*
 * The Class AJDEditPart.
 * 
 * @author Chicu Veaceslav
 */
public abstract class AJDEditPart extends AbstractGraphicalEditPart {

	/**
	 * Gets the model node.
	 * 
	 * @return the model node
	 */
	public INode getModelNode() {
		return (INode) getModel();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	@Override
	public void activate() {
		super.activate();

		// ANode node = (ANode) getModel();
		// node.getPropertyChangeSupport().addPropertyChangeListener((PropertyChangeListener) this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	@Override
	public void deactivate() {
		super.deactivate();
		// ANode node = (ANode) getModel();
		// node.getPropertyChangeSupport().removePropertyChangeListener((PropertyChangeListener) this);
	}

	@Override
	public Object getAdapter(Class key) {
		if (key == IResource.class || key == IFile.class) {
			if (associatedFile == null) {
				associatedFile = getAssociatedFile();
			}
			return associatedFile;
		}
		return super.getAdapter(key);
	}

	private IResource associatedFile;

	/**
	 * Returns the file associated.
	 * <p>
	 * Given the current edit part belonging to the active JRXML editor (report designer) the related file is returned.
	 * 
	 * @return the associated file resource
	 */
	public IResource getAssociatedFile() {
		IEditorInput edinput = null;
		if (getViewer() != null && getViewer().getEditDomain() instanceof DefaultEditDomain) {
			IEditorPart ip = ((DefaultEditDomain) getViewer().getEditDomain()).getEditorPart();
			edinput = ip.getEditorInput();
		} else {
			IEditorPart ep = SelectionHelper.getActiveJRXMLEditor();
			if (ep != null)
				edinput = ep.getEditorInput();
		}
		if (edinput instanceof IFileEditorInput) {
			return ((IFileEditorInput) edinput).getFile();
		}
		return null;
	}
}
