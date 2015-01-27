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
package com.jaspersoft.studio.data.hbase.querydesigner;

import java.util.HashMap;
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
 * This class provides the style information for the MongoDB query text (JSON based) line being drawn.
 * <p>
 * NOTE: Re-used code and idea from JavaViewer SWT Example. 
 * 
 * @see HBaseScanner
 *
 */
public class HBaseLineStyler implements LineStyleListener {

	private HBaseScanner jsonScanner;
	private Map<JsonTokensType, Color> colorsMap;
	
	public HBaseLineStyler(){
		initializeColors();
		jsonScanner=new HBaseScanner();
	}

	private void initializeColors() {
		colorsMap=new HashMap<JsonTokensType, Color>(JsonTokensType.getColoredTokensNum());
		colorsMap.put(JsonTokensType.TEXT, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(JsonTokensType.KEYWORD, ResourceManager.getColor(196, 58, 34));
		colorsMap.put(JsonTokensType.QUOTED_LITERAL, ResourceManager.getColor(15,128,0));
		colorsMap.put(JsonTokensType.SYMBOL, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(JsonTokensType.OTHER, ResourceManager.getColor(0, 0, 0));
		colorsMap.put(JsonTokensType.NUMBER, ResourceManager.getColor(45, 0, 255));
		colorsMap.put(JsonTokensType.JRPARAMETER,ResourceManager.getColor(178, 0, 0));
		colorsMap.put(JsonTokensType.JRFIELD,ResourceManager.getColor(32,187,34));
		colorsMap.put(JsonTokensType.JRVARIABLE,ResourceManager.getColor(17,18,254));
	}
	
	/*
	 * Gets the right color for the specified token type.
	 */
	private Color getColor(JsonTokensType type){
		if(type!=null){
			return colorsMap.get(type);
		}
		return null;
	}

	@Override
	public void lineGetStyle(LineStyleEvent event) {
		Vector<StyleRange> styles=new Vector<StyleRange>();
		JsonTokensType token=null;
		StyleRange lastStyle=null;
	    
	    Color defaultFgColor = ((Control) event.widget).getForeground();
	    jsonScanner.setRange(event.lineText);
	    token = jsonScanner.nextToken();
	    while (token != JsonTokensType.EOF) {
	      if (token == JsonTokensType.OTHER) {
	        // do nothing for non-colored tokens
	      } else if (token != JsonTokensType.SPACE) {
	        Color color = getColor(token);
	        // Only create a style if the token color is different than the
	        // widget's default foreground color and the token's style is not bold.
	        // Keywords, symbols and operators are drawn bold.
	        if (!color.equals(defaultFgColor) || (token == JsonTokensType.SYMBOL)) {
	          StyleRange style = new StyleRange(jsonScanner.getStartOffset()
	              + event.lineOffset, jsonScanner.getLength(), color,
	              null);
	          if (token==JsonTokensType.SYMBOL) {
	            style.fontStyle = SWT.BOLD;
	          }
	          if (styles.isEmpty()) {
	            styles.addElement(style);
	          } else {
	            // Merge similar styles. Doing so will improve performance.
	            lastStyle = (StyleRange) styles.lastElement();
	            if (lastStyle.similarTo(style)
	                && (lastStyle.start + lastStyle.length == style.start)) {
	              lastStyle.length += style.length;
	            } else {
	              styles.addElement(style);
	            }
	          }
	        }
	      } else if ((!styles.isEmpty())
	          && ((lastStyle = (StyleRange) styles.lastElement()).fontStyle == SWT.BOLD)) {
	        int start = jsonScanner.getStartOffset() + event.lineOffset;
	        lastStyle = (StyleRange) styles.lastElement();
	        // A font style of SWT.BOLD implies that the last style
	        // represents a java keyword.
	        if (lastStyle.start + lastStyle.length == start) {
	          // Have the white space take on the style before it to
	          // minimize the number of style ranges created and the
	          // number of font style changes during rendering.
	          lastStyle.length += jsonScanner.getLength();
	        }
	      }
	      token = jsonScanner.nextToken();
	    }
	    event.styles = new StyleRange[styles.size()];
	    styles.copyInto(event.styles);

	}
}
