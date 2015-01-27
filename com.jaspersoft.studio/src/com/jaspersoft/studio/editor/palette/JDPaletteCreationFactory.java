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
package com.jaspersoft.studio.editor.palette;

import org.eclipse.gef.requests.CreationFactory;

/*
 * A factory for creating JDPaletteCreation objects.
 * 
 * @author Chicu Veaceslav
 */
public class JDPaletteCreationFactory implements CreationFactory {

	/** The template. */
	private Object template;

	/**
	 * Instantiates a new jD palette creation factory.
	 * 
	 * @param t
	 *          the t
	 */
	public JDPaletteCreationFactory(Object t) {
		this.template = t;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getNewObject()
	 */
	public Object getNewObject() {
		if (template == null)
			return null;
		try {
			if (template instanceof Class) {
				return ((Class<?>) template).newInstance();
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
		return template;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.requests.CreationFactory#getObjectType()
	 */
	public Object getObjectType() {
		return template;
	}

}
