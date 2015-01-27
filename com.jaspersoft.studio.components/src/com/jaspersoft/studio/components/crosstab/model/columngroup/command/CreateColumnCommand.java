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
package com.jaspersoft.studio.components.crosstab.model.columngroup.command;

import java.util.List;

import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.util.Pair;

import org.eclipse.gef.commands.Command;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.components.crosstab.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.crosstab.model.columngroup.MColumnGroups;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateColumnCommand extends Command {

	private JRDesignCrosstabColumnGroup jrGroup;

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
	public CreateColumnCommand(MColumnGroups destNode, MColumnGroup srcNode,
			int index) {
		this((ANode) destNode, srcNode, index);
	}

	public CreateColumnCommand(MColumnGroup destNode, MColumnGroup srcNode,
			int index) {
		this(destNode.getMCrosstab(), srcNode, index);
	}

	public CreateColumnCommand(MCell destNode, MColumnGroup srcNode, int index) {
		this(destNode.getMCrosstab(), srcNode, index);
	}

	public CreateColumnCommand(MCrosstab destNode, MColumnGroup srcNode,
			int index) {
		this((ANode) destNode, srcNode, index);
	}

	private CreateColumnCommand(ANode destNode, MColumnGroup srcNode, int index) {
		super();
		this.jrCrosstab = (JRDesignCrosstab) destNode.getValue();
		this.index = index;
		if (srcNode != null && srcNode.getValue() != null)
			this.jrGroup = (JRDesignCrosstabColumnGroup) srcNode.getValue();
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
			jrGroup = createColumnGroup(jasperDesign, jrCrosstab,
					Messages.CreateColumnGroupCommand_column_group,
					CrosstabTotalPositionEnum.END);
		}
		if (jrGroup != null) {
			try {

				addColumnGroup(jrCrosstab, jrGroup, index);

			} catch (JRException e) {
				e.printStackTrace();
				if (e.getMessage()
						.startsWith(
								"A group or measure having the same name already exists in the crosstab.")) { //$NON-NLS-1$
					String defaultName = ModelUtils.getDefaultName(
							jrCrosstab.getColumnGroupIndicesMap(),
							"CopyOFColumnGroup_"); //$NON-NLS-1$
					InputDialog dlg = new InputDialog(
							Display.getDefault().getActiveShell(),
							Messages.CreateColumnGroupCommand_column_group_name,
							Messages.CreateColumnGroupCommand_column_group_name_dialog,
							defaultName, null);
					if (dlg.open() == InputDialog.OK) {
						jrGroup.setName(dlg.getValue());
						execute();
					}
				}
			}
		}
		jrCrosstab.getCellsList();
	}

	public static JRDesignCrosstabColumnGroup createColumnGroup(
			JasperDesign jasperDesign, JRDesignCrosstab jrCrosstab,
			String name, CrosstabTotalPositionEnum total) {
		JRDesignCrosstabColumnGroup jrGroup = new JRDesignCrosstabColumnGroup();
		jrGroup.setTotalPosition(total);
		jrGroup.setName(ModelUtils.getDefaultName(jrCrosstab, name));
		jrGroup.setHeight(20);

		JRDesignExpression exp = new JRDesignExpression();
		exp.setText(""); //$NON-NLS-1$
		exp.setValueClass(String.class);
		JRDesignCrosstabBucket bucket = new JRDesignCrosstabBucket();
		bucket.setExpression(exp);
		bucket.setOrder(SortOrderEnum.ASCENDING);
		jrGroup.setBucket(bucket);

		JRDesignCellContents headerCell = new JRDesignCellContents();
		jrGroup.setHeader(headerCell);

		exp = new JRDesignExpression();
		exp.setText("$V{" + jrGroup.getName() + "}"); //$NON-NLS-1$ //$NON-NLS-2$

		JRDesignTextField tf = (JRDesignTextField) new MTextField()
				.createJRElement(jasperDesign);
		tf.setX(0);
		tf.setY(0);
		tf.setWidth(60);
		tf.setHeight(jrGroup.getHeight());
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
		stext.setWidth(60);
		stext.setHeight(jrGroup.getHeight());
		stext.setText(Messages.common_total + " " + jrGroup.getName()); //$NON-NLS-1$
		totalCell.addElement(stext);
		jrGroup.setTotalHeader(totalCell);
		return jrGroup;
	}

	public static void addColumnGroup(JRDesignCrosstab jrCross,
			JRDesignCrosstabColumnGroup jrRowGr, int index) throws JRException {
		if (index >= 0 && index <= jrCross.getColumnGroupsList().size())
			jrCross.addColumnGroup(index, jrRowGr);
		else
			jrCross.addColumnGroup(jrRowGr);

		// I need to add the extra cells...

		if (!jrCross.getCellsMap().containsKey(
				new Pair<String, String>(null, null))) {
			JRDesignCrosstabCell dT = new JRDesignCrosstabCell();
			dT.setColumnTotalGroup(null);
			dT.setRowTotalGroup(null);
			jrCross.addCell(dT);
			dT.setHeight(jrRowGr.getHeight());
			dT.setWidth(60);
		}

		JRDesignCrosstabCell dT = new JRDesignCrosstabCell();
		dT.setColumnTotalGroup(jrRowGr.getName());
		jrCross.addCell(dT);
		dT.setHeight(jrRowGr.getHeight());
		dT.setWidth(60);
		// for each column, we need to add the total...
		List<JRCrosstabRowGroup> rows = jrCross.getRowGroupsList();
		if (rows != null)
			for (JRCrosstabRowGroup r : rows) {
				JRDesignCrosstabCell cell = new JRDesignCrosstabCell();
				cell.setColumnTotalGroup(jrRowGr.getName());
				cell.setRowTotalGroup(r.getName());
				jrCross.addCell(cell);
				cell.setHeight(jrRowGr.getHeight());
				cell.setWidth(r.getWidth());
				// Add some cells...

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
		DeleteColumnGroupCommand.removeColumnGroup(jrCrosstab, jrGroup);
	}
}
