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
package com.jaspersoft.studio.editor.java2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.draw2d.DeferredUpdateManager;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GraphicsSource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
/*
 * The Class J2DUpdateManager.
 */
public final class J2DUpdateManager extends DeferredUpdateManager {
	
	/** The root figure. */
	private IFigure rootFigure;
	
	/** The invalid figures. */
	private List<IFigure> invalidFigures = new ArrayList<IFigure>();
	
	/** The validating. */
	private boolean validating;
	
	/** The updating. */
	private boolean updating;
	
	/** The after update. */
	private RunnableChain afterUpdate;
	
	/** The update queued. */
	private boolean updateQueued;
	
	/** The current gs. */
	GraphicsSource currentGs = null;
	
	/**
	 * The Class RunnableChain.
	 */
	private static class RunnableChain {
		
		/** The next. */
		RunnableChain next;
		
		/** The run. */
		Runnable run;

		/**
		 * Instantiates a new runnable chain.
		 * 
		 * @param run
		 *          the run
		 * @param next
		 *          the next
		 */
		RunnableChain(Runnable run, RunnableChain next) {
			this.run = run;
			this.next = next;
		}
		
		/**
		 * Run.
		 */
		void run() {
			if (next != null)
				next.run();
			run.run();
		}
	}
	
	//void paint(final GC gc) {
	//	paintAll(gc);
	//}
	
	/**
	 * Paint all.
	 * 
	 * @param gc
	 *          the gc
	 */
	public void paintAll(final GC gc) {
		
		
		if (!validating) {
			J2DGraphicsSource gs = new J2DGraphicsSource(gc);
			
			// Get the are to repaint...
			org.eclipse.swt.graphics.Rectangle rectGC = gc.getClipping();
			org.eclipse.draw2d.geometry.Rectangle rect = new org.eclipse.draw2d.geometry.Rectangle(
					rectGC.x, rectGC.y, rectGC.width, rectGC.height);

			// Create a J2DGraphics with the reight size..
			Graphics graphics = gs.getGraphics(rect); 
			((J2DGraphics)graphics).getGraphics2D().setColor(java.awt.Color.red);
			((J2DGraphics)graphics).getGraphics2D().fillRect(0,0,rect.width, rect.height);
			
			if (!updating) {
				/**
				 * If a paint occurs not as part of an update, we should notify that the region
				 * is being painted. Otherwise, notification already occurs in repairDamage().
				 */
				Rectangle rect2 = graphics.getClip(new Rectangle());
				Map<IFigure,Rectangle> map = new HashMap<IFigure,Rectangle>();
				map.put(rootFigure, rect2);
				firePainting(rect, map);
			}
			performValidation();
			rootFigure.paint(graphics);
			
			
			gs.flushGraphics(rect);
			graphics.dispose();
			
		} else {
			/*
			 * If figures are being validated then we can simply
			 * add a dirty region here and update will repaint this region with other 
			 * dirty regions when it gets to painting. We can't paint if we're not sure
			 * that all figures are valid. 
			 */
			addDirtyRegion(rootFigure, new Rectangle(gc.getClipping()));
		}
		
	}
	
	/**
	 * Sets the root figure.
	 * @param figure the root figure
	 */
	public void setRoot(IFigure figure) {
		rootFigure = figure;
		super.setRoot(figure);
	}
	
	/**
	 * Perform validation.
	 * 
	 * @see UpdateManager#performValidation()
	 */
	public void performValidation() {
		if (invalidFigures.isEmpty() || validating)
			return;
		try {
			IFigure fig;
			validating = true;
			fireValidating();
			for (int i = 0; i < invalidFigures.size(); i++) {
				fig = invalidFigures.get(i);
				invalidFigures.set(i, null);
				fig.validate();
			}
		} finally {
			invalidFigures.clear();
			validating = false;
		}
	}
	
	/**
	 * Adds the given figure to the update queue.  Invalid figures will be validated before 
	 * the damaged regions are repainted.
	 * 
	 * @param f the invalid figure
	 */
	public synchronized void addInvalidFigure(IFigure f) {
		if (invalidFigures.contains(f))
			return;
		queueWork();
		invalidFigures.add(f);
	}
	
	/**
	 * Performs the update.  Validates the invalid figures and then repaints the dirty
	 * regions.
	 * @see #validateFigures()
	 * @see #repairDamage()
	 */
	public synchronized void performUpdate() {
		if (isDisposed() || updating)
			return;
		updating = true;
		try {
			performValidation();
			updateQueued = false;
			repairDamage();
			if (afterUpdate != null) {
				RunnableChain chain = afterUpdate;
				afterUpdate = null;
				chain.run(); //chain may queue additional Runnable.
				if (afterUpdate != null)
					queueWork();
			}
		} finally {
			updating = false;
		}
	}
	
	/**
	 * Adds the given runnable and queues an update if an update is not under progress.
	 * @param runnable the runnable
	 */
	public synchronized void runWithUpdate(Runnable runnable) {
		afterUpdate = new RunnableChain(runnable, afterUpdate);
		if (!updating)
			queueWork();
	}

	
	/**
	 * Posts an {@link UpdateRequest} using {@link Display#asyncExec(Runnable)}.  If work has
	 * already been queued, a new request is not needed.
	 */
	protected void queueWork() {
		if (!updateQueued) {
			sendUpdateRequest();
			updateQueued = true;
		}
	}
	
	

}
