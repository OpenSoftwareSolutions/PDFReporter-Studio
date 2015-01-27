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
package com.jaspersoft.studio.property.section.report;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * Section to display the page format section in the report tab
 * @author Orlandin Marco
 *
 */
public class PageFormatSection extends AbstractSection {

	/**
	 * The report Model
	 */
	private MReport report;

	/**
	 * The container of the page format components (need to easily refresh when the format is changed)
	 */
	private Composite pageFormatPanel;

	/**
	 * The page format preview widget
	 */
	private PageFormatWidget previewWidget;

	/**
	 * The informative label
	 */
	private StyledText valuesLabel;

	/**
	 * Show the page format info, textual and visual, with the updated value
	 */
	private void setPreviewWidgetData() {
		// Read the data from the report
		Integer pageWidth = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_PAGE_WIDTH);
		Integer pageHeight = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_PAGE_HEIGHT);
		Integer colNumber = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_COLUMN_COUNT);
		Integer colWidth = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_COLUMN_WIDTH);
		Integer colSpace = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_COLUMN_SPACING);
		Integer leftMargin = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_LEFT_MARGIN);
		Integer rightMargin = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_RIGHT_MARGIN);
		Integer topMargin = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_TOP_MARGIN);
		Integer bottomMargin = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_BOTTOM_MARGIN);
		Integer orientationValue = (Integer) report.getPropertyValue(JasperDesign.PROPERTY_ORIENTATION);
		// Updating the page format preview widget
		previewWidget.setPwidth(pageWidth);
		previewWidget.setPheight(pageHeight);
		previewWidget.setCols(colNumber);
		previewWidget.setCwidth(colWidth);
		previewWidget.setSpace(colSpace);
		previewWidget.setLmargin(leftMargin);
		previewWidget.setRmargin(rightMargin);
		previewWidget.setTmargin(topMargin);
		previewWidget.setBmargin(bottomMargin);
		// Building the tooltip message
		String lineSeparator = System.getProperty("line.separator"); //$NON-NLS-1$
		String toolTipWidth = Messages.PageFormatDialog_23.concat(": ").concat(pageWidth.toString()).concat(lineSeparator); //$NON-NLS-1$
		String toolTipHeight = Messages.PageFormatDialog_25.concat(": ").concat(pageHeight.toString()) //$NON-NLS-1$
				.concat(lineSeparator);
		String toolTipColNumber = Messages.PageFormatDialog_3.concat(": ").concat(colNumber.toString()) //$NON-NLS-1$
				.concat(lineSeparator);
		String toolTipColWidth = Messages.PageFormatDialog_5.concat(": ").concat(colWidth.toString()).concat(lineSeparator); //$NON-NLS-1$
		String toolTipColSpace = Messages.PageFormatDialog_8.concat(": ").concat(colSpace.toString()).concat(lineSeparator); //$NON-NLS-1$
		String toolTipLeftMargin = Messages.PageMarginSection_left_margin_tool_tip.concat(": ") //$NON-NLS-1$
				.concat(leftMargin.toString()).concat(lineSeparator);
		String toolTipRightMargin = Messages.PageMarginSection_right_margin_tool_tip.concat(": ") //$NON-NLS-1$
				.concat(rightMargin.toString()).concat(lineSeparator);
		String toolTipTopMargin = Messages.PageMarginSection_top_margin_tool_tip.concat(": ").concat(topMargin.toString()) //$NON-NLS-1$
				.concat(lineSeparator);
		String toolTipBottomMargin = Messages.PageMarginSection_bottom_margin_tool_tip.concat(": ").concat( //$NON-NLS-1$
				bottomMargin.toString());
		previewWidget.getCanvas()
				.setToolTipText(
						(toolTipWidth.concat(toolTipHeight).concat(toolTipColNumber).concat(toolTipColWidth)
								.concat(toolTipColSpace).concat(toolTipLeftMargin).concat(toolTipRightMargin).concat(toolTipTopMargin)
								.concat(toolTipBottomMargin)));
		GridData gd = new GridData(GridData.FILL_BOTH);
		String orientation;
		if (orientationValue == 0) {
			orientation = Messages.PageFormatDialog_19;
			gd.heightHint = 150;
			gd.widthHint = 100;
		} else {
			orientation = Messages.PageFormatDialog_20;
			gd.heightHint = 100;
			gd.widthHint = 150;
		}
		valuesLabel.setText(pageWidth.toString().concat("x").concat(pageHeight.toString()).concat(lineSeparator) //$NON-NLS-1$
				.concat(orientation));
		previewWidget.setLayoutData(gd);
		pageFormatPanel.layout();
		previewWidget.setTBounds();
		previewWidget.getCanvas().redraw();
	}

	/**
	 * Open the dialog to edit the page format, the if closed with the Ok button the preview will be refreshed
	 */
	private void openEditDialog() {
		PageFormatDialog dlg = new PageFormatDialog(Display.getCurrent().getActiveShell(), report);
		if (dlg.open() == Window.OK) {
			getEditDomain().getCommandStack().execute(dlg.getCommand());
			setPreviewWidgetData();
		}
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		// Adding the page format section
		pageFormatPanel = getWidgetFactory().createSection(parent, Messages.PageFormatSection_sectionTitle, true, 1);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalAlignment = SWT.CENTER;
		pageFormatPanel.setLayoutData(gd);
		report = (MReport) getElement();
		Composite previewInfo = new Composite(pageFormatPanel, SWT.NONE);
		previewInfo.setLayout(new GridLayout(2, false));
		previewInfo.setLayoutData(gd);
		// Adding the previw widget
		previewWidget = new PageFormatWidget(previewInfo, SWT.NONE);
		previewWidget.getCanvas().addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
				openEditDialog();
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
		});
		// Adding the textual info
		Composite textualInfo = new Composite(previewInfo, SWT.NONE);
		gd = new GridData();
		gd.verticalAlignment = SWT.TOP;
		textualInfo.setLayout(new GridLayout(2, false));
		textualInfo.setLayoutData(gd);
		Label infoLabel = new Label(textualInfo, SWT.NONE);
		infoLabel.setText(Messages.PageFormatSection_pageLabel);
		valuesLabel = new StyledText(textualInfo, SWT.READ_ONLY);
		valuesLabel.setEnabled(false);
		// Add the button
		Button editLayoutButton = getWidgetFactory().createButton(pageFormatPanel, Messages.PageFormatSection_buttonText, SWT.PUSH);
		gd = new GridData();
		gd.horizontalAlignment = SWT.CENTER;
		editLayoutButton.setLayoutData(gd);
		editLayoutButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openEditDialog();
			}
		});
		// Set the datafiels
		setPreviewWidgetData();
	}
}
