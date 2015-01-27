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
package com.jaspersoft.studio.components.table.model.cell.command;

import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.model.column.MCell;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.command.Tag;

public class CreateE4ObjectCommand extends CreateElementCommand {
	protected ANode child;
	protected ANode parent;
	protected JRDesignDataset jDataset;

	public CreateE4ObjectCommand(ANode child, MCell parent, Rectangle location, int index) {
		super(parent, null, location, index);
		JasperDesign jd = parent.getJasperDesign();
		jDataset = jd.getMainDesignDataset();
		StandardTable st = TableManager.getTable(parent.getMTable());
		JRDatasetRun dr = st.getDatasetRun();
		if (dr != null) {
			String dbname = dr.getDatasetName();
			if (dbname != null)
				jDataset = (JRDesignDataset) jd.getDatasetMap().get(dbname);
		}

		this.child = child;
		this.parent = parent;
	}

	@Override
	protected void createObject() {
		try {
			MCell mparent = (MCell) parent;
			Tag tag = Tag.getExpression(child);
			switch (mparent.getType()) {
			case TableUtil.COLUMN_HEADER:
			case TableUtil.COLUMN_FOOTER:
				var = Tag.createVariable(tag, ResetTypeEnum.COLUMN, null,
						jDataset);
				srcNode = Tag.createTextField(
						tag.txt.replaceAll("%", tag.name), tag.classname);
				break;
			case TableUtil.COLUMN_GROUP_HEADER:
				var = Tag.createVariable(tag, ResetTypeEnum.GROUP,
						mparent.getJrGroup(), jDataset);
				srcNode = Tag.createTextField(
						tag.txt.replaceAll("%", tag.name), tag.classname);
				break;
			case TableUtil.COLUMN_GROUP_FOOTER:
				var = Tag.createVariable(tag, ResetTypeEnum.GROUP,
						mparent.getJrGroup(), jDataset);
				srcNode = Tag.createTextField(
						tag.txt.replaceAll("%", tag.name), tag.classname);
				break;
			case TableUtil.COLUMN_DETAIL:
				srcNode = Tag.createTextField(
						tag.txt.replaceAll("%", tag.name), tag.classname);
				break;
			case TableUtil.TABLE_FOOTER:
			case TableUtil.TABLE_HEADER:
				var = Tag.createVariable(tag, ResetTypeEnum.REPORT, null,
						jDataset);
				srcNode = Tag.createTextField(
						tag.txt.replaceAll("%", tag.name), tag.classname);
				break;
			default:
				srcNode = Tag.createStaticText(tag.name);
			}
			jrElement = srcNode.getValue();
			super.createObject();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JRDesignVariable var;

	@Override
	public void execute() {
		super.execute();
		try {
			if (var != null)
				jDataset.addVariable((JRDesignVariable) var);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void undo() {
		super.undo();
		if (var != null)
			jDataset.removeVariable(var);
	}
}
