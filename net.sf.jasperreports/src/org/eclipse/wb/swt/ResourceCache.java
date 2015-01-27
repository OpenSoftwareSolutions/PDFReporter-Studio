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
package org.eclipse.wb.swt;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;

/**
 * Class used to locally cache elements such fonts, colors, images
 * that can be disposed all at once when they are no more needed
 * 
 * @author Orlandin Marco
 *
 */
public class ResourceCache extends ColorManager{

	protected Map<String, Font> fFontTable = new HashMap<String, Font>(10);
	
	protected Map<Object, Image> fImageTable = new HashMap<Object, Image>(10);
	
	/**
	 * Dispose all the stored resources
	 */
	public void dispose() {
		super.dispose();
		Iterator<Font> f = fFontTable.values().iterator();
		while (f.hasNext())
			f.next().dispose();
		fFontTable.clear();
		Iterator<Image> i = fImageTable.values().iterator();
		while (i.hasNext())
			i.next().dispose();
		fImageTable.clear();
	}

	/**
	 * Get the font
	 * 
	 * @param fontName name of the font
	 * @param fontSize size of the font 
	 * @param fontStyle style of the font
	 * @return the font requested. If already requested before the old instance is 
	 * returned, otherwise a new instance is build
	 */
	public Font getFont(String fontName, int fontSize, int fontStyle) {
		String key = fontName + Integer.toString(fontSize) + Integer.toString(fontStyle);
		Font font = fFontTable.get(key);
		if (font == null) {
			font = new Font(Display.getCurrent(), fontName, fontSize, fontStyle);
			fFontTable.put(key, font);
		}
		return font;
	}
	
	/**
	 * Get the font
	 * 
	 * @param fd data of the font
	 * @return the font requested. If already requested before the old instance is 
	 * returned, otherwise a new instance is build
	 */
	public Font getFont(FontData fd){
		return getFont(fd.getName(), fd.getHeight(), fd.getStyle());
	}
	
	/**
	 * Get the image
	 * 
	 * @param fd data of the image. If it is null the method dosen't store anything 
	 * and return null
	 * @return the image requested. If already requested before the old instance is 
	 * returned, otherwise a new instance is build from the data
	 */
	public Image getImage(ImageData data){
		if (data == null) return null;
		Image img = fImageTable.get(data);
		if (img == null){
			img = new Image(Display.getCurrent(), data);
			fImageTable.put(data, img);
		}
		return img;
	}
	
	/**
	 * Get the image
	 * 
	 * @param stream input stream to the image
	 * @return the image requested. If already requested before the old instance is 
	 * returned, otherwise a new instance is build from the stream
	 */
	public Image getImage(InputStream stream){
		Image img = fImageTable.get(stream);
		if (img == null){
			img = new Image(Display.getCurrent(), stream);
			fImageTable.put(stream, img);
		}
		return img;
	}
	
	/**
	 * Get the image
	 * 
	 * @param key an unique string key that identify the image
	 * @return the image requested if there is some image stored with the specified
	 * key, otherwise null
	 */
	public Image getImage(String key){
		Image img = fImageTable.get(key);
		return img;
	}
	
	/**
	 * Store an image with a key
	 * 
	 * @param key an unique string key that identify the image
	 * @param image the image to store
	 * @return true if the image was stored, false if there is 
	 * already an image stored with the same key. In this case
	 * the image passed is not inserted into the map
	 */
	public boolean storeImage(String key, Image image){
		if (fImageTable.containsKey(key)) return false;
		fImageTable.put(key, image);
		return true;
	}
	
}
