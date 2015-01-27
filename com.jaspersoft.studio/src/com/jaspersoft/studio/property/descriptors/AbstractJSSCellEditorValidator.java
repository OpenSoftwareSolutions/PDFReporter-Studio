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
package com.jaspersoft.studio.property.descriptors;

import org.eclipse.jface.viewers.ICellEditorValidator;

import com.jaspersoft.studio.model.APropertyNode;

/**
 * This class want to be a validator for property node but with the possibility to set the target
 * of the validation in any moment
 * 
 * @author Orlandin Marco
 *
 */
public abstract class AbstractJSSCellEditorValidator implements ICellEditorValidator {

	/**
	 * Target node of the validation
	 */
	protected APropertyNode targetNode;
	
	/**
	 * Set the target
	 * @param target the new target
	 */
	public void setTargetNode(APropertyNode target){
		this.targetNode = target;
	}
	
	/**
	 * return the target
	 * @return the actual target
	 */
	public APropertyNode getTarget(){
		return targetNode;
	}
	
	
}
