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
 * Interface for suggestion provider configuration settings. Classes that
 * implements this interface can represent the configuration as any type of
 * object, which depends on the implementation of
 * {@code updateConfigurationSetting()} method in {@link ISuggestionProvider}.
 *
 * @author Samir Soyer
 *
 */
public interface ISuggestionProviderConfigurationSetting {

	/**
	 * @return configuration setting in any type of object. Note: implementation
	 *         of {@code updateConfigurationSetting()} method in
	 *         {@link ISuggestionProvider} should be able to handle the returned
	 *         object from this method.
	 */
	Object getConfigurationSetting();

}
