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
package com.jaspersoft.studio.components.crosstab.model.rowgroup.command;

import java.util.List;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroup;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroups;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteRowGroupCommand extends Command {

	private JRDesignCrosstab jrCrosstab;
	private JRDesignCrosstabRowGroup jrRowGroup;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete parameter command.
	 * 
	 * @param destNode
	 *            the dest node
	 * @param srcNode
	 *            the src node
	 */
	public DeleteRowGroupCommand(MRowGroups destNode, MRowGroup srcNode) {
		super();
		this.jrCrosstab = (JRDesignCrosstab) destNode.getValue();
		this.jrRowGroup = (JRDesignCrosstabRowGroup) srcNode.getValue();
	}

	public DeleteRowGroupCommand(MCrosstab destNode, MRowGroup srcNode) {
		super();
		this.jrCrosstab = destNode.getValue();
		this.jrRowGroup = (JRDesignCrosstabRowGroup) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrCrosstab.getRowGroupsList().indexOf(jrRowGroup);
		removeRowGroup(jrCrosstab, jrRowGroup);

	}

	public static void removeRowGroup(JRDesignCrosstab jrCross,
			JRDesignCrosstabRowGroup jrRowGr) {
		String name = jrRowGr.getName();

		List<?> cells = jrCross.getCellsList();
		jrCross.removeRowGroup(jrRowGr);

		for (int i = 0; i < cells.size(); ++i) {
			JRDesignCrosstabCell cell = (JRDesignCrosstabCell) cells.get(i);
			if (cell != null) {
				String totalGroup = cell.getColumnTotalGroup();
				if (totalGroup != null && totalGroup.equals(name)) {
					jrCross.removeCell(cell);
					i--;
				}
			}
		}

		jrCross.preprocess();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrCrosstab == null || jrRowGroup == null)
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
			CreateRowCommand.addRowGroup(jrCrosstab, jrRowGroup,
					elementPosition);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
