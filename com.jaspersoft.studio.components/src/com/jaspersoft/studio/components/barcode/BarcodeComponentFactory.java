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
package com.jaspersoft.studio.components.barcode;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.barbecue.BarbecueComponent;
import net.sf.jasperreports.components.barcode4j.CodabarComponent;
import net.sf.jasperreports.components.barcode4j.Code128Component;
import net.sf.jasperreports.components.barcode4j.Code39Component;
import net.sf.jasperreports.components.barcode4j.DataMatrixComponent;
import net.sf.jasperreports.components.barcode4j.EAN128Component;
import net.sf.jasperreports.components.barcode4j.EAN13Component;
import net.sf.jasperreports.components.barcode4j.EAN8Component;
import net.sf.jasperreports.components.barcode4j.FourStateBarcodeComponent;
import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;
import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.components.barcode4j.POSTNETComponent;
import net.sf.jasperreports.components.barcode4j.RoyalMailCustomerComponent;
import net.sf.jasperreports.components.barcode4j.UPCAComponent;
import net.sf.jasperreports.components.barcode4j.UPCEComponent;
import net.sf.jasperreports.components.barcode4j.USPSIntelligentMailComponent;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;
import org.eclipse.ui.part.WorkbenchPart;

import com.jaspersoft.studio.components.barcode.command.CreateBarcodeCommand;
import com.jaspersoft.studio.components.barcode.figure.BarcodeFigure;
import com.jaspersoft.studio.components.barcode.model.MBarcode;
import com.jaspersoft.studio.components.barcode.model.MBarcodeBarbecue;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MBarcode4j;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MCodabar;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MCode128;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MCode39;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MDataMatrix;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MEAN128;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MEAN13;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MEAN8;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MFourStateBarcode;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MInterleaved2Of5;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MPDF417;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MPOSTNET;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MRoyalMail;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MUPCA;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MUPCE;
import com.jaspersoft.studio.components.barcode.model.barcode4j.MUSPSIntelligent;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.report.AbstractVisualEditor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.plugin.IComponentFactory;
import com.jaspersoft.studio.plugin.IPaletteContributor;
import com.jaspersoft.studio.plugin.PaletteContributor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class BarcodeComponentFactory implements IComponentFactory {

	private static List<Class<?>> knownClasses;
	
	static {
		knownClasses = new ArrayList<Class<?>>(18);
		knownClasses.add(MBarcode.class);
		knownClasses.add(MBarcode4j.class);
		knownClasses.add(MCodabar.class);
		knownClasses.add(MCode128.class);
		knownClasses.add(MCode39.class);
		knownClasses.add(MDataMatrix.class);
		knownClasses.add(MEAN128.class);
		knownClasses.add(MEAN13.class);
		knownClasses.add(MEAN8.class);
		knownClasses.add(MFourStateBarcode.class);
		knownClasses.add(MRoyalMail.class);
		knownClasses.add(MUSPSIntelligent.class);
		knownClasses.add(MInterleaved2Of5.class);
		knownClasses.add(MPDF417.class);
		knownClasses.add(MPOSTNET.class);
		knownClasses.add(MUPCA.class);
		knownClasses.add(MUPCE.class);
		knownClasses.add(MBarcodeBarbecue.class);
	}

	public ANode createNode(ANode parent, Object jrObject, int newIndex) {
		if (jrObject instanceof JRDesignComponentElement) {
			if (((JRDesignComponentElement) jrObject).getComponent() instanceof BarbecueComponent)
				return new MBarcodeBarbecue(parent,
						(JRDesignComponentElement) jrObject, newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof CodabarComponent)
				return new MCodabar(parent,
						(JRDesignComponentElement) jrObject, newIndex);

			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof EAN128Component)
				return new MEAN128(parent, (JRDesignComponentElement) jrObject,
						newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof Code128Component)
				return new MCode128(parent,
						(JRDesignComponentElement) jrObject, newIndex);

			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof Code39Component)
				return new MCode39(parent, (JRDesignComponentElement) jrObject,
						newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof DataMatrixComponent)
				return new MDataMatrix(parent,
						(JRDesignComponentElement) jrObject, newIndex);

			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof EAN13Component)
				return new MEAN13(parent, (JRDesignComponentElement) jrObject,
						newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof EAN8Component)
				return new MEAN8(parent, (JRDesignComponentElement) jrObject,
						newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof Interleaved2Of5Component)
				return new MInterleaved2Of5(parent,
						(JRDesignComponentElement) jrObject, newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof PDF417Component)
				return new MPDF417(parent, (JRDesignComponentElement) jrObject,
						newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof POSTNETComponent)
				return new MPOSTNET(parent,
						(JRDesignComponentElement) jrObject, newIndex);

			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof RoyalMailCustomerComponent)
				return new MRoyalMail(parent,
						(JRDesignComponentElement) jrObject, newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof USPSIntelligentMailComponent)
				return new MUSPSIntelligent(parent,
						(JRDesignComponentElement) jrObject, newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof FourStateBarcodeComponent)
				return new MFourStateBarcode(parent,
						(JRDesignComponentElement) jrObject, newIndex);

			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof UPCAComponent)
				return new MUPCA(parent, (JRDesignComponentElement) jrObject,
						newIndex);
			else if (((JRDesignComponentElement) jrObject).getComponent() instanceof UPCEComponent)
				return new MUPCE(parent, (JRDesignComponentElement) jrObject,
						newIndex);

		}
		return null;
	}

	public IFigure createFigure(ANode node) {
		if (node instanceof MBarcodeBarbecue || node instanceof MBarcode4j)
			return new BarcodeFigure();
		return null;
	}

	public List<?> getChildren4Element(Object jrObject) {

		return null;
	}

	public IPaletteContributor getPaletteEntries() {
		PaletteContributor pc = new PaletteContributor();
		pc.add(MBarcodeBarbecue.class);
		return pc;
	}

	public Command getCreateCommand(ANode parent, ANode child,
			Rectangle location, int newIndex) {
		if (child instanceof MBarcode) {
			if (parent instanceof MElementGroup)
				return new CreateBarcodeCommand((MElementGroup) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MBand)
				return new CreateBarcodeCommand((MBand) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MFrame)
				return new CreateBarcodeCommand((MFrame) parent,
						(MGraphicElement) child, location, newIndex);
			if (parent instanceof MReport)
				return new CreateBarcodeCommand(parent,
						(MGraphicElement) child, location, newIndex);

			if (parent instanceof IGroupElement) {
				return new CreateBarcodeCommand(parent,
						(MGraphicElement) child, location, newIndex);
			}
		}
		return null;
	}

	public Command getDeleteCommand(ANode parent, ANode child) {
		return null;
	}

	public Command getReorderCommand(ANode parent, ANode child, int newIndex) {
		return null;
	}

	public List<Action> getActions(WorkbenchPart part) {
		return null;
	}

	public List<String> getActionsID() {
		return null;
	}

	public EditPart createEditPart(EditPart context, Object model) {
		return null;
	}

	public Command getOrphanCommand(ANode parent, ANode child) {
		return null;
	}

	public AbstractVisualEditor getEditor(Object node,
			JasperReportsConfiguration jrContext) {
		return null;
	}

	public ExpressionContext getElementExpressionContext(Object jrObject) {
		// FIXME - Implement this method.
		return null;
	}

	@Override
	public Command getStretchToContent(ANode node) {
		return null;
	}

	@Override
	public List<Class<?>> getKnownClasses() {
		return knownClasses;
	}
}
