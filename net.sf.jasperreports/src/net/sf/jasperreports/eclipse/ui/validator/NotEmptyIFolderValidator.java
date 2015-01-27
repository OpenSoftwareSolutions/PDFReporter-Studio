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

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class NotEmptyIFolderValidator implements IValidator {

	public IStatus validate(Object value) {
		if (value != null && !((String) value).isEmpty()) {
			// IResource f =
			// ResourcesPlugin.getWorkspace().getRoot().getContainerForLocation(new
			// Path((String) value));
			// if (f == null || !f.exists() || !(f instanceof IFolder))
			// return ValidationStatus.error("The folder does not exists.");
		}
		return Status.OK_STATUS;
	}

}
