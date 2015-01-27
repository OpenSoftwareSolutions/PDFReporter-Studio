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
package com.jaspersoft.jrx.query;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;
import net.sf.jasperreports.engine.util.JRXmlUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.tools.codec.Base64Encoder;
import org.xml.sax.SAXException;

import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.XMLUtils;

/**
 * XPath query executer implementation.
 * <p/>
 * The XPath query of the report is executed against the document specified by
 * the
 * {@link com.jaspersoft.jrx.query.JRXPathQueryExecuterFactory#PARAMETER_XML_DATA_DOCUMENT
 * PARAMETER_XML_DATA_DOCUMENT} parameter.
 * <p/>
 * All the paramters in the XPath query are replaced by calling
 * <code>String.valueOf(Object)</code> on the parameter value.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRXPathQueryExecuter.java 3 2007-04-19 15:19:54Z teodord $
 */
public class JRXPathQueryExecuter extends JRAbstractQueryExecuter {
	private static final Log log = LogFactory
			.getLog(JRXPathQueryExecuter.class);

	private final Document document;

	public JRXPathQueryExecuter(JasperReportsContext context,
			JRDataset dataset,
			Map<String, ? extends JRValueParameter> parametersMap) {
		super(context, dataset, parametersMap);

		Document tmpDocument = (Document) getParameterValue(JRXPathQueryExecuterFactory.PARAMETER_XML_DATA_DOCUMENT);

		if (tmpDocument == null) {
			// check for XML_URL
			String url = (String) getParameterValue(JRXPathQueryExecuterFactory.XML_URL);
			if (url != null && url.trim().length() > 0) {
				try {
					tmpDocument = getDocumentFromUrl(parametersMap);
				} catch (Exception ex) {
					log.error(ex.getMessage(), ex);
					tmpDocument = null;
				}
			}

			if (tmpDocument == null) {
				log.warn("The supplied org.w3c.dom.Document object is null.");
			}
		}

		document = tmpDocument;

		parseQuery();
	}

	protected String getParameterReplacement(String parameterName) {
		return String.valueOf(getParameterValue(parameterName));
	}

	public JRDataSource createDatasource() throws JRException {
		JRXmlDataSource datasource = null;

		String xPath = getQueryString();

		if (log.isDebugEnabled()) {
			log.debug("XPath query: " + xPath);
		}

		if (document != null && xPath != null) {
			datasource = new JRXmlDataSource(document, xPath);
			datasource
					.setLocale((Locale) getParameterValue(JRXPathQueryExecuterFactory.XML_LOCALE));
			datasource
					.setDatePattern((String) getParameterValue(JRXPathQueryExecuterFactory.XML_DATE_PATTERN));
			datasource
					.setNumberPattern((String) getParameterValue(JRXPathQueryExecuterFactory.XML_NUMBER_PATTERN));
			datasource
					.setTimeZone((TimeZone) getParameterValue(JRXPathQueryExecuterFactory.XML_TIME_ZONE));
		}

		return datasource;
	}

	public void close() {
		// nothing to do
	}

	public boolean cancelQuery() throws JRException {
		// nothing to cancel
		return false;
	}

	private Document getDocumentFromUrl(
			Map<String, ? extends JRValueParameter> parametersMap)
			throws Exception {
		// Get the url...
		String urlString = (String) getParameterValue(JRXPathQueryExecuterFactory.XML_URL);

		// add GET parameters to the urlString...
		Iterator<String> i = parametersMap.keySet().iterator();

		String div = "?";
		URL url = new URL(urlString);
		if (url.getQuery() != null)
			div = "&";

		while (i.hasNext()) {
			String keyName = "" + i.next();
			if (keyName.startsWith("XML_GET_PARAM_")) {
				String paramName = keyName.substring("XML_GET_PARAM_".length());
				String value = (String) getParameterValue(keyName);

				urlString += div + URLEncoder.encode(paramName, "UTF-8") + "="
						+ URLEncoder.encode(value, "UTF-8");
				div = "&";
			}
		}

		url = new URL(urlString);

		if (url.getProtocol().toLowerCase().equals("file")) {
			// do nothing
			return JRXmlUtils.parse(url.openStream());
		} else if (url.getProtocol().toLowerCase().equals("http")
				|| url.getProtocol().toLowerCase().equals("https")) {
			String username = (String) getParameterValue(JRXPathQueryExecuterFactory.XML_USERNAME);
			String password = (String) getParameterValue(JRXPathQueryExecuterFactory.XML_PASSWORD);

			if (url.getProtocol().toLowerCase().equals("https")) {
				JRPropertiesUtil dPROP = PropertiesHelper.DPROP;
				String socketFactory = dPROP
						.getProperty("net.sf.jasperreports.query.executer.factory.xPath.DefaultSSLSocketFactory");
				if (socketFactory == null) {
					socketFactory = dPROP
							.getProperty("net.sf.jasperreports.query.executer.factory.XPath.DefaultSSLSocketFactory");
				}

				if (socketFactory != null) {
					// setSSLSocketFactory
					HttpsURLConnection
							.setDefaultSSLSocketFactory((SSLSocketFactory) Class
									.forName(socketFactory).newInstance());
				} else {
					log.debug("No SSLSocketFactory defined, using default");
				}

				String hostnameVerifyer = dPROP
						.getProperty("net.sf.jasperreports.query.executer.factory.xPath.DefaultHostnameVerifier");
				if (hostnameVerifyer == null) {
					hostnameVerifyer = dPROP
							.getProperty("net.sf.jasperreports.query.executer.factory.XPath.DefaultHostnameVerifier");
				}

				if (hostnameVerifyer != null) {
					// setSSLSocketFactory
					HttpsURLConnection
							.setDefaultHostnameVerifier((HostnameVerifier) Class
									.forName(hostnameVerifyer).newInstance());
				} else {
					log.debug("No HostnameVerifier defined, using default");
				}
			}

			URLConnection conn = url.openConnection();

			if (username != null && username.length() > 0 && password != null) {
				ByteArrayInputStream bytesIn = new ByteArrayInputStream(
						(username + ":" + password).getBytes());
				ByteArrayOutputStream dataOut = new ByteArrayOutputStream();
				Base64Encoder enc = new Base64Encoder(bytesIn, dataOut);
				enc.process();
				String encoding = dataOut.toString();
				conn.setRequestProperty("Authorization", "Basic " + encoding);
			}

			// add POST parameters to the urlString...
			i = parametersMap.keySet().iterator();

			String data = "";
			div = "";
			while (i.hasNext()) {
				String keyName = "" + i.next();
				if (keyName.startsWith("XML_POST_PARAM_")) {
					String paramName = keyName.substring("XML_POST_PARAM_"
							.length());
					String value = (String) getParameterValue(keyName);
					data += div + URLEncoder.encode(paramName, "UTF-8") + "="
							+ URLEncoder.encode(value, "UTF-8");
					div = "&";
				}
			}

			conn.setDoOutput(true);

			if (data.length() > 0) {
				conn.setDoInput(true);
				OutputStreamWriter wr = new OutputStreamWriter(
						conn.getOutputStream());
				wr.write(data);
				wr.flush();
			}

			try {
				return XMLUtils.parseNoValidation(conn.getInputStream());
			} catch (SAXException e) {
				throw new JRException("Failed to parse the xml document", e);
			} catch (IOException e) {
				throw new JRException("Failed to parse the xml document", e);
			} catch (ParserConfigurationException e) {
				throw new JRException(
						"Failed to create a document builder factory", e);
			}

			// return JRXmlUtils.parse(conn.getInputStream());
		} else {
			throw new JRException("URL protocol not supported");
		}
	}
}
