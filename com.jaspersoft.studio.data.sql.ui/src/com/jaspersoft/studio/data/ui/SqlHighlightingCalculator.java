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
package com.jaspersoft.studio.data.ui;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import com.jaspersoft.studio.data.sql.ExpOperand;
import com.jaspersoft.studio.data.sql.POperand;
import com.jaspersoft.studio.data.sql.XExpr;

/**
 * Custom class for semantic highlighting of the expression.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class SqlHighlightingCalculator implements ISemanticHighlightingCalculator {

	public void provideHighlightingFor(XtextResource resource, IHighlightedPositionAcceptor acceptor) {
		if (resource == null)
			return;
		IParseResult parseResult = resource.getParseResult();
		if (parseResult == null)
			return;
		INode root = parseResult.getRootNode();
		Iterator<ILeafNode> leafNodesIt = root.getLeafNodes().iterator();

		while (leafNodesIt.hasNext()) {
			ILeafNode nextLeaf = leafNodesIt.next();
			EObject semanticElement = nextLeaf.getSemanticElement();
			if (semanticElement instanceof POperand) {
				acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), SqlHighlightingConfiguration.PARAM_TOKEN);
			} else if (semanticElement instanceof ExpOperand) {
				acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), SqlHighlightingConfiguration.PARAM_TOKEN);
			} else if (semanticElement instanceof XExpr) {
				acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), SqlHighlightingConfiguration.XEXPRESSION_TOKEN);
			}
		}
	}

}
