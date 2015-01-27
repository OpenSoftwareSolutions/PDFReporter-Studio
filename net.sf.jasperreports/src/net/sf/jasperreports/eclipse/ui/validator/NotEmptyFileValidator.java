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

import java.io.File;
import java.io.InputStream;

import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.eclipse.core.databinding.validation.IValidator;
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class NotEmptyFileValidator implements IValidator {
	private static final IStatus ERROR_STATUS = ValidationStatus.error(Messages.NotEmptyFileValidator_filenotexists);
	private JasperReportsContext jrContext;

	public NotEmptyFileValidator() {
		super();
	}

	public NotEmptyFileValidator(JasperReportsContext jrContext) {
		super();
		this.jrContext = jrContext;
	}

	public IStatus validate(Object value) {
		IStatus s = Status.OK_STATUS;
		if (value != null && !((String) value).isEmpty()) {
			File f = new File((String) value);
			if (!f.exists() || !f.isFile()) {
				if (jrContext != null) {
					InputStream is = null;
					try {
						System.out.println(jrContext);
						is = RepositoryUtil.getInstance(jrContext).getInputStreamFromLocation((String) value);
						if (is == null)
							s = ERROR_STATUS;
					} catch (Exception e) {
						s = ERROR_STATUS;
					} finally {
						FileUtils.closeStream(is);
					}
				} else
					s = ERROR_STATUS;
			}
		}
		return s;
	}
}
