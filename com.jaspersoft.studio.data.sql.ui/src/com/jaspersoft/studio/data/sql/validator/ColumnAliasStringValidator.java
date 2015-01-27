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
package com.jaspersoft.studio.data.sql.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.eclipse.messages.Messages;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class ColumnAliasStringValidator implements IValidator {
	private Pattern pattern = Pattern.compile("^[A-Za-z0-9_-]{0,100}$"); //$NON-NLS-1$

	public IStatus validate(Object value) {
		if (value == null || ((String) value).isEmpty())
			return Status.OK_STATUS;
		String str = (String) value;

		Matcher matcher = pattern.matcher(str.replace(" ", ""));
		if (!matcher.matches())
			return ValidationStatus.error(Messages.IDStringValidator_InvalidChars);
		return Status.OK_STATUS;
	}
}
