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
package com.jaspersoft.studio.editor.action.pdf;


import org.eclipse.ui.IWorkbenchPart;

public class PdfActionTable extends PdfActionAbstact {
	
	/** Id of the actions */
	public static final String ID_Table_Full = "PdfAction_Table_Full"; //$NON-NLS-1$
	public static final String ID_Table_Start = "PdfAction_Table_Start"; //$NON-NLS-1$
	public static final String ID_Table_End = "PdfAction_Table_End"; //$NON-NLS-1$
	public static final String ID_Table_None = "PdfAction_Table_None"; //$NON-NLS-1$
	
	
	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 * @param action_position
	 * 					Identify The position of the label
	 */
	public PdfActionTable(IWorkbenchPart part,Position action_position) {
		super(part, action_position, ID_Table_Full, ID_Table_Start, ID_Table_End, ID_Table_None);
	}

	/**
	 * method to return the property name 
	 * @return Property for which one the value must be changed
	 */
	protected String GetPropertyName(){
		return "net.sf.jasperreports.export.pdf.tag.table";
	}

}
