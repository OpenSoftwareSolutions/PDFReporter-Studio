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
package com.jaspersoft.studio.properties.view;

/**
 * Allows property sheet page contributors to map the input object type to a
 * domain model type. The domain model type is then used for matching against
 * the input attribute of the propertySection extension.
 * <p>
 * The type mapper is most frequently used to return the type of the model
 * object when selecting a view object in a workbench part. For example, nodes
 * in a tree may all be TreeObjects in a structured selection. The type mapper
 * will take the tree node and return the type of the model object the node
 * represents.
 * </p>
 * <p>
 * This interface should not be extended or implemented. New type mapper instances
 * should be created using <code>AbstractTypeMapper</code>.
 * </p>
 * 
 * @author Anthony Hunter
 */
public interface ITypeMapper {

	/**
	 * Map the input object to a domain model type.
	 * 
	 * @param object
	 *            the input object.
	 * @return the type of the input object, mapped to the domain type if
	 *         required.
	 */
	public Class<?> mapType(Object object);
}
