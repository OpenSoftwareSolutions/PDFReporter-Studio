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
package com.jaspersoft.studio.runtime.adapters;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;

import com.jaspersoft.studio.editor.gef.parts.AJDEditPart;

/**
 * Adapter factory for the AJDEditPart subclasses.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JDEditPartAdapterFactory implements IAdapterFactory {

	/** The list of provided adapters. */
	private static final Class<?>[] ADAPTER_LIST= new Class[] { IResource.class, IFile.class };
	
	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		if (IResource.class.equals(adapterType)
				&& adaptableObject instanceof AJDEditPart) {
			return ((AJDEditPart) adaptableObject).getAdapter(IResource.class);
		}
		if (IFile.class.equals(adapterType)
				&& adaptableObject instanceof AJDEditPart) {
			return ((AJDEditPart) adaptableObject).getAdapter(IFile.class);
		}
		return null;
	}

	@Override
	public Class<?>[] getAdapterList() {
		return ADAPTER_LIST;
	}

}
