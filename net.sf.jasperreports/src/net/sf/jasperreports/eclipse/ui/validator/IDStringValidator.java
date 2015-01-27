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
package net.sf.jasperreports.eclipse.ui.validator;

import net.sf.jasperreports.eclipse.messages.Messages;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class IDStringValidator implements IValidator {
	private final static char[] allowed = "_.".toCharArray(); //$NON-NLS-1$

	// private Pattern pattern = Pattern.compile("^[A-Za-z0-9_.//-]{0,100}$"); //$NON-NLS-1$

	public IStatus validate(Object value) {
		String v = (String) value;
		if (value == null || v.isEmpty())
			return ValidationStatus.error(Messages.IDStringValidator_EmptyError);
		if (v.length() > 100)
			return ValidationStatus.error("ID size between 0 and 100");
		for (char c : v.toCharArray()) {
			if (Character.isLetterOrDigit(c))
				continue;
			boolean isAllowed = false;
			for (char a : allowed) {
				if (c == a) {
					isAllowed = true;
					break;
				}
			}
			if (!isAllowed)
				return ValidationStatus.error(Messages.IDStringValidator_InvalidChars);
		}
		return Status.OK_STATUS;
	}

	public static String safeChar(String input) {
		char[] charArray = input.toString().toCharArray();
		StringBuilder result = new StringBuilder();
		for (char c : charArray) {
			if (Character.isLetterOrDigit(c))
				result.append(c);
			else
				for (char a : allowed) {
					if (c == a)
						result.append(a);
				}
		}
		return result.toString();
	}
}
