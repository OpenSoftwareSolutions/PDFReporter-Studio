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
package com.jaspersoft.studio.editor.action;

import java.util.List;

import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.command.ReorderBandCommandByRelativeIndex;

/**
 * Action to move a detail before the detail band above it it
 * 
 * @author Orlandin Marco
 * 
 */
public class MoveDetailUpAction extends ACachedSelectionAction implements IGlobalAction {

	/** The Constant ID. */
	public static final String ID = "move_detail_up"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public MoveDetailUpAction(IWorkbenchPart part) {
		super(part);
	}

	
	@Override
	protected Command createCommand() {
		List<Object> bands = editor.getSelectionCache().getSelectionModelForType(MBand.class);
		if (bands.isEmpty()) return null;
		MBand firstBand = (MBand)bands.get(0);
		JSSCompoundCommand cmd = new JSSCompoundCommand(firstBand);
		for(Object obj : bands){
			MBand bandNode = (MBand)obj;
			if ( bandNode.getBandType() == BandTypeEnum.GROUP_FOOTER || bandNode.getBandType() == BandTypeEnum.GROUP_HEADER){
					cmd.add(new ReorderBandCommandByRelativeIndex((MBandGroupFooter) bandNode, -1));
			} else if (bandNode.getBandType() == BandTypeEnum.DETAIL) {
				cmd.add(new ReorderBandCommandByRelativeIndex(bandNode, (MReport) bandNode.getParent(), -1));
			}
		}
		return cmd.isEmpty() ? null : cmd;
	}
	
	@Override
	public boolean calculateEnabled() {
		return super.calculateEnabled();
	}
	
	
	/**
	 * Initializes this action's text and images.
	 */
	@Override
	protected void init() {
		super.init();
		setText(Messages.MoveDetailUpAction_actionName);
		setToolTipText(Messages.MoveDetailUpAction_actionDescription);
		setId(MoveDetailUpAction.ID);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/arrow-curve-up.png")); //$NON-NLS-1$
		setEnabled(false);
	}

}
