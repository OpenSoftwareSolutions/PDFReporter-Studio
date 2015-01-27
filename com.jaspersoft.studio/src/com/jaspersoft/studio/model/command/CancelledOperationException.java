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
package com.jaspersoft.studio.model.command;

/**
 * 
 * This exception could be raised during the execution of a command to  interrupt
 * the command itself. Essentially it is only a marker of a simple exception
 * 
 * @author Orlandin Marco
 *
 */
public class CancelledOperationException extends Exception {
	
	private static final long serialVersionUID = 677645812987466762L;

	public CancelledOperationException(){
		super("cancelled");
	}
}
