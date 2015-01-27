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
package com.jaspersoft.studio.components.map.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemData;
import net.sf.jasperreports.components.map.MapComponent;
import net.sf.jasperreports.components.map.StandardItemData;
import net.sf.jasperreports.components.map.StandardMapComponent;
import net.sf.jasperreports.components.map.type.MapImageTypeEnum;
import net.sf.jasperreports.components.map.type.MapScaleEnum;
import net.sf.jasperreports.components.map.type.MapTypeEnum;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.OnErrorTypeEnum;
import net.sf.jasperreports.engine.util.JRCloneUtils;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.map.MapNodeIconDescriptor;
import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.ElementDataHelper;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementsConfiguration;
import com.jaspersoft.studio.components.map.model.marker.MarkerDescriptor;
import com.jaspersoft.studio.components.map.model.marker.MarkersDTO;
import com.jaspersoft.studio.components.map.model.path.MapPathsDescriptor;
import com.jaspersoft.studio.components.map.model.style.MapStylesDescriptor;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.jasper.MapDesignConverter;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IDatasetContainer;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.model.dataset.descriptor.DatasetRunPropertyDescriptor;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * 
 * @author sanda zaharia
 * 
 */
public class MMap extends MGraphicElement implements IDatasetContainer {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MMap() {
		super();
	}

	public MMap(ANode parent, JRDesignComponentElement jrObject, int newIndex) {
		super(parent, jrObject, newIndex);
		listenMap();
	}

	@Override
	public void setParent(ANode parent, int newIndex) {
		super.setParent(parent, newIndex);
		if (parent == null) {
			unlistenMap();
		}
	}

	private void listenMap() {
		StandardMapComponent m = getMapComponent();
		if (m != null && ModelUtils.getSingleMarkerData(m) != null) {
			((StandardItemData) ModelUtils.getSingleMarkerData(m)).getEventSupport().addPropertyChangeListener(this);
		}
	}

	private void unlistenMap() {
		StandardMapComponent m = getMapComponent();
		if (m != null && ModelUtils.getSingleMarkerData(m) != null) {
			((StandardItemData) ModelUtils.getSingleMarkerData(m)).getEventSupport().removePropertyChangeListener(this);
		}
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
			iconDescriptor = new MapNodeIconDescriptor("map"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private IPropertyDescriptor[] descriptors;
	private JSSEnumPropertyDescriptor onErrorTypeD;
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

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		JRExpressionPropertyDescriptor latitudeExprD = new JRExpressionPropertyDescriptor(StandardMapComponent.PROPERTY_LATITUDE_EXPRESSION, Messages.MMap_latitude);
		latitudeExprD.setDescription(Messages.MMap_latitude_description);
		desc.add(latitudeExprD);
		latitudeExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/components.schema.reference.html#latitudeExpression")); //$NON-NLS-1$

		JRExpressionPropertyDescriptor longitudeExprD = new JRExpressionPropertyDescriptor(StandardMapComponent.PROPERTY_LONGITUDE_EXPRESSION, Messages.MMap_longitude);
		longitudeExprD.setDescription(Messages.MMap_longitude_description);
		desc.add(longitudeExprD);
		longitudeExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/components.schema.reference.html#longitudeExpression")); //$NON-NLS-1$

		JRExpressionPropertyDescriptor zoomExprD = new JRExpressionPropertyDescriptor(StandardMapComponent.PROPERTY_ZOOM_EXPRESSION, Messages.MMap_zoom);
		zoomExprD.setDescription(Messages.MMap_zoom_description);
		desc.add(zoomExprD);
		zoomExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/components.schema.reference.html#zoomExpression")); //$NON-NLS-1$

		JRExpressionPropertyDescriptor langExprD = new JRExpressionPropertyDescriptor(StandardMapComponent.PROPERTY_LANGUAGE_EXPRESSION, Messages.MMap_languageExpressionTitle);
		langExprD.setDescription(Messages.MMap_languageExpressionDescription);
		desc.add(langExprD);
		langExprD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/components.schema.reference.html#languageExpression")); //$NON-NLS-1$

		ComboBoxPropertyDescriptor evaluationTimeD = new ComboBoxPropertyDescriptor(StandardMapComponent.PROPERTY_EVALUATION_TIME, Messages.MMap_evaluation_time, EnumHelper.getEnumNames(
				EvaluationTimeEnum.values(), NullEnum.NOTNULL));
		evaluationTimeD.setDescription(Messages.MMap_evaluation_time_description);
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(StandardMapComponent.PROPERTY_EVALUATION_GROUP, Messages.MMap_evaluation_group, new String[] { "" }); //$NON-NLS-1$  //$NON-NLS-2$
		evaluationGroupNameD.setDescription(Messages.MMap_evaluation_group_description);
		desc.add(evaluationGroupNameD);

		mapTypeD = new JSSEnumPropertyDescriptor(StandardMapComponent.PROPERTY_MAP_TYPE, Messages.MMap_mapTypeTitle, MapTypeEnum.class, NullEnum.NOTNULL);
		mapTypeD.setDescription(Messages.MMap_mapTypeDescription);
		desc.add(mapTypeD);

		mapScaleD = new JSSEnumPropertyDescriptor(StandardMapComponent.PROPERTY_MAP_SCALE, Messages.MMap_mapScaleTitle, MapScaleEnum.class, NullEnum.NOTNULL);
		mapScaleD.setDescription(Messages.MMap_mapScaleDescription);
		desc.add(mapScaleD);

		imageTypeD = new JSSEnumPropertyDescriptor(StandardMapComponent.PROPERTY_IMAGE_TYPE, Messages.MMap_imageTypeTitle, MapImageTypeEnum.class, NullEnum.NOTNULL);
		imageTypeD.setDescription(Messages.MMap_imageTypeDescription);
		desc.add(imageTypeD);

		onErrorTypeD = new JSSEnumPropertyDescriptor(StandardMapComponent.PROPERTY_ON_ERROR_TYPE, Messages.MMap_OnErrorType, OnErrorTypeEnum.class, NullEnum.NULL);
		onErrorTypeD.setDescription(Messages.MMap_OnErrorTypeDescription);
		desc.add(onErrorTypeD);

		DatasetRunPropertyDescriptor datasetRunD = new DatasetRunPropertyDescriptor(JRDesignElementDataset.PROPERTY_DATASET_RUN, Messages.MMap_markerDatasetTitle, true);
		datasetRunD.setDescription(Messages.MMap_markerDatasetDescription);
		desc.add(datasetRunD);

		MarkerDescriptor markersD = new MarkerDescriptor(StandardItemData.PROPERTY_ITEMS, Messages.MMap_markersTitle);
		markersD.setDescription(Messages.MMap_markersDescription);
		desc.add(markersD);
		markersD.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/components.schema.reference.html#markerDataset")); //$NON-NLS-1$

		NTextPropertyDescriptor mapKeyD = new NTextPropertyDescriptor(MapComponent.PROPERTY_KEY, Messages.MMap_ApiKeyText);
		mapKeyD.setDescription(Messages.MMap_ApiKeyDescription);
		desc.add(mapKeyD);

		NTextPropertyDescriptor mapClientIdD = new NTextPropertyDescriptor(MapComponent.PROPERTY_CLIENT_ID, Messages.MMap_ClientIdText);
		mapClientIdD.setDescription(Messages.MMap_ClientIdDescription);
		desc.add(mapClientIdD);

		NTextPropertyDescriptor mapClientSignatureD = new NTextPropertyDescriptor(MapComponent.PROPERTY_SIGNATURE, Messages.MMap_SignatureText);
		mapClientSignatureD.setDescription(Messages.MMap_SignatureDescription);
		desc.add(mapClientSignatureD);

		NTextPropertyDescriptor mapVersionD = new NTextPropertyDescriptor(MapComponent.PROPERTY_VERSION, Messages.MMap_VersionText);
		mapVersionD.setDescription(Messages.MMap_VersionDescription);
		desc.add(mapVersionD);

		MapPathsDescriptor mapPathsD = new MapPathsDescriptor(StandardMapComponent.PROPERTY_PATH_DATA_LIST, Messages.MMap_MapPaths);
		mapPathsD.setDescription(Messages.MMap_MapPathsDescription);
		desc.add(mapPathsD);

		MapStylesDescriptor mapPathStylesD = new MapStylesDescriptor(StandardMapComponent.PROPERTY_PATH_STYLE_LIST, Messages.MMap_MapStyles);
		mapPathStylesD.setDescription(Messages.MMap_MapStylesDescription);
		desc.add(mapPathStylesD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/components.schema.reference.html#"); //$NON-NLS-1$

		markersD.setCategory(Messages.MMap_common_map_properties);
		langExprD.setCategory(Messages.MMap_common_map_properties);
		datasetRunD.setCategory(Messages.MMap_common_map_properties);
		mapTypeD.setCategory(Messages.MMap_common_map_properties);
		mapScaleD.setCategory(Messages.MMap_common_map_properties);
		imageTypeD.setCategory(Messages.MMap_common_map_properties);
		onErrorTypeD.setCategory(Messages.MMap_common_map_properties);
		evaluationTimeD.setCategory(Messages.MMap_common_map_properties);
		evaluationGroupNameD.setCategory(Messages.MMap_common_map_properties);
		latitudeExprD.setCategory(Messages.MMap_common_map_properties);
		longitudeExprD.setCategory(Messages.MMap_common_map_properties);
		zoomExprD.setCategory(Messages.MMap_common_map_properties);

		mapKeyD.setCategory(Messages.MMap_Category_Authentication);
		mapClientIdD.setCategory(Messages.MMap_Category_Authentication);
		mapClientSignatureD.setCategory(Messages.MMap_Category_Authentication);
		mapVersionD.setCategory(Messages.MMap_Category_Authentication);

		mapPathStylesD.setCategory(Messages.MMap_PathsStylesCategory);
		mapPathsD.setCategory(Messages.MMap_PathsStylesCategoryDesc);

		defaultsMap.put(StandardMapComponent.PROPERTY_MAP_TYPE, MapTypeEnum.ROADMAP);
		defaultsMap.put(StandardMapComponent.PROPERTY_MAP_TYPE, MapScaleEnum.ONE);
		defaultsMap.put(StandardMapComponent.PROPERTY_IMAGE_TYPE, MapImageTypeEnum.PNG);
		defaultsMap.put(StandardMapComponent.PROPERTY_ON_ERROR_TYPE, onErrorTypeD.getEnumValue(OnErrorTypeEnum.ERROR));
		defaultsMap.put(StandardMapComponent.PROPERTY_EVALUATION_TIME, EvaluationTimeEnum.NOW);
		defaultsMap.put(StandardMapComponent.PROPERTY_ZOOM_EXPRESSION, MapComponent.DEFAULT_ZOOM);
	}

	private MDatasetRun mDatasetRun;

	@Override
	public Object getPropertyValue(Object id) {
		StandardMapComponent component = getMapComponent();

		if (id.equals(StandardItemData.PROPERTY_ITEMS)) {
			List<Item> markers = ModelUtils.safeGetMarkerData(component, false).getItems();
			if (markers == null)
				markers = new ArrayList<Item>();
			else {
				markers = JRCloneUtils.cloneList(markers);
			}
			return new MarkersDTO(markers, this);
		}
		if (id.equals(StandardItemData.PROPERTY_DATASET)) {
			if (ModelUtils.getSingleMarkerData(component) != null) {
				return ModelUtils.getSingleMarkerData(component).getDataset();
			}
			return null;
		}
		if (id.equals(JRDesignElementDataset.PROPERTY_DATASET_RUN)) {
			JRElementDataset markerdataset = ModelUtils.safeGetMarkerData(component, false).getDataset();
			JRDatasetRun j = null;
			if (markerdataset != null)
				j = markerdataset.getDatasetRun();
			if (j == null)
				j = new JRDesignDatasetRun();
			else
				j = (JRDatasetRun) j.clone();
			if (mDatasetRun != null)
				mDatasetRun.setValue(j);
			else {
				mDatasetRun = new MDatasetRun(j, getJasperDesign());
				mDatasetRun.setJasperConfiguration(getJasperConfiguration());
				setChildListener(mDatasetRun);
			}
			return mDatasetRun;
		}

		if (id.equals(StandardMapComponent.PROPERTY_EVALUATION_TIME))
			return EnumHelper.getValue(component.getEvaluationTime(), 1, false);
		if (id.equals(StandardMapComponent.PROPERTY_EVALUATION_GROUP))
			return component.getEvaluationGroup();

		if (id.equals(StandardMapComponent.PROPERTY_LANGUAGE_EXPRESSION))
			return ExprUtil.getExpression(component.getLanguageExpression());
		if (id.equals(StandardMapComponent.PROPERTY_LONGITUDE_EXPRESSION))
			return ExprUtil.getExpression(component.getLongitudeExpression());
		if (id.equals(StandardMapComponent.PROPERTY_LATITUDE_EXPRESSION))
			return ExprUtil.getExpression(component.getLatitudeExpression());
		if (id.equals(StandardMapComponent.PROPERTY_ZOOM_EXPRESSION))
			return ExprUtil.getExpression(component.getZoomExpression());

		if (id.equals(StandardMapComponent.PROPERTY_MAP_TYPE))
			return mapTypeD.getEnumValue(component.getMapType());
		if (id.equals(StandardMapComponent.PROPERTY_MAP_SCALE))
			return mapScaleD.getEnumValue(component.getMapScale());
		if (id.equals(StandardMapComponent.PROPERTY_IMAGE_TYPE))
			return imageTypeD.getEnumValue(component.getImageType());
		if (id.equals(StandardMapComponent.PROPERTY_ON_ERROR_TYPE)) {
			return onErrorTypeD.getEnumValue(component.getOnErrorType());
		}

		// map authentication info
		if (id.equals(MapComponent.PROPERTY_KEY)) {
			return getJasperDesign().getProperty(MapComponent.PROPERTY_KEY);
		} else if (id.equals(MapComponent.PROPERTY_CLIENT_ID)) {
			return getJasperDesign().getProperty(MapComponent.PROPERTY_CLIENT_ID);
		} else if (id.equals(MapComponent.PROPERTY_SIGNATURE)) {
			return getJasperDesign().getProperty(MapComponent.PROPERTY_SIGNATURE);
		} else if (id.equals(MapComponent.PROPERTY_VERSION)) {
			return getJasperDesign().getProperty(MapComponent.PROPERTY_VERSION);
		}

		if (id.equals(StandardMapComponent.PROPERTY_PATH_DATA_LIST)) {
			List<ItemData> pathDataList = component.getPathDataList();
			if (pathDataList == null) {
				pathDataList = new ArrayList<ItemData>();
			} else {
				pathDataList = JRCloneUtils.cloneList(pathDataList);
			}
			return ElementDataHelper.convertFromElementDataInformation(pathDataList, Messages.MMap_PathLabel);
		}

		if (id.equals(StandardMapComponent.PROPERTY_PATH_STYLE_LIST)) {
			List<ItemData> pathStylesList = component.getPathStyleList();
			if (pathStylesList == null) {
				pathStylesList = new ArrayList<ItemData>();
			} else {
				pathStylesList = JRCloneUtils.cloneList(pathStylesList);
			}
			return ElementDataHelper.convertFromElementDataInformation(pathStylesList, Messages.MMap_StyleLabel);
		}

		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		StandardMapComponent component = getMapComponent();

		StandardItemData markerdata = (StandardItemData) ModelUtils.getSingleMarkerData(component);
		if (id.equals(StandardItemData.PROPERTY_ITEMS)) {
			if (value instanceof MarkersDTO) {
				markerdata = safeGetMarkerData(component, markerdata);
				List<Item> markers = markerdata.getItems();
				if (!markers.isEmpty()) {
					Item[] marray = markers.toArray(new Item[markers.size()]);
					for (Item m : marray)
						markerdata.removeItem(m);
				}
				MarkersDTO mdto = (MarkersDTO) value;
				if (mdto.getMarkers() != null)
					for (Item m : mdto.getMarkers())
						markerdata.addItem(m);
			}
		} else if (id.equals(StandardItemData.PROPERTY_DATASET)) {
			markerdata = safeGetMarkerData(component, markerdata);
			if (value instanceof JRElementDataset) {
				markerdata.setDataset((JRElementDataset) value);
			} else {
				markerdata.setDataset(null);
			}
		} else if (id.equals(JRDesignElementDataset.PROPERTY_DATASET_RUN)) {
			MDatasetRun mdr = (MDatasetRun) value;
			JRDesignDatasetRun dr = (JRDesignDatasetRun) mdr.getValue();
			markerdata = safeGetMarkerData(component, markerdata);
			if (markerdata.getDataset() == null) {
				markerdata.setDataset(new JRDesignElementDataset());
			}
			if (dr.getDatasetName() != null) {
				((JRDesignElementDataset) markerdata.getDataset()).setDatasetRun(dr);
			} else {
				((JRDesignElementDataset) markerdata.getDataset()).setDatasetRun(null);
			}
		} else if (id.equals(StandardMapComponent.PROPERTY_EVALUATION_TIME))
			component.setEvaluationTime((EvaluationTimeEnum) EnumHelper.getSetValue(EvaluationTimeEnum.values(), value, 1, false));
		else if (id.equals(StandardMapComponent.PROPERTY_EVALUATION_GROUP))
			component.setEvaluationGroup((String) value);
		else if (id.equals(StandardMapComponent.PROPERTY_LONGITUDE_EXPRESSION)) {
			component.setLongitudeExpression(ExprUtil.setValues(component.getLongitudeExpression(), value, null));
		} else if (id.equals(StandardMapComponent.PROPERTY_LATITUDE_EXPRESSION)) {
			component.setLatitudeExpression(ExprUtil.setValues(component.getLatitudeExpression(), value, null));
		} else if (id.equals(StandardMapComponent.PROPERTY_LANGUAGE_EXPRESSION)) {
			component.setLanguageExpression(ExprUtil.setValues(component.getLanguageExpression(), value, null));
		} else if (id.equals(StandardMapComponent.PROPERTY_ZOOM_EXPRESSION)) {
			component.setZoomExpression(ExprUtil.setValues(component.getZoomExpression(), value, null));
		} else if (id.equals(StandardMapComponent.PROPERTY_MAP_TYPE))
			component.setMapType((MapTypeEnum) mapTypeD.getEnumValue(value));
		else if (id.equals(StandardMapComponent.PROPERTY_MAP_SCALE))
			component.setMapScale((MapScaleEnum) mapScaleD.getEnumValue(value));
		else if (id.equals(StandardMapComponent.PROPERTY_IMAGE_TYPE))
			component.setImageType((MapImageTypeEnum) imageTypeD.getEnumValue(value));
		else if (id.equals(StandardMapComponent.PROPERTY_ON_ERROR_TYPE)) {
			component.setOnErrorType((OnErrorTypeEnum) onErrorTypeD.getEnumValue(value));
		} else if (id.equals(MapComponent.PROPERTY_KEY)) {
			if (value instanceof String) {
				getJasperDesign().setProperty(MapComponent.PROPERTY_KEY, (String) value);
			} else {
				getJasperDesign().removeProperty(MapComponent.PROPERTY_KEY);
			}
		} else if (id.equals(MapComponent.PROPERTY_CLIENT_ID)) {
			if (value instanceof String) {
				getJasperDesign().setProperty(MapComponent.PROPERTY_CLIENT_ID, (String) value);
			} else {
				getJasperDesign().removeProperty(MapComponent.PROPERTY_CLIENT_ID);
			}
		} else if (id.equals(MapComponent.PROPERTY_SIGNATURE)) {
			if (value instanceof String) {
				getJasperDesign().setProperty(MapComponent.PROPERTY_SIGNATURE, (String) value);
			} else {
				getJasperDesign().removeProperty(MapComponent.PROPERTY_SIGNATURE);
			}
		} else if (id.equals(MapComponent.PROPERTY_VERSION)) {
			if (value instanceof String) {
				getJasperDesign().setProperty(MapComponent.PROPERTY_VERSION, (String) value);
			} else {
				getJasperDesign().removeProperty(MapComponent.PROPERTY_VERSION);
			}
		} else if (id.equals(StandardMapComponent.PROPERTY_PATH_DATA_LIST)) {
			List<ItemData> pathDataList = ElementDataHelper.convertToElementDataInformation((MapDataElementsConfiguration) value);
			Object[] existingPaths = component.getPathDataList().toArray();
			for (Object p : existingPaths) {
				component.removePathData((ItemData) p);
			}
			for (ItemData n : pathDataList) {
				component.addPathData(n);
			}
		} else if (id.equals(StandardMapComponent.PROPERTY_PATH_STYLE_LIST)) {
			List<ItemData> pathStyleList = ElementDataHelper.convertToElementDataInformation((MapDataElementsConfiguration) value);
			Object[] existingStyles = component.getPathStyleList().toArray();
			for (Object p : existingStyles) {
				component.removePathStyle((ItemData) p);
			}
			for (ItemData n : pathStyleList) {
				component.addPathStyle(n);
			}
		} else
			super.setPropertyValue(id, value);
	}

	private StandardItemData safeGetMarkerData(StandardMapComponent component, StandardItemData markerdata) {
		if (markerdata == null) {
			markerdata = new StandardItemData();
			component.addMarkerData(markerdata);
			listenMap();
		}
		return markerdata;
	}

	private StandardMapComponent getMapComponent() {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		if (jrElement == null)
			return null;
		return (StandardMapComponent) jrElement.getComponent();
	}

	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

	private RComboBoxPropertyDescriptor evaluationGroupNameD;
	private static JSSEnumPropertyDescriptor mapTypeD;
	private static JSSEnumPropertyDescriptor imageTypeD;
	private static JSSEnumPropertyDescriptor mapScaleD;

	@Override
	public void setValue(Object value) {
		if (getValue() != null) {
			Object obj = getComponent();
			if (obj instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport().removePropertyChangeListener(this);
			unlistenMap();
		}
		if (value != null) {
			Object obj = getComponent(value);
			if (value instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport().addPropertyChangeListener(this);
			listenMap();
		}
		super.setValue(value);
	}

	private Object getComponent() {
		return getComponent(getValue());
	}

	private Object getComponent(Object value) {
		if (value != null) {
			JRDesignComponentElement jrElement = (JRDesignComponentElement) value;
			return jrElement.getComponent();
		}
		return null;
	}

	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement designMap = new JRDesignComponentElement();
		StandardMapComponent component = new StandardMapComponent();
		JRDesignExpression exp1 = new JRDesignExpression();
		exp1.setText(MapDesignConverter.DEFAULT_LATITUDE.toString() + "f"); //$NON-NLS-1$
		JRDesignExpression exp2 = new JRDesignExpression();
		exp2.setText(MapDesignConverter.DEFAULT_LONGITUDE.toString() + "f"); //$NON-NLS-1$
		component.setLatitudeExpression(exp1);
		component.setLongitudeExpression(exp2);
		JRDesignExpression exp3 = new JRDesignExpression();
		exp3.setText(String.valueOf(MapComponent.DEFAULT_ZOOM));
		component.setZoomExpression(exp3);
		designMap.setComponent(component);
		designMap.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", "c", //$NON-NLS-1$ //$NON-NLS-2$
				"map")); //$NON-NLS-1$
		StandardItemData markerData = safeGetMarkerData(component, null);
		markerData.setDataset(new JRDesignElementDataset());
		
		DefaultManager.INSTANCE.applyDefault(this.getClass(), designMap);
		
		return designMap;
	}
	
	/**
	 * Returns the expression context that should be used with the map markers.
	 * Markers can be generated by a dataset and therefore the related expressions
	 * should use this as their context.
	 * 
	 * @return the expression context to be used for the markers expressions
	 */
	public ExpressionContext getMarkersExpressionContext() {
		MDatasetRun datasetRun = (MDatasetRun) getPropertyValue(JRDesignElementDataset.PROPERTY_DATASET_RUN);
		if(datasetRun!=null){
			JRDesignDatasetRun value = datasetRun.getValue();
			if(value!=null && value.getDatasetName()!=null){
				JRDesignDataset ds = ModelUtils.getDesignDatasetByName(getJasperDesign(),value.getDatasetName());
				if(ds!=null) {
					return new ExpressionContext(ds, getJasperConfiguration());
				}
			}
		}
		return null;
	}

	/**
	 * The table dataset update the value of the inner JRDesignDatasetRun only
	 * when a set of the property of the datasetrun is done directly on the map
	 * element. If the set of the value is done only on the inner dataset run
	 * model then the change will be overwritten on the first get property value
	 * of the dataset run property
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class MapDatasetRun extends MDatasetRun {

		private static final long serialVersionUID = -7526842349391237513L;
		private MMap mapModel;

		public MapDatasetRun(JRDatasetRun value, MMap mapModel) {
			super(value, mapModel.getJasperDesign());
			this.mapModel = mapModel;
		}

		@Override
		public void setPropertyValue(Object id, Object value) {
			if (id.equals(JRDesignDatasetRun.PROPERTY_DATASET_NAME)) {
				super.setPropertyValue(id, value);
				mapModel.setPropertyValue(JRDesignElementDataset.PROPERTY_DATASET_RUN, this);
			} else
				super.setPropertyValue(id, value);
		}

	}

	@Override
	public List<MDatasetRun> getDatasetRunList() {
		List<MDatasetRun> datasetList = new ArrayList<MDatasetRun>();
		MDatasetRun datasetRun = (MDatasetRun) getPropertyValue(JRDesignElementDataset.PROPERTY_DATASET_RUN);
		datasetList.add(new MapDatasetRun(datasetRun.getValue(), this));
		return datasetList;
	}

	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);

		StandardMapComponent jrSourceMap = getMapComponent();
		JRDesignComponentElement jrTargetElement = (JRDesignComponentElement) target;
		StandardMapComponent jrTargetMap = (StandardMapComponent) jrTargetElement.getComponent();
		
		jrTargetMap.setMapType(jrSourceMap.getMapType());
		jrTargetMap.setMapScale(jrSourceMap.getMapScale());
		jrTargetMap.setImageType(jrSourceMap.getImageType());
		jrTargetMap.setOnErrorType(jrSourceMap.getOnErrorType());

	}
}
