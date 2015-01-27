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
package com.jaspersoft.studio.editor.gef.parts;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGeometry;

import com.jaspersoft.studio.editor.gef.parts.band.BandEditPart;

/**
 * Extend the original SnapToGeometry to give the possibility to set the threshold of snap
 * and to change the indexing of the items
 * @author Orlandin Marco
 *
 */
public class SnapToGeometryThreshold extends SnapToGeometry{

	private static JasperDesign jd = null;
	
	private static Rectangle clientArea = null;
	
	/**
	 * Extension necessary to use the protected class entry
	 *
	 */
	protected static class ThresholdEntry extends Entry{
		protected ThresholdEntry(int type, int location) {
			super(type, location);
		}

	}
		
	/**
	 * Initialize the class with the report design and client area bounds
	 * @param container
	 */
	public SnapToGeometryThreshold(GraphicalEditPart container) {
		super(container);
		if (container instanceof BandEditPart && clientArea == null){
			jd = ((BandEditPart)container).getJasperDesign();
			clientArea = ((ReportPageEditPart)container.getParent()).getFigure().getClientArea();
		}
	}
	
	
	/**
	 * Set A new threshold
	 */
	public void setThreshold(double value){
		super.setThreshold(value);
	}

	/**
	 * Add to the original method the snap points for the left and right margins.
	 * These margins are not included by default because they aren't editparts, but lines
	 * drawn on the edit part that represent the page
	 */
	@Override
	protected void populateRowsAndCols(List parts) {

		if (clientArea != null){
			rows = new Entry[(parts.size() * 3)+6];
			cols = new Entry[(parts.size() * 3)+6];
			//Build the Snap to the right and left margin
			Point topLeft = new Point(clientArea.x + jd.getLeftMargin(), clientArea.y);
			Point topRight = new Point(clientArea.x + jd.getPageWidth() - jd.getRightMargin(), clientArea.y);
			Point bottomLeft = new Point(topLeft.x, clientArea.y + jd.getPageHeight());
			Point bottomRight = new Point(topRight.x, clientArea.y + jd.getPageHeight());
			int startPost = (parts.size() * 3);
			cols[startPost] = new ThresholdEntry(-1, topLeft.x);
			rows[startPost] = new ThresholdEntry(-1, topLeft.y);
			cols[startPost + 1] = new ThresholdEntry(0, topLeft.x + (bottomLeft.x- 1) / 2);
			rows[startPost + 1] = new ThresholdEntry(0, topLeft.y + (bottomLeft.y - 1) / 2);
			cols[startPost + 2] = new ThresholdEntry(1, bottomLeft.x - 1);
			rows[startPost + 2] = new ThresholdEntry(1, bottomLeft.y - 1);
			startPost+=3;
			cols[startPost] = new ThresholdEntry(-1, topRight.x);
			rows[startPost] = new ThresholdEntry(-1, topRight.y);
			cols[startPost + 1] = new ThresholdEntry(0, topRight.x + (bottomRight.x- 1) / 2);
			rows[startPost + 1] = new ThresholdEntry(0, topRight.y + (bottomRight.y - 1) / 2);
			cols[startPost + 2] = new ThresholdEntry(1, bottomRight.x - 1);
			rows[startPost + 2] = new ThresholdEntry(1, bottomRight.y - 1);
		} else {
			rows = new Entry[(parts.size() * 3)];
			cols = new Entry[(parts.size() * 3)];
		}
			
		for (int i = 0; i < parts.size(); i++) {
			GraphicalEditPart child = (GraphicalEditPart) parts.get(i);
			Rectangle bounds = getFigureBounds(child);
			cols[i * 3] = new ThresholdEntry(-1, bounds.x);
			rows[i * 3] = new ThresholdEntry(-1, bounds.y);
			cols[i * 3 + 1] = new ThresholdEntry(0, bounds.x + (bounds.width - 1) / 2);
			rows[i * 3 + 1] = new ThresholdEntry(0, bounds.y + (bounds.height - 1) / 2);
			cols[i * 3 + 2] = new ThresholdEntry(1, bounds.right() - 1);
			rows[i * 3 + 2] = new ThresholdEntry(1, bounds.bottom() - 1);
		}

		
	}

	
	/**
	 * The list of element is based on the father of first selected item
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected List generateSnapPartsList(List exclusions) {
		// Don't snap to any figure that is being dragged
		List children = new ArrayList();
		if (exclusions.size()>0){
			EditPart selectedItem = (EditPart)exclusions.get(0);
			children.addAll(selectedItem.getParent().getChildren());
			children.removeAll(exclusions);
			// Don't snap to hidden figures
			List hiddenChildren = new ArrayList();
			for (Iterator iter = children.iterator(); iter.hasNext();) {
				GraphicalEditPart child = (GraphicalEditPart) iter.next();
				if (!child.getFigure().isVisible())
					hiddenChildren.add(child);
			}
			children.removeAll(hiddenChildren);
		}
		return children;
	}

}
