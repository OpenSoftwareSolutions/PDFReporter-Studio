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
package com.jaspersoft.studio.model.band;

import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.parts.band.BandResizeTracker;
import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutCommand;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.property.IPostSetValue;

public class PostSetSizeBand implements IPostSetValue {

	@Override
	public Command postSetValue(IPropertySource target, Object prop, Object newValue, Object oldValue) {
		if (target instanceof MBand && prop.equals(JRDesignBand.PROPERTY_HEIGHT)) {
			MBand mband = (MBand) target;
			JasperDesign jDesign = mband.getJasperDesign();
			return getBandResizeCommand(mband, jDesign);
		}
		if (target instanceof MReport
				&& (prop.equals(JasperDesign.PROPERTY_PAGE_WIDTH) || prop.equals(JasperDesign.PROPERTY_LEFT_MARGIN) || prop
						.equals(JasperDesign.PROPERTY_RIGHT_MARGIN))) {
			MReport mrep = (MReport) target;
			JasperDesign jDesign = mrep.getJasperDesign();
			JSSCompoundCommand c = new JSSCompoundCommand(mrep);
			for (INode n : mrep.getChildren()) {
				if (n instanceof MBand && n.getValue() != null)
					c.add(getBandResizeCommand((MBand) n, jDesign));
			}
			if (!c.isEmpty())
				return c;
		}
		return null;
	}

	public Command getBandResizeCommand(MBand mband, JasperDesign jDesign) {
		JRDesignBand band = mband.getValue();
		int w = jDesign.getPageWidth() - jDesign.getLeftMargin() - jDesign.getRightMargin();
		// Check if the size is valid
		int maxHeight = BandResizeTracker.getMaxBandHeight(band, jDesign);
		if (band.getHeight() > maxHeight) {
			band.setHeight(maxHeight - 1);
		}
		Dimension d = new Dimension(w, band.getHeight());
		ILayout layout = LayoutManager.getLayout(new JRPropertiesHolder[] { band }, jDesign, null);
		return new LayoutCommand(band, layout, d);
	}

}
