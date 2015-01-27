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
package com.jaspersoft.studio.components.table.model.cell.command;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.ColumnGroup;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.model.column.MCell;
import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutCommand;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.utils.SelectionHelper;

public class CreateElementCommand extends Command {
	protected MGraphicElement srcNode;
	protected JRDesignElement jrElement;
	private StandardBaseColumn jrColumn;
	private StandardTable sTable;
	private JasperDesign jDesign;
	private DesignCell jrCell;

	private Rectangle location;

	protected int index = -1;
	private JRPropertiesHolder[] pholder;

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *            the dest node
	 * @param srcNode
	 *            the src node
	 * @param index
	 *            the index
	 */
	public CreateElementCommand(MCell destNode, MGraphicElement srcNode,
			Rectangle position, int index) {
		super();
		if (srcNode != null)
			this.jrElement = (JRDesignElement) srcNode.getValue();
		this.jrCell = destNode.getCell();
		this.index = index;
		this.location = position;
		this.srcNode = srcNode;
		sTable = destNode.getTable().getStandardTable();
		this.jrColumn = (StandardBaseColumn) destNode.getValue();
		jDesign = destNode.getJasperDesign();
		pholder = ((IContainerLayout) destNode).getPropertyHolder();
	}

	/**
	 * Creates the object.
	 */
	protected void createObject() {
		if (jrElement == null) {
			jrElement = srcNode.createJRElement(srcNode.getJasperDesign());
		}
		if (jrElement != null) {
			if (location == null)
				location = new Rectangle(
						0,
						0,
						Math.min(srcNode.getDefaultWidth(), jrColumn.getWidth()),
						Math.min(srcNode.getDefaultHeight(), jrCell.getHeight()));
			if (location.width < 0)
				location.width = srcNode.getDefaultWidth();
			if (location.height < 0)
				location.height = srcNode.getDefaultHeight();

		}

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

		Dimension d = new Dimension(jrColumn.getWidth(), jrCell.getHeight());
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
		com.jaspersoft.studio.model.command.CreateElementCommand.removeElement(
				jDesign, jrElement);
		removeElementFromColumn(sTable.getColumns());
	}

	public void removeElementFromColumn(List<BaseColumn> cols) {
		for (BaseColumn bc : cols) {
			Cell cell = bc.getTableHeader();
			if (cell != null)
				com.jaspersoft.studio.model.command.CreateElementCommand
						.removeElement(jrElement, cell.getElements());

			cell = bc.getTableFooter();
			if (cell != null)
				com.jaspersoft.studio.model.command.CreateElementCommand
						.removeElement(jrElement, cell.getElements());

			cell = bc.getColumnHeader();
			if (cell != null)
				com.jaspersoft.studio.model.command.CreateElementCommand
						.removeElement(jrElement, cell.getElements());

			cell = bc.getColumnFooter();
			if (cell != null)
				com.jaspersoft.studio.model.command.CreateElementCommand
						.removeElement(jrElement, cell.getElements());

			for (GroupCell gc : bc.getGroupHeaders()) {
				cell = gc.getCell();
				if (cell != null)
					com.jaspersoft.studio.model.command.CreateElementCommand
							.removeElement(jrElement, cell.getElements());
			}
			for (GroupCell gc : bc.getGroupFooters()) {
				cell = gc.getCell();
				if (cell != null)
					com.jaspersoft.studio.model.command.CreateElementCommand
							.removeElement(jrElement, cell.getElements());
			}
			if (bc instanceof ColumnGroup)
				removeElementFromColumn(((ColumnGroup) bc).getColumns());
		}
	}

}
