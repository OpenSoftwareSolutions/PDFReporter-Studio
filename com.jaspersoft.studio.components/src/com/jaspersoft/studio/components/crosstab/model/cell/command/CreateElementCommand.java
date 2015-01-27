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
package com.jaspersoft.studio.components.crosstab.model.cell.command;

import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutCommand;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.utils.SelectionHelper;

public class CreateElementCommand extends Command {
	protected MGraphicElement srcNode;
	protected JRDesignElement jrElement;
	private JasperDesign jDesign;
	private JRDesignCellContents jrCell;
	private JRDesignCrosstab crosstab;

	private Rectangle location;

	private int index = -1;
	private JRPropertiesHolder[] pholder;

	public CreateElementCommand(MCell destNode, MGraphicElement srcNode, Rectangle position, int index) {
		super();
		if (srcNode != null)
			this.jrElement = (JRDesignElement) srcNode.getValue();
		this.jrCell = (JRDesignCellContents) destNode.getValue();
		this.index = index;
		this.location = position;
		this.srcNode = srcNode;
		jDesign = destNode.getJasperDesign();
		pholder = ((IContainerLayout) destNode).getPropertyHolder();
		crosstab = destNode.getCrosstab().getValue();
	}

	/**
	 * Creates the object.
	 */
	protected void createObject() {
		if (jrElement == null)
			jrElement = srcNode.createJRElement(srcNode.getJasperDesign());

		setElementBounds();
	}

	protected void setElementBounds() {
		if (location == null)
			location = new Rectangle(0, 0, srcNode.getDefaultWidth(), srcNode.getDefaultHeight());
		if (location.width < 0)
			location.width = srcNode.getDefaultWidth();
		if (location.height < 0)
			location.height = srcNode.getDefaultHeight();

		jrElement.setX(location.x);
		jrElement.setY(location.y);
		jrElement.setWidth(location.width);
		jrElement.setHeight(location.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrElement != null) {
			removeElements(jrElement);
			if (index >= 0 && index <= jrCell.getChildren().size())
				jrCell.addElement(index, jrElement);
			else
				jrCell.addElement(jrElement);
		}
		if (firstTime) {
			SelectionHelper.setSelection(jrElement, false);
			firstTime = false;
		}
		Dimension d = new Dimension(jrCell.getWidth(), jrCell.getHeight());
		if (lCmd == null) {
			ILayout layout = LayoutManager.getLayout(pholder, jDesign, null);
			lCmd = new LayoutCommand(jrCell, layout, d);
		}
		lCmd.execute();
	}

	private LayoutCommand lCmd;
	private boolean firstTime = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrCell == null || jrElement == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		lCmd.undo();
		jrCell.removeElement(jrElement);
	}

	private void removeElements(JRDesignElement element) {
		com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(jDesign, jrElement);
		for (JRCrosstabCell c : crosstab.getCellsList()) {
			com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(element, c.getContents().getElements());
		}

		for (JRCrosstabRowGroup rg : crosstab.getRowGroupsList()) {
			com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(element, rg.getHeader().getElements());
			if (rg.getTotalHeader() != null)
				com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(element, rg.getTotalHeader().getElements());
		}
		for (JRCrosstabColumnGroup rc : crosstab.getColumnGroupsList()) {
			com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(element, rc.getHeader().getElements());
			if (rc.getTotalHeader() != null)
				com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(element, rc.getTotalHeader().getElements());
		}
	}
}
