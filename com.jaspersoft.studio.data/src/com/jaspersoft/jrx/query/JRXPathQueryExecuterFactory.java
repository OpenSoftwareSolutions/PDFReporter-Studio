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

import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;

/**
 * XPath query executer factory.
 * <p/>
 * The factory creates {@link com.jaspersoft.jrx.query.JRXPathQueryExecuter
 * JRXPathQueryExecuter} query executers.
 * 
 * @author Lucian Chirita (lucianc@users.sourceforge.net)
 * @version $Id: JRXPathQueryExecuterFactory.java 3 2007-04-19 15:19:54Z teodord
 *          $
 */
public class JRXPathQueryExecuterFactory implements QueryExecuterFactory {
	public static final String QUERY_LANGUAGE = "xpath2";
	/**
	 *
	 */
	public static final String XML_DS_SOCKET_FACTORY = JRPropertiesUtil.PROPERTY_PREFIX
			+ "datasource.xml.";

	/**
	 * Built-in parameter holding the value of the org.w3c.dom.Document used to
	 * run the XPath query.
	 */
	public final static String PARAMETER_XML_DATA_DOCUMENT = "XML_DATA_DOCUMENT";

	/**
	 * Parameter holding the format pattern used to instantiate java.util.Date
	 * instances.
	 */
	public final static String XML_DATE_PATTERN = "XML_DATE_PATTERN";

	/**
	 * Parameter holding the format pattern used to instantiate java.lang.Number
	 * instances.
	 */
	public final static String XML_NUMBER_PATTERN = "XML_NUMBER_PATTERN";

	/**
	 * Parameter holding the value of the datasource Locale
	 */
	public final static String XML_LOCALE = "XML_LOCALE";

	/**
	 * Parameter holding the value of the datasource Timezone
	 */
	public final static String XML_TIME_ZONE = "XML_TIME_ZONE";

	/**
	 * Built-in parameter holding an URL from which take the XML file
	 */
	public final static String XML_URL = "XML_URL";

	/**
	 * Built-in parameter holding an optional username to use with the XML_URL
	 * from which take the XML file
	 */
	public final static String XML_USERNAME = "XML_USERNAME";

	/**
	 * Built-in parameter holding an optional password to use with the XML_URL
	 * from which take the XML file
	 */
	public final static String XML_PASSWORD = "XML_PASSWORD";

	private final static Object[] XPATH_BUILTIN_PARAMETERS = {
			PARAMETER_XML_DATA_DOCUMENT, org.w3c.dom.Document.class,
			XML_DATE_PATTERN, java.lang.String.class, XML_NUMBER_PATTERN,
			java.lang.String.class, XML_LOCALE, java.util.Locale.class,
			XML_TIME_ZONE, java.util.TimeZone.class, XML_URL,
			java.lang.String.class, XML_USERNAME, java.lang.String.class,
			XML_PASSWORD, java.lang.String.class, };

	public Object[] getBuiltinParameters() {
		return XPATH_BUILTIN_PARAMETERS;
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters)
			throws JRException {
		return new JRXPathQueryExecuter(
				DefaultJasperReportsContext.getInstance(), dataset, parameters);
	}

	public boolean supportsQueryParameterType(String className) {
		return true;
	}

	public JRQueryExecuter createQueryExecuter(
			JasperReportsContext jasperReportsContext, JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters)
			throws JRException {
		return new JRXPathQueryExecuter(jasperReportsContext, dataset,
				parameters);
	}
}
