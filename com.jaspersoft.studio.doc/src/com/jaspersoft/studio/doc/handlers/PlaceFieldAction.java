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
package com.jaspersoft.studio.doc.handlers;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.command.CreateE4ObjectCommand;
import com.jaspersoft.studio.model.field.MField;

public class PlaceFieldAction extends Action {

	@Override
	public void run() {
		MField field = (MField)HandlersUtil.getRootFields().getChildren().get(0);
		MBand band = (MBand)HandlersUtil.getBand();
		CreateE4ObjectCommand addField = new CreateE4ObjectCommand(field, band, new Rectangle(200, 200, -1, -1), -1);
		addField.execute();
	}
}
