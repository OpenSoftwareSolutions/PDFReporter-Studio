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

import java.util.ArrayList;

import org.eclipse.babel.editor.widgets.suggestion.exception.InvalidConfigurationSetting;

/**
 * This class contains a list of all suggestion providers.
 * {@link org.eclipse.babel.editor.widgets.suggestion.SuggestionBubble} gets its
 * suggestion providers from this class, therefore the ones that should be used
 * in SuggestionBubble must be registered in this class by calling {@link
 * SuggestionProviderUtils.addSuggestionProvider()} method
 *
 * @author Samir Soyer
 *
 */
public class SuggestionProviderUtils {
	private static ArrayList<ISuggestionProvider> providers = new ArrayList<ISuggestionProvider>();
	private static ArrayList<ISuggestionProviderListener> listeners = new ArrayList<ISuggestionProviderListener>();

	/**
	 * Adds suggestion provider object to the list
	 *
	 * @param provider
	 *            is the suggestion provider to be registered
	 */
	public static void addSuggestionProvider(ISuggestionProvider provider) {
		if (!providers.contains(provider)) {
			providers.add(provider);
		}
	}

	/**
	 * Removes a specific {@link ISuggestionProvider} from the list of
	 * suggestion providers.
	 *
	 * @param provider
	 *            is the {@link ISuggestionProvider} to be removed from the list
	 */
	public static void removeSuggestionProvider(ISuggestionProvider provider) {
		providers.remove(provider);
	}

	/**
	 * @return all the registered suggestion providers
	 */
	public static ArrayList<ISuggestionProvider> getSuggetionProviders() {
		return providers;
	}

	/**
	 * Adds a new suggestion provider listener, which calls {@link
	 * ISuggestionProviderListener.suggestionProviderUpdated()} method, when a
	 * suggestion provider object is updated
	 *
	 * @param listener
	 *            is the object, whose {@link
	 *            ISuggestionProviderListener.suggestionProviderUpdated()} will
	 *            be called, when the suggestion provider is updated
	 */
	public static void addSuggestionProviderUpdateListener(
			ISuggestionProviderListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method is to call after updating a provider
	 */
	public static void fireSuggestionProviderUpdated(
			ISuggestionProvider provider) {
		for (ISuggestionProviderListener listener : listeners) {
			listener.suggestionProviderUpdated(provider);
		}
	}

	/**
	 * Allows to update one particular configuration setting
	 *
	 * @param setting
	 *            The configuration Setting of type
	 *            {@link ISuggestionProviderConfigurationSetting}
	 */
	public static void updateConfigurationSetting(String configurationId,
			ISuggestionProviderConfigurationSetting setting)
			throws InvalidConfigurationSetting {

		for (ISuggestionProvider provider : providers) {
			if (provider.getAllConfigurationSettings().containsKey(
					configurationId)) {
				provider.updateConfigurationSetting(configurationId, setting);
				fireSuggestionProviderUpdated(provider);
			}
		}

	}
}
