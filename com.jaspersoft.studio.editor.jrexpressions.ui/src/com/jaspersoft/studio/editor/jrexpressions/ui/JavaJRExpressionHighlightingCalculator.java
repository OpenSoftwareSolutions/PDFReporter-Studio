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

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.TerminalRule;
import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.parser.IParseResult;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.DefaultHighlightingConfiguration;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.DoubleLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FloatLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.IntLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRFieldObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRParameterObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRResourceBundleKeyObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JRVariableObj;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.LongLiteral;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.StringLiteral;
import com.jaspersoft.studio.editor.jrexpressions.util.JRExpressionsModelUtil;

/**
 * Custom class for semantic highlighting of the expression.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JavaJRExpressionHighlightingCalculator implements ISemanticHighlightingCalculator {
 
	public void provideHighlightingFor(XtextResource resource,
			IHighlightedPositionAcceptor acceptor) {
		if (resource == null)
			return;
		IParseResult parseResult = resource.getParseResult();
		if(parseResult==null)
			return;
		INode root = parseResult.getRootNode();
		Iterator<ILeafNode> leafNodesIt = root.getLeafNodes().iterator();
			
		while (leafNodesIt.hasNext()){
			ILeafNode nextLeaf = leafNodesIt.next();
			EObject semanticElement = nextLeaf.getSemanticElement();
			EObject grammarElement = nextLeaf.getGrammarElement();
			if(!isHiddenToken(grammarElement)) {
				if(semanticElement instanceof StringLiteral){
					acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), JavaJRExpressionHighlightingConfiguration.STRING_ID);
				}
				else if(semanticElement instanceof JRParameterObj){
					acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), JavaJRExpressionHighlightingConfiguration.PARAM_TOKEN);
				}
				else if(semanticElement instanceof JRVariableObj){
					System.out.println(nextLeaf.getGrammarElement());
					acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), JavaJRExpressionHighlightingConfiguration.VARIABLE_TOKEN);
				}
				else if(semanticElement instanceof JRFieldObj){
					acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), JavaJRExpressionHighlightingConfiguration.FIELD_TOKEN);
				}
				else if(semanticElement instanceof JRResourceBundleKeyObj) {
					acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), JavaJRExpressionHighlightingConfiguration.RESOURCE_BUNDLE_KEY);
				}
				else if(semanticElement instanceof FullMethodName){
					if(JRExpressionsModelUtil.isFunctionLibrary((FullMethodName)semanticElement)){
						acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), JavaJRExpressionHighlightingConfiguration.FUNCTION_METHOD);
					}
				}
				else if(isNumberElement(semanticElement)){
					acceptor.addPosition(nextLeaf.getOffset(), nextLeaf.getLength(), DefaultHighlightingConfiguration.NUMBER_ID);
				}
			}
		}
	}

	/*
	 * Checks if the specified element can be a hidden rule
	 * like single/multiline comments or spaces. 
	 */
	private static final boolean isHiddenToken(EObject element) {
		if (element instanceof TerminalRule) {
			String name = ((TerminalRule) element).getName();
			return "ML_COMMENT".equals(name) || "SL_COMMENT".equals(name)
					|| "WS".equals(name);
		}
		return false;
	}
	
	/*
	 * Checks if the semantic element can be a "Number".
	 */
	private static final boolean isNumberElement(EObject element){
		return element instanceof IntLiteral ||
				element instanceof LongLiteral ||
				element instanceof FloatLiteral ||
				element instanceof DoubleLiteral;
	}

}
