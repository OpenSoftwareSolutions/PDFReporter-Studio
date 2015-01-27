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
package com.jaspersoft.studio.server.wizard.pages;

import net.sf.jasperreports.eclipse.messages.Messages;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class UsernameValidator implements IValidator {

	public IStatus validate(Object value) {
		String uname = (String) value;
		if (value == null || uname.isEmpty())
			return ValidationStatus.error(Messages.EmptyStringValidator_EmptyError);
		if (uname.length() > 100)
			return ValidationStatus.error("Username is too long, max 100 chars");
		if(uname.contains(" "))
			return ValidationStatus.error("don't use spaces in name");
		for (char c : uname.toCharArray()) {
			if (Character.isLetterOrDigit(c))
				continue;
			return ValidationStatus.error("don't use any special symbols in name");
		}
		return Status.OK_STATUS;
	}

}
