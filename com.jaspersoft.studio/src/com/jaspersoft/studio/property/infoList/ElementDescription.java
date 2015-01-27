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
package com.jaspersoft.studio.property.infoList;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

/**
 * Define an element that can be listed into a SelectableComposite
 * 
 * @author Orlandin Marco
 *
 */
public class ElementDescription implements Comparable<ElementDescription> {
	
	/**
	 * Textual title of the element
	 */
	private String name;
	
	/**
	 * Textual description of the element
	 */
	private String description;
	
	/**
	 * Styles of the element
	 */
	private List<SortableStyleRange> textStyles;
	
	/**
	 * Class that extends the standard StyleRange making it sortable
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class SortableStyleRange extends StyleRange implements Comparable<SortableStyleRange>{
		@Override
		public int compareTo(SortableStyleRange o) {
			return (start + length) - (o.start + o.length);
		}	
	}
	
	/**
	 * Create an element for the SelectableComposite
	 * 
	 * @param name the title of the element, displayed in bold style
	 * @param description the textual description of the element
	 * @param parseHtml this class has a really basic html parser to format a description that contains
	 * html tags. Set this value to true to parse the description or to false to use it as it is.
	 */
	public ElementDescription(String name, String description, boolean parseHtml){
		this.name = name;
		this.description = description;
		textStyles = new ArrayList<SortableStyleRange>();
		if (parseHtml) parseHTMLdescription();
	}
	
	/**
	 * Utility method used to replace all the occurrences of a pattern inside a string
	 * 
	 * @param regex regular expression of the pattern
	 * @param replacment replacment string
	 */
	private void replaceAll(String regex, String replacment){
		Pattern p = Pattern.compile(regex);
		Matcher matcher = p.matcher(description);
		description = matcher.replaceAll(replacment);
	}
	
	/**
	 * Remove the code tag from the description and generate the styles to have it in italic
	 * 
	 * @param index position where the <code> tag start
	 */
	private void removeCode(int index){
		if (index != -1){
			int endIndex = description.indexOf("</code>");
			SortableStyleRange codeRange = new SortableStyleRange();
			codeRange.start = index;
			codeRange.length = endIndex - index -6;
			codeRange.fontStyle = SWT.ITALIC;
			textStyles.add(codeRange);
			description = description.substring(0, index) + description.substring(index+6, endIndex) + description.substring(endIndex+7);
		}
	}
	
	/**
	 * Remove the <b> tag from the description and generate the styles to have it in bold
	 * 
	 * @param index position where the <b> tag start
	 */
	private void removeBold(int index){
		if (index != -1){
			int endIndex = description.indexOf("</b>");
			SortableStyleRange codeRange = new SortableStyleRange();
			codeRange.start = index;
			codeRange.length = endIndex - index -3;
			codeRange.fontStyle = SWT.BOLD;
			textStyles.add(codeRange);
			description = description.substring(0, index) + description.substring(index+3, endIndex) + description.substring(endIndex+4);
		}
	}
	
	/**
	 * Remove the <api> tag from the description and generate the styles to have it as link
	 * 
	 * @param index position where the <api> tag start
	 */
	private void removeLink(int index){
		if (index != -1){
			int startLinkIndex = description.indexOf("\"", index+1);
			int endLinkIndex = description.indexOf("\"", startLinkIndex+1);
			int closeApiIndex = description.indexOf(">", endLinkIndex);
			String link = description.substring(startLinkIndex+1, endLinkIndex);
			int endIndex = description.indexOf("</api>");
			String linkText = description.substring(closeApiIndex+1,endIndex);
			String stringStart = description.substring(0,index);
			
			SortableStyleRange codeRange = new SortableStyleRange();
			codeRange.start = stringStart.length();
			codeRange.length = linkText.length();
			codeRange.fontStyle = SWT.UNDERLINE_LINK;
			codeRange.data = link;
			textStyles.add(codeRange);
			description = stringStart+linkText+description.substring(endIndex+6);
		}
	}
	
	/**
	 * Get an array of integers an return the index of the smaller one different from -1
	 * if the are all equals to -1 it return null
	 * 
	 * @param numbersArray array of integer number
	 * @return index of the smaller number in the array different from -1
	 */
	private Integer getMinimum(int[] numbersArray){
		Integer minimumIndex = null;
		int actualMinimum = Integer.MAX_VALUE;
		for(int i=0; i<numbersArray.length; i++){
			int actualNumber = numbersArray[i];
			if (actualNumber != -1 && actualNumber<actualMinimum) {
				minimumIndex = i;
				actualMinimum = actualNumber;
			}
		}
		return minimumIndex;
	}
	
	/**
	 * Parse the description to remove some html tags
	 */
	private void parseHTMLdescription(){
		//remove all the unnecessary white spaces between tags
		replaceAll("> +<", "><");
		//substitute the line break characters with \r\n
		replaceAll("<br>|<br/>|</br>|</ul>|<p>|</p>", "\r\n");
		replaceAll("<ul>|</li>", "");
		replaceAll("<li>", "\r\n -");
		replaceAll("&lt;", "<");
		replaceAll("&gt;", ">");
		
		while (true){
		 int indexCode = description.indexOf("<code>");
		 int indexBold = description.indexOf("<b>");
		 int indexLink = description.indexOf("<api ");
		 //it is really important to remove each time the tag placed first, to avoid to broke the 
		 //char positions of the previously placed StyleRanges
		 Integer min = getMinimum(new int[]{indexCode, indexBold, indexLink});
		 if (min == null) break;
		 if (min == 0) removeCode(indexCode);
		 else if (min == 1)removeBold(indexBold);
		 else if (min == 2) removeLink(indexLink);
		}
	}
	
	/**
	 * return the name
	 * 
	 * @return string representing the name
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * return the description, or its parsed version if the parse html was enabled in the constructor
	 * 
	 * @return
	 */
	public String getDescription(){
		return description;
	}
	
	/**
	 * return the created styleranges as array
	 * 
	 * @return not null array of style ranges
	 */
	public StyleRange[] getStyles(){
		Collections.sort(textStyles);
		return textStyles.toArray(new StyleRange[textStyles.size()]);
	}

	@Override
	public int compareTo(ElementDescription o) {
		return name.compareTo(o.getName());
	}
	
	/**
	 * Retrieves a list of element descriptions from the specified properties file.
	 * 
	 * @param pathname the location of the properties file
	 * @return the list of descriptions
	 */
	public static List<ElementDescription> getPropertiesInformation(String pathname) {
		List<ElementDescription> descriptions = new ArrayList<ElementDescription>();
		try {
			FileInputStream fin = new FileInputStream(new File(pathname));
			Properties props = new Properties();
			props.load(fin);
			for(String pName : props.stringPropertyNames()) {
				descriptions.add(new ElementDescription(pName, props.getProperty(pName), false));
			}
			FileUtils.closeStream(fin);
		} catch (Exception e) {
			JaspersoftStudioPlugin.getInstance().logError(
					NLS.bind("Error occurred while opening the file {0}.",pathname), e);
		}
		Collections.sort(descriptions, new Comparator<ElementDescription>() {
			@Override
			public int compare(ElementDescription o1, ElementDescription o2) {
				if(o1.getName()!=null && o2.getName()!=null) {
					return o1.getName().compareTo(o2.getName());
				}
				else {
					return 0;
				}
			}
		});		
		return descriptions;
	}
}
