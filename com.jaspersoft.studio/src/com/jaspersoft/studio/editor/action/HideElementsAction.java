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
package com.jaspersoft.studio.editor.action;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.JrxmlEditor;
import com.jaspersoft.studio.editor.report.ReportEditor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * 
 * Action that can be used to hide or show the content of a band.
 * Hide the content of a band can be useful when it has a lot of elements
 * and hide it will improve the performances. This action is available only
 * on the band elements
 * 
 * @author Orlandin Marco
 *
 */
public class HideElementsAction extends ACachedSelectionAction {
	
	public static final String ID_VISIBLE = "setVisibilityAction_visible"; //$NON-NLS-1$
	
	public static final String ID_NOT_VISIBLE = "setVisibilityAction_notVisible";  //$NON-NLS-1$
	
	/**
	 * This action can be created two time both to hide or show the elements, this flag 
	 * is used to know which one the current action is
	 */
	private boolean makeVisible = true;

	/**
	 * Command to set the visibility of a band and of all it's children. A possible improvement is to
	 * set the visibility only on the first level since on the second level the parent has already the 
	 * correct visibility
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class SetVisibilityCommand extends Command{
		
		/**
		 * The band
		 */
		private MBand band;
		
		/**
		 * The new visibility for the band and its children
		 */
		private boolean newVisibilityValue;
		
		/**
		 * Create the command 
		 * 
		 * @param band a not null band 
		 * @param newVisibilityValue The new visibility for the band and its children
		 */
		public SetVisibilityCommand(MBand band, boolean newVisibilityValue){
			this.band = band;
			this.newVisibilityValue = newVisibilityValue;
		}
		
		/**
		 * Recursively set the visibility on a node and on all it's children
		 * 
		 * @param children the current list of node
		 * @param visibility the visibility to set
		 */
		private void recursiveVisibility(List<INode> children, boolean visibility){
			for(INode child :children){
				if (child instanceof MGraphicElement){
					((MGraphicElement)child).setVisible(visibility);
				}
				recursiveVisibility(child.getChildren(), visibility);
			}
		}
		
		@Override
		public void execute() {
			recursiveVisibility(band.getChildren(), newVisibilityValue);
			band.setVisible(newVisibilityValue);
			//Necessary to update the outline label
			band.getPropertyChangeSupport().firePropertyChange(JSSCompoundCommand.REFRESH_UI_EVENT, null, null);
			updateActions();
		}
		
		@Override
		public void undo() {
			recursiveVisibility(band.getChildren(), !newVisibilityValue);
			band.setVisible(!newVisibilityValue);
			//Necessary to update the outline label
			band.getPropertyChangeSupport().firePropertyChange(JSSCompoundCommand.REFRESH_UI_EVENT, null, null);
			updateActions();
		}
	}
	
	/**
	 * Constructor
	 * 
	 * @param makeVisible true if the action is to make the elements visible, false otherwise
	 */
	public HideElementsAction(IWorkbenchPart part, boolean makeVisible) {
		super(part);
		setLazyEnablementCalculation(false);
		this.makeVisible = makeVisible;
		init();
	}

	/**
	 * Initializes this action's text and images.
	 */
	protected void init() {
		super.init();
		if (makeVisible){
			setText(Messages.HideElementsAction_showTitle);
			setToolTipText(Messages.HideElementsAction_showTooltip);
			setId(ID_VISIBLE);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/show.png")); //$NON-NLS-1$
		} else {
			setText(Messages.HideElementsAction_hideTitle);
			setToolTipText(Messages.HideElementsAction_hideTooltip);
			setId(ID_NOT_VISIBLE);
			setImageDescriptor(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/hide.png")); //$NON-NLS-1$
		}
		setEnabled(false);
	}

	/**
	 * Force the editor to update the actions, because when a band is hidden the selection dosen't change
	 * but the actions need to be updated to remove the hide action and to insert the show action 
	 */
	private void updateActions(){
		IEditorPart editor = SelectionHelper.getActiveJRXMLEditor();
		if (editor instanceof JrxmlEditor){
			JrxmlEditor jrxmlEditor = (JrxmlEditor)editor;
			IEditorPart part = jrxmlEditor.getReportContainer().getActiveEditor();
			if (part instanceof ReportEditor){
				((ReportEditor)part).forceUpdateActions();
			}
		}
	}
	
	@Override
	protected Command createCommand() {
		List<Object> bands = editor.getSelectionCache().getSelectionModelForType(MBand.class);
		if (!bands.isEmpty()){
			JSSCompoundCommand visibilityCommands = new JSSCompoundCommand((ANode)bands.get(0));
			for(Object rawBand : bands){
				MBand band = (MBand)rawBand;
				if (band.isVisible() != makeVisible && band.getValue() != null) {
					visibilityCommands.add(new SetVisibilityCommand(band, makeVisible));
				}
			}
			return visibilityCommands.isEmpty() ? null : visibilityCommands;
		}
		return null;
	}

}
