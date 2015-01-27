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
package org.eclipse.babel.editor.widgets.suggestion.exception;

/**
 * This class contains all the error messages that can be used by
 * {@link org.eclipse.babel.editor.widgets.suggestion.provider.ISuggestionProvider}
 *
 * @author Samir Soyer
 *
 */
public class SuggestionErrors {

	/**
	 * to be used, in case suggestion provider can't provide a suggestion
	 */
	public final static String NO_SUGESTION_ERR = "No suggestions available";
	/**
	 * to be used, in case an error occurs that is related to Internet
	 * (Connection / protocol error, etc.)
	 */
	public final static String CONNECTION_ERR = "Connection error, check your internet connection";
	/**
	 * to be used, in case suggestion provider doesn't support a specific
	 * language
	 */
	public final static String LANG_NOT_SUPPORT_ERR = "Language not supported";
	/**
	 * to be used, in case quota for maximum allowed translations were exceeded.
	 */
	public final static String QUOTA_EXCEEDED = "Translation quota has been exceeded";
	/**
	 * to be used, in case selected glossary file can not be read.
	 */
	public final static String INVALID_GLOSSARY = "Invalid glossary file";
	/**
	 * to be used, in case no glossary file is selected.
	 */
	public final static String NO_GLOSSARY_FILE = "Open a glossary to see suggestions";

	/**
	 * Checks whether a string is contained in this class, i.e whether the
	 * string is an error message.
	 *
	 * @param s
	 *            is the string to check.
	 * @return true if string is a error message, otherwise false.
	 */
	public static boolean contains(String s) {
		if (s.equals(SuggestionErrors.LANG_NOT_SUPPORT_ERR)
				|| s.equals(SuggestionErrors.CONNECTION_ERR)
				|| s.equals(SuggestionErrors.NO_SUGESTION_ERR)
				|| s.equals(SuggestionErrors.QUOTA_EXCEEDED)
				|| s.equals(SuggestionErrors.NO_GLOSSARY_FILE)) {
			return true;
		}
		return false;
	}
}
