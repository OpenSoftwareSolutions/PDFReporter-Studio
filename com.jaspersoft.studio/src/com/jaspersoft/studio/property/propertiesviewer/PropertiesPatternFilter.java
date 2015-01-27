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
package com.jaspersoft.studio.property.propertiesviewer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.activities.WorkbenchActivityHelper;
import org.eclipse.ui.dialogs.PatternFilter;


/**
 * The custom pattern filter used in conjunction with FilteredTree when dealing with properties dialog.
 * In order to determine if a node should be filtered it uses the content and label provider of the tree to do pattern matching on its children.
 * This causes the entire tree structure to be realized. 
 *  
 * @author mrabbi
 *
 */
public class PropertiesPatternFilter extends PatternFilter {
	
	/* A cache to store the keyword collections for every properties node */
	private Map<IPropertiesViewerNode,Collection<String>> keywordCache = new HashMap<IPropertiesViewerNode,Collection<String>>();

	public PropertiesPatternFilter() {
		super();
	}

	/*
	 * Retrieves the list of keywords from the selected element.
	 */
	private String[] getKeywords(Object element) {
		List<String> keywordList = new ArrayList<String>();
		if (element instanceof IPropertiesViewerNode) {
			IPropertiesViewerNode node = (IPropertiesViewerNode) element;

			Collection<String> keywordCollection = (Collection<String>) keywordCache
					.get(element);
			if (keywordCollection == null) {
				keywordCollection = node.getNodeKeywords();
				keywordCache.put(node, keywordCollection);
			}
			if (!keywordCollection.isEmpty()){
				keywordList.addAll(keywordCollection);
			}
		}
		return (String[]) keywordList.toArray(new String[keywordList.size()]);
	}

	@Override
	public boolean isElementSelectable(Object element) {
		return element instanceof IPropertiesViewerNode;
	}

	@Override
	public boolean isElementVisible(Viewer viewer, Object element) {
	    if (WorkbenchActivityHelper.restrictUseOf(element))
	        return false;
	    
		// Nodes are not differentiated based on category since 
		// categories are selectable nodes.
		if (isLeafMatch(viewer, element)) {
			return true;
		}

		ITreeContentProvider contentProvider = 
				(ITreeContentProvider) ((TreeViewer) viewer).getContentProvider();
		IPropertiesViewerNode node = (IPropertiesViewerNode) element;
		Object[] children = contentProvider.getChildren(node);
		// Will return true if any subnode of the element matches the search
		if (filter(viewer, element, children).length > 0) {
			return true;
		}		
		return false;
	}

	@Override
	protected boolean isLeafMatch(Viewer viewer, Object element) {
		IPropertiesViewerNode node = (IPropertiesViewerNode) element;
		String text = node.getName();

		if (wordMatches(text)) {
			return true;
		}

		// Also need to check the keywords
		String[] keywords = getKeywords(node);
		for (int i = 0; i < keywords.length; i++){
			if (wordMatches(keywords[i])) {
				return true;
			}
		}
		return false;
	}
}
