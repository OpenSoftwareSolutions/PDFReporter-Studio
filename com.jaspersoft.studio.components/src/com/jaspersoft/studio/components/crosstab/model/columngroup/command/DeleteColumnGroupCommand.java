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
package com.jaspersoft.studio.components.crosstab.model.columngroup.command;

import java.util.List;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.crosstab.model.columngroup.MColumnGroups;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteColumnGroupCommand extends Command {

	private JRDesignCrosstab jrCrosstab;
	private JRDesignCrosstabColumnGroup jrColumnGroup;

	/** The element position. */
	private int index = 0;

	/**
	 * Instantiates a new delete parameter command.
	 * 
	 * @param destNode
	 *            the dest node
	 * @param srcNode
	 *            the src node
	 */
	public DeleteColumnGroupCommand(MColumnGroups destNode, MColumnGroup srcNode) {
		super();
		this.jrCrosstab = (JRDesignCrosstab) destNode.getValue();
		this.jrColumnGroup = (JRDesignCrosstabColumnGroup) srcNode.getValue();
	}

	public DeleteColumnGroupCommand(MCrosstab destNode, MColumnGroup srcNode) {
		super();
		this.jrCrosstab = destNode.getValue();
		this.jrColumnGroup = (JRDesignCrosstabColumnGroup) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		index = jrCrosstab.getColumnGroupsList().indexOf(jrColumnGroup);
		removeColumnGroup(jrCrosstab, jrColumnGroup);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrCrosstab == null || jrColumnGroup == null)
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
		try {
			if (index >= 0 && index < jrCrosstab.getColumnGroupsList().size())
				jrCrosstab.addColumnGroup(index, jrColumnGroup);
			else
				jrCrosstab.addColumnGroup(jrColumnGroup);
			// jrCrosstab.addParameter(elementPosition, jrParameter);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	public static void removeColumnGroup(JRDesignCrosstab jrCross,
			JRDesignCrosstabColumnGroup jrRowGr) {
		jrCross.removeColumnGroup(jrRowGr);

		List<?> cells = jrCross.getCellsList();

		String name = jrRowGr.getName();

		for (int i = 0; i < cells.size(); ++i) {
			JRDesignCrosstabCell cell = (JRDesignCrosstabCell) cells.get(i);
			if (cell != null) {
				String totalGroup = cell.getRowTotalGroup();
				if (totalGroup != null && totalGroup.equals(name)) {
					jrCross.removeCell(cell);
					i--;
				}
			}
		}
		jrCross.preprocess();
	}
}
