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

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroup;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroups;
/*
 * The Class ReorderParameterCommand.
 */
public class ReorderRowGroupCommand extends Command {

	/** The new index. */
	private int oldIndex, newIndex;

	/** The jr parameter. */
	private JRDesignCrosstabRowGroup jrRowGroup;

	/** The jr dataset. */
	private JRDesignCrosstab jrCrosstab;

	/**
	 * Instantiates a new reorder parameter command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderRowGroupCommand(MRowGroup child, MRowGroups parent, int newIndex) {
		super(Messages.common_reorder_elements);

		this.newIndex = newIndex;
		this.jrCrosstab = (JRDesignCrosstab) parent.getValue();
		this.jrRowGroup = (JRDesignCrosstabRowGroup) child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = jrCrosstab.getRowGroupsList().indexOf(jrRowGroup);

		jrCrosstab.getRowGroupsList().remove(jrRowGroup);
		jrCrosstab.getEventSupport().fireCollectionElementRemovedEvent(JRDesignCrosstab.PROPERTY_ROW_GROUPS, jrRowGroup,
				oldIndex);
		if (newIndex >= 0 && newIndex < jrCrosstab.getRowGroupsList().size())
			jrCrosstab.getRowGroupsList().add(newIndex, jrRowGroup);
		else
			jrCrosstab.getRowGroupsList().add(jrRowGroup);
		jrCrosstab.getEventSupport().fireCollectionElementAddedEvent(JRDesignCrosstab.PROPERTY_ROW_GROUPS, jrRowGroup,
				newIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {

		jrCrosstab.getRowGroupsList().remove(jrRowGroup);
		jrCrosstab.getEventSupport().fireCollectionElementRemovedEvent(JRDesignCrosstab.PROPERTY_ROW_GROUPS, jrRowGroup,
				newIndex);
		if (oldIndex >= 0 && oldIndex < jrCrosstab.getRowGroupsList().size())
			jrCrosstab.getRowGroupsList().add(oldIndex, jrRowGroup);
		else
			jrCrosstab.getRowGroupsList().add(jrRowGroup);
		jrCrosstab.getEventSupport().fireCollectionElementAddedEvent(JRDesignCrosstab.PROPERTY_ROW_GROUPS, jrRowGroup,
				oldIndex);
	}
}
