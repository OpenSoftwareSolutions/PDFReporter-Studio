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
package com.jaspersoft.studio.model;

import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Rectangle;
/*
 * The Interface IGraphicElement.
 * 
 * @author Chicu Veaceslav
 */
public interface IGraphicElement {

	/**
	 * Gets the bounds.
	 * 
	 * @return the bounds
	 */
	public Rectangle getBounds();

	/**
	 * Gets the default width.
	 * 
	 * @return the default width
	 */
	public int getDefaultWidth();

	/**
	 * Gets the default height.
	 * 
	 * @return the default height
	 */
	public int getDefaultHeight();

	/**
	 * Creates the jr element.
	 * 
	 * @param jasperDesign
	 *          the jasper design
	 * @return the jR design element
	 */
	public abstract JRDesignElement createJRElement(JasperDesign jasperDesign);
}
