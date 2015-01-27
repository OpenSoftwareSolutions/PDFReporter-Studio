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
package com.jaspersoft.studio.components.table.part.editpolicy;

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.part.TableCellEditPart;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.property.SetValueCommand;

public class CreateResize {
	public static Command createResizeCommand(ChangeBoundsRequest request,
			TableCellEditPart editPart) {
		MColumn model = editPart.getModel();
		MColumn oldmodel = model;
		Dimension sd = request.getSizeDelta();
		ANode mparent = model.getParent();
		if (request.getResizeDirection() == PositionConstants.WEST) {
			int index = mparent.getChildren().indexOf(model);
			if (index > 0)
				model = (MColumn) mparent.getChildren().get(index - 1);
			else
				return null;
		}
		if (request.getResizeDirection() == PositionConstants.NORTH) {
			model = model.getNorth();
			if (model == null)
				return null;
		}
		StandardBaseColumn jrdesign = model.getValue();
		PrecisionRectangle deltaRect = new PrecisionRectangle(new Rectangle(0,
				0, sd.width, sd.height));
		editPart.getFigure().translateToRelative(deltaRect);
		JSSCompoundCommand c = new JSSCompoundCommand("Change Cell Size", model); //$NON-NLS-1$
		if (request.getSizeDelta().width != 0) {
			int w = deltaRect.width;
			if (request.getResizeDirection() == PositionConstants.WEST)
				w = -w;
			int width = jrdesign.getWidth() + w;
			if (width < 0)
				return null;

			SetValueCommand setCommand = new SetValueCommand();
			setCommand.setTarget(model);
			setCommand.setPropertyId(StandardBaseColumn.PROPERTY_WIDTH);
			setCommand.setPropertyValue(width);
			c.add(setCommand);

			if (request.getResizeDirection() == PositionConstants.WEST) {
				jrdesign = oldmodel.getValue();
				w = deltaRect.width;
				width = jrdesign.getWidth() + w;
				if (width < 0)
					return null;
				setCommand = new SetValueCommand();
				setCommand.setTarget(oldmodel);
				setCommand.setPropertyId(StandardBaseColumn.PROPERTY_WIDTH);
				setCommand.setPropertyValue(width);
				c.add(setCommand);
			}
		}
		if (request.getSizeDelta().height != 0 && model instanceof MColumn) {
			MColumn mc = (MColumn) model;
			int h = deltaRect.height;
			if (request.getResizeDirection() == PositionConstants.NORTH)
				h = -h;
			int height = (Integer) mc
					.getPropertyValue(DesignCell.PROPERTY_HEIGHT) + h;
			if (height < 0)
				return null;

			SetValueCommand setCommand = new SetValueCommand();
			setCommand.setTarget(model);
			setCommand.setPropertyId(DesignCell.PROPERTY_HEIGHT);
			setCommand.setPropertyValue(height);
			c.add(setCommand);

			if (request.getResizeDirection() == PositionConstants.NORTH
					&& oldmodel instanceof MColumn) {
				mc = (MColumn) oldmodel;
				h = deltaRect.height;
				height = (Integer) mc
						.getPropertyValue(DesignCell.PROPERTY_HEIGHT) + h;
				if (height < 0)
					height = 0;

				setCommand = new SetValueCommand();
				setCommand.setTarget(oldmodel);
				setCommand.setPropertyId(DesignCell.PROPERTY_HEIGHT);
				setCommand.setPropertyValue(height);
				c.add(setCommand);
			}
		}
		if (c.isEmpty())
			return null;
		return c;
	}
}
