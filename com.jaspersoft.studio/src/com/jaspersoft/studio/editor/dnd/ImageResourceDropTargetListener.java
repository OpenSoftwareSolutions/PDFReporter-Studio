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
package com.jaspersoft.studio.editor.dnd;

import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.core.resources.IResource;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.ui.part.ResourceTransfer;

import com.jaspersoft.studio.model.image.MImage;
import com.jaspersoft.studio.model.image.command.CreateImageCommand;
import com.jaspersoft.studio.utils.ImageUtils;

/**
 * Implementation of a drop target listener that is supposed to handle
 * the dropping of images inside {@link EditPartViewer}s.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ImageResourceDropTargetListener extends AbstractTransferDropTargetListener {
	
	private SimpleImageCreationFactory factory = new SimpleImageCreationFactory();

	public ImageResourceDropTargetListener(EditPartViewer viewer, Transfer xfer) {
		super(viewer, xfer);
	}

	public ImageResourceDropTargetListener(EditPartViewer viewer) {
		super(viewer);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.dnd.AbstractTransferDropTargetListener#updateTargetRequest()
	 */
	@Override
	protected void updateTargetRequest() {
		((CreateRequest)getTargetRequest()).setLocation(getDropLocation());
	}

	@Override
	public boolean isEnabled(DropTargetEvent event) {
		if(event.data!=null){
			// check if it can be an image
			return isDroppedDataAnImage(event);
		}
		return super.isEnabled(event);
	}

	/*
	 * Verifies if the object being dropped is a valid image resource.
	 */
	private boolean isDroppedDataAnImage(DropTargetEvent event) {
		if (ResourceTransfer.getInstance().isSupportedType(event.currentDataType)){
			if(event.data instanceof IResource[]){
				// Dropping an image resource from inside workspace
				IResource imgResource = ((IResource[])event.data)[0];
				return ImageUtils.hasValidFileImageExtension(
						imgResource.getProjectRelativePath().getFileExtension());
			}
		}
		else if(FileTransfer.getInstance().isSupportedType(event.currentDataType)){
			// Dropping an image resource from outside workspace
			if(event.data instanceof String[]){
				String filepath = ((String[])event.data)[0];
				if(filepath!=null){
					int lastIndexOfDot = filepath.lastIndexOf(".");
					if(lastIndexOfDot!=-1){
						String extension = filepath.substring(lastIndexOfDot+1);
						return ImageUtils.hasValidFileImageExtension(extension);				
					}
				}
			}
		}
		else if(ImageURLTransfer.getInstance().isSupportedType(event.currentDataType)){
			// Dropping an image dropped from a contributed view (i.e: repository view)
			return (event.data instanceof String);
		}

		return false;
	}

	@Override
	protected void handleDrop() {
		updateTargetRequest();
		updateTargetEditPart();

		if (getTargetEditPart() != null) {
			Command command = getCommand();
			if (command instanceof CreateImageCommand && command.canExecute()){
				setImageExpression((CreateImageCommand)command);
				getViewer().getEditDomain().getCommandStack().execute(command);
			}
			else
				getCurrentEvent().detail = DND.DROP_NONE;
		} else
			getCurrentEvent().detail = DND.DROP_NONE;
	}
	
	/*
	 * Updates the image creation command with the valid expression
	 * for the newly created image element.
	 */
	private void setImageExpression(CreateImageCommand command) {
		if (ResourceTransfer.getInstance().isSupportedType(getCurrentEvent().currentDataType)){
			// Dropping an image resource from inside workspace
			IResource imgResource = ((IResource[])getCurrentEvent().data)[0];
			command.setImageExpression(
					new JRDesignExpression("\""+imgResource.getProjectRelativePath()+"\""));
		}
		else if(FileTransfer.getInstance().isSupportedType(getCurrentEvent().currentDataType)){
			// Dropping an image resource from outside workspace
			String filepath = ((String[])getCurrentEvent().data)[0];
			if(filepath!=null){
				command.setImageExpression(new JRDesignExpression("\""+filepath+"\""));
			}
		}
		else if(ImageURLTransfer.getInstance().isSupportedType(getCurrentEvent().currentDataType)){
			// Dropping an image dropped from a contributed view (i.e: repository view)
			String filepath = (String)getCurrentEvent().data;
			if(filepath!=null){
				command.setImageExpression(new JRDesignExpression("\""+filepath+"\""));
			}
		}
	}

	@Override
	protected Request createTargetRequest() {
		CreateRequest request = new CreateRequest();
		request.setFactory(factory);
		return request;
	}
	
	private class SimpleImageCreationFactory implements CreationFactory {
		public Object getNewObject() {
			return new MImage();
		}

		public Object getObjectType() {
			return MImage.class;
		}
	}
}
