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
package com.jaspersoft.studio.help;

import static java.util.Arrays.asList;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.PlatformUI;

import com.jaspersoft.studio.help.ovverriders.GenericOverrider;
import com.jaspersoft.studio.help.ovverriders.IHelpOverrider;
import com.jaspersoft.studio.help.ovverriders.RegularExpressionOverrider;
import com.jaspersoft.studio.help.ovverriders.StylesOverrider;

/**
 * 
 * This class implements an help listener for a table element. The table it is supposed to be a on properties table and when 
 * the help key is pressed a contextual help is opened, showing informations about the selected properties on the table.
 *
 * @author Orlandin Marco
 *
 */
public class TableHelpListener implements Listener {

		/**
		 * A list of overrider, because some attributes dosen't have their documentation on the default location
		 */
		private static List<IHelpOverrider> overrideMap = null;
	
		/**
		 * Table where the help listener is applied
		 */
		private Table table;
		
		
		protected TableHelpListener(Table table){
			this.table = table;
		}
		
		protected IHelpOverrider getOverrider(String propertyName){
			for(IHelpOverrider actualElement : overrideMap){
				if (actualElement.isOverrided(propertyName)) return actualElement;
			}
			return null;
		}
		
		/**
		 * Check if a row of a table is selected, then take the text from the first column of the selected 
		 * row to get the name of the properties. The name is concatenated to the prefix to show the help 
		 * window
		 */
		@Override
		public void handleEvent(Event event) {
			int selectedIndex = table.getSelectionIndex();
			if (selectedIndex != -1){
				String propertyName = table.getItem(selectedIndex).getText(0);
				IHelpOverrider overrider = getOverrider(propertyName);
				URL url = null;
				if (overrider == null) url = PlatformUI.getWorkbench().getHelpSystem().resolve(IHelpOverrider.PREFIX.concat(propertyName), false);
				else  url = PlatformUI.getWorkbench().getHelpSystem().resolve(overrider.getPropertyURL(propertyName), false);
				PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(url.toExternalForm());
			}
		}
		
		/**
		 * Receive a properties table and set the contextual help on that table. The table must be not null.
		 */
		public static void setTableHelp(Table table){
			if (table != null){
				//Initialize the help list if was not done before
				if (overrideMap == null){
					overrideMap = new ArrayList<IHelpOverrider>();
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.export.csv.column.names.", "net.sf.jasperreports.csv.column.names.{arbitrary_name}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.ejbql.query.hint.", "net.sf.jasperreports.ejbql.query.hint.{hint}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.query.executer.factory.", "net.sf.jasperreports.query.executer.factory.{language}"));
					overrideMap.add(new RegularExpressionOverrider(asList("net\\.sf\\.jasperreports\\.compiler\\.\\p{Alnum}+", "^(?!net\\.sf\\.jasperreports\\.compiler\\.classpath).*$"), "net.sf.jasperreports.compiler.{language}"));
					overrideMap.add(new StylesOverrider());
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.chart.renderer.factory.", "net.sf.jasperreports.chart.renderer.factory.{render_type}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.markup.processor.factory.", "net.sf.jasperreports.markup.processor.factory.{markup}"));
					overrideMap.add(new RegularExpressionOverrider("net\\.sf\\.jasperreports\\.export\\.\\p{Alnum}+\\.exclude\\.origin\\.\\p{Alnum}+\\.\\p{Alnum}+", "net.sf.jasperreports.export.{format}.exclude.origin.{suffix}.{arbitrary_name}"));
					overrideMap.add(new RegularExpressionOverrider("net\\.sf\\.jasperreports\\.export\\.\\p{Alnum}+\\.exclude\\.origin\\.keep\\.first\\.\\p{Alnum}+\\.\\p{Alnum}+", "net.sf.jasperreports.export.{format}.exclude.origin.keep.first.{suffix}.{arbitrary_name}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.export.filter.factory.", "net.sf.jasperreports.export.filter.factory.{filter_element}"));
					overrideMap.add(new RegularExpressionOverrider("net\\.sf\\.jasperreports\\.export\\.\\p{Alnum}+\\.default\\.filter\\.factory", "net.sf.jasperreports.export.{arbitrary_name}.default.filter.factory"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.csv.column.names.", "net.sf.jasperreports.csv.column.names.{arbitrary_name}"));
					overrideMap.add(new GenericOverrider("nnet.sf.jasperreports.export.pdf.font.", "net.sf.jasperreports.export.pdf.font.{arbitrary_name}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.export.pdf.fontdir.", "net.sf.jasperreports.export.pdf.fontdir.{arbitrary_name}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.export.xls.column.names.", "net.sf.jasperreports.export.xls.column.names.{suffix}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.export.xls.row.outline.level.", "net.sf.jasperreports.export.xls.row.outline.level.{arbitrary_level}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.export.xls.sheet.names.", "net.sf.jasperreports.export.xls.sheet.names.{arbitrary_name}"));
					overrideMap.add(new RegularExpressionOverrider("net\\.sf\\.jasperreports\\.extension\\.\\p{Alnum}\\.\\p{Alnum}", "net.sf.jasperreports.extension.{registry_id}.{property_suffix}"));
					overrideMap.add(new GenericOverrider("net.sf.jasperreports.extension.registry.factory.", "net.sf.jasperreports.extension.registry.factory.{arbitrary_name}"));
					overrideMap.add(new RegularExpressionOverrider("net\\.sf\\.jasperreports\\.components\\.\\p{Alnum}\\.version", "net.sf.jasperreports.components.{built_in_component_name}.version"));						
				}
				table.addListener(SWT.Help, new TableHelpListener(table));
			}
		}
}
