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
package com.jaspersoft.studio.model.image.command;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignImage;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.DialogEnabledCommand;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.image.command.dialog.ImageCreationDialog;

public class CreateImageCommand extends CreateElementCommand implements DialogEnabledCommand{

	private JRDesignExpression imageExpression;

	public CreateImageCommand(ANode destNode, MGraphicElement srcNode, Rectangle position, int index) {
		super(destNode, srcNode, position, index);
	}
	
	public CreateImageCommand(MBand destNode, MGraphicElement srcNode, int index) {
		super(destNode, srcNode, index);
	}

	public CreateImageCommand(MElementGroup destNode, MGraphicElement srcNode, int index) {
		super(destNode, srcNode, index);
	}

	public CreateImageCommand(MFrame destNode, MGraphicElement srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Creates the object.
	 */
	@Override
	protected void createObject() {
		if (getJrElement() == null) {
			if (srcNode.getValue() == null)
				jrElement = srcNode.createJRElement(jasperDesign);
			else
				jrElement = (JRDesignElement) srcNode.getValue();

			if (jrElement != null)
				setElementBounds();

			// NOTE: #createObject() is invoked during executeCommand that is
			// supposed to be called only when the ImageCreationDialog has been
			// opened and closed successfully.
			// So if we are here, the user has already chosen how to set
			// the image expression correctly.
			((JRDesignImage)jrElement).setExpression(imageExpression);
		}
	}

	@Override
	public int openDialog() {
		ImageCreationDialog d = new ImageCreationDialog(UIUtils.getShell());
		d.configureDialog(jConfig);
		int dialogResult = d.open();
		imageExpression = d.getImageExpression();
		return dialogResult;
	}
	
	public void setImageExpression(JRDesignExpression imageExpression){
		this.imageExpression=imageExpression;
	}
}
