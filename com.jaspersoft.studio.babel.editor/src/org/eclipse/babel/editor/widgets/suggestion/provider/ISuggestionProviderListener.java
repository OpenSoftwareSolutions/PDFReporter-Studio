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
package org.eclipse.babel.editor.widgets.suggestion.provider;

/**
 * Listener interface for {@link ISuggestionProvider}s. Defines the method to
 * call when an update event occurs.
 *
 * @author Samir Soyer
 *
 */
public interface ISuggestionProviderListener {

	/**
	 * This method will be called after a {@link ISuggestionProvider} is
	 * updated. e.q if resource of a suggestion provider is updated after
	 * creating the object.
	 *
	 * @param provider
	 *            is the suggestion provider which was updated
	 */
	public void suggestionProviderUpdated(ISuggestionProvider provider);
}
