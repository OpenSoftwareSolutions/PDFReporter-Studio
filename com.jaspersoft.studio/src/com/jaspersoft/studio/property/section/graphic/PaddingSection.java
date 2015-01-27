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
package com.jaspersoft.studio.property.section.graphic;

import net.sf.jasperreports.engine.base.JRBaseLineBox;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElementLineBox;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.properties.internal.IHighlightPropertyWidget;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.BackgroundHighlight;
import com.jaspersoft.studio.utils.UIUtil;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class PaddingSection extends AbstractSection {
	private Spinner bottomPadding;
	private Spinner topPadding;
	private Spinner leftPadding;
	private Spinner rightPadding;
	private Spinner allPadding;

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode model = super.getModelFromEditPart(item);
		if (model != null && model instanceof MGraphicElementLineBox || model instanceof MStyle)
			model = (APropertyNode) model.getPropertyValue(MGraphicElementLineBox.LINE_BOX);
		return model;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		GridLayout layout = new GridLayout(14, false);
		composite.setLayout(layout);

		CLabel label = getWidgetFactory().createCLabel(composite, Messages.common_padding + ":", SWT.RIGHT); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.widthHint = 100;
		label.setLayoutData(gd);

		CLabel l = new CLabel(composite, SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_frame.gif")); //$NON-NLS-1$
		l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		allPadding = new Spinner(composite, SWT.BORDER);
		allPadding.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		allPadding.setToolTipText(Messages.PaddingSection_all_padding_tool_tip);
		allPadding.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeProperty(JRBaseLineBox.PROPERTY_PADDING, new Integer(allPadding.getSelection()));
			}
		});

		l = new CLabel(composite, SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_top.gif")); //$NON-NLS-1$
		l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		topPadding = new Spinner(composite, SWT.BORDER);
		topPadding.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		topPadding.setToolTipText(Messages.PaddingSection_top_padding_tool_tip);
		topPadding.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeProperty(JRBaseLineBox.PROPERTY_TOP_PADDING, new Integer(topPadding.getSelection()));
			}
		});

		l = new CLabel(composite, SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_bottom.png")); //$NON-NLS-1$
		l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		bottomPadding = new Spinner(composite, SWT.BORDER);
		bottomPadding.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		bottomPadding.setToolTipText(Messages.PaddingSection_bottom_padding_tool_tip);
		bottomPadding.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeProperty(JRBaseLineBox.PROPERTY_BOTTOM_PADDING, new Integer(bottomPadding.getSelection()));
			}
		});

		l = new CLabel(composite, SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_left.gif")); //$NON-NLS-1$
		l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		leftPadding = new Spinner(composite, SWT.BORDER);
		leftPadding.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		leftPadding.setToolTipText(Messages.PaddingSection_left_padding_tool_tip);
		leftPadding.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeProperty(JRBaseLineBox.PROPERTY_LEFT_PADDING, new Integer(leftPadding.getSelection()));
			}
		});

		l = new CLabel(composite, SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_right.gif")); //$NON-NLS-1$
		l.setBackground(composite.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		rightPadding = new Spinner(composite, SWT.BORDER);
		rightPadding.setValues(0, 0, Integer.MAX_VALUE, 0, 1, 10);
		rightPadding.setToolTipText(Messages.PaddingSection_right_padding_tool_tip);
		rightPadding.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				changeProperty(JRBaseLineBox.PROPERTY_RIGHT_PADDING, new Integer(rightPadding.getSelection()));
			}
		});

	}
	
	@Override
	public IHighlightPropertyWidget getWidgetForProperty(Object propertyId) {
		if (propertyId.equals(JRBaseLineBox.PROPERTY_PADDING)) return new ASHighlightControl(allPadding, new BackgroundHighlight(allPadding));
		else if (propertyId.equals(JRBaseLineBox.PROPERTY_LEFT_PADDING)) return new ASHighlightControl(leftPadding, new BackgroundHighlight(leftPadding));
		else if (propertyId.equals(JRBaseLineBox.PROPERTY_RIGHT_PADDING)) return new ASHighlightControl(rightPadding, new BackgroundHighlight(rightPadding));
		else if (propertyId.equals(JRBaseLineBox.PROPERTY_TOP_PADDING)) return new ASHighlightControl(topPadding, new BackgroundHighlight(topPadding));
		else return new ASHighlightControl(bottomPadding, new BackgroundHighlight(bottomPadding));
	}
		
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseLineBox.PROPERTY_PADDING,  Messages.BordersSection_Padding_Box_Title);
		addProvidedProperties(JRBaseLineBox.PROPERTY_BOTTOM_PADDING, Messages.common_bottom);
		addProvidedProperties(JRBaseLineBox.PROPERTY_TOP_PADDING, Messages.BordersSection_Top_Label);
		addProvidedProperties(JRBaseLineBox.PROPERTY_LEFT_PADDING, Messages.BordersSection_Left_Label);
		addProvidedProperties(JRBaseLineBox.PROPERTY_RIGHT_PADDING, Messages.common_right);
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.view.ITabbedPropertySection#refresh()
	 */
	public void refresh() {
		setRefreshing(true);
		APropertyNode element = getElement();
		if (element != null) {
			UIUtil.setSpinnerSelection(allPadding, element.getPropertyValue(JRBaseLineBox.PROPERTY_PADDING), 0);
			UIUtil.setSpinnerSelection(bottomPadding, element.getPropertyValue(JRBaseLineBox.PROPERTY_BOTTOM_PADDING), 0);
			UIUtil.setSpinnerSelection(topPadding, element.getPropertyValue(JRBaseLineBox.PROPERTY_TOP_PADDING), 0);
			UIUtil.setSpinnerSelection(leftPadding, element.getPropertyValue(JRBaseLineBox.PROPERTY_LEFT_PADDING), 0);
			UIUtil.setSpinnerSelection(rightPadding, element.getPropertyValue(JRBaseLineBox.PROPERTY_RIGHT_PADDING), 0);
		}
		setRefreshing(false);
	}

	@Override
	public boolean isDisposed() {
		return allPadding.isDisposed();
	}
}
