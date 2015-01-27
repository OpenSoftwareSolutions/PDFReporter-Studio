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
package net.sf.jasperreports.eclipse.util.xml;

/*
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: SourceLocation.java 22 2007-03-08 15:18:26Z lucianc $
 */
public class SourceLocation
{

	private int lineNumber;
	private int columnNumber;
	private String xPath;
	
	public SourceLocation()
	{
	}

	
	public int getColumnNumber()
	{
		return columnNumber;
	}

	
	public void setColumnNumber(int columnNumber)
	{
		this.columnNumber = columnNumber;
	}

	
	public int getLineNumber()
	{
		return lineNumber;
	}

	
	public void setLineNumber(int lineNumber)
	{
		this.lineNumber = lineNumber;
	}

	
	public String getXPath()
	{
		return xPath;
	}

	
	public void setXPath(String path)
	{
		xPath = path;
	}
	
}
