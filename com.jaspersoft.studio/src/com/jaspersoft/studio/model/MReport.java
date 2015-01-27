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
package com.jaspersoft.studio.model;

import java.beans.IndexedPropertyChangeEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.StringTokenizer;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JROrigin;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JRDesignSection;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;
import net.sf.jasperreports.engine.type.BandTypeEnum;
import net.sf.jasperreports.engine.type.OrientationEnum;
import net.sf.jasperreports.engine.type.PrintOrderEnum;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.classname.ImportDeclarationPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.properties.JPropertiesPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.report.PageFormatUtils;
import com.jaspersoft.studio.property.section.report.util.PHolderUtil;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPToolBarEnum;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class MReport.
 * 
 * @author Chicu Veaceslav
 */
public class MReport extends MLockableRefresh implements IGraphicElement, IContainerEditPart, IContainerLayout,
		IPastable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private Map<Object, ANode> obj2Node = new HashMap<Object, ANode>();

	/**
	 * used when we need to change the position of a band. The differences between this and 
	 * for example JRDesignSection.PROPERTY_BANDS is that this key uses a more light method to
	 * do the changes and since the hierarchy remains the same it will keep also all the listeners
	 */
	public static final String CHANGE_BAND_POSITION = "changeBandPosition";
	
	
	@Override
	public INode getRoot() {
		return this;
	}

	@Override
	public void register(ANode n) {
		if (n.getValue() != null)
			obj2Node.put(n.getValue(), n);
	}

	@Override
	public void unregister(ANode n) {
		if (n.getValue() != null)
			obj2Node.remove(n.getValue());
	}

	public ANode getNode(Object obj) {
		return obj2Node.get(obj);
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("report"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m report.
	 * 
	 * @param parent
	 *          the parent
	 * @param jd
	 *          the jd
	 */
	public MReport(ANode parent, JasperReportsConfiguration jConfig) {
		super(parent, -1);
		setJasperConfiguration(jConfig);
		setValue(jConfig.getJasperDesign());
	}

	@Override
	public JasperDesign getValue() {
		return (JasperDesign) super.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		return this;
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	public MBand getBand(BandTypeEnum type) {
		List<INode> children = this.getChildren();
		for (INode node : children) {
			if (node instanceof MBand && ((MBand) node).getBandType().equals(type))
				return (MBand) node;
		}
		return null;
	}

	private MDataset mDataset;

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {

		ImportDeclarationPropertyDescriptor importsD = new ImportDeclarationPropertyDescriptor(
				JasperDesign.PROPERTY_IMPORTS, Messages.MReport_imports);
		importsD.setDescription(Messages.MReport_imports_description);
		desc.add(importsD);
		importsD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#import"));

		JSSTextPropertyDescriptor nameD = new JSSTextPropertyDescriptor(JasperDesign.PROPERTY_NAME,
				Messages.MReport_report_name);
		nameD.setDescription(Messages.MReport_report_name_description);
		nameD.setCategory(Messages.common_report);
		desc.add(nameD);

		NClassTypePropertyDescriptor formatFactoryClassD = new NClassTypePropertyDescriptor(
				JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS, Messages.MReport_format_factory_class);
		formatFactoryClassD.setDescription(Messages.MReport_format_factory_class_description);
		desc.add(formatFactoryClassD);

		// main dataset
		PropertyDescriptor datasetD = new PropertyDescriptor(JasperDesign.PROPERTY_MAIN_DATASET,
				Messages.MReport_main_dataset);
		datasetD.setDescription(Messages.MReport_main_dataset_description);
		desc.add(datasetD);

		// -------------------
		PixelPropertyDescriptor heightD = new PixelPropertyDescriptor(JasperDesign.PROPERTY_PAGE_HEIGHT,
				Messages.MReport_page_height);
		heightD.setDescription(Messages.MReport_page_height_description);
		heightD.setCategory(Messages.MReport_report_page_category);
		desc.add(heightD);

		IntegerPropertyDescriptor widthD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_PAGE_WIDTH,
				Messages.MReport_page_width);
		widthD.setDescription(Messages.MReport_page_width_description);
		widthD.setCategory(Messages.MReport_report_page_category);
		desc.add(widthD);

		IntegerPropertyDescriptor rightMarginD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_RIGHT_MARGIN,
				Messages.MReport_right_margin);
		rightMarginD.setDescription(Messages.MReport_right_margin_description);
		rightMarginD.setCategory(Messages.MReport_report_page_category);
		desc.add(rightMarginD);

		IntegerPropertyDescriptor leftMarginD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_LEFT_MARGIN,
				Messages.MReport_left_margin);
		leftMarginD.setDescription(Messages.MReport_left_margin_description);
		leftMarginD.setCategory(Messages.MReport_report_page_category);
		desc.add(leftMarginD);

		IntegerPropertyDescriptor topMarginD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_TOP_MARGIN,
				Messages.MReport_top_margin);
		topMarginD.setDescription(Messages.MReport_top_margin_description);
		topMarginD.setCategory(Messages.MReport_report_page_category);
		desc.add(topMarginD);

		IntegerPropertyDescriptor bottomMarginD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_BOTTOM_MARGIN,
				Messages.MReport_bottom_margin);
		bottomMarginD.setDescription(Messages.MReport_bottom_margin_description);
		bottomMarginD.setCategory(Messages.MReport_report_page_category);
		desc.add(bottomMarginD);

		IntegerPropertyDescriptor columnCountD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_COLUMN_COUNT,
				Messages.MReport_column_count);
		columnCountD.setDescription(Messages.MReport_column_count_description);
		columnCountD.setCategory(Messages.MReport_columns_category);
		desc.add(columnCountD);

		IntegerPropertyDescriptor columnWidthD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_COLUMN_WIDTH,
				Messages.MReport_column_width);
		columnWidthD.setDescription(Messages.MReport_column_width_description);
		columnWidthD.setCategory(Messages.MReport_columns_category);
		desc.add(columnWidthD);

		IntegerPropertyDescriptor columnSpaceD = new IntegerPropertyDescriptor(JasperDesign.PROPERTY_COLUMN_SPACING,
				Messages.MReport_column_space);
		columnSpaceD.setDescription(Messages.MReport_column_space_description);
		columnSpaceD.setCategory(Messages.MReport_columns_category);
		desc.add(columnSpaceD);

		RWComboBoxPropertyDescriptor languageD = new RWComboBoxPropertyDescriptor(JasperDesign.PROPERTY_LANGUAGE,
				Messages.common_language, ModelUtils.getDefaultReportLanguages(), NullEnum.NOTNULL, false);
		languageD.setDescription(Messages.MReport_language_description);
		languageD.setCategory(Messages.common_report);
		desc.add(languageD);

		orientationD = new JSSEnumPropertyDescriptor(JasperDesign.PROPERTY_ORIENTATION, Messages.MReport_page_orientation,
				OrientationEnum.class, NullEnum.NOTNULL) {
			@Override
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				Image[] images = new Image[] { JaspersoftStudioPlugin.getInstance().getImage("icons/resources/portrait16.png"),
						JaspersoftStudioPlugin.getInstance().getImage("icons/resources/landscape16.png") };
				return new SPToolBarEnum(parent, section, this, images);
			}
		};
		orientationD.setDescription(Messages.MReport_page_orientation_description);
		orientationD.setCategory(Messages.MReport_report_page_category);
		desc.add(orientationD);

		printOrderD = new JSSEnumPropertyDescriptor(JasperDesign.PROPERTY_PRINT_ORDER, Messages.MReport_print_order,
				PrintOrderEnum.class, NullEnum.NULL);
		printOrderD.setDescription(Messages.MReport_print_order_description);
		printOrderD.setCategory(Messages.MReport_columns_category);
		desc.add(printOrderD);

		whenNoDataD = new JSSEnumPropertyDescriptor(JasperDesign.PROPERTY_WHEN_NO_DATA_TYPE,
				Messages.MReport_when_no_data_type, WhenNoDataTypeEnum.class, NullEnum.NULL);
		whenNoDataD.setDescription(Messages.MReport_when_no_data_type_description);
		whenNoDataD.setCategory(Messages.common_report);
		desc.add(whenNoDataD);

		// checkboxes
		CheckBoxPropertyDescriptor titleNewPageD = new CheckBoxPropertyDescriptor(JasperDesign.PROPERTY_TITLE_NEW_PAGE,
				Messages.MReport_title_on_a_new_page);
		titleNewPageD.setDescription(Messages.MReport_title_on_a_new_page_description);
		desc.add(titleNewPageD);

		CheckBoxPropertyDescriptor summaryNewPageD = new CheckBoxPropertyDescriptor(JasperDesign.PROPERTY_SUMMARY_NEW_PAGE,
				Messages.MReport_summary_on_a_new_page);
		summaryNewPageD.setDescription(Messages.MReport_summary_on_a_new_page_description);
		desc.add(summaryNewPageD);

		CheckBoxPropertyDescriptor summaryWHFD = new CheckBoxPropertyDescriptor(
				JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER, Messages.MReport_summary_with_page_header_and_footer);
		summaryWHFD.setDescription(Messages.MReport_summary_with_page_header_and_footer_description);
		desc.add(summaryWHFD);

		CheckBoxPropertyDescriptor floatColumnFooterD = new CheckBoxPropertyDescriptor(
				JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER, Messages.MReport_float_column_footer);
		floatColumnFooterD.setDescription(Messages.MReport_float_column_footer_description);
		desc.add(floatColumnFooterD);

		CheckBoxPropertyDescriptor ignorePaginationD = new CheckBoxPropertyDescriptor(
				JasperDesign.PROPERTY_IGNORE_PAGINATION, Messages.MReport_ignore_pagination);
		ignorePaginationD.setDescription(Messages.MReport_ignore_pagination_description);
		desc.add(ignorePaginationD);

		JPropertiesPropertyDescriptor propertiesMapD = new JPropertiesPropertyDescriptor(MGraphicElement.PROPERTY_MAP,
				Messages.common_properties);
		propertiesMapD.setDescription(Messages.common_properties);
		desc.add(propertiesMapD);

		titleNewPageD.setCategory(Messages.MReport_pagination);
		ignorePaginationD.setCategory(Messages.MReport_pagination);
		summaryNewPageD.setCategory(Messages.MReport_pagination);
		floatColumnFooterD.setCategory(Messages.MReport_pagination);
		summaryWHFD.setCategory(Messages.MReport_pagination);

		defaultsMap.put(JasperDesign.PROPERTY_PAGE_WIDTH, new Integer(595));
		defaultsMap.put(JasperDesign.PROPERTY_PAGE_HEIGHT, new Integer(842));
		defaultsMap.put(JasperDesign.PROPERTY_TOP_MARGIN, new Integer(30));
		defaultsMap.put(JasperDesign.PROPERTY_BOTTOM_MARGIN, new Integer(30));
		defaultsMap.put(JasperDesign.PROPERTY_LEFT_MARGIN, new Integer(20));
		defaultsMap.put(JasperDesign.PROPERTY_RIGHT_MARGIN, new Integer(20));

		defaultsMap.put(JasperDesign.PROPERTY_LANGUAGE, "Java"); //$NON-NLS-1$

		defaultsMap.put(JasperDesign.PROPERTY_COLUMN_COUNT, new Integer(1));
		defaultsMap.put(JasperDesign.PROPERTY_COLUMN_WIDTH, new Integer(555));
		defaultsMap.put(JasperDesign.PROPERTY_COLUMN_SPACING, new Integer(0));
		defaultsMap.put(JasperDesign.PROPERTY_ORIENTATION, orientationD.getEnumValue(OrientationEnum.PORTRAIT));
		defaultsMap.put(JasperDesign.PROPERTY_PRINT_ORDER, printOrderD.getEnumValue(PrintOrderEnum.VERTICAL));
		defaultsMap.put(JasperDesign.PROPERTY_WHEN_NO_DATA_TYPE, whenNoDataD.getEnumValue(WhenNoDataTypeEnum.NO_PAGES));
		defaultsMap.put(JasperDesign.PROPERTY_TITLE_NEW_PAGE, Boolean.FALSE);
		defaultsMap.put(JasperDesign.PROPERTY_SUMMARY_NEW_PAGE, Boolean.FALSE);
		defaultsMap.put(JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER, Boolean.FALSE);
		defaultsMap.put(JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER, Boolean.FALSE);
		defaultsMap.put(JasperDesign.PROPERTY_IGNORE_PAGINATION, Boolean.FALSE);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#jasperReport");
	}

	private void createDataset(JasperDesign jrDesign) {
		mDataset = new MDataset(this, (JRDesignDataset) jrDesign.getMainDataset());
		mDataset.setJasperConfiguration(getJasperConfiguration());
		setChildListener(mDataset);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java .lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JasperDesign jrDesign = (JasperDesign) getValue();
		if (id.equals(JasperDesign.PROPERTY_NAME))
			return jrDesign.getName();
		if (id.equals(JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS))
			return jrDesign.getFormatFactoryClass();
		if (id.equals(JasperDesign.PROPERTY_IMPORTS)) {
			String res = ""; //$NON-NLS-1$
			String[] imports = jrDesign.getImports();
			if (imports != null) {
				int lenght = imports.length;
				for (int i = 0; i < lenght; i++) {
					res += imports[i] + ";"; //$NON-NLS-1$
				}
			}
			return res;
		}

		if (id.equals(JasperDesign.PROPERTY_MAIN_DATASET)) {
			if (mDataset == null) {
				createDataset(jrDesign);
			}
			return mDataset;
		}

		if (id.equals(JasperDesign.PROPERTY_PAGE_HEIGHT))
			return new Integer(jrDesign.getPageHeight());
		if (id.equals(JasperDesign.PROPERTY_PAGE_WIDTH))
			return new Integer(jrDesign.getPageWidth());
		if (id.equals(JasperDesign.PROPERTY_RIGHT_MARGIN))
			return new Integer(jrDesign.getRightMargin());
		if (id.equals(JasperDesign.PROPERTY_LEFT_MARGIN))
			return new Integer(jrDesign.getLeftMargin());
		if (id.equals(JasperDesign.PROPERTY_TOP_MARGIN))
			return new Integer(jrDesign.getTopMargin());
		if (id.equals(JasperDesign.PROPERTY_BOTTOM_MARGIN))
			return new Integer(jrDesign.getBottomMargin());
		if (id.equals(JasperDesign.PROPERTY_COLUMN_COUNT))
			return new Integer(jrDesign.getColumnCount());
		if (id.equals(JasperDesign.PROPERTY_COLUMN_SPACING))
			return new Integer(jrDesign.getColumnSpacing());
		if (id.equals(JasperDesign.PROPERTY_COLUMN_WIDTH))
			return new Integer(jrDesign.getColumnWidth());

		if (id.equals(JasperDesign.PROPERTY_LANGUAGE))
			return jrDesign.getLanguage();

		if (id.equals(JasperDesign.PROPERTY_ORIENTATION))
			return orientationD.getEnumValue(jrDesign.getOrientationValue());
		if (id.equals(JasperDesign.PROPERTY_PRINT_ORDER))
			return printOrderD.getEnumValue(jrDesign.getPrintOrderValue());
		if (id.equals(JasperDesign.PROPERTY_WHEN_NO_DATA_TYPE))
			return whenNoDataD.getEnumValue(jrDesign.getWhenNoDataTypeValue());

		if (id.equals(JasperDesign.PROPERTY_TITLE_NEW_PAGE))
			return new Boolean(jrDesign.isTitleNewPage());
		if (id.equals(JasperDesign.PROPERTY_SUMMARY_NEW_PAGE))
			return new Boolean(jrDesign.isSummaryNewPage());
		if (id.equals(JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER))
			return new Boolean(jrDesign.isSummaryWithPageHeaderAndFooter());
		if (id.equals(JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER))
			return new Boolean(jrDesign.isFloatColumnFooter());
		if (id.equals(JasperDesign.PROPERTY_IGNORE_PAGINATION))
			return new Boolean(jrDesign.isIgnorePagination());
		if (id.equals(MGraphicElement.PROPERTY_MAP)) {
			// to avoid duplication I remove it first
			return (JRPropertiesMap) jrDesign.getPropertiesMap().cloneProperties();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JasperDesign jrDesign = (JasperDesign) getValue();
		if (id.equals(JasperDesign.PROPERTY_NAME))
			jrDesign.setName((String) value);
		else if (id.equals(JasperDesign.PROPERTY_FORMAT_FACTORY_CLASS)) {
			if (value instanceof String && ((String) value).trim().isEmpty())
				value = null;
			jrDesign.setFormatFactoryClass((String) value);
		} else if (id.equals(JasperDesign.PROPERTY_IMPORTS)) {
			String[] imports = jrDesign.getImports();
			if (imports != null) {
				int lenght = imports.length;
				for (int i = 0; i < lenght; i++) {
					jrDesign.removeImport(imports[i]);
				}
			}
			if (value != null && value instanceof String) {
				StringTokenizer st = new StringTokenizer((String) value, ";"); //$NON-NLS-1$
				while (st.hasMoreTokens()) {
					String imp = st.nextToken();
					jrDesign.addImport(imp);
				}
			}
		}

		else if (id.equals(JasperDesign.PROPERTY_LANGUAGE)) {
			String str = (String) value;
			if (str != null && str.isEmpty())
				str = null;
			if (str != null)
				str = str.toLowerCase();
			jrDesign.setLanguage(str);
		} else if (id.equals(JasperDesign.PROPERTY_PAGE_HEIGHT))
			jrDesign.setPageHeight((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_PAGE_WIDTH))
			jrDesign.setPageWidth((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_RIGHT_MARGIN))
			jrDesign.setRightMargin((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_LEFT_MARGIN))
			jrDesign.setLeftMargin((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_TOP_MARGIN))
			jrDesign.setTopMargin((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_BOTTOM_MARGIN))
			jrDesign.setBottomMargin((Integer) Misc.nvl(value, Integer.valueOf(0)));

		else if (id.equals(JasperDesign.PROPERTY_COLUMN_COUNT))
			jrDesign.setColumnCount((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_COLUMN_SPACING))
			jrDesign.setColumnSpacing((Integer) Misc.nvl(value, Integer.valueOf(0)));
		else if (id.equals(JasperDesign.PROPERTY_COLUMN_WIDTH))
			jrDesign.setColumnWidth((Integer) Misc.nvl(value, Integer.valueOf(0)));
		// -- enums
		else if (id.equals(JasperDesign.PROPERTY_ORIENTATION))
			jrDesign.setOrientation((OrientationEnum) orientationD.getEnumValue(value));
		else if (id.equals(JasperDesign.PROPERTY_PRINT_ORDER))
			jrDesign.setPrintOrder((PrintOrderEnum) printOrderD.getEnumValue(value));
		else if (id.equals(JasperDesign.PROPERTY_WHEN_NO_DATA_TYPE))
			jrDesign.setWhenNoDataType((WhenNoDataTypeEnum) whenNoDataD.getEnumValue(value));
		// -- booleans
		else if (id.equals(JasperDesign.PROPERTY_TITLE_NEW_PAGE))
			jrDesign.setTitleNewPage(((Boolean) value).booleanValue());
		else if (id.equals(JasperDesign.PROPERTY_SUMMARY_NEW_PAGE))
			jrDesign.setSummaryNewPage(((Boolean) value).booleanValue());
		else if (id.equals(JasperDesign.PROPERTY_SUMMARY_WITH_PAGE_HEADER_AND_FOOTER))
			jrDesign.setSummaryWithPageHeaderAndFooter(((Boolean) value).booleanValue());
		else if (id.equals(JasperDesign.PROPERTY_FLOAT_COLUMN_FOOTER))
			jrDesign.setFloatColumnFooter(((Boolean) value).booleanValue());
		else if (id.equals(JasperDesign.PROPERTY_IGNORE_PAGINATION))
			jrDesign.setIgnorePagination(((Boolean) value).booleanValue());
		else if (id.equals(MGraphicElement.PROPERTY_MAP)) {
			JRPropertiesMap v = (JRPropertiesMap) value;
			String[] names = jrDesign.getPropertiesMap().getPropertyNames();
			for (int i = 0; i < names.length; i++)
				jrDesign.getPropertiesMap().removeProperty(names[i]);
			names = v.getPropertyNames();

			for (String str : v.getPropertyNames())
				jrDesign.setProperty(str, v.getProperty(str));
			this.getPropertyChangeSupport().firePropertyChange(MGraphicElement.PROPERTY_MAP, false, true);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return ((JasperDesign) getValue()).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getDefaultHeight()
	 */
	public int getDefaultHeight() {
		return 800;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getDefaultWidth()
	 */
	public int getDefaultWidth() {
		return 800;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#createJRElement(net.sf.jasperreports.engine.design.JasperDesign)
	 */
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.ANode#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_COUNT) || 
				evt.getPropertyName().equals(JasperDesign.PROPERTY_LEFT_MARGIN) ||
				evt.getPropertyName().equals(JasperDesign.PROPERTY_RIGHT_MARGIN) || 
				evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_WIDTH) || 
				evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_SPACING)){
			PageFormatUtils.updateColumnWidth(getValue());
		} else if (evt.getPropertyName().equals(JasperDesign.PROPERTY_DATASETS)) {
			handleDatasourceChanged(evt);
		} else if (evt.getPropertyName().equals(JasperDesign.PROPERTY_TITLE)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_HEADER)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_HEADER)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_COLUMN_FOOTER)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_PAGE_FOOTER)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_LAST_PAGE_FOOTER)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_SUMMARY)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_NO_DATA)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_DETAIL)
				|| evt.getPropertyName().equals(JasperDesign.PROPERTY_BACKGROUND)) {
			handleBandChanged(evt);
		} else if (evt.getPropertyName().equals(JRDesignSection.PROPERTY_BANDS)) {
			handleDetailBandChanged(evt);
		} else if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_GROUPS)) {
			handleGroupChanged(evt);
		} else if (evt.getPropertyName().equals(CHANGE_BAND_POSITION)) {
			handleChangeOrder(evt);
		} else if (evt.getPropertyName().equals(JRDesignDataset.PROPERTY_QUERY))
			return;
		super.propertyChange(evt);
	}

	/**
	 * Handle datasource changed.
	 * 
	 * @param evt
	 *          the evt
	 */
	private void handleDatasourceChanged(PropertyChangeEvent evt) {
		if (evt.getSource() == getValue()) {
			if (evt.getOldValue() == null && evt.getNewValue() != null) {
				int newIndex = -1;
				if (evt instanceof CollectionElementAddedEvent) {
					newIndex = ((CollectionElementAddedEvent) evt).getAddedIndex() + 5;
				}
				if (evt.getNewValue() instanceof JRDesignDataset) {

					// add the node to this parent
					JRDesignDataset jrDataset = (JRDesignDataset) evt.getNewValue();
					if (this.findElement(jrDataset) == -1) {
						ANode n = ReportFactory.createNode(this, jrDataset, newIndex);
						ReportFactory.createDataset(n, jrDataset, true);
					}
				}
			} else if (evt.getOldValue() != null && evt.getNewValue() == null) {
				// delete
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue()) {
						removeChild((ANode) n);
						break;
					}
				}
			} else {
				// changed
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue())
						n.setValue(evt.getNewValue());
				}
			}
		}
	}

	/**
	 * Handle band changed.
	 * 
	 * @param evt
	 *          the evt
	 */
	private void handleBandChanged(PropertyChangeEvent evt) {
		for (Iterator<?> it = getChildren().iterator(); it.hasNext();) {
			ANode node = (ANode) it.next();
			if (node instanceof MBand) {
				MBand mBand = (MBand) node;
				if (evt.getPropertyName().equals(mBand.getBandType().getName())) {
					mBand.setValue(evt.getNewValue());
					if (evt.getNewValue() != null)
						ReportFactory.createElementsForBand(mBand, ((JRDesignBand) evt.getNewValue()).getChildren());
					else
						mBand.removeChildren();
					mBand.propertyChange(new PropertyChangeEvent(mBand, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
					break;
				}
			}
		}
	}

	/**
	 * Handle the change of the position of a band
	 * 
	 * @param evt the event that changed the band position
	 */
	private void handleChangeOrder(PropertyChangeEvent evt) {
		if (evt instanceof IndexedPropertyChangeEvent && evt.getNewValue() instanceof Integer) {
			JRDesignSection source = (JRDesignSection) evt.getSource();
			int newInd = ((IndexedPropertyChangeEvent) evt).getIndex();
			JRBand b = source.getBandsList().get(newInd);
			MBand mb = null;
			for (INode n : getChildren()) {
				if (n.getValue() == b) {
					mb = (MBand) n;
					break;
				}
			}
			if (mb != null) {
				newInd = getChildren().indexOf(mb) + (newInd - (Integer) evt.getOldValue());
				getChildren().remove(mb);
				getChildren().add(newInd, mb);
			}
		}
	}
	
	/**
	 * Handle detail band changed.
	 * 
	 * @param evt
	 *          the evt
	 */
	private void handleDetailBandChanged(PropertyChangeEvent evt) {
		MBand firstBand = null;
		MBand lastBand = null;
		int lastIndex = 0;
		JRDesignSection source = (JRDesignSection) evt.getSource();
		JROrigin sourceOrigin = source.getOrigin();
		String groupName = sourceOrigin.getGroupName();
		for (INode n : getChildren()) {
			if (n instanceof MBand) {
				MBand mBand = (MBand) n;
				BandTypeEnum bt = sourceOrigin.getBandTypeValue();
				if ((mBand instanceof MBandGroupHeader && groupName != null && bt.equals(BandTypeEnum.GROUP_HEADER) && groupName
						.equals(((MBandGroupHeader) mBand).getJrGroup().getName()))

						|| (mBand instanceof MBandGroupFooter && groupName != null && bt.equals(BandTypeEnum.GROUP_FOOTER) && groupName
								.equals(((MBandGroupFooter) mBand).getJrGroup().getName()))

						|| (bt.equals(BandTypeEnum.DETAIL) && BandTypeEnum.DETAIL.equals(mBand.getBandType()))) {
					if (firstBand == null)
						firstBand = mBand;
					lastBand = mBand;
				} else if (firstBand != null)
					break;
			}
			lastIndex++;
		}
		int find = getChildren().indexOf(firstBand);
		if (evt instanceof IndexedPropertyChangeEvent && evt.getNewValue() instanceof Integer) {
			int newInd = ((IndexedPropertyChangeEvent) evt).getIndex();
			JRBand b = source.getBandsList().get(newInd);
			MBand mb = null;
			for (INode n : getChildren()) {
				if (n.getValue() == b) {
					mb = (MBand) n;
					break;
				}
			}
			if (mb != null) {
				newInd = getChildren().indexOf(mb) + (newInd - (Integer) evt.getOldValue());
				removeChild(mb);
				addChild(mb, newInd);
			}
		} else if (evt.getNewValue() != null) {
			// new value
			if (firstBand != null && firstBand.equals(lastBand) && firstBand.getValue() == null) {
				firstBand.setValue(evt.getNewValue());
			} else {
				int index = lastIndex;
				if (evt instanceof CollectionElementAddedEvent)
					index = find + ((CollectionElementAddedEvent) evt).getAddedIndex();
				if (firstBand instanceof MBandGroupHeader)
					firstBand = new MBandGroupHeader(this, ((MBandGroupHeader) firstBand).getJrGroup(),
							(JRBand) evt.getNewValue(), index);
				else if (firstBand instanceof MBandGroupFooter)
					firstBand = new MBandGroupFooter(this, ((MBandGroupFooter) firstBand).getJrGroup(),
							(JRBand) evt.getNewValue(), index);
				else
					firstBand = (MBand) ReportFactory.createNode(this, evt.getNewValue(), index);
			}
			ReportFactory.createElementsForBand(firstBand, ((JRDesignBand) evt.getNewValue()).getChildren());
			firstBand.propertyChange(new PropertyChangeEvent(firstBand, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
		} else {
			// delete
			if (firstBand != null && firstBand.equals(lastBand)) {
				firstBand.setValue(evt.getNewValue());
				firstBand.removeChildren();
				firstBand.propertyChange(new PropertyChangeEvent(firstBand, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
			} else {
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue()) {
						firstBand = (MBand) n;
						removeChild(firstBand);
						break;
					}
				}
			}
		}
	}

	/**
	 * Handle group changed.
	 * 
	 * @param evt
	 *          the evt
	 */
	private void handleGroupChanged(PropertyChangeEvent evt) {
		if (evt.getOldValue() != null && evt.getNewValue() == null) { // delete
			JRDesignGroup group = (JRDesignGroup) evt.getOldValue();
			removeGroupListener(group);
			List<ANode> dNodes = new ArrayList<ANode>();
			for (INode node : getChildren()) {
				if (node instanceof MBandGroupHeader) {
					MBandGroupHeader band = (MBandGroupHeader) node;
					if (band.getJrGroup().equals(group))
						dNodes.add(band);
				} else if (node instanceof MBandGroupFooter) {
					MBandGroupFooter band = (MBandGroupFooter) node;
					if (band.getJrGroup().equals(group))
						dNodes.add(band);
				}
			}
			for (ANode n : dNodes) {
				removeChild(n);
			}
		} else if (evt instanceof CollectionElementAddedEvent && evt.getNewValue() != null && evt.getOldValue() == null) {
			JRDesignGroup group = (JRDesignGroup) evt.getNewValue();
			for (INode n : getChildren()) {
				if (n instanceof MBandGroupHeader && ((MBandGroupHeader) n).getJrGroup() == group)
					return;
				if (n instanceof MBandGroupFooter && ((MBandGroupFooter) n).getJrGroup() == group)
					return;
			}

			// Check if the new group is for the main dataset or from a subdataset, in the second case the band are not
			// created
			boolean createBands = !getJasperDesign().getDatasetMap().containsKey(((JRDataset) evt.getSource()).getName());
			if (createBands) {
				// find the right position to put the band
				addGroupListener(group);
				int position = 0;
				int grPosition = ((CollectionElementAddedEvent) evt).getAddedIndex();
				int groupsToSkip = grPosition;
				JRDesignGroup previousGroup = null;
				for (INode node : getChildren()) {
					if (node instanceof MBandGroupHeader) {
						MBandGroupHeader band = (MBandGroupHeader) node;
						if (previousGroup == null || !previousGroup.equals(band.getJrGroup())) {
							previousGroup = band.getJrGroup();
							// ok, we are after the group
							if (groupsToSkip == 0) {
								break;
							}
							groupsToSkip--;
						}
						// ok, I'm now just create in the right position the bands
					} else if (node instanceof MBand && ((MBand) node).getBandType().equals(BandTypeEnum.DETAIL))
						break;
					position++;
				}

				if (group.getGroupHeaderSection() != null) {
					List<?> grhBands = ((JRDesignSection) group.getGroupHeaderSection()).getBandsList();
					if (grhBands != null) {
						if (grhBands.isEmpty()) {
							MBand b = new MBandGroupHeader(this, group, null, position);
							b.propertyChange(new PropertyChangeEvent(b, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
						} else {
							int j = 0;
							for (Iterator<?> it = grhBands.iterator(); it.hasNext(); j++) {
								JRDesignBand jrDB = (JRDesignBand) it.next();
								MBandGroupHeader b = new MBandGroupHeader(this, group, jrDB, position + j);
								ReportFactory.createElementsForBand(b, jrDB.getChildren());
								b.propertyChange(new PropertyChangeEvent(b, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
							}
						}
					}
				}
				position = getChildren().size();
				previousGroup = null;
				// ADD FOOTER
				groupsToSkip = grPosition;
				for (ListIterator<INode> it = getChildren().listIterator(getChildren().size()); it.hasPrevious();) {
					INode node = it.previous();
					if (node instanceof MBandGroupFooter) {
						MBandGroupFooter band = (MBandGroupFooter) node;
						if (previousGroup == null || !previousGroup.equals(band.getJrGroup())) {
							previousGroup = band.getJrGroup();
							// ok, we are after the group
							if (groupsToSkip == 0) {
								break;
							}
							groupsToSkip--;
						}
						// ok, I'm now just create in the right position the bands
					} else if (node instanceof MBand && ((MBand) node).getBandType().equals(BandTypeEnum.DETAIL))
						break;
					position--;
				}
				if (group.getGroupFooterSection() != null) {
					List<?> grhBands = ((JRDesignSection) group.getGroupFooterSection()).getBandsList();
					if (grhBands != null) {
						if (grhBands.isEmpty()) {
							MBand b = new MBandGroupFooter(this, group, null, position);
							b.propertyChange(new PropertyChangeEvent(b, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
						} else {
							int j = 0;
							for (Iterator<?> it = grhBands.iterator(); it.hasNext(); j++) {
								JRDesignBand jrDB = (JRDesignBand) it.next();
								MBandGroupFooter b = new MBandGroupFooter(this, group, jrDB, position + j);
								ReportFactory.createElementsForBand(b, jrDB.getChildren());
								b.propertyChange(new PropertyChangeEvent(b, "VALUE", evt.getOldValue(), evt.getNewValue())); //$NON-NLS-1$
							}
						}
					}
				}
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.ANode#setValue(java.lang.Object)
	 */
	@Override
	public void setValue(Object value) {
		if (getValue() != null) {
			JasperDesign jasperDesign = (JasperDesign) getValue();
			JRDesignDataset jrDesignDataset = (JRDesignDataset) jasperDesign.getMainDataset();
			jrDesignDataset.getEventSupport().removePropertyChangeListener(this);
			((JRDesignSection) jasperDesign.getDetailSection()).getEventSupport().removePropertyChangeListener(this);
			for (JRGroup obj : jrDesignDataset.getGroupsList())
				removeGroupListener((JRDesignGroup) obj);
			// for (JRField obj : jrDesignDataset.getFieldsList())
			// ((JRDesignField) obj).getEventSupport().removePropertyChangeListener(this);
			// for (JRSortField obj : jrDesignDataset.getSortFieldsList())
			// ((JRDesignSortField) obj).getEventSupport().removePropertyChangeListener(this);
			// for (JRParameter obj : jrDesignDataset.getParametersList())
			// ((JRDesignParameter) obj).getEventSupport().removePropertyChangeListener(this);
			// for (JRVariable obj : jrDesignDataset.getVariablesList())
			// ((JRDesignVariable) obj).getEventSupport().removePropertyChangeListener(this);

			JRPropertiesMap pmap = jrDesignDataset.getPropertiesMap();
			pmap.getEventSupport().removePropertyChangeListener(this);
			if (mDataset != null)
				mDataset.setValue(null);
		}
		if (value != null) {
			JasperDesign jasperDesign = (JasperDesign) value;
			JRDesignDataset jrDesignDataset = (JRDesignDataset) jasperDesign.getMainDataset();
			jrDesignDataset.getEventSupport().addPropertyChangeListener(this);
			for (JRGroup obj : jrDesignDataset.getGroupsList())
				addGroupListener((JRDesignGroup) obj);
			// for (JRField obj : jrDesignDataset.getFieldsList())
			// ((JRDesignField) obj).getEventSupport().addPropertyChangeListener(this);
			// for (JRSortField obj : jrDesignDataset.getSortFieldsList())
			// ((JRDesignSortField) obj).getEventSupport().addPropertyChangeListener(this);
			// for (JRParameter obj : jrDesignDataset.getParametersList())
			// ((JRDesignParameter) obj).getEventSupport().addPropertyChangeListener(this);
			// for (JRVariable obj : jrDesignDataset.getVariablesList())
			// ((JRDesignVariable) obj).getEventSupport().addPropertyChangeListener(this);

			JRPropertiesMap pmap = jrDesignDataset.getPropertiesMap();
			pmap.getEventSupport().addPropertyChangeListener(this);

			((JRDesignSection) jasperDesign.getDetailSection()).getEventSupport().addPropertyChangeListener(this);
			// mDataset = null;
			if (mDataset != null)
				mDataset.setValue(jrDesignDataset);
		}
		super.setValue(value);
	}

	/**
	 * Adds the group listener.
	 * 
	 * @param gr
	 *          the gr
	 */
	private void addGroupListener(JRDesignGroup gr) {
		((JRDesignSection) gr.getGroupFooterSection()).getEventSupport().addPropertyChangeListener(this);
		((JRDesignSection) gr.getGroupHeaderSection()).getEventSupport().addPropertyChangeListener(this);
	}

	/**
	 * Removes the group listener.
	 * 
	 * @param gr
	 *          the gr
	 */
	private void removeGroupListener(JRDesignGroup gr) {
		((JRDesignSection) gr.getGroupFooterSection()).getEventSupport().removePropertyChangeListener(this);
		((JRDesignSection) gr.getGroupHeaderSection()).getEventSupport().removePropertyChangeListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getBounds()
	 */
	public Rectangle getBounds() {
		JasperDesign jd = getJasperDesign();
		return new Rectangle(jd.getLeftMargin(), jd.getTopMargin(), jd.getPageWidth() - jd.getLeftMargin()
				- jd.getRightMargin(), jd.getPageHeight() - jd.getTopMargin() - jd.getBottomMargin());
	}

	private Map<String, Object> parameters;

	public Object getParameter(String key) {
		if (parameters == null)
			parameters = new HashMap<String, Object>();
		return parameters.get(key);
	}

	private static JSSEnumPropertyDescriptor orientationD;
	private static JSSEnumPropertyDescriptor printOrderD;
	private static JSSEnumPropertyDescriptor whenNoDataD;

	public void putParameter(String key, Object value) {
		if (parameters == null)
			parameters = new HashMap<String, Object>();
		parameters.put(key, value);
	}

	public static String getMeasureUnit(JasperReportsConfiguration jConfig, JasperDesign jd) {
		String defunit = jConfig.getProperty(DesignerPreferencePage.P_PAGE_DEFAULT_UNITS);
		defunit = PHolderUtil.getUnit(jd, "", defunit);
		return defunit;
	}

	@Override
	public JRPropertiesHolder[] getPropertyHolder() {
		return new JRPropertiesHolder[] { getValue() };
	}

	private Map<Object, Integer> bandIndexMap = new HashMap<Object, Integer>();

	public Integer getBandIndex(Object band) {
		return bandIndexMap.get(band);
	}

	public void setBandIndex(int index, Object band) {
		bandIndexMap.put(band, index);
	}
	
	@Override
	public boolean canAcceptChildren(ANode child) {
		return (child instanceof MBand); 
	}
}
