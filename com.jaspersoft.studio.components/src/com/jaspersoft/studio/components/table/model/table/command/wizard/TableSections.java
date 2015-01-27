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
package com.jaspersoft.studio.components.table.model.table.command.wizard;

public class TableSections {
	
	private boolean columnHeader;
	
	private int columnHeaderHeight = 25;
	
	private boolean columnFooter;
	
	private int columnFooterHeight = 25;

	private boolean tableHeader;
	
	private int tableHeaderHeight = 25;
	
	private boolean tableFooter;
	
	private int tableFooterHeight = 25;
	
	private boolean groupHeader;
	
	private int groupHeaderHeight = 25;
	
	private boolean groupFooter;
	
	private int groupFooterHeight = 25;
	
	private int detailHeight = 25;
	
	public TableSections(boolean tableHeader, boolean tableFooter, boolean columnHeader, boolean columnFooter, boolean groupHeader, boolean groupFooter){
		this.columnHeader = columnHeader;
		this.columnFooter = columnFooter;
		this.tableHeader = tableHeader;
		this.tableFooter = tableFooter;
		this.groupHeader = groupHeader;
		this.groupFooter = groupFooter;
	}
	
	public boolean isColumnHeader(){
		return columnHeader;
	}
	
	public boolean isColumnFooter(){
		return columnFooter;
	}
	
	public boolean isTableHeader(){
		return tableHeader;
	}
	
	public boolean isTableFooter(){
		return tableFooter;
	}
	
	public boolean isGroupHeader(){
		return groupHeader;
	}
	
	public boolean isGroupFooter(){
		return groupFooter;
	}

	public int getColumnHeaderHeight() {
		return columnHeaderHeight;
	}

	public void setColumnHeaderHeight(int columnHeaderHeight) {
		this.columnHeaderHeight = columnHeaderHeight;
	}

	public int getColumnFooterHeight() {
		return columnFooterHeight;
	}

	public void setColumnFooterHeight(int columnFooterHeight) {
		this.columnFooterHeight = columnFooterHeight;
	}

	public int getTableHeaderHeight() {
		return tableHeaderHeight;
	}

	public void setTableHeaderHeight(int tableHeaderHeight) {
		this.tableHeaderHeight = tableHeaderHeight;
	}

	public int getTableFooterHeight() {
		return tableFooterHeight;
	}

	public void setTableFooterHeight(int tableFooterHeight) {
		this.tableFooterHeight = tableFooterHeight;
	}

	public int getGroupHeaderHeight() {
		return groupHeaderHeight;
	}

	public void setGroupHeaderHeight(int groupHeaderHeight) {
		this.groupHeaderHeight = groupHeaderHeight;
	}

	public int getGroupFooterHeight() {
		return groupFooterHeight;
	}

	public void setGroupFooterHeight(int groupFooterHeight) {
		this.groupFooterHeight = groupFooterHeight;
	}

	public int getDetailHeight() {
		return detailHeight;
	}

	public void setDetailHeight(int detailHeight) {
		this.detailHeight = detailHeight;
	}
}
