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
package com.jaspersoft.studio.plugin;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

public class PaletteGroup {
	private String id;
	private String name;
	private ImageDescriptor image;
	private String afterGroup;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ImageDescriptor getImage() {
		return image;
	}

	public void setImage(ImageDescriptor image) {
		this.image = image;
	}

	public void setImage(String image) {
		if (image != null && !image.trim().isEmpty())
			setImage(JaspersoftStudioPlugin.getInstance().getImageDescriptor(image));
		else
			setImage(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/elementgroup-16.png"));
	}

	public String getAfterGroup() {
		return afterGroup;
	}

	public void setAfterGroup(String afterGroup) {
		this.afterGroup = afterGroup;
	}
}
