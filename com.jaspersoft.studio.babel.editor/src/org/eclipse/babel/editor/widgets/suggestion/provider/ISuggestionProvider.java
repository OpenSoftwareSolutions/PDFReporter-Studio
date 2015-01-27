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

import java.util.Map;

import org.eclipse.babel.editor.widgets.suggestion.exception.InvalidConfigurationSetting;
import org.eclipse.babel.editor.widgets.suggestion.model.Suggestion;

/**
 * Interface for the suggestion providers which should implement {@link
 * ISuggestionProvider.getSuggestion()} method to return provided suggestion
 *
 * @author Samir Soyer
 * @author Martin Reiterer - Added suggestion provider configuration methods
 *
 */
public interface ISuggestionProvider {

	/**
	 * Returns translation of the original text to a given language
	 *
	 * @param original
	 *            is the untranslated string
	 * @param targetLanguage
	 *            is the language, to which the original text will be translated
	 * @return translation of original text
	 */
	Suggestion getSuggestion(String original, String targetLanguage);

	/**
	 * Returns a list of all configuration settings of the suggestion provider
	 *
	 * @return The list of active configuration settings
	 */
	Map<String, ISuggestionProviderConfigurationSetting> getAllConfigurationSettings();

	/**
	 * Allows to update one particular configuration setting
	 *
	 * @param setting
	 *            The configuration Setting of type
	 *            {@link ISuggestionProviderConfigurationSetting}
	 */
	void updateConfigurationSetting(String configurationId,
			ISuggestionProviderConfigurationSetting setting)
			throws InvalidConfigurationSetting;
}
