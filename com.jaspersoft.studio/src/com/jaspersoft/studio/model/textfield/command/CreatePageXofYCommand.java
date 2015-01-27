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
package com.jaspersoft.studio.model.textfield.command;

import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JRDesignTextField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.command.CreateElementCommand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.model.textfield.MPageNumber;
import com.jaspersoft.studio.model.textfield.MPageXofY;
import com.jaspersoft.studio.model.textfield.MTotalPages;
import com.jaspersoft.studio.utils.SelectionHelper;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreatePageXofYCommand extends CreateElementCommand {

	/** The jr design. */
	private JasperDesign jrDesign;
	private JRDesignTextField tfPageTotal;
	private JRDesignTextField tfPageNumber;

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreatePageXofYCommand(MElementGroup destNode, MPageXofY srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreatePageXofYCommand(MFrame destNode, MPageXofY srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreatePageXofYCommand(MBand destNode, MPageXofY srcNode, int index) {
		super(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the page xof y command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param position
	 *          the position
	 * @param index
	 *          the index
	 */
	public CreatePageXofYCommand(ANode destNode, MPageXofY srcNode, Rectangle position, int index) {
		super(destNode, srcNode, position, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.command.CreateElementCommand#setContext(com.jaspersoft.studio.model.ANode,
	 * com.jaspersoft.studio.model.MGeneric, int)
	 */
	@Override
	protected void setContext(ANode destNode, MGraphicElement srcNode, int index) {
		super.setContext(destNode, srcNode, index);
		jrDesign = destNode.getJasperDesign();
	}

	/**
	 * Creates the object.
	 */
	@Override
	protected void createObject() {
		Rectangle location = getLocation();
		if (location == null) {
			if (jrElement != null)
				location = new Rectangle(jrElement.getX(), jrElement.getY(), jrElement.getWidth(), jrElement.getHeight());
			else
				location = new Rectangle(0, 0, 100, 50);
		}
		if (location.width < 0)
			location.width = srcNode.getDefaultWidth() * 2;
		if (location.height < 0)
			location.height = srcNode.getDefaultHeight();

		int index = getIndex();
		JRElementGroup jrGroup = getJrGroup();
		if (index < 0)
			index = jrGroup.getChildren().size();

		// CREATE 2 TEXTFIELDS
		MPageNumber mPageNumber = new MPageNumber();
		tfPageNumber = mPageNumber.createJRElement(jrDesign);
		tfPageNumber.setX(location.x);
		tfPageNumber.setY(location.y);
		tfPageNumber.setWidth(location.width / 2);
		tfPageNumber.setHeight(location.height);
		tfPageNumber.setHorizontalAlignment(HorizontalAlignEnum.RIGHT);
		tfPageNumber.setEvaluationTime(EvaluationTimeEnum.NOW);
		JRDesignExpression expression = new JRDesignExpression();
		expression.setText(Messages.CreatePageXofYCommand_0);
		tfPageNumber.setExpression(expression);

		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).addElement(index, tfPageNumber);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).addElement(index, tfPageNumber);

		// CREATE SECOND TEXT FIELD
		MTotalPages mTotalPages = new MTotalPages();
		tfPageTotal = mTotalPages.createJRElement(jrDesign);
		tfPageTotal.setX(location.x + tfPageNumber.getWidth());
		tfPageTotal.setY(location.y);
		tfPageTotal.setWidth(location.width / 2);
		tfPageTotal.setHeight(location.height);
		tfPageTotal.setHorizontalAlignment(HorizontalAlignEnum.LEFT);
		tfPageTotal.setEvaluationTime(EvaluationTimeEnum.REPORT);
		expression = new JRDesignExpression();
		expression.setText(Messages.CreatePageXofYCommand_1);
		tfPageTotal.setExpression(expression);

		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).addElement(index + 1, tfPageTotal);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).addElement(index + 1, tfPageTotal);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.command.CreateElementCommand#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (firstTime) {
			SelectionHelper.setSelection(tfPageNumber, false);
			firstTime = false;
		}
	}

	private boolean firstTime = true;

	@Override
	public void undo() {
		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).removeElement(tfPageNumber);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).removeElement(tfPageNumber);

		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).removeElement(tfPageTotal);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).removeElement(tfPageTotal);
	}
}
