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
package com.jaspersoft.studio.editor.gef.decorator.csv;

import org.eclipse.gef.GraphicalViewer;

import com.jaspersoft.studio.editor.action.snap.ACheckResourcePrefAction;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Tag in the viewer to display or not the CSV decorator
 * 
 * @author Orlandin Marco
 * 
 */
public class ShowCSVTagsAction extends ACheckResourcePrefAction {
	public static final String ID = "com.jaspersoft.studio.editor.gef.decorator.csv.ShowCSVTagsAction"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param diagramViewer
	 *          the GraphicalViewer whose grid enablement and visibility properties are to be toggled
	 */
	public ShowCSVTagsAction(GraphicalViewer diagramViewer, JasperReportsConfiguration jrConfig) {
		super(Messages.ShowCSVTagsAction_label, jrConfig);
		setToolTipText(Messages.ShowCSVTagsAction_tooltip);
		setId(ID);
	}

	@Override
	protected String getProperty() {
		return ID;
	}
}
