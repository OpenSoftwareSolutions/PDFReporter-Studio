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
package com.jaspersoft.studio.editor.gef.parts.band;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Arc2D;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.GridData;
import org.eclipse.draw2d.GridLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.java2d.J2DGraphics;

/**
 * This handle is supposed to be used with bands, so that it can propose useful contextual buttons.
 * The pad is drawn on top of the band.
 * <p>
 * 
 * <b>NOTE</b>: if the zooming factor is too low the pad is not painted. Moreover its size is kept
 * constant when a zooming operation occurs, this to avoid nasty (scaling) effects on image buttons.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class BandButtonPadHandle extends AbstractHandle {
	
	// FIXME	THIS CLASS IS FOR THE MOMENT A PROOF OF CONCEPT
	// 				TO SEE IT IN ACTION ADD THE FOLLOWING CODE TO THE CLASS BandResizableEditPolicy#createSelectionHandles()
	//
	//						BandButtonPadHandle buttonPadHandle=new BandButtonPadHandle((GraphicalEditPart)getHost());
	//						buttonPadHandle.setBorder(null);	
	//						list.add(buttonPadHandle);
	
	// DEFAULT SIZE
	private static final int DEFAULT_PAD_WIDTH=100;
	private static final int DEFAULT_PAD_HEIGHT=20;
	// MINIMUM ZOOM FACTOR ALLOWED
	private static final double MINIMUM_ZOOM_SCALE=0.75d;

	/**
	 * Creates a new {@link BandButtonPadHandle} instance.
	 * 
	 * @param owner the owner edit part for the handle
	 */
	public BandButtonPadHandle(GraphicalEditPart owner){
		super(owner, new BandButtonPadLocator(owner.getFigure()));
		createButtons();
	}
	
	/*
	 * Creates the pad buttons.
	 */
	private void createButtons() {
		// TODO	1	We should provide a dynamic way to get the buttons to show.
		//				Moreover we should enhance this POC for the band in order to extend it to also other report element.
		//				For example a factory that creates a list of suitable buttons depending on the editpart kind (i.e. band, textbox, image..)
		//				This will require a "dynamic" size calculation of the button bar width and so on.
		// TODO 2 Instead of using ImageFigure, we could use Clickable(s) to build the button pad (?!)
		GridLayout gridLayout=new GridLayout(1, false);
		gridLayout.marginWidth=15;
		gridLayout.marginHeight=2;
		gridLayout.horizontalSpacing=10;
		this.setLayoutManager(gridLayout);
		
		ImageFigure btn1=new ImageFigure(JaspersoftStudioPlugin.getInstance().getImage("/icons/resources/eclipse/etool16/delete_edit.gif"));
		btn1.setToolTip(new Label("Delete band"));
		btn1.addMouseListener(new MouseListener() {
			public void mousePressed(MouseEvent me) {
				boolean deleteOK = MessageDialog.openConfirm(Display.getDefault().getActiveShell(), "Delete band", "Are you sure you want to delete the band?");
				if(deleteOK){
					// Try the creation and execution of the delete request:
					GroupRequest request = new GroupRequest(RequestConstants.REQ_DELETE);
					request.setEditParts(getOwner());
					Command delcmd = getOwner().getCommand(request);
					if(delcmd!=null && delcmd.canExecute()){
						getCommandStack().execute(delcmd);
					}
				}
			}

			public void mouseReleased(MouseEvent me) {
			}

			public void mouseDoubleClicked(MouseEvent me) {
			}
		});
		btn1.setCursor(Cursors.HAND);
		this.add(btn1);
		
		GridData imgFigGD=new GridData(GridData.HORIZONTAL_ALIGN_END,GridData.VERTICAL_ALIGN_CENTER,true,false);
		gridLayout.setConstraint(btn1, imgFigGD);
	}
	
	/*
	 * Gets the command stack for redo-undo support.
	 */
	private CommandStack getCommandStack(){
		// TODO - Get command stack in a more safe way??
		return
			getOwner().getViewer().getEditDomain().getCommandStack();
	}

	
	@Override
	protected DragTracker createDragTracker() {
		return null;
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		if(isZoomOK() && isSingleSelected()){
			Rectangle padBounds = getBounds().getCopy();
			padBounds.width=DEFAULT_PAD_WIDTH;
			padBounds.height=DEFAULT_PAD_HEIGHT;
			setBounds(padBounds);
			
			Rectangle r=padBounds.getCopy();
			if(graphics instanceof J2DGraphics){
				Graphics2D g = ((J2DGraphics) graphics).getGraphics2D();
				g.setColor(new Color(223,237,249));
				g.fillArc(r.x+r.width-22, r.y,20,40,0,90);
				g.fillArc(r.x+2, r.y,20,40,90,90);
				g.fillRect(r.x+10, r.y, r.width-20, r.height);
			}
			else{
				// Fall-back solution: should never happen
				graphics.fillRectangle(r);
			}
	
			if(graphics instanceof J2DGraphics){
				Graphics2D g = ((J2DGraphics) graphics).getGraphics2D();
				g.setStroke(new BasicStroke(1.0f));
				g.setColor(new Color(181,198,209));
				g.draw(new Arc2D.Double(
						r.x+r.width-22, r.y,20,40,
	          0, 90,Arc2D.OPEN));
				g.draw(new Arc2D.Double(
						r.x+2, r.y,20,40,
	          90, 90,Arc2D.OPEN));
				g.drawLine(r.x+11, r.y, r.x+r.width-11, r.y);
				
				g.setColor(Color.WHITE);
				g.draw(new Arc2D.Double(
						r.x+r.width-23, r.y+1,20,40,
	          0, 90,Arc2D.OPEN));
				g.draw(new Arc2D.Double(
						r.x+3, r.y+1,20,40,
	          90, 90,Arc2D.OPEN));
				g.drawLine(r.x+11, r.y+1, r.x+r.width-11, r.y+1);			
			}
			else{
				// Fall-back solution: should never happen
				graphics.drawRectangle(r);
			}
		}
	}
	
	@Override
	protected void paintChildren(Graphics graphics) {
		if(isZoomOK() && isSingleSelected()){
			super.paintChildren(graphics);
		}
	}

	/*
	 * Checks if the editpart is the only one selected
	 */
	private boolean isSingleSelected() {
		return
				getOwner().getSelected() == EditPart.SELECTED_PRIMARY &&
				((IStructuredSelection) getOwner().getViewer().getSelection()).size() == 1;
	}

	/*
	 * Checks if the zooming factor is not low.
	 * Actually the allowed minimum zoom size is 75%.
	 */
	private boolean isZoomOK(){
		ZoomManager zoomManager = (ZoomManager) getOwner().getViewer().getProperty(ZoomManager.class.toString());
		if(zoomManager!=null && 
				zoomManager.getZoom()>= MINIMUM_ZOOM_SCALE){
			return true;
		}

		return false;
	}
	
}

/**
 * The Locator for the band button pad that will popup once the 
 * band edit part is selected.
 */
class BandButtonPadLocator implements Locator {
	
	/* OFFSET for custom drawing the pad on top of the band */
	private static final int Y_OFFSET=20;
	private static final int X_OFFSET=0;
	/* Reference to the band figure */
	private IFigure reference;
	
	/**
	 * Creates a new {@link BandButtonPadLocator} instance.
	 * 
	 * @param reference the reference to the band figure
	 */
	public BandButtonPadLocator(IFigure reference){
		this.reference=reference;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.draw2d.Locator#relocate(org.eclipse.draw2d.IFigure)
	 */
	public void relocate(IFigure target) {
		Rectangle bounds = reference.getBounds().getCopy();
		reference.translateToAbsolute(bounds);
		target.translateToRelative(bounds);
		bounds.y-=Y_OFFSET;
		bounds.x-=X_OFFSET;
		target.setBounds(bounds);
	}
	
}
