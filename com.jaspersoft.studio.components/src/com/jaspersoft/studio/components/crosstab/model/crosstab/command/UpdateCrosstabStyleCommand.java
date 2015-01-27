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
package com.jaspersoft.studio.components.crosstab.model.crosstab.command;
import java.util.ArrayList;
import java.util.Arrays;

import net.sf.jasperreports.engine.design.JRDesignStyle;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.dialog.ApplyCrosstabStyleAction;
import com.jaspersoft.studio.components.crosstab.model.dialog.CrosstabStyle;

/**
 * The command to update the CrosstabStyle of a Crosstab, support the undo
 * 
 * @author Orlandin Marco
 *
 */
public class UpdateCrosstabStyleCommand extends Command{
	
	/**
	 * The model of the Crosstab
	 */
	private MCrosstab crosstab;
	
	/**
	 * The styles of the Crosstab before the change
	 */
	private JRDesignStyle[] oldStyles;
	
	/**
	 * The new styles
	 */
	private CrosstabStyle newStyleTemplate;
	
	/**
	 * True if the new styles will overwrite the old ones, false if the old ones will keep and 
	 * the new ones will have a different name
	 */
	private boolean updateOldStyles;
	
	/**
	 * Create the command to change the CrosstabStyle of a Crosstab
	 * 
	 * @param crosstab The model of the Crosstab
	 * @param newStyle The new styles to apply to the Crosstab
	 * @param updateOldStyles True if the new styles will overwrite the old ones, false if the old ones will keep and 
	 * the new ones will have a different name and the Table element will use these new ones
	 */
	public UpdateCrosstabStyleCommand(MCrosstab crosstab, CrosstabStyle newStyle, boolean updateOldStyles){
		this.crosstab = crosstab;
		this.newStyleTemplate = newStyle;
		oldStyles = null;
		this.updateOldStyles = updateOldStyles;
	}
	

	@Override
	public void execute() {
		ApplyCrosstabStyleAction applyAction = new ApplyCrosstabStyleAction(newStyleTemplate, crosstab.getValue()); 
		//Save the old style
		oldStyles = applyAction.getStylesFromCrosstab();
		//Apply the new style, the old one if not overwritten are not removed
		applyAction.updateStyle(crosstab.getJasperDesign(), newStyleTemplate, updateOldStyles, false);
		crosstab.setChangedProperty(true);
	}
	
	@Override
	public void undo() {
		ArrayList<JRDesignStyle> styles =  new ArrayList<JRDesignStyle>(Arrays.asList(oldStyles));
		ApplyCrosstabStyleAction applyAction = new ApplyCrosstabStyleAction(styles, crosstab.getValue()); 
		//Restore the new style, if the update has created new styles they will be also removed
		applyAction.updateStyle(crosstab.getJasperDesign(), styles, false, true);
		oldStyles = null;
		crosstab.setChangedProperty(true);
	}
	
	/**
	 * Undo is available if the Crosstab and the styles previous the update are available 
	 */
	@Override
	public boolean canUndo() {
		return (crosstab != null && oldStyles != null);
	}

}
