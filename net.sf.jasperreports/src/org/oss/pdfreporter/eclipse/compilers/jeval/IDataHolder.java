/*******************************************************************************
 * Copyright (c) 2013 Open Software Solutions GmbH.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.html
 * 
 * Contributors:
 *     Open Software Solutions GmbH - initial API and implementation
 ******************************************************************************/
package org.oss.pdfreporter.eclipse.compilers.jeval;

import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.fill.JRFillField;
import net.sf.jasperreports.engine.fill.JRFillVariable;

public interface IDataHolder {
	JRValueParameter getParameter(String name);
	JRFillField getField(String name);
	JRFillVariable getVariable(String name);	
}
