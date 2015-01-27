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
package com.jaspersoft.studio.property.descriptor.classname;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.viewers.ICellEditorValidator;
/*
 * The Class IntegerCellEditorValidator.
 * 
 * @author Chicu Veaceslav
 */
public class ImportDeclarationCellEditorValidator implements ICellEditorValidator {
	public static String regexp = "^(([A-Za-z0-9_])+.)+[A-Z]([A-Za-z0-9])+$"; //$NON-NLS-1$
	/** The instance. */
	private static ImportDeclarationCellEditorValidator instance;

	/**
	 * Instance.
	 * 
	 * @return the integer cell editor validator
	 */
	public static ImportDeclarationCellEditorValidator instance() {
		if (instance == null)
			instance = new ImportDeclarationCellEditorValidator();
		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ICellEditorValidator#isValid(java.lang.Object)
	 */
	public String isValid(Object value) {
		if (value instanceof String) {
			if (value != null) {
				Pattern pattern = Pattern.compile(regexp);
				Matcher matcher = pattern.matcher((String) value);
				if (matcher.matches())
					return null;
			}
			return "Value should be not null";
		}
		return "This is a not correct java Class";
	}

}
