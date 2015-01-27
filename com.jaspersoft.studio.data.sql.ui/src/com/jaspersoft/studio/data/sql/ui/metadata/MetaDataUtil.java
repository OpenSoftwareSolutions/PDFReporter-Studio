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
package com.jaspersoft.studio.data.sql.ui.metadata;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.model.MDBObjects;
import com.jaspersoft.studio.data.sql.model.metadata.MFunction;
import com.jaspersoft.studio.data.sql.model.metadata.MProcedure;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlSchema;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.metadata.MTables;
import com.jaspersoft.studio.data.sql.model.metadata.keys.ForeignKey;
import com.jaspersoft.studio.data.sql.model.metadata.keys.PrimaryKey;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.utils.Misc;

public class MetaDataUtil {
	public synchronized static List<MSqlSchema> readSchemas(IProgressMonitor monitor, MRoot root, DatabaseMetaData meta, String[] cschemas) throws SQLException {
		List<MSqlSchema> mcurrent = new ArrayList<MSqlSchema>();

		boolean isSchema = meta.supportsSchemasInTableDefinitions();
		boolean isCatalog = meta.supportsCatalogsInTableDefinitions();
		ResultSet rs = isSchema ? meta.getSchemas() : meta.getCatalogs();
		try {
			while (rs.next()) {
				String tableCatalog = isCatalog && !isSchema ? rs.getString("TABLE_CAT") : null;// rs.getString("TABLE_CATALOG");
				String tableSchema = isSchema ? rs.getString("TABLE_SCHEM") : tableCatalog;
				MSqlSchema mschema = new MSqlSchema(root, tableSchema, tableCatalog);
				new MDummy(mschema);
				if (monitor.isCanceled())
					break;
			}
		} finally {
			SchemaUtil.close(rs);
		}
		if (Misc.isNullOrEmpty(root.getChildren())) {
			MSqlSchema mschema = new MSqlSchema(root, null, null);
			new MDummy(mschema);
		}
		if (cschemas != null)
			for (String s : cschemas) {
				for (INode n : root.getChildren()) {
					if (n instanceof MSqlSchema && s.equals(((MSqlSchema) n).getValue())) {
						mcurrent.add(((MSqlSchema) n));
						((MSqlSchema) n).setCurrent(true);
					}
				}
			}
		return mcurrent;
	}

	public synchronized static void readSchema(DatabaseMetaData meta, MSqlSchema schema, IProgressMonitor monitor, List<String> tableTypes) {
		ResultSet rs = null;
		try {
			boolean isSchema = meta.supportsSchemasInTableDefinitions();
			boolean isCatalog = meta.supportsCatalogsInTableDefinitions();
			if (!isSchema && !isCatalog && schema.getValue() == null) {
				schema.removeChildren();
				schema.setNotInMetadata(false);

				for (String ttype : tableTypes)
					new MTables(schema, ttype);
			} else {
				rs = isSchema ? meta.getSchemas() : meta.getCatalogs();
				while (rs.next()) {
					String tableCatalog = isCatalog && !isSchema ? rs.getString("TABLE_CAT") : null;// rs.getString("TABLE_CATALOG");
					String tableSchema = isSchema ? rs.getString("TABLE_SCHEM") : tableCatalog;

					if (tableSchema.equals(schema.getValue())) {
						schema.removeChildren();
						schema.setNotInMetadata(false);

						for (String ttype : tableTypes)
							new MTables(schema, ttype);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SchemaUtil.close(rs);
		}
	}

	public synchronized static void readSchemaTables(DatabaseMetaData meta, MSqlSchema schema, LinkedHashMap<String, MSqlTable> tables, IProgressMonitor monitor) {
		try {
			for (INode n : schema.getChildren())
				if (n instanceof MTables)
					MetaDataUtil.readTables(meta, schema.getValue(), schema.getTableCatalog(), (MTables) n, tables, monitor);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void readTables(DatabaseMetaData meta, String tableSchema, String tableCatalog, MTables mview, LinkedHashMap<String, MSqlTable> tblMap, IProgressMonitor monitor) {
		ResultSet rs = null;
		try {
			rs = meta.getTables(tableCatalog, tableSchema, "%", new String[] { mview.getValue() });
			while (rs.next()) {
				MSqlTable mt = new MSqlTable(mview, rs.getString("TABLE_NAME"), rs);
				new MDummy(mt);
				tblMap.put(mt.toSQLString(), mt);
				if (monitor.isCanceled())
					break;
			}
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			SchemaUtil.close(rs);
		}
	}

	public synchronized static void readTableColumns(DatabaseMetaData meta, MSqlTable mtable, IProgressMonitor monitor) throws SQLException {
		
		MTables tables = (MTables) mtable.getParent();
		mtable.removeChildren();
		ResultSet rs = meta.getColumns(tables.getTableCatalog(), tables.getTableSchema(), mtable.getValue(), "%");
		try {
			while (rs.next())
				new MSQLColumn(mtable, rs.getString("COLUMN_NAME"), rs);
		} finally {
			SchemaUtil.close(rs);
		}
	}

	public synchronized static void readTableKeys(DatabaseMetaData meta, MSqlTable mtable, IProgressMonitor monitor) throws SQLException {
		MetaDataUtil.readPrimaryKeys(meta, mtable, monitor);
		if (!monitor.isCanceled())
			MetaDataUtil.readForeignKeys(meta, mtable, monitor);
	}

	private static void readPrimaryKeys(DatabaseMetaData meta, MSqlTable mt, IProgressMonitor monitor) throws SQLException {
		MTables tables = (MTables) mt.getParent();
		ResultSet rs = meta.getPrimaryKeys(tables.getTableCatalog(), tables.getTableSchema(), mt.getValue());
		PrimaryKey pk = null;
		List<MSQLColumn> cols = new ArrayList<MSQLColumn>();
		try {
			while (rs.next()) {
				if (pk == null)
					pk = new PrimaryKey(rs.getString("PK_NAME"));
				String cname = rs.getString("COLUMN_NAME");
				// short keySeq = rs.getShort("KEY_SEQ");
				for (INode n : mt.getChildren()) {
					if (n.getValue().equals(cname)) {
						((MSQLColumn) n).setPrimaryKey(pk);
						cols.add((MSQLColumn) n);
						break;
					}
				}
				if (monitor.isCanceled())
					break;
			}
		} finally {
			SchemaUtil.close(rs);
		}
		if (pk != null)
			pk.setColumns(cols.toArray(new MSQLColumn[cols.size()]));
	}

	private static void readForeignKeys(DatabaseMetaData meta, MSqlTable mt, IProgressMonitor monitor) throws SQLException {
		MTables tables = (MTables) mt.getParent();
		ResultSet rs = meta.getImportedKeys(tables.getTableCatalog(), tables.getTableSchema(), mt.getValue());
		ForeignKey fk = null;
		List<MSQLColumn> srcCols = new ArrayList<MSQLColumn>();
		List<MSQLColumn> dstCols = new ArrayList<MSQLColumn>();
		List<String[]> fks = new ArrayList<String[]>();
		try {
			while (rs.next()) {
				fks.add(new String[] { rs.getString("PKTABLE_CAT"), rs.getString("PKTABLE_SCHEM"), rs.getString("PKTABLE_NAME"), rs.getString("PKCOLUMN_NAME"), rs.getString("FKCOLUMN_NAME"),
						rs.getString("FK_NAME") });
			}
		} finally {
			SchemaUtil.close(rs);
		}
		for (String[] f : fks) {
			String pkcatalog = f[0];
			String pkschema = f[1];
			String pktable = f[2];
			String pkcolname = f[3];
			String fkcolname = f[4];
			String fkname = f[5];
			MSqlTable dTable = null;
			if (fk == null || !fk.getFkName().equals(fkname)) {
				closeForeignKey(fk, srcCols, dstCols);
				fk = new ForeignKey(fkname, mt);
				dTable = Util.getTable((MRoot) tables.getRoot(), pkcatalog, pkschema, pktable);
				if (dTable.getChildren().isEmpty() || dTable.getChildren().get(0) instanceof MDummy) {
					readTableColumns(meta, dTable, monitor);
					readTableKeys(meta, dTable, monitor);
				}
			}
			for (INode n : mt.getChildren()) {
				if (n.getValue().equals(fkcolname)) {
					srcCols.add((MSQLColumn) n);
					break;
				}
			}
			if (dTable != null)
				for (INode n : dTable.getChildren()) {
					if (n.getValue().equals(pkcolname)) {
						dstCols.add((MSQLColumn) n);
						break;
					}
				}
			else {
				fk = null;
				// the link is not good, what we do?
			}
			if (monitor.isCanceled())
				break;
		}
		closeForeignKey(fk, srcCols, dstCols);
	}

	private static void closeForeignKey(ForeignKey fk, List<MSQLColumn> srcCols, List<MSQLColumn> dstCols) {
		if (fk != null) {
			for (MSQLColumn c : srcCols)
				c.addForeignKey(fk);
			for (MSQLColumn c : dstCols)
				c.addForeignKey(fk);
			fk.setColumns(srcCols.toArray(new MSQLColumn[srcCols.size()]), dstCols.toArray(new MSQLColumn[dstCols.size()]));
		}
	}

	public synchronized static void readProcedures(DatabaseMetaData meta, MSqlSchema schema, IProgressMonitor monitor) {
		ResultSet rs = null;
		try {
			rs = meta.getProcedures(schema.getTableCatalog(), schema.getValue(), "%");
			MDBObjects mprocs = new MDBObjects(schema, "Procedures", "icons/function.png");
			while (rs.next())
				new MProcedure(mprocs, rs.getString("PROCEDURE_NAME"), rs);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			SchemaUtil.close(rs);
		}
		if (monitor.isCanceled())
			return;
		try {
			rs = meta.getFunctions(schema.getTableCatalog(), schema.getValue(), "%");
			MDBObjects mfunct = new MDBObjects(schema, "Functions", "icons/function.png");
			while (rs.next())
				new MFunction(mfunct, rs.getString("FUNCTION_NAME"), rs);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			SchemaUtil.close(rs);
		}
	}
}
