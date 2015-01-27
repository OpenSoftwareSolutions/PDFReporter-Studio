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
package com.jaspersoft.studio.components.table;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.component.Component;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.internal.Workbench;
import org.eclipse.ui.part.WorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.components.table.action.EditTableStyleAction;
import com.jaspersoft.studio.components.table.action.RemoveTableStylesAction;
import com.jaspersoft.studio.components.table.editor.TableEditor;
import com.jaspersoft.studio.components.table.figure.CellFigure;
import com.jaspersoft.studio.components.table.figure.EmptyCellFigure;
import com.jaspersoft.studio.components.table.figure.TableFigure;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.MTableColumnFooter;
import com.jaspersoft.studio.components.table.model.MTableColumnHeader;
import com.jaspersoft.studio.components.table.model.MTableDetail;
import com.jaspersoft.studio.components.table.model.MTableFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupHeader;
import com.jaspersoft.studio.components.table.model.MTableHeader;
import com.jaspersoft.studio.components.table.model.cell.command.CreateE4ObjectCommand;
import com.jaspersoft.studio.components.table.model.cell.command.CreateElementCommand;
import com.jaspersoft.studio.components.table.model.cell.command.CreateElementGroupCommand;
import com.jaspersoft.studio.components.table.model.cell.command.DeleteElementCommand;
import com.jaspersoft.studio.components.table.model.cell.command.DeleteElementGroupCommand;
import com.jaspersoft.studio.components.table.model.cell.command.OrphanElementCommand;
import com.jaspersoft.studio.components.table.model.cell.command.OrphanElementGroupCommand;
import com.jaspersoft.studio.components.table.model.cell.command.ReorderElementCommand;
import com.jaspersoft.studio.components.table.model.cell.command.ReorderElementGroupCommand;
import com.jaspersoft.studio.components.table.model.column.MCell;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnAfterAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnBeforeAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnBeginAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnCellAction;
import com.jaspersoft.studio.components.table.model.column.action.CreateColumnEndAction;
import com.jaspersoft.studio.components.table.model.column.action.DeleteColumnAction;
import com.jaspersoft.studio.components.table.model.column.action.DeleteColumnCellAction;
import com.jaspersoft.studio.components.table.model.column.action.DeleteRowAction;
import com.jaspersoft.studio.components.table.model.column.command.CreateColumnCellCommand;
import com.jaspersoft.studio.components.table.model.column.command.CreateColumnCommand;
import com.jaspersoft.studio.components.table.model.column.command.CreateColumnFromGroupCommand;
import com.jaspersoft.studio.components.table.model.column.command.CreateColumnGroupCellCommand;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnCellCommand;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnCommand;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnFromGroupCommand;
import com.jaspersoft.studio.components.table.model.column.command.DeleteColumnGroupCellCommand;
import com.jaspersoft.studio.components.table.model.column.command.OrphanColumn4GroupCommand;
import com.jaspersoft.studio.components.table.model.column.command.OrphanColumnCommand;
import com.jaspersoft.studio.components.table.model.column.command.ReorderColumnCommand;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;
import com.jaspersoft.studio.components.table.model.columngroup.action.GroupColumnsAction;
import com.jaspersoft.studio.components.table.model.columngroup.action.UnGroupColumnsAction;
import com.jaspersoft.studio.components.table.model.columngroup.command.CreateColumnGroupCommand;
import com.jaspersoft.studio.components.table.model.columngroup.command.CreateColumnGroupFromGroupCommand;
import com.jaspersoft.studio.components.table.model.columngroup.command.ReorderColumnGroupCommand;
import com.jaspersoft.studio.components.table.model.table.command.CreateTableCommand;
import com.jaspersoft.studio.components.table.part.TableCellEditPart;
import com.jaspersoft.studio.components.table.part.TableEditPart;
import com.jaspersoft.studio.components.table.part.TablePageEditPart;
import com.jaspersoft.studio.editor.AContextMenuProvider;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.editor.report.ReportContainer;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGraphicElementContainer;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MCollection;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.image.command.CreateImageCommand;
import com.jaspersoft.studio.model.parameter.MParameterSystem;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.model.variable.MVariableSystem;
import com.jaspersoft.studio.plugin.IComponentFactory;
import com.jaspersoft.studio.plugin.IPaletteContributor;
import com.jaspersoft.studio.plugin.PaletteContributor;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class TableComponentFactory implements IComponentFactory {
	
	private static List<Class<?>> knownClasses;
	
	static {
		knownClasses = new ArrayList<Class<?>>(4);
		knownClasses.add(MTable.class);
		knownClasses.add(MCell.class);
		knownClasses.add(MColumnGroup.class);
		knownClasses.add(MColumn.class);
	}

	public ANode createNode(final ANode parent, Object jrObject, int newIndex) {
		if (jrObject instanceof JRDesignComponentElement) {
			JRDesignComponentElement tbl = (JRDesignComponentElement) jrObject;
			if (tbl.getComponent() instanceof StandardTable) {
				JasperDesign jasperDesign = parent.getJasperDesign();
				TableManager tblManager = new TableManager(tbl, jasperDesign);
				MTable mt = new MTable(parent, tbl, newIndex, tblManager);
				if (parent instanceof MPage) {
					createTable(mt);
					//Removed a lot of old listener in this class on the 10-06-2014, need test
					//to see if all it's working
					JasperDesign jd = mt.getJasperDesign();
					ReportFactory.createStyles(parent.getJasperConfiguration(), jd, parent, 0);

					StandardTable st = TableManager.getTable(mt);
					DSListener dslistner = new DSListener(parent, jd, st);
					setDataset(parent, jd, st);

					st.getEventSupport().addPropertyChangeListener(dslistner);

					MCallout.createCallouts(mt);
				}
				return mt;
			}
		}
		return null;
	}

	/**
	 * Listener to update the dataset inside the table designer when the dataset
	 * run is changed
	 * 
	 * @author Orlandin Marco
	 *
	 */
	class DSListener implements PropertyChangeListener {
		private ANode parent;
		private JasperDesign jd;
		private StandardTable st;

		public DSListener(ANode parent, JasperDesign jd, StandardTable st) {
			this.parent = parent;
			this.jd = jd;
			this.st = st;
		}

		public void propertyChange(PropertyChangeEvent evt) {
			if (StandardTable.PROPERTY_DATASET_RUN.equals(evt.getPropertyName())){
					setDataset(parent, jd, st);
			}
		}
	};

	/**
	 * Remove the old dataset from the mpage of a table and add the new one, read
	 * from the dataset run of the table
	 * 
	 * @param parent the mpage of the table editor
	 * @param jd the jasperdesign
	 * @param st the table element inside the table editor
	 */
	public void setDataset(ANode parent, final JasperDesign jd, StandardTable st) {
		//Remove all the old dataset inside the page
		for (INode n : parent.getChildren()){
			if (n instanceof MDataset) parent.removeChild((ANode) n);
		}
		JRDesignDatasetRun dr = (JRDesignDatasetRun) st.getDatasetRun();
		if (dr != null) {
			String dbname = dr.getDatasetName();
			JRDesignDataset dataset;
			if (dbname != null){
				dataset = (JRDesignDataset) jd.getDatasetMap().get(dbname);
			} else {
 				dataset = (JRDesignDataset) jd.getMainDataset();
			}
			if (dataset != null) {
				MDataset nDataset = new MDataset(parent, dataset, 1);
				ReportFactory.createDataset(nDataset, dataset, true);
			}

		}
	}

	public static ANode createTable(MTable mt) {
		JRDesignComponentElement tbl = (JRDesignComponentElement) mt.getValue();
		StandardTable table = (StandardTable) tbl.getComponent();
		MTableHeader mth = new MTableHeader(mt, tbl, StandardColumn.PROPERTY_TABLE_HEADER);
		MTableColumnHeader mch = new MTableColumnHeader(mt, tbl, StandardColumn.PROPERTY_COLUMN_HEADER);

		List<?> groupsList = TableUtil.getGroupList(table, mt.getJasperDesign());
		List<MTableGroupHeader> grHeaders = new ArrayList<MTableGroupHeader>();
		List<MTableGroupFooter> grFooters = new ArrayList<MTableGroupFooter>();

		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.next();
				grHeaders.add(new MTableGroupHeader(mt, tbl, jrGroup, ""));
			}

		MTableDetail mtd = new MTableDetail(mt, tbl, StandardColumn.PROPERTY_DETAIL);

		if (groupsList != null)
			for (ListIterator<?> it = groupsList.listIterator(groupsList.size()); it.hasPrevious();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.previous();
				grFooters.add(new MTableGroupFooter(mt, tbl, jrGroup, ""));
			}

		MTableColumnFooter mtcf = new MTableColumnFooter(mt, tbl, StandardColumn.PROPERTY_COLUMN_FOOTER);
		MTableFooter mtf = new MTableFooter(mt, tbl, StandardColumn.PROPERTY_TABLE_FOOTER);

		createColumns(mt, table.getColumns(), mth, mch, mtd, mtcf, mtf, grHeaders, grFooters);

		return mt;
	}

	public static void createColumns(ANode parent, List<BaseColumn> columns, MTableHeader mth, MTableColumnHeader mch, MTableDetail mtd, MTableColumnFooter mcf, MTableFooter mtf,
			List<MTableGroupHeader> grHeaders, List<MTableGroupFooter> grFooter) {
		int j = 1;
		for (int i = 0; i < columns.size(); i++) {
			BaseColumn bc = columns.get(i);
			createCellTableHeader(mth, bc, i + 1, i);

			createCellColumnHeader(mch, bc, i + 1, i);

			for (MTableGroupHeader mtgh : grHeaders)
				createCellGroupHeader(mtgh, bc, i + 1, mtgh.getJrDesignGroup().getName(), i);

			j = createCellDetail(mtd, bc, j, i);

			for (MTableGroupFooter mtgh : grFooter)
				createCellGroupFooter(mtgh, bc, i + 1, mtgh.getJrDesignGroup().getName(), i);

			createCellColumnFooter(mcf, bc, i + 1, i);

			createCellTableFooter(mtf, bc, i + 1, i);
		}
	}

	public static int createCellGroupHeader(ANode mth, BaseColumn bc, int i, String grName, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			MColumn mcg = getColumnGroup(mth, scg, (DesignCell) scg.getGroupHeader(grName), i, index);
			for (int j = 0; j < scg.getColumns().size(); j++)
				i = createCellGroupHeader(mcg, scg.getColumns().get(j), i, grName, j);
		} else {
			createColumnCell(mth, bc, i, bc.getGroupHeader(grName), index);
			return ++i;
		}
		return i;
	}

	public static int createCellGroupFooter(ANode mth, BaseColumn bc, int i, String grName, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			MColumn mcg = getColumnGroup(mth, scg, (DesignCell) scg.getGroupFooter(grName), i, index);
			for (int j = 0; j < scg.getColumns().size(); j++)
				i = createCellGroupFooter(mcg, scg.getColumns().get(j), i, grName, j);
		} else {
			createColumnCell(mth, bc, i, bc.getGroupFooter(grName), index);
			return ++i;
		}
		return i;
	}

	public static int createCellDetail(ANode mth, BaseColumn bc, int i, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			for (BaseColumn bcg : scg.getColumns())
				i = createCellDetail(mth, bcg, i, i);
		} else {
			createColumnCell(mth, bc, i, ((StandardColumn) bc).getDetailCell(), index);
			return ++i;
		}
		return i;
	}

	public static int createCellColumnHeader(ANode mth, BaseColumn bc, int i, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			MColumn mcg = getColumnGroup(mth, scg, (DesignCell) scg.getColumnHeader(), i, index);
			for (int j = 0; j < scg.getColumns().size(); j++)
				i = createCellColumnHeader(mcg, scg.getColumns().get(j), i, j);
		} else {
			createColumnCell(mth, bc, i, bc.getColumnHeader(), index);
			return ++i;
		}
		return i;
	}

	public static int createCellColumnFooter(ANode mth, BaseColumn bc, int i, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			MColumn mcg = getColumnGroup(mth, scg, (DesignCell) scg.getColumnFooter(), i, index);
			for (int j = 0; j < scg.getColumns().size(); j++)
				i = createCellColumnFooter(mcg, scg.getColumns().get(j), i, j);
		} else {
			createColumnCell(mth, bc, i, bc.getColumnFooter(), index);
			return ++i;
		}
		return i;
	}

	public static int createCellTableHeader(ANode mth, BaseColumn bc, int i, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			MColumn mcg = getColumnGroup(mth, scg, (DesignCell) scg.getTableHeader(), i, index);
			for (int j = 0; j < scg.getColumns().size(); j++)
				i = createCellTableHeader(mcg, scg.getColumns().get(j), i, j);
		} else {
			createColumnCell(mth, bc, i, bc.getTableHeader(), index);
			return ++i;
		}
		return i;
	}

	public static int createCellTableFooter(ANode mth, BaseColumn bc, int i, int index) {
		if (bc instanceof StandardColumnGroup) {
			StandardColumnGroup scg = (StandardColumnGroup) bc;
			MColumn mcg = getColumnGroup(mth, scg, (DesignCell) scg.getTableFooter(), i, index);
			for (int j = 0; j < scg.getColumns().size(); j++)
				i = createCellTableFooter(mcg, scg.getColumns().get(j), i, j);
		} else {
			createColumnCell(mth, bc, i, bc.getTableFooter(), index);
			return ++i;
		}
		return i;
	}

	public static ANode createColumnCell(ANode parent, BaseColumn bc, int i, Cell grHeader, int index) {
		String name = Messages.common_column + i;
		if (grHeader != null) {
			MCell mc = new MCell(parent, (StandardBaseColumn) bc, (DesignCell) grHeader, name, index);
			ReportFactory.createElementsForBand(mc, grHeader.getChildren());
			return mc;
		}
		return new MColumn(parent, (StandardBaseColumn) bc, name, index);
	}

	public static MColumn getColumnGroup(ANode mth, StandardColumnGroup scg, DesignCell cell, int i, int index) {
		String name = Messages.common_columns + " " + i + "-" + (i + scg.getColumns().size() - 1); //$NON-NLS-1$ //$NON-NLS-2$
		MColumn mcg = null;
		if (cell != null) {
			mcg = new MColumnGroupCell(mth, scg, cell, name, index);
			ReportFactory.createElementsForBand(mcg, cell.getChildren());
		} else
			mcg = new MColumnGroup(mth, scg, name, index);
		return mcg;
	}

	public IFigure createFigure(ANode node) {
		if (node instanceof MTable)
			return new TableFigure((MTable)node);
		if (node instanceof MCell)
			return new CellFigure();

		if (node instanceof MColumn && !(node instanceof MCell))
			return new EmptyCellFigure();
		return null;
	}

	public List<?> getChildren4Element(Object jrObject) {
		return null;
	}

	public IPaletteContributor getPaletteEntries() {
		PaletteContributor pc = new PaletteContributor();
		// pc.add(IPaletteContributor.KEY_COMMON_CONTAINER, MTable.class);
		pc.add(MTable.class);
		return pc;
	}

	public Command getCreateCommand(ANode parent, ANode child, Rectangle location, int newIndex) {
		if (parent instanceof MPage) {
			for (INode c : parent.getChildren()) {
				if (c instanceof MTable) {
					parent = (ANode) c;
					break;
				}
			}
		}
		if (child instanceof MField && (child.getValue() != null && parent instanceof MCell))
			return new CreateE4ObjectCommand(child, (MCell) parent, location, newIndex);
		if (child instanceof MParameterSystem && (child.getValue() != null && parent instanceof MCell))
			return new CreateE4ObjectCommand(child, (MCell) parent, location, newIndex);
		if (child instanceof MVariableSystem && (child.getValue() != null && parent instanceof MCell))
			return new CreateE4ObjectCommand(child, (MCell) parent, location, newIndex);

		if (child instanceof MStyle && (child.getValue() != null && parent instanceof MCell)) {
			SetValueCommand cmd = new SetValueCommand();
			cmd.setTarget((MCell) parent);
			cmd.setPropertyId(DesignCell.PROPERTY_STYLE);
			JRStyle style = (JRStyle) child.getValue();
			cmd.setPropertyValue(style.getName());
			return cmd;
		}
		if (child instanceof MStyle && (child.getValue() != null && parent instanceof MTable)) {
			SetValueCommand cmd = new SetValueCommand();
			cmd.setTarget((MTable) parent);
			cmd.setPropertyId(JRDesignElement.PROPERTY_PARENT_STYLE);
			JRStyle style = (JRStyle) child.getValue();
			cmd.setPropertyValue(style.getName());
			return cmd;
		}

		if (child instanceof MCell) {
			if (parent instanceof MColumnGroup)
				return new CreateColumnGroupCellCommand(((MColumn) parent).getSection(), (MColumnGroup) parent);
			if (parent instanceof MColumn && !(parent instanceof MCell))
				return new CreateColumnCellCommand(((MColumn) parent).getSection(), (MColumn) parent);

		} else if (child instanceof MColumnGroup) {
			if (parent instanceof AMCollection)
				return new CreateColumnGroupCommand((AMCollection) parent, (MColumnGroup) child, newIndex);

			if (parent instanceof MColumnGroupCell)
				return new CreateColumnGroupFromGroupCommand((MColumnGroupCell) parent, (MColumnGroup) child, newIndex);
			if (parent instanceof MColumnGroup)
				return new CreateColumnGroupFromGroupCommand((MColumnGroup) parent, (MColumnGroup) child, newIndex);

			if (parent.getParent() instanceof MColumnGroupCell)
				return new CreateColumnGroupFromGroupCommand((MColumnGroupCell) parent.getParent(), (MColumnGroup) child, newIndex);
			if (parent.getParent() instanceof MColumnGroup)
				return new CreateColumnGroupFromGroupCommand((MColumnGroup) parent.getParent(), (MColumnGroup) child, newIndex);

			if (parent instanceof MColumn)
				return new CreateColumnGroupCommand((MColumn) parent, (MColumnGroup) child, newIndex);

		} else if (child instanceof MColumn) {
			if (parent instanceof MTable)
				return new CreateColumnCommand((MTable) parent, (MColumn) child, newIndex);
			if (parent instanceof MTableGroupHeader)
				return new CreateColumnCommand((MTableGroupHeader) parent, (MColumn) child, newIndex);
			if (parent instanceof MTableGroupFooter)
				return new CreateColumnCommand((MTableGroupFooter) parent, (MColumn) child, newIndex);
			if (parent instanceof AMCollection)
				return new CreateColumnCommand((AMCollection) parent, (MColumn) child, newIndex);

			if (parent instanceof MColumnGroupCell)
				return new CreateColumnFromGroupCommand((MColumnGroupCell) parent, (MColumn) child, newIndex);
			if (parent instanceof MColumnGroup)
				return new CreateColumnFromGroupCommand((MColumnGroup) parent, (MColumn) child, newIndex);

			if (parent.getParent() instanceof MColumnGroupCell)
				return new CreateColumnFromGroupCommand((MColumnGroupCell) parent.getParent(), (MColumn) child, newIndex);
			if (parent.getParent() instanceof MColumnGroup)
				return new CreateColumnFromGroupCommand((MColumnGroup) parent.getParent(), (MColumn) child, newIndex);

			if (parent instanceof MColumn && ((MColumn) parent).getMTable() != null)
				return new CreateColumnCommand((MColumn) parent, (MColumn) child, newIndex);
		}
		if (child instanceof MTable) {
			if (parent instanceof MElementGroup)
				return new CreateTableCommand((MElementGroup) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MBand)
				return new CreateTableCommand((MBand) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MFrame)
				return new CreateTableCommand((MFrame) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MReport)
				return new CreateTableCommand(parent, (MGraphicElement) child, location, newIndex);

			if (parent instanceof IGroupElement && parent instanceof IGraphicElementContainer) {
				return new CreateTableCommand(parent, (MGraphicElement) child, location, newIndex);
			}
		} 
		if (child instanceof MImage) {
			if (parent instanceof MCell)
				return new CreateImageCommand((MCell) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MElementGroup)
				return new CreateImageCommand((MElementGroup) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MBand)
				return new CreateImageCommand((MBand) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MFrame)
				return new CreateImageCommand((MFrame) parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof MReport)
				return new CreateImageCommand(parent, (MGraphicElement) child, location, newIndex);
			if (parent instanceof IGroupElement) {
				return new com.jaspersoft.studio.model.command.CreateElementCommand(parent, (MGraphicElement) child, location, newIndex);
			}
		} 
		if (child instanceof MGraphicElement && parent instanceof MCell)
			return new CreateElementCommand((MCell) parent, (MGraphicElement) child, location, newIndex);
		if (child instanceof MElementGroup && parent instanceof MCell)
			return new CreateElementGroupCommand((MCell) parent, (MElementGroup) child, newIndex);
		if (parent instanceof MTable && child instanceof MGraphicElement) {
			MTable mt = (MTable) parent;
			if (location != null) {
				final Cell cell = mt.getTableManager().getCell(new Point(location.x, location.y));
				if (cell != null) {
					Rectangle r = mt.getTableManager().getCellBounds(cell);
					int x = r != null ? r.x : 0;
					int y = r != null ? r.y : 0;
					location = location.setLocation(location.x - x, location.y - y);
					
					//The parent even if it is a MTable could not have the children if we are in the main editor
					//so we must open the table editor and take the table from there to get the children
					IEditorPart editorPart = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
					INode tableEditorModel = null;
					if (editorPart instanceof JrxmlEditor){
						JrxmlEditor jrxmlEditor = (JrxmlEditor)editorPart;
						jrxmlEditor.openEditor(parent.getValue(), parent);
						IEditorPart reportContainer = jrxmlEditor.getActiveEditor();
						if (reportContainer instanceof ReportContainer){
							ReportContainer container = (ReportContainer)reportContainer;
							if (container.getActiveEditor() instanceof TableEditor){
								TableEditor tableEditor = (TableEditor)container.getActiveEditor();
								tableEditorModel = tableEditor.getModel();
							}
						}
					}
					
					ModelVisitor<MCell> mv = new ModelVisitor<MCell>(tableEditorModel != null? tableEditorModel : parent) {
						@Override
						public boolean visit(INode n) {
							if (n instanceof MCell && ((MCell) n).getCell() == cell)
								setObject((MCell) n);
							return true;
						}
					};
					MCell mcell = (MCell) mv.getObject();
					if (mcell == null){
						UIUtils.showInformation("Impossible to create the element", "The element could not be created in this position, try to enter into the table editor and place it from there");
						return null;
					}
					return new CreateElementCommand(mcell, (MGraphicElement) child, location, newIndex);
				}
			}
		}
		return null;
	}

	public Command getDeleteCommand(ANode parent, ANode child) {
		if (child instanceof MTable)
			return null;
		if (parent instanceof MPage)
			parent = child.getParent();
		if (child instanceof MGraphicElement && parent instanceof MCell && child.getValue() != null)
			return new DeleteElementCommand((MCell) parent, (MGraphicElement) child);
		if (child instanceof MElementGroup && parent instanceof MCell && child.getValue() != null)
			return new DeleteElementGroupCommand((MCell) parent, (MElementGroup) child);
		return null;
	}

	public static Command getDeleteCellCommand(ANode parent, ANode child) {
		if (child instanceof MColumnGroupCell){
			if (parent instanceof MColumnGroupCell)
				return new DeleteColumnGroupCellCommand((ANode) parent.getParent(), (MColumnGroupCell) child);
			if (parent instanceof MColumnGroup)
				return new DeleteColumnGroupCellCommand((ANode) parent.getParent(), (MColumnGroupCell) child);
			if (parent instanceof AMCollection)
				return new DeleteColumnGroupCellCommand((ANode) parent, (MColumnGroupCell) child);
		} else if (child instanceof MCell) {
			if (parent instanceof MColumnGroupCell)
				return new DeleteColumnCellCommand((ANode) parent.getParent(), (MCell) child);
			if (parent instanceof MColumnGroup)
				return new DeleteColumnCellCommand((ANode) parent.getParent(), (MCell) child);
			if (parent instanceof AMCollection)
				return new DeleteColumnCellCommand((ANode) parent, (MCell) child);
		}
		return null;
	}

	public static Command getDeleteColumnCommand(ANode parent, ANode child) {
		if (child instanceof MColumn) {
			if (parent instanceof MTableGroupHeader)
				return new DeleteColumnCommand((MTableGroupHeader) parent, (MColumn) child);
			if (parent instanceof MTableGroupFooter)
				return new DeleteColumnCommand((MTableGroupFooter) parent, (MColumn) child);
			if (parent instanceof AMCollection)
				return new DeleteColumnCommand((AMCollection) parent, (MColumn) child);

			if (parent instanceof MColumnGroup)
				return new DeleteColumnFromGroupCommand((MColumnGroup) parent, (MColumn) child);
			if (parent instanceof MColumnGroupCell)
				return new DeleteColumnFromGroupCommand((MColumnGroupCell) parent, (MColumn) child);
		}
		return null;
	}

	public Command getReorderCommand(ANode parent, ANode child, int newIndex) {
		if (child instanceof MColumn) {
			if (parent instanceof MCollection)
				return new ReorderColumnCommand((MColumn) child, (MTable) parent.getParent(), newIndex);
			if (parent instanceof MColumnGroupCell)
				return new ReorderColumnGroupCommand((MColumn) child, (MColumnGroupCell) parent, newIndex);
			if (parent instanceof MColumnGroup)
				return new ReorderColumnGroupCommand((MColumn) child, (MColumnGroup) parent, newIndex);

		}
		if (child instanceof MGraphicElement && parent instanceof MCell)
			return new ReorderElementCommand((MGraphicElement) child, (MCell) parent, newIndex);
		if (child instanceof MElementGroup && parent instanceof MCell)
			return new ReorderElementGroupCommand((MElementGroup) child, (MCell) parent, newIndex);
		return null;
	}

	public List<Action> getActions(WorkbenchPart part) {
		List<Action> lst = new ArrayList<Action>();
		// lst.add(new CreateColumnAction(part));
		// lst.add(new CreateColumnGroupAction(part));
		// lst.add(new CreateColumnCellAction(part));
		lst.add(new EditTableStyleAction(part));
		lst.add(new RemoveTableStylesAction(part));
		return lst;
	}

	public List<String> getActionsID() {
		List<String> lst = new ArrayList<String>();
		lst.add(CreateColumnAfterAction.ID);
		lst.add(CreateColumnBeforeAction.ID);
		lst.add(CreateColumnBeginAction.ID);
		lst.add(CreateColumnEndAction.ID);
		lst.add(AContextMenuProvider.SEPARATOR);

		lst.add(GroupColumnsAction.ID);
		lst.add(UnGroupColumnsAction.ID);
		lst.add(CreateColumnCellAction.ID);

		lst.add(AContextMenuProvider.SEPARATOR);
		lst.add(DeleteColumnAction.ID);
		lst.add(DeleteRowAction.ID);
		lst.add(DeleteColumnCellAction.ID);
		lst.add(EditTableStyleAction.ID);
		lst.add(RemoveTableStylesAction.ID);
		return lst;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		if (model instanceof MRoot) {
			ANode n = ModelUtils.getFirstChild((MRoot) model);
			if (n != null && n instanceof MPage) {
				for (INode child : n.getChildren()) {
					if (child instanceof MTable)
						return new TablePageEditPart();
				}
			}
		}
		if (model instanceof MTable)
			return new TableEditPart();
		if (model instanceof MCell)
			return new TableCellEditPart();
		else if (model instanceof MColumn)
			return new TableCellEditPart();
		return null;
	}

	public Command getOrphanCommand(ANode parent, ANode child) {
		if (child instanceof MColumn) {
			if (parent instanceof AMCollection)
				return new OrphanColumnCommand((AMCollection) parent, (MColumn) child);
			if (parent instanceof MColumnGroupCell)
				return new OrphanColumn4GroupCommand((MColumnGroupCell) parent, (MColumn) child);
			if (parent instanceof MColumnGroup)
				return new OrphanColumn4GroupCommand((MColumnGroup) parent, (MColumn) child);
		}
		if (child instanceof MGraphicElement && parent instanceof MCell)
			return new OrphanElementCommand((MCell) parent, (MGraphicElement) child);
		if (child instanceof MElementGroup && parent instanceof MCell)
			return new OrphanElementGroupCommand((MCell) parent, (MElementGroup) child);
		return null;
	}

	public AbstractVisualEditor getEditor(Object node, JasperReportsConfiguration jrContext) {
		if (node != null && node instanceof JRDesignComponentElement) {
			Component component = ((JRDesignComponentElement) node).getComponent();
			if (component != null && component instanceof StandardTable)
				return new TableEditor(jrContext);
		}
		return null;
	}

	public ExpressionContext getElementExpressionContext(Object jrObject) {
		if (jrObject instanceof MTable && ((MTable) jrObject).getValue() instanceof JRDesignComponentElement) {
			MTable mtable = (MTable) jrObject;
			StandardTable table = (StandardTable) ((JRDesignComponentElement) mtable.getValue()).getComponent();
			JRDesignDataset designDS = ModelUtils.getDesignDatasetForDatasetRun(mtable.getJasperConfiguration().getJasperDesign(), table.getDatasetRun());
			return new ExpressionContext(designDS, mtable.getJasperConfiguration());
		}
		return null;
	}

	@Override
	public Command getStretchToContent(ANode node) {
		if (node instanceof MColumn) {
			MColumn model = (MColumn) node;
			ColumnCell cc = new ColumnCell(model.getType(), model.getGrName(), model.getValue());
			if (model.getMTable()!= null && model.getMTable().getTableManager() != null){
				Dimension d = model.getMTable().getTableManager().getCellPackSize(cc);
				if (d.height > 0 && d.width > 0) {
					JSSCompoundCommand c = new JSSCompoundCommand("Resize to container",model);
	
					SetValueCommand cmd = new SetValueCommand();
	
					cmd.setTarget(model);
					cmd.setPropertyId(DesignCell.PROPERTY_HEIGHT);
					cmd.setPropertyValue(d.height);
					c.add(cmd);
	
					cmd = new SetValueCommand();
					cmd.setTarget(model);
					cmd.setPropertyId(StandardColumn.PROPERTY_WIDTH);
					cmd.setPropertyValue(d.width);
					c.add(cmd);
	
					return c;
				}
			}
		}
		return null;
	}

	private static TableComponentFactory inst;

	public static TableComponentFactory INST() {
		if (inst == null)
			inst = new TableComponentFactory();
		return inst;
	}

	@Override
	public List<Class<?>> getKnownClasses() {
		return knownClasses;
	}
}
