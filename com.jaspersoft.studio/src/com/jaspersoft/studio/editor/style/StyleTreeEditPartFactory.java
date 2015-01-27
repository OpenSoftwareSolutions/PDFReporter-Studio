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
package com.jaspersoft.studio.editor.style;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;

import com.jaspersoft.studio.editor.style.command.CreateStyleCommand;
import com.jaspersoft.studio.editor.style.command.CreateStyleTemplateReferenceCommand;
import com.jaspersoft.studio.editor.style.command.DeleteStyleCommand;
import com.jaspersoft.studio.editor.style.command.DeleteStyleTemplateCommand;
import com.jaspersoft.studio.editor.style.tree.StyleContainerTreeEditPart;
import com.jaspersoft.studio.editor.style.tree.StyleTreeEditPart;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.model.style.MStyleTemplateReference;
import com.jaspersoft.studio.model.style.MStylesTemplate;
/*
 * A factory for creating OutlineTreeEditPart objects.
 */
public class StyleTreeEditPartFactory implements EditPartFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPartFactory#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart editPart = null;
		if (model instanceof IContainerEditPart)
			editPart = new StyleContainerTreeEditPart();
		else
			editPart = new StyleTreeEditPart();
		if (editPart != null)
			editPart.setModel(model);
		return editPart;
	}

	/**
	 * Gets the delete command.
	 * 
	 * @param parent
	 *          the parent
	 * @param child
	 *          the child
	 * @return the delete command
	 */
	public static Command getDeleteCommand(ANode parent, ANode child) {
		if (parent instanceof MStylesTemplate) {
			if (child instanceof MStyleTemplateReference)
				return new DeleteStyleTemplateCommand((MStylesTemplate) parent, (MStyleTemplateReference) child);
			if (child instanceof MStyle)
				return new DeleteStyleCommand((MStylesTemplate) parent, (MStyle) child);
		}
		return null;
	}

	/**
	 * Gets the reorder command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 * @return the reorder command
	 */
	public static Command getReorderCommand(ANode child, ANode parent, int newIndex) {
		// if (child instanceof MStyle) {
		// if (parent instanceof MStyles) {
		// return new ReorderStyleCommand((MStyle) child, (MStyles) parent, newIndex);
		// }
		// }
		return null;
	}

	/**
	 * Gets the creates the command.
	 * 
	 * @param parent
	 *          the parent
	 * @param child
	 *          the child
	 * @param location
	 *          the location
	 * @param newIndex
	 *          the new index
	 * @return the creates the command
	 */
	public static Command getCreateCommand(ANode parent, ANode child, Rectangle location, int newIndex) {
		if (parent instanceof MStylesTemplate) {
			if (child instanceof MStyle)
				return new CreateStyleCommand((MStylesTemplate) parent, (MStyle) child, newIndex);
			if (child instanceof MStyleTemplateReference)
				return new CreateStyleTemplateReferenceCommand((MStylesTemplate) parent, (MStyleTemplateReference) child,
						newIndex);
		} else if (parent.getParent() instanceof MStylesTemplate) {
			if (child instanceof MStyle && !(child instanceof MConditionalStyle))
				return new CreateStyleCommand((MStylesTemplate) parent.getParent(), (MStyle) child, newIndex);
			if (child instanceof MStyleTemplateReference)
				return new CreateStyleTemplateReferenceCommand((MStylesTemplate) parent.getParent(),
						(MStyleTemplateReference) child, newIndex);
		}
		return null;
	}

	/**
	 * Gets the orphan command.
	 * 
	 * @param parent
	 *          the parent
	 * @param child
	 *          the child
	 * @return the orphan command
	 */
	public static Command getOrphanCommand(ANode parent, ANode child) {
		return UnexecutableCommand.INSTANCE;
	}
}
