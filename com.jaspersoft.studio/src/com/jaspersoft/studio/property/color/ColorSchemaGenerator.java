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
package com.jaspersoft.studio.property.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.sf.jasperreports.engine.type.ColorEnum;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * This class provide some methods to easily define Colors and Gradiations, and in general to generate 
 * colors. It also embed a series of common colors, with their name and hexadecimal code.
 *
 * @author Giulio Toffoli & Orlandin Marco
 */
public class ColorSchemaGenerator {
    
		/**
		 * the available values for the gradation
		 * 
		 * @author Orlandin Marco
		 *
		 */
    public static enum SCHEMAS{DEFAULT, PASTEL, SOFT, HARD, LIGHT, PALE};
    
    /**
     * Alteration value for every schema available
     */
    private static float[] schema_default = new float[]{ -1f,-1f, 1f,-0.7f, 0.25f,1f, 0.5f,1f };
    private static float[] schema_pastel = new float[]{ 0.5f,-0.9f, 0.5f,0.5f, 0.1f,0.9f, 0.75f,0.75f };
    private static float[] schema_soft = new float[]{ 0.3f,-0.8f, 0.3f,0.5f, 0.1f,0.9f, 0.5f,0.75f };
    private static float[] schema_hard = new float[]{ 1f,-1f, 1f,-0.6f, 0.1f,1f, 0.6f,1f };
    private static float[] schema_light = new float[]{ 0.25f,1f, 0.5f,0.75f, 0.1f,1f, 0.5f,1f };
    private static float[] schema_pale = new float[]{ 0.1f,-0.85f, 0.1f,0.5f, 0.1f,1f, 0.1f,0.75f };
    
    private static java.util.Map<SCHEMAS, float[]> schemas = new HashMap<SCHEMAS, float[]>();
    
    static {
        
        schemas.put(SCHEMAS.DEFAULT, schema_default);
        schemas.put(SCHEMAS.PASTEL, schema_pastel);
        schemas.put(SCHEMAS.SOFT, schema_soft);
        schemas.put(SCHEMAS.HARD, schema_hard);
        schemas.put(SCHEMAS.LIGHT, schema_light);
        schemas.put(SCHEMAS.PALE, schema_pale);
        
    }
    
    /**
     * Create a gradation of a color
     * 
     * @param base the base color
     * @param i the gradation of the color (a number between 0 and 3)
     * @param schemaName the variation of the color 
     * @return the generated color, as an AWT Color
     */
    public static Color createColor(Color base, int i, SCHEMAS schemaName)
    {
        
        i = Math.abs(i %= 3); 
        if (schemaName == null) schemaName = SCHEMAS.SOFT;
        float[] schema = schemas.get(schemaName);
        
        float[] components = Color.RGBtoHSB(base.getRed(), base.getGreen(), base.getBlue(), null);
	
        components[1] = (schema[i*2] < 0) ? -schema[i*2] * components[1] : schema[i*2];
        if (components[1] > 1) components[1] = 1.0f;
        if (components[1] < 0) components[1] = 0;
        
        
        components[2] = (schema[i*2+1] < 0) ? -schema[i*2+1] * components[2] : schema[i*2+1];
        if (components[2] > 1) components[2] = 1.0f;
        if (components[2] < 0) components[2] = 0;
        
        return new Color( Color.HSBtoRGB(components[0], components[1], components[2]));
    }
    
    /**
     * Create a gradation of a color
     * 
     * @param base the base color
     * @param i the gradation of the color (a number between 0 and 3)
     * @param schemaName the variation of the color 
     * @return the generated color, as an SWT RGB
     */
    public static AlfaRGB createColor(AlfaRGB base, int i, SCHEMAS schemaName){
    		RGB baseRGB = base.getRgb();
    		Color createdColor =  createColor(new Color(baseRGB.red, baseRGB.green, baseRGB.blue), i, schemaName);
    		return new AlfaRGB(new RGB(createdColor.getRed(), createdColor.getGreen(), createdColor.getBlue()),base.getAlfa());
    }
    
    /**
     * Return a list of string of all the available colors embedded in the class
     * @return a list with the human name of the color
     */
    public static List<String> getColors()
    {
        if (colorsList == null)
        {
            colorsList = new ArrayList<String>();
            colorsMap = new HashMap<String,String>();
            namesMap = new HashMap<String, String>();
            
            for (int i=0; i<colors.length/2; ++i)
            {
                colorsList.add( colors[i*2] );
                colorsMap.put(colors[i*2], colors[(i*2)+1]);
                namesMap.put(colors[(i*2)+1], colors[i*2]);
            }
        }
        
        return colorsList;
    }

    /**
     * Return a a preview image of an embedded color. The image will have size of 
     * 20x10. The image are cached, so it will be generated only the first time that are 
     * requested.
     * 
     * @param colorName the human name of the color
     * @return an SWT image, preview of the color. It can be null if the human name dosen't match 
     * none of the embedded colors.
     */
    public static Image getImagePreview(String colorName){
			//The images are cached and disposed at the end
			String key = "colorPreview_"+colorName; //$NON-NLS-1$
			Image image = ResourceManager.getImage(key);
			if (image == null){
				if (colorsMap == null) getColors();
				String colorCode = colorsMap.get(colorName);
				if (colorCode != null){
					Color color = decodeColor("#"+colorCode); //$NON-NLS-1$
					RGB colorRGB = new RGB(color.getRed(), color.getGreen(), color.getBlue());
					ImageData data = new ImageData(20, 10, 1, new PaletteData(new RGB[]{colorRGB}));
					image = new Image(null,data); 
					GC graphics = new GC(image);
					graphics.setForeground(ColorConstants.black);
					graphics.drawRectangle(0,0,19,9);
					graphics.dispose();
					ResourceManager.addImage(key, image);
				} else return null;
			}
			return image;
  	}
				
    
    /**
     * Return an embedded color, starting from it embedded human name
     * 
     * @param name the human name of an embedded color
     * @return the AWT color, or null if the provided name doesn't math any embedded color
     */
    public static Color getColor(String name)
    {
        if (colorsMap == null)
        {
            getColors();
        }
        String rgb = colorsMap.get(name);
        return decodeColor("#"+rgb); //$NON-NLS-1$
    }
    
    /**
     * From a color return its name
     * 
     * @param color the color 
     * @return the name of the color, or null if the color is unsopported
     */
    public static String getName(RGB color){
      String hex = Integer.toHexString(color.red) + Integer.toHexString(color.green) +  
          						Integer.toHexString(color.blue);
      return namesMap.get(hex.toUpperCase());
    }
    
    
    /**
     * Given the hexadecimal value of a color, it will be converted into an AWT color object
     * 
     * @param colorString a string representing the hexadecimal value of a color, it can have or not the 
     * starting symbol "#".
     * 
     * @return an AWT color
     */
    public static Color decodeColor(String colorString)
    {
        java.awt.Color color = null;
        if (colorString == null) return null;
        char firstChar = colorString.charAt(0);
        if (firstChar == '#')
        {
               color = new java.awt.Color(Integer.parseInt(colorString.substring(1), 16));
        }
        else if ('0' <= firstChar && firstChar <= '9')
        {
               color = new java.awt.Color(Integer.parseInt(colorString));
        }
        else
        {
                if (ColorEnum.getByName(colorString) != null)
                {
                        color = ColorEnum.getByName(colorString).getColor();
                }
                else
                {
                        color = java.awt.Color.black;
                }
        }
        return color;

    }
    
    /**
     * Return a list of all the available variations with their name and value
     * @return a list of pairs, where every pair is composed of the name of a variations and its enumeration value
     */
    static public List<Tag> getVariants()
    {
        List<Tag> variants = new ArrayList<Tag>();
        variants.add(new Tag(SCHEMAS.DEFAULT, Messages.ColorSchemaGenerator_schema_default));
        variants.add(new Tag(SCHEMAS.PASTEL, Messages.ColorSchemaGenerator_schema_pastel));
        variants.add(new Tag(SCHEMAS.SOFT, Messages.ColorSchemaGenerator_schema_darkpastel));
        variants.add(new Tag(SCHEMAS.HARD, Messages.ColorSchemaGenerator_schema_lightPastel));
        variants.add(new Tag(SCHEMAS.LIGHT, Messages.ColorSchemaGenerator_schema_contrast));
        variants.add(new Tag(SCHEMAS.PALE, Messages.ColorSchemaGenerator_schema_pale));
        return variants;
    }

    /**
     * saturates the color and return a new color.
     * 
     * @param color the base color
     * @param value (0-1.0) value of the saturation
     * @return the new Color
     */
    public static Color desaturate(Color color, float value)
    {

         float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
         hsb[1] -= hsb[1]*value;
         if (hsb[1] < 0f) hsb[1] = 0f;
         return new Color( Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    /**
     * change the brightness of a color an return a new color
     * 
     * @param color the base color
     * @param value (0-1.0) value of the brightness
     * @return the new Color
     */
    public static Color bright(Color color, float value)
    {

         float[] hsb = Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), null);
         hsb[2] += hsb[2]*value;
         if (hsb[2] > 1.0f) hsb[2] = 1.0f;
         return new Color( Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }


    /**
     * Make a color brighter and return a new color
     * @param color the base color 
     * @return the new color
     */
    public static java.awt.Color overlayWhite(Color color)
    {

         return new Color((color.getRed()+255)/2, (color.getGreen()+255)/2, (color.getBlue()+255)/2);
    }
    
    /**
     * The human name list of all the color embedded in the class
     */
    static private List<String> colorsList = null;
    
    /**
     * Hashmap where at every human name of a color is associated it hexadecimal value
     */
    static private HashMap<String, String> colorsMap = null;
    
    /**
     * Hashmap where at every supported color is associated its name
     */
    static private HashMap<String, String> namesMap = null;
    
    /**
     * Array of string where every odd position is the human name of a color, and the pair position 
     * are the hexadecimal value associated at the name of the precedent position
     */
    static private String[] colors = new String[]{
            "Aliceblue","F0F8FF", 
            "Antiquewhite","FAEBD7",
            "Aqua","00FFFF",
            "Aquamarine","7FFFD4",
            "Azure","F0FFFF",
            "Beige","F5F5DC",
            "Bisque","FFE4C4",
            "Black","000000",
            "Blanchedalmond","FFEBCD",
            "Blue","0000FF",
            "Blueviolet","8A2BE2",
            "Brown","A52A2A",
            "Burlywood","DEB887",
            "Cadetblue","5F9EA0",
            "Chartreuse","7FFF00",
            "Chocolate","D2691E",
            "Coral","FF7F50",
            "Cornflowerblue","6495ED",
            "Cornsilk","FFF8DC",
            "Crimson","DC143C",
            "Cyan","00FFFF",
            "Darkblue","00008B",
            "Darkcyan","008B8B",
            "Darkgoldenrod","B8860B",
            "Darkgray","A9A9A9",
            "Darkgreen","006400",
            "Darkkhaki","BDB76B",
            "Darkmagenta","8B008B",
            "Darkolivegreen","556B2F",
            "Darkorange","FF8C00",
            "Darkorchid","9932CC",
            "Darkred","8B0000",
            "Darksalmon","E9967A",
            "Darkseagreen","8FBC8F",
            "Darkslateblue","483D8B",
            "Darkturqoise","00CED1",
            "Darkslategray","2F4F4F",
            "Darkviolet","9400D3",
            "Deeppink","FF1493",
            "Deepskyblue","00BFFF",
            "Dimgray","696969",
            "Dodgerblue","1E90FF",
            "Firebrick","B22222",
            "Floralwhite","FFFAF0",
            "Forestgreen","228B22",
            "Fuchsia","FF00FF",
            "Gainsboro","DCDCDC",
            "Ghostwhite","F8F8FF",
            "Gold","FFD700",
            "Goldenrod","DAA520",
            "Gray","808080",
            "Green","008000",
            "Greenyellow","ADFF2F",
            "Honeydew","F0FFF0",
            "Hotpink","FF69B4",
            "Indianred","CD5C5C",
            "Indigo","4B0082",
            "Ivory","FFFFF0",
            "Khaki","F0E68C",
            "Lavender","E6E6FA",
            "Lavenderblush","FFF0F5",
            "Lawngreen","7CFC00",
            "Lemonchiffon","FFFACD",
            "Lightblue","ADD8E6",
            "Lightcoral","F08080",
            "Lightcyan","E0FFFF",
            "Lightgoldenrodyellow","FAFAD2",
            "Lightgreen","90EE90",
            "Lightgrey","D3D3D3",
            "Lightpink","FFB6C1",
            "Lightsalmon","FFA07A",
            "Lightseagreen","20B2AA",
            "Lightskyblue","87CEFA",
            "Lightslategray","778899",
            "Lisghtsteelblue","B0C4DE",
            "Lightyellow","FFFFE0",
            "Lime","00FF00",
            "Limegreen","32CD32",
            "Linen","FAF0E6",
            "Magenta","FF00FF",
            "Maroon","800000",
            "Mediumaquamarine","66CDAA",
            "Mediumblue","0000CD",
            "Mediumorchid","BA55D3",
            "Mediumpurple","9370DB",
            "Mediumseagreen","3CB371",
            "Mediumslateblue","7B68EE",
            "Mediumspringgreen","00FA9A",
            "Mediumturquoise","48D1CC",
            "Mediumvioletred","C71585",
            "Midnightblue","191970",
            "Mintcream","F5FFFA",
            "Mistyrose","FFE4E1",
            "Moccasin","FFE4B5",
            "Navajowhite","FFDEAD",
            "Navy","000080",
            "Navyblue","9FAFDF",
            "Oldlace","FDF5E6",
            "Olive","808000",
            "Olivedrab","6B8E23",
            "Orange","FFA500",
            "Orangered","FF4500",
            "Orchid","DA70D6",
            "Palegoldenrod","EEE8AA",
            "Palegreen","98FB98",
            "Paleturquoise","AFEEEE",
            "Palevioletred","DB7093",
            "Papayawhip","FFEFD5",
            "Peachpuff","FFDAB9",
            "Peru","CD853F",
            "Pink","FFC0CB",
            "Plum","DDA0DD",
            "Powderblue","B0E0E6",
            "Purple","800080",
            "Red","FF0000",
            "Rosybrown","BC8F8F",
            "Royalblue","4169E1",
            "Saddlebrown","8B4513",
            "Salmon","FA8072",
            "Sandybrown","F4A460",
            "Seagreen","2E8B57",
            "Seashell","FFF5EE",
            "Sienna","A0522D",
            "Silver","C0C0C0",
            "Skyblue","87CEEB",
            "Slateblue","6A5ACD",
            "Snow","FFFAFA",
            "Springgreen","00FF7F",
            "Steelblue","4682B4",
            "Tan","D2B48C",
            "Teal","008080",
            "Thistle","D8BFD8",
            "Tomato","FF6347",
            "Turquoise","40E0D0",
            "Violet","EE82EE",
            "Wheat","F5DEB3",
            "White","FFFFFF",
            "Whitesmoke","F5F5F5",
            "Yellow","FFFF00",
            "Yellowgreen","9ACD32"};
}
