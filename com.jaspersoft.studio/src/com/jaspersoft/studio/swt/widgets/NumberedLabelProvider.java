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
package com.jaspersoft.studio.swt.widgets;

import org.eclipse.jface.viewers.LabelProvider;

/**
 * Label provider that should be used when listing generic elements
 * without a specific text. The representing label will be composed by a prefix, 
 * plus the item incremental position.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class NumberedLabelProvider extends LabelProvider {
	private int currentIdx = 1;
	private String prefix;
	
	public NumberedLabelProvider(String prefix) {
		this.prefix = prefix;
	}
	
	@Override
	public String getText(Object element) {
		return prefix + currentIdx++;
	}
	
	public void resetIndex() {
		currentIdx=1;
	}
}
