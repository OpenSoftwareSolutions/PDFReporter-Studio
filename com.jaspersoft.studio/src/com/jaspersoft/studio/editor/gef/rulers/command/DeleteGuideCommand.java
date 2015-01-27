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
package com.jaspersoft.studio.editor.gef.rulers.command;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.editor.gef.rulers.ReportRuler;
import com.jaspersoft.studio.editor.gef.rulers.ReportRulerGuide;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.IGuidebleElement;
/*
 * The Class DeleteGuideCommand.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteGuideCommand extends Command {

	/** The parent. */
	private ReportRuler parent;

	/** The guide. */
	private ReportRulerGuide guide;

	/** The old parts. */
	private Map<IGuidebleElement, Integer> oldParts;

	/**
	 * Instantiates a new delete guide command.
	 * 
	 * @param guide
	 *          the guide
	 * @param parent
	 *          the parent
	 */
	public DeleteGuideCommand(ReportRulerGuide guide, ReportRuler parent) {
		super(Messages.DeleteGuideCommand_delete_guide);
		this.guide = guide;
		this.parent = parent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		oldParts = new HashMap<IGuidebleElement, Integer>(guide.getMap());
		for (IGuidebleElement part : guide.getParts()) {
			guide.detachPart(part);
		}
		parent.removeGuide(guide);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		parent.addGuide(guide);
		for (IGuidebleElement part : guide.getParts()) {
			guide.attachPart(part, (oldParts.get(part)).intValue());
		}
	}
}
