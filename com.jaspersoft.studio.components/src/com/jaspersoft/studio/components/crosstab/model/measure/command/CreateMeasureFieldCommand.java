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
package com.jaspersoft.studio.components.crosstab.model.measure.command;

import net.sf.jasperreports.crosstabs.JRCrosstabDataset;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.components.crosstab.model.measure.MMeasure;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.text.MTextField;

/**
 * Create a field for a measure inside a cell of a crosstab
 * 
 * @author Orlandin Marco
 *
 */
public class CreateMeasureFieldCommand extends Command {
	
	/**
	 * Model of the measure
	 */
	protected MMeasure child;
	
	/**
	 * Cell where the element is placed
	 */
	protected MCell parent;
	
	/**
	 * Dataset of the crosstab
	 */
	protected JRDesignDataset jDataset;
	
	/**
	 * New textfield created
	 */
	protected JRDesignTextField newElement = null;
	
	/**
	 * Drop location
	 */
	protected Rectangle location;

	/**
	 * Create the command
	 * 
	 * @param child measure dragged by the user
	 * @param parent cell where the measure should be placed
	 * @param location position of the drop
	 */
	public CreateMeasureFieldCommand(MMeasure child, MCell parent, Rectangle location) {
		JasperDesign jd = parent.getJasperDesign();
		jDataset = jd.getMainDesignDataset();
		JRCrosstabDataset d = parent.getMCrosstab().getValue().getDataset();
		JRDesignDatasetRun dr = (JRDesignDatasetRun) d.getDatasetRun();
		if (dr != null) {
			String dbname = dr.getDatasetName();
			if (dbname != null)
				jDataset = (JRDesignDataset) jd.getDatasetMap().get(dbname);
		}
		this.child = child;
		this.parent = parent;
		this.location = location;
	}

	/**
	 * Set the element position and location
	 * 
	 * @param jrElement the JRTextField created inside the cell
	 * @param gElement An MTextField, used to take the default field size
	 */
	protected void setElementBounds(JRDesignElement jrElement, MGraphicElement gElement) {
		if (location == null)
			location = new Rectangle(0, 0, parent.getDefaultWidth(),
					parent.getDefaultHeight());
		if (location.width < 0)
			location.width = gElement.getDefaultWidth();
		if (location.height < 0)
			location.height = gElement.getDefaultHeight();
		
		jrElement.setX(location.x);
		jrElement.setY(location.y);
		jrElement.setWidth(location.width);
		jrElement.setHeight(location.height);
	}
	
	@Override
	public void execute() {
		MTextField mElement = new MTextField();
		JRDesignTextField textElement =  mElement.createJRElement(parent.getJasperDesign());
		String measureName = "$V{"+(String)child.getPropertyActualValue(JRDesignCrosstabMeasure.PROPERTY_NAME)+"}";
		textElement.setExpression(new JRDesignExpression(measureName));
		newElement = textElement;
		parent.getValue().addElement(textElement);
		setElementBounds(textElement,mElement);
	}
	
	@Override
	public boolean canUndo() {
		return (newElement!= null);
	}

	@Override
	public void undo() {
		if (newElement != null){
			parent.getValue().removeElement(newElement);
			newElement = null;
		}
	}
}

