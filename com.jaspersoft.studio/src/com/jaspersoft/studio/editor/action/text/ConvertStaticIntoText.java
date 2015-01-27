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
package com.jaspersoft.studio.editor.action.text;

import java.util.List;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRParagraph;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignTextField;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.action.ACachedSelectionAction;
import com.jaspersoft.studio.editor.gef.parts.text.StaticTextFigureEditPart;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.command.DeleteElementCommand;
import com.jaspersoft.studio.model.text.MStaticText;
import com.jaspersoft.studio.model.text.MTextField;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * 
 * Action to convert a Static Text element into a text field element. All the common attributes are 
 * maintained in the conversion
 * 
 * @author Orlandin Marco
 *
 */
public class ConvertStaticIntoText extends ACachedSelectionAction {

	/**
	 * Wrapper for the CreateElementCommand. This command allow to generate
	 * the commands that will be executed but without generating also the new elements.
	 * In this way the new elements are created only when the command is executed
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class LazyCreateTextFieldCommand extends Command{
		
		/**
		 * The executed create command
		 */
		private CreateElementCommand cmd = null;
		
		/**
		 * The element to copy
		 */
		private MStaticText elementToCopy;
		
		/**
		 * The parent of the converted node
		 */
		private ANode parent;
		
		public LazyCreateTextFieldCommand(MStaticText elementToCopy){
			this.elementToCopy = elementToCopy;
			//Need to store some values because if the copied node is deleted
			//its parent is no longer reachable
			this.parent = elementToCopy.getParent();
		}
		
		@Override
		public void execute() {
			MTextField modelText = new MTextField();
			
			JRDesignStaticText labelObject = (JRDesignStaticText)elementToCopy.getValue();
			JRDesignTextField textObject =  (JRDesignTextField)modelText.createJRElement(elementToCopy.getJasperDesign());

			cloneTextField(textObject, labelObject);
			
			modelText.setValue(textObject);
			Rectangle position = new Rectangle(labelObject.getX(),labelObject.getY(),labelObject.getWidth(),labelObject.getHeight());

			int oldIndex = ModelUtils.getChildrenPosition(elementToCopy);
			cmd = new CreateElementCommand(parent, modelText, position, oldIndex);
			cmd.setJasperDesign(parent.getJasperDesign());
			cmd.execute();
		}
		
		@Override
		public void undo() {
			cmd.undo();
			cmd = null;
		}
		
		@Override
		public boolean canExecute() {
			return elementToCopy != null && parent != null;
		}
		
		@Override
		public boolean canUndo() {
			return cmd != null;
		}
	}
	
	/**
	 * The id of the action
	 */
	public static final String ID = "ConvertStaticIntoText"; 
	
	public ConvertStaticIntoText(IWorkbenchPart part) {
		super(part);
		setId(ID);
		setText(Messages.ConvertStaticIntoText_actionName);
		setToolTipText(Messages.ConvertStaticIntoText_actionTooltip);
		setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/convert_to_field.png")); //$NON-NLS-1$
	}

	/**
	 * Check if the text into the static text seems to be valid for an expression
	 * 
	 * @param value text into the static text
	 * @return true if the text could be an expression, otherwise false
	 */
	private boolean isValidExpression(String value){
		if (value.startsWith("\"") && value.endsWith("\"")) return true; 
		if (value.startsWith("$P{") && value.endsWith("}")) return true; 
		if (value.startsWith("$V{") && value.endsWith("}")) return true; 
		if (value.startsWith("$R{") && value.endsWith("}")) return true;
		if (value.startsWith("$F{") && value.endsWith("}")) return true; 
		return false;
	}
	
	/**
	 * Copy the box section from the a linebox to another
	 * 
	 * @param fieldBox destination
	 * @param staticBox source
	 */
	private void cloneBox(JRLineBox fieldBox, JRLineBox staticBox){
		if (fieldBox == null || staticBox == null) return;
		fieldBox.setBottomPadding(staticBox.getBottomPadding());
		fieldBox.setLeftPadding(staticBox.getLeftPadding());
		fieldBox.setPadding(staticBox.getPadding());
		fieldBox.setRightPadding(staticBox.getRightPadding());
		fieldBox.setTopPadding(staticBox.getTopPadding());
		
		fieldBox.copyTopPen(staticBox.getTopPen());
		fieldBox.copyBottomPen(staticBox.getBottomPen());
		fieldBox.copyLeftPen(staticBox.getLeftPen());
		fieldBox.copyRightPen(staticBox.getRightPen());
		fieldBox.copyPen(staticBox.getPen());
	}
	
	private void cloneParagraph(JRParagraph fieldBox, JRParagraph staticBox){
			if (fieldBox == null || staticBox == null) return;
			fieldBox.setFirstLineIndent(staticBox.getFirstLineIndent());
			fieldBox.setLeftIndent(staticBox.getLeftIndent());
			fieldBox.setLineSpacing(staticBox.getLineSpacing());
			fieldBox.setLineSpacingSize(staticBox.getLineSpacingSize());
			fieldBox.setRightIndent(staticBox.getRightIndent());
			fieldBox.setSpacingAfter(staticBox.getSpacingAfter());
			fieldBox.setSpacingBefore(staticBox.getSpacingBefore());
			fieldBox.setTabStopWidth(staticBox.getTabStopWidth());
	}
	
	/**
	 * Copy all the common attributes from the static text to the new text field element 
	 * 
	 * @param textObject the new text field element, that will substitute the static text
	 * @param labelObject the substituted static text
	 */
	private void cloneTextField(JRDesignTextField textObject, JRDesignStaticText labelObject)
	{
		String staticTextValue = labelObject.getText();
		//If the text is not valid for an expression it will be handled as a string
		if (!isValidExpression(staticTextValue)){
			if (!staticTextValue.startsWith("\"")) staticTextValue = "\"".concat(staticTextValue); //$NON-NLS-1$ //$NON-NLS-2$
			if (!staticTextValue.endsWith("\"")) staticTextValue = staticTextValue.concat("\""); //$NON-NLS-1$ //$NON-NLS-2$
		}
		textObject.setExpression(ExprUtil.setValues(textObject.getExpression(), staticTextValue));
		
		textObject.setHeight(labelObject.getHeight());
		textObject.setWidth(labelObject.getWidth());
		textObject.setX(labelObject.getX());
		textObject.setY(labelObject.getY());
		textObject.setFontName(labelObject.getFontName());
		textObject.setFontSize(labelObject.getFontsize());
		textObject.setBackcolor(labelObject.getBackcolor()); 
		textObject.setForecolor(labelObject.getForecolor());
		
		JRStyle originStyle = labelObject.getStyle();
		textObject.setStyle(originStyle != null ? (JRStyle)originStyle.clone() : null);
		
		textObject.setStyleNameReference(labelObject.getStyleNameReference());
		textObject.setBold(labelObject.isBold());
		textObject.setItalic(labelObject.isItalic());
		textObject.setUnderline(labelObject.isUnderline());
		textObject.setStrikeThrough(labelObject.isStrikeThrough());
		textObject.setHorizontalAlignment(labelObject.getHorizontalAlignmentValue());
		textObject.setVerticalAlignment(labelObject.getVerticalAlignmentValue());
		textObject.setMode(labelObject.getModeValue());
		textObject.setRotation(labelObject.getRotationValue());
		textObject.setStretchType(labelObject.getStretchTypeValue());
		textObject.setKey(labelObject.getKey());
		textObject.setMarkup(labelObject.getMarkup());
		textObject.setPdfEmbedded(labelObject.isPdfEmbedded());
		textObject.setPdfEncoding(labelObject.getPdfEncoding());
		textObject.setPdfFontName(labelObject.getPdfFontName());
		textObject.setPositionType(labelObject.getPositionTypeValue());
		textObject.setPrintInFirstWholeBand(labelObject.isPrintInFirstWholeBand());
		textObject.setPrintRepeatedValues(labelObject.isPrintRepeatedValues());
		textObject.setPrintWhenDetailOverflows(labelObject.isPrintWhenDetailOverflows());
		
		cloneBox(textObject.getLineBox(),labelObject.getLineBox());

		cloneParagraph(textObject.getParagraph(), labelObject.getParagraph());
		
		JRExpression originExpression = labelObject.getPrintWhenExpression();
		textObject.setPrintWhenExpression(originExpression != null ? (JRExpression)originExpression.clone() : null);
		
		JRGroup originGroup = labelObject.getPrintWhenGroupChanges();
		textObject.setPrintWhenGroupChanges(originGroup != null ? (JRGroup)originGroup.clone() : null);
		
		textObject.setRemoveLineWhenBlank(labelObject.isRemoveLineWhenBlank());
	}
	
	/**
	 * Create the commands to create a new text field similar to the static text selected
	 * and to delete the static text
	 * 
	 * @return a compound command with two commands in it, one to remove the selected static texts, and one to 
	 * create in their place similar text fields
	 */
	@Override
	protected Command createCommand() {
		List<Object> editparts = editor.getSelectionCache().getSelectionPartForType(StaticTextFigureEditPart.class);
		if (editparts.isEmpty())
			return null;
		JSSCompoundCommand command = new JSSCompoundCommand(null);
		for(Object part : editparts){
			StaticTextFigureEditPart editPart = (StaticTextFigureEditPart)part;
			MStaticText staticText = (MStaticText)editPart.getModel();

			command.setReferenceNodeIfNull(staticText);
			
			DeleteElementCommand deleteCommand = new DeleteElementCommand(null, staticText);
			
			LazyCreateTextFieldCommand createCommand = new LazyCreateTextFieldCommand(staticText);
			
			command.add(deleteCommand);
			command.add(createCommand);
		}
		return command;
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		execute(createCommand());
	}
}
