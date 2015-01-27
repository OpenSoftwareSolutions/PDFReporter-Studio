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
/*
 * Copyright (c) 2006-2009 Nicolas Richeton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors :
 *    Nicolas Richeton (nicolas.richeton@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.eclipse.nebula.animation.movement;
/*
 * Interface for movement implementations. Can be linear, exponential, ...
 * 
 * @author Nicolas Richeton
 */
public interface IMovement {

	/**
	 * Set up the movement. with init( 0, 1, 50), it will take 50 steps to go
	 * from 0 to 1. There is no garanties that getValue will return only values
	 * between min and max.
	 * 
	 * The only thing you know is : getValue( 0 ) = 0 and getValue( 50 ) = 1
	 * 
	 * @param min
	 *            - min (start) value
	 * @param max
	 *            - max( end ) value
	 * @param steps
	 *            - number of steps
	 */
	void init(double min, double max, int steps);

	/**
	 * Get value at step 'step'
	 * 
	 * @param step
	 * @return current value.
	 */
	double getValue(double step);

}
