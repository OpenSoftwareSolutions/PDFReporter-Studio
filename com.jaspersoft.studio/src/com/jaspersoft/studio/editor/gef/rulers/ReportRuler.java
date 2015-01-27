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
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;
/*
 * The Class ReportRuler.
 * 
 * @author Chicu Veaceslav
 */
public class ReportRuler implements Serializable {

	/** The Constant PROPERTY_CHILDREN. */
	public static final String PROPERTY_CHILDREN = "children changed"; //$NON-NLS-1$

	/** The Constant PROPERTY_UNIT. */
	public static final String PROPERTY_UNIT = "units changed"; //$NON-NLS-1$

	public static final String PROPERTY_HOFFSET = "HOFFSET"; //$NON-NLS-1$
	public static final String PROPERTY_VOFFSET = "VOFFSET"; //$NON-NLS-1$
	public static final String PROPERTY_HEND = "HEND"; //$NON-NLS-1$
	public static final String PROPERTY_VEND = "VEND"; //$NON-NLS-1$

	/** The Constant serialVersionUID. */
	static final long serialVersionUID = 1;

	/** The listeners. */
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);

	/** The unit. */
	private int unit;

	private int hoffset, voffset, hend, vend;

	/** The horizontal. */
	private boolean horizontal;

	/** The guides. */
	private List<ReportRulerGuide> guides = new ArrayList<ReportRulerGuide>();

	/**
	 * Instantiates a new report ruler.
	 * 
	 * @param isHorizontal
	 *          the is horizontal
	 */
	public ReportRuler(boolean isHorizontal) {
		this(isHorizontal, RulerProvider.UNIT_PIXELS);
	}

	/**
	 * Instantiates a new report ruler.
	 * 
	 * @param isHorizontal
	 *          the is horizontal
	 * @param unit
	 *          the unit
	 */
	public ReportRuler(boolean isHorizontal, int unit) {
		horizontal = isHorizontal;
		setUnit(unit);
	}

	/**
	 * Adds the guide.
	 * 
	 * @param guide
	 *          the guide
	 */
	public void addGuide(ReportRulerGuide guide) {
		if (!guides.contains(guide)) {
			guide.setHorizontal(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	/**
	 * Adds the property change listener.
	 * 
	 * @param listener
	 *          the listener
	 */
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	// the returned list should not be modified
	/**
	 * Gets the guides.
	 * 
	 * @return the guides
	 */
	public List<ReportRulerGuide> getGuides() {
		return guides;
	}

	/**
	 * Gets the unit.
	 * 
	 * @return the unit
	 */
	public int getUnit() {
		return unit;
	}

	/**
	 * Checks if is hidden.
	 * 
	 * @return true, if is hidden
	 */
	public boolean isHidden() {
		return false;
	}

	/**
	 * Checks if is horizontal.
	 * 
	 * @return true, if is horizontal
	 */
	public boolean isHorizontal() {
		return horizontal;
	}

	/**
	 * Removes the guide.
	 * 
	 * @param guide
	 *          the guide
	 */
	public void removeGuide(ReportRulerGuide guide) {
		if (guides.remove(guide)) {
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	/**
	 * Removes the property change listener.
	 * 
	 * @param listener
	 *          the listener
	 */
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	/**
	 * Sets the hidden.
	 * 
	 * @param isHidden
	 *          the new hidden
	 */
	public void setHidden(boolean isHidden) {
	}

	/**
	 * Sets the unit.
	 * 
	 * @param newUnit
	 *          the new unit
	 */
	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			int oldUnit = unit;
			unit = newUnit;
			listeners.firePropertyChange(PROPERTY_UNIT, oldUnit, newUnit);
		}
	}

	public int getHoffset() {
		return hoffset;
	}

	public void setHoffset(int hoffset) {
		if (this.hoffset != hoffset) {
			int old = this.hoffset;
			this.hoffset = hoffset;
			listeners.firePropertyChange(PROPERTY_HOFFSET, old, hoffset);
		}
	}

	public int getVoffset() {
		return voffset;
	}

	public void setVoffset(int voffset) {
		if (this.voffset != voffset) {
			int old = this.voffset;
			this.voffset = voffset;
			listeners.firePropertyChange(PROPERTY_VOFFSET, old, voffset);
		}
	}

	public int getHend() {
		return hend;
	}

	public void setHend(int hend) {
		if (this.hend != hend) {
			int old = this.hend;
			this.hend = hend;
			listeners.firePropertyChange(PROPERTY_HEND, old, hend);
		}
	}

	public int getVend() {
		return vend;
	}

	public void setVend(int vend) {
		if (this.vend != vend) {
			int old = this.vend;
			this.vend = vend;
			listeners.firePropertyChange(PROPERTY_VEND, old, vend);
		}
	}

}
