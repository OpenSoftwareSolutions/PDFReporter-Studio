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
package com.jaspersoft.studio.property.combomenu;

import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.swt.events.HelpEvent;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.PlatformUI;

/**
 * Add an help listener to a menu, using an URI as link to the help content
 * 
 * @author Orlandin Marco
 *
 */
public class HelpProvider {

	
	/**
	 * The menu where the help listener will be added
	 */
	private Menu openedMenu;
	
	 /**
   * Class that implement an help listener to open an
   * uri link as help
   * @author Orlandin Marco
   *
   */
  private class HelpOpener implements HelpListener {
  	
  	/**
  	 * the uri to open
  	 */
  	private String href;
  	
  	/**
  	 * 
  	 * @param href uri to open
  	 */
  	public HelpOpener(String href){
  		this.href = href;
  	}
  	
  	/**
  	 * Open the selected uri if it is not null
  	 */
		@Override
		public void helpRequested(HelpEvent e) {
			if (href != null) {
				URL url = PlatformUI.getWorkbench().getHelpSystem().resolve(href, false);
				try {
					PlatformUI.getWorkbench().getHelpSystem().displayHelpResource(url.toURI().toASCIIString());
				} catch (URISyntaxException e1) {
					e1.printStackTrace();
				}
			}
		}

  	
		/**
		 * Return the uri to open
		 * @return the uri as a string
		 */
		public String getRef(){
			return href;
		}
		
		/**
		 * Redefinition of the equals, two HelpOpener are equals when they point the same uri
		 */
		@Override
		public boolean equals(Object obj) {
			return (obj instanceof HelpOpener && ((HelpOpener)obj).getRef().equals(href));
		}
	};
	
	/**
	 * Create an instance of the class
	 * @param opendMenu menu where the help listener will be added
	 */
	public HelpProvider(Menu opendMenu){
		this.openedMenu = opendMenu;
	}
	
	/**
	 * Set the listener to the menu specified on the creation of the class, and the content 
	 * of the help are linked using the uri passed as parameter
	 * 
	 * @param href the link to the help content
	 * @return the created listener, or null is the menu is null
	 */
	public HelpListener setHelp(String href){
		if (openedMenu != null){
			HelpOpener handler = new HelpOpener(href);
			openedMenu.removeHelpListener(handler);
			openedMenu.addHelpListener(handler);
			return handler;
		}
		return null;
	}
}
