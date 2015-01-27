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
package com.jaspersoft.studio.editor.gef.rulers.component;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.AccessibleEditPart;
import org.eclipse.gef.AccessibleHandleProvider;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editparts.ZoomListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.internal.ui.rulers.GuideFigure;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.swt.accessibility.AccessibleControlEvent;
import org.eclipse.swt.accessibility.AccessibleEvent;
import org.eclipse.swt.graphics.Cursor;

public class JDGuideEditPart extends AbstractGraphicalEditPart {

	public static final int MIN_DISTANCE_BW_GUIDES = 5;
	public static final int DELETE_THRESHOLD = 20;

	private AccessibleEditPart accPart;
	private GuideLineFigure guideLineFig;
	private Cursor cursor = null;
	private ZoomListener zoomListener = new ZoomListener() {
		public void zoomChanged(double zoom) {
			handleZoomChanged();
		}
	};
	private RulerChangeListener listener = new RulerChangeListener.Stub() {
		public void notifyGuideMoved(Object guide) {
			if (getModel() == guide) {
				handleGuideMoved();
			}
		}

		public void notifyPartAttachmentChanged(Object part, Object guide) {
			if (getModel() == guide) {
				handlePartAttachmentChanged(part);
			}
		}
	};

	public JDGuideEditPart(Object model) {
		setModel(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#activate()
	 */
	public void activate() {
		super.activate();
		getRulerProvider().addRulerChangeListener(listener);
		if (getZoomManager() != null)
			getZoomManager().addZoomListener(zoomListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new GuideSelectionPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new JDDragGuidePolicy());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	 */
	protected IFigure createFigure() {
		guideLineFig = createGuideLineFigure();
		getGuideLayer().add(getGuideLineFigure());
		getGuideLayer().setConstraint(getGuideLineFigure(), new Boolean(isHorizontal()));
		return new GuideFigure(isHorizontal());
	}

	protected GuideLineFigure createGuideLineFigure() {
		return new GuideLineFigure();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#deactivate()
	 */
	public void deactivate() {
		if (getZoomManager() != null)
			getZoomManager().removeZoomListener(zoomListener);
		getRulerProvider().removeRulerChangeListener(listener);
		if (getGuideLineFigure().getParent() != null)
			getGuideLineFigure().getParent().remove(getGuideLineFigure());
		super.deactivate();
	}

	protected AccessibleEditPart getAccessibleEditPart() {
		if (accPart == null)
			accPart = new AccessibleGraphicalEditPart() {
				public void getDescription(AccessibleEvent e) {
					if (getRulerProvider() != null)
						getRulerProvider().getAccGuideDescription(e, getModel());
				}

				public void getName(AccessibleEvent e) {
					if (getRulerProvider() != null)
						getRulerProvider().getAccGuideName(e, getModel());
				}

				public void getValue(AccessibleControlEvent e) {
					if (getRulerProvider() != null)
						getRulerProvider().getAccGuideValue(e, getModel());
				}
			};
		return accPart;
	}

	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		if (key == AccessibleHandleProvider.class) {
			return new AccessibleHandleProvider() {
				public List<?> getAccessibleHandleLocations() {
					List<Point> result = new ArrayList<Point>();
					Point pt = getFigure().getBounds().getCenter();
					getFigure().translateToAbsolute(pt);
					result.add(pt);
					return result;
				}
			};
		}
		return super.getAdapter(key);
	}

	public Cursor getCurrentCursor() {
		if (cursor == null) {
			return getFigure().getCursor();
		}
		return cursor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#getDragTracker(org .eclipse.gef.Request)
	 */
	public DragTracker getDragTracker(Request request) {
		return new DragEditPartsTracker(this) {
			protected Cursor calculateCursor() {
				if (isInState(STATE_INVALID))
					return Cursors.NO;
				return getCurrentCursor();
			}

			protected boolean isMove() {
				return true;
			}
		};
	}

	public IFigure getGuideLayer() {
		return getRulerEditPart().getGuideLayer();
	}

	public IFigure getGuideLineFigure() {
		return guideLineFig;
	}

	public JDRulerEditPart getRulerEditPart() {
		return (JDRulerEditPart) getParent();
	}

	public RulerProvider getRulerProvider() {
		return getRulerEditPart().getRulerProvider();
	}

	public int getZoomedPosition() {
		double position = getRulerProvider().getGuidePosition(getModel());
		if (getZoomManager() != null) {
			position = Math.round(position * getZoomManager().getZoom());
		}
		return (int) position;
	}

	public ZoomManager getZoomManager() {
		return getRulerEditPart().getZoomManager();
	}

	protected void handleGuideMoved() {
		refreshVisuals();
	}

	protected void handlePartAttachmentChanged(Object part) {
	}

	protected void handleZoomChanged() {
		refreshVisuals();
	}

	public boolean isHorizontal() {
		return !getRulerEditPart().isHorizontal();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#refreshVisuals()
	 */
	protected void refreshVisuals() {
		updateLocationOfFigures(getZoomedPosition());
	}

	public void removeNotify() {
		GraphicalEditPart nextSelection = null;
		if (getParent().isActive()) {
			// This guide is being deleted (but the ruler is still active). If
			// this guide
			// is selected, determine which part is to be selected next.
			int thisPos = getRulerProvider().getGuidePosition(getModel());
			if (getSelected() != SELECTED_NONE || hasFocus()) {
				List<?> siblings = getParent().getChildren();
				int minDistance = -1;
				for (int i = 0; i < siblings.size(); i++) {
					JDGuideEditPart guide = (JDGuideEditPart) siblings.get(i);
					if (guide == this)
						continue;
					int posDiff = Math.abs(thisPos - getRulerProvider().getGuidePosition(guide.getModel()));
					if (minDistance == -1 || posDiff < minDistance) {
						minDistance = posDiff;
						nextSelection = guide;
					}
				}
				if (nextSelection == null)
					nextSelection = (GraphicalEditPart) getParent();
			}
		}
		super.removeNotify();
		if (nextSelection != null)
			getViewer().select(nextSelection);
	}

	public void setCurrentCursor(Cursor c) {
		cursor = c;
	}

	public void updateLocationOfFigures(int position) {
		getRulerEditPart().setLayoutConstraint(this, getFigure(), new Integer(position));
		Point guideFeedbackLocation = getGuideLineFigure().getBounds().getLocation();
		if (isHorizontal()) {
			guideFeedbackLocation.y = position;
		} else {
			guideFeedbackLocation.x = position;
		}
		getGuideLineFigure().setLocation(guideFeedbackLocation);
		getGuideLineFigure().revalidate();
	}

	public static class GuideLineFigure extends Figure {
		public GuideLineFigure() {
			setPreferredSize(1, 1);
		}

		protected void paintFigure(Graphics g) {
			g.setLineStyle(Graphics.LINE_DOT);
			g.setXORMode(true);
			g.setForegroundColor(ColorConstants.black);
			if (bounds.width > bounds.height) {
				g.drawLine(bounds.x, bounds.y, bounds.right(), bounds.y);
				g.drawLine(bounds.x + 2, bounds.y, bounds.right(), bounds.y);
			} else {
				g.drawLine(bounds.x, bounds.y, bounds.x, bounds.bottom());
				g.drawLine(bounds.x, bounds.y + 2, bounds.x, bounds.bottom());
			}
		}
	}

	public static class GuideSelectionPolicy extends SelectionEditPolicy {
		protected void hideFocus() {
			((GuideFigure) getHostFigure()).setDrawFocus(false);
		}

		protected void hideSelection() {
			((GuideFigure) getHostFigure()).setDrawFocus(false);
		}

		protected void showFocus() {
			((GuideFigure) getHostFigure()).setDrawFocus(true);
		}

		protected void showSelection() {
			((GuideFigure) getHostFigure()).setDrawFocus(true);
		}
	}

}
