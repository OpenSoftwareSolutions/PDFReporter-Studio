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
package com.jaspersoft.ireport.jasperserver.ws;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.util.SecretsUtil;

import org.apache.axis.EngineConfiguration;
import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.apache.axis.transport.http.HTTPConstants;
import org.eclipse.osgi.util.NLS;

import com.jaspersoft.ireport.jasperserver.ws.permissions.PermissionsManagement;
import com.jaspersoft.ireport.jasperserver.ws.permissions.PermissionsManagementServiceLocator;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.ReportScheduler;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.ReportSchedulerServiceLocator;
import com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagement;
import com.jaspersoft.ireport.jasperserver.ws.userandroles.UserAndRoleManagementServiceLocator;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.OperationResult;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Request;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.ws.xml.Marshaller;
import com.jaspersoft.jasperserver.ws.xml.Unmarshaller;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.secret.JRServerSecretsProvider;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * 
 * @author gtoffoli
 */
public class WSClient {

	public static final String AXIS_CONFIGURATION_RESOURCE = "/com/jaspersoft/ireport/jasperserver/ws/client-config.wsdd"; //$NON-NLS-1$

	private JServer server = null;

	private String webservicesUri = null; // "http://127.0.0.1:8080/axis2/services/repository-ws-1.0";

	private ManagementService managementService = null;
	private UserAndRoleManagement userAndRoleManagementService = null;
	private PermissionsManagement permissionsManagement = null;
	private ReportScheduler reportScheduler = null;

	private Unmarshaller unmarshaller = new Unmarshaller();
	private Marshaller marshaller = new Marshaller();

	private String cachedServerVersion;

	private SecretsUtil secretsUtil;

	public WSClient(JServer server) throws Exception {
		this.server = server;

		URL url;
		try {
			url = new URL(server.getUrl());
		} catch (MalformedURLException e1) {
			throw new Exception(e1);
		}

		setWebservicesUri(url.toString());
	}

	/**
	 * List all datasources. It returns a list of resourceDescriptors.
	 */
	public java.util.List<ResourceDescriptor> listDatasources() throws Exception {

		Request req = new Request();

		req.setOperationName(Request.OPERATION_LIST);
		req.setResourceDescriptor(null);
		req.setLocale(getServer().getLocale());

		req.getArguments().add(new Argument(Argument.LIST_DATASOURCES, Argument.VALUE_TRUE));

		StringWriter xmlStringWriter = new StringWriter();
		Marshaller.marshal(req, xmlStringWriter);

		return list(xmlStringWriter.toString());
	}

	/**
	 * It returns a list of resourceDescriptors.
	 */
	public java.util.List<ResourceDescriptor> list(ResourceDescriptor descriptor) throws Exception {
		Request req = new Request();

		req.setOperationName(Request.OPERATION_LIST);
		req.setResourceDescriptor(descriptor);
		req.setLocale(getServer().getLocale());

		StringWriter xmlStringWriter = new StringWriter();
		Marshaller.marshal(req, xmlStringWriter);

		return list(xmlStringWriter.toString());
	}

	/**
	 * It returns a list of resourceDescriptors.
	 */
	public String getVersion() throws Exception {
		if (cachedServerVersion != null) {
			return cachedServerVersion;
		}

		Request req = new Request();

		req.setOperationName(Request.OPERATION_LIST);
		req.setResourceDescriptor(null);
		req.setLocale(getServer().getLocale());

		try {

			ManagementService ms = getManagementService();
			String reqXml = marshaller.marshal(req);
			// System.out.println("Executing list for version.." + new
			// java.util.Date());
			// System.out.flush();
			String result = ms.list(reqXml);
			// System.out.println("Finished list for version.." + new
			// java.util.Date());
			// System.out.flush();

			// In order to avoid problem with the classloading, forse a
			// classloader for
			// the parting...
			OperationResult or = (OperationResult) unmarshal(result);

			if (or.getReturnCode() != 0)
				throw new Exception(composeErrorMessage(or));

			cachedServerVersion = or.getVersion();
			return cachedServerVersion;

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * It returns a list of resourceDescriptors.
	 */
	public java.util.List<ResourceDescriptor> list(String xmlRequest) throws Exception {
		try {

			String result = getManagementService().list(xmlRequest);

			OperationResult or = (OperationResult) unmarshal(result);

			if (or.getReturnCode() != 0)
				throw new Exception(composeErrorMessage(or));

			return or.getResourceDescriptors();

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public void delete(ResourceDescriptor descriptor) throws Exception {
		delete(descriptor, null);
	}

	/**
	 * Delete a resource and its contents Specify the reportUnitUri if you are
	 * deleting something inside this report unit.
	 * 
	 */
	public void delete(ResourceDescriptor descriptor, String reportUnitUri) throws Exception {

		try {
			Request req = new Request();
			req.setOperationName("delete"); //$NON-NLS-1$
			req.setResourceDescriptor(descriptor);
			req.setLocale(getServer().getLocale());

			if (reportUnitUri != null && reportUnitUri.length() > 0) {
				req.getArguments().add(new Argument(Argument.MODIFY_REPORTUNIT, reportUnitUri));
			}

			String result = getManagementService().delete(marshaller.marshal(req));

			OperationResult or = (OperationResult) unmarshal(result);

			if (or.getReturnCode() != 0)
				throw new Exception(composeErrorMessage(or));

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Export a resource using the "get" ws and save the resource in the file
	 * specified by the user... If the outputFile is null, the argument
	 * "NO_ATTACHMENT" is added to the request in order of avoid the attachment
	 * trasmission.
	 * 
	 */
	public ResourceDescriptor get(ResourceDescriptor descriptor, File outputFile) throws Exception {
		return get(descriptor, outputFile, null);
	}

	/**
	 * When the server return an error as result of an operation this function
	 * compose the message to display to the user
	 * 
	 * @param or
	 *          the operation result
	 * @return message to display to the user
	 */
	private String composeErrorMessage(OperationResult or) {
		if (or.getMessage() != null) {
			return NLS.bind(Messages.WSClient_errorWithMessage, new Object[] { or.getReturnCode(), or.getMessage() });
		} else {
			return NLS.bind(Messages.WSClient_errorWithoutMessage, or.getReturnCode());
		}
	}

	/**
	 * Export a resource using the "get" ws and save the resource in the file
	 * specified by the user... If the outputFile is null, the argument
	 * "NO_ATTACHMENT" is added to the request in order of avoid the attachment
	 * trasmission.
	 * 
	 */
	public ResourceDescriptor get(ResourceDescriptor descriptor, File outputFile, java.util.List<Argument> args) throws Exception {
		ResourceDescriptor rd = null;
		java.io.InputStream is = null;
		OutputStream os = null;
		try {
			Request req = new Request();

			req.setOperationName("get"); //$NON-NLS-1$
			req.setResourceDescriptor(descriptor);
			req.setLocale(getServer().getLocale());

			if (args != null) {
				for (int i = 0; i < args.size(); ++i) {
					Argument arg = (Argument) args.get(i);
					req.getArguments().add(arg);
				}
			}

			if (outputFile == null) {
				req.getArguments().add(new Argument(Argument.NO_RESOURCE_DATA_ATTACHMENT, null));
			}

			/*
			 * if (descriptor.getWsType().equals("css")) { for(int i=0;
			 * i<descriptor.getProperties().size();i++){ ResourceProperty prop =
			 * (ResourceProperty)descriptor.getProperties().get(i); if
			 * (prop.getName().equals("PROP_IS_REFERENCE") ||
			 * prop.getName().equals("PROP_HAS_DATA")){
			 * descriptor.getProperties().remove(i); i--; } }
			 * descriptor.setWsType("unknow"); }
			 */

			String result = getManagementService().get(marshaller.marshal(req));

			OperationResult or = (OperationResult) unmarshal(result);

			if (or.getReturnCode() != 0)
				throw new Exception(composeErrorMessage(or));

			Object[] resAtts = ((org.apache.axis.client.Stub) getManagementService()).getAttachments();
			if (resAtts != null && resAtts.length > 0 && outputFile != null) {
				is = ((org.apache.axis.attachments.AttachmentPart) resAtts[0]).getDataHandler().getInputStream();

				byte[] buffer = new byte[1024];
				os = new FileOutputStream(outputFile);
				int bCount = 0;
				while ((bCount = is.read(buffer)) > 0) {
					os.write(buffer, 0, bCount);
				}
			} else if (outputFile != null) {
				throw new Exception("Attachment not present!"); //$NON-NLS-1$
			}

			rd = (ResourceDescriptor) or.getResourceDescriptors().get(0);

		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		} finally {
			if (is != null)
				is.close();
			if (os != null)
				os.close();
		}
		return rd;
	}

	/*
	 * public JasperPrint runReport(ResourceDescriptor descriptor, java.util.Map
	 * parameters) throws Exception { List args = new ArrayList(1); args.add(new
	 * Argument(Argument.RUN_OUTPUT_FORMAT, Argument.RUN_OUTPUT_FORMAT_JRPRINT));
	 * Map attachments = runReport(descriptor, parameters, args);
	 * 
	 * FileContent content = null; if (attachments != null &&
	 * !attachments.isEmpty()) {
	 * 
	 * content = (FileContent)(attachments.values().toArray()[0]);
	 * //attachments.get("jasperPrint");
	 * 
	 * }
	 * 
	 * 
	 * if (content == null) { throw new Exception("No JasperPrint"); }
	 * 
	 * InputStream is = new ByteArrayInputStream(content.getData());
	 * 
	 * JasperPrint print = (JasperPrint) JRLoader.loadObject(is); return print; }
	 */

	/**
	 * This method run a report. The return is an OperationResult. If the result
	 * is succesfull, the message contains a set of strings (one for each row)
	 * with the list of files attached complete of the relative path. I.e.
	 * 
	 * main_report.html images/logo1.jpg images/chartxyz.jpg
	 * 
	 * Arguments:
	 * 
	 * 
	 * 
	 * The request must contains the descriptor of the report to execute (only the
	 * URI is used). Arguments can be attached to the descriptor as childs. Each
	 * argument is a ListItem, with the parameter name as Name and the object
	 * rapresenting the value as Value.
	 * 
	 * Operation result Codes: 0 - Success 1 - Generic error
	 * 
	 */
	public Map<String, FileContent> runReport(ResourceDescriptor descriptor, java.util.Map<String, Object> parameters, List<Argument> args) throws Exception {

		try {
			Request req = new Request();
			req.setOperationName("runReport"); //$NON-NLS-1$
			req.setLocale(getServer().getLocale());
			ResourceDescriptor newRUDescriptor = new ResourceDescriptor();
			newRUDescriptor.setUriString(descriptor.getUriString());
			for (Iterator<String> i = parameters.keySet().iterator(); i.hasNext();) {
				String key = "" + i.next(); //$NON-NLS-1$
				Object value = parameters.get(key);
				if (value instanceof java.util.Collection) {
					Iterator<?> cIter = ((Collection<?>) value).iterator();
					while (cIter.hasNext()) {
						String item = "" + cIter.next(); //$NON-NLS-1$
						ListItem l = new ListItem(key + "", item); //$NON-NLS-1$
						l.setIsListItem(true);
						newRUDescriptor.getParameters().add(l);
					}
				} else {
					newRUDescriptor.getParameters().add(new ListItem(key + "", parameters.get(key))); //$NON-NLS-1$
				}
			}

			req.setResourceDescriptor(newRUDescriptor);
			req.getArguments().addAll(args);

			String result = getManagementService().runReport(marshaller.marshal(req));

			OperationResult or = (OperationResult) unmarshal(result);

			if (or.getReturnCode() != 0)
				throw new Exception(composeErrorMessage(or));

			Map<String, FileContent> results = new HashMap<String, FileContent>();

			Object[] resAtts = ((org.apache.axis.client.Stub) getManagementService()).getAttachments();
			boolean attachFound = false;
			for (int i = 0; resAtts != null && i < resAtts.length; ++i) {
				attachFound = true;
				DataHandler actualDH = (DataHandler) ((org.apache.axis.attachments.AttachmentPart) resAtts[i]).getDataHandler();
				String name = actualDH.getName(); // ((org.apache.axis.attachments.AttachmentPart)resAtts[i]).getAttachmentFile();
				String contentId = ((org.apache.axis.attachments.AttachmentPart) resAtts[i]).getContentId();
				if (name == null)
					name = "attachment-" + i; //$NON-NLS-1$
				if (contentId == null)
					contentId = "attachment-" + i; //$NON-NLS-1$

				InputStream is = actualDH.getInputStream();
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
				byte[] data = new byte[1000];
				try {
					int bytesRead;

					while ((bytesRead = is.read(data)) != -1) {
						bos.write(data, 0, bytesRead);
					}

					data = bos.toByteArray();
				} finally {
					FileUtils.closeStream(is);
					FileUtils.closeStream(bos);
				}
				String contentType = actualDH.getContentType();

				FileContent content = new FileContent();
				content.setData(data);
				content.setMimeType(contentType);
				content.setName(name);

				results.put(contentId, content);

			}
			if (!attachFound) {
				throw new Exception("Attachment not present!");
			}

			return results;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	/**
	 * Add or Modify a resource. Return the updated ResourceDescriptor
	 * 
	 */
	public ResourceDescriptor addOrModifyResource(ResourceDescriptor descriptor, File inputFile) throws Exception {
		return modifyReportUnitResource(null, descriptor, inputFile);
	}

	public ResourceDescriptor putResource(ResourceDescriptor descriptor, RequestAttachment[] attachments) throws Exception {
		return putReportUnitResource(null, descriptor, attachments);
	}

	public JServer getServer() {
		return server;
	}

	public void setServer(JServer server) {
		this.server = server;
	}

	public String getUsername() {
		return getServer().getUsername();
	}

	public String getPassword() {
		if (secretsUtil == null) {
			secretsUtil = SecretsUtil.getInstance(getJrContext());
		}
		return secretsUtil.getSecret(JRServerSecretsProvider.SECRET_NODE_ID, getServer().getPassword());
	}

	protected JasperReportsContext getJrContext() {
		if (JaspersoftStudioPlugin.getInstance() == null)
			return DefaultJasperReportsContext.getInstance();
		return JasperReportsConfiguration.getDefaultInstance();
	}

	public int getTimeout() {
		return Math.max(600000, getServer().getTimeout());
	}

	public boolean isChuncked() {
		return getServer().isChunked();
	}

	public ResourceDescriptor modifyReportUnitResource(String reportUnitUri, ResourceDescriptor descriptor, File inputFile) throws Exception {
		RequestAttachment[] attachments;
		if (inputFile == null) {
			attachments = new RequestAttachment[0];
		} else {
			// patch jrxml files....
			// if (IReportManager.getPreferences().getBoolean("use_jrxml_DTD",
			// false))
			// {
			// if (inputFile.getName().toLowerCase().endsWith(".jrxml"))
			// {
			// inputFile = patchJRXML(inputFile);
			// }
			// }

			FileDataSource fileDataSource = new FileDataSource(inputFile);
			RequestAttachment attachment = new RequestAttachment(fileDataSource);
			attachments = new RequestAttachment[] { attachment };
		}
		return putReportUnitResource(reportUnitUri, descriptor, attachments);
	}

	public ResourceDescriptor putReportUnitResource(String reportUnitUri, ResourceDescriptor descriptor, RequestAttachment[] attachments) throws Exception {

		try {
			Request req = new Request();
			req.setOperationName("put"); //$NON-NLS-1$
			req.setLocale(getServer().getLocale());

			if (reportUnitUri != null && reportUnitUri.length() > 0) {
				req.getArguments().add(new Argument(Argument.MODIFY_REPORTUNIT, reportUnitUri));
			}

			ManagementService ms = getManagementService();

			// ManagementServiceServiceLocator rsl = new
			// ManagementServiceServiceLocator();
			// ManagementService ms = rsl.getrepository(new java.net.URL(
			// getWebservicesUri() ) );
			// ((org.apache.axis.client.Stub)ms).setUsername( getUsername() );
			// ((org.apache.axis.client.Stub)ms).setPassword( getPassword() );
			// ((org.apache.axis.client.Stub)ms).setMaintainSession( false );

			// attach the file...
			if (attachments != null && attachments.length > 0) {
				descriptor.setHasData(true);
				// Tell the stub that the message being formed also contains an
				// attachment, and it is of type MIME encoding.
				if (getServer().isMime())
					((org.apache.axis.client.Stub) ms)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_MIME);
				else
					((org.apache.axis.client.Stub) ms)._setProperty(Call.ATTACHMENT_ENCAPSULATION_FORMAT, Call.ATTACHMENT_ENCAPSULATION_FORMAT_DIME);

				for (int i = 0; i < attachments.length; i++) {
					RequestAttachment attachment = attachments[i];
					DataHandler attachmentHandler = new DataHandler(attachment.getDataSource());
					AttachmentPart attachmentPart = new AttachmentPart(attachmentHandler);
					if (attachment.getContentID() != null) {
						attachmentPart.setContentId(attachment.getContentID());
					}

					// Add the attachment to the message
					((org.apache.axis.client.Stub) ms).addAttachment(attachmentPart);
				}
			}

			req.setResourceDescriptor(descriptor);

			String result = ms.put(marshaller.marshal(req));

			OperationResult or = (OperationResult) unmarshal(result);

			if (or.getReturnCode() != 0)
				throw new Exception(composeErrorMessage(or));

			return (ResourceDescriptor) or.getResourceDescriptors().get(0);

		} catch (Exception ex) {
			// ex.printStackTrace();
			throw ex;
		}
	}

	public void move(ResourceDescriptor resource, String destinationFolderURI) throws Exception {
		try {
			Request req = new Request();
			req.setOperationName("move"); //$NON-NLS-1$
			req.setResourceDescriptor(resource);
			req.setLocale(getServer().getLocale());
			req.getArguments().add(new Argument(Argument.DESTINATION_URI, destinationFolderURI));

			String result = getManagementService().move(marshaller.marshal(req));
			OperationResult or = (OperationResult) unmarshal(result);
			if (or.getReturnCode() != OperationResult.SUCCESS) {
				throw new Exception(or.getReturnCode() + " - " + or.getMessage()); //$NON-NLS-1$
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public ResourceDescriptor copy(ResourceDescriptor resource, String destinationFolderURI) throws Exception {
		try {
			Request req = new Request();
			req.setOperationName("copy"); //$NON-NLS-1$
			req.setResourceDescriptor(resource);
			req.setLocale(getServer().getLocale());
			req.getArguments().add(new Argument(Argument.DESTINATION_URI, destinationFolderURI));

			String result = getManagementService().copy(marshaller.marshal(req));
			OperationResult or = (OperationResult) unmarshal(result);
			if (or.getReturnCode() != OperationResult.SUCCESS) {
				throw new Exception(or.getReturnCode() + " - " + or.getMessage()); //$NON-NLS-1$
			}

			ResourceDescriptor copyDescriptor;
			List resultDescriptors = or.getResourceDescriptors();
			if (resultDescriptors == null || resultDescriptors.isEmpty()) {
				copyDescriptor = null;
			} else {
				copyDescriptor = (ResourceDescriptor) resultDescriptors.get(0);
			}
			return copyDescriptor;
		} catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	public String getWebservicesUri() {
		return webservicesUri;
	}

	public void setWebservicesUri(String webservicesUri) {
		this.webservicesUri = webservicesUri;
	}

	public ManagementService getManagementService() throws Exception {

		if (managementService == null) {
			ManagementServiceServiceLocator rsl = new ManagementServiceServiceLocator(getEngineConfiguration());
			managementService = rsl.getrepository(new java.net.URL(getWebservicesUri()));
			((org.apache.axis.client.Stub) managementService).setUsername(getUsername());
			((org.apache.axis.client.Stub) managementService).setPassword(getPassword());
			((org.apache.axis.client.Stub) managementService).setMaintainSession(true);
			((org.apache.axis.client.Stub) managementService).setTimeout(getTimeout());

			Hashtable headers = (Hashtable) ((org.apache.axis.client.Stub) managementService)._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null)
				headers = new Hashtable();
			headers.put(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED, isChuncked() ? Boolean.TRUE : Boolean.FALSE);
			((org.apache.axis.client.Stub) managementService)._setProperty(HTTPConstants.REQUEST_HEADERS, headers);

		}

		// int timeout = IReportManager.getPreferences().getInt("client_timeout", 0)
		// * 1000;
		// if (timeout !=
		// ((org.apache.axis.client.Stub)managementService).getTimeout())
		// {
		// ((org.apache.axis.client.Stub)managementService).setTimeout(timeout);
		// }
		return managementService;
	}

	protected EngineConfiguration getEngineConfiguration() {
		try {
			return new ResourceConfigurationProvider(AXIS_CONFIGURATION_RESOURCE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public void setManagementService(ManagementService managementService) {
		this.managementService = managementService;
	}

	protected Object unmarshal(String xml) throws Exception {
		Object obj = null;
		ClassLoader oldClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			// Thread.currentThread().setContextClassLoader(DOMParser.class.getClassLoader());
			obj = unmarshaller.unmarshal(xml);
		} finally {
			Thread.currentThread().setContextClassLoader(oldClassLoader);
		}

		return obj;

	}

	private File patchJRXML(File inputFile) throws Exception {

		/*
		 * String content = ""; FileInputStream is = new FileInputStream(inputFile);
		 * byte[] buffer = new byte[1024]; int bcount = 0; while ( (bcount =
		 * is.read(buffer)) > 0) { content += new String(buffer,0,bcount); }
		 * is.close();
		 * 
		 * if (content.indexOf(
		 * "xmlns=\"http://jasperreports.sourceforge.net/jasperreports\"") > 0) {
		 * content = Misc.string_replace(
		 * "<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD Report Design//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n<jasperReport "
		 * ,
		 * "<jasperReport xmlns=\"http://jasperreports.sourceforge.net/jasperreports\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://jasperreports.sourceforge.net/jasperreports http://jasperreports.sourceforge.net/xsd/jasperreport.xsd\""
		 * , content); File newFile = new File(
		 * JasperServerManager.createTmpFileName(null, null)); FileOutputStream os =
		 * new FileOutputStream(newFile); os.write( content.getBytes()); os.close();
		 * return newFile; }
		 */
		return inputFile;
	}

	/**
	 * @return the userAndRoleManagementService
	 */
	public UserAndRoleManagement getUserAndRoleManagementService() throws Exception {

		if (userAndRoleManagementService == null) {
			UserAndRoleManagementServiceLocator rsl = new UserAndRoleManagementServiceLocator(getEngineConfiguration());
			String uriString = getWebservicesUri();
			uriString = uriString.replace("/repository", "/UserAndRoleManagementService"); //$NON-NLS-1$ //$NON-NLS-2$

			userAndRoleManagementService = rsl.getUserAndRoleManagementServicePort(new java.net.URL(uriString));
			((org.apache.axis.client.Stub) userAndRoleManagementService).setUsername(getUsername());
			((org.apache.axis.client.Stub) userAndRoleManagementService).setPassword(getPassword());
			((org.apache.axis.client.Stub) userAndRoleManagementService).setMaintainSession(true);
			((org.apache.axis.client.Stub) userAndRoleManagementService).setTimeout(getTimeout());
			Hashtable headers = (Hashtable) ((org.apache.axis.client.Stub) userAndRoleManagementService)._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null)
				headers = new Hashtable();
			headers.put(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED, isChuncked() ? Boolean.TRUE : Boolean.FALSE);
			((org.apache.axis.client.Stub) userAndRoleManagementService)._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}

		// int timeout =
		// IReportManager.getPreferences().getInt("client_timeout", 0) * 1000;
		// if (timeout !=
		// ((org.apache.axis.client.Stub)managementService).getTimeout())
		// {
		// ((org.apache.axis.client.Stub)managementService).setTimeout(timeout);
		// }
		return userAndRoleManagementService;
	}

	/**
	 * @param userAndRoleManagementService
	 *          the userAndRoleManagementService to set
	 */
	public void setUserAndRoleManagementService(UserAndRoleManagement userAndRoleManagementService) {
		this.userAndRoleManagementService = userAndRoleManagementService;
	}

	/**
	 * @return the permissionsManagement
	 */
	public PermissionsManagement getPermissionsManagement() throws Exception {
		if (permissionsManagement == null) {
			PermissionsManagementServiceLocator rsl = new PermissionsManagementServiceLocator(getEngineConfiguration());
			String uriString = getWebservicesUri();
			uriString = uriString.replace("/repository", "/PermissionsManagementService"); //$NON-NLS-1$ //$NON-NLS-2$

			permissionsManagement = rsl.getPermissionsManagementServicePort(new java.net.URL(uriString));
			((org.apache.axis.client.Stub) permissionsManagement).setUsername(getUsername());
			((org.apache.axis.client.Stub) permissionsManagement).setPassword(getPassword());
			((org.apache.axis.client.Stub) permissionsManagement).setMaintainSession(true);
			((org.apache.axis.client.Stub) permissionsManagement).setTimeout(getTimeout());
			Hashtable headers = (Hashtable) ((org.apache.axis.client.Stub) permissionsManagement)._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null)
				headers = new Hashtable();
			headers.put(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED, isChuncked() ? Boolean.TRUE : Boolean.FALSE);
			((org.apache.axis.client.Stub) permissionsManagement)._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}

		// int timeout =
		// IReportManager.getPreferences().getInt("client_timeout", 0) * 1000;
		// if (timeout !=
		// ((org.apache.axis.client.Stub)managementService).getTimeout())
		// {
		// ((org.apache.axis.client.Stub)managementService).setTimeout(timeout);
		// }
		return permissionsManagement;
	}

	/**
	 * @param permissionsManagement
	 *          the permissionsManagement to set
	 */
	public void setPermissionsManagement(PermissionsManagement permissionsManagement) {
		this.permissionsManagement = permissionsManagement;
	}

	/**
	 * @return the reportSchedulerService
	 */
	public ReportScheduler getReportScheduler() throws Exception {
		if (reportScheduler == null) {
			ReportSchedulerServiceLocator rsl = new ReportSchedulerServiceLocator(getEngineConfiguration());
			String uriString = getWebservicesUri();
			uriString = uriString.replace("/repository", "/ReportScheduler"); //$NON-NLS-1$ //$NON-NLS-2$

			reportScheduler = rsl.getReportScheduler(new java.net.URL(uriString));
			((org.apache.axis.client.Stub) reportScheduler).setUsername(getUsername());
			((org.apache.axis.client.Stub) reportScheduler).setPassword(getPassword());
			((org.apache.axis.client.Stub) reportScheduler).setMaintainSession(true);
			((org.apache.axis.client.Stub) reportScheduler).setTimeout(getTimeout());
			Hashtable headers = (Hashtable) ((org.apache.axis.client.Stub) reportScheduler)._getProperty(HTTPConstants.REQUEST_HEADERS);
			if (headers == null)
				headers = new Hashtable();
			headers.put(HTTPConstants.HEADER_TRANSFER_ENCODING_CHUNKED, isChuncked() ? Boolean.TRUE : Boolean.FALSE);
			((org.apache.axis.client.Stub) reportScheduler)._setProperty(HTTPConstants.REQUEST_HEADERS, headers);
		}

		// int timeout =
		// IReportManager.getPreferences().getInt("client_timeout", 0) * 1000;
		// if (timeout !=
		// ((org.apache.axis.client.Stub)managementService).getTimeout())
		// {
		// ((org.apache.axis.client.Stub)managementService).setTimeout(timeout);
		// }
		return reportScheduler;
	}

	/**
	 * @param reportSchedulerService
	 *          the reportSchedulerService to set
	 */
	public void setReportScheduler(ReportScheduler reportScheduler) {
		this.reportScheduler = reportScheduler;
	}
}
