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
 * This class contains configuration setting for {@link ISuggestionProvider} in
 * string format, i.e object that contains the configuration setting is a
 * string.
 *
 * @author Samir Soyer
 *
 */
public class StringConfigurationSetting implements
		ISuggestionProviderConfigurationSetting {

	private String config;

	/**
	 * Constructor
	 *
	 * @param config
	 *            is the string that contains the configuration, e.g
	 *            {@code "/home/xyz/file.xml"}
	 */
	public StringConfigurationSetting(String config) {
		super();
		this.config = config;
	}

	/**
	 * @return configuration as string
	 */
	public String getConfig() {
		return config;
	}

	/**
	 * @param config
	 *            is the string that contains the configuration, e.g
	 *            {@code "/home/xyz/file.xml"
	 */
	public void setConfig(String config) {
		this.config = config;
	}

	/**
	 * @see org.eclipse.babel.editor.widgets .suggestion.provider
	 *      .ISuggestionProviderConfigurationSetting#getConfigurationSetting()
	 */
	@Override
	public Object getConfigurationSetting() {
		return config;
	}

}
