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
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.jaspersoft.ireport.jasperserver.ws.examples;

import java.io.File;
import java.io.IOException;

import com.jaspersoft.ireport.jasperserver.ws.JServer;
import com.jaspersoft.ireport.jasperserver.ws.WSClient;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;

/**
 *
 * @author gtoffoli
 */
public class CreateReportUnit {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here

        JServer server = new JServer();

        server.setUrl("http://127.0.0.1:8080/jasperserver-pro/services/repository");
        server.setUsername("jasperadmin|organization_1");
        server.setPassword("jasperadmin");

        WSClient client = new WSClient(server);

        createFolder(client, "My Sample Folder", "MY_FOLDER");
        createReportUnit(client, "/MY_FOLDER", "MY_REPORT", "My Sample Report", "c:\\test.jrxml");
        addReportResource(client, "/MY_FOLDER/MY_REPORT", "logo.gif", "c:\\logo.gif");
        addReportInputControl(client, "/MY_FOLDER/MY_REPORT", "parameter1", "Accounts starting with");
    }

    /**
     * This function shows how to create a folder in the root directory.
     * Subfolders can be created just specifying a proper Uri string i.e.
     * rd.setUriString("/this/is/my/new/folder");
     *
     * @param client
     * @param folderLabel
     * @param folderName
     * @throws IOException
     */
    public static void createFolder(WSClient client, String folderLabel, String folderName) throws IOException
    {
            System.out.println("\n\n-----------" + folderName + " --------------");
            ResourceDescriptor rd = new ResourceDescriptor();
            rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
            rd.setUriString("/" + folderName);
            rd.setName(folderName);
            rd.setLabel(folderLabel);


            // If you want delete a folder in case it already exists, just uncomment this code

            //try {
            //    client.delete(rd);
            //} catch (Exception ex) { }


            rd.setIsNew(true);

            try {
                client.addOrModifyResource(rd, null);
                System.out.println("Folder " + "/" + folderName + " created");
            } catch (Exception ex)
            {
                System.out.println("Creation of folder " + "/" + folderName + " failed: " + ex.getMessage());
                //ex.printStackTrace();
            }

    }


    private static void createReportUnit(WSClient client, String folder, String name, String label, String jrxmlFile) throws Exception {

        String reportUnitUri = folder + "/" + name;
        
        ResourceDescriptor rd = new ResourceDescriptor();
        rd.setName(name);
        rd.setLabel(label);
        rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
        rd.setUriString(folder + "/" + name);
        rd.setParentFolder(folder);
        


        // If you want delete the report in case it already exists, just uncomment this code

        //try {
        //    client.delete(rd);
        //} catch (Exception ex) { }

        rd.setIsNew(true);
        rd.setResourceProperty(ResourceDescriptor.PROP_RU_ALWAYS_PROPMT_CONTROLS, true);

        ResourceDescriptor datasource = new ResourceDescriptor();
        datasource.setIsNew(true);
        datasource.setWsType(ResourceDescriptor.TYPE_DATASOURCE);
        datasource.setIsReference(true);
        datasource.setReferenceUri("/datasources/JServerJdbcDS");
        rd.getChildren().add(datasource);

        ResourceDescriptor jrxmlDescriptor = new ResourceDescriptor();
        jrxmlDescriptor.setName("main_jrxml");
        jrxmlDescriptor.setLabel("Main Jrxml");
        jrxmlDescriptor.setWsType( ResourceDescriptor.TYPE_JRXML );
        jrxmlDescriptor.setIsNew(true);
        jrxmlDescriptor.setMainReport(true);
        jrxmlDescriptor.setIsReference(false);
        jrxmlDescriptor.setHasData(true);
        rd.getChildren().add( jrxmlDescriptor );

        client.addOrModifyResource( rd, new File(jrxmlFile));
        
        System.out.println("  Created report " + label);
        System.out.flush();
    }


    private static void addReportResource(WSClient client, String reportUnitUri, String name, String imageFile) throws Exception {

        // Add a simple Resources...
        ResourceDescriptor resourceDescriptor = new ResourceDescriptor();
        resourceDescriptor.setWsType( ResourceDescriptor.TYPE_IMAGE );
        resourceDescriptor.setName(name);
        resourceDescriptor.setLabel(name);
        resourceDescriptor.setIsNew(true);
        resourceDescriptor.setHasData(true);
        resourceDescriptor.setUriString(reportUnitUri+"/" + name);

        client.modifyReportUnitResource(reportUnitUri, resourceDescriptor, new File(imageFile));

        System.out.println("  Image " + name + " added to the report");
        System.out.flush();
    }


    private static void addReportInputControl(WSClient client, String reportUnitUri, String name, String label) throws Exception {



        // Let's add a simple input controls...
        ResourceDescriptor icDescriptor = new ResourceDescriptor();
        icDescriptor.setName(name);
        icDescriptor.setLabel(label);
        icDescriptor.setIsNew(true);
        icDescriptor.setIsReference(false);
        icDescriptor.setWsType( ResourceDescriptor.TYPE_INPUT_CONTROL );
        icDescriptor.setResourceProperty( ResourceDescriptor.PROP_INPUTCONTROL_TYPE, ResourceDescriptor.IC_TYPE_SINGLE_VALUE);
        icDescriptor.setUriString(reportUnitUri+"/<controls>/"+name);

        

        // Now we need to add the input control data type...
        ResourceDescriptor dataTypeDescriptor = new ResourceDescriptor();
        dataTypeDescriptor.setName("myDatatype");
        dataTypeDescriptor.setLabel("Simple text data type");
        dataTypeDescriptor.setIsNew(true);
        dataTypeDescriptor.setIsReference(false);
        dataTypeDescriptor.setWsType( ResourceDescriptor.TYPE_DATA_TYPE );
        dataTypeDescriptor.setDataType( ResourceDescriptor.DT_TYPE_TEXT);
        dataTypeDescriptor.setUriString(reportUnitUri+"/<controls>/" + name +"/myDatatype");

        
        if (icDescriptor.getChildren() == null) icDescriptor.setChildren(new java.util.ArrayList());
        icDescriptor.getChildren().add(dataTypeDescriptor);
        
        client.modifyReportUnitResource(reportUnitUri, icDescriptor, null);

        System.out.println("  Input control " + name + " added to the report");
        System.out.flush();

    }

}
