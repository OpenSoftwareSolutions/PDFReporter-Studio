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
package com.jaspersoft.studio.components.chartspider.wizard.action;

import java.text.MessageFormat;
import java.util.List;

import net.sf.jasperreports.charts.JRCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.components.spiderchart.StandardSpiderDataset;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.wizard.HyperlinkPage;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.ADSComponent;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.dialog.SeriesDialog;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.series.CategorySerie;
import com.jaspersoft.studio.components.chart.wizard.fragments.data.widget.DatasetSeriesWidget;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.MHyperLink;
import com.jaspersoft.studio.property.dataset.ExpressionWidget;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class DSSpider extends ADSComponent {
	private StandardSpiderDataset dataset;
	private ExpressionWidget valueWidget;
	private ExpressionWidget categWidget;
	private ExpressionWidget labelWidget;
	private Combo seriesCombo;
	private Button hyperlinkBtn;

	public DSSpider(Composite composite, DatasetSeriesWidget dsWidget) {
		super(composite, dsWidget);
	}

	@Override
	public String getName() {
		return "Category Dataset";
	}

	@Override
	public void setData(JSSDrawVisitor drawVisitor, JRDesignElement jrChart,
			JRDesignElementDataset eDataset,
			JasperReportsConfiguration jrContext) {
		Assert.isTrue(eDataset instanceof StandardSpiderDataset);
		super.setData(drawVisitor, jrChart, eDataset, jrContext);
		this.dataset = (StandardSpiderDataset) eDataset;
		setSeries(0);		
	}

	private void setSeries(int selection) {
		List<JRCategorySeries> seriesList = dataset.getSeriesList();
		if (!seriesList.isEmpty()) {
			String[] srnames = new String[seriesList.size()];
			for (int i = 0; i < seriesList.size(); i++) {
				JRCategorySeries cs = seriesList.get(i);
				JRExpression se = cs.getSeriesExpression();
				srnames[i] = se != null && se.getText() != null ? se.getText()
						: "";
			}
			seriesCombo.setItems(srnames);
			seriesCombo.select(selection);
			hyperlinkBtn.setEnabled(true);
			handleSelectSeries(selection);
		} else {
			seriesCombo.setItems(new String[0]);
			hyperlinkBtn.setEnabled(false);
			hyperlinkBtn.setText(Messages.DSCategory_hyperlinkButtonDisabled);
			handleSelectSeries(-1);
		}
	}

	private void handleSelectSeries(int selection) {
		JRCategorySeries serie = null;
		if (selection >= 0 && selection < dataset.getSeriesList().size()){
			serie = dataset.getSeriesList().get(selection);
			hyperlinkBtn.setText(MessageFormat.format(Messages.DSCategory_defineHyperlinkButtton,seriesCombo.getText()));
		}

		valueWidget.bindObject(serie, "ValueExpression");
		categWidget.bindObject(serie, "CategoryExpression");
		labelWidget.bindObject(serie, "LabelExpression");
		hyperlinkBtn.getParent().layout();
	}

	protected Control createChartTop(Composite composite) {
		Composite yCompo = new Composite(composite, SWT.NONE);
		yCompo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		yCompo.setLayout(new GridLayout(10, false));

		Label lbl = new Label(yCompo, SWT.NONE);
		lbl.setText(Messages.DSCategory_seriesLabel);

		seriesCombo = new Combo(yCompo, SWT.READ_ONLY | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 300;
		seriesCombo.setLayoutData(gd);
		seriesCombo.setItems(new String[] { "series 1" });
		seriesCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleSelectSeries(seriesCombo.getSelectionIndex());
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		final Button btn = new Button(yCompo, SWT.PUSH | SWT.FLAT);
		btn.setText("...");
		btn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				CategorySerie serie = new CategorySerie();
				SeriesDialog dlg = new SeriesDialog(btn.getShell(), serie);
				dlg.setExpressionContext(expContext);
				List<JRCategorySeries> oldList = dataset.getSeriesList();
				int oldsel = seriesCombo.getSelectionIndex();
				JRCategorySeries selected = null;
				if (oldsel >= 0)
					selected = oldList.get(oldsel);
				serie.setList(oldList);
				if (dlg.open() == Window.OK) {
					List<JRCategorySeries> newlist = serie.getList();
					for (JRCategorySeries item : dataset.getSeries())
						dataset.removeCategorySeries(item);
					for (JRCategorySeries item : newlist)
						dataset.addCategorySeries(item);

					int sel = selected != null
							&& newlist.indexOf(selected) >= 0 ? newlist
							.indexOf(selected) : 0;
					setSeries(sel);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		
		hyperlinkBtn = new Button(yCompo, SWT.PUSH | SWT.FLAT);
		hyperlinkBtn.setSelection(false);
		hyperlinkBtn.setText(Messages.DSCategory_hyperlinkButtonDisabled);
		hyperlinkBtn.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int selection = seriesCombo.getSelectionIndex();
				JRDesignCategorySeries serie = null;
				if (selection >= 0 && selection < dataset.getSeriesList().size())
					serie = (JRDesignCategorySeries) dataset.getSeriesList().get(selection);
				if (serie != null){
					MHyperLink hyperLinkElement = null;
					JRHyperlink hyperlink = serie.getItemHyperlink();
					if (hyperlink != null){
						hyperLinkElement = new MHyperLink((JRHyperlink)hyperlink.clone());
					} else {
						hyperLinkElement = new MHyperLink(new JRDesignHyperlink());
					}
					String dialogTitle = MessageFormat.format(Messages.HyperlinkDialog_hyperlinkDialogTitle, seriesCombo.getText());
					HyperlinkPage dlg = new HyperlinkPage(hyperlinkBtn.getShell(), hyperLinkElement, dialogTitle);
					int operationResult = dlg.open();
					if (operationResult == Window.OK) {
						serie.setItemHyperlink((JRHyperlink)dlg.getElement().getValue());
					} else if (operationResult == IDialogConstants.ABORT_ID){
						serie.setItemHyperlink(null);
					}
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		
		return yCompo;
	}

	@Override
	protected Control createChartRight(Composite parent) {
		Composite yCompo = new Composite(parent, SWT.NONE);
		yCompo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		yCompo.setLayout(new GridLayout(3, false));

		labelWidget = new ExpressionWidget(yCompo, Messages.DSCategory_labelLabel);
		return yCompo;
	}

	@Override
	protected Control createChartLeft(Composite parent) {
		Composite yCompo = new Composite(parent, SWT.NONE);
		yCompo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		yCompo.setLayout(new GridLayout(3, false));

		valueWidget = new ExpressionWidget(yCompo, Messages.DSCategory_valueLabel);
		return yCompo;
	}

	@Override
	protected Control createChartBottom(Composite parent) {
		Composite xCompo = new Composite(parent, SWT.NONE);
		xCompo.setLayout(new GridLayout(3, false));
		xCompo.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));

		categWidget = new ExpressionWidget(xCompo, Messages.DSCategory_categoryLabel);
		return xCompo;
	}

	@Override
	public void setExpressionContext(ExpressionContext expContext) {
		super.setExpressionContext(expContext);
		valueWidget.setExpressionContext(expContext);
		categWidget.setExpressionContext(expContext);
		labelWidget.setExpressionContext(expContext);
	}

}
