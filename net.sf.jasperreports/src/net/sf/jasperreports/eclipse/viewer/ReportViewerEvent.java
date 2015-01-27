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
package net.sf.jasperreports.eclipse.viewer;

import java.util.EventObject;

public class ReportViewerEvent extends EventObject {

	private static final long serialVersionUID = 3397562694482011109L;

	/**
	 * Constructs the event with the specified event source
	 * 
	 * @param source
	 *          the source of the event
	 */
	public ReportViewerEvent(Object source) {
		super(source);
	}

}
