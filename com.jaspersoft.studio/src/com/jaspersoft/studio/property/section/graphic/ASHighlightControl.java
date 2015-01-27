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
package com.jaspersoft.studio.property.section.graphic;

import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.properties.internal.IHighlightPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IHighlightControl;

/**
 * 
 * Wrapper for a generic control that offer a  highlight function for that control type
 * if it is available, otherwise a default one
 * 
 * @author Orlandin Marco
 */
public class ASHighlightControl implements IHighlightPropertyWidget {

	/**
	 * Control to highlight
	 */
	private Control controlToHighLight;
	
	/**
	 * The class type used to request and highlighter for the control
	 */
	private IHighlightControl painterClass;
	

	/**
	 * Create an instance of the class, allow to specify a type of highlighter different from
	 * the control one
	 * 
	 * @param controlToHighLight the control to highlight.
	 * @param painterClass type of the highlighter requested
	 */
	public ASHighlightControl(Control controlToHighLight, IHighlightControl painterClass){
		this.controlToHighLight = controlToHighLight;
		this.painterClass = painterClass;
	}
	
	/**
	 * Highlight the control for a fixed amount of time
	 */
	@Override
	public void highLightWidget(long ms) {
		// if there isn't a control defined where add the border then return
		if (controlToHighLight == null) return;
		final IHighlightControl highLight = painterClass;
		if (highLight == null) return;
		//highlight the control
		highLight.highLightControl();
		final long sleepTime = ms;
		// Create a thread to remove the paint listener after specified time
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(sleepTime);
					// It need two thread to avoid to freeze the UI during the sleep
					controlToHighLight.getDisplay().asyncExec(new Runnable() {
						@Override
						public void run() {
							highLight.deHighLightControl();
						}
					});
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	@Override
	public Control getControlToBorder() {
		return controlToHighLight;
	}

}
