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
package com.jaspersoft.studio.data.sample;

public class AddressBean {
	
	private String street;
    
    private String country;
    
    private String state;
    
    /** Creates a new instance of AddressBean */
    public AddressBean() {
    }
    
    /**
     * Getter for property country.
     * @return Value of property country.
     */
    public java.lang.String getCountry() {
        return country;
    }
    
    /**
     * Setter for property country.
     * @param country New value of property country.
     */
    public void setCountry(java.lang.String country) {
        this.country = country;
    }
    
    /**
     * Getter for property state.
     * @return Value of property state.
     */
    public java.lang.String getState() {
        return state;
    }
    
    /**
     * Setter for property state.
     * @param state New value of property state.
     */
    public void setState(java.lang.String state) {
        this.state = state;
    }
    
    /**
     * Getter for property street.
     * @return Value of property street.
     */
    public java.lang.String getStreet() {
        return street;
    }
    
    /**
     * Setter for property street.
     * @param street New value of property street.
     */
    public void setStreet(java.lang.String street) {
        this.street = street;
    }
}
