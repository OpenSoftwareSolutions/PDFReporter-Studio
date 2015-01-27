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
package com.jaspersoft.studio.editor.action.band;

import java.util.List;

import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.ModelUtils;

public class MaximizeContainerAction extends SelectionAction {

	/** The Constant ID. */
	public static final String ID = "maximizecontainer"; //$NON-NLS-1$

	/**
	 * Constructs a <code>CreateAction</code> using the specified part.
	 * 
	 * @param part
	 *          The part for this action
	 */
	public MaximizeContainerAction(IWorkbenchPart part) {
		super(part);
		setLazyEnablementCalculation(true);
		setText(Messages.MaximizeContainerAction_name);
		setToolTipText(Messages.MaximizeContainerAction_tooltip);
		setId(ID);
		// setImageDescriptor(JaspersoftStudioPlugin.getImageDescriptor(layout.getIcon()));
		// setDisabledImageDescriptor(JaspersoftStudioPlugin.getImageDescriptor(layout.getIcon()));
		setEnabled(false);
	}

	/**
	 * Returns <code>true</code> if the selected objects can be created. Returns <code>false</code> if there are no
	 * objects selected or the selected objects are not {@link EditPart}s.
	 * 
	 * @return if the command should be enabled
	 */
	protected boolean calculateEnabled() {
		Command cmd = createReorderCommand(getSelectedObjects());
		if (cmd == null)
			return false;
		return cmd.canExecute();
	}

	/**
	 * Create a command to create the selected objects.
	 * 
	 * @param objects
	 *          The objects to be deleted.
	 * @return The command to remove the selected objects.
	 */
	public Command createReorderCommand(List<?> objects) {
		if (objects == null || objects.isEmpty())
			return null;
		Object obj = objects.get(0);
		if (obj instanceof EditPart) {
			ANode n = (ANode) ((EditPart) obj).getModel();
			if (n instanceof MPage) {
				for (INode c : n.getChildren()) {
					if (c instanceof MGraphicElement) {
						n = (ANode) c;
						break;
					}
				}
			}
			if (!(n instanceof IGraphicElement))
				return null;

			JRElementGroup container = getContainer(n);
			if (container == null)
				return null;

			APropertyNode mcontainer = getContainerNode(n);
			JSSCompoundCommand cc = new JSSCompoundCommand(getText(), mcontainer);
			if (container instanceof JRDesignBand) {
				int bandHeight = ModelUtils.getMaxBandHeight((JRDesignBand) container, mcontainer.getJasperDesign());
				if (bandHeight > 0) {
					SetValueCommand cmd = new SetValueCommand();
					cmd.setTarget(mcontainer);
					cmd.setPropertyId(JRDesignBand.PROPERTY_HEIGHT);
					cmd.setPropertyValue(bandHeight);
					cc.add(cmd);
				}
			}
			return cc;
		}
		return null;
	}

	private JRElementGroup getContainer(ANode n) {
		if (n != null){
			Object val = n.getValue();
			if (n instanceof IGroupElement)
				return ((IGroupElement) n).getJRElementGroup();
			if (val instanceof JRElementGroup)
				return (JRElementGroup) val;
			if (val instanceof JRDesignElement)
				return getContainer(n.getParent());
		}
		return null;
	}

	private APropertyNode getContainerNode(ANode n) {
		Object val = n.getValue();
		if (n instanceof IGroupElement)
			return (APropertyNode) n;
		if (val instanceof JRElementGroup) {
			if(n instanceof MElementGroup) {
				return getContainerNode(n.getParent());
			}
			else {
				return (APropertyNode) n;
			}
		}
		if (val instanceof JRDesignElement)
			return getContainerNode(n.getParent());
		return null;
	}

	/**
	 * Performs the create action on the selected objects.
	 */
	public void run() {
		execute(createReorderCommand(getSelectedObjects()));
	}

}
