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
package com.jaspersoft.studio.model.style.command;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.model.style.MStyles;
import com.jaspersoft.studio.utils.ModelUtils;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteStyleCommand extends Command {

	/** The jr design. */
	private JasperDesign jrDesign;

	/** The jr style. */
	private JRDesignStyle jrStyle;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * When a style is removed is keeped trace of the element and styles
	 * that was using that style. So on the undo it is possibile to restore their
	 * values
	 */
	private List<JRDesignElement> elementsUsingStyle = null;
	
	private List<JRDesignStyle> stylesUsingStyle = null;
	
	/**
	 * Instantiates a new delete style command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteStyleCommand(MStyles destNode, MStyle srcNode) {
		super();
		this.jrDesign = srcNode.getJasperDesign();
		this.jrStyle = (JRDesignStyle) srcNode.getValue();
	}
	
	public DeleteStyleCommand(JasperDesign design, JRDesignStyle style) {
		this.jrDesign = design;
		this.jrStyle = style;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrDesign.getStylesList().indexOf(jrStyle);
		jrDesign.removeStyle(jrStyle);
		elementsUsingStyle = new ArrayList<JRDesignElement>();
		for(JRDesignElement element : ModelUtils.getAllElements(jrDesign)){
			if (jrStyle.equals(element.getStyle())){
				elementsUsingStyle.add(element);
				element.setStyle(null);
			}
		}
		stylesUsingStyle = new ArrayList<JRDesignStyle>();
		for(JRStyle style : jrDesign.getStyles()){
			if (jrStyle.equals(style.getStyle()) && style instanceof JRDesignStyle){
				JRDesignStyle baseStyle = (JRDesignStyle)style;
				stylesUsingStyle.add(baseStyle);
				baseStyle.setParentStyle(null);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrDesign == null || jrStyle == null || elementsUsingStyle == null || stylesUsingStyle == null)
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
		try {
			if (elementPosition < 0 || elementPosition > jrDesign.getStylesList().size())
				jrDesign.addStyle(jrStyle);
			else
				jrDesign.addStyle(elementPosition, jrStyle);
			for(JRDesignElement element : elementsUsingStyle){
				element.setStyle(jrStyle);
			}
			for(JRDesignStyle style : stylesUsingStyle){
				style.setParentStyle(jrStyle);
			}
			elementsUsingStyle = null;
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
