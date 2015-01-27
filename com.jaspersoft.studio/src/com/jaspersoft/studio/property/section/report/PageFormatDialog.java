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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.OrientationEnum;

import org.eclipse.gef.commands.Command;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetWidgetFactory;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.property.section.report.util.PHolderUtil;
import com.jaspersoft.studio.property.section.report.util.PageSize;
import com.jaspersoft.studio.property.section.report.util.UnitsWidget;
import com.jaspersoft.studio.property.section.report.util.ValueUnitsWidget;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

public final class PageFormatDialog extends FormDialog {
	private JasperDesign jd;
	private JasperReportsConfiguration jConfig;
	private MReport jnode;

	public PageFormatDialog(Shell shell, ANode node) {
		super(shell);
		jConfig = node.getJasperConfiguration();
		jnode = (MReport) node.getRoot();
		this.jd = node.getJasperDesign();
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.PageFormatDialog_0);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(newShell, ContextHelpIDs.WIZARD_FORMAT_PAGE);
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		mform.getForm().setText(Messages.PageFormatDialog_1);

		toolkit = new TabbedPropertySheetWidgetFactory();

		Composite composite = mform.getForm().getBody();
		composite.setLayout(new GridLayout(2, false));
		composite.setBackgroundMode(SWT.INHERIT_FORCE);

		createPageSize(composite);

		createThumbnail(composite);

		createOrientation(composite);

		createMargins(composite);

		createColumns(composite);

		setJasperDesign(jd);

		setTBounds();
	}

	private void createColumns(Composite composite) {
		Group bright = toolkit.createGroup(composite, Messages.PageFormatDialog_2);
		bright.setLayoutData(new GridData(GridData.FILL_BOTH));
		bright.setBackgroundMode(SWT.INHERIT_FORCE);
		bright.setLayout(new GridLayout(3, false));

		new Label(bright, SWT.NONE).setText(Messages.PageFormatDialog_3);

		cols = new Spinner(bright, SWT.BORDER);
		cols.setValues(1, 1, Integer.MAX_VALUE, 0, 1, 10);
		cols.setToolTipText(Messages.PageFormatDialog_4);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		cols.setLayoutData(gd);

		cwidth = new ValueUnitsWidget(jConfig);
		cwidth.createComponent(bright, Messages.PageFormatDialog_5, Messages.PageFormatDialog_6);

		space = new ValueUnitsWidget(jConfig);
		space.createComponent(bright, Messages.PageFormatDialog_7, Messages.PageFormatDialog_8);

		uvWidgets.add(cwidth);
		uvWidgets.add(space);

		ModifyListener listener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				recalcColumns();
				setTBounds();
			}
		};
		cols.addModifyListener(listener);
		cwidth.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setTBounds();
			}
		});
		space.addModifyListener(listener);
	}

	private void recalcColumns() {
		int pagespace = pwidth.getValue() - lmargin.getValue() - rmargin.getValue();
		int nrcolspace = cols.getSelection() - 1;
		int colspace = nrcolspace * space.getValue();
		int mspace = Math.max(0, nrcolspace > 0 ? colspace / nrcolspace : pagespace);
		int maxspace = Math.max(0, nrcolspace > 0 ? pagespace / nrcolspace : pagespace);
		if (mspace > maxspace)
			mspace = maxspace;

		if (mspace < space.getValue() && !ignoreEvents)
			space.setValue(mspace);
		space.setMax(maxspace);

		int cw = (int) Math.floor((double) (pagespace - nrcolspace * space.getValue()) / (cols.getSelection()));
		if (!ignoreEvents)
			cwidth.setValue(cw);
		cwidth.setMax(cw);

		tmargin.setMax(pheigh.getValue() - bmargin.getValue());
		bmargin.setMax(pheigh.getValue() - tmargin.getValue());
		lmargin.setMax(pwidth.getValue() - rmargin.getValue());
		rmargin.setMax(pwidth.getValue() - lmargin.getValue());
	}

	private void createMargins(Composite composite) {
		Group bleft = toolkit.createGroup(composite, Messages.PageFormatDialog_9);
		bleft.setLayoutData(new GridData(GridData.FILL_BOTH));
		bleft.setLayout(new GridLayout(3, false));
		bleft.setBackgroundMode(SWT.INHERIT_FORCE);

		tmargin = new ValueUnitsWidget(jConfig);
		tmargin.createComponent(bleft, Messages.PageFormatDialog_10, Messages.PageFormatDialog_11);

		bmargin = new ValueUnitsWidget(jConfig);
		bmargin.createComponent(bleft, Messages.PageFormatDialog_12, Messages.PageFormatDialog_13);

		lmargin = new ValueUnitsWidget(jConfig);
		lmargin.createComponent(bleft, Messages.PageFormatDialog_14, Messages.PageFormatDialog_15);

		rmargin = new ValueUnitsWidget(jConfig);
		rmargin.createComponent(bleft, Messages.PageFormatDialog_16, Messages.PageFormatDialog_17);

		uvWidgets.add(tmargin);
		uvWidgets.add(bmargin);
		uvWidgets.add(lmargin);
		uvWidgets.add(rmargin);

		ModifyListener mlistner = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				recalcColumns();
				setTBounds();
			}
		};
		tmargin.addModifyListener(mlistner);
		bmargin.addModifyListener(mlistner);
		lmargin.addModifyListener(mlistner);
		rmargin.addModifyListener(mlistner);
	}

	private void createOrientation(Composite composite) {
		Group mleft = toolkit.createGroup(composite, Messages.PageFormatDialog_18);
		mleft.setLayoutData(new GridData(GridData.FILL_BOTH));
		mleft.setLayout(new GridLayout(2, false));
		portrait = toolkit.createButton(mleft, Messages.PageFormatDialog_19, SWT.RADIO);
		landscape = toolkit.createButton(mleft, Messages.PageFormatDialog_20, SWT.RADIO);

		portrait.setSelection(true);

		SelectionListener orientationlistner = new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (ignoreEvents)
					return;
				// change width with height
				int w = pwidth.getValue();
				int h = pheigh.getValue();
				if ((w > h && portrait.getSelection()) || (h > w && !portrait.getSelection())) {
					pwidth.setValue(h);
					pheigh.setValue(w);
					setTBounds();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		};
		portrait.addSelectionListener(orientationlistner);
	}

	private void createThumbnail(Composite composite) {
		pageFormatWidget = new PageFormatWidget(composite, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		pageFormatWidget.setLayoutData(gd);
	}

	private void setTBounds() {
		pageFormatWidget.setCols(cols.getSelection());

		pageFormatWidget.setTmargin(tmargin.getValue());
		pageFormatWidget.setBmargin(bmargin.getValue());
		pageFormatWidget.setLmargin(lmargin.getValue());
		pageFormatWidget.setRmargin(rmargin.getValue());

		pageFormatWidget.setPheight(pheigh.getValue());
		pageFormatWidget.setPwidth(pwidth.getValue());

		pageFormatWidget.setSpace(space.getValue());
		pageFormatWidget.setCwidth(cwidth.getValue());

		pageFormatWidget.setTBounds();
	}

	private void createPageSize(Composite composite) {
		Composite tleft = toolkit.createComposite(composite);
		tleft.setLayout(new GridLayout(3, false));
		tleft.setLayoutData(new GridData(GridData.FILL_BOTH));

		toolkit.createLabel(tleft, Messages.PageFormatDialog_21);
		pformat = toolkit.createCombo(tleft, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		pformat.setItems(PageSize.getFormats());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		pformat.setLayoutData(gd);

		pwidth = new ValueUnitsWidget(jConfig);
		pwidth.createComponent(tleft, Messages.PageFormatDialog_22, Messages.PageFormatDialog_23);

		pheigh = new ValueUnitsWidget(jConfig);
		pheigh.createComponent(tleft, Messages.PageFormatDialog_24, Messages.PageFormatDialog_25);

		uvWidgets.add(pwidth);
		uvWidgets.add(pheigh);

		uw = new UnitsWidget();
		uw.createComponent(tleft, Messages.PageFormatDialog_26, Messages.PageFormatDialog_27, 2);
		uw.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				recalcColumns();
				setAllUnits();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		ModifyListener psizeMListener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				String format = PageSize.deductPageFormat(pwidth.getValue(), pheigh.getValue());
				pformat.select(PageSize.getFormatIndx(format));
				recalcColumns();
				setTBounds();
			}

		};
		pwidth.addModifyListener(psizeMListener);
		pheigh.addModifyListener(psizeMListener);

		pformat.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				Point p = PageSize.getFormatSize(PageSize.getFormats()[pformat.getSelectionIndex()]);
				// Standard measures are for portrait: should switch if landascape
				if (portrait.getSelection()) {
					pwidth.setValue(p.x);
					pheigh.setValue(p.y);
				} else {
					pwidth.setValue(p.y);
					pheigh.setValue(p.x);
				}
				recalcColumns();
				setTBounds();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
	}

	private void setAllUnits() {
		String unit = uw.getUnit();
		for (ValueUnitsWidget vuw : uvWidgets)
			vuw.setUnit(unit);
	}

	private List<ValueUnitsWidget> uvWidgets = new ArrayList<ValueUnitsWidget>();
	private ValueUnitsWidget pheigh;
	private ValueUnitsWidget pwidth;
	private ValueUnitsWidget tmargin;
	private ValueUnitsWidget bmargin;
	private ValueUnitsWidget lmargin;
	private ValueUnitsWidget rmargin;
	private ValueUnitsWidget cwidth;
	private ValueUnitsWidget space;
	private Button portrait;
	private Button landscape;
	private Spinner cols;
	private Combo pformat;
	private JSSCompoundCommand command;
	private PageFormatWidget pageFormatWidget;
	private UnitsWidget uw;
	private TabbedPropertySheetWidgetFactory toolkit;
	private boolean ignoreEvents;

	private void setJasperDesign(JasperDesign jd) {
		ignoreEvents = true;
		pheigh.setValue(jd.getPageHeight());
		pwidth.setValue(jd.getPageWidth());
		String format = PageSize.deductPageFormat(jd.getPageWidth(), jd.getPageHeight());
		pformat.select(PageSize.getFormatIndx(format));

		tmargin.setValue(jd.getTopMargin());
		bmargin.setValue(jd.getBottomMargin());
		lmargin.setValue(jd.getLeftMargin());
		rmargin.setValue(jd.getRightMargin());

		cwidth.setValue(jd.getColumnWidth());
		space.setValue(jd.getColumnSpacing());
		cols.setSelection(jd.getColumnCount());

		landscape.setSelection(false);
		portrait.setSelection(false);
		if (jd.getOrientationValue().equals(OrientationEnum.LANDSCAPE))
			landscape.setSelection(true);
		else if (jd.getOrientationValue().equals(OrientationEnum.PORTRAIT))
			portrait.setSelection(true);
		ignoreEvents = false;

		String defunit = MReport.getMeasureUnit(jConfig, jd);
		uw.setUnit(defunit);

		pheigh.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_PAGE_HEIGHT, defunit));
		pwidth.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_PAGE_WIDTH, defunit));

		tmargin.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_TOP_MARGIN, defunit));
		bmargin.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_BOTTOM_MARGIN, defunit));
		lmargin.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_LEFT_MARGIN, defunit));
		rmargin.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_RIGHT_MARGIN, defunit));

		cwidth.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_COLUMN_WIDTH, defunit));
		space.setUnit(PHolderUtil.getUnit(jd, JasperDesign.PROPERTY_COLUMN_SPACING, defunit));
	}

	@Override
	public boolean close() {
		createCommand();
		return super.close();
	}

	public JSSCompoundCommand getCommand() {
		return command;
	}

	public void createCommand() {
		command = new JSSCompoundCommand(jnode);
		if (jd.getPageHeight() != pheigh.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_PAGE_HEIGHT, pheigh.getValue()));
		if (jd.getPageWidth() != pwidth.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_PAGE_WIDTH, pwidth.getValue()));

		if (jd.getTopMargin() != tmargin.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_TOP_MARGIN, tmargin.getValue()));
		if (jd.getBottomMargin() != bmargin.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_BOTTOM_MARGIN, bmargin.getValue()));
		if (jd.getLeftMargin() != lmargin.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_LEFT_MARGIN, lmargin.getValue()));
		if (jd.getRightMargin() != rmargin.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_RIGHT_MARGIN, rmargin.getValue()));

		if (jd.getColumnCount() != cols.getSelection())
			command.add(createCommand(JasperDesign.PROPERTY_COLUMN_COUNT, cols.getSelection()));
		if (jd.getColumnWidth() != cwidth.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_COLUMN_WIDTH, cwidth.getValue()));
		if (jd.getColumnSpacing() != space.getValue())
			command.add(createCommand(JasperDesign.PROPERTY_COLUMN_SPACING, space.getValue()));

		if (jd.getOrientationValue().equals(OrientationEnum.LANDSCAPE) && !landscape.getSelection())
			command.add(createCommand(JasperDesign.PROPERTY_ORIENTATION, OrientationEnum.PORTRAIT));
		else if (jd.getOrientationValue().equals(OrientationEnum.PORTRAIT) && !portrait.getSelection())
			command.add(createCommand(JasperDesign.PROPERTY_ORIENTATION, OrientationEnum.LANDSCAPE));

		boolean changes = false;
		JRPropertiesMap pmap = jd.getPropertiesMap().cloneProperties();
		String defunit = uw.getUnit();
		changes = PHolderUtil.setProperty(changes, pmap, "", defunit, null);

		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_PAGE_HEIGHT, pheigh.getUnit(), defunit);
		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_PAGE_WIDTH, pwidth.getUnit(), defunit);

		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_TOP_MARGIN, tmargin.getUnit(), defunit);
		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_BOTTOM_MARGIN, bmargin.getUnit(), defunit);
		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_LEFT_MARGIN, lmargin.getUnit(), defunit);
		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_RIGHT_MARGIN, rmargin.getUnit(), defunit);

		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_COLUMN_WIDTH, cwidth.getUnit(), defunit);
		changes = PHolderUtil.setProperty(changes, pmap, JasperDesign.PROPERTY_COLUMN_SPACING, space.getUnit(), defunit);
		if (changes)
			command.add(createCommand(MGraphicElement.PROPERTY_MAP, pmap));
	}

	private Command createCommand(String property, Object value) {
		SetValueCommand cmd = new SetValueCommand();
		cmd.setTarget(jnode);
		cmd.setPropertyId(property);
		cmd.setPropertyValue(value);
		return cmd;
	}
}
