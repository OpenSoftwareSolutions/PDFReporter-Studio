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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.editor.gef.commands.SetConstraintCommand;
import com.jaspersoft.studio.editor.gef.rulers.ReportRulerGuide;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGuidebleElement;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.utils.ModelUtils;
/*
 * The Class MoveGuideCommand.
 * 
 * @author Chicu Veaceslav
 */
public class MoveGuideCommand extends Command {
	private List<SetConstraintCommand> constraintCommands;
	/** The p delta. */
	private int pDelta;

	/** The guide. */
	private ReportRulerGuide guide;

	/**
	 * Instantiates a new move guide command.
	 * 
	 * @param guide
	 *          the guide
	 * @param positionDelta
	 *          the position delta
	 */
	public MoveGuideCommand(ReportRulerGuide guide, int positionDelta) {
		super(Messages.MoveGuideCommand_move_guide);
		this.guide = guide;
		pDelta = positionDelta;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		if (constraintCommands == null) {
			constraintCommands = new ArrayList<SetConstraintCommand>();
			for (IGuidebleElement part : guide.getParts()) {
				ANode node = (ANode) part;
				if (node.getParent() == null)
					continue;
				if (node instanceof MGraphicElement) {
					JRDesignElement jrE = (JRDesignElement) node.getValue();
					Point location = ModelUtils.getY4Element((MGraphicElement) node);
					if (guide.isHorizontal()) {
						location.y += pDelta;
					} else {
						location.x += pDelta;
					}
					SetConstraintCommand cc = new SetConstraintCommand();
					cc.setContext(null, node, new Rectangle(location.x, location.y, jrE.getWidth(), jrE.getHeight()));
					constraintCommands.add(cc);
				}
			}
		}
		guide.setPosition(guide.getPosition() + pDelta);
		for (SetConstraintCommand c : constraintCommands)
			c.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		guide.setPosition(guide.getPosition() - pDelta);
		for (SetConstraintCommand c : constraintCommands)
			c.undo();
	}

}
