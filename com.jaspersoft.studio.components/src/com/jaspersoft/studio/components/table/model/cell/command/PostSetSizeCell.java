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

import java.util.List;

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.table.ColumnCell;
import com.jaspersoft.studio.components.table.Guide;
import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutCommand;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.property.IPostSetValue;

public class PostSetSizeCell implements IPostSetValue {

	@Override
	public Command postSetValue(IPropertySource target, Object prop,
			Object newValue, Object oldValue) {
		if (target instanceof MColumn
				&& (prop.equals(StandardBaseColumn.PROPERTY_WIDTH) || prop
						.equals(DesignCell.PROPERTY_HEIGHT))) {
			MColumn mband = (MColumn) target;
			JasperDesign jDesign = mband.getJasperDesign();
			return getResizeCommand(mband, jDesign, prop);
		}
		return null;
	}

	public Command getResizeCommand(MColumn mcell, JasperDesign jDesign,
			Object prop) {
		MTable mTable = mcell.getMTable();

		JRPropertiesHolder[] pholder = new JRPropertiesHolder[3];
		pholder[2] = mTable.getValue();

		TableManager tb = mTable.getTableManager();
		JSSCompoundCommand c = new JSSCompoundCommand("Resize Table Cell", mTable);
		ColumnCell cc = tb.getMatrixHelper().getColumnCell(
				new ColumnCell(mcell.getType(), mcell.getGrName(), mcell
						.getValue()));
		if (prop.equals(DesignCell.PROPERTY_HEIGHT))
			if (TableManager.isBottomOfTable(cc.type)) {
				Guide guide = cc.getNorth();
				createCommands(jDesign, pholder, c, guide.getNext());
			} else {
				Guide guide = cc.getSouth();
				createCommands(jDesign, pholder, c, guide.getPrev());
			}
		else if (prop.equals(StandardBaseColumn.PROPERTY_WIDTH)) {
			Guide guide = cc.getWest();
			createCommands(jDesign, pholder, c, guide.getNext());
		}
		return c;
	}

	public void createCommands(JasperDesign jDesign, JRPropertiesHolder[] pholder, JSSCompoundCommand c, List<ColumnCell> cells) {
		for (ColumnCell ccell : cells) {
			if (ccell.cell == null)
				continue;
			pholder[0] = ccell.cell;
			pholder[1] = ccell.column;
			ILayout layout = LayoutManager.getLayout(pholder, jDesign, null);
			Rectangle r = ccell.getBounds();
			Dimension d = new Dimension(r.width, r.height);

			c.add(new LayoutCommand(ccell.cell, layout, d));
		}
	}

}
