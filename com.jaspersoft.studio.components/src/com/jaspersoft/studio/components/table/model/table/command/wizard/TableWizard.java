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

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.ModeEnum;

import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.command.CreateColumnCommand;
import com.jaspersoft.studio.components.table.model.dialog.ApplyTableStyleAction;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle;
import com.jaspersoft.studio.components.table.model.dialog.TableStyle.BorderStyleEnum;
import com.jaspersoft.studio.model.dataset.command.CreateDatasetCommand;
import com.jaspersoft.studio.model.style.command.CreateStyleCommand;
import com.jaspersoft.studio.model.text.MStaticText;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.property.dataset.wizard.WizardConnectionPage;
import com.jaspersoft.studio.property.dataset.wizard.WizardDatasetPage;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.wizards.JSSWizard;

public class TableWizard extends JSSWizard {
	private WizardDatasetPage step1;
	private TableWizardFieldsPage step3;
	private WizardConnectionPage step2;
	private TableWizardLayoutPage step4;
	private MTable table = null;;
	
	
	
	float baseColor = new Float(Math.tan(Math.toRadians(208.0)));

	public TableWizard() {
		super();
		setWindowTitle(Messages.common_table_wizard);
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		
		step1 = new WizardDatasetPage(false, "Table");
		addPage(step1);
		
		step2 = new WizardConnectionPage();
		addPage(step2);
		
		
		// Setting up the expressions context. This is not really useful, since
		// we still don't know where the element will be added, so this call will fall back to the default dataset.
		// FIXME: pass a proper ANode to the wizard to let the code to lookup for a more appropriate dataset.
		step2.setExpressionContext(ModelUtils.getElementExpressionContext(null, null));

		step3 = new TableWizardFieldsPage();
		addPage(step3);

		step4 = new TableWizardLayoutPage();
		addPage(step4);
	}

	/**
	 * The getNextPage implementations does nothing, since all the logic has
	 * been moved inside each page, specifically extended for
	 * this wizard
	 * 
	 * @see com.jaspersoft.studio.wizards.JSSWizard#getNextPage(org.eclipse.jface.wizard.IWizardPage)
	 *
	 * @param the current page.
	 *
	 * @return the next page
	 */
	@Override
	public IWizardPage getNextPage(IWizardPage page) {
		
		// Nothing to do. If you change this method, please update the
		// comment.
		
		return super.getNextPage(page);
	}
	
	/**
	 * This method returns a dataset object
	 * based on what has been selected in the first step
	 * of the wizard (existing dataset, main dataset, new dataset, etc...)
	 * 
	 *  @return JRDesignDataset
	 */
	public JRDesignDataset getDataset() {
		return step1.getSelectedDataset();
	}
		

	/* ************************************************************** */
	// Table generation code...
	
	
	
	/**
	 * 
	 * Generates the table created by this wizard.
	 * This method will generate the table only the first time it is called, then
	 * a cached version will be returned, this because the creation of the table
	 * involved the creation of a set of commands, and we don't want to create
	 * commands twice. The second time the call is made, the cached table will be
	 * returned.</br>
	 * </br>
	 * Please note that if this method is invoked before the end of the wizard, the final table may
	 * result incomplete.
	 * 
	 * 
	 * @param tableWidth
	 * 				An optional width to be used as size of the table to create. This will help
	 *              to calculate the columns width.
	 *
	 *  @return MTable
	 *  			An MTable object with a JasperReports configuration attached.
	 */
	public MTable getTable(int tableWidth) {
		
		if (table != null) return table;
		
		table = new MTable();
		table.setValue(table.createJRElement(getConfig().getJasperDesign()));
		table.setJasperConfiguration(getConfig());
	
		List<Object> lst = step3.getSelectedFields();
		
		StandardTable tbl = TableManager.getTable(table);

		// Configure a proper dataset run...
		JRDesignDataset dataset = getDataset();
		
		JRDesignDatasetRun datasetRun = step2.getJRDesignDatasetRun();
		if (datasetRun == null)
		{
			datasetRun = new JRDesignDatasetRun();
			
		}
		datasetRun.setDatasetName( dataset.isMainDataset() ? null : dataset.getName() );
		tbl.setDatasetRun(datasetRun);
		
		// Get the connection/datasource expression from the proper wizard step...
		JasperDesign jd = getConfig().getJasperDesign();

		if (tbl != null && lst != null) {
			int colWidth = 40;
			if (tableWidth < 0)
				tableWidth = table.getDefaultWidth();
			if (lst.size() > 0)
				colWidth = tableWidth / lst.size();
			for (Object f : lst) {
				StandardColumn col = CreateColumnCommand.addColumn(jd, tbl,
						step4.isTableHeader(), step4.isTableFooter(),
						step4.isColumnHeader(), step4.isColumnFooter(),
						step4.isGroupHeader(), step4.isGroupFooter(), -1);
				col.setWidth(colWidth);
				DesignCell colHeadCell = (DesignCell) col.getColumnHeader();
				DesignCell detCell = (DesignCell) col.getDetailCell();
				if (step4.isColumnHeader()) {
					JRDesignStaticText sText = (JRDesignStaticText) new MStaticText()
							.createJRElement(jd);
					sText.setWidth(col.getWidth());
					sText.setHeight(colHeadCell.getHeight());
					sText.setText(((JRField) f).getName());
					colHeadCell.addElement(sText);
				}
				JRDesignTextField fText = (JRDesignTextField) new MTextField()
						.createJRElement(jd);
				fText.setWidth(col.getWidth());
				fText.setHeight(detCell.getHeight());
				JRDesignExpression jre = new JRDesignExpression();
				jre.setText("$F{" + ((JRField) f).getName() + "}");//$NON-NLS-1$ //$NON-NLS-2$
				fText.setExpression(jre);
				detCell.addElement(fText);
				tbl.addColumn(col);
			}
		}
		String dsname = (String) tbl.getDatasetRun().getDatasetName();
		if (dsname == null || dsname.trim().isEmpty()) {
			// create an empty dataset
			JRDesignDataset jrDataset = new JRDesignDataset(false);
			jrDataset.setName(ModelUtils.getDefaultName(jd.getDatasetMap(),
					"Empty Dataset"));
			addCommand(new CreateDatasetCommand(getConfig(), jrDataset));
			((JRDesignDatasetRun) tbl.getDatasetRun()).setDatasetName(jrDataset
					.getName());
		}

		//Apply the style to the table
		ApplyTableStyleAction applyAction = new ApplyTableStyleAction(step4.getSelectedStyle(), table.getValue());
		applyAction.applayStyle(jd);
		return table;
	}


	
	/**
	 * Set all the borders of a JR style to a precise width
	 * 
	 * @param element a JR style
	 * @param lineWidth the width
	 */
	private void setBorderWidth(JRDesignStyle element, float lineWidth){
		JRLineBox box = element.getLineBox();
		box.getPen().setLineWidth(lineWidth);
		box.getLeftPen().setLineWidth(lineWidth);
		box.getRightPen().setLineWidth(lineWidth);
		box.getBottomPen().setLineWidth(lineWidth);
		box.getTopPen().setLineWidth(lineWidth);
	}
	
	/**
	 * Set all the borders of a JR style to a precise color
	 * 
	 * @param element a JR style
	 * @param lineWidth the width
	 */
	private void setBorderColor(JRDesignStyle element, Color lineColor){
		JRLineBox box = element.getLineBox();
		box.getPen().setLineColor(lineColor);
		box.getLeftPen().setLineColor(lineColor);
		box.getRightPen().setLineColor(lineColor);
		box.getBottomPen().setLineColor(lineColor);
		box.getTopPen().setLineColor(lineColor);
	}
	
	/**
	 * Starting from a TableStyle it generate a list of styles that will be applied to the table.
	 * For every style generated will be executed an addCommand to add them to the report
	 * 
	 * @param jd the jasperdesign
	 * @param style the TableStyle from where all the styles for the table will be generated
	 * @return a list of style that can be applied to the table
	 */
    public List<JRDesignStyle> createStyles(JasperDesign jd, TableStyle style)
    {
    	String baseName = "Table";
		
		for (int i = 0;; i++) {
			String name = baseName;
			if (i > 0) {
				name = baseName + " " + i;
			}

			if (!(jd.getStylesMap().containsKey(name))) {
				baseName = name;
				break;
			}
		}
    	
        List<JRDesignStyle> styles = new ArrayList<JRDesignStyle>();

        JRDesignStyle tableStyle=  new JRDesignStyle();
        tableStyle.setName(baseName);

        if (style.getBorderStyle() == BorderStyleEnum.FULL || style.getBorderStyle() == BorderStyleEnum.PARTIAL_VERTICAL)
        {
            setBorderColor(tableStyle, style.getBorderColor());
            setBorderWidth(tableStyle, 1.0f);
        }
        else
        {
            tableStyle.getLineBox().getTopPen().setLineColor(style.getBorderColor());
            tableStyle.getLineBox().getTopPen().setLineWidth(1.0f);
            tableStyle.getLineBox().getBottomPen().setLineColor(style.getBorderColor());
            tableStyle.getLineBox().getBottomPen().setLineWidth(1.0f);
        }

        addCommand( new CreateStyleCommand(jd, tableStyle));
        styles.add(tableStyle);

        JRDesignStyle tableHeaderStyle=  new JRDesignStyle();
        tableHeaderStyle.setName(baseName + "_TH");

        if (style.getBorderStyle() == BorderStyleEnum.FULL)
        {
            setBorderColor(tableHeaderStyle, style.getBorderColor());
            setBorderWidth(tableHeaderStyle, 0.5f);
        }
        else
        {
            tableHeaderStyle.getLineBox().getBottomPen().setLineColor(style.getBorderColor());
            tableHeaderStyle.getLineBox().getBottomPen().setLineWidth(0.5f);
            tableHeaderStyle.getLineBox().getTopPen().setLineColor(style.getBorderColor());
            tableHeaderStyle.getLineBox().getTopPen().setLineWidth(0.5f);
        }

        tableHeaderStyle.setMode(ModeEnum.OPAQUE);
        tableHeaderStyle.setBackcolor(style.getColorValue(TableStyle.COLOR_TABLE_HEADER));

        addCommand( new CreateStyleCommand(jd, tableHeaderStyle));
        styles.add(tableHeaderStyle);

        JRDesignStyle columnHeaderStyle=  new JRDesignStyle();
        columnHeaderStyle.setName(baseName + "_CH");

        if (style.getBorderStyle() == BorderStyleEnum.FULL)
        {
            setBorderColor(columnHeaderStyle, style.getBorderColor());
            setBorderWidth(columnHeaderStyle, 0.5f);
        }
        else
        {
            columnHeaderStyle.getLineBox().getBottomPen().setLineColor(style.getBorderColor());
            columnHeaderStyle.getLineBox().getBottomPen().setLineWidth(0.5f);
            columnHeaderStyle.getLineBox().getTopPen().setLineColor(style.getBorderColor());
            columnHeaderStyle.getLineBox().getTopPen().setLineWidth(0.5f);
        }

        columnHeaderStyle.setMode(ModeEnum.OPAQUE);
        columnHeaderStyle.setBackcolor(style.getColorValue(TableStyle.COLOR_COL_HEADER));

        addCommand( new CreateStyleCommand(jd, columnHeaderStyle));
        styles.add(columnHeaderStyle);

        JRDesignStyle cellStyle=  new JRDesignStyle();
        cellStyle.setName(baseName + "_TD");

        if (style.getBorderStyle() == BorderStyleEnum.FULL)
        {
            setBorderColor(cellStyle, style.getBorderColor());
            setBorderWidth(cellStyle, 0.5f);
        }
        else
        {
            cellStyle.getLineBox().getBottomPen().setLineColor(style.getBorderColor());
            cellStyle.getLineBox().getBottomPen().setLineWidth(0.5f);
            cellStyle.getLineBox().getTopPen().setLineColor(style.getBorderColor());
            cellStyle.getLineBox().getTopPen().setLineWidth(0.5f);
        }

        cellStyle.setMode(ModeEnum.OPAQUE);
        cellStyle.setBackcolor(Color.WHITE);


        if (style.hasAlternateColor())
        {
            JRDesignConditionalStyle condStyle = new JRDesignConditionalStyle();
            condStyle.setConditionExpression(ModelUtils.createExpression("new Boolean($V{REPORT_COUNT}.intValue()%2==0)"));
            condStyle.setBackcolor(style.getColorValue(TableStyle.COLOR_DETAIL));
            cellStyle.addConditionalStyle(condStyle);
        }

        addCommand( new CreateStyleCommand(jd, cellStyle));
        styles.add(cellStyle);


        return styles;
    }	
	
}
