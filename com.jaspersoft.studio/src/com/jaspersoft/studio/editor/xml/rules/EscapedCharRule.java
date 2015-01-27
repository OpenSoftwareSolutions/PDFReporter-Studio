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

public class EscapedCharRule implements IRule {

	IToken fToken;
	StringBuffer buffer = new StringBuffer();

	public EscapedCharRule(IToken token) {
		super();
		this.fToken = token;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {

		buffer.setLength(0);

		int c = read(scanner);
		if (c == '&') {

			int i = 0;
			do {
				c = read(scanner);
				i++;

				if (c == '<' || c == ']') {
					System.out.println("Char " + (char) c);
					for (int j = i - 1; j > 0; j--)
						scanner.unread();
					return Token.UNDEFINED;
				}
			} while (c != ';');
			return fToken;
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	private int read(ICharacterScanner scanner) {
		int c = scanner.read();
		buffer.append((char) c);
		return c;
	}

}
