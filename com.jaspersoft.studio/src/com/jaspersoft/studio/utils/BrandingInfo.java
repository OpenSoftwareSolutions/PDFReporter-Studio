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
package com.jaspersoft.studio.utils;

/**
 * Bean representing the basic branding information for the Jaspersoft Studio tool.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class BrandingInfo {
	
	// Common shared constants
	public static final String JSS_INSTALLATION_TYPE_PROPERTY = "jss_installation_type";
	public static final String JSS_PROFESSIONAL_EDITION = "jss_pro";

	// Name identifiers for branding preference properties
	public static final String BRANDING_PRODUCT_NAME = "jssbranding_product_name";
	public static final String BRANDING_PRODUCT_VERSION = "jssbranding_product_version";
	public static final String BRANDING_PRODUCT_MAINBUNDLE = "jssbranding_product_mainbundle";
	
	// a human readable text describing the product
	// possible examples: 
	//	- Jaspersoft Studio plug-in, 
	//	- Jaspersoft Studio Community Edition, 
	// 	- Jaspersoft Studio Professional Edition
	private String productName;
	// the version for the product (plugin/rcp)
	private String productVersion;
	// the main bundle identifying the product
	private String productMainBundleID;
	
	public String getProductName() {
		return productName;
	}
	
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public String getProductVersion() {
		return productVersion;
	}
	
	public void setProductVersion(String productVersion) {
		this.productVersion = productVersion;
	}
	
	public String getProductMainBundleID() {
		return productMainBundleID;
	}
	
	public void setProductMainBundleID(String productMainBundleID) {
		this.productMainBundleID = productMainBundleID;
	}
	
	public static boolean isProfessionalEdition() {
		String type = System.getProperty(JSS_INSTALLATION_TYPE_PROPERTY);
		return JSS_PROFESSIONAL_EDITION.equals(type);
	}
	
	public static boolean isCommunityEdition(){
		return !isProfessionalEdition();
	}
}
