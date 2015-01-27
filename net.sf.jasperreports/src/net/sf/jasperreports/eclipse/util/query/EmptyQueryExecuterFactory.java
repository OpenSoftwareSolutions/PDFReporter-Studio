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
package net.sf.jasperreports.eclipse.util.query;

import java.util.Map;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JREmptyQueryExecuter;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;

/*
 * Empty query executer factory.
 * <p/>
 * The factory creates {@link net.sf.jasperreports.engine.query.JREmptyQueryExecuter JREmptyQueryExecuter}
 * query executers.
 * 
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: EmptyQueryExecuterFactory.java 920 2014-07-23 16:22:43Z mrabbi $
 */
public class EmptyQueryExecuterFactory implements QueryExecuterFactory {

	public Object[] getBuiltinParameters() {
		return null;
	}

	@Deprecated
	public JRQueryExecuter createQueryExecuter(JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) throws JRException {
		return new JREmptyQueryExecuter();
	}

	public boolean supportsQueryParameterType(String className) {
		return true;
	}

	public JRQueryExecuter createQueryExecuter(JasperReportsContext jasperReportsContext, JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) throws JRException {
		return new JREmptyQueryExecuter();
	}
}
