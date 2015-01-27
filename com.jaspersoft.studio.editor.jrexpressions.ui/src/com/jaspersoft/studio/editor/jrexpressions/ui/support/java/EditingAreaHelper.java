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
package com.jaspersoft.studio.editor.jrexpressions.ui.support.java;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.EObjectAtOffsetHelper;

import com.jaspersoft.studio.editor.expression.FunctionsLibraryUtil;
import com.jaspersoft.studio.editor.jrexpressions.functions.AdditionalStaticFunctions;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.ExpressionList;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression;
import com.jaspersoft.studio.editor.jrexpressions.ui.support.ObjectCategorySelectionEvent;
import com.jaspersoft.studio.editor.jrexpressions.ui.support.ObjectCategorySelectionListener;
import com.jaspersoft.studio.editor.jrexpressions.ui.support.StyledTextXtextAdapter2;

/**
 * Utility object that exposes some methods to work with the current editing area.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class EditingAreaHelper {

	// flag to indicate if an update operation (text modification) is occurring.
	private boolean update;	
	// reference to the adapter for the styled text widget containing the xtext expression
	private StyledTextXtextAdapter2 xtextAdapter;
	// reference to the text widget with the expression
	private StyledText textArea;	
	// list of listeners
	private List<ObjectCategorySelectionListener> categorySelectionListeners=new ArrayList<ObjectCategorySelectionListener>();

	/**
	 * Creates the helper object.
	 * 
	 * @param xtextAdapter the adapter for the styled text widget
	 * @param textArea the text widget where the expression is currently being edited
	 */
	public EditingAreaHelper(StyledTextXtextAdapter2 xtextAdapter, StyledText textArea) {
		super();
		this.xtextAdapter = xtextAdapter;
		this.textArea = textArea;
	}
	
	/**
	 * Returns the current function name using the cursor position in the text editor.
	 * 
	 * <p>
	 * This method seeks for library functions so no dotted identifier is considered 
	 * for now. This means that when other kind of method invocation are found this
	 * method returns <code>null</code>.
	 * 
	 * @return the name of the currently selected function, <code>null</code> otherwise
	 */
	public String getCurrentLibraryFunctionName(){
		// TODO - When dotted library functions will be allowed this method must be improved
		Arguments args = getMethodArguments();
		if(args!=null && args.eContainer() instanceof MethodInvocation){
			MethodInvocation methodInv=(MethodInvocation)args.eContainer();
			ICompositeNode methodInvNode = NodeModelUtils.findActualNodeFor(methodInv);
			// Avoid malformed method invocation
			if(methodInvNode!=null && methodInvNode.getText().endsWith(")")){ //$NON-NLS-1$
				FullMethodName fullyQualifiedMethodName = methodInv.getFullyQualifiedMethodName();
				if(fullyQualifiedMethodName!=null){
					String methodName = fullyQualifiedMethodName.getMethodName();
					if(FunctionsLibraryUtil.existsFunction(methodName) || 
							AdditionalStaticFunctions.getAllNames().contains(methodName)){
						return methodName;
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * Returns the position of a function argument currently identified by
	 * the cursor position in the editing area.
	 * 
	 * <p>
	 * NOTE: a valid position can be returned only when the cursor is inside a method
	 * invocation element.
	 * 
	 * @return the function argument position if any, <code>-1</code> otherwise
	 */
	public int getArgumentPosition(){
		Arguments args=getMethodArguments();
		int actualNodeOffset = getActualNodeOffset();
		if(args!=null){
			final ExpressionList exprLst=args.getExprLst();
			if(exprLst!=null){
				ICompositeNode expressionsLst = NodeModelUtils.getNode(exprLst);
				if(actualNodeOffset>=expressionsLst.getOffset()){
					int argumentPos=1;
					for (INode child : expressionsLst.getChildren()){
						if(child.getOffset()<actualNodeOffset && 
							child.getGrammarElement() instanceof Keyword && 
							",".equals(((Keyword)child.getGrammarElement()).getValue())){ //$NON-NLS-1$
								argumentPos++;
						}
					}
					
					return argumentPos; 
				}
			}
			else{
				// No arguments found.
				// Potential position for argument 1 (excluded the '(' char)
				if(actualNodeOffset>=NodeModelUtils.getNode(args).getOffset()+1){
					return 1;
				}
			}
		}

		return -1;
	}

	/**
	 * Returns the text for the argument in the specified position, 
	 * of the (potential) selected function.
	 * 
	 * <p>
	 * NOTE: a valid function can be located only when the cursor is inside a method
	 * invocation element.
	 * 
	 * @param position the argument position
	 * @return the text associated to the argument, <code>null</code> otherwise
	 */
	public String getTextForArgument(int position) {
		Arguments args=getMethodArguments();
		if(args!=null){
			final ExpressionList exprLst=args.getExprLst();
			if(exprLst!=null){ 
				if(exprLst.getCommas().size()>0){
					if(position>exprLst.getCommas().size()+1){
						// already out of scope: parameter position not valid
						return null;
					}
			
					int commasNum = exprLst.getCommas().size();
					ICompositeNode argsNode = NodeModelUtils.getNode(args);
					int argsStart = argsNode.getOffset();	// left parenthesis position
					int argsEnd = argsNode.getTotalEndOffset();	// right parenthesis position
					int selectionStart=-1;
					int selectionEnd=-1;
					
					// The parameter index is comprised in the available locations
					// Comma separated expressions (or even blank chars) => get all comma positions
					List<Integer> commasOffsets=new ArrayList<Integer>();									
					for(INode c : NodeModelUtils.findActualNodeFor(exprLst).getChildren()){
						if(c.getGrammarElement() instanceof Keyword && 
								",".equals(((Keyword)c.getGrammarElement()).getValue())){ //$NON-NLS-1$
							commasOffsets.add(c.getOffset());
						}
					}
					
					if(position==1){
						selectionStart=argsStart+1;
						selectionEnd=commasOffsets.get(0);
					}
					else if (position == commasNum+1){
						selectionStart=commasOffsets.get(commasNum-1)+1;
						selectionEnd=argsEnd-1;
					}
					else {
						selectionStart=commasOffsets.get(position-2)+1;
						selectionEnd=commasOffsets.get(position-1);
					}
					
					if((selectionStart!=-1 && selectionEnd!=-1) &&
							selectionEnd>selectionStart){
						try{
							return textArea.getText(selectionStart, selectionEnd-1);
						}
						catch(IllegalArgumentException ex){
							// Text modification is occurred while
							// we were updating the text widget content
							// in the function detail panel => ignore it
							return null;
						}
					}
					else {
						return null;
					}
					
				}
				else{
					// One single potential parameter
					if(exprLst.getExpressions().size()==1 && position==1){
						ICompositeNode exprNode = NodeModelUtils.getNode(exprLst.getExpressions().get(0));
						return NodeModelUtils.getTokenText(exprNode);
					}
				}
			}
		}
		return null;
	}

	/**
	 * Performs the selection of the expression text corresponding to the argument
	 * in the specified position of the (potential) actual function.
	 * 
	 * <p>
	 * NOTE: a valid function can be located only when the cursor is inside a method
	 * invocation element.
	 * 
	 * @param position position of the argument to be selected
	 * @param lastPosition the last valid argument position
	 * @param parametersTexts texts contained in the parameter widgets
	 */
	public void selectMethodArgument(int position, int lastPosition, List<String> parametersTexts) {
		Arguments args=getMethodArguments(); 
		if(args!=null){
			ICompositeNode argsNode = NodeModelUtils.findActualNodeFor(args);
			int argsStart = argsNode.getOffset();	// left parenthesis position
			int argsEnd = argsNode.getTotalEndOffset();	// right parenthesis position

			// Handle potential broken expression text (and tree too)
			// Example: DAY(DATE(,12,5))
			// We could get an Arguments object that has the following text: (DATE(,12,5)
			// First expressions is considered the broken MethodsExpression DATE(
			if (args.getExprLst()!=null && args.getExprLst().getExpressions()!=null && 
					args.getExprLst().getExpressions().size()>0){
				JasperReportsExpression firstExpr = args.getExprLst().getExpressions().get(0);
				if(firstExpr instanceof MethodsExpression){
					ICompositeNode methodsExprNode = NodeModelUtils.findActualNodeFor(firstExpr);
					if(!methodsExprNode.getText().endsWith(")")){ //$NON-NLS-1$
						// error found: so let's set the 
						argsStart=methodsExprNode.getTotalEndOffset()-1;
					}
				}
			}
			
			// Manually re-construct it using the parameters text as information
			// This solution has been preferred to the one that uses the model nodes.
			// Main reason is that in case of broken expressions we would eventually end-up
			// to adopt this strategy.
			int positionStart=-1;
			int positionEnd=-1;
			textArea.setSelection(argsStart+1,argsEnd-1);
			StringBuffer sb=new StringBuffer(""); //$NON-NLS-1$
			for (int i=0;i<lastPosition-1;i++){
				String paramTxt = parametersTexts.get(i);
				int paramTxtLength = paramTxt.length();
				if(i==position-1){
					positionStart=argsStart+sb.length()+1;
					// need to consider a comma, and a white space
					// for nice formatting when empty param text
					positionEnd=positionStart+Math.max(paramTxtLength,1);  
				}
				sb.append(paramTxt);
				if(paramTxtLength==0){
					sb.append(" "); //$NON-NLS-1$
				}
				sb.append(","); //$NON-NLS-1$
			}
			String lastParamTxt=parametersTexts.get(lastPosition-1);
			int lastParamTextLength=lastParamTxt.length();
			if(position==lastPosition){
				positionStart=argsStart+sb.length()+1;
				positionEnd=positionStart+Math.max(lastParamTextLength,1); 
			}
			if(lastParamTextLength==0){
				sb.append(" "); //$NON-NLS-1$
			}
			sb.append(lastParamTxt);
			textArea.insert(sb.toString());
			textArea.setSelection(positionStart,positionEnd);
			return;
		}
	}
	
	/*
	 * Given the current position in the editing area, tries to retrieve the semantic element 
	 * representing the arguments of a method invocation.
	 */
	private Arguments getMethodArguments(){
		if(xtextAdapter.getXtextParseResult()!=null){
			ICompositeNode actualNode=getActualNode();
			if(actualNode!=null){
				INode tmpParentNode=actualNode;
				boolean foundParentNode=false;
				while(!foundParentNode && tmpParentNode!=null){
					if(tmpParentNode.getSemanticElement() instanceof Arguments || tmpParentNode.getSemanticElement() instanceof MethodInvocation){
						foundParentNode=true;
					}
					else{
						tmpParentNode=tmpParentNode.getParent();
					}
				}
				if(foundParentNode){
					Arguments args=null;
					if(tmpParentNode.getSemanticElement() instanceof MethodInvocation){
						args=((MethodInvocation)tmpParentNode.getSemanticElement()).getArgs();
					}
					else{
						args=(Arguments)tmpParentNode.getSemanticElement();
					}
					return args;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Given the current position in the editing area, tries
	 * to recover the corresponding actual node.
	 * 
	 * @return the actual node, <code>null</code> if not possible
	 */
	public ICompositeNode getActualNode(){
		if(xtextAdapter.getXtextParseResult()!=null){
			int caretOffset = textArea.getCaretOffset();
			EObjectAtOffsetHelper eobjHelper=new EObjectAtOffsetHelper();
			EObject resolvedEObj = eobjHelper.resolveElementAt(xtextAdapter.getFakeResourceContext().getFakeResource(), caretOffset);
			return NodeModelUtils.findActualNodeFor(resolvedEObj);
		}
		return null;
	}
	
	/**
	 * Given the current position in the editing area, tries
	 * to recover the corresponding actual node offset.
	 * 
	 * @return the actual node offset, <code>-1</code> if the actual node is not available
	 */
	public int getActualNodeOffset(){
		ICompositeNode actualNode = getActualNode();
		return actualNode!=null ? actualNode.getOffset() : -1;
	}
	
	/**
	 * Inserts new text in the editing area and if specified select also 
	 * the newly inserted text.
	 * 
	 * @param partialExpression the text string to enter in the editing area
	 * @param selectNewText applies or not the selection of the newly inserted text
	 */
	public void insertAtCurrentLocation(String partialExpression,boolean selectNewText){
		int start=textArea.getSelection().x;
		int end=start;
		textArea.insert(partialExpression);
		if(selectNewText){
			end=start+partialExpression.length();
		}
		textArea.setSelection(start, end);
	}
	
	/**
	 * Toggles the update status.
	 * 
	 * @param update flag for update operation status
	 */
	public void setUpdate(boolean update){
		this.update=update;
	}
	
	/**
	 * @return 
	 * 		<code>true</code> if an update operation is currently being performed, 
	 * 		<code>false</code> otherwise
	 */
	public boolean isUpdate(){
		return this.update;
	}
	
	/**
	 * Remove an existing category selection listener.
	 * 
	 * @param listener the listener to be removed
	 */
	public void removeCategorySelectionListener(ObjectCategorySelectionListener listener){
		categorySelectionListeners.remove(listener);
	}
	
	/**
	 * Adds a new category selection listener.
	 * 
	 * @param listener the listener to be added
	 */
	public void addCategorySelectionListener(ObjectCategorySelectionListener listener){
		categorySelectionListeners.add(listener);
	}

	/**
	 * Notifies the selection of a new category.
	 * 
	 * @param selectionEvent the selection event containing the information on the selected category
	 */
	public void notifyCategorySelection(
			ObjectCategorySelectionEvent selectionEvent) {
		for (ObjectCategorySelectionListener l : categorySelectionListeners){
			l.select(selectionEvent);
		}
	}
	
	/**
	 * Enables/disables the support for the auto edit strategies of the
	 * Xtext viewer associated to the editing area.
	 * 
	 * @param ignore flag to determine if the auto edit strategies must be ignored
	 */
	public void ignoreAutoEditStrategies(boolean ignore){
		xtextAdapter.ignoreAutoEditStrategies(ignore);
	}
}
