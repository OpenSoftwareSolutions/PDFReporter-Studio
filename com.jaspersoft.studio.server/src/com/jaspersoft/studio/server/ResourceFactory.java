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
package com.jaspersoft.studio.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.server.model.MAdHocDataView;
import com.jaspersoft.studio.server.model.MContentResource;
import com.jaspersoft.studio.server.model.MDataType;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MInputControl;
import com.jaspersoft.studio.server.model.MJar;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MListOfValues;
import com.jaspersoft.studio.server.model.MRAccessGrantSchema;
import com.jaspersoft.studio.server.model.MRCSS;
import com.jaspersoft.studio.server.model.MRDashboard;
import com.jaspersoft.studio.server.model.MRDataAdapter;
import com.jaspersoft.studio.server.model.MRFont;
import com.jaspersoft.studio.server.model.MRImage;
import com.jaspersoft.studio.server.model.MRQuery;
import com.jaspersoft.studio.server.model.MRStyleTemplate;
import com.jaspersoft.studio.server.model.MReference;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MReportUnitOptions;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.MResourceBundle;
import com.jaspersoft.studio.server.model.MUnknown;
import com.jaspersoft.studio.server.model.MXmlFile;
import com.jaspersoft.studio.server.model.datasource.MRDatasource;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceAWS;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceBean;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceCustom;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceDiagnostic;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceJDBC;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceJNDI;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceVDS;
import com.jaspersoft.studio.server.model.datasource.MRMondrianSchema;
import com.jaspersoft.studio.server.model.datasource.MRMondrianXmlaDefinitionClientType;
import com.jaspersoft.studio.server.model.datasource.MROlapMondrianConnection;
import com.jaspersoft.studio.server.model.datasource.MROlapUnit;
import com.jaspersoft.studio.server.model.datasource.MROlapXmlaConnection;
import com.jaspersoft.studio.server.model.datasource.MRSecureMondrianConnection;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.plugin.ExtensionManager;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.restv2.WsTypes;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.server.wizard.resource.page.CSSPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.DataTypePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.FilePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.FontPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.ImagePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.InputControlPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.JarPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.JrxmlPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.LovPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.QueryPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.ReferencePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.ResourceBundlePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.ResourcePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.StyleTemplatePageContent;
import com.jaspersoft.studio.server.wizard.resource.page.XmlPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DataAdapterPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DatasourceAWSPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DatasourceBeanPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DatasourceCustomPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DatasourceJDBCPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DatasourceJndiPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.datasource.DatasourceVDSPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.olap.MondrianXMLADefinitionContent;
import com.jaspersoft.studio.server.wizard.resource.page.olap.OLAPMondrianSchemaContent;
import com.jaspersoft.studio.server.wizard.resource.page.olap.OLAPXmlaPageContent;
import com.jaspersoft.studio.server.wizard.resource.page.olap.OlapConnectionContent;
import com.jaspersoft.studio.server.wizard.resource.page.runit.ReportUnitContent;
import com.jaspersoft.studio.server.wizard.resource.page.runit.ReportUnitDatasourceContent;
import com.jaspersoft.studio.server.wizard.resource.page.runit.ReportUnitInputControlContent;
import com.jaspersoft.studio.server.wizard.resource.page.runit.ReportUnitOptionsContent;
import com.jaspersoft.studio.server.wizard.resource.page.runit.ReportUnitQueryContent;
import com.jaspersoft.studio.utils.Misc;

public class ResourceFactory {

	private Map<Class<? extends MResource>, IWizardPage[]> pagemap = new HashMap<Class<? extends MResource>, IWizardPage[]>();

	public IWizardPage[] getResourcePage(ANode parent, MResource resource) {
		if (resource.getWsClient() == null) {
			if (parent instanceof MResource)
				resource.setMRoot((ANode) parent.getRoot());
			else if (parent instanceof MServerProfile)
				resource.setMRoot(parent);
		}
		IWizardPage[] page = pagemap.get(resource.getClass());
		if (page == null) {
			page = Activator.getExtManager().getResourcePage(parent, resource);
			if (page == null) {
				if (resource instanceof MRImage)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ImagePageContent(parent, resource));
				else if (resource instanceof MRCSS)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new CSSPageContent(parent, resource));

				else if (resource instanceof MRFont)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new FontPageContent(parent, resource));
				else if (resource instanceof MJar)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new JarPageContent(parent, resource));
				else if (resource instanceof MResourceBundle)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ResourceBundlePageContent(parent, resource));
				else if (resource instanceof MJrxml)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new JrxmlPageContent(parent, resource));
				else if (resource instanceof MReference)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReferencePageContent(parent, resource));
				else if (resource instanceof MRDatasourceVDS)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DatasourceVDSPageContent(parent, resource));
				else if (resource instanceof MRDatasourceJNDI)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DatasourceJndiPageContent(parent, resource));
				else if (resource instanceof MRDatasourceAWS)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DatasourceAWSPageContent(parent, resource));
				else if (resource instanceof MRDatasourceJDBC)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DatasourceJDBCPageContent(parent, resource));
				else if (resource instanceof MRDatasourceBean)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DatasourceBeanPageContent(parent, resource));
				else if (resource instanceof MRDatasourceCustom)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DatasourceCustomPageContent(parent, resource));
				else if (resource instanceof MRDatasource || resource instanceof MFolder)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource));
				else if (resource instanceof MReportUnit)
					if (ReportUnitQueryContent.hasTypeQuery(resource))
						page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReportUnitContent(parent, resource), new ReportUnitDatasourceContent(parent, resource),
								new ReportUnitQueryContent(parent, resource), new ReportUnitInputControlContent(parent, resource));
					else
						page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReportUnitContent(parent, resource), new ReportUnitDatasourceContent(parent, resource),
								new ReportUnitInputControlContent(parent, resource));
				else if (resource instanceof MInputControl)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new InputControlPageContent(parent, resource));
				else if (resource instanceof MDataType)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DataTypePageContent(parent, resource));
				else if (resource instanceof MRQuery)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new QueryPageContent(parent, resource), new ReportUnitDatasourceContent(parent, resource));
				else if (resource instanceof MListOfValues)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new LovPageContent(parent, resource));
				else if (resource instanceof MReportUnitOptions)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReportUnitOptionsContent(parent, resource));
				else if (resource instanceof MXmlFile)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new XmlPageContent(parent, resource));
				else if (resource instanceof MUnknown)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource));
				else if (resource instanceof MContentResource)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new FilePageContent(parent, resource));
				else if (resource instanceof MRStyleTemplate)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new StyleTemplatePageContent(parent, resource));
				else if (resource instanceof MRDataAdapter)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new DataAdapterPageContent(parent, resource));

				else if (resource instanceof MRDashboard)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource));
				else if (resource instanceof MAdHocDataView)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReportUnitDatasourceContent(parent, resource));
				else if (resource instanceof MROlapMondrianConnection)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReportUnitDatasourceContent(parent, resource, true), new OLAPMondrianSchemaContent(parent, resource));
				else if (resource instanceof MRSecureMondrianConnection)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new ReportUnitDatasourceContent(parent, resource, true), new OLAPMondrianSchemaContent(parent, resource));
				else if (resource instanceof MRMondrianSchema)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new XmlPageContent(parent, resource));
				else if (resource instanceof MRMondrianXmlaDefinitionClientType)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new MondrianXMLADefinitionContent(parent, resource));
				else if (resource instanceof MRAccessGrantSchema)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new XmlPageContent(parent, resource));

				else if (resource instanceof MROlapUnit)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new QueryPageContent(parent, resource, false), new OlapConnectionContent(parent, resource));
				else if (resource instanceof MROlapXmlaConnection)
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource), new OLAPXmlaPageContent(parent, resource));
				else
					page = APageContent.getPages(resource, new ResourcePageContent(parent, resource));
			}
			if (page != null)
				pagemap.put(resource.getClass(), page);
		}
		return page;
	}

	public static MResource getResource(ANode parent, ResourceDescriptor resource, int index) {
		ExtensionManager extManager = Activator.getExtManager();
		MResource m = extManager.getResource(parent, resource, index);
		if (m != null)
			return m;
		String wstype = resource.getWsType();
		if (wstype.equals(ResourceDescriptor.TYPE_FOLDER)) {
			MFolder folder = new MFolder(parent, resource, index);
			new MDummy(folder);
			return folder;
		}
		if (wstype.equals(ResourceDescriptor.TYPE_INPUT_CONTROL))
			return new MInputControl(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_JRXML))
			return new MJrxml(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_IMAGE))
			return new MRImage(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_REFERENCE))
			return new MReference(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_REPORTUNIT)) {
			MReportUnit runit = new MReportUnit(parent, resource, index);
			new MDummy(runit);
			return runit;
		}

		if (wstype.equals(ResourceDescriptor.TYPE_LOV))
			return new MListOfValues(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_UNKNOW))
			return new MUnknown(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_CLASS_JAR))
			return new MJar(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_RESOURCE_BUNDLE))
			return new MResourceBundle(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_QUERY))
			return new MRQuery(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_DATA_TYPE))
			return new MDataType(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_FONT))
			return new MRFont(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_STYLE_TEMPLATE))
			return new MRStyleTemplate(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_CONTENT_RESOURCE))
			return new MContentResource(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_DATASOURCE))
			return new MRDatasource(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_DATASOURCE_BEAN))
			return new MRDatasourceBean(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM)) {
			ResourceProperty rp = ResourceDescriptorUtil.getProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS, resource.getProperties());
			if (rp != null) {
				if (rp.getValue().equals(MRDatasourceDiagnostic.CUSTOM_CLASS))
					return new MRDatasourceDiagnostic(parent, resource, index);
			}
			return new MRDatasourceCustom(parent, resource, index);
		}

		if (wstype.equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC))
			return new MRDatasourceJDBC(parent, resource, index);
		if (wstype.equals(MRDatasourceVDS.TYPE_DATASOURCE_VDS))
			return new MRDatasourceVDS(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_DATASOURCE_JNDI))
			return new MRDatasourceJNDI(parent, resource, index);
		if (wstype.equals(MRDatasourceAWS.TYPE_AWS))
			return new MRDatasourceAWS(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_REPORT_OPTIONS) || wstype.equals("ReportOptionsResource")) {
			MReportUnitOptions mro = new MReportUnitOptions(parent, resource, index);
			if (mro.isSupported(Feature.INPUTCONTROLS_ORDERING))
				new MDummy(mro);
			return mro;
		}

		if (wstype.equals(ResourceDescriptor.TYPE_XML_FILE))
			return new MXmlFile(parent, resource, index);

		if (wstype.equals(ResourceDescriptor.TYPE_DASHBOARD)) {
			MRDashboard mrd = new MRDashboard(parent, resource, index);
			if (mrd.isSupported(Feature.INPUTCONTROLS_ORDERING))
				new MDummy(mrd);
			return mrd;
		}
		if (wstype.equals(ResourceDescriptor.TYPE_ADHOC_DATA_VIEW)) {
			MAdHocDataView madv = new MAdHocDataView(parent, resource, index);
			if (madv.isSupported(Feature.INPUTCONTROLS_ORDERING))
				new MDummy(madv);
			return madv;
		}
		if (wstype.equals(ResourceDescriptor.TYPE_MONDRIAN_SCHEMA))
			return new MRMondrianSchema(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_MONDRIAN_XMLA_DEFINITION_CLIENT_TYPE))
			return new MRMondrianXmlaDefinitionClientType(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION))
			return new MROlapMondrianConnection(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION))
			return new MRSecureMondrianConnection(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION))
			return new MROlapXmlaConnection(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_OLAPUNIT))
			return new MROlapUnit(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_ACCESS_GRANT_SCHEMA))
			return new MRAccessGrantSchema(parent, resource, index);
		if (wstype.equals(ResourceDescriptor.TYPE_CSS_FILE))
			return new MRCSS(parent, resource, index);

		return new MUnknown(parent, resource, index);
	}

	private static Set<String> fileTypes = new HashSet<String>();
	static {
		fileTypes.add(ResourceDescriptor.TYPE_IMAGE);
		fileTypes.add(ResourceDescriptor.TYPE_FONT);
		fileTypes.add(ResourceDescriptor.TYPE_JRXML);
		fileTypes.add(ResourceDescriptor.TYPE_CLASS_JAR);
		fileTypes.add(ResourceDescriptor.TYPE_RESOURCE_BUNDLE);
		fileTypes.add(ResourceDescriptor.TYPE_STYLE_TEMPLATE);
		fileTypes.add(ResourceDescriptor.TYPE_CONTENT_RESOURCE);
		fileTypes.add(ResourceDescriptor.TYPE_XML_FILE);
		fileTypes.add(ResourceDescriptor.TYPE_CSS_FILE);
	}

	public static boolean isFileResourceType(ResourceDescriptor r) {
		return fileTypes.contains(r.getWsType());
	}

	private static final String[] REST_FILETYPES = new String[] { ResourceMediaType.FILE_CLIENT_TYPE };

	public static String[] getFileTypes() {
		return REST_FILETYPES;
	}

	private static Map<String, ImageDescriptor> tIcons = new HashMap<String, ImageDescriptor>();
	private static Map<String, String> tName = new HashMap<String, String>();

	public static Image getIcon(String rtype) {
		ImageDescriptor id = tIcons.get(rtype);
		if (id == null) {
			initType(rtype);
			id = tIcons.get(rtype);
		}
		return Activator.getDefault().getImage(id);
	}

	protected static void initType(String rtype) {
		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setWsType(WsTypes.INST().toSoapType(rtype));
		MResource r = getResource(null, rd, -1);
		tIcons.put(rtype, r.getThisIconDescriptor().getIcon16());
		tName.put(rtype, r.getThisIconDescriptor().getTitle());
		System.out.println(rtype + ":" + r.getThisIconDescriptor().getTitle());
	}

	public static String getName(String rtype) {
		String id = tName.get(rtype);
		if (id == null) {
			initType(rtype);
			id = tName.get(rtype);
		}
		return id;
	}

	private static Map<String, String> typeNames;

	public static Map<String, String> getTypeNames() {
		if (typeNames == null) {
			typeNames = new HashMap<String, String>();
			for (String rtype : WsTypes.INST().getRestTypes())
				typeNames.put(rtype, getName(rtype));
			typeNames = Misc.sortByValues(typeNames);
		}
		return typeNames;
	}
}
