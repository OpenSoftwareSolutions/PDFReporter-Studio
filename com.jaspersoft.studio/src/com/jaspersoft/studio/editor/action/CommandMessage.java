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
package com.jaspersoft.studio.editor.action;

import org.eclipse.gef.commands.Command;

/**
 * A custom message that can be associated to a {@link Command}.
 * <p>
 * 
 * It can become handy in different situation when we want to specify besides a custom 
 * human readable text also if it is warning, error or simply an info message.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see MessageProviderCommand
 *
 */
public class CommandMessage {

	public enum Status {
		INFO,WARNING,ERROR
	}
	
	private Status status;
	private String message;
	
	public CommandMessage(Status status, String message){
		this.status=status;
		this.message=message;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getMessage() {
		return message;
	}
	
	
}
