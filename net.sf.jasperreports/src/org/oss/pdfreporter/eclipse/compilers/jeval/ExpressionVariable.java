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

import org.oss.pdfreporter.eclipse.compilers.jeval.IExpressionChunk.ExpresionType;
import net.sf.jasperreports.engine.fill.JRFillVariable;


public class ExpressionVariable implements IVariable {

	private final IDataHolder data;
	private final String name;
		
	public ExpressionVariable(IDataHolder data, String name) {
		this.data = data;
		this.name = name;
	}

	@Override
	public Object getValue() {
		return getVariable().getValue();
	}

	@Override
	public Object getOldValue() {
		return getVariable().getOldValue();
	}
	
	@Override
	public Object getVariableHolder() {
		return getVariable();
	}

	@Override
	public ExpresionType getType() {
		return ExpresionType.TYPE_VARIABLE;
	}

	@Override
	public String getName() {
		return name;
	}

	private JRFillVariable getVariable() {
		return data.getVariable(getName());
	}

	@Override
	public String toString() {
		return "ExpressionVariable [name=" + name + "]";
	}
	
}
