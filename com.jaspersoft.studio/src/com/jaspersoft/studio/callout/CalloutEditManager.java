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
package com.jaspersoft.studio.callout;

import net.sf.jasperreports.engine.base.JRBaseFont;

import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.tools.CellEditorLocator;
import org.eclipse.gef.tools.DirectEditManager;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.CellEditorActionHandler;

public class CalloutEditManager extends DirectEditManager {
	private IActionBars actionBars;
	private MCallout textElement;
	private CellEditorActionHandler actionHandler;
	private IAction copy, cut, paste, undo, redo, find, selectAll, delete;
	// private double cachedZoom = -1.0;
	private Font scaledFont;
	private ZoomListener zoomListener = new ZoomListener() {
		public void zoomChanged(double newZoom) {
			updateScaledFont(newZoom, textElement);
		}
	};

	public CalloutEditManager(GraphicalEditPart source, CellEditorLocator locator) {
		super(source, null, locator);
	}

	/** * @see org.eclipse.gef.tools.DirectEditManager#bringDown() */
	protected void bringDown() {
		ZoomManager zoomMgr = (ZoomManager) getEditPart().getViewer().getProperty(ZoomManager.class.toString());
		if (zoomMgr != null)
			zoomMgr.removeZoomListener(zoomListener);
		if (actionHandler != null) {
			actionHandler.dispose();
			actionHandler = null;
		}
		if (actionBars != null) {
			restoreSavedActions(actionBars);
			actionBars.updateActionBars();
			actionBars = null;
		}
		super.bringDown();
		// dispose any scaled fonts that might have been created disposeScaledFont();
	}

	protected CellEditor createCellEditorOn(Composite composite) {
		return new TextCellEditor(composite, SWT.MULTI | SWT.WRAP);
	}

	public void dispose() {
		disposeScaledFont();
	}

	private void disposeScaledFont() {
		if (scaledFont != null) {
			scaledFont.dispose();
			scaledFont = null;
		}
	}

	protected void initCellEditor() {
		// update text

		try {
			MCallout model = ((CalloutEditPart) getEditPart()).getModel();
			getCellEditor().setValue(model.getPropertyValue(MCallout.PROP_TEXT));
			getCellEditor().getControl().setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

			// update font
			ZoomManager zoomMgr = (ZoomManager) getEditPart().getViewer().getProperty(ZoomManager.class.toString());
			if (zoomMgr != null) { // this will force the font to be set cachedZoom = -1.0;
				updateScaledFont(zoomMgr.getZoom(), model);
				zoomMgr.addZoomListener(zoomListener);
			} // else
				// getCellEditor().getControl().setFont(stickyNote.getFont());
			// Hook the cell editor's copy/paste actions to the actionBars so that they can
			// be invoked via keyboard
			// shortcuts.
			actionBars = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
					.getEditorSite().getActionBars();
			saveCurrentActions(actionBars);
			actionHandler = new CellEditorActionHandler(actionBars);
			actionHandler.addCellEditor(getCellEditor());
			actionBars.updateActionBars();

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void restoreSavedActions(IActionBars actionBars) {
		actionBars.setGlobalActionHandler(ActionFactory.COPY.getId(), copy);
		actionBars.setGlobalActionHandler(ActionFactory.PASTE.getId(), paste);
		actionBars.setGlobalActionHandler(ActionFactory.DELETE.getId(), delete);
		actionBars.setGlobalActionHandler(ActionFactory.SELECT_ALL.getId(), selectAll);
		actionBars.setGlobalActionHandler(ActionFactory.CUT.getId(), cut);
		actionBars.setGlobalActionHandler(ActionFactory.FIND.getId(), find);
		actionBars.setGlobalActionHandler(ActionFactory.UNDO.getId(), undo);
		actionBars.setGlobalActionHandler(ActionFactory.REDO.getId(), redo);
	}

	private void saveCurrentActions(IActionBars actionBars) {
		copy = actionBars.getGlobalActionHandler(ActionFactory.COPY.getId());
		paste = actionBars.getGlobalActionHandler(ActionFactory.PASTE.getId());
		delete = actionBars.getGlobalActionHandler(ActionFactory.DELETE.getId());
		selectAll = actionBars.getGlobalActionHandler(ActionFactory.SELECT_ALL.getId());
		cut = actionBars.getGlobalActionHandler(ActionFactory.CUT.getId());
		find = actionBars.getGlobalActionHandler(ActionFactory.FIND.getId());
		undo = actionBars.getGlobalActionHandler(ActionFactory.UNDO.getId());
		redo = actionBars.getGlobalActionHandler(ActionFactory.REDO.getId());
	}

	private void updateScaledFont(double zoom, MCallout model) {
		// if (cachedZoom == zoom)
		// return;

		int fontSize = 1;
		Object fontSizeValue = model == null ? null : model.getPropertyValue(JRBaseFont.PROPERTY_FONT_SIZE);

		try {
			if (fontSizeValue != null) {
				if (fontSizeValue instanceof String) {
					fontSize = Integer.parseInt((String) fontSizeValue);
				} else if (fontSizeValue instanceof Integer) {
					fontSize = ((Integer) fontSizeValue).intValue();
				}

			}
		} catch (Exception ex) {

		}

		String fontName = model == null ? null : (String) model.getPropertyValue(JRBaseFont.PROPERTY_FONT_NAME);

		Text text = (Text) getCellEditor().getControl();
		Font font = getEditPart().getFigure().getFont();

		disposeScaledFont();

		try {
			FontData fd = font.getFontData()[0];
			fd.setHeight((int) (zoom * fontSize * 0.75)); // fd.getHeight()
			if (fontName != null && fontName.length() > 0) {
				fd.setName(fontName);
			}
			text.setFont(scaledFont = new Font(null, fd));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
