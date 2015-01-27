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
package com.jaspersoft.studio.components.crosstab.model.measure.command;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabMeasure;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.components.crosstab.model.measure.MMeasure;
import com.jaspersoft.studio.components.crosstab.model.measure.MMeasures;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateMeasureCommand extends Command {

	/** The jr parameter. */
	private JRDesignCrosstabMeasure jrMeasure;

	/** The jr dataset. */
	private JRDesignCrosstab jrCrosstab;

	/** The index. */
	private int index;

	/**
	 * Instantiates a new creates the parameter command.
	 * 
	 * @param destNode
	 *            the dest node
	 * @param srcNode
	 *            the src node
	 * @param position
	 *            the position
	 * @param index
	 *            the index
	 */
	public CreateMeasureCommand(MMeasures destNode, MMeasure srcNode, int index) {
		this((ANode) destNode, srcNode, index);
	}

	public CreateMeasureCommand(MCrosstab destNode, MMeasure srcNode, int index) {
		this((ANode) destNode, srcNode, index);
	}

	public CreateMeasureCommand(MCell destNode, MMeasure srcNode, int index) {
		this(destNode.getMCrosstab(), srcNode, index);
	}

	private CreateMeasureCommand(ANode destNode, MMeasure srcNode, int index) {
		super();
		this.jrCrosstab = (JRDesignCrosstab) destNode.getValue();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrMeasure = (JRDesignCrosstabMeasure) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrMeasure == null) {
			jrMeasure = createMesure(jrCrosstab,
					Messages.CreateMeasureCommand_measure);
		}
		if (jrMeasure != null) {
			try {
				if (index >= 0 && index < jrCrosstab.getMesuresList().size())
					jrCrosstab.addMeasure(index, jrMeasure);
				else
					jrCrosstab.addMeasure(jrMeasure);
				// jrCrosstab.addParameter(index, jrParameter);
			} catch (JRException e) {
				e.printStackTrace();
				if (e.getMessage()
						.startsWith(
								"A group or measure having the same name already exists in the crosstab")) { //$NON-NLS-1$
					String defaultName = ModelUtils
							.getDefaultName(jrCrosstab.getMeasureIndicesMap(),
									"CopyOFMeasure_"); //$NON-NLS-1$
					InputDialog dlg = new InputDialog(Display.getDefault()
							.getActiveShell(),
							Messages.CreateMeasureCommand_parameter_name,
							Messages.CreateMeasureCommand_dialog_text,
							defaultName, null);
					if (dlg.open() == InputDialog.OK) {
						jrMeasure.setName(dlg.getValue());
						execute();
					}
				}
			}
		}
	}

	public static JRDesignCrosstabMeasure createMesure(
			JRDesignCrosstab jrCrosstab, String name) {
		JRDesignCrosstabMeasure jrMeasure = new JRDesignCrosstabMeasure();
		jrMeasure.setName(ModelUtils.getDefaultName(jrCrosstab, name));
		return jrMeasure;
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
		jrCrosstab.removeMeasure(jrMeasure);
	}
}
