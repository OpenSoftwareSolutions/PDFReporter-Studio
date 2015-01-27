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
package com.jaspersoft.studio.data.designer.tree;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.model.ANode;

/**
 * Styled label provider that can highlight in bold the actual specified nodes. 
 * Nodes are supposed to be instances of {@link ANode}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class NodeBoldStyledLabelProvider<T extends ANode> extends StyledCellLabelProvider{
	private List<T> selectedNodes=new ArrayList<T>();
	
	@Override
	public void update(ViewerCell cell) {
		Object element = cell.getElement();
		StyledString text = new StyledString();
		
		if(element instanceof ANode){
			ANode node=(ANode)element;
			if(selectedNodes.contains(node)){
				// bold text
				final Font boldFont = ResourceManager.getBoldFont(getViewer().getControl().getFont());
				text.append(((ANode)element).getDisplayText(), new Styler() {
					@Override
					public void applyStyles(TextStyle textStyle) {
						textStyle.font=boldFont;
					}
				});
			}
			else{
				// normal text
				text.append(((ANode)element).getDisplayText());				
			}
		}
		else if(element instanceof CustomStyleStatus){
			// normal text
			text.append(((CustomStyleStatus) element).getMessage());
		}
		
		cell.setText(text.toString());
		cell.setStyleRanges(text.getStyleRanges());
		cell.setImage(getImage(element));
		super.update(cell);
	}

	private Image getImage(Object element) {
		if(element instanceof ANode){
			return ResourceManager.getImage(((ANode) element).getImagePath());
		}
		else if(element instanceof CustomStyleStatus){
			return ((CustomStyleStatus) element).getImage();
		}
		return null;
	}

	/**
	 * Notifies which are the nodes that currently selected ones.
	 * 
	 * @param selectedNodes the selected nodes
	 */
	public void setSelectedNodes(List<T> selectedNodes){
		this.selectedNodes.clear();
		this.selectedNodes.addAll(selectedNodes);
	}

	public interface CustomStyleStatus{
		String getMessage();
		Image getImage();
	}
}
