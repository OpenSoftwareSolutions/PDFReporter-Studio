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
package org.eclipse.babel.editor.widgets.suggestion.model;

import org.eclipse.babel.editor.widgets.suggestion.provider.ISuggestionProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Encapsulates text of the suggestion and icon of the suggestion provider,
 * which provides the respective translation.
 *
 * @author Samir Soyer
 *
 */
public class Suggestion {

	private Image icon;
	private String text;
	private ISuggestionProvider provider;

	/**
	 * @param icon
	 *            is the image of suggestion provider which provides the
	 *            translation of the text
	 * @param text
	 *            is the translated suggestion
	 */
	public Suggestion(Image icon, String text, ISuggestionProvider provider) {
		this.icon = icon;
		this.text = text;
		this.provider = provider;
	}

	/**
	 * @return Image object of the suggestion provider which provides the
	 *         suggestion
	 */
	public Image getIcon() {
		return icon;
	}

	/**
	 * @param icon
	 *            Image object of the suggestion provider which provides the
	 *            suggestion
	 */
	public void setIcon(Image icon) {
		this.icon = icon;
	}

	/**
	 * @return translated text, i.e suggestion
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text
	 *            is the translated text, i.e suggestion
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * @return {@link ISuggestionProvider} that provides this suggestion
	 */
	public ISuggestionProvider getProvider() {
		return provider;
	}

	/**
	 * @param provider
	 *            is the {@link ISuggestionProvider} that provides this
	 *            suggestion
	 */
	public void setProvider(ISuggestionProvider provider) {
		this.provider = provider;
	}

}
