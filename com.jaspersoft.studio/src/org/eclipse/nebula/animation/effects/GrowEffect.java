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
package org.eclipse.nebula.animation.effects;

import org.eclipse.nebula.animation.AnimationRunner;
import org.eclipse.nebula.animation.movement.IMovement;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;
/*
 * Resizes a control while keeping it centered.
 * 
 * @author Nicolas Richeton
 * 
 */
public class GrowEffect extends AbstractEffect {

	/**
	 * @deprecated
	 * @param w
	 * @param duration
	 * @param movement
	 * @param onStop
	 * @param onCancel
	 */
	public void grow(AnimationRunner runner, Control w, int duration,
			IMovement movement, Runnable onStop, Runnable onCancel) {
		IEffect effect = new GrowEffect(w, w.getBounds(), new Rectangle(w
				.getBounds().x + 10, w.getBounds().y + 10,
				w.getBounds().width + 10, w.getBounds().height + 10), duration,
				movement, onStop, onCancel);
		runner.runEffect(effect);
	}

	Rectangle src, dest, diff;

	Control control = null;

	public GrowEffect(Control control, Rectangle src, Rectangle dest,
			long lengthMilli, IMovement movement, Runnable onStop,
			Runnable onCancel) {
		super(lengthMilli, movement, onStop, onCancel);
		this.src = src;
		this.dest = dest;
		this.control = control;
		this.diff = new Rectangle(dest.x - src.x, dest.y - src.y, dest.width
				- src.width, dest.height - src.height);

		easingFunction.init(0, 1, (int) lengthMilli);
	}

	public void applyEffect(final long currentTime) {
		if (!control.isDisposed()) {
			control.setBounds((int) (src.x - diff.x
					* easingFunction.getValue(currentTime)),
					(int) (src.y - diff.y
							* easingFunction.getValue(currentTime)),
					(int) (src.width + 2 * diff.width
							* easingFunction.getValue(currentTime)),
					(int) (src.height + 2 * diff.height
							* easingFunction.getValue(currentTime)));
		}
	}
}
