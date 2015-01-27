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
package com.jaspersoft.studio.editor.jrexpressions.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;

/**
 * Custom class containing the list of available configurations for the JRExpression(s) elements.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JavaJRExpressionHighlightingConfiguration extends DefaultHighlightingConfiguration {

	public static final String FIELD_TOKEN="Field"; //$NON-NLS-1$
	public static final String PARAM_TOKEN="Parameter"; //$NON-NLS-1$
	public static final String VARIABLE_TOKEN="Variable"; //$NON-NLS-1$
	public static final String FUNCTION_METHOD="Library function"; //$NON-NLS-1$
	public static final String RESOURCE_BUNDLE_KEY="ResourceBundle Key"; //$NON-NLS-1$
	
	@Override
	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		super.configure(acceptor);
		addElementConfiguration(acceptor, PARAM_TOKEN, 187, 29, 29, SWT.BOLD);
		addElementConfiguration(acceptor, VARIABLE_TOKEN, 0, 0, 255, SWT.BOLD);
		addElementConfiguration(acceptor, FIELD_TOKEN, 0, 178, 0, SWT.BOLD);
		addElementConfiguration(acceptor, FUNCTION_METHOD, 0, 0, 0, SWT.ITALIC);
		addElementConfiguration(acceptor, RESOURCE_BUNDLE_KEY, 102, 46, 153, SWT.BOLD);
	}

	public void addElementConfiguration(IHighlightingConfigurationAcceptor acceptor, String s,
			int r, int g, int b, int style) {
		TextStyle textStyle = new TextStyle();
		textStyle.setBackgroundColor(new RGB(255, 255, 255));
		textStyle.setColor(new RGB(r, g, b));
		textStyle.setStyle(style);
		acceptor.acceptDefaultHighlighting(s, s, textStyle);
	}
}
