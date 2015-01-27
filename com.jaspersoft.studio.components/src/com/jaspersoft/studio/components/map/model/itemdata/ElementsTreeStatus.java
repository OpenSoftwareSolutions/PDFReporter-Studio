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
package com.jaspersoft.studio.components.map.model.itemdata;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.map.ItemProperty;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.TreeItem;

import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementDTO;

/**
 * This class maintains the status of a {@link TreeViewer} presenting a list of {@link MapDataElementDTO}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ElementsTreeStatus {

	// the names of the elements
	private List<ItemProperty> names;
	// the last operation performed before
	// taking a snapshot of the status
	private LAST_OPERATION lastOperationPerformed;
	// selection levels
	private int level1SelectionIdx = -1;
	private int level2SelectionIdx = -1;

	public int getLevel1SelectionIdx() {
		return level1SelectionIdx;
	}

	public void setLevel1SelectionIdx(int level1SelectionIdx) {
		this.level1SelectionIdx = level1SelectionIdx;
	}

	public int getLevel2SelectionIdx() {
		return level2SelectionIdx;
	}

	public void setLevel2SelectionIdx(int level2SelectionIdx) {
		this.level2SelectionIdx = level2SelectionIdx;
	}

	public List<ItemProperty> getNames() {
		if(names==null){
			names = new ArrayList<ItemProperty>();
		}
		return names;
	}

	public void setNames(List<ItemProperty> names) {
		this.names = names;
	}
	
	public LAST_OPERATION getLastOperationPerformed() {
		return lastOperationPerformed;
	}

	public void setLastOperationPerformed(LAST_OPERATION lastOperationPerformed) {
		this.lastOperationPerformed = lastOperationPerformed;
	}
	
	public void addName(ItemProperty name) {
		getNames().add(name);
	}
	
	public void addName(int index, ItemProperty name) {
		getNames().add(index, name);
	}
	
	public void removeName(ItemProperty name) {
		getNames().remove(name);
	}
	
	public static enum LAST_OPERATION {
		ADD, EDIT, REMOVE, MOVEUP, MOVEDOWN, REFRESH
	}

	public Object[] findExpandedElements(List<MapDataElementDTO> candidates) {
		List<Object> found = new ArrayList<Object>();
		for(MapDataElementDTO c : candidates) {
			for(ItemProperty n : getNames()) {
				if(ElementDataHelper.areNamesEqual(n,c.getName())) {
					found.add(c);
					break;
				}
			}
		}
		return found.toArray();
	}
	
	public static ElementsTreeStatus getElementsTreeStatus(TreeViewer elementsTV, LAST_OPERATION lastOperation) {
		Object[] expandedElements = elementsTV.getExpandedElements();
		ElementsTreeStatus status = new ElementsTreeStatus();
		status.setLastOperationPerformed(lastOperation);
		for(Object el : expandedElements) {
			if(el instanceof MapDataElementDTO) {
				status.addName(((MapDataElementDTO) el).getName());
			}
		}

		TreeItem[] selection = elementsTV.getTree().getSelection();
		if(selection.length==1){
			TreeItem selItem = selection[0];
			TreeItem parentItem = selItem.getParentItem();
			
			// Level 2, if any
			if(parentItem!=null){
				int level2Idx = 0;
				for(TreeItem i : parentItem.getItems()) {
					if(i.equals(selItem)){
						status.setLevel2SelectionIdx(level2Idx);
						selItem = parentItem;
						break;
					}
					else {
						level2Idx++;
					}
				}
			}
			
			// Level 1, always
			int level1Idx = 0;
			for (TreeItem i : elementsTV.getTree().getItems()) {
				if(i.equals(selItem)) {
					status.setLevel1SelectionIdx(level1Idx);
					break;
				}
				else {
					level1Idx++;
				}
			}
		}
		
		return status;
	}
	
	public static ISelection getSuggestedSelection(TreeViewer elementsTV, ElementsTreeStatus status) {
		int level1Idx = status.getLevel1SelectionIdx();
		int level2Idx = status.getLevel2SelectionIdx();
		if(level1Idx>=0 && level1Idx<elementsTV.getTree().getItemCount()) {
			TreeItem level1Item = elementsTV.getTree().getItem(level1Idx);
			if(level2Idx>=0 && status.getLastOperationPerformed()!=LAST_OPERATION.REMOVE) {
				switch (status.getLastOperationPerformed()) {
				case MOVEUP:
					level2Idx = Math.max(0, level2Idx-1);
					break;
				case MOVEDOWN:
					level2Idx = Math.min(level1Item.getItemCount()-1,level2Idx+1);
					break;
				default:
					break;
				}
				return getSuggestedSelection(level1Item, level2Idx);
			}
			else {
				return level1Item.getData()!=null ? new StructuredSelection(level1Item.getData()) : StructuredSelection.EMPTY;
			}
		}
		return StructuredSelection.EMPTY;
	}
	
	private static ISelection getSuggestedSelection(TreeItem item, int level) {
		if(level>=0){
			TreeItem childItem = item.getItem(level);
			if(childItem.getData()!=null) {
				return new StructuredSelection(childItem.getData());
			}
		}
		return StructuredSelection.EMPTY;
	}
}
