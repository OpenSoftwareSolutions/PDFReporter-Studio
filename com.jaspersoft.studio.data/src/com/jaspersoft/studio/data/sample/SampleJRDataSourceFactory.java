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

import java.util.Vector;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;

/**
*
* @author gtoffoli
*/
public class SampleJRDataSourceFactory {
	
	// This is the method to call to get the datasource.
	// The method must be static.....    
	public JRDataSource createDatasource()
	{
		javax.swing.table.DefaultTableModel tm = new javax.swing.table.DefaultTableModel(4,2);
       
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        tm.setValueAt(person, 0, 0);
        tm.setValueAt("Test value row 1 col 1", 0, 1);
       
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        tm.setValueAt(person, 1, 0);
        tm.setValueAt("Test value row 2 col 1", 1, 1);
       
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        tm.setValueAt(person, 2, 0);
        tm.setValueAt("Test value row 3 col 1", 2, 1);
       
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        tm.setValueAt(person, 3, 0);
        tm.setValueAt("Test value row 4 col 1", 3, 1);
       
        return new JRTableModelDataSource(tm);
    }
	
	public JRDataSource createBeanCollectionDatasource()
    {
        return new JRBeanCollectionDataSource(createBeanCollection());
    }    
    
    public static Vector<?> createBeanCollection()
    {
    	java.util.Vector<PersonBean> coll = new java.util.Vector<PersonBean>();
       
        PersonBean person = new PersonBean();
        person.setFirstName("Giulio");
        person.setLastName("Toffoli");
        person.setEmail("gt@businesslogic.it");
        coll.add(person);
       
        person = new PersonBean();
        person.setFirstName("Teodor");
        person.setLastName("Danciu");
        person.setEmail("teodor@hotmail.com");
        coll.add(person);
       
        person = new PersonBean();
        person.setFirstName("Mario");
        person.setLastName("Rossi");
        person.setEmail("mario@rossi.org");
        coll.add(person);
       
        person = new PersonBean();
        person.setFirstName("Jennifer");
        person.setLastName("Lopez");
        person.setEmail("lopez@jennifer.com");
        coll.add(person);
   
        return coll;
    }
}
