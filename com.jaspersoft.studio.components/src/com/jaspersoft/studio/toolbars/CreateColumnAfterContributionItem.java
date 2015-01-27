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
package com.jaspersoft.studio.toolbars;

import com.jaspersoft.studio.components.table.model.column.MCell;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnAfterAction;

/**
 * Create the toolbar button to add a column to the selected table
 * 
 * @author Orlandin Marco
 *
 */
public class CreateColumnAfterContributionItem extends CreateColumnContributionItem {

	/**
	 * Action that will be executed to add the column
	 */
	@Override
	protected CreateColumnAction getAction(){ 
		return new CreateColumnAfterAction(null);
	}
	
	@Override
	public boolean isVisible() {
		if (!super.isVisible()) return false;
		if (getSelectionForType(MCell.class).size() == 1){
			setEnablement();
			return true;
		}
		return false;
	}

}
