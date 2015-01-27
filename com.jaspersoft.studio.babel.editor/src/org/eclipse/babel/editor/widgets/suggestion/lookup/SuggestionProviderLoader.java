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
package org.eclipse.babel.editor.widgets.suggestion.lookup;

import org.eclipse.babel.editor.widgets.suggestion.provider.ISuggestionProvider;
import org.eclipse.babel.editor.widgets.suggestion.provider.SuggestionProviderUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.ISafeRunnable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SafeRunner;

/**
 * Look up for suggestion providers, which implements the extension point
 * {@literal "org.eclipselabs.tapiji.translator.suggestion"}.
 * @author Samir Soyer
 *
 */
public class SuggestionProviderLoader {
	private static final String ISUGGESTIONPROVIDER_ID = "com.jaspersoft.studio.babel.editor.suggestion";

	/**
	 * Finds all the suggestion providers resp. extensions that implement
	 * {@code org.eclipselabs.tapiji.translator.suggestion} extension point.
	 * Then registers them at
	 * {@link org.eclipse.babel.editor.widgets.
	 * suggestion.provider.SuggestionProviderUtils}
	 */
	public static void registerProviders() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();

		IConfigurationElement[] config =
				registry.getConfigurationElementsFor(ISUGGESTIONPROVIDER_ID);

		try {
			for (IConfigurationElement e : config) {
				final Object o =
						e.createExecutableExtension("class");
				if (o instanceof ISuggestionProvider) {
					executeExtension(o);
				}
			}
		} catch (CoreException ex) {
			//TODO logging
			//			System.out.println(ex.getMessage());
		}
	}

	private static void executeExtension(final Object o) {
		ISafeRunnable runnable = new ISafeRunnable() {
			@Override
			public void handleException(Throwable e) {
				//TODO logging
				//				System.out.println("Exception in extension");
			}

			@Override
			public void run() throws Exception {
				ISuggestionProvider provider = ((ISuggestionProvider) o);
				SuggestionProviderUtils.addSuggestionProvider(provider);
			}
		};
		SafeRunner.run(runnable);
	}
}
