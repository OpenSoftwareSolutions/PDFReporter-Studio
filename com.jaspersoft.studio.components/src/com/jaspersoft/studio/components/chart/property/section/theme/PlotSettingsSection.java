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
package com.jaspersoft.studio.components.chart.property.section.theme;

import net.sf.jasperreports.chartthemes.simple.PlotSettings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class PlotSettingsSection extends AbstractSection {
	
	private ExpandableComposite sectionBackground;
	
	private ExpandableComposite sectionPadding;
	
	private ExpandableComposite sectionDomainGridLine;
	
	private ExpandableComposite sectionRangeGridLine;
	
	private ExpandableComposite sectionOutline;
	
	private ExpandableComposite sectionSeries;
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		Composite group = getWidgetFactory().createComposite(parent);
		group.setLayout(new GridLayout(2, false));
		createWidget4Property(group, PlotSettings.PROPERTY_labelRotation);
		createWidget4Property(group, PlotSettings.PROPERTY_foregroundAlpha);
		createWidget4Property(group, PlotSettings.PROPERTY_orientation);

		group = getWidgetFactory().createSection(parent, Messages.common_background, true, 3);
		sectionBackground = (ExpandableComposite)group.getParent();
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, PlotSettings.PROPERTY_backgroundPaint).getControl().setLayoutData(gd);
		createWidget4Property(group, PlotSettings.PROPERTY_backgroundImage);
		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, PlotSettings.PROPERTY_backgroundImageAlignment).getControl().setLayoutData(gd);
		createWidget4Property(group, PlotSettings.PROPERTY_backgroundImageAlpha);

		Composite paddingSection = PadUtil.createWidgets4Property(parent, "", com.jaspersoft.studio.messages.Messages.common_padding, this);
		sectionPadding = (ExpandableComposite)paddingSection.getParent();

		group = getWidgetFactory().createSection(parent, "Domain Grid Line", true, 2);
		sectionDomainGridLine = (ExpandableComposite)group.getParent();
		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, PlotSettings.PROPERTY_domainGridlineVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, PlotSettings.PROPERTY_domainGridlinePaint);
		createWidget4Property(group, PlotSettings.PROPERTY_domainGridlineStroke);

		group = getWidgetFactory().createSection(parent, "Range Grid Line", true, 2);
		sectionRangeGridLine = (ExpandableComposite)group.getParent();
		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, PlotSettings.PROPERTY_rangeGridlineVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, PlotSettings.PROPERTY_rangeGridlinePaint);
		createWidget4Property(group, PlotSettings.PROPERTY_rangeGridlineStroke);

		group = getWidgetFactory().createSection(parent, "Outline", true, 2);
		sectionOutline = (ExpandableComposite)group.getParent();
		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, PlotSettings.PROPERTY_outlineVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, PlotSettings.PROPERTY_outlinePaint);
		createWidget4Property(group, PlotSettings.PROPERTY_outlineStroke);

		group = getWidgetFactory().createSection(parent, "Series", true, 2);
		sectionSeries = (ExpandableComposite)group.getParent();
		createWidget4Property(group, PlotSettings.PROPERTY_seriesStrokeSequence);
		createWidget4Property(group, PlotSettings.PROPERTY_seriesColorSequence);
		createWidget4Property(group, PlotSettings.PROPERTY_seriesGradientPaintSequence);
		createWidget4Property(group, PlotSettings.PROPERTY_seriesOutlinePaintSequence);
		createWidget4Property(group, PlotSettings.PROPERTY_seriesOutlineStrokeSequence);
	}
	
	private boolean checkExpandBackround(Object propertyId){
		if (propertyId.equals(PlotSettings.PROPERTY_backgroundPaint) 
					|| propertyId.equals(PlotSettings.PROPERTY_backgroundImage)
						|| propertyId.equals(PlotSettings.PROPERTY_backgroundImageAlignment) 
							|| propertyId.equals(PlotSettings.PROPERTY_backgroundImageAlpha)) {
			expandSection(sectionBackground);
			return true;
		}
		return false;
	}
	
	private boolean checkExpandPadding(Object propertyId){
		if (propertyId.equals(PadUtil.PADDING_TOP) 
					|| propertyId.equals(PadUtil.PADDING_BOTTOM)
						|| propertyId.equals(PadUtil.PADDING_LEFT) 
							|| propertyId.equals(PadUtil.PADDING_RIGHT)) {
			expandSection(sectionPadding);
			return true;
		}
		return false;
	}
	
	private boolean checkExpandDomainGridLine(Object propertyId){
		if (propertyId.equals(PlotSettings.PROPERTY_domainGridlineVisible) 
					|| propertyId.equals(PlotSettings.PROPERTY_domainGridlinePaint)
						|| propertyId.equals(PlotSettings.PROPERTY_domainGridlineStroke)) {
			expandSection(sectionDomainGridLine);
			return true;
		}
		return false;
	}
	
	private boolean checkExpandRangeGridLine(Object propertyId){
		if (propertyId.equals(PlotSettings.PROPERTY_rangeGridlineVisible) 
					|| propertyId.equals(PlotSettings.PROPERTY_rangeGridlinePaint)
						|| propertyId.equals(PlotSettings.PROPERTY_rangeGridlineStroke)) {
			expandSection(sectionRangeGridLine);
			return true;
		}
		return false;
	}
	
	private boolean checkExpandOutline(Object propertyId){
		if (propertyId.equals(PlotSettings.PROPERTY_outlineVisible) 
					|| propertyId.equals(PlotSettings.PROPERTY_outlinePaint)
						|| propertyId.equals(PlotSettings.PROPERTY_outlineStroke)) {
			expandSection(sectionOutline);
			return true;
		}
		return false;
	}
	
	private boolean checkExpandSeries(Object propertyId){
		if (propertyId.equals(PlotSettings.PROPERTY_seriesStrokeSequence) 
					|| propertyId.equals(PlotSettings.PROPERTY_seriesColorSequence)
						|| propertyId.equals(PlotSettings.PROPERTY_seriesGradientPaintSequence) 
							|| propertyId.equals(PlotSettings.PROPERTY_seriesOutlinePaintSequence)
								|| propertyId.equals(PlotSettings.PROPERTY_seriesOutlineStrokeSequence)) {
			expandSection(sectionSeries);
			return true;
		}
		return false;
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (checkExpandBackround(propertyId)) return;
		if (checkExpandPadding(propertyId)) return;
		if (checkExpandDomainGridLine(propertyId)) return;
		if (checkExpandRangeGridLine(propertyId)) return;
		if (checkExpandOutline(propertyId)) return;
		if (checkExpandSeries(propertyId)) return;
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(PlotSettings.PROPERTY_labelRotation,  Messages.MPlotSettings_labelRotationTitle);
		addProvidedProperties(PlotSettings.PROPERTY_foregroundAlpha, Messages.MPlotSettings_foregroundAlphaTitle);
		addProvidedProperties(PlotSettings.PROPERTY_orientation, Messages.MPlotSettings_orientationTitle);
		
		addProvidedProperties(PlotSettings.PROPERTY_backgroundPaint, Messages.MPlotSettings_backgroundColorTitle);
		addProvidedProperties(PlotSettings.PROPERTY_backgroundImage, Messages.MPlotSettings_backGroundImageTitle);
		addProvidedProperties(PlotSettings.PROPERTY_backgroundImageAlignment, Messages.MPlotSettings_backgroundImgAlignTitle);
		addProvidedProperties(PlotSettings.PROPERTY_backgroundImageAlpha, Messages.MPlotSettings_backgroundAlphaTitle);
		
		addProvidedProperties(PadUtil.PADDING_TOP, com.jaspersoft.studio.messages.Messages.common_top);
		addProvidedProperties(PadUtil.PADDING_BOTTOM, com.jaspersoft.studio.messages.Messages.common_bottom);
		addProvidedProperties(PadUtil.PADDING_LEFT, com.jaspersoft.studio.messages.Messages.common_left);
		addProvidedProperties(PadUtil.PADDING_RIGHT, com.jaspersoft.studio.messages.Messages.common_right);
		
		addProvidedProperties(PlotSettings.PROPERTY_domainGridlineVisible,  Messages.MPlotSettings_domainGridLineVisibleTitle);
		addProvidedProperties(PlotSettings.PROPERTY_domainGridlinePaint, Messages.MPlotSettings_domainGridLineColorTitle);
		addProvidedProperties(PlotSettings.PROPERTY_domainGridlineStroke, Messages.MPlotSettings_domainGridLineStrokeTitle);
		
		addProvidedProperties(PlotSettings.PROPERTY_rangeGridlineVisible,  Messages.MPlotSettings_rangeGridLineVisibleTitle);
		addProvidedProperties(PlotSettings.PROPERTY_rangeGridlinePaint, Messages.MPlotSettings_rangeGridLineColorTitle);
		addProvidedProperties(PlotSettings.PROPERTY_rangeGridlineStroke, Messages.MPlotSettings_rangeGridLineStrokeTitle);
		
		addProvidedProperties(PlotSettings.PROPERTY_outlineVisible,  Messages.MPlotSettings_outlineVisibleTitle);
		addProvidedProperties(PlotSettings.PROPERTY_outlinePaint, Messages.MPlotSettings_outlineColorTitle);
		addProvidedProperties(PlotSettings.PROPERTY_outlineStroke, Messages.MPlotSettings_outlineStrokeTitle);
		
		addProvidedProperties(PlotSettings.PROPERTY_seriesStrokeSequence,  Messages.MPlotSettings_strokeSequenceTitle);
		addProvidedProperties(PlotSettings.PROPERTY_seriesColorSequence, Messages.MPlotSettings_colorSequenceTitle);
		addProvidedProperties(PlotSettings.PROPERTY_seriesGradientPaintSequence, Messages.MPlotSettings_gradientPaintSequenceTitle);
		addProvidedProperties(PlotSettings.PROPERTY_seriesOutlinePaintSequence,  Messages.MPlotSettings_outlinePaintSequenceTitle);
		addProvidedProperties(PlotSettings.PROPERTY_seriesOutlineStrokeSequence, Messages.MPlotSettings_outlineStrokeSequenceTitle);
	}
	
	public void expandSection(ExpandableComposite section) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
}
