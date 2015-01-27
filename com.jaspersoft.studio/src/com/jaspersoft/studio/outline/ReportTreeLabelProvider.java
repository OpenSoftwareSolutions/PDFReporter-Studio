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
package com.jaspersoft.studio.outline;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.model.INode;

/*
 * The Class ReportTreeLabelProvider.
 * 
 * @author Chicu Veaceslav
 */
public class ReportTreeLabelProvider extends StyledCellLabelProvider implements IStyledLabelProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellLabelProvider#getToolTipText(java.lang.Object)
	 */
	@Override
	public String getToolTipText(Object element) {
		return ((INode) element).getToolTip();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipDisplayDelayTime(java.lang.Object)
	 */
	public int getToolTipDisplayDelayTime(Object object) {
		return 200;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ViewerLabelProvider#getTooltipTimeDisplayed(java.lang.Object)
	 */
	public int getToolTipTimeDisplayed(Object object) {
		return 5000;
	}

	/*
	 * @see ILabelProvider#getImage(Object)
	 */
	/**
	 * Gets the image.
	 * 
	 * @param element
	 *          the element
	 * @return the image
	 */
	public Image getImage(Object element) {
		ImageDescriptor imagePath = ((INode) element).getImagePath();
		if (imagePath == null)
			return null;
		return JaspersoftStudioPlugin.getInstance().getImage(imagePath);
	}

	/*
	 * @see ILabelProvider#getText(Object)
	 */
	/**
	 * Gets the text.
	 * 
	 * @param element
	 *          the element
	 * @return the text
	 */
	public String getText(Object element) {
		if (element instanceof INode)
			return ((INode) element).getDisplayText();
		return "UNKNOWN ELEMENT"; //$NON-NLS-1$
	}

	/**
	 * Gets the foreground.
	 * 
	 * @param element
	 *          the element
	 * @return the foreground
	 */
	private Color getForeground(Object element) {
		if (element instanceof INode)
			return ((INode) element).getForeground();
		return null;
	}

	/**
	 * Gets the background.
	 * 
	 * @param element
	 *          the element
	 * @return the background
	 */
	private Color getBackground(Object element) {
		if (element instanceof INode)
			return ((INode) element).getBackground();
		return null;
	}

	/**
	 * Gets the font.
	 * 
	 * @param element
	 *          the element
	 * @return the font
	 */
	private Font getFont(Object element) {
		if (element instanceof INode)
			return ((INode) element).getFont();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.CellLabelProvider#update(org.eclipse.jface.viewers.ViewerCell)
	 */
	@Override
	public void update(ViewerCell cell) {
		try {
			Object element = cell.getElement();
			StyledString st = getStyledText(element);
			cell.setText(st.getString());
			cell.setStyleRanges(getStyledText(element).getStyleRanges());
			cell.setImage(getImage(element));
			cell.setBackground(getBackground(element));
			cell.setForeground(getForeground(element));
			cell.setFont(getFont(element));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public StyledString getStyledText(Object element) {
		if (element instanceof INode)
			return ((INode) element).getStyledDisplayText();
		return new StyledString("UNKNOWN ELEMENT"); //$NON-NLS-1$
	}

}
