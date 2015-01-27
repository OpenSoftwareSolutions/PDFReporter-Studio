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

import java.util.Collection;
import java.util.Iterator;

/**
 * This class represent a pair of a String and Object. These are supposed to be the human
 * name of an element and the value of the element.  For example the name of a color could be 
 * "Black" and its value could be an SWT or AWT color
 *
 *
 * @author Giulio Toffoli
 */
public class Tag {
    
		/**
		 * The value of the element
		 */
    private Object value;
    
    /**
     * The human name of the element
     */
    private String name = "";
    
    /**
     * Build a pair of the value to represent the human readable value of an element and it 
     * real value.
     * @param value
     * @param name
     */
    public Tag(Object value, String name) {
        setName( name );
        setValue(value);
    }
    
    public Tag(String value) {
        setName( value );
        setValue(value);
    }
    
    public Tag(Object value) {
        setName( value+"");
        setValue(value);
    }
    
    public Tag(int value, String name) {
        setName( name );
        setValue(value);
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
    
    public void setValue(int value) {
        this.value = ""+value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString()
    {
        return getName();
    }
    
    /**
     * Look for the first tag with the specified name.
     */
    public static final Tag findTagByName(String name, Collection<?> c)
    {
            if (c == null) return null;
            Iterator<?> i = c.iterator();
            while (i.hasNext())
            {
                Tag t = (Tag)i.next();
                if ( (name == null && t.getName() == null) ||
                     (t.getName() != null && t.getName().equals(name)) )
                {
                    return t;
                }
            }
            return null;
    }
    
    /**
     * Look for the first tag with the specified name.
     */
    public static final Tag findTagByValue(Object value, Collection<?> c)
    {
            if (c == null) return null;
            Iterator<?> i = c.iterator();
            while (i.hasNext())
            {
                Tag t = (Tag)i.next();
                if ( (value == null && t.getValue() == value) ||
                     (t.getValue() != null && t.getValue().equals(value)) )
                {
                    return t;
                }
            }
            return null;
    }
    
}
