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
package com.jaspersoft.studio.community.zip;

/**
 * Bean describing an entry that will be put in the final ZIP attached to the
 * new issue created.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ZipEntry {

	private String name;
	private String location;
	private ZipEntryType type;

	public ZipEntry() {
	}
	
	public ZipEntry(String name, String location, ZipEntryType type) {
		super();
		this.name = name;
		this.location = location;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public ZipEntryType getType() {
		return type;
	}

	public void setType(ZipEntryType type) {
		this.type = type;
	}

}
