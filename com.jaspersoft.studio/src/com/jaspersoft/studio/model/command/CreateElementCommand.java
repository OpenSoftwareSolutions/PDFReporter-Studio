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
package com.jaspersoft.studio.model.command;

import java.util.List;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRCommonElement;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.base.JRBaseElement;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.editor.gef.parts.band.BandResizeTracker;
import com.jaspersoft.studio.editor.layout.ILayout;
import com.jaspersoft.studio.editor.layout.LayoutCommand;
import com.jaspersoft.studio.editor.layout.LayoutManager;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.IGraphicElementContainer;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.MElementGroup;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.frame.MFrame;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
/**
 * @author slavic
 * 
 */
public class CreateElementCommand extends Command {
	protected JasperDesign jasperDesign;
	protected JasperReportsConfiguration jConfig;

	/** The src node. */
	protected MGraphicElement srcNode;
	protected ANode destNode;

	/** The jr element. */
	protected JRDesignElement jrElement;

	/** The jr group. */
	protected JRElementGroup jrGroup;

	/** The location. */
	protected Rectangle location;

	/** The index. */
	protected int index;

	/**
	 * Flag used to mark a command as cancelled during it's execution
	 */
	protected boolean operationCancelled = false;;

	protected CreateElementCommand() {
		super();
	}

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateElementCommand(MElementGroup destNode, MGraphicElement srcNode, int index) {
		super();
		setContext(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateElementCommand(MFrame destNode, MGraphicElement srcNode, int index) {
		super();
		setContext(destNode, srcNode, index);
	}

	public CreateElementCommand(MFrame destNode, MGraphicElement srcNode, Rectangle position, int index) {
		super();
		this.location = position;
		setContext(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateElementCommand(MBand destNode, MGraphicElement srcNode, int index) {
		super();
		setContext(destNode, srcNode, index);
	}

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param position
	 *          the position
	 * @param index
	 *          the index
	 */
	public CreateElementCommand(ANode destNode, MGraphicElement srcNode, Rectangle position, int index) {
		super();
		location = position;
		jrElement = (JRDesignElement) srcNode.getValue();
		if (destNode instanceof IGroupElement) {
			setContext(destNode, srcNode, index);
			// if (destNode instanceof MBand)
			// fixLocation(location, (MBand) destNode);
		} else if (destNode instanceof MFrame) {
			setContext(destNode, srcNode, index);
		} else
			setContext(destNode, srcNode, index);
	}

	private Object destValue;
	private JRPropertiesHolder[] pholder;

	/**
	 * Sets the context.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	protected void setContext(ANode destNode, MGraphicElement srcNode, int index) {
		if (destNode != null) {
			this.jConfig = destNode.getJasperConfiguration();
			this.srcNode = srcNode;
			this.jasperDesign = destNode.getJasperDesign();
			this.jrElement = (JRDesignElement) srcNode.getValue();
			if (destNode instanceof IGroupElement)
				jrGroup = ((IGroupElement) destNode).getJRElementGroup();
			else if (destNode.getValue() instanceof JRElementGroup)
				jrGroup = (JRElementGroup) destNode.getValue();
			destValue = destNode.getValue();
			this.destNode = destNode;
			this.index = index;
			if (destNode instanceof IGraphicElementContainer)
				d = ((IGraphicElementContainer) destNode).getSize();
			if (destNode instanceof IContainerLayout)
				pholder = ((IContainerLayout) destNode).getPropertyHolder();
		} else {
			this.destNode = null;
			// MessageDialog.openInformation(UIUtils.getShell(), "Unable to create the element",
			// "The element can not be created because there aren't containers where it can be placed");
		}
	}
	
	/**
	 * Manually set the jasperdesign for the command
	 * 
	 * @param design the new jasper design
	 */
	public void setJasperDesign(JasperDesign design){
		this.jasperDesign = design;
	}

	/**
	 * Check if the command was cancelled during the execution
	 * 
	 * @return true if the command was cancelled during the execution, false otherwise
	 */
	public boolean isCancelled() {
		return operationCancelled;
	}

	@Override
	public boolean canExecute() {
		return destNode != null && destNode.canAcceptChildren(srcNode);
	}

	private Dimension d;

	public void fixLocation(Rectangle position, MBand band) {
		if (location == null) {
			if (jrElement != null)
				location = new Rectangle(jrElement.getX(), jrElement.getY(), jrElement.getWidth(), jrElement.getHeight());
			else if (band != null)
				location = new Rectangle(band.getBounds().x, band.getBounds().y, 50, 30);
			else
				location = new Rectangle(0, 0, 100, 100);
		}
		if (band != null)
			location = fixLocation(location, band, jrElement);
	}

	public static Rectangle fixLocation(Rectangle position, MBand band, JRDesignElement jrElement) {
		int x = position.x - band.getBounds().x;
		int y = position.y - band.getBounds().y;
		position.setLocation(x, y);
		return position;
	}

	/**
	 * Creates the object.
	 */
	protected void createObject() {
		if (jrElement == null)
			jrElement = srcNode.createJRElement(jasperDesign);
		if (jrElement != null)
			setElementBounds();
	}

	protected void setElementBounds() {
		if (location == null)
			location = new Rectangle(jrElement.getX(), jrElement.getY(), jrElement.getWidth(), jrElement.getHeight());
		if (location.width <= 0)
			location.width = srcNode.getDefaultWidth();
		if (location.height <= 0)
			location.height = srcNode.getDefaultHeight();

		jrElement.setX(location.x);
		jrElement.setY(location.y);
		jrElement.setWidth(location.width);
		jrElement.setHeight(location.height);

		if (jrGroup instanceof JRDesignBand && destNode.getJasperConfiguration().getPropertyBoolean(DesignerPreferencePage.P_RESIZE_CONTAINER, Boolean.TRUE)) {
			JRDesignBand band = (JRDesignBand) jrGroup;
			int height = jrElement.getY() + jrElement.getHeight();
			if (band.getHeight() < height) {
				int maxBandHeight = BandResizeTracker.getMaxBandHeight(band, jasperDesign);
				// If the element is too big it will be resized to the maximum band size
				if (maxBandHeight < height) {
					height = maxBandHeight - 1;
					jrElement.setHeight(height - jrElement.getY());
					// Commented for back-compatibility in 3.6.
					// Replaced with the following line.
					// location.setHeight(height - jrElement.getY());
					location.height = height - jrElement.getY();
				}
				SetValueCommand cmd = new SetValueCommand();
				cmd.setTarget((IPropertySource) destNode);
				cmd.setPropertyId(JRDesignBand.PROPERTY_HEIGHT);
				cmd.setPropertyValue(height);
				addCommand(cmd);
			}
		}
	}

	public void setJrGroup(JRElementGroup jrGroup) {
		this.jrGroup = jrGroup;
	}

	private JSSCompoundCommand commands;

	protected void addCommand(Command command) {
		if (commands == null)
			commands = new JSSCompoundCommand(srcNode);
		commands.add(command);
	}

	protected void addCommands(List<Command> cmds) {
		if (cmds != null)
			for (Command c : cmds)
				addCommand(c);
	}

	protected void executeCommands() {
		if (commands != null)
			commands.execute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrElement != null) {
			removeElement(jasperDesign, jrElement);
			if (jrGroup instanceof JRDesignElementGroup) {
				JRDesignElementGroup jrdgroup = (JRDesignElementGroup) jrGroup;
				if (index < 0 || index > jrGroup.getChildren().size())
					jrdgroup.addElement(jrElement);
				else
					jrdgroup.addElement(index, jrElement);
			} else if (jrGroup instanceof JRDesignFrame) {
				JRDesignFrame jFrame = (JRDesignFrame) jrGroup;
				if (index < 0 || index > jrGroup.getChildren().size())
					jFrame.addElement(jrElement);
				else
					jFrame.addElement(index, jrElement);
			}
			if (destValue instanceof JRPropertiesHolder) {
				String uuid = null;
				if (destValue instanceof JRBaseElement)
					uuid = ((JRBaseElement) destValue).getUUID().toString();
				if (destValue instanceof JRCommonElement) {
					JRCommonElement jce = (JRCommonElement) destValue;
					d = new Dimension(jce.getWidth(), jce.getHeight());
				}
				if (destValue instanceof JRDesignBand) {
					int w = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();
					d = new Dimension(w, ((JRDesignBand) destValue).getHeight());
				}
				if (lCmd == null) {
					ILayout layout = LayoutManager.getLayout(pholder, jasperDesign, uuid);
					lCmd = new LayoutCommand(jrGroup, layout, d);
					addCommand(lCmd);
				}
			}
			executeCommands();
			if (firstTime) {
				SelectionHelper.setSelection(jrElement, false);
				firstTime = false;
			}
		}
	}

	private LayoutCommand lCmd;
	private boolean firstTime = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrGroup == null || jrElement == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (commands != null)
			commands.undo();
		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).removeElement(jrElement);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).removeElement(jrElement);
	}

	/**
	 * Gets the jr element.
	 * 
	 * @return the jr element
	 */
	public JRDesignElement getJrElement() {
		return jrElement;
	}

	/**
	 * Gets the jr group.
	 * 
	 * @return the jr group
	 */
	public JRElementGroup getJrGroup() {
		return jrGroup;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public Rectangle getLocation() {
		return location;
	}

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * remove element from other containers
	 * 
	 * @param jasperDesign
	 * @param element
	 */
	public static void removeElement(JasperDesign jasperDesign, JRDesignElement element) {
		for (JRBand band : jasperDesign.getAllBands()) {
			JRDesignBand b = (JRDesignBand) band;
			b.removeElement(element);
			removeElement(element, b.getElements());
		}

	}

	public static void removeElement(JRDesignElement element, JRElement[] elements) {
		for (JRElement el : elements) {
			if (el instanceof IGroupElement) {
				JRDesignElementGroup egroup = (JRDesignElementGroup) ((IGroupElement) el).getJRElementGroup();
				egroup.removeElement(element);
				removeElement(element, egroup.getElements());
			} else if (el instanceof JRDesignFrame) {
				JRDesignFrame frame = (JRDesignFrame) el;
				frame.removeElement(element);
				removeElement(element, frame.getElements());
			}
		}
	}
}
