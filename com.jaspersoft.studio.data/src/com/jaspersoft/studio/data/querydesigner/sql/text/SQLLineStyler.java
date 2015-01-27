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
package com.jaspersoft.studio.data.querydesigner.sql.text;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.ResourceManager;

/**
 * This class provides the style information for the SQL text line being drawn.
 * <p>
 * NOTE: Re-used code and idea from JavaViewer SWT Example.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see SQLScanner
 * 
 */
public class SQLLineStyler implements LineStyleListener {

	private SQLScanner sqlScanner;
	private Map<SQLTokensType, Color> colorsMap;
	private List<int[]> blockComments;

	public SQLLineStyler() {
		initializeColors();
		sqlScanner = getSQLBasedScanner();
		blockComments = new ArrayList<int[]>();
	}

	/**
	 * Returns a simple scanner for the SQL-based language.
	 * <p>
	 * 
	 * SQL-like languages implementors should override this method.
	 * 
	 * @return the simple fuzzy scanner for the SQL language
	 */
	protected SQLScanner getSQLBasedScanner() {
		return new SQLScanner();
	}

	/**
	 * Initialize the colors map.
	 */
	protected void initializeColors() {
		colorsMap = new HashMap<SQLTokensType, Color>(SQLTokensType.getColoredTokensNum());
		colorsMap.put(SQLTokensType.STANDARD_TEXT, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(SQLTokensType.KEYWORD, ResourceManager.getColor(126, 12, 88));
		colorsMap.put(SQLTokensType.COMMENT, ResourceManager.getColor(63, 127, 104));
		colorsMap.put(SQLTokensType.QUOTED_LITERAL, ResourceManager.getColor(42, 0, 255));
		colorsMap.put(SQLTokensType.BRACKETED_LITERAL, ResourceManager.getColor(42, 0, 255));
		colorsMap.put(SQLTokensType.SYMBOL, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(SQLTokensType.OTHER, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(SQLTokensType.NUMBER, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(SQLTokensType.JRPARAMETER, ResourceManager.getColor(190, 39, 39));
		colorsMap.put(SQLTokensType.JRFIELD, ResourceManager.getColor(0, 178, 0));
		colorsMap.put(SQLTokensType.JRVARIABLE, ResourceManager.getColor(0, 0, 255));
	}

	/*
	 * Gets the right color for the specified token type.
	 */
	private Color getColor(SQLTokensType type) {
		if (type != null) {
			return colorsMap.get(type);
		}
		return null;
	}

	@Override
	public void lineGetStyle(LineStyleEvent event) {
		Vector<StyleRange> styles = new Vector<StyleRange>();
		SQLTokensType token = null;
		StyleRange lastStyle = null;
		// Check for comments
		if (inBlockComment(event.lineOffset, event.lineOffset + event.lineText.length())) {
			styles.add(new StyleRange(event.lineOffset, event.lineText.length(), getColor(SQLTokensType.COMMENT), null));
			event.styles = new StyleRange[styles.size()];
			styles.copyInto(event.styles);
			return;
		}

		Color defaultFgColor = ((Control) event.widget).getForeground();
		sqlScanner.setRange(event.lineText);
		token = sqlScanner.nextToken();
		while (token != SQLTokensType.EOF) {
			if (token == SQLTokensType.OTHER) {
				// do nothing for non-colored tokens
			} else if (token != SQLTokensType.SPACE) {
				Color color = getColor(token);
				// Only create a style if the token color is different than the
				// widget's default foreground color and the token's style is not bold.
				// Keywords, symbols and operators are drawn bold.
				if ((!color.equals(defaultFgColor)) || (token == SQLTokensType.KEYWORD) || (token == SQLTokensType.SYMBOL)) {
					StyleRange style = new StyleRange(sqlScanner.getStartOffset() + event.lineOffset, sqlScanner.getLength(), color, null);
					if (token == SQLTokensType.KEYWORD || token == SQLTokensType.SYMBOL) {
						style.fontStyle = SWT.BOLD;
					}
					if (styles.isEmpty()) {
						styles.addElement(style);
					} else {
						// Merge similar styles. Doing so will improve performance.
						lastStyle = (StyleRange) styles.lastElement();
						if (lastStyle.similarTo(style) && (lastStyle.start + lastStyle.length == style.start)) {
							lastStyle.length += style.length;
						} else {
							styles.addElement(style);
						}
					}
				}
			} else if ((!styles.isEmpty()) && ((lastStyle = (StyleRange) styles.lastElement()).fontStyle == SWT.BOLD)) {
				int start = sqlScanner.getStartOffset() + event.lineOffset;
				lastStyle = (StyleRange) styles.lastElement();
				// A font style of SWT.BOLD implies that the last style
				// represents a java keyword.
				if (lastStyle.start + lastStyle.length == start) {
					// Have the white space take on the style before it to
					// minimize the number of style ranges created and the
					// number of font style changes during rendering.
					lastStyle.length += sqlScanner.getLength();
				}
			}
			token = sqlScanner.nextToken();
		}
		event.styles = new StyleRange[styles.size()];
		styles.copyInto(event.styles);

	}

	/*
	 * Checks if position range specified is into a block comment.
	 */
	private boolean inBlockComment(int start, int end) {
		for (int i = 0; i < blockComments.size(); i++) {
			int[] offsets = blockComments.get(i);
			// start of comment in the line
			if ((offsets[0] >= start) && (offsets[0] <= end))
				return true;
			// end of comment in the line
			if ((offsets[1] >= start) && (offsets[1] <= end))
				return true;
			if ((offsets[0] <= start) && (offsets[1] >= end))
				return true;
		}
		return false;
	}

	/**
	 * Parses the block comments up front since block comments can go across
	 * lines.
	 * 
	 * @param text
	 *          the text to be parsed
	 */
	public void parseBlockComments(String text) {
		// TODO - This code should be improved, not very efficient for big text.
		blockComments = new ArrayList<int[]>();
		StringReader buffer = new StringReader(text);
		int ch;
		boolean blkComment = false;
		int cnt = 0;
		int[] offsets = new int[2];
		boolean done = false;

		try {
			while (!done) {
				switch (ch = buffer.read()) {
				case -1: {
					if (blkComment) {
						offsets[1] = cnt;
						blockComments.add(offsets);
					}
					done = true;
					break;
				}
				case '/': {
					ch = buffer.read();
					if ((ch == '*') && (!blkComment)) {
						offsets = new int[2];
						offsets[0] = cnt;
						blkComment = true;
						cnt++;
					} else {
						cnt++;
					}
					cnt++;
					break;
				}
				case '*': {
					if (blkComment) {
						ch = buffer.read();
						cnt++;
						if (ch == '/') {
							blkComment = false;
							offsets[1] = cnt;
							blockComments.add(offsets);
						}
					}
					cnt++;
					break;
				}
				default: {
					cnt++;
					break;
				}
				}
			}
		} catch (IOException e) {
			// ignore errors
		}
	}

}
