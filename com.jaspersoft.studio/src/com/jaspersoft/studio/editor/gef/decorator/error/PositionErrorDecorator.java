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
package com.jaspersoft.studio.editor.gef.decorator.error;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.ui.actions.RetargetAction;

import com.jaspersoft.studio.editor.gef.decorator.IDecorator;
import com.jaspersoft.studio.editor.gef.decorator.IElementDecorator;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MPage;
import com.jaspersoft.studio.model.band.MBand;

public class PositionErrorDecorator implements IElementDecorator {

	private IDecorator decorator = null;
	private List<String> actionIDs;

	@Override
	public void setupFigure(ComponentFigure fig, FigureEditPart editPart) {

		// It will remove the type ErrorDecorator from the list because equals is override in ErrorDecorator
		fig.removeDecorator(decorator);
		// check if we should show this decorator

		if (editPart.getjConfig().getPropertyBooleanDef(ShowErrorsAction.ID, false)) {
			if (decorator == null) {
				decorator = new ErrorDecorator();
			}
			if (!isValidPosition(fig, (MGraphicElement) editPart.getModel())) {
				fig.addDecorator(decorator);
			}
		}
	}

	/**
	 * Evaluate if an element is between the bounds of the father
	 * 
	 * @param fig
	 *          The moved graphical element.
	 * @param itemModel
	 *          the model associated to the moved item
	 * @return true if the element position is valid (so no warning should be shown), false otherwise
	 */
	private boolean isValidPosition(ComponentFigure fig, MGraphicElement itemModel) {
		if (itemModel.getParent() instanceof APropertyNode) {
			JRDesignElement item = itemModel.getValue();
			int x = item.getX();
			int y = item.getY();
			int w = item.getWidth();
			int h = item.getHeight();

			int fh = Integer.MAX_VALUE;
			int fw = Integer.MAX_VALUE;

			if (itemModel.getParent() instanceof MGraphicElement) {
				MGraphicElement fatherModel = (MGraphicElement) itemModel.getParent();
				JRDesignElement fitem = fatherModel.getValue();

				fh = fitem.getHeight();
				fw = fitem.getWidth();
			} else if (itemModel.getParent() instanceof MBand) {
				JRDesignBand band = ((MBand) itemModel.getParent()).getValue();
				fh = band.getHeight();
				fw = itemModel.getJasperDesign().getPageWidth();
			} else if (itemModel.getParent() instanceof MPage) {
				// I'm into a separate editor, here the relative dimensions inside the band dosen't count
				x = 0;
				y = 0;
				if (fig.getParent() != null) {
					Rectangle r = fig.getParent().getBounds();
					fh = r.height;
					fw = r.width;
				} else {
					MPage ge = (MPage) itemModel.getParent();
					JasperDesign jd = (JasperDesign) ge.getValue();
					fh = jd.getPageHeight();
					fw = jd.getPageWidth();
				}
			} else if (itemModel.getParent() instanceof IGraphicElement) {
				IGraphicElement ge = (IGraphicElement) itemModel.getParent();
				Rectangle r = ge.getBounds();
				if (r != null) {
					fh = r.height;
					fw = r.width;
				}
			}
			if (fig.getJrElement().getElementGroup() instanceof JRBand) {
				// Integer father_width = itemModel.getRoot().getJasperDesign().getColumnWidth();
				return y + h <= fh;
			} else if (fig.getJrElement().getElementGroup() != null) {
				return fh >= h + y && x >= 0 && y >= 0 && fw >= x + w;
			}
		}
		return true;
	}

	@Override
	public void registerActions(ActionRegistry registry, List<String> selectionActions, GraphicalViewer gviewer,
			AbstractVisualEditor part) {
		gviewer.setProperty(ShowErrorsAction.ID, true);
		IAction action = new ShowErrorsAction(gviewer, part.getJrContext());
		registry.registerAction(action);
	}

	@Override
	public void buildContextMenu(ActionRegistry registry, EditPartViewer viewer, IMenuManager menu) {
	}

	@Override
	public RetargetAction[] buildMenuActions() {
		return new RetargetAction[] { new RetargetAction(ShowErrorsAction.ID, Messages.ShowErrorsAction_title,
				IAction.AS_CHECK_BOX) };
	}

	@Override
	public void contribute2Menu(ActionRegistry registry, MenuManager menuManager) {
		menuManager.add(registry.getAction(ShowErrorsAction.ID));
	}

	@Override
	public List<String> getActionIDs() {
		if (actionIDs == null) {
			actionIDs = new ArrayList<String>(1);
			actionIDs.add(ShowErrorsAction.ID);
		}
		return actionIDs;
	}

}
