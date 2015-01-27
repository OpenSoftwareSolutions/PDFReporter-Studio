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
package com.jaspersoft.studio.components.chart.property.widget;

import java.util.List;

import net.sf.jasperreports.charts.JRDataRange;
import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.util.JRMeterInterval;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorCellEditor;
import com.jaspersoft.studio.property.descriptor.color.ColorLabelProvider;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionCellEditor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;
import com.jaspersoft.studio.utils.Misc;

/**
 * Dialog with a table that show all the meter intervals defined, and allow to edit, move
 * delete and add them
 * 
 * @author Orlandin Marco
 *
 */
public class MeterIntervalsDialog extends Dialog {
	
	/**
	 * Section used to get the selected element
	 */
	private AbstractSection section;
	
	/**
	 * Descriptor of the property
	 */
	private IPropertyDescriptor pDescriptor;
	
	/**
	 * List of the intervals actually shown in the table
	 */
	private List<JRMeterInterval> intervalsList;
	
	/**
	 * Table where the intervals are shown
	 */
	private Table table;
	
	/**
	 * Table viewer
	 */
	private TableViewer tableViewer;
	
	/**
	 * Composite where the table is placed
	 */
	private Composite sectioncmp;
	
	/**
	 * Cell editor for the low expression
	 */
	private JRExpressionCellEditor lowExp;
	
	/**
	 * Cell editor for the high expression
	 */
	private JRExpressionCellEditor highExp;
	
	/**
	 * Create the dialog
	 * 
	 * @param parentShell parent shell
	 * @param section section of the element
	 * @param pDescriptor descriptor of the intervals property
	 * @param intervalsList list of the intervals already inside the meter chart
	 */
	public MeterIntervalsDialog(Shell parentShell, AbstractSection section, IPropertyDescriptor pDescriptor, List<JRMeterInterval> intervalsList) {
		super(parentShell);
		this.pDescriptor = pDescriptor;
		this.intervalsList = intervalsList;
		this.section = section;
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.MeterIntervalsDialog_dialogTitle);
	}

	/**
	 * 
	 * Custom label provider for the table
	 *
	 */
	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {
		private ColorLabelProvider colorLabel = new ColorLabelProvider(NullEnum.NULL);

		/**
		 * Return an image only on the second column of the table, the one with the color. The
		 * image show a sample of the color
		 */
		public Image getColumnImage(Object element, int columnIndex) {
			JRMeterInterval mi = (JRMeterInterval) element;
			switch (columnIndex) {
			case 1:
				AlfaRGB color = Colors.getSWTRGB4AWTGBColor(mi.getBackgroundColor());
				Double alfa = mi.getAlphaDouble();
				color.setAlfa(alfa != null ? alfa : 1.0d);
				return colorLabel.getImage(color);
			}
			return null;
		}
		
		/**
		 * Return an appropriate string for every column of the table
		 */
		public String getColumnText(Object element, int columnIndex) {
			JRMeterInterval mi = (JRMeterInterval) element;
			JRDataRange dataRange = mi.getDataRange();

			switch (columnIndex) {
			case 0:
				return Misc.nvl(mi.getLabel(), ""); //$NON-NLS-1$
			case 1:
				AlfaRGB color = Colors.getSWTRGB4AWTGBColor(mi.getBackgroundColor());
				Double alfa = mi.getAlphaDouble();
				color.setAlfa(alfa != null ? alfa : 1.0d);
				RGB rgb = color.getRgb();
				return "RGBA (" + rgb.red + "," + rgb.green + "," + rgb.blue + "," + color.getAlfa()+")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			case 2:
				if (dataRange != null) {
					JRExpression lowe = dataRange.getLowExpression();
					return lowe != null ? lowe.getText() : ""; //$NON-NLS-1$
				}
				break;
			case 3:
				if (dataRange != null) {
					JRExpression highe = dataRange.getHighExpression();
					return highe != null ? highe.getText() : ""; //$NON-NLS-1$
				}
				break;
			}
			return ""; //$NON-NLS-1$
		}
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		sectioncmp = (Composite)super.createDialogArea(parent);
		sectioncmp = new Composite(sectioncmp, SWT.NONE);
		sectioncmp.setLayout(new GridLayout(2,false));
		sectioncmp.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		Composite bGroup = new Composite(sectioncmp, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		
		buildTable(sectioncmp);
	
		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				NewMeterIntervalWizard wizard = new NewMeterIntervalWizard();
				WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
				if (dialog.open() == WizardDialog.OK){
					return wizard.getMeterInterval();
				} else return null;
			}
		});

		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);
		table.setToolTipText(pDescriptor.getDescription());
		
		//Set the content of the table
		APropertyNode selctedNode = section.getElement();
		if (selctedNode != null) {
			ExpressionContext expContext = new ExpressionContext(selctedNode.getJasperConfiguration());
			lowExp.setExpressionContext(expContext);
			highExp.setExpressionContext(expContext);
		}
		tableViewer.setInput(intervalsList);
		return sectioncmp;
	}

	/**
	 * Create the table element with all the cell editors
	 * 
	 * @param composite parent of the table
	 */
	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 580;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new TLabelProvider());
		attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(25));
		tlayout.addColumnData(new ColumnWeightData(25));
		tlayout.addColumnData(new ColumnWeightData(25));
		tlayout.addColumnData(new ColumnWeightData(25));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[4];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.MeterIntervalsDialog_label);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.MeterIntervalsDialog_background);

		column[2] = new TableColumn(table, SWT.NONE);
		column[2].setText(Messages.MeterIntervalsDialog_lowExpression);

		column[3] = new TableColumn(table, SWT.NONE);
		column[3].setText(Messages.MeterIntervalsDialog_highExpression);

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();
	}

	/**
	 * Attach the cell editor to the table
	 * 
	 * @param viewer viewer of the table
	 * @param parent the table
	 */
	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			
			//Every column can be modfied
			public boolean canModify(Object element, String property) {
				if (property.equals("LABEL")) //$NON-NLS-1$
					return true;
				if (property.equals("COLOR")) //$NON-NLS-1$
					return true;
				if (property.equals("HIGH")) //$NON-NLS-1$
					return true;
				if (property.equals("LOW")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRMeterInterval mi = (JRMeterInterval) element;
				if (property.equals("LABEL"))//$NON-NLS-1$
					return mi.getLabel();
				if (property.equals("COLOR")){//$NON-NLS-1$
					AlfaRGB color = Colors.getSWTRGB4AWTGBColor(mi.getBackgroundColor());
					Double alfa = mi.getAlphaDouble();
					color.setAlfa(alfa != null ? alfa : 1.0d);
					return color;
				}
				if (property.equals("HIGH"))//$NON-NLS-1$
					return mi.getDataRange().getHighExpression();
				if (property.equals("LOW"))//$NON-NLS-1$
					return mi.getDataRange().getLowExpression();
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem ti = (TableItem) element;
				JRMeterInterval mi = (JRMeterInterval) ti.getData();
				if (property.equals("LABEL")) {//$NON-NLS-1$
					mi.setLabel((String) value);
				}
				if (property.equals("COLOR")) {//$NON-NLS-1$
					AlfaRGB argb = (AlfaRGB) value;
					mi.setBackgroundColor(Colors.getAWT4SWTRGBColor(argb));
					mi.setAlpha(argb.getAlfa() / 255.0d);
				}
				if (property.equals("HIGH")) {//$NON-NLS-1$
					((JRDesignDataRange) mi.getDataRange()).setHighExpression((JRExpression) value);
				}
				if (property.equals("LOW")) {//$NON-NLS-1$
					((JRDesignDataRange) mi.getDataRange()).setLowExpression((JRExpression) value);
				}
				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
				propertyChange();
			}
		});

		lowExp = new JRExpressionCellEditor(parent, null);
		highExp = new JRExpressionCellEditor(parent, null);
		ColorCellEditor argbColor = new ColorCellEditor(parent){
			@Override
			protected void updateContents(Object value) {
				AlfaRGB argb = (AlfaRGB) value;
				if (argb == null) {
					rgbLabel.setText(""); //$NON-NLS-1$
				} else {
					RGB rgb = argb.getRgb();
					rgbLabel.setText("RGBA (" + rgb.red + "," + rgb.green + "," + rgb.blue + "," + argb.getAlfa()+")");//$NON-NLS-4$ //$NON-NLS-5$//$NON-NLS-3$//$NON-NLS-2$//$NON-NLS-1$
				}
			}
		};
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), argbColor, lowExp, highExp });
		viewer.setColumnProperties(new String[] { "LABEL", "COLOR",  "LOW", "HIGH" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
	}

	/**
	 * When something in the table change, the list of the element inside the table is update as well
	 */
	@SuppressWarnings("unchecked")
	private void propertyChange() {
		intervalsList = (List<JRMeterInterval>)tableViewer.getInput();
	}
	
	/**
	 * Return the list of the intervals actually shown in the table
	 * 
	 * @return a list of intervals, can be null
	 */
	public List<JRMeterInterval> getIntervalsList(){
		return intervalsList;
	}

}
