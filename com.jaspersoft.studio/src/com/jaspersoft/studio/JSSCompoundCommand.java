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
package com.jaspersoft.studio;

import java.beans.PropertyChangeEvent;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MLockableRefresh;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.utils.SelectionHelper;

/**
 * Special compound command that disable the refresh of the editor and of the selection
 * during the execution of the commands, this is done for performance improvements and
 * avoid unnecessary selections
 * 
 * @author Orlandin Marco
 *
 */
public class JSSCompoundCommand extends CompoundCommand {
	
	public static final String REFRESH_UI_EVENT = "refreshUIEvent";

	/**
	 * Node used to go back into the model until the mpage or an mreport (
	 * or more generally an MLockableRefresh) is found. In this class, for every
	 * editor is located the flag to set to avoid unnecessary refresh. If this 
	 * node is null this command works exactly like a compound command
	 */
	private ANode referenceNode = null;
	
	/**
	 * Create an instance of the from a compound command. All the commands inside the compound
	 * command, and eventually its name, will be also present in the new JSSCompoundCommand
	 * 
	 * @param cmd a not null compound command
	 * @param referenceNode a node of the model manipulated by the inner commands.
	 * Can be null, in this case this command works like a standard compound command
	 */
	public JSSCompoundCommand(CompoundCommand cmd, ANode node){
		setLabel(cmd.getLabel());
		for(Object item : cmd.getCommands()){
			add((Command)item);
		}
		this.referenceNode = node;
	}
	
	/**
	 * Create an instance of the class
	 * 
	 * @param referenceNode a node of the model manipulated by the inner commands.
	 * Can be null, in this case this command works like a standard compound command
	 */
	public JSSCompoundCommand(ANode node){
		super();
		this.referenceNode = node;
	}
	
	/**
	 * Create an instance of the class with a name
	 * 
	 * @param referenceNode a node of the model manipulated by the inner commands.
	 * Can be null, in this case this command works like a standard compound command
	 */
	public JSSCompoundCommand(String name, ANode node){
		super(name);
		this.referenceNode = node;
	}
	
	/**
	 * Set the reference node with the value of the parameter
	 * 
	 * @param referenceNode new value for the reference node
	 */
	protected void setReferenceNode(ANode referenceNode){
		this.referenceNode = referenceNode;
	}
	
	/**
	 * Set the reference node node if and only if the actual value
	 * is null
	 * 
	 * @param referenceNode the reference node
	 */
	public void setReferenceNodeIfNull(ANode referenceNode){
		if (this.referenceNode == null) this.referenceNode = referenceNode;
	}
	
	/**
	 * Set the reference node node if and only if the actual value
	 * is null and the new value is an ANode
	 * 
	 * @param referenceNode the reference node
	 */
	public void setReferenceNodeIfNull(Object referenceNode){
		if (this.referenceNode == null && referenceNode instanceof ANode){
			this.referenceNode = (ANode)referenceNode;
		}
	}
	
	/**
	 * Return true if for the passed model has the ignore refresh flag active.
	 * If the node dosen't bring to the root of the model then it fallback using the
	 * model inside the opened editor
	 * 
	 * @param node a node of the model
	 * @return true if the refresh must be ignored, false otherwise
	 */
	public static boolean isRefreshEventsIgnored(ANode node){
		ANode mainNode = getMainNode(node);
		if (mainNode == null) mainNode = getMainNode(SelectionHelper.getOpenedRoot());
		if (mainNode instanceof MLockableRefresh) return ((MLockableRefresh)mainNode).isRefreshEventIgnored();
		else return false;
	}
	
	/**
	 * Take a node and go back into it until it find a MLockableRefresh node
	 * 
	 * @param startNode starting node of the model
	 * @return a MLockableNode or null if it can not be found
	 */
	public static ANode getMainNode(ANode startNode){
		if (startNode != null){
			if (startNode instanceof MLockableRefresh) return startNode;
			else if (startNode instanceof MRoot) {
				//I'm on the root, need to go down
				return getMainNode((ANode)startNode.getChildren().get(0));
			} else { 
				ANode node = startNode.getParent();
				if (node instanceof MRoot) return null;
				else return getMainNode(node);
			}
		}
		return null;
	}
	
	/**
	 * Return the actual reference node
	 * 
	 * @return an ANode, could be null
	 */
	public ANode getReferenceNode(){
		return referenceNode;
	}
	
	/**
	 * Take a node and go back into it until it find a MLockableRefresh node. During
	 * the search the reference node it is updated so when it will be found it will be 
	 * substitute the actual reference node, to speedup the next research
	 * 
	 * @return a MLockableNode or null if it can not be found
	 */
	protected ANode getNode(){
		if (referenceNode != null){
			if (referenceNode instanceof MLockableRefresh) return referenceNode;
			else if (referenceNode instanceof MRoot) {
				//I'm on the root, need to go down
				referenceNode = (ANode)referenceNode.getChildren().get(0);
				return getNode();
			} else {
				referenceNode = referenceNode.getParent();
				if (referenceNode instanceof MRoot) {
					referenceNode = null;
					return null;
				}
				else {
					return getNode();
				}
			}
		}
		return null;
	}
	
	/**
	 * Create a fake command to force the refresh of the editor and outline panels, this override
	 * the disable refresh flag, so calling this the editor area is always updated
	 */
	protected void refreshVisuals(){
			 ANode report = getNode();
			 if (report != null){
				 PropertyChangeEvent event = new PropertyChangeEvent(report.getJasperDesign(), REFRESH_UI_EVENT, null, null);
				 report.getPropertyChangeSupport().firePropertyChange(event);
			 }
	}
	
	/**
	 * Create a fake command to force the refresh of the editor and outline panel on the passed node, this override
	 * the disable refresh flag, so calling this the editor area is always updated
	 */
	public static void forceRefreshVisuals(ANode node){
			PropertyChangeEvent event = new PropertyChangeEvent(node.getValue(), REFRESH_UI_EVENT, null, null);
			node.getPropertyChangeSupport().firePropertyChange(event);
	}
	
	/**
	 * Set to ignore or not the refresh on the root of the model of the actual reference node
	 * 
	 * @param value true if the refresh should be disabled, false otherwise
	 */
	protected void setIgnoreEvents(boolean value){
		ANode rootNode = getNode();
		if (rootNode != null){
			if (rootNode instanceof MLockableRefresh) ((MLockableRefresh)rootNode).setIgnoreEvents(value, this);
		}
	}
	
	/**
	 * Get if the events are ignored or not
	 * 
	 * @param value true if the refresh should be disabled, false otherwise
	 */
	protected boolean isIgnoreEvents(){
		ANode rootNode = getNode();
		if (rootNode != null){
			if (rootNode instanceof MLockableRefresh) return ((MLockableRefresh)rootNode).isRefreshEventIgnored();
		}
		return false;
	}
	
	
	/**
	 * Override of the execute command, disable the refresh before the first command
	 * and enable it at the end
	 */
	@Override
	public void execute() {
		if (size() > 0){
			List<?> commands = getCommands(); 
			setIgnoreEvents(true);
			for (int i = 0; i < size(); i++) {
				Command cmd = (Command) commands.get(i);
				cmd.execute();
			}
			setIgnoreEvents(false);
			//Since there could be multiple compound command executed at the same time, the refresh
			//is not forced if the actual command is not the last lock removed. In other words the refresh
			//is disable until the last command has finished
			if (!isIgnoreEvents()) refreshVisuals();
		}
	}
	
	/**
	 * Override of the undo command, disable the refresh before the first command
	 * and enable it at the end
	 */
	@Override
	public void undo() {
		if (size() > 0){
			List<?> commands = getCommands();
			setIgnoreEvents(true);
			for (int i = size() - 1; i >= 0; i--){
				((Command) commands.get(i)).undo();
			}
			setIgnoreEvents(false);
			if (!isIgnoreEvents()) refreshVisuals();
		}
	}
	
	/**
	 * Override of the red command, disable the refresh before the first command
	 * and enable it at the end
	 */
	@Override
	public void redo() {
		if (size() > 0){
			List<?> commands = getCommands();
			setIgnoreEvents(true);
			for (int i = 0; i < size(); i++){
				((Command) commands.get(i)).redo();
			}
			setIgnoreEvents(false);
			if (!isIgnoreEvents()) refreshVisuals();
		}
	}
	
	/**
	 * Add a command to the head of the list
	 * 
	 * @param command command to add
	 */
	public void addFirst(Command command){
		@SuppressWarnings("unchecked")
		List<Object> commands = getCommands();
		commands.add(0, command);
	}
	
	/**
	 * Add a list of command to the commands to executed
	 * 
	 * @param commands not null list of commands to add
	 */
	@SuppressWarnings("unchecked")
	public void addAll(List<?> commands){
		getCommands().addAll(commands);
	}
}




