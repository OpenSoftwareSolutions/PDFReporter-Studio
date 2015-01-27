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
package com.jaspersoft.studio.data.sql.model.query;

import java.util.UUID;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.data.sql.model.MSQLRoot;
import com.jaspersoft.studio.data.sql.text2model.ConvertUtil;
import com.jaspersoft.studio.model.AMapElement;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public abstract class AMQueryObject<T> extends AMapElement implements IQueryString {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	transient private ImageDescriptor icon;
	private String image;
	protected String tooltip;
	private String id;

	public AMQueryObject(ANode parent, T value, String image) {
		this(parent, value, image, -1);
	}

	public AMQueryObject(ANode parent, T value, String image, int index) {
		super(parent, index);
		setValue(value);
		this.image = image;
		id = UUID.randomUUID().toString();
		if (image != null)
			icon = JasperReportsPlugin.getDefault().getImageDescriptor(image);
	}

	@Override
	public MSQLRoot getRoot() {
		return (MSQLRoot) super.getRoot();
	}

	public String getId() {
		return id;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T getValue() {
		return (T) super.getValue();
	}

	@Override
	public String getToolTip() {
		if (getValue() instanceof INode) {
			String name = ((INode) getValue()).getToolTip();
			if (tooltip != null)
				name += "\n" + tooltip;
			return name;
		} else if (getValue() instanceof String)
			return (String) getValue();
		return null;
	}

	@Override
	public ImageDescriptor getImagePath() {
		if (icon == null && image != null)
			icon = JasperReportsPlugin.getDefault().getImageDescriptor(image);
		return icon;
	}

	@Override
	public String getDisplayText() {
		if (getValue() instanceof IQueryString)
			return ConvertUtil.cleanDbNameFull(((IQueryString) getValue()).toSQLString());
		else if (getValue() instanceof String)
			return (String) getValue();
		return null;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		return obj.getClass().equals(getClass()) && ((AMQueryObject<?>) obj).getId().equals(getId());
	}

	public int hashCode() {
		return getId().hashCode();
	};

	@Override
	public String toSQLString() {
		String sql = "";
		if (getValue() instanceof IQueryString)
			sql = ((IQueryString) getValue()).toSQLString();
		else if (getValue() instanceof String)
			sql = (String) getValue();
		return (isFirst() ? sql : ",\n\t" + sql);
	}
}
