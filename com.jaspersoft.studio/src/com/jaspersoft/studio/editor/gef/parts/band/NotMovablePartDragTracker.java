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
package com.jaspersoft.studio.editor.gef.parts.band;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.SelectEditPartTracker;
import org.eclipse.gef.util.EditPartUtilities;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.editor.java2d.J2DGraphics;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.utils.compatibility.FigureUtilities;

/**
 * This class define a marquee selection tool to select the edit parts by simply dragging on the elements.
 * This class handle a double type of selection, in fact when selecting up to down
 * only elements Completely enclosed in the selection will be selected, otherwise an element will be selected even if
 * it's only touched.
 * 
 * This drag tracker is used by the elements that can't be moved, like bands for example
 * 
 * @author Orlandin Marco
 * 
 */
public class NotMovablePartDragTracker extends SelectEditPartTracker {

	/**
	 * Border color for the selection figure
	 */
	private static Color borderColor = new java.awt.Color(0, 50, 200, 128);
	
	/**
	 * Fill color for the selection figure
	 */
	private static Color fillColor = new java.awt.Color(168, 202, 236, 128);

	/**
	 * The Figure painted by the selection
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	class MarqueeRectangleFigure extends Figure {

		private static final int DELAY = 110; // animation delay in millisecond
		private int offset = 0;
		private boolean schedulePaint = true;

		/**
		 * Paint a semi-transparent rectangle
		 */
		protected void paintFigure(Graphics graphics) {
			Rectangle bounds = getBounds().getCopy();
			graphics.translate(getLocation());
			Graphics2D gr = ((J2DGraphics) graphics).getGraphics2D();
			// gr.setColor(new java.awt.Color(168,202,236,128));
			gr.setColor(fillColor);
			gr.fillRect(0, 0, bounds.width - 1, bounds.height - 1);
			gr.setStroke(new BasicStroke(2.0f));
			gr.setColor(borderColor);
			gr.drawRect(0, 0, bounds.width - 1, bounds.height - 1);
			if (schedulePaint) {
				Display.getCurrent().timerExec(DELAY, new Runnable() {
					public void run() {
						offset++;
						if (offset > 5)
							offset = 0;

						schedulePaint = true;
						repaint();
					}
				});
			}

			schedulePaint = false;
		}
	}

	/**
	 * This behavior selects nodes completely encompassed by the marquee rectangle. This is the default behavior for this
	 * tool. 
	 */
	public static final int BEHAVIOR_NODES_CONTAINED = new Integer(1).intValue();

	/**
	 * This behavior selects nodes that intersect the marquee rectangle.
	 */
	public static final int BEHAVIOR_NODES_TOUCHED = new Integer(4).intValue();

	/**
	 * The request done by the marquee operation
	 */
	private static final Request MARQUEE_REQUEST = new Request(RequestConstants.REQ_SELECTION);

	/**
	 * The property to be used in {@link AbstractTool#setProperties(java.util.Map)} for {@link #setMarqueeBehavior(int)}.
	 */
	public static final Object PROPERTY_MARQUEE_BEHAVIOR = "marqueeBehavior"; //$NON-NLS-1$

	/**
	 * Constant defining the default marquee selection behavior.
	 */
	public static final int DEFAULT_MARQUEE_BEHAVIOR = BEHAVIOR_NODES_CONTAINED;
	
	/**
	 * Value for the mode to set as selected the elements into the selection
	 */
	private static final int DEFAULT_MODE = 0;
	
	/**
	 * Value for the mode to set as selected the elements previously selected without the elements into the current selection 
	 */
	private static final int TOGGLE_MODE = 1;
	
	/**
	 * Value for the mode to set as selected the elements previously selected plus the elements into the current selection 
	 */
	private static final int APPEND_MODE = 2;

	/**
	 * The current marquee behavior
	 */
	private int marqueeBehavior = DEFAULT_MARQUEE_BEHAVIOR;
	
	/**
	 * The feedback figure
	 */
	private Figure marqueeRectangleFigure;
	
	/**
	 * The mode defined by the keyboard key pressed during the selection (CTRL or SHIFT)
	 */
	private int mode;

	/**
	 * The last selected edit parts
	 */
	private Collection<EditPart> selectedEditParts;

	/**
	 * The current request
	 */
	private Request targetRequest;

	/**
	 * Point on the y axes from where the drag selection action start
	 */
	private int dragStart = -1;

	/**
	 * Create the drag tracker for an edit part 
	 * 
	 * @param owner the parent edit part
	 */
	public NotMovablePartDragTracker(EditPart owner) {
		super(owner);
	}
	
	/**
	 * Creates a new Marquee Selection Tool of default type {@link #BEHAVIOR_NODES_CONTAINED}.
	 * The no param constructor is necessary to have the JDMarqueeToolEntry working
	 */
	public NotMovablePartDragTracker() {
		super(null);
		setDefaultCursor(SharedCursors.CROSS);
		setUnloadWhenFinished(false);
	}

	/**
	 * When a click is done in a band only that band will be selected and all others elements deselected, when is done out
	 * of the work area all the elements will be deselected
	 */
	@Override
	public void mouseUp(MouseEvent me, EditPartViewer viewer) {

		boolean wasDragging = movedPastThreshold();
 		if (me.button == 1 && !wasDragging) {
			EditPart clickedPart = viewer.findObjectAt(new Point(me.x, me.y));
			if (clickedPart instanceof BandEditPart) {
				viewer.select(clickedPart);
			} else
				viewer.deselectAll();
		} else
			super.mouseUp(me, viewer);
	};

	/**
	 * Called when the mouse button is released. Overridden to do nothing, since a drag tracker does not need to unload
	 * when finished.
	 */
	protected void handleFinished() {
		dragStart = -1;
		if (unloadWhenFinished())
			getDomain().loadDefaultTool();
		else
			reactivate();
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#applyProperty(java.lang.Object, java.lang.Object)
	 */
	protected void applyProperty(Object key, Object value) {
		if (PROPERTY_MARQUEE_BEHAVIOR.equals(key)) {
			if (value instanceof Integer)
				setMarqueeBehavior(((Integer) value).intValue());
			return;
		}
		super.applyProperty(key, value);
	}

	/**
	 * Called from {@link #performMarqueeSelect()} to determine those {@link EditPart}s that are affected by the current
	 * marquee selection. In default and append mode, the edit parts returned here will become selected in the current
	 * viewer's new selection (which is calculated and set in {@link #performMarqueeSelect()}), while in toggle mode their
	 * selection state will be inverted.
	 * 
	 * Calculation is delegated to {@link #calculatePrimaryMarqueeSelectedEditParts()} and
	 * {@link #calculateSecondaryMarqueeSelectedEditParts(Collection)} to compute the set of marquee selected edit parts
	 * in a two step-process, where all directly affected edit parts are determined first, and those indirectly affected
	 * 
	 * Clients may overwrite to customize the calculation of marquee selected edit parts.
	 * 
	 * @return A collection containing all edit parts that should be regarded as being included in the current marquee
	 *         selection, i.e. which should get selected in default or append mode, and whose selection state should get
	 *         inverted in toggle mode.
	 */
	protected Collection<EditPart> calculateMarqueeSelectedEditParts() {
		Collection<EditPart> marqueeSelectedEditParts = new HashSet<EditPart>();
		marqueeSelectedEditParts.addAll(calculatePrimaryMarqueeSelectedEditParts());
		marqueeSelectedEditParts.addAll(calculateSecondaryMarqueeSelectedEditParts(marqueeSelectedEditParts));
		return marqueeSelectedEditParts;
	}
	

	/**
	 * Responsible of calculating those edit parts that should be regarded as directly affected by the current marquee
	 * selection. By default, the method calculates which edit parts are potential candidates based on the current marquee
	 * behavior and delegates to {@link #isMarqueeSelectable(GraphicalEditPart)} and
	 * {@link #isPrimaryMarqueeSelectedEditPart(GraphicalEditPart)} to decide whether the candidate is to be included in
	 * the marquee selection.
	 * 
	 * @return A {@link Collection} containing all {@link EditPart}s that should be regarded as being directly affected by
	 *         the current marquee selection.
	 */
	@SuppressWarnings("unchecked")
	protected Collection<EditPart> calculatePrimaryMarqueeSelectedEditParts() {
		
		Collection<EditPart> editPartsToProcess = new HashSet<EditPart>();
		editPartsToProcess.addAll(EditPartUtilities.getAllChildren((GraphicalEditPart) getCurrentViewer().getRootEditPart()));
		
		Collection<EditPart> marqueeSelectedEditParts = new ArrayList<EditPart>();
		for (Iterator<EditPart> iterator = editPartsToProcess.iterator(); iterator.hasNext();) {
			GraphicalEditPart editPart = (GraphicalEditPart) iterator.next();
			//The page and the bands are not valid selectable items, so the isMarqueeSelectable retrun false for every element that it isn't an
			//MGraphical element
			if (isMarqueeSelectable(editPart) && isPrimaryMarqueeSelectedEditPart(editPart)) {
				marqueeSelectedEditParts.add(editPart);
			}
		}
		return marqueeSelectedEditParts;
	}

	/**
	 * Responsible of calculating those edit parts that should be regarded as being indirectly affected by the marquee
	 * selection. By default, the method calculates which edit parts are potential candidates based on the current marquee
	 * behavior and delegates to {@link #isMarqueeSelectable(GraphicalEditPart)} and
	 * {@link #isSecondaryMarqueeSelectedEditPart(Collection, EditPart)} to decide whether the candidate is to be included
	 * in the marquee selection.
	 * 
	 * @param directlyMarqueeSelectedEditParts
	 *          A collection containing those {@link EditPart}s that were already identified as being directly affected by
	 *          the marquee selection
	 * @return A {@link Collection} containing all {@link EditPart}s that are indirectly affected by the current marquee
	 *         selection
	 */
	@SuppressWarnings("unchecked")
	protected Collection<EditPart> calculateSecondaryMarqueeSelectedEditParts(Collection<EditPart> directlyMarqueeSelectedEditParts) {

		Collection<EditPart> editPartsToProcess = new HashSet<EditPart>();
		for (Iterator<?> iterator = directlyMarqueeSelectedEditParts.iterator(); iterator.hasNext();) {
			GraphicalEditPart marqueeSelectedEditPart = (GraphicalEditPart) iterator.next();
			editPartsToProcess.addAll(marqueeSelectedEditPart.getSourceConnections());
			editPartsToProcess.addAll(marqueeSelectedEditPart.getTargetConnections());
		}

		Collection<EditPart> secondaryMarqueeSelectedEditParts = new HashSet<EditPart>();
		for (Iterator<?> iterator = editPartsToProcess.iterator(); iterator.hasNext();) {
			GraphicalEditPart editPart = (GraphicalEditPart) iterator.next();
			if (isMarqueeSelectable(editPart)) {
				secondaryMarqueeSelectedEditParts.add(editPart);
			}
		}
		return secondaryMarqueeSelectedEditParts;
	}
	
	/**
	 * Determines which edit parts are directly affected by the current marquee selection. Calculation is performed by
	 * regarding the current marquee selection rectangle ( {@link #getCurrentMarqueeSelectionRectangle()}), taking into
	 * consideration the current marquee behavior (contained vs. touched) that was provided (
	 * {@link #setMarqueeBehavior(int)} ).
	 * 
	 * @param editPart
	 *          the {@link EditPart} whose state is to be determined
	 * @return <code>true</code> if the {@link EditPart} should be regarded as being included in the current marquee
	 *         selection, <code>false</code> otherwise.
	 */
	private boolean isPrimaryMarqueeSelectedEditPart(GraphicalEditPart editPart) {
		// figure bounds are used to determine if edit part is included in
		// selection
		IFigure figure = editPart.getFigure();
		Rectangle r = figure.getBounds().getCopy();
		figure.translateToAbsolute(r);

		boolean included = false;
		Rectangle marqueeSelectionRectangle = getCurrentMarqueeSelectionRectangle();
		// otherwise children will only be 'node' edit parts
		if (marqueeBehavior == BEHAVIOR_NODES_TOUCHED) {
			included = marqueeSelectionRectangle.intersects(r);
		} else if (marqueeBehavior == BEHAVIOR_NODES_CONTAINED) {
			included = marqueeSelectionRectangle.contains(r);
		}
		return included;
	}


	/**
	 * Create the target request
	 */
	protected Request createTargetRequest() {
		return MARQUEE_REQUEST;
	}

	/**
	 * Erases feedback if necessary and puts the tool into the terminal state.
	 */
	public void deactivate() {
		if (isInState(STATE_DRAG_IN_PROGRESS)) {
			eraseMarqueeFeedback();
			eraseTargetFeedback();
		}
		super.deactivate();
		setState(STATE_TERMINAL);
	}

	/**
	 * Erase the feedback figure if it isn't already erased
	 */
	protected void eraseMarqueeFeedback() {
		if (marqueeRectangleFigure != null) {
			removeFeedback(marqueeRectangleFigure);
			marqueeRectangleFigure = null;
		}
	}
	
	/**
	 * Erase the feedbkac from the single selected edit parts
	 */
	protected void eraseTargetFeedback() {
		if (selectedEditParts == null)
			return;
		Iterator<EditPart> oldEditParts = selectedEditParts.iterator();
		while (oldEditParts.hasNext()) {
			EditPart editPart = oldEditParts.next();
			editPart.eraseTargetFeedback(getTargetRequest());
		}
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#getCommandName()
	 */
	protected String getCommandName() {
		return REQ_SELECTION;
	}

	/**
	 * Returns the current marquee selection rectangle.
	 * 
	 * @return A {@link Rectangle} representing the current marquee selection.
	 */
	protected Rectangle getCurrentMarqueeSelectionRectangle() {
		return new Rectangle(getStartLocation(), getLocation());
	}

	/**
	 * Returns the current selection mode, i.e. default, append, or toggle
	 * 
	 * @return on of {@link #DEFAULT_MODE}, {@link #APPEND_MODE}, or {@link #TOGGLE_MODE}
	 */
	protected int getCurrentSelectionMode() {
		return mode;
	}

	/**
	 * Return the current selection feedback figure
	 * 
	 * @return the current figure, could be null
	 */
	protected IFigure getMarqueeFeedbackFigure() {
		if (marqueeRectangleFigure == null) {
			marqueeRectangleFigure = new MarqueeRectangleFigure();
			addFeedback(marqueeRectangleFigure);
		}
		return marqueeRectangleFigure;
	}

	/**
	 * Return the target request
	 */
	protected Request getTargetRequest() {
		if (targetRequest == null)
			targetRequest = createTargetRequest();
		return targetRequest;
	}

	/**
	 * When the mouse button is pressed if it is the left one 
	 * start with the selection and set the mode according 
	 * to the current keyboard key pressed
	 * 
	 * @param mouse button pressed
	 */
	protected boolean handleButtonDown(int button) {
		if (!isViewerImportant(null))
			return true;
		if (button != 1) {
			setState(STATE_INVALID);
			handleInvalidInput();
		}
		if (stateTransition(STATE_INITIAL, STATE_DRAG_IN_PROGRESS)) {
			if (getCurrentInput().isModKeyDown(SWT.MOD1))
				setSelectionMode(TOGGLE_MODE);
			else if (getCurrentInput().isShiftKeyDown())
				setSelectionMode(APPEND_MODE);
			else
				setSelectionMode(DEFAULT_MODE);
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleButtonUp(int)
	 */
	@Override
	protected boolean handleButtonUp(int button) {
		if (stateTransition(STATE_DRAG_IN_PROGRESS, STATE_TERMINAL)) {
			eraseTargetFeedback();
			eraseMarqueeFeedback();
			performMarqueeSelect();
		}
		handleFinished();
		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleDragInProgress()
	 */
	@Override
	protected boolean handleDragInProgress() {
		if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
			if (dragStart == -1) {
				dragStart = getLocation().y;
			} else if (getLocation().y<dragStart){
				marqueeBehavior = BEHAVIOR_NODES_TOUCHED;
			}
			showMarqueeFeedback();
			eraseTargetFeedback();
			selectedEditParts = calculateMarqueeSelectedEditParts();
			showTargetFeedback();
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.tools.AbstractTool#handleFocusLost()
	 */
	@Override
	protected boolean handleFocusLost() {
		if (isInState(STATE_DRAG | STATE_DRAG_IN_PROGRESS)) {
			handleFinished();
			return true;
		}
		return false;
	}

	/**
	 * This method is called when mouse or keyboard input is invalid and erases the feedback.
	 * 
	 * @return <code>true</code>
	 */
	protected boolean handleInvalidInput() {
		eraseTargetFeedback();
		eraseMarqueeFeedback();
		return true;
	}

	/**
	 * Handles high-level processing of a key down event. KeyEvents are forwarded to the current viewer's
	 * {@link KeyHandler}, via {@link KeyHandler#keyPressed(KeyEvent)}.
	 * 
	 * @see AbstractTool#handleKeyDown(KeyEvent)
	 */
	protected boolean handleKeyDown(KeyEvent e) {
		if (super.handleKeyDown(e))
			return true;
		if (getCurrentViewer().getKeyHandler() != null)
			return getCurrentViewer().getKeyHandler().keyPressed(e);
		return false;
	}

	/**
	 * Decides whether the given edit part may potentially be included in the current marquee selection.
	 * 
	 * @param editPart
	 *          the {@link EditPart} of interest
	 * @return <code>true</code> if the given edit part may be included into the marquee selection, <code>false</code>
	 *         otherwise
	 */
	protected boolean isMarqueeSelectable(GraphicalEditPart editPart) {
		return editPart.isSelectable() && FigureUtilities.isNotFullyClipped(editPart.getFigure()) && editPart.getModel() instanceof MGraphicElement;
	}


	/**
	 * MarqueeSelectionTool is only interested in GraphicalViewers, not TreeViewers.
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#isViewerImportant(org.eclipse.gef.EditPartViewer)
	 */
	protected boolean isViewerImportant(EditPartViewer viewer) {
		return getCurrentViewer() instanceof GraphicalViewer;
	}

	/**
	 * Calculates and sets a new viewer selection based on the current marquee selection.
	 * 
	 * By default, this method delegates to {@link #calculateMarqueeSelectedEditParts()} to obtain the set of edit parts,
	 * which should be regarded as being affected by the current marquee selection. It then calculates a new viewer
	 * selection based on the current selection state of all affected edit parts and the current selection mode of the
	 * tool ( {@link #getCurrentSelectionMode()}), as well as the current selection of the viewer (in case of APPEND
	 * mode), which is then passed to the current viewer.
	 * 
	 */
	@SuppressWarnings("unchecked")
	protected void performMarqueeSelect() {

		// determine which edit parts are affected by the current marquee
		// selection
		Collection<EditPart> marqueeSelectedEditParts = calculateMarqueeSelectedEditParts();

		// calculate nodes/connections that are to be selected/deselected,
		// dependent on the current mode of the tool
		Collection<EditPart> editPartsToSelect = new LinkedHashSet<EditPart>();
		Collection<EditPart> editPartsToDeselect = new HashSet<EditPart>();
		for (Iterator<EditPart> iterator = marqueeSelectedEditParts.iterator(); iterator.hasNext();) {
			EditPart affectedEditPart = iterator.next();
			if (affectedEditPart.getSelected() == EditPart.SELECTED_NONE || getCurrentSelectionMode() != TOGGLE_MODE)
				editPartsToSelect.add(affectedEditPart);
			else
				editPartsToDeselect.add(affectedEditPart);
		}

		// include the current viewer selection, if not in DEFAULT mode.
		if (getCurrentSelectionMode() != DEFAULT_MODE) {
			editPartsToSelect.addAll(getCurrentViewer().getSelectedEditParts());
			editPartsToSelect.removeAll(editPartsToDeselect);
		}

		getCurrentViewer().setSelection(new StructuredSelection(editPartsToSelect.toArray()));
	}

	/**
	 * Sets the type of parts that this tool will select. This method should only be invoked once: when the tool is being
	 * initialized
	 * 
	 * @param type {@link #BEHAVIOR_NODES_TOUCHED} or {@link #BEHAVIOR_NODES_CONTAINED}
	 */
	public void setMarqueeBehavior(int type) {
		if (type != BEHAVIOR_NODES_TOUCHED && type != BEHAVIOR_NODES_CONTAINED){
			throw new IllegalArgumentException("Invalid marquee behaviour specified."); //$NON-NLS-1$
		} else {
			marqueeBehavior = type;
		}
	}

	/**
	 * Set the current selection mode, that define 
	 * how manage the newly selected elements
	 * 
	 * @param mode the mode
	 */
	private void setSelectionMode(int mode) {
		this.mode = mode;
	}

	/**
	 * Calls {@link #performOpen()} if the double click was with mouse button 1.
	 * 
	 * @see org.eclipse.gef.tools.AbstractTool#handleDoubleClick(int)
	 */
	@Override
	protected boolean handleDoubleClick(int button) {
		if (getSourceEditPart() != null) {
			super.handleDoubleClick(button);
		}
		return true;
	}

	/**
	 * @see org.eclipse.gef.Tool#setViewer(org.eclipse.gef.EditPartViewer)
	 */
	@Override
	public void setViewer(EditPartViewer viewer) {
		if (viewer == getCurrentViewer())
			return;
		super.setViewer(viewer);
		if (viewer instanceof GraphicalViewer)
			setDefaultCursor(SharedCursors.CROSS);
		else
			setDefaultCursor(SharedCursors.NO);
	}

	/**
	 * Show the marquee figure
	 */
	private void showMarqueeFeedback() {
		Rectangle rect = getCurrentMarqueeSelectionRectangle().getCopy();
		IFigure marqueeFeedbackFigure = getMarqueeFeedbackFigure();
		marqueeFeedbackFigure.translateToRelative(rect);
		marqueeFeedbackFigure.setBounds(rect);
		marqueeFeedbackFigure.validate();
	}

	/**
	 * Show the marquee selection (highlighted borders) on the selected elements
	 */
	protected void showTargetFeedback() {
		for (Iterator<EditPart> itr = selectedEditParts.iterator(); itr.hasNext();) {
			EditPart editPart = itr.next();
			editPart.showTargetFeedback(getTargetRequest());
		}
	}
	
	/**
	 * This method define when the mouse is moved enough to consider the operation
	 * a drag operation. In normal condition the supermethod is good, but when 
	 * the drag tracker is created with the palette tool then every drag must
	 * be considered the start of a selection, so in that case this return always
	 * true
	 */
	protected boolean movedPastThreshold() {
		if (getSourceEditPart() == null) return true;
		else return super.movedPastThreshold();
	}
}
