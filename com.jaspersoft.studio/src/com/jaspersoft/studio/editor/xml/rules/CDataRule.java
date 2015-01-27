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
package com.jaspersoft.studio.editor.xml.rules;

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

public class CDataRule implements IRule {

	IToken fToken;
	StringBuffer buffer = new StringBuffer();
	int charsRead = 0;

	private String matchString;
	private static final String START_MATCH_STRING = "<![CDATA[";
	private static final String END_MATCH_STRING = "]]>";

	public CDataRule(IToken token, boolean start) {
		super();
		this.fToken = token;
		this.matchString = start ? START_MATCH_STRING : END_MATCH_STRING;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {

		buffer.setLength(0);

		charsRead = 0;
		int c = read(scanner);

		if (c == matchString.charAt(0)) {
			do {
				c = read(scanner);
			} while (isOK((char) c));

			if (charsRead == matchString.length()) {
				return fToken;
			} else {
				rewind(scanner);
				return Token.UNDEFINED;
			}

		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	private void rewind(ICharacterScanner scanner) {
		int rewindLength = charsRead;
		while (rewindLength > 0) {
			scanner.unread();
			rewindLength--;
		}
	}

	private int read(ICharacterScanner scanner) {
		int c = scanner.read();
		buffer.append((char) c);
		charsRead++;
		return c;
	}

	private boolean isOK(char c) {
		if (charsRead >= matchString.length())
			return false;
		if (matchString.charAt(charsRead - 1) == c)
			return true;
		else
			return false;
	}
}
