/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.model.command;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.components.customvisualization.model.MCustomVisualization;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.frame.MFrame;

/**
 * Create command for the Custom Visualization component element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CreateCustomVisualizationCommand extends CreateElementCommand {

	public CreateCustomVisualizationCommand(ANode destNode, MGraphicElement srcNode,
			Rectangle position, int index) {
		super(destNode, srcNode, position, index);
	}

	public CreateCustomVisualizationCommand(MBand destNode, MGraphicElement srcNode,
			int index) {
		super(destNode, srcNode, index);
	}

	public CreateCustomVisualizationCommand(MElementGroup destNode, MGraphicElement srcNode,
			int index) {
		super(destNode, srcNode, index);
	}

	public CreateCustomVisualizationCommand(MFrame destNode, MGraphicElement srcNode,
			int index) {
		super(destNode, srcNode, index);
	}

	public CreateCustomVisualizationCommand(MFrame destNode, MGraphicElement srcNode,
			Rectangle position, int index) {
		super(destNode, srcNode, position, index);
	}

	@Override
	protected void createObject() {
		if (jrElement == null) {
			srcNode = new MCustomVisualization();
			jrElement = srcNode.createJRElement(jasperDesign);
		}
		if (jrElement != null) {
			setElementBounds();
		}
	}
}
