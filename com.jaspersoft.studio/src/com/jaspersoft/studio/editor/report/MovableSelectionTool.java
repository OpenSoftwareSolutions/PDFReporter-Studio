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
package com.jaspersoft.studio.editor.report;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Handle;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.tools.SelectionTool;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.parts.JSSGraphicalViewerKeyHandler;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.UIUtil;

/**
 * This selection tool should be used in order to enable the movements on
 * the primary-selected {@link EditPart} simply using the keyboard arrows,
 * without the need to iterate through all the available {@link Handle handles}
 * via the PERIOD keystroke.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @deprecated No longer used, we now replaced this feature using the dedicated 
 * {@link JSSGraphicalViewerKeyHandler} on the graphical viewer
 * 
 */
public class MovableSelectionTool extends SelectionTool {
	
	@Override
	protected boolean handleKeyDown(KeyEvent e) {
		if (isInState(STATE_INITIAL) && UIUtil.isArrowKey(e.keyCode)) {
			EditPartViewer viewer = getCurrentViewer();
			if (viewer instanceof GraphicalViewer) {
				JSSCompoundCommand ccmd = new JSSCompoundCommand(null);
				for(Object selectedEditPart : getCurrentViewer().getSelectedEditParts()) {
					if (selectedEditPart instanceof GraphicalEditPart) {
						Object modelObj = ((EditPart) selectedEditPart).getModel();
						if(modelObj instanceof MGraphicElement) {
							MGraphicElement node = (MGraphicElement) modelObj;
							ccmd.add(getNewXYCommand(e.keyCode,node));
						}
					}
				}
				if(!ccmd.isEmpty()) {
					getDomain().getCommandStack().execute(ccmd);
					return true;
				}	
			}
		}
		return super.handleKeyDown(e);
	}
	
	/*
	 * Gets a new command that modify the x or y coordinate depending on the 
	 * arrow key pressed.
	 * Standard movement is 1px. If SHIFT key is also pressed 10px is the step.
	 */
	private Command getNewXYCommand(int arrowKeyCode, MGraphicElement node) {
		int step=1;
		if (JasperReportsPlugin.isPressed(SWT.SHIFT)) step = 10;
		Integer x = (Integer) node.getPropertyValue(JRDesignElement.PROPERTY_X);
		Integer y = (Integer) node.getPropertyValue(JRDesignElement.PROPERTY_Y);
		SetValueCommand newXYCmd = new SetValueCommand();
		newXYCmd.setTarget(node);
		switch (arrowKeyCode) {
		case SWT.ARROW_UP:
			newXYCmd.setPropertyId(JRDesignElement.PROPERTY_Y);
			newXYCmd.setPropertyValue(y-step);
			break;
		case SWT.ARROW_DOWN:
			newXYCmd.setPropertyId(JRDesignElement.PROPERTY_Y);
			newXYCmd.setPropertyValue(y+step);
			break;
		case SWT.ARROW_LEFT:
			newXYCmd.setPropertyId(JRDesignElement.PROPERTY_X);
			newXYCmd.setPropertyValue(x-step);
			break;
		case SWT.ARROW_RIGHT:
			newXYCmd.setPropertyId(JRDesignElement.PROPERTY_X);
			newXYCmd.setPropertyValue(x+step);
			break;
		default:
			throw new RuntimeException("Only arrow keys can be accepted!");
		}
		return newXYCmd;
	}
	
}
