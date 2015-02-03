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

import org.oss.pdfreporter.eclipse.compilers.IExpressionElement;

public class SingleChunkTextTypeFactory {

	public static IExpressionElement buildExpression(String text) throws ExpressionParseException {
		if (NumberConstant.isNumber(text)) {
			return NumberConstant.parseNumber(text);
		}
		if (TextConstant.isText(text)) {
			return TextConstant.parseText(text);
		}
		return null;
//		throw new ExpressionParseException("Not supported expression: " + text);
	}
}
