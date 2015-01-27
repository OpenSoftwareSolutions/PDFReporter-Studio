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
package com.jaspersoft.studio.model.util;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.utils.Misc;

public class ModelUtil {
	public static INode getNode(final Object obj, INode parent) {
		ModelVisitor<INode> mv = new ModelVisitor<INode>(parent) {

			@Override
			public boolean visit(INode n) {
				if (n.getValue() == obj)
					setObject(n);
				return true;
			}
		};
		return (INode) mv.getObject();
	}

	public static String list2string(String[][] items) {
		String str = "";
		for (int i = 0; i < items.length; i++) {
			for (int j = 0; j < items[i].length; j++) {
				str += items[i][j];
				if (j < items[i].length - 1)
					str += ";";
			}
			str += "\n";
		}
		return str;
	}

	public static boolean isEmpty(ANode n) {
		return Misc.isNullOrEmpty(n.getChildren()) || n.getChildren().get(0) instanceof MDummy;
	}
}
