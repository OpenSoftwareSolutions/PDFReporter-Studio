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
package com.jaspersoft.studio.property.descriptor.resource;

import org.eclipse.jface.viewers.ICellEditorValidator;

/*
 * The Class IntegerCellEditorValidator.
 * 
 * @author Chicu Veaceslav
 */
public class NResourceCellEditorValidator implements ICellEditorValidator {
	/** The instance. */
	private static NResourceCellEditorValidator instance;

	/**
	 * Instance.
	 * 
	 * @return the integer cell editor validator
	 */
	public static NResourceCellEditorValidator instance() {
		if (instance == null)
			instance = new NResourceCellEditorValidator();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if (value == null)
			return null;
		if (value instanceof String) {
			// if (value != null) {
			// // Pattern pattern = Pattern.compile(ClassTypeCellEditorValidator.regexp);
			// // Matcher matcher = pattern.matcher((String) value);
			// // if (matcher.matches())
			// return null;
			// }
			return null;
		}
		return "This is a not correct resource";
	}

}
