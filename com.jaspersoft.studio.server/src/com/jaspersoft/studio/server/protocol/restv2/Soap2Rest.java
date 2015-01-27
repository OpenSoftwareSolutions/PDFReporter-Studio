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
package com.jaspersoft.studio.server.protocol.restv2;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.jasperserver.dto.resources.AbstractClientReportUnit;
import com.jaspersoft.jasperserver.dto.resources.AbstractClientReportUnit.ControlsLayoutType;
import com.jaspersoft.jasperserver.dto.resources.ClientAdhocDataView;
import com.jaspersoft.jasperserver.dto.resources.ClientAwsDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientBeanDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientCustomDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientDataType;
import com.jaspersoft.jasperserver.dto.resources.ClientDataType.TypeOfDataType;
import com.jaspersoft.jasperserver.dto.resources.ClientFile;
import com.jaspersoft.jasperserver.dto.resources.ClientInputControl;
import com.jaspersoft.jasperserver.dto.resources.ClientJdbcDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientJndiJdbcDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientListOfValues;
import com.jaspersoft.jasperserver.dto.resources.ClientListOfValuesItem;
import com.jaspersoft.jasperserver.dto.resources.ClientMondrianConnection;
import com.jaspersoft.jasperserver.dto.resources.ClientMondrianXmlaDefinition;
import com.jaspersoft.jasperserver.dto.resources.ClientOlapUnit;
import com.jaspersoft.jasperserver.dto.resources.ClientProperty;
import com.jaspersoft.jasperserver.dto.resources.ClientQuery;
import com.jaspersoft.jasperserver.dto.resources.ClientReference;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableDataType;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableFile;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableInputControl;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableListOfValues;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableMondrianConnection;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenceableQuery;
import com.jaspersoft.jasperserver.dto.resources.ClientReferenciableOlapConnection;
import com.jaspersoft.jasperserver.dto.resources.ClientReportUnit;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.ClientSecureMondrianConnection;
import com.jaspersoft.jasperserver.dto.resources.ClientSubDataSourceReference;
import com.jaspersoft.jasperserver.dto.resources.ClientVirtualDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientXmlaConnection;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.utils.Misc;

public class Soap2Rest {
	public static Object getResourceContainer(ARestV2Connection rc, ResourceDescriptor rd) throws ParseException {
		if (rd.getParentFolder() != null && !rd.getParentFolder().endsWith("_files"))
			rd.setIsReference(true);
		if (rd.getIsReference())
			return new ClientReference(Misc.nvl(rd.getReferenceUri(), rd.getUriString()));
		if (!rd.getIsNew() && !rd.hasDirtyChildren())
			return new ClientReference(Misc.nvl(rd.getReferenceUri(), rd.getUriString()));
		ClientResource<?> res = getResource(rc, rd);
		if (rd.getIsNew())
			res.setVersion(-1);
		return res;
	}

	public static ClientResource<?> getResource(ARestV2Connection rc, ResourceDescriptor rd) throws ParseException {
		ClientResource<?> cr = WsTypes.INST().createResource(rd);
		cr.setCreationDate(rc.timestamp2str(rd.getCreationDate()));
		cr.setLabel(rd.getLabel());
		cr.setDescription(rd.getDescription());
		cr.setUri(rd.getUriString());
		cr.setVersion(rd.getVersion());
		cr.setUpdateDate(DiffFields.getSoapValue(rd, DiffFields.UPDATEDATE));
		cr.setPermissionMask(rd.getPermissionMask(null));

		if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE))
			getDataType(rc, (ClientDataType) cr, rd);

		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_ADHOC_DATA_VIEW))
			getAdhocDataView(rc, (ClientAdhocDataView) cr, rd);

		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC))
			getJdbcDataSource(rc, (ClientJdbcDataSource) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_BEAN))
			getBeanDataSource(rc, (ClientBeanDataSource) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_JNDI))
			getJndiDataSource(rc, (ClientJndiJdbcDataSource) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM))
			getCustomDataSource(rc, (ClientCustomDataSource) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_AWS))
			getAWSDataSource(rc, (ClientAwsDataSource) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL))
			getVirtualDataSource(rc, (ClientVirtualDataSource) cr, rd);

		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_QUERY))
			getQuery(rc, (ClientQuery) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL))
			getInputControl(rc, (ClientInputControl) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_LOV))
			getLOV(rc, (ClientListOfValues) cr, rd);

		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
			getXmlaConnection(rc, (ClientXmlaConnection) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_OLAPUNIT))
			getOlapUnit(rc, (ClientOlapUnit) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION))
			getMondrianConnection(rc, (ClientMondrianConnection) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION))
			getSecureMondrianConnection(rc, (ClientSecureMondrianConnection) cr, rd);
		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_MONDRIAN_XMLA_DEFINITION_CLIENT_TYPE))
			getMondrianXmlaDefinition(rc, (ClientMondrianXmlaDefinition) cr, rd);

		else if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT))
			getReportUnit(rc, (ClientReportUnit) cr, rd);

		if (WsTypes.INST().getSoapfileMap().containsKey(rd.getWsType()))
			getFile(rc, (ClientFile) cr, rd);

		else
			Activator.getExtManager().getResource(rc, cr, rd);
		return cr;
	}

	private static void getAdhocDataView(ARestV2Connection rc, ClientAdhocDataView cr, ResourceDescriptor rd) throws ParseException {
		List<ResourceDescriptor> children = rd.getChildren();
		for (ResourceDescriptor r : children)
			if (SelectorDatasource.isDatasource(r))
				cr.setDataSource((ClientReferenceableDataSource) getResourceContainer(rc, r));
	}

	private static void getLOV(ARestV2Connection rc, ClientListOfValues cr, ResourceDescriptor rd) {
		List<ClientListOfValuesItem> lovs = new ArrayList<ClientListOfValuesItem>();
		if (rd.getListOfValues() != null)
			for (ListItem l : (List<ListItem>) rd.getListOfValues())
				lovs.add(new ClientListOfValuesItem(l.getLabel(), (String) l.getValue()));
		cr.setItems(lovs);
	}

	private static void getMondrianConnection(ARestV2Connection rc, ClientMondrianConnection cr, ResourceDescriptor rd) throws ParseException {
		for (ResourceDescriptor r : (List<ResourceDescriptor>) rd.getChildren()) {
			if (SelectorDatasource.isDatasource(r))
				cr.setDataSource((ClientReferenceableDataSource) getResourceContainer(rc, r));
			else if (r.getWsType().equals(ResourceDescriptor.TYPE_MONDRIAN_SCHEMA))
				cr.setSchema((ClientReferenceableFile) getResourceContainer(rc, r));
		}
	}

	private static void getSecureMondrianConnection(ARestV2Connection rc, ClientSecureMondrianConnection cr, ResourceDescriptor rd) throws ParseException {
		for (ResourceDescriptor r : (List<ResourceDescriptor>) rd.getChildren()) {
			if (SelectorDatasource.isDatasource(r))
				cr.setDataSource((ClientReferenceableDataSource) getResourceContainer(rc, r));
			else if (r.getWsType().equals(ResourceDescriptor.TYPE_MONDRIAN_SCHEMA))
				cr.setSchema((ClientReferenceableFile) getResourceContainer(rc, r));
			else if (r.getWsType().equals(ResourceDescriptor.TYPE_ACCESS_GRANT_SCHEMA)) {
				if (cr.getAccessGrants() == null)
					cr.setAccessGrants(new ArrayList<ClientReferenceableFile>());
				cr.getAccessGrants().add((ClientReferenceableFile) getResourceContainer(rc, r));
			}
		}
	}

	private static void getMondrianXmlaDefinition(ARestV2Connection rc, ClientMondrianXmlaDefinition cr, ResourceDescriptor rd) throws ParseException {
		ResourceProperty rp = rd.getResourceProperty(ResourceDescriptor.PROP_XMLA_CATALOG);
		if (rp != null)
			cr.setCatalog(rp.getValue());
		for (ResourceDescriptor r : (List<ResourceDescriptor>) rd.getChildren()) {
			if (r.getWsType().equals(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION) || r.getWsType().equals(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION))
				cr.setMondrianConnection((ClientReferenceableMondrianConnection) getResourceContainer(rc, r));
		}
	}

	private static void getXmlaConnection(ARestV2Connection rc, ClientXmlaConnection cr, ResourceDescriptor rd) {
		cr.setUrl(rd.getResourcePropertyValue(ResourceDescriptor.PROP_XMLA_URI));
		cr.setDataSource(rd.getResourcePropertyValue(ResourceDescriptor.PROP_XMLA_DATASOURCE));
		cr.setCatalog(rd.getResourcePropertyValue(ResourceDescriptor.PROP_XMLA_CATALOG));
		cr.setUsername(rd.getResourcePropertyValue(ResourceDescriptor.PROP_XMLA_USERNAME));
		cr.setPassword(Misc.nullValue(rd.getResourcePropertyValue(ResourceDescriptor.PROP_XMLA_PASSWORD)));
	}

	private static void getOlapUnit(ARestV2Connection rc, ClientOlapUnit cr, ResourceDescriptor rd) throws ParseException {
		cr.setMdxQuery(rd.getSql());
		for (ResourceDescriptor r : (List<ResourceDescriptor>) rd.getChildren()) {
			if (r.getWsType().equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION) || r.getWsType().equals(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION)
					|| r.getWsType().equals(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION))
				cr.setOlapConnection((ClientReferenciableOlapConnection) getResourceContainer(rc, r));
		}
	}

	private static void getVirtualDataSource(ARestV2Connection rc, ClientVirtualDataSource cr, ResourceDescriptor rd) {
		List<ClientSubDataSourceReference> ds = new ArrayList<ClientSubDataSourceReference>();
		for (ResourceDescriptor r : (List<ResourceDescriptor>) rd.getChildren()) {
			ClientSubDataSourceReference d = new ClientSubDataSourceReference();
			d.setId(r.getResourcePropertyValue("PROP_DATASOURCE_SUB_DS_ID"));
			d.setUri(r.getReferenceUri());
			ds.add(d);
		}
		cr.setSubDataSources(ds);
	}

	private static void getAWSDataSource(ARestV2Connection rc, ClientAwsDataSource cr, ResourceDescriptor rd) {
		cr.setAccessKey(DiffFields.getSoapValue(rd, DiffFields.ACCESSKEY));
		cr.setSecretKey(DiffFields.getSoapValue(rd, DiffFields.SECRETKEY));
		cr.setRoleArn(DiffFields.getSoapValue(rd, DiffFields.ROLEARN));
		cr.setRegion(DiffFields.getSoapValue(rd, DiffFields.REGION));
		cr.setDbName(DiffFields.getSoapValue(rd, DiffFields.DBNAME));
		cr.setDbInstanceIdentifier(DiffFields.getSoapValue(rd, DiffFields.DBINSTANCEIDENTIFIER));
		cr.setDbService(DiffFields.getSoapValue(rd, DiffFields.DBSERVICE));
		cr.setTimezone(DiffFields.getSoapValue(rd, DiffFields.TIMEZONE));
	}

	private static void getCustomDataSource(ARestV2Connection rc, ClientCustomDataSource cr, ResourceDescriptor rd) {
		cr.setServiceClass(rd.getServiceClass());
		cr.setDataSourceName(DiffFields.getSoapValue(rd, DiffFields.DATASOURCENAME));
		Map<String, String> map = rd.getPropertyMap();
		List<ClientProperty> props = new ArrayList<ClientProperty>();
		for (String key : map.keySet()) {
			if (key.equals("password") && Misc.isNullOrEmpty(map.get(key)))
				continue;
			props.add(new ClientProperty(key, map.get(key)));
		}
		cr.setProperties(props);
	}

	private static void getJndiDataSource(ARestV2Connection rc, ClientJndiJdbcDataSource cr, ResourceDescriptor rd) {
		cr.setJndiName(rd.getJndiName());
		cr.setTimezone(DiffFields.getSoapValue(rd, DiffFields.TIMEZONE));
	}

	private static void getBeanDataSource(ARestV2Connection rc, ClientBeanDataSource cr, ResourceDescriptor rd) {
		cr.setBeanName(rd.getBeanName());
		cr.setBeanMethod(rd.getBeanMethod());
	}

	private static void getJdbcDataSource(ARestV2Connection rc, ClientJdbcDataSource cr, ResourceDescriptor rd) {
		cr.setDriverClass(rd.getDriverClass());
		cr.setPassword(Misc.nullValue(rd.getPassword()));
		cr.setUsername(rd.getUsername());
		cr.setConnectionUrl(rd.getConnectionUrl());
		cr.setTimezone(DiffFields.getSoapValue(rd, DiffFields.TIMEZONE));
	}

	private static void getDataType(ARestV2Connection rc, ClientDataType cr, ResourceDescriptor rd) {
		switch (rd.getDataType()) {
		case 1:
			cr.setType(TypeOfDataType.text);
			break;
		case 2:
			cr.setType(TypeOfDataType.number);
			break;
		case 3:
			cr.setType(TypeOfDataType.date);
			break;
		case 4:
			cr.setType(TypeOfDataType.datetime);
			break;
		case 5:
			cr.setType(TypeOfDataType.time);
			break;
		}
		cr.setPattern(rd.getPattern());
		cr.setMaxValue(rd.getMaxValue());
		cr.setStrictMax(rd.isStrictMax());
		cr.setMinValue(rd.getMinValue());
		cr.setStrictMin(rd.isStrictMin());

		Integer ml = DiffFields.getSoapValueInteger(rd, DiffFields.MAXLENGHT);
		if (ml != null && ml.intValue() > 0)
			cr.setMaxLength(ml);
	}

	private static void getQuery(ARestV2Connection rc, ClientQuery cr, ResourceDescriptor rd) throws ParseException {
		cr.setValue(rd.getSql());
		cr.setLanguage(rd.getResourcePropertyValue(ResourceDescriptor.PROP_QUERY_LANGUAGE));
		List<ResourceDescriptor> children = rd.getChildren();
		for (ResourceDescriptor r : children)
			if (SelectorDatasource.isDatasource(r))
				cr.setDataSource((ClientReferenceableDataSource) getResourceContainer(rc, r));
	}

	private static void getFile(ARestV2Connection rc, ClientFile cr, ResourceDescriptor rd) {
		cr.setType(WsTypes.INST().toRestFileType(rd.getWsType()));
		if (rd.getData() != null) {
			String content = new String(rd.getData());// new
																								// String(Base64.decodeBase64(content))
			if (content.isEmpty())
				content = "    "; // if empty, jrs throw an exception
			cr.setContent(content);
		}
	}

	private static void getInputControl(ARestV2Connection rc, ClientInputControl cr, ResourceDescriptor rd) throws ParseException {
		cr.setMandatory(rd.isMandatory());
		cr.setReadOnly(rd.isReadOnly());
		cr.setVisible(rd.isVisible());

		cr.setType(rd.getControlType());
		cr.setValueColumn(rd.getQueryValueColumn());
		if (rd.getQueryVisibleColumns() != null)
			cr.setVisibleColumns(Arrays.asList(rd.getQueryVisibleColumns()));
		List<ResourceDescriptor> children = rd.getChildren();
		for (ResourceDescriptor r : children) {
			if (r.getWsType().equals(ResourceDescriptor.TYPE_LOV))
				cr.setListOfValues((ClientReferenceableListOfValues) getResourceContainer(rc, r));
			else if (r.getWsType().equals(ResourceDescriptor.TYPE_QUERY))
				cr.setQuery((ClientReferenceableQuery) getResourceContainer(rc, r));
			else if (r.getWsType().equals(ResourceDescriptor.TYPE_DATA_TYPE))
				cr.setDataType((ClientReferenceableDataType) getResourceContainer(rc, r));
		}
	}

	public static void getReportUnit(ARestV2Connection rc, AbstractClientReportUnit<?> cr, ResourceDescriptor rd) throws ParseException {
		cr.setAlwaysPromptControls(Misc.nvl(rd.getResourcePropertyValueAsBoolean(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS), Boolean.FALSE));
		cr.setInputControlRenderingView(rd.getResourcePropertyValue(ResourceDescriptor.PROP_RU_INPUTCONTROL_RENDERING_VIEW));
		cr.setReportRenderingView(rd.getResourcePropertyValue(ResourceDescriptor.PROP_RU_REPORT_RENDERING_VIEW));
		int rurv = Misc.nvl(rd.getResourcePropertyValueAsInteger(ResourceDescriptor.PROP_RU_CONTROLS_LAYOUT), (int) ResourceDescriptor.RU_CONTROLS_LAYOUT_POPUP_SCREEN);
		switch (rurv) {
		case (int) ResourceDescriptor.RU_CONTROLS_LAYOUT_POPUP_SCREEN:
			cr.setControlsLayout(ControlsLayoutType.popupScreen);
			break;
		case (int) ResourceDescriptor.RU_CONTROLS_LAYOUT_SEPARATE_PAGE:
			cr.setControlsLayout(ControlsLayoutType.separatePage);
			break;
		case (int) ResourceDescriptor.RU_CONTROLS_LAYOUT_TOP_OF_PAGE:
			cr.setControlsLayout(ControlsLayoutType.topOfPage);
			break;
		case 4:
			cr.setControlsLayout(ControlsLayoutType.inPage);
			break;
		}
		List<ResourceDescriptor> children = rd.getChildren();
		List<ClientReferenceableInputControl> ics = cr.getInputControls();
		if (ics == null) {
			ics = new ArrayList<ClientReferenceableInputControl>();
			cr.setInputControls(ics);
		}
		Map<String, ClientReferenceableFile> icf = cr.getFiles();
		if (icf == null) {
			icf = new HashMap<String, ClientReferenceableFile>();
			cr.setFiles(icf);
		}
		for (ResourceDescriptor r : children) {
			if (r == null)
				continue;
			if (SelectorDatasource.isDatasource(r))
				cr.setDataSource((ClientReferenceableDataSource) getResourceContainer(rc, r));
			else {
				String t = r.getWsType();
				if (t.equals(ResourceDescriptor.TYPE_QUERY))
					cr.setQuery((ClientReferenceableQuery) getResourceContainer(rc, r));
				else if ((t.equals(ResourceDescriptor.TYPE_JRXML) || t.equals(ResourceDescriptor.TYPE_REFERENCE)) && r.isMainReport()) {
					// r.setName("main_jrxml");
					// r.setLabel("Main Jrxml");
					// r.setUriString(rd.getUriString() + "_files/" + r.getName());
					cr.setJrxml((ClientReferenceableFile) getResourceContainer(rc, r));
				} else if (t.equals(ResourceDescriptor.TYPE_INPUT_CONTROL))
					ics.add((ClientReferenceableInputControl) getResourceContainer(rc, r));
				else if (ResourceFactory.isFileResourceType(r) || t.equals(ResourceDescriptor.TYPE_REFERENCE)) {
					icf.put(r.getName(), (ClientReferenceableFile) getResourceContainer(rc, r));
				}
			}
		}

	}
}
