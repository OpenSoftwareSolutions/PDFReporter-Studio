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
package com.jaspersoft.studio.swt.events;

import net.sf.jasperreports.engine.JRExpression;

import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.Widget;

import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 * The event that should be notified/received when dealing with expression widgets, like for example the {@link WTextExpression}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see ExpressionModifiedListener
 * @see WTextExpression
 *
 */
public class ExpressionModifiedEvent extends TypedEvent {

	private static final long serialVersionUID = -8192762845793694496L;
	
	/** The modified expression */
	public JRExpression modifiedExpression = null;
	
	public ExpressionModifiedEvent(Widget expressionWidget){
		super(expressionWidget);
	}

}
