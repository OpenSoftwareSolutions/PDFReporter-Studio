/*******************************************************************************
 * Copyright (c) 2013 Samir Soyer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Samir Soyer - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.editor.widgets.suggestion.filter;

import org.eclipse.babel.editor.widgets.suggestion.exception.SuggestionErrors;
import org.eclipse.babel.editor.widgets.suggestion.model.Suggestion;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filter class for {@link org.eclipse.jface.viewers.TableViewer.TableViewer} in
 * {@link org.eclipse.babel.editor.widgets.suggestion.SuggestionBubble}
 *
 * @author Samir Soyer
 *
 */
public class SuggestionFilter extends ViewerFilter {

	private String searchString;

	/**
	 * Sets the text that is going to be checked, whether it matches suggestions
	 * in the tableviewer
	 *
	 * @param s
	 *            is the text to be searched for
	 */
	public void setSearchText(String s) {

		this.searchString = ".*" + s + ".*";

	}

	/**
	 * @see org.eclipse.jface.viewers.ViewerFilter#select(org.eclipse.jface.viewers.Viewer,
	 *      java.lang.Object, java.lang.Object)
	 */
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (searchString == null || searchString.length() == 0) {
			return true;
		}

		Suggestion s = (Suggestion) element;
		if (s.getText().toLowerCase().matches(searchString.toLowerCase())
				|| SuggestionErrors.contains(s.getText())) {
			return true;
		}

		return false;
	}
}