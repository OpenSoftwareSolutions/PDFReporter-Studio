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
package com.jaspersoft.studio.editor.gef.rulers;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.jaspersoft.studio.model.IGuidebleElement;
/*
 * Model object representing a guide.
 * <p>
 * In addition to maintaining information about which parts are attached to the guide, LogicGuide also maintains
 * information about the edge along which those parts are attached. This information is useful during resize operations
 * to determine the attachment status of a part.
 * 
 * @author Chicu Veaceslav
 */
public class ReportRulerGuide implements Serializable {

	/** Property used to notify listeners when the parts attached to a guide are changed. */
	public static final String PROPERTY_CHILDREN = "subparts changed"; //$NON-NLS-1$

	/** Property used to notify listeners when the guide is re-positioned. */
	public static final String PROPERTY_POSITION = "position changed"; //$NON-NLS-1$

	/** The Constant serialVersionUID. */
	static final long serialVersionUID = 1;

	/** The listeners. */
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	/** The map. */
	private Map<IGuidebleElement, Integer> map;

	/** The position. */
	private int position;

	/** The horizontal. */
	private boolean horizontal;

	/**
	 * Empty default constructor.
	 */
	public ReportRulerGuide() {
		// empty constructor
	}

	/**
	 * Constructor.
	 * 
	 * @param isHorizontal
	 *          <code>true</code> if the guide is horizontal (i.e., placed on a vertical ruler)
	 */
	public ReportRulerGuide(boolean isHorizontal) {
		setHorizontal(isHorizontal);
	}

	/**
	 * Adds the property change listener.
	 * 
	 * @param listener
	 *          the listener
	 * @see PropertyChangeSupport#addPropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	/**
	 * Gets the map.
	 * 
	 * @return The Map containing all the parts attached to this guide, and their alignments; the keys are LogicSubparts
	 *         and values are Integers
	 */
	public Map<IGuidebleElement, Integer> getMap() {
		if (map == null) {
			map = new Hashtable<IGuidebleElement, Integer>();
		}
		return map;
	}

	/**
	 * Gets the parts.
	 * 
	 * @return the set of all the parts attached to this guide; a set is used because a part can only be attached to a
	 *         guide along one edge.
	 */
	public Set<IGuidebleElement> getParts() {
		return getMap().keySet();
	}

	/**
	 * Gets the position.
	 * 
	 * @return the position/location of the guide (in pixels)
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Checks if is horizontal.
	 * 
	 * @return if the guide is horizontal (i.e., placed on a vertical ruler)
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * Removes the property change listener.
	 * 
	 * @param listener
	 *          the listener
	 * @see PropertyChangeSupport#removePropertyChangeListener(java.beans.PropertyChangeListener)
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Sets the orientation of the guide.
	 * 
	 * @param isHorizontal
	 *          <code>true</code> if this guide is to be placed on a vertical ruler
	 */
	public void setHorizontal(boolean isHorizontal) {
		horizontal = isHorizontal;
	}

	/**
	 * Sets the location of the guide.
	 * 
	 * @param offset
	 *          The location of the guide (in pixels)
	 */
	public void setPosition(int offset) {
		if (position != offset) {
			int oldValue = position;
			position = offset;
			listeners.firePropertyChange(PROPERTY_POSITION, new Integer(oldValue), new Integer(position));
		}
	}

	/**
	 * Attaches the given part along the given edge to this guide. The LogicSubpart is also updated to reflect this
	 * attachment.
	 * 
	 * @param part
	 *          The part that is to be attached to this guide; if the part is already attached, its alignment is updated
	 * @param alignment
	 *          -1 is left or top; 0, center; 1, right or bottom
	 */
	public void attachPart(IGuidebleElement part, int alignment) {
		if (getMap().containsKey(part) && getAlignment(part) == alignment)
			return;

		getMap().put(part, new Integer(alignment));
		ReportRulerGuide parent = isHorizontal() ? part.getHorizontalGuide() : part.getVerticalGuide();
		if (parent != null && parent != this) {
			parent.detachPart(part);
		}
		if (isHorizontal()) {
			part.setHorizontalGuide(this);
		} else {
			part.setVerticalGuide(this);
		}
		listeners.firePropertyChange(PROPERTY_CHILDREN, null, part);
	}

	/**
	 * Detaches the given part from this guide. The LogicSubpart is also updated to reflect this change.
	 * 
	 * @param part
	 *          the part that is to be detached from this guide
	 */
	public void detachPart(IGuidebleElement part) {
		if (getMap().containsKey(part)) {
			getMap().remove(part);
			if (isHorizontal()) {
				part.setHorizontalGuide(null);
			} else {
				part.setVerticalGuide(null);
			}
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, part);
		}
	}

	/**
	 * This methods returns the edge along which the given part is attached to this guide. This information is used by
	 * {@link org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy LogicXYLayoutEditPolicy} to determine
	 * whether to attach or detach a part from a guide during resize operations.
	 * 
	 * @param part
	 *          The part whose alignment has to be found
	 * @return an int representing the edge along which the given part is attached to this guide; 1 is bottom or right; 0,
	 *         center; -1, top or left; -2 if the part is not attached to this guide
	 * @see org.eclipse.gef.examples.logicdesigner.edit.LogicXYLayoutEditPolicy#createChangeConstraintCommand(ChangeBoundsRequest,
	 *      EditPart, Object)
	 */
	public int getAlignment(IGuidebleElement part) {
		if (getMap().get(part) != null)
			return (getMap().get(part)).intValue();
		return -2;
	}

}
