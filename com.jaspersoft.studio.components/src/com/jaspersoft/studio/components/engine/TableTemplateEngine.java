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
package com.jaspersoft.studio.components.engine;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

import org.eclipse.draw2d.ColorConstants;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.column.command.CreateColumnCommand;
import com.jaspersoft.studio.components.table.model.dialog.ApplyTableStyleAction;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle.BorderStyleEnum;
import com.jaspersoft.studio.components.table.model.table.command.wizard.TableSections;
import com.jaspersoft.studio.components.table.model.table.command.wizard.TableWizardLayoutPage;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.property.color.ColorSchemaGenerator;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.templates.engine.DefaultTemplateEngine;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.templates.ReportBundle;
import com.jaspersoft.templates.TemplateBundle;
import com.jaspersoft.templates.TemplateEngineException;

/**
 * Template engine to build a report with a table in the summary, from a TableTemplate
 * 
 * @author Orlandin Marco
 *
 */
public class TableTemplateEngine extends DefaultTemplateEngine {

	/**
	 * Text inside a textfield placeholder
	 */
	public static final String FIELD_MARKER = "field";
	
	
	public static final String GROUP_MARKER = "groupfield";
	
	/**
	 * Text inside a textfield placeholder, before JSS the 5.6.1
	 */
	private static final String FIELD_COMPATIBILITY_MARKER = "detailfield";
	
	/**
	 * Text inside a static text placeholder
	 */
	public static final String TEXT_MARKER = "label";
	
	/**
	 * The list of styles that will applied to the table, the styles order is important, and it should be
	 * Table Style, Table Header, Column Header and detail
	 */
	private List<JRDesignStyle> stylesList;
	
	/**
	 * The list of fields of the table
	 */
	private List<Object> tableFields;
	
	/**
	 * The list of group of the table
	 */
	private List<Object> groupFields;
	
	/**
	 * List of the design element that are inside the column header of the template
	 */
	private List<JRDesignElement> colHeaderContent;
	
	/**
	 * List of the design element that are inside the column footer of the template
	 */
	private List<JRDesignElement> colFooterContent;
	
	/**
	 * List of the design element that are inside the table header of the template
	 */
	private List<JRDesignElement> tableHeaderContent;
	
	/**
	 * Sample of the text element that should be used inside the table group cells
	 */
	private List<List<JRDesignElement>> tableGroupField;
	
	/**
	 * List of the design element that are inside the detail of the template
	 */
	private List<JRDesignElement> detailContent;
	
	/**
	 * List of the design element that are inside the table footer of the template
	 */
	private List<JRDesignElement> tableFooterContent;
	
	/**
	 * Width of the table in the template
	 */
	private int tableWidth = 200;
	
	/**
	 * Height of the table
	 */
	private int tableHeight = 200;
	
	/**
	 * The width of the column in the template
	 */
	private int columnWidth = 40;

	/**
	 * The width of the group in the template
	 */
	private int templateGroupWidth = 200;
	
	/**
	 * X position of the table
	 */
	private int tableX = 0;
	
	/**
	 * Y position of the table
	 */
	private int tableY = 0;
	
	/**
	 * Section to display of the table
	 */
	private TableSections sections;
	
	/**
	 * The height of the summary
	 */
	private int summaryHeight;
	
	/**
	 * Create a column of the table of the specified width. The section in the new column
	 * will be the same one of the column of the template and also the content. The size
	 * and position of the content will be relative to the width of the new column that 
	 * may be different from the one of the template.
	 * 
	 *  
	 * @param tbl the table 
	 * @param jd the jasper design
	 * @param fieldName the column header
	 * @param fieldValue the field value
	 * @param colWidth the column width
	 */
	private StandardColumn generateColumn(StandardTable tbl, JasperDesign jd, String fieldName, String fieldValue, int colWidth){
		StandardColumn col = CreateColumnCommand.addColumn(jd, tbl,
				sections.isTableHeader(), sections.isTableFooter(),
				sections.isColumnHeader(), sections.isColumnFooter(),
				sections.isGroupHeader(), sections.isGroupFooter(), -1);
		col.setWidth(colWidth);
		
		
		if (sections.isTableHeader()){
			DesignCell tableHeader = (DesignCell)col.getTableHeader();
			tableHeader.setHeight(sections.getTableHeaderHeight());
			for(JRDesignElement element : tableHeaderContent){
				JRDesignElement copyElement = (JRDesignElement)element.clone();
				copyElement.setX(getRelativeWidth(colWidth, copyElement.getX()));
				copyElement.setWidth(getRelativeWidth(colWidth, copyElement.getWidth()));
				tableHeader.addElement(copyElement);
			}
		}
		
		if (sections.isTableFooter()){
			DesignCell tableFooter = (DesignCell)col.getTableFooter();
			tableFooter.setHeight(sections.getTableFooterHeight());
			for(JRDesignElement element : tableFooterContent){
				JRDesignElement copyElement = (JRDesignElement)element.clone();
				copyElement.setX(getRelativeWidth(colWidth, copyElement.getX()));
				copyElement.setWidth(getRelativeWidth(colWidth, copyElement.getWidth()));
				tableFooter.addElement(copyElement);
			}
		}
		
		if (sections.isColumnFooter()){
			DesignCell columnFooter = (DesignCell)col.getColumnFooter();
			columnFooter.setHeight(sections.getColumnFooterHeight());
			for(JRDesignElement element : colFooterContent){
				JRDesignElement copyElement = (JRDesignElement)element.clone();
				copyElement.setX(getRelativeWidth(colWidth, copyElement.getX()));
				copyElement.setWidth(getRelativeWidth(colWidth, copyElement.getWidth()));
				columnFooter.addElement(copyElement);
			}
		}
		
		//Create the column header
		if (sections.isColumnHeader()) {
			DesignCell columnHeader = (DesignCell)col.getColumnHeader();
			columnHeader.setHeight(sections.getColumnHeaderHeight());
			for(JRDesignElement element : colHeaderContent){
				JRDesignElement copyElement = (JRDesignElement)element.clone();
				copyElement.setX(getRelativeWidth(colWidth, copyElement.getX()));
				copyElement.setWidth(getRelativeWidth(colWidth, copyElement.getWidth()));
				
				if (copyElement instanceof JRDesignStaticText){
					JRDesignStaticText text = (JRDesignStaticText)copyElement;
					if (isColumnHeaderPlaceholder(text)){
						text.setText(fieldName);
					}
				}
		
				columnHeader.addElement(copyElement);
			}
		}
		
		DesignCell detailCell = (DesignCell)col.getDetailCell();
		detailCell.setHeight(sections.getDetailHeight());
		for(JRDesignElement element : detailContent){
			JRDesignElement copyElement = (JRDesignElement)element.clone();
			copyElement.setX(getRelativeWidth(colWidth, copyElement.getX()));
			copyElement.setWidth(getRelativeWidth(colWidth, copyElement.getWidth()));
			
			if (copyElement instanceof JRDesignTextField){
				JRDesignTextField field = (JRDesignTextField)copyElement;
				if (isDetailPlaceholder(field)){
					JRDesignExpression jre = new JRDesignExpression();
					jre.setText(fieldValue);
					field.setExpression(jre);
				}
			}
	
			detailCell.addElement(copyElement);
		}
		return col;
	}
	
	/**
	 * Create a group column of the table, with a width of the whole table. the content in the new column
 	 * The size and position of the content will be relative to the width of the new column that 
	 * may be different from the one of the template.
	 * 
	 *  
	 * @param groupCell cell of the group where the element read from the template are placed
	 * @param groupIndex number of the group
	 * @param newGroupWidth width of the group in the resulting table
	 * @param groupExpression the expression of the groupfield
	 * @param jd the jasperdesign
	 */
	private void createGroupCell(DesignCell groupCell, int groupIndex, int newGroupWidth, JRDesignExpression groupExpression, JasperDesign jd){
		List<JRDesignElement> groupHeaderContent = null;
		if (tableGroupField.size() > groupIndex){
			groupHeaderContent = tableGroupField.get(groupIndex);
		} else if (!tableGroupField.isEmpty()){
			groupHeaderContent = tableGroupField.get(tableGroupField.size()-1);
		}
		if (groupHeaderContent != null){
			for(JRDesignElement element : groupHeaderContent){
				JRDesignElement copyElement = (JRDesignElement)element.clone();
				copyElement.setX(getRelativeWidth(newGroupWidth, copyElement.getX(), templateGroupWidth));
				copyElement.setWidth(getRelativeWidth(newGroupWidth, copyElement.getWidth(), templateGroupWidth));
				
				if (copyElement instanceof JRDesignTextField){
					JRDesignTextField field = (JRDesignTextField)copyElement;
					if (isGroupPlaceholder(field)){
						field.setExpression(groupExpression);
					}
				}
				
				groupCell.addElement(copyElement);
			}
		} else {
			JRDesignTextField sText = new MTextField().createJRElement(jd);
			sText.setWidth(newGroupWidth);
			sText.setHeight(groupCell.getHeight());
			sText.setX(0);
			sText.setY(0);
			sText.setExpression(groupExpression);
			groupCell.addElement(sText);
		}
	}
	
	
	/**
	 * Check if a JRDesignText element is a placeholder for the static text appearance in
	 * the column header
	 * 
	 * @param element the element
	 * @return true if it is a place holder, false otherwise
	 */
	private boolean isColumnHeaderPlaceholder(JRDesignStaticText element){
		if (element.getText() != null && element.getText().toLowerCase().equals(TEXT_MARKER)){
				return true;
		}
		return false;
	}
	
	/**
	 * Check if a JRDesignTextField element is a placeholder for the text field appearance in
	 * the detail
	 * 
	 * @param element the element
	 * @return true if it is a place holder, false otherwise
	 */
	private boolean isDetailPlaceholder(JRDesignTextField element){
		JRExpression expression = element.getExpression();
		if (expression != null && expression.getText() != null){
			String text = expression.getText().toLowerCase();
			if (text.startsWith("\"")) { //$NON-NLS-1$
				text = text.substring(1);
			}
			if (text.endsWith("\"")) { //$NON-NLS-1$
				text = text.substring(0, text.length() - 1);
			}
			if (text.equals(FIELD_MARKER) || text.equals(FIELD_COMPATIBILITY_MARKER)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Check if a JRDesignTextField element is a placeholder for the text field appearance in
	 * the detail
	 * 
	 * @param element the element
	 * @return true if it is a place holder, false otherwise
	 */
	private boolean isGroupPlaceholder(JRDesignTextField element){
		JRExpression expression = element.getExpression();
		if (expression != null && expression.getText() != null){
			String text = expression.getText().toLowerCase();
			if (text.startsWith("\"")) { //$NON-NLS-1$
				text = text.substring(1);
			}
			if (text.endsWith("\"")) { //$NON-NLS-1$
				text = text.substring(0, text.length() - 1);
			}
			if (text.equals(GROUP_MARKER)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Calculate a relative width for an element using a proportion between
	 * the width of the column in the template and the width in the generated
	 * report
	 * 
	 * @param newColwidth width of the column in the report
	 * @param elementWidth width of the element in the template
	 * @return width of the element in the report
	 */
	private int getRelativeWidth(int newColwidth, int elementWidth){
		return getRelativeWidth(newColwidth,elementWidth,columnWidth);
	}
	
	private int getRelativeWidth(int newColwidth, int elementWidth, int colWidth){
		return (newColwidth*elementWidth)/colWidth;
	}
	
	/**
	 * Request the creation of a column and add it to a Column Group
	 *  
	 * @param tbl the table 
	 * @param jd the jasper design
	 * @param fieldName the column header
	 * @param fieldValue the field value
	 * @param colWidth the column width
	 * @param parentCol the group where the column will be add
	 */
	protected void createGroupColumn(StandardTable tbl, JasperDesign jd, String fieldName, String fieldValue, int colWidth, StandardColumnGroup parentCol){
		parentCol.addColumn(generateColumn(tbl, jd, fieldName, fieldValue, colWidth));
	}
	
	/**
	 * Request the creation of a column and add it to a table
	 *  
	 * @param tbl the table 
	 * @param jd the jasper design
	 * @param fieldName the column header
	 * @param fieldValue the field value
	 * @param colWidth the column width
	 */
	private void createColumn(StandardTable tbl, JasperDesign jd, String fieldName, String fieldValue, int colWidth){
		tbl.addColumn(generateColumn(tbl, jd, fieldName, fieldValue, colWidth));
	}
	
	
	/**
	 * Return the real column height, necessary to show all the cells
	 * 
	 * @param col a column, used to calculate the height
	 * @return the table height
	 */
	private int getTableHeight(BaseColumn col){
		int height = 0;
		
		if (col.getTableHeader() != null) height += col.getTableHeader().getHeight();
		if (col.getTableFooter() != null) height += col.getTableFooter().getHeight();
		if (col.getColumnHeader()!= null) height += col.getColumnHeader().getHeight();
		if (col.getColumnFooter() != null) height += col.getColumnFooter().getHeight();
		for(GroupCell cell : col.getGroupFooters()){
			height += cell.getCell().getHeight();
		}
		for(GroupCell cell : col.getGroupHeaders()){
			height += cell.getCell().getHeight();
		}
		
		if (col instanceof StandardColumnGroup){
			StandardColumnGroup groupCol = (StandardColumnGroup)col;
			height += getTableHeight(groupCol.getColumns().get(0));
		}
		
		if (col instanceof StandardColumn){
			StandardColumn standardCol = (StandardColumn)col;
			if (standardCol.getDetailCell() != null) height += standardCol.getDetailCell().getHeight();
		}
		return height;
	}
	
	/**
	 * Build the table JRElement and return it
	 * 
	 * @param jd The JasperDesign of the report where the table will be placed
	 * @param datasetRun the dataset of the table
	 * @return a JRDesignComponentElement that contains a StandardTable
	 */
	private JRDesignElement getTable(JasperDesign jd, JRDesignDatasetRun datasetRun) {
		JRDesignComponentElement jrElement = new JRDesignComponentElement();
		StandardTable tbl = new StandardTable();
		((JRDesignComponentElement) jrElement).setComponent(tbl);
		((JRDesignComponentElement) jrElement).setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", "jr", "table")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		tbl.setDatasetRun(datasetRun);
		
		
		if (tableFields != null && tableFields.size()>0) {
			int colWidth = columnWidth;
			if (columnWidth * tableFields.size() > tableWidth) colWidth = tableWidth / tableFields.size();
			if (sections == null) sections = TableWizardLayoutPage.getDefaultSection();
			
			//If there are at least one group then a Group column will be build
			if (groupFields!= null && groupFields.size()>0)
			{	
				//The group col is wide like all the other cells of a row together
				int groupColWidth = colWidth*tableFields.size();
				StandardColumnGroup parentCol = new StandardColumnGroup();
				parentCol.setWidth(groupColWidth);
				//Create the column for the group column
				for (Object f : tableFields) {
					createGroupColumn(tbl,jd,((JRField) f).getName(),"$F{" + ((JRField) f).getName() + "}",colWidth,parentCol); //$NON-NLS-1$ //$NON-NLS-2$
				}
				int height = sections.getGroupHeaderHeight();
				//Create a spanned cell inside the column group, that take the field with the name of the group
				int groupIndex = 0;
				for(Object field : groupFields){
					JRDesignField groupField = (JRDesignField) field;
					DesignCell cell = new DesignCell();
					cell.setHeight(height);
					JRDesignExpression groupExpression = ExprUtil.setValues(new JRDesignExpression(), "$F{" + groupField.getName() + "}", groupField.getValueClassName()); //$NON-NLS-1$ //$NON-NLS-2$
					createGroupCell(cell, groupIndex, groupColWidth, groupExpression, jd);
					parentCol.setGroupHeader(groupField.getName(), cell);
					groupIndex++;
				}
				tbl.addColumn(parentCol);
				int minimumHeight = getTableHeight(parentCol);
				if (tableHeight < minimumHeight) tableHeight = minimumHeight;
			} else {
				//There are no groups, so will not be created group columns
				for (Object f : tableFields) {
					createColumn(tbl,jd,((JRField) f).getName(),"$F{" + ((JRField) f).getName() + "}",colWidth); //$NON-NLS-1$ //$NON-NLS-2$
				}
				int minimumHeight = getTableHeight((StandardColumn)tbl.getColumns().get(0));
				if (tableHeight < minimumHeight) tableHeight = minimumHeight;
			}
		} else {
			//If there are no fields defined create an empty column
			createColumn(tbl,jd,"","\"\"",160); //$NON-NLS-1$ //$NON-NLS-2$
		}
				
		//Create and apply the styles to the table. The styles should be read from the template report
		//if for some reason this styles are not present then a default set of styles will be used
		ApplyTableStyleAction applyAction;
		if (stylesList != null) applyAction = new ApplyTableStyleAction(stylesList, jrElement);
		else  applyAction = new ApplyTableStyleAction(TableWizardLayoutPage.getDefaultStyle(), jrElement);
		applyAction.applayStyle(jd);
		
		//Recalculate the real table height
		return jrElement;
	}
	
	/**
	 * From an array of JRDesignStyle try to find the style to apply to the table. The styles searched must have 
	 * a specific name: Table, Table_TH, Table_CH, Table_TD. If all this four styles are found then a List with 
	 * their references is returned, otherwise some default styles will be generated and returned
	 * 
	 * @param styleArray the array of JRDesignStyle
	 * @return a list of style that will be applied to the table
	 */
	private List<JRDesignStyle> buildStylesList(JasperDesign jd){
		JRStyle[] styleArray = jd.getStyles();
		JRDesignStyle[] result = new JRDesignStyle[4];
		for(JRStyle style : styleArray){
			if(style instanceof JRDesignStyle){
				//if (style.getName().equals("Table")) result[0] = (JRDesignStyle)style; else
				if (style.getName().equals("Table_TH")) result[1] = (JRDesignStyle)style; //$NON-NLS-1$
				else if (style.getName().equals("Table_CH")) result[2] = (JRDesignStyle)style; //$NON-NLS-1$
				else if (style.getName().equals("Table_TD")) result[3] = (JRDesignStyle)style; //$NON-NLS-1$
			}
		}
		if (result[1] == null || result[2] == null || result[3] == null ) {
			//Styles missing, generating default styles
			TableStyle defaultPattern = new TableStyle(AlfaRGB.getFullyOpaque(ColorConstants.white.getRGB()), ColorSchemaGenerator.SCHEMAS.PALE, 
															BorderStyleEnum.FULL, AlfaRGB.getFullyOpaque(ColorConstants.black.getRGB()), false);
			ApplyTableStyleAction stylesGenerator = new ApplyTableStyleAction(defaultPattern, null);
			List<JRDesignStyle> defaultStyles = stylesGenerator.createStyles(jd);
			defaultStyles.get(1).setBackcolor(Color.white);
			defaultStyles.get(2).setBackcolor(Color.white);
			defaultStyles.get(3).setBackcolor(Color.white);
			if (result[1] == null) result[1] = defaultStyles.get(1);
			if (result[2] == null) result[2] = defaultStyles.get(2);
			if (result[3] == null) result[3] = defaultStyles.get(3);
		}
		return new ArrayList<JRDesignStyle>(Arrays.asList(result));
	}
	
	
	/**
	 * Search the template table inside a JasperDesign, looking in the children of the summary band
	 * 
	 * @param jd the jasperdesign that contains the sample table
	 * @return a JRDesignComponentElement that contains a StandardTable, or null if it isn't found
	 */
	private static JRDesignComponentElement getTable(JasperDesign jd){
		if (jd.getSummary() == null) return null;
		for(JRChild child : jd.getSummary().getChildren()){
			if (child instanceof JRDesignComponentElement){
				JRDesignComponentElement component = (JRDesignComponentElement)child;
				if (component.getComponent() instanceof StandardTable) return component;
			}
		}
		//No table found, create a default one
		return null;
	}
	
	private void addSectionElementsToList(Cell section, List<JRDesignElement> currentList){
		if (section != null){
			for(JRChild child : section.getChildren()){
				if (child instanceof JRDesignElement) currentList.add((JRDesignElement)child);
			}
		}
	}
	
	private void generateTableContentList(StandardTable table){
		StandardColumn firstCol = getStandadColumn(table.getColumns().get(0));
		
		tableHeaderContent = new ArrayList<JRDesignElement>();
		addSectionElementsToList(firstCol.getTableHeader(), tableHeaderContent);
		
		tableFooterContent = new ArrayList<JRDesignElement>();
		addSectionElementsToList(firstCol.getTableFooter(), tableFooterContent);
		
		colHeaderContent = new ArrayList<JRDesignElement>();
		addSectionElementsToList(firstCol.getColumnHeader(), colHeaderContent);
		
		colFooterContent = new ArrayList<JRDesignElement>();
		addSectionElementsToList(firstCol.getColumnFooter(), colFooterContent);
		
		detailContent = new ArrayList<JRDesignElement>();
		addSectionElementsToList(firstCol.getDetailCell(), detailContent);
		
		tableGroupField = new ArrayList<List<JRDesignElement>>();
		StandardColumnGroup groupColumn = getStandadGroupColumn(table.getColumns().get(0));
		if (groupColumn != null){
			templateGroupWidth = groupColumn.getWidth();
			for(GroupCell cell : groupColumn.getGroupHeaders()){
				if (cell.getCell() != null){
					List<JRDesignElement> cellContent = new ArrayList<JRDesignElement>();
					addSectionElementsToList(cell.getCell(), cellContent);
					tableGroupField.add(cellContent);
				}
			}
		}
	}
	

	/**
	 * Initialize the fields needed to build the style of the report
	 */
	@Override
	protected void processTemplate(JasperDesign jd, List<Object> fields, List<Object> groupFields) {
		//Initialize the styles list
		stylesList = buildStylesList(jd);
		JRDesignComponentElement tableComponent = getTable(jd);
		summaryHeight = jd.getSummary().getHeight();
		/**
		 * If the template table is found it will be used to create the style of the real table and of its 
		 * content
		 */
		if (tableComponent != null){
			StandardTable table = (StandardTable)tableComponent.getComponent();
			
			tableWidth = tableComponent.getWidth();
			templateGroupWidth = tableComponent.getWidth();
			tableHeight = tableComponent.getHeight();
			columnWidth = table.getColumns().get(0).getWidth();
			tableX = tableComponent.getX();
			tableY = tableComponent.getY();
			
			generateTableContentList(table);
			
			if (table.getColumns().size()>0){
				StandardColumn col = getStandadColumn(table.getColumns().get(0));
				boolean tableHeader = col.getTableHeader() != null;
				boolean tableFooter = col.getTableFooter() != null;
				boolean columnHeader = col.getColumnHeader() != null;
				boolean columnFooter = col.getColumnFooter() != null;
				boolean groupHeader = col.getGroupHeaders() != null && col.getGroupHeaders().size()>0; 
				boolean groupFooter = col.getGroupFooters() != null && col.getGroupFooters().size()>0; 
				sections = new TableSections(tableHeader, tableFooter, columnHeader, columnFooter, groupHeader, groupFooter);
				if (tableHeader) sections.setTableHeaderHeight(col.getTableHeader().getHeight());
				if (tableFooter) sections.setTableFooterHeight(col.getTableFooter().getHeight());
				if (columnHeader) sections.setColumnHeaderHeight(col.getColumnHeader().getHeight());
				if (columnFooter) sections.setColumnFooterHeight(col.getColumnFooter().getHeight());
				if (groupHeader) sections.setGroupHeaderHeight(col.getGroupHeaders().get(0).getCell().getHeight());
				if (groupFooter) sections.setGroupHeaderHeight(col.getGroupFooters().get(0).getCell().getHeight());
				sections.setDetailHeight(col.getDetailCell().getHeight());
			}
			removeElement(jd.getSummary(), tableComponent);
		} else {
			//If the table is not found try to build the template with some default values
			JRDesignStaticText colHeaderLabel = DefaultTemplateEngine.findStaticTextElement(jd.getColumnHeader(), "Label"); //$NON-NLS-1$
			JRDesignTextField cellField = DefaultTemplateEngine.findTextFieldElement(jd.getDetailSection().getBands()[0], "Field"); //$NON-NLS-1$
			tableHeaderContent = new ArrayList<JRDesignElement>();
			tableFooterContent = new ArrayList<JRDesignElement>();
			colFooterContent = new ArrayList<JRDesignElement>();
			colHeaderContent = new ArrayList<JRDesignElement>();
			detailContent = new ArrayList<JRDesignElement>();
			tableGroupField = new ArrayList<List<JRDesignElement>>();
			List<JRDesignElement> fakeGroupPlaceHolder = new ArrayList<JRDesignElement>();
			JRDesignTextField groupElement = new MTextField().createJRElement(jd);
			groupElement.setExpression(ExprUtil.setValues(new JRDesignExpression(), "$F{Group1}", "java.Lang.Object"));
			fakeGroupPlaceHolder.add(groupElement);
			tableGroupField.add(fakeGroupPlaceHolder);
			
			if (colHeaderLabel != null) colHeaderContent.add(colHeaderLabel);
			if (cellField != null) detailContent.add(cellField);
		
			tableWidth = jd.getPageWidth()-jd.getLeftMargin()-jd.getRightMargin();
			
			if (jd.getSummary() == null){
				//I need to create the summary where place the table
				jd.setSummary(MBand.createJRBand());
				((JRDesignBand)jd.getSummary()).setHeight(jd.getDetailSection().getBands()[0].getHeight());
			}
			tableHeight = jd.getSummary().getHeight();
			tableX = 0;
			tableY = 0;
			sections = new TableSections(false, false, true, true, false, false);
			JRDesignElementGroup summaryBand = (JRDesignElementGroup)jd.getSummary();
			for(JRChild child : summaryBand.getChildren())
				summaryBand.removeElement((JRDesignElement)child);
		}
		
		removeUnwantedBand(jd);
		jd.removeDataset("tableDataset"); //$NON-NLS-1$
	}
	
	private void removeUnwantedBand(JasperDesign jd){
		/**
		 * Remove unwanted band and the placeholder dataset of the table
		 */
		jd.setColumnHeader(null);
		jd.setColumnFooter(null);
		JRDesignSection bandSection = (JRDesignSection)jd.getDetailSection();
		for(JRBand actualDetail : jd.getDetailSection().getBands())
			bandSection.removeBand(actualDetail);
		//Delete the groups
		while (jd.getGroupsList().size()>0)
			jd.getGroupsList().remove(0);
	}
	
	/**
	 * Find a JRDesignStaticText inside a table element having exp as text.
	 * 
	 * @param parent table where to search
	 * @param exp the text of the element
	 * @return the first matching element or null.
	 */
	public static JRDesignStaticText findStaticTextElement(StandardTable parent, String exp) {
		StandardColumn col = getStandadColumn(parent.getColumns().get(0));
		if (col != null){
			JRDesignStaticText result = null;
			if (col.getTableHeader() != null) result = DefaultTemplateEngine.findStaticTextElement(col.getTableHeader(), exp);
			if (col.getColumnHeader() != null && result == null) result = DefaultTemplateEngine.findStaticTextElement(col.getColumnHeader(), exp);
			if (col.getDetailCell() != null && result == null) result = DefaultTemplateEngine.findStaticTextElement(col.getDetailCell(), exp);
			return result;
		}
		return null;
	}
	
	/**
	 * Find a JRDesignTextField inside a table element having exp as expression.
	 * 
	 * @param parent table where to search
	 * @param exp the expression of the element
	 * @return the first matching element or null.
	 */
	public static JRDesignTextField findTextFieldElement(StandardTable parent, String exp) {
		StandardColumn col = getStandadColumn(parent.getColumns().get(0));
		if (col != null){
			JRDesignTextField result = null;
			if (col.getTableHeader() != null) result = DefaultTemplateEngine.findTextFieldElement(col.getTableHeader(), exp);
			if (col.getColumnHeader() != null && result == null) result = DefaultTemplateEngine.findTextFieldElement(col.getColumnHeader(), exp);
			if (col.getDetailCell() != null && result == null) result = DefaultTemplateEngine.findTextFieldElement(col.getDetailCell(), exp);
			return result;
		}
		return null;
	}
	
	/**
	 * Return the first standard group column, drilling down if the current column is a group
	 * 
	 * @return a standard group column or null if it can't be found
	 */
	private static StandardColumn getStandadColumn(BaseColumn container){
		if (container instanceof StandardColumnGroup){
			return getStandadColumn(((StandardColumnGroup)container).getColumns().get(0));
		} else if (container instanceof StandardColumn){
			return (StandardColumn)container;
		}
		return null;
	}
	
	/**
	 * Check if the passed column is a group column and return it
	 * 
	 * @param the column to check
	 * @return the parameter column casted to the correct type if it is a standard group column
	 * null otherwise
	 */
	private static StandardColumnGroup getStandadGroupColumn(BaseColumn container){
		if (container instanceof StandardColumnGroup){
			return (StandardColumnGroup)container;
		} 
		return null;
	}
	
	@Override
	public void setReportDataAdapter(ReportBundle bundle, DataAdapterDescriptor dataadapter, JRPropertiesMap properties) {
		JRDesignDataset tableDataset = (JRDesignDataset)bundle.getJasperDesign().getDatasetMap().get("tableDataset"); //$NON-NLS-1$
		JasperDesign jd = bundle.getJasperDesign();
		for (String key : properties.getPropertyNames()){
			jd.setProperty(key, properties.getProperty(key));
			if (key.contains("ireport")) tableDataset.setProperty(key, properties.getProperty(key)); //$NON-NLS-1$
		}
		tableDataset.setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, dataadapter.getName());
		jd.setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, dataadapter.getName());

		//Remove the main dataset query
		JRDesignDataset mainDataset = jd.getMainDesignDataset();

		((JRDesignQuery) mainDataset.getQuery()).setText(null);
		((JRDesignQuery) mainDataset.getQuery()).setLanguage(null);
		for (JRField field : mainDataset.getFields())
			mainDataset.removeField(field);
	}

	/**
	 * Create the report with the table inside
	 */
	@SuppressWarnings("unchecked")
	@Override
	public ReportBundle generateReportBundle(TemplateBundle template, Map<String, Object> settings, JasperReportsContext jContext)
			throws TemplateEngineException {
		//Generate the base report bundle
		ReportBundle reportBundle = super.generateReportBundle(template, settings, jContext);
		//The fields that will be added to the table
		tableFields = (List<Object>)settings.get(DefaultTemplateEngine.FIELDS);
		//Create the dataset, by default we set the dataset run to use the report connection...
		JRDesignDataset tableDataset = new JRDesignDataset(jContext, false);
		tableDataset.setName("tableDataset"); //$NON-NLS-1$
		//Create the dataset query
		JRDesignDataset dataset = (JRDesignDataset)settings.get(DefaultTemplateEngine.DATASET);
		JRDesignQuery query = new JRDesignQuery();
		if (dataset != null){
			query.setLanguage(dataset.getQuery().getLanguage());
			query.setText(dataset.getQuery().getText());
		}
		tableDataset.setQuery(query);
		//Add the fields to the dataset, check if i have an empty dataset
		if (tableFields != null){
			for(Object field : tableFields){
				try {
					tableDataset.addField((JRDesignField)field);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}
		
		//Create the groups into the dataset
		groupFields = (List<Object>) settings.get(DefaultTemplateEngine.GROUP_FIELDS);
		if (groupFields != null){
			for(Object field : groupFields){
				try {
					JRDesignGroup newGroup = new JRDesignGroup();
					JRDesignField groupField = (JRDesignField)field;
					newGroup.setName(groupField.getName());
					JRDesignExpression groupExpression = ExprUtil.setValues(new JRDesignExpression(), "$F{" +groupField.getName() + "}", groupField.getValueClassName()); //$NON-NLS-1$ //$NON-NLS-2$
					newGroup.setExpression(groupExpression);
					tableDataset.addGroup(newGroup);
					
					if (createSortFields){
						JRDesignSortField sortfield = new JRDesignSortField();
						sortfield.setType(SortFieldTypeEnum.FIELD);
						sortfield.setOrder(SortOrderEnum.DESCENDING);
						sortfield.setName(groupField.getName());
						tableDataset.addSortField(sortfield);
					}
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}
		
		JRDesignDatasetRun datasetRun = new JRDesignDatasetRun();
		JRDesignExpression exp = new JRDesignExpression();
		exp.setText("$P{REPORT_CONNECTION}"); //$NON-NLS-1$
		datasetRun.setConnectionExpression( exp );
		datasetRun.setDatasetName("tableDataset"); //$NON-NLS-1$
		try {
			reportBundle.getJasperDesign().addDataset(tableDataset);
		} catch (JRException e) {
			e.printStackTrace();
		}
		
		JasperDesign jd = reportBundle.getJasperDesign();
		jd.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL); 
		
		//Build the table and recalculate the table height
		JRDesignElement table = getTable(jd,datasetRun);
		
		//If the summary doesn't exist it will be created
		JRDesignBand summaryBand =  (JRDesignBand)jd.getSummary();
		if (summaryBand == null) {
			summaryBand = MBand.createJRBand();
			jd.setSummary(summaryBand);
		}
		
		//Set the summary and table height and width according to the new value, and add the table to the report
		//summaryBand.setHeight(tableHeight);
		summaryBand.setHeight(summaryHeight);
		table.setWidth(tableWidth);
		table.setHeight(tableHeight);
		table.setX(tableX);
		table.setY(tableY);
		summaryBand.addElement(table);
		
		return reportBundle;
	}
	
	/**
	 * Get a JasperDesign and check if that JasperDesign can be used as Template and processed
	 * by this engine. 
	 * 
	 * @param design the design to check
	 * @return a List of founded error, the list is void if no error are found
	 */
	public static List<String> validateJasperDesig(JasperDesign design){
		
		List<String> errorsList = new ArrayList<String>();
		
		boolean[] foundedStyles = {false, false,false};
		
		JRStyle[] styleArray = design.getStyles();
		for(JRStyle style : styleArray){
			if(style instanceof JRDesignStyle){
				if (style.getName().equals("Table_TH")) foundedStyles[0] = true;  //$NON-NLS-1$
				else if (style.getName().equals("Table_CH")) foundedStyles[1] = true; //$NON-NLS-1$
				else if (style.getName().equals("Table_TD")) foundedStyles[2] = true; //$NON-NLS-1$
			}
		}
		if (!foundedStyles[0]) errorsList.add(Messages.TableTemplateEngine_missingStyleTH);
		if (!foundedStyles[1]) errorsList.add(Messages.TableTemplateEngine_missingStyleCH);
		if (!foundedStyles[2]) errorsList.add(Messages.TableTemplateEngine_missingStyleD);
		
		JRDesignComponentElement tableComponent = getTable(design);
		if (tableComponent == null) errorsList.add(Messages.TableTemplateEngine_missingTable);
		else{
			StandardTable table = (StandardTable)tableComponent.getComponent();
			
			if (table.getColumns().size()>1 || table.getColumns().size() < 1) {
				errorsList.add(Messages.TableTemplateEngine_oneColumnError); 
			} else {
				if (findStaticTextElement(table, TEXT_MARKER) == null) errorsList.add(Messages.TableTemplateEngine_missingStaticText); //$NON-NLS-1$
				if (findTextFieldElement(table, FIELD_MARKER) == null) errorsList.add(Messages.TableTemplateEngine_missingTextField); //$NON-NLS-1$
			}
		}
		
		return errorsList;
	}
	
}
