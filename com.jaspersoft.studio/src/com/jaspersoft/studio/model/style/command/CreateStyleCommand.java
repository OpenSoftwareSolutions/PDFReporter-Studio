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
package com.jaspersoft.studio.model.style.command;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.model.style.MStyles;
import com.jaspersoft.studio.utils.ModelUtils;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateStyleCommand extends Command {

	/** The jr style. */
	private JRDesignStyle jrStyle;

	/** The jr design. */
	private JasperDesign jrDesign;

	/** The index. */
	private int index;

	/**
	 * Instantiates a new creates the style command to append a new style at the
	 * end of the list
	 * 
	 * @param jd
	 *          the jasper design on which we are operating
	 * @param style
	 *          the style to add
	 */
	public CreateStyleCommand(JasperDesign jd, JRDesignStyle style) {
		super();
		this.jrDesign = jd;
		this.jrStyle = style;
		this.index = -1;
	}
	
	
	/**
	 * Instantiates a new creates the style command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateStyleCommand(MStyles destNode, MStyle srcNode, int index) {
		super();
		this.jrDesign = destNode.getJasperDesign();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrStyle = (JRDesignStyle) srcNode.getValue().clone();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrStyle == null) {
			this.jrStyle = MStyle.createJRStyle(jrDesign);
		}
		if (jrStyle != null) {
			try {
				if (index < 0 || index > jrDesign.getStylesList().size())
					jrDesign.addStyle(jrStyle);
				else
					jrDesign.addStyle(index, jrStyle);
			} catch (JRException e) {
				e.printStackTrace();
				if (e.getMessage().startsWith("Duplicate declaration")) { //$NON-NLS-1$
					String defaultName = ModelUtils.getDefaultName(jrDesign.getStylesMap(), "CopyOf_"+jrStyle.getName()); //$NON-NLS-1$
					InputDialog dlg = new InputDialog(Display.getCurrent().getActiveShell(),
							Messages.CreateStyleCommand_style_name, Messages.CreateStyleCommand_style_name_dialog_text, defaultName,
							null);
					if (dlg.open() == InputDialog.OK) {
						jrStyle.setName(dlg.getValue());
						execute();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrDesign.removeStyle(jrStyle);
	}
}
