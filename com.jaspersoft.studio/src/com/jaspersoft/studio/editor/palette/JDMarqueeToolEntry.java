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
package com.jaspersoft.studio.editor.palette;

import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.editor.gef.parts.band.NotMovablePartDragTracker;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.compatibility.SharedImages;

/**
 * Palette tool to used the NotMoveablePartDragTracker from any location
 * 
 * @author Orlandin Marco
 *
 */
public class JDMarqueeToolEntry extends ToolEntry {
	
	/**
	 * Creates a new MarqueeToolEntry that can select nodes
	 */
	public JDMarqueeToolEntry() {
		this(null, null);
	}

	/**
	 * Constructor for MarqueeToolEntry.
	 * 
	 * @param label the label; can be <code>null</code>
	 * @param description the description (can be <code>null</code>)
	 */
	public JDMarqueeToolEntry(String label, String description) {
		super(label, description, null, null, NotMovablePartDragTracker.class);
		if (label == null || label.length() == 0)
			setLabel(Messages.JDMarqueeToolEntry_Marquee);
		setUserModificationPermission(PERMISSION_NO_MODIFICATION);
	}
	
	/**
	 * @see org.eclipse.gef.palette.PaletteEntry#getDescription()
	 */
	@Override
	public String getDescription() {
		String description = super.getDescription();
		if (description != null)
			return description;

		int marqueeBehavior = getMarqueeBehavior();
		if (marqueeBehavior == NotMovablePartDragTracker.BEHAVIOR_NODES_TOUCHED) {
			return Messages.JDMarqueeToolEntry_Behavior_Nodes_Touched;
		}
		if (marqueeBehavior == NotMovablePartDragTracker.BEHAVIOR_NODES_CONTAINED) {
			return Messages.JDMarqueeToolEntry_Behavior_Nodes_Contained;
		}
		throw new IllegalArgumentException("Unknown marquee behavior"); //$NON-NLS-1$
	}

	/**
	 * @see org.eclipse.gef.palette.PaletteEntry#getLargeIcon()
	 */
	@Override
	public ImageDescriptor getLargeIcon() {
		ImageDescriptor imageDescriptor = super.getLargeIcon();
		if (imageDescriptor != null) {
			return imageDescriptor;
		}
		return SharedImages.DESC_MARQUEE_TOOL_NODES_24;
	}

	private int getMarqueeBehavior() {
		// retrieve marquee behavior from tool property
		Object value = getToolProperty(NotMovablePartDragTracker.PROPERTY_MARQUEE_BEHAVIOR);
		if (value != null && value instanceof Integer) {
			return ((Integer) value).intValue();
		}
		// return default behavior
		return NotMovablePartDragTracker.DEFAULT_MARQUEE_BEHAVIOR;
	}

	/**
	 * @see org.eclipse.gef.palette.PaletteEntry#getSmallIcon()
	 */
	public ImageDescriptor getSmallIcon() {
		ImageDescriptor imageDescriptor = super.getSmallIcon();
		if (imageDescriptor != null) {
			return imageDescriptor;
		}
		return SharedImages.DESC_MARQUEE_TOOL_NODES_16;
	}

}
