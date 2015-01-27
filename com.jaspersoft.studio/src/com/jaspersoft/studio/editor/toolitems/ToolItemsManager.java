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
package com.jaspersoft.studio.editor.toolitems;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;



public class ToolItemsManager {
	
	/**
	 * Map with the default visibilities for the contributed controls. If an item is 
	 * not in this map then its default visibility is false
	 */
	private static final Map<String, Boolean> staticDefaultValues = new HashMap<String, Boolean>();
	
	static{
		staticDefaultValues.put("com.jaspersoft.studio.graphic.bring", false);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.text", true);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.create", true);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.align", false);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.size", true);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.borders", false);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.image", true);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.exporters", false);
		staticDefaultValues.put("com.jaspersoft.studio.graphic.movebands", false);
		staticDefaultValues.put("com.jaspersoft.studio.components.tableactions", true);
		staticDefaultValues.put("com.jaspersoft.studio.components.crosstabaction", true);
		
	}
	
	/**
	 * Cache of the toolbar items loaded from the extension point
	 */
	private List<ToolItemsSet> sets = new ArrayList<ToolItemsSet>();

	/**
	 * Map to pair every control contributed to the toolbar with the id
	 * of the toolbar which it belong
	 */
	private Map<String, String> itemsSetMap = new HashMap<String, String>();

	/**
	 * Cache for the fonts available inside for a specific jasperdesign
	 */
  private HashMap<JasperDesign, String[]> fontsCache = new HashMap<JasperDesign, String[]>();
  
  /**
   * Listener to discard the fonts list cache when something in a jasperdesign changes
   */
  private PropertyChangeListener designChange = new PropertyChangeListener() {
		
		@Override
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getSource() instanceof JasperDesign){
				JasperDesign jd = (JasperDesign)evt.getSource();
				fontsCache.remove(jd);
			}
			
		}
	};
	
	
	public boolean isToolbarVisible(String controlId){
		IPreferenceStore store = JaspersoftStudioPlugin.getInstance().getPreferenceStore();
		String toolbarID = itemsSetMap.get(controlId);
		if (toolbarID == null) return false;
		else {
			return store.getBoolean(toolbarID);
		}
	}
	
	public void init() {
		IConfigurationElement[] cToolItemSets = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio.toolitemsets");
		for (IConfigurationElement toolbar : cToolItemSets) {
			IConfigurationElement[] controls = toolbar.getChildren("toolitem");
			String toolbarId = toolbar.getAttribute("id");
			ToolItemsSet set = new ToolItemsSet();
			set.setId(toolbarId);
			set.setName(toolbar.getAttribute("label"));
			//Set the default visibility
			Boolean defaultVisible = staticDefaultValues.get(toolbarId);
			if (defaultVisible != null) set.setVisible(defaultVisible);
			else set.setVisible(false);
				
			sets.add(set);
			for(IConfigurationElement control : controls){
				String controlId = control.getAttribute("id");
				itemsSetMap.put(controlId, toolbarId);
				set.addControlConfiguration(control);
			}
		}
	}

	/**
	 * Return the list of the fonts available for a specific report
	 * 
	 * @param jrConfig the configuration of the report
	 * @return a not null array of string representing the font names
	 */
  public String[] getFonts(JasperReportsConfiguration jrConfig){  	
  	JasperDesign jd = jrConfig.getJasperDesign();
  	
  	//Set the change listener
  	if (jd != null && jd.getEventSupport() != null){
	  	jd.getEventSupport().removePropertyChangeListener(designChange);
	  	jd.getEventSupport().addPropertyChangeListener(designChange);
  	}
  	String[] fonts = fontsCache.get(jd);
  	if (fonts == null){
  		fonts = jrConfig.getFontList();
  		fontsCache.put(jd, fonts);
  	}
  	return fonts;
  }
	
	public List<ToolItemsSet> getSets() {
		return sets;
	}
	
	public String getParentToolbarId(String controlId){
		return itemsSetMap.get(controlId);
	}

}
