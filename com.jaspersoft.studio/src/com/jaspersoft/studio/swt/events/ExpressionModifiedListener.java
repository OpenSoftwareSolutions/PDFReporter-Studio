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

import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 * This listener is meant to be used when dealing with expression widgets, like for example the {@link WTextExpression}.
 *  
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see ExpressionModifiedEvent
 * @see WTextExpression
 *
 */
public interface ExpressionModifiedListener {
	/**
	 * Invoked when an expression is modified.
	 * 
	 * @param event the modification event
	 */
	void expressionModified(ExpressionModifiedEvent event);
}
