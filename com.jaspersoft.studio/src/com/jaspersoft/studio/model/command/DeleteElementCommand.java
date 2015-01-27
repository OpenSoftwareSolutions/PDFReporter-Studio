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
package com.jaspersoft.studio.model.command;

import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutCommand;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.MGraphicElement;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteElementCommand extends Command {
	private JasperDesign jDesign;
	/** The jr group. */
	private JRElementGroup jrGroup;

	/** The jr element. */
	private JRDesignElement jrElement;

	/** The element position. */
	private int elementPosition = 0;
	private JRPropertiesHolder[] pholder;

	/**
	 * Instantiates a new delete element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteElementCommand(ANode destNode, MGraphicElement srcNode) {
		super();
		jrElement = (JRDesignElement) srcNode.getValue();
		jrGroup = jrElement.getElementGroup();
		jDesign = srcNode.getJasperDesign();
		ANode parent = srcNode.getParent();
		if (parent instanceof IContainerLayout)
			pholder = ((IContainerLayout) parent).getPropertyHolder();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrGroup != null && jrGroup.getChildren() != null) {
			elementPosition = jrGroup.getChildren().indexOf(jrElement);
			if (jrGroup instanceof JRDesignElementGroup) {
				((JRDesignElementGroup) jrGroup).removeElement(jrElement);
			} else if (jrGroup instanceof JRDesignFrame) {
				((JRDesignFrame) jrGroup).removeElement(jrElement);
			}
			if (jrGroup instanceof JRPropertiesHolder) {
				String uuid = null;
				if (jrGroup instanceof JRBaseElement)
					uuid = ((JRBaseElement) jrGroup).getUUID().toString();
				Dimension d = new Dimension(0, 0);
				if (jrGroup instanceof JRCommonElement) {
					JRCommonElement jce = (JRCommonElement) jrGroup;
					// Commented for back-compatibility in 3.6.
					// Replaced with the following line.
					// d.setSize(jce.getWidth(), jce.getHeight());
					d.setSize(new Dimension(jce.getWidth(), jce.getHeight()));
				}
				if (jrGroup instanceof JRDesignBand) {
					int w = jDesign.getPageWidth() - jDesign.getLeftMargin() - jDesign.getRightMargin();
					// Commented for back-compatibility in 3.6.
					// Replaced with the following line.
					// d.setSize(w, ((JRDesignBand) jrGroup).getHeight());
					d.setSize(new Dimension(w, ((JRDesignBand) jrGroup).getHeight()));
				}
				if (lCmd == null) {
					ILayout layout = LayoutManager.getLayout(pholder, jDesign, uuid);
					lCmd = new LayoutCommand(jrGroup, layout, d);
					lCmd.execute();
				}
			}
		}
	}

	private LayoutCommand lCmd;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrGroup == null || jrElement == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (lCmd != null)
			lCmd.undo();
		if (jrGroup != null && jrGroup.getChildren() != null) {
			if (jrGroup instanceof JRDesignElementGroup) {
				if (elementPosition > ((JRDesignElementGroup) jrGroup).getChildren().size())
					((JRDesignElementGroup) jrGroup).addElement(jrElement);
				else
					((JRDesignElementGroup) jrGroup).addElement(elementPosition, jrElement);
			} else if (jrGroup instanceof JRDesignFrame) {
				if (elementPosition > ((JRDesignFrame) jrGroup).getChildren().size())
					((JRDesignFrame) jrGroup).addElement(jrElement);
				else
					((JRDesignFrame) jrGroup).addElement(elementPosition, jrElement);
			}
		}
	}
}
