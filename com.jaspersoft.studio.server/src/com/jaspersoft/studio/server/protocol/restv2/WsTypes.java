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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.ClientUnknown;
import com.jaspersoft.jasperserver.dto.resources.ClientAdhocDataView;
import com.jaspersoft.jasperserver.dto.resources.ClientAwsDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientBeanDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientCustomDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientDataType;
import com.jaspersoft.jasperserver.dto.resources.ClientFile;
import com.jaspersoft.jasperserver.dto.resources.ClientFile.FileType;
import com.jaspersoft.jasperserver.dto.resources.ClientFolder;
import com.jaspersoft.jasperserver.dto.resources.ClientInputControl;
import com.jaspersoft.jasperserver.dto.resources.ClientJdbcDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientJndiJdbcDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientListOfValues;
import com.jaspersoft.jasperserver.dto.resources.ClientMondrianConnection;
import com.jaspersoft.jasperserver.dto.resources.ClientMondrianXmlaDefinition;
import com.jaspersoft.jasperserver.dto.resources.ClientOlapUnit;
import com.jaspersoft.jasperserver.dto.resources.ClientQuery;
import com.jaspersoft.jasperserver.dto.resources.ClientReportUnit;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceLookup;
import com.jaspersoft.jasperserver.dto.resources.ClientSecureMondrianConnection;
import com.jaspersoft.jasperserver.dto.resources.ClientSemanticLayerDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientVirtualDataSource;
import com.jaspersoft.jasperserver.dto.resources.ClientXmlaConnection;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.model.datasource.filter.DatasourcesAllFilter;
import com.jaspersoft.studio.utils.Misc;

public class WsTypes {
	private WsTypes() {
		setType(ClientFolder.class);
		setType(ClientReportUnit.class);
		setType(ClientBeanDataSource.class);
		setType(ClientCustomDataSource.class);
		setType(ClientDataType.class);
		setType(ClientFile.class);
		setType(ClientInputControl.class);
		setType(ClientJndiJdbcDataSource.class);
		setType(ClientListOfValues.class);
		setType(ClientMondrianXmlaDefinition.class);
		setType(ClientOlapUnit.class);
		setType(ClientVirtualDataSource.class);
		setType(ClientXmlaConnection.class);
		setType(ClientAwsDataSource.class);
		setType(ClientJdbcDataSource.class);
		setType(ClientQuery.class);
		setType(ClientSemanticLayerDataSource.class);
		setType(ClientAdhocDataView.class);
		setType(ClientMondrianConnection.class);
		setType(ClientSecureMondrianConnection.class);
		setType(ClientUnknown.class);

		setRestType(ResourceMediaType.ADHOC_DATA_VIEW_CLIENT_TYPE, ResourceDescriptor.TYPE_ADHOC_DATA_VIEW);
		setRestType(ResourceMediaType.AWS_DATA_SOURCE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATASOURCE_AWS);
		setRestType(ResourceMediaType.BEAN_DATA_SOURCE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATASOURCE_BEAN);
		setRestType(ResourceMediaType.CUSTOM_DATA_SOURCE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATASOURCE_CUSTOM);
		setRestType(ResourceMediaType.DATA_TYPE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATA_TYPE);

		setRestType(ResourceMediaType.FILE_CLIENT_TYPE, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestType(ResourceMediaType.FOLDER_CLIENT_TYPE, ResourceDescriptor.TYPE_FOLDER);
		setRestType(ResourceMediaType.INPUT_CONTROL_CLIENT_TYPE, ResourceDescriptor.TYPE_INPUT_CONTROL);
		setRestType(ResourceMediaType.JDBC_DATA_SOURCE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATASOURCE_JDBC);
		setRestType(ResourceMediaType.JNDI_JDBC_DATA_SOURCE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATASOURCE_JNDI);
		setRestType(ResourceMediaType.LIST_OF_VALUES_CLIENT_TYPE, ResourceDescriptor.TYPE_LOV);
		setRestType(ResourceMediaType.MONDRIAN_CONNECTION_CLIENT_TYPE, ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION);
		setRestType(ResourceMediaType.MONDRIAN_XMLA_DEFINITION_CLIENT_TYPE, ResourceDescriptor.TYPE_MONDRIAN_XMLA_DEFINITION_CLIENT_TYPE);
		setRestType(ResourceMediaType.OLAP_UNIT_CLIENT_TYPE, ResourceDescriptor.TYPE_OLAPUNIT);
		setRestType(ResourceMediaType.QUERY_CLIENT_TYPE, ResourceDescriptor.TYPE_QUERY);
		setRestType(ResourceMediaType.REPORT_UNIT_CLIENT_TYPE, ResourceDescriptor.TYPE_REPORTUNIT);
		setRestType(ResourceMediaType.SECURE_MONDRIAN_CONNECTION_CLIENT_TYPE, ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION);
		setRestType(ResourceMediaType.VIRTUAL_DATA_SOURCE_CLIENT_TYPE, ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL);
		setRestType(ResourceMediaType.XMLA_CONNECTION_CLIENT_TYPE, ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION);

		setSoapType(ResourceDescriptor.TYPE_ACCESS_GRANT_SCHEMA, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_ADHOC_DATA_VIEW, ResourceMediaType.ADHOC_DATA_VIEW_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_ADHOC_REPORT, null); // should be?
		setSoapType(ResourceDescriptor.TYPE_CLASS_JAR, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_CONTENT_RESOURCE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DASHBOARD_STATE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATA_TYPE, ResourceMediaType.DATA_TYPE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE, null);// should be?
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE_AWS, ResourceMediaType.AWS_DATA_SOURCE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE_BEAN, ResourceMediaType.BEAN_DATA_SOURCE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM, ResourceMediaType.CUSTOM_DATA_SOURCE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE_JDBC, ResourceMediaType.JDBC_DATA_SOURCE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE_JNDI, ResourceMediaType.JNDI_JDBC_DATA_SOURCE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_DATASOURCE_VIRTUAL, ResourceMediaType.VIRTUAL_DATA_SOURCE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_FOLDER, ResourceMediaType.FOLDER_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_FONT, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_IMAGE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_INPUT_CONTROL, ResourceMediaType.INPUT_CONTROL_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_JRXML, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_LOV, ResourceMediaType.LIST_OF_VALUES_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_MONDRIAN_SCHEMA, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_MONDRIAN_XMLA_DEFINITION_CLIENT_TYPE, ResourceMediaType.MONDRIAN_XMLA_DEFINITION_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION, ResourceMediaType.MONDRIAN_CONNECTION_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION, ResourceMediaType.XMLA_CONNECTION_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_OLAPUNIT, ResourceMediaType.OLAP_UNIT_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_QUERY, ResourceMediaType.QUERY_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_REFERENCE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_REPORTUNIT, ResourceMediaType.REPORT_UNIT_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_RESOURCE_BUNDLE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_STYLE_TEMPLATE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION, ResourceMediaType.SECURE_MONDRIAN_CONNECTION_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_UNKNOW, ResourceMediaType.RESOURCE_LOOKUP_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_XML_FILE, ResourceMediaType.FILE_CLIENT_TYPE);
		setSoapType(ResourceDescriptor.TYPE_CSS_FILE, ResourceMediaType.FILE_CLIENT_TYPE);

		setSoapFileType(ResourceDescriptor.TYPE_ACCESS_GRANT_SCHEMA, FileType.accessGrantSchema);
		setSoapFileType(ResourceDescriptor.TYPE_CLASS_JAR, FileType.jar);
		setSoapFileType(ResourceDescriptor.TYPE_CONTENT_RESOURCE, FileType.unspecified);
		setSoapFileType(ResourceDescriptor.TYPE_FONT, FileType.font);
		setSoapFileType(ResourceDescriptor.TYPE_IMAGE, FileType.img);
		setSoapFileType(ResourceDescriptor.TYPE_JRXML, FileType.jrxml);
		setSoapFileType(ResourceDescriptor.TYPE_MONDRIAN_SCHEMA, FileType.olapMondrianSchema);
		setSoapFileType(ResourceDescriptor.TYPE_RESOURCE_BUNDLE, FileType.prop);
		setSoapFileType(ResourceDescriptor.TYPE_STYLE_TEMPLATE, FileType.jrtx);
		setSoapFileType(ResourceDescriptor.TYPE_XML_FILE, FileType.xml);
		setSoapFileType(ResourceDescriptor.TYPE_CSS_FILE, FileType.css);

		setRestFileType(FileType.accessGrantSchema, ResourceDescriptor.TYPE_ACCESS_GRANT_SCHEMA);
		setRestFileType(FileType.css, ResourceDescriptor.TYPE_CSS_FILE);
		setRestFileType(FileType.csv, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.docx, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.font, ResourceDescriptor.TYPE_FONT);
		setRestFileType(FileType.html, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.img, ResourceDescriptor.TYPE_IMAGE);
		setRestFileType(FileType.jar, ResourceDescriptor.TYPE_CLASS_JAR);
		setRestFileType(FileType.jrtx, ResourceDescriptor.TYPE_STYLE_TEMPLATE);
		setRestFileType(FileType.jrxml, ResourceDescriptor.TYPE_JRXML);
		setRestFileType(FileType.ods, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.odt, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.olapMondrianSchema, ResourceDescriptor.TYPE_MONDRIAN_SCHEMA);
		setRestFileType(FileType.pdf, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.prop, ResourceDescriptor.TYPE_RESOURCE_BUNDLE);
		setRestFileType(FileType.rtf, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.txt, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.unspecified, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.xls, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.xlsx, ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		setRestFileType(FileType.xml, ResourceDescriptor.TYPE_XML_FILE);

		Activator.getExtManager().initWsTypes(this);
	}

	public static FileType getFileType(FileType ftype, String ext) {
		ext = ext.toLowerCase();
		switch (ftype) {
		case accessGrantSchema:
		case olapMondrianSchema:
			return ftype;
		default:
			try {
				return FileType.valueOf(ext);
			} catch (IllegalArgumentException e) {
				if (ext.equals("properties"))
					return FileType.prop;
				if (ext.equals("xhtml") || ext.equals("htm"))
					return FileType.html;
				if (ext.equals("ttf") || ext.equals("eot") || ext.equals("woff") || ext.equals("svg"))
					return FileType.font;
				if (ext.equals("png") || ext.equals("gif") || ext.equals("jpg") || ext.equals("jpeg") || ext.equals("bmp") || ext.equals("tiff"))
					return FileType.img;
				return FileType.unspecified;
			}
		}
	}

	private static WsTypes instance;

	public static WsTypes INST() {
		if (instance == null)
			instance = new WsTypes();
		return instance;
	}

	private static final Map<String, String> restMap = new HashMap<String, String>();
	private static final Map<FileType, String> restFileMap = new HashMap<FileType, String>();
	private static final Map<String, String> soapMap = new HashMap<String, String>();
	private static final Map<String, FileType> soapFileMap = new HashMap<String, FileType>();

	public void setRestType(String stype, String rtype) {
		restMap.put(stype, rtype);
	}

	public void setRestFileType(FileType rtype, String stype) {
		restFileMap.put(rtype, stype);
	}

	public String toSoapFileType(FileType ftype) {
		return restFileMap.get(ftype);
	}

	public String toSoapType(String rtype) {
		return Misc.nvl(restMap.get(rtype), ResourceDescriptor.TYPE_UNKNOW);
	}

	public String toSoapType(ClientResource<?> cr) {
		if (cr instanceof ClientResourceLookup)
			return toSoapType(((ClientResourceLookup) cr).getResourceType());
		if (cr instanceof ClientFile)
			return toSoapFileType(((ClientFile) cr).getType());
		return toSoapType(types.inverse().get(cr.getClass()));
	}

	public void setSoapType(String stype, String rtype) {
		soapMap.put(stype, rtype);
	}

	public void setSoapFileType(String stype, FileType rtype) {
		soapFileMap.put(stype, rtype);
	}

	public FileType toRestFileType(String stype) {
		return Misc.nvl(soapFileMap.get(stype), FileType.unspecified);
	}

	public Map<String, FileType> getSoapfileMap() {
		return soapFileMap;
	}

	public String toRestType(String stype) {
		return Misc.nvl(soapMap.get(stype), stype);
	}

	private static final BiMap<String, Class<? extends ClientResource<?>>> types = HashBiMap.create();

	public void setType(Class<? extends ClientResource<?>> type) {
		types.put(getType(type), type);
	}

	private static List<String> tlist;

	public List<String> getRestTypes() {
		if (tlist == null)
			tlist = new ArrayList<String>(types.keySet());
		return tlist;
	}

	public Class<? extends ClientResource<?>> getType(String type) {
		return types.get(type);
	}

	public String getRestType(ClientResource<?> clazz) {
		return types.inverse().get(clazz.getClass());
	}

	private static String getType(Class<? extends ClientResource<?>> clientObjectClass) {
		String clientResourceType = null;
		XmlRootElement xmlRootElement = clientObjectClass.getAnnotation(XmlRootElement.class);
		if (xmlRootElement != null && !"##default".equals(xmlRootElement.name()))
			clientResourceType = xmlRootElement.name();
		else {
			XmlType xmlType = clientObjectClass.getAnnotation(XmlType.class);
			if (xmlType != null && !"##default".equals(xmlType.name()))
				clientResourceType = xmlType.name();
		}
		if (clientResourceType == null) {
			String classSimpleName = clientObjectClass.getSimpleName();
			clientResourceType = classSimpleName.replaceFirst("^.", classSimpleName.substring(0, 1).toLowerCase());
		}
		return clientResourceType;
	}

	public ClientResource<?> createResource(ResourceDescriptor rd) {
		ClientResource<?> cr = null;
		Class<? extends ClientResource<?>> clazz = types.get(toRestType(rd.getWsType()));
		try {
			cr = clazz.newInstance();
			if (cr instanceof ClientFile)
				((ClientFile) cr).setType(toRestFileType(rd.getWsType()));
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		if (cr == null)
			cr = new ClientUnknown();
		return cr;
	}

	private static final Set<String> dsTypes = new HashSet<String>();

	public Set<String> getDatasources() {
		if (dsTypes.isEmpty())
			for (String s : DatasourcesAllFilter.getTypes())
				dsTypes.add(WsTypes.INST().toRestType(s));
		return dsTypes;
	}

	public String[] getDatasourcesArray() {
		return getDatasources().toArray(new String[dsTypes.size()]);
	}

	private static final Set<Class<? extends ClientResource<?>>> containers = new HashSet<Class<? extends ClientResource<?>>>();
	static {
		containers.add(ClientQuery.class);
		containers.add(ClientMondrianConnection.class);
		containers.add(ClientSecureMondrianConnection.class);
		containers.add(ClientMondrianXmlaDefinition.class);
		containers.add(ClientReportUnit.class);
		containers.add(ClientAdhocDataView.class);
		containers.add(ClientXmlaConnection.class);
		containers.add(ClientInputControl.class);
		containers.add(ClientOlapUnit.class);
		Activator.getExtManager().initContainers(containers);
	}

	public boolean isContainerType(Class<?> cr) {
		return containers.contains(cr);
	}

}
