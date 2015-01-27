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
package com.jaspersoft.studio.editor.gef.commands;

import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.editor.action.size.Size2BorderAction;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IGraphicElementContainer;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.band.MBand;

public class ResizeCommand extends Command {
	private int alignement;
	private Dimension parent;
	private JRDesignElement jrElement;

	private int oldX, oldY, oldWidth, oldHeight;

	public ResizeCommand(int alignement, EditPart editPart) {
		this(alignement,(MGraphicElement) editPart.getModel());
		
	}
	
	public ResizeCommand(int alignement, MGraphicElement m) {
		super();
		this.alignement = alignement;
		jrElement = (JRDesignElement) m.getValue();
		INode n = m.getParent();
		//Get the real parent of the element if it's inside a subeditor
		if (n instanceof MPage){
			MPage page = (MPage)n;
			n = page.getRealParent();
		}
		if (n instanceof IContainer) {
			if (n instanceof MBand) {
				// height of band, width of Report - margins
				JRDesignBand band = (JRDesignBand) ((MBand) n).getValue();
				int h = band.getHeight();
				JasperDesign jasperDesign = m.getJasperDesign();
				int w = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();
				parent = new Dimension(w, h);
			} else if (n instanceof IGraphicElementContainer)
				parent = ((IGraphicElementContainer) n).getSize();
		}
	}

	@Override
	public void execute() {
		oldX = jrElement.getX();
		oldY = jrElement.getY();
		oldWidth = jrElement.getWidth();
		oldHeight = jrElement.getHeight();

		int newX = oldX;
		int newY = oldY;
		int newWidth = oldWidth;
		int newHeight = oldHeight;
		switch (alignement) {
		case Size2BorderAction.WIDTH:
			newX = 0;
			newWidth = parent.width;
			break;
		case Size2BorderAction.HEIGHT:
			newY = 0;
			newHeight = parent.height;
			break;
		case Size2BorderAction.BOTH:
			newX = 0;
			newY = 0;
			newWidth = parent.width;
			newHeight = parent.height;
			break;
		}
		jrElement.setX(newX);
		jrElement.setY(newY);
		jrElement.setWidth(newWidth);
		jrElement.setHeight(newHeight);
	}

	@Override
	public void undo() {
		jrElement.setX(oldX);
		jrElement.setY(oldY);
		jrElement.setWidth(oldWidth);
		jrElement.setHeight(oldHeight);
	}
}
