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

import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.Pair;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroup;
import com.jaspersoft.studio.components.crosstab.model.rowgroup.MRowGroups;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateRowCommand extends Command {

	private JRDesignCrosstabRowGroup jrGroup;
	private JRDesignCrosstab jrCrosstab;

	private int index;
	private JasperDesign jasperDesign;

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
	public CreateRowCommand(MRowGroups destNode, MRowGroup srcNode, int index) {
		this((ANode) destNode, srcNode, index);
	}

	public CreateRowCommand(MCrosstab destNode, MRowGroup srcNode, int index) {
		this((ANode) destNode, srcNode, index);
	}

	public CreateRowCommand(MRowGroup destNode, MRowGroup srcNode, int index) {
		this(destNode.getMCrosstab(), srcNode, index);
	}

	public CreateRowCommand(MCell destNode, MRowGroup srcNode, int index) {
		this(destNode.getMCrosstab(), srcNode, index);
	}

	private CreateRowCommand(ANode destNode, MRowGroup srcNode, int index) {
		super();
		this.jrCrosstab = (JRDesignCrosstab) destNode.getValue();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrGroup = (JRDesignCrosstabRowGroup) srcNode.getValue();
		jasperDesign = destNode.getJasperDesign();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrGroup == null) {
			jrGroup = createRowGroup(jasperDesign, jrCrosstab,
					Messages.CreateRowGroupCommand_row_group,
					CrosstabTotalPositionEnum.END);
		}
		if (jrGroup != null) {
			try {
				addRowGroup(jrCrosstab, jrGroup, index);
			} catch (JRException e) {
				e.printStackTrace();
				if (e.getMessage()
						.startsWith(
								"A group or measure having the same name already exists in the crosstab.")) { //$NON-NLS-1$
					String defaultName = ModelUtils.getDefaultName(
							jrCrosstab.getRowGroupIndicesMap(),
							"CopyOFRowGroup_"); //$NON-NLS-1$
					InputDialog dlg = new InputDialog(
							Display.getDefault().getActiveShell(),
							Messages.CreateRowGroupCommand_row_group_name,
							Messages.CreateRowGroupCommand_row_group_dialog_text,
							defaultName, null);
					if (dlg.open() == InputDialog.OK) {
						jrGroup.setName(dlg.getValue());
						execute();
					}
				}
			}
		}
	}

	public static JRDesignCrosstabRowGroup createRowGroup(
			JasperDesign jasperDesign, JRDesignCrosstab jrCrosstab,
			String name, CrosstabTotalPositionEnum total) {
		JRDesignCrosstabRowGroup jrGroup = new JRDesignCrosstabRowGroup();
		jrGroup.setTotalPosition(total);
		jrGroup.setName(ModelUtils.getDefaultName(jrCrosstab, name));
		jrGroup.setWidth(60);

		JRDesignExpression exp = new JRDesignExpression();
		exp.setText(""); //$NON-NLS-1$
		exp.setValueClass(String.class);
		JRDesignCrosstabBucket bucket = new JRDesignCrosstabBucket();
		bucket.setExpression(exp);
		jrGroup.setBucket(bucket);

		JRDesignCellContents headerCell = new JRDesignCellContents();
		jrGroup.setHeader(headerCell);

		exp = new JRDesignExpression();
		exp.setText("$V{" + jrGroup.getName() + "}"); //$NON-NLS-1$ //$NON-NLS-2$

		JRDesignTextField tf = (JRDesignTextField) new MTextField()
				.createJRElement(jasperDesign);
		tf.setX(0);
		tf.setY(0);
		tf.setWidth(jrGroup.getWidth());
		tf.setHeight(20);
		if ("Crosstab Data Text" != null && jasperDesign.getStylesMap().containsKey("Crosstab Data Text")) { //$NON-NLS-1$ //$NON-NLS-2$
			tf.setStyle((JRStyle) jasperDesign.getStylesMap().get(
					"Crosstab Data Text")); //$NON-NLS-1$
		}
		tf.setExpression(exp);

		headerCell.addElement(tf); // NOI18N
		JRDesignCellContents totalCell = new JRDesignCellContents();
		JRDesignStaticText stext = new JRDesignStaticText();
		stext.setX(0);
		stext.setY(0);
		stext.setWidth(jrGroup.getWidth());
		stext.setHeight(20);
		stext.setText(Messages.common_total + " " + jrGroup.getName()); //$NON-NLS-1$
		totalCell.addElement(stext);
		jrGroup.setTotalHeader(totalCell);
		return jrGroup;
	}

	public static void addRowGroup(JRDesignCrosstab jrCross,
			JRDesignCrosstabRowGroup jrRowGr, int index) throws JRException {
		if (index >= 0 && index < jrCross.getRowGroupsList().size())
			jrCross.addRowGroup(index, jrRowGr);
		else
			jrCross.addRowGroup(jrRowGr);

		if (!jrCross.getCellsMap().containsKey(
				new Pair<String, String>(null, null))) {
			JRDesignCrosstabCell dT = new JRDesignCrosstabCell();
			dT.setColumnTotalGroup(null);
			dT.setRowTotalGroup(null);
			jrCross.addCell(dT);
			dT.setHeight(20);
			dT.setWidth(jrRowGr.getWidth());
		}

		JRDesignCrosstabCell dT = new JRDesignCrosstabCell();
		dT.setRowTotalGroup(jrRowGr.getName());
		jrCross.addCell(dT);
		dT.setHeight(20);
		dT.setWidth(jrRowGr.getWidth());

		List<JRCrosstabColumnGroup> columns = jrCross.getColumnGroupsList();
		if (columns != null)
			for (JRCrosstabColumnGroup c : columns) {
				JRDesignCrosstabCell cell = new JRDesignCrosstabCell();
				cell.setRowTotalGroup(jrRowGr.getName());
				cell.setColumnTotalGroup(c.getName());
				jrCross.addCell(cell);
				cell.setHeight(c.getHeight());
				cell.setWidth(jrRowGr.getWidth());
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
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		DeleteRowGroupCommand.removeRowGroup(jrCrosstab, jrGroup);
	}
}
