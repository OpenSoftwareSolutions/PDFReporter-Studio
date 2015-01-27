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

import com.jaspersoft.ireport.jasperserver.ws.JServer;
import com.jaspersoft.ireport.jasperserver.ws.WSClient;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.CalendarDaysType;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.IntervalUnit;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.Job;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.JobCalendarTrigger;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.JobMailNotification;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.JobParameter;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.JobRepositoryDestination;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.JobSimpleTrigger;
import com.jaspersoft.ireport.jasperserver.ws.scheduler.ResultSendType;

/**
 *
 * @author gtoffoli
 */
public class ScheduleReport {

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


        createSchedule(client, "/MY_FOLDER/MY_REPORT", "/MY_FOLDER", "A");
    }

    /**
     * This function shows how to create a schedule to run a report once a week and onother once a month
     *
     * @param client
     * @param reportUri
     * @param parameter1
     * @throws Exception
     */
    public static void createSchedule(WSClient client, String reportUri, String destinationUri, String parameter1) throws Exception
    {

        // To delete or list the jobs, use this code
//        JobSummary[] jobs =  client.getReportScheduler().getReportJobs(reportUri);
//
//        if (jobs != null)
//        {
//            for (JobSummary j : jobs)
//            {
//                client.getReportScheduler().deleteJob(j.getId());
//            }
//        }

        JobSimpleTrigger trigger = new JobSimpleTrigger(0,0,"GMT",null,null,-1,1, IntervalUnit.WEEK);
        JobParameter[] params = new JobParameter[1];
        params[0] = new JobParameter("parameter1", parameter1);

        // Create the destination name of the output
        JobRepositoryDestination dest = new JobRepositoryDestination(0,0,destinationUri,true,true,"","yyyyMMdd");

        // Email notification
        JobMailNotification notification = new JobMailNotification(0,0,new String[]{"someone@mycompany.org"},"My Weekly Report","The report of the week.",ResultSendType.SEND_ATTACHMENT,true);

        Job job = new Job(0, 0, reportUri,
                "myuser", "Weekly report", "This is the job description",
                trigger, null,
                params, "weekly_report",
                new String[]{"pdf","xls"}, null, dest,notification);

        client.getReportScheduler().scheduleJob(job);

        System.out.println("Weekly schedule created succesfully.");


        JobCalendarTrigger calendarTrigger = new JobCalendarTrigger();
        calendarTrigger.setMinutes("0");
        calendarTrigger.setHours("0");
        calendarTrigger.setMonthDays("1");
        calendarTrigger.setMonths(new int[]{1,2,3,4,5,6,7,8,9,10,11,12});
        calendarTrigger.setDaysType(CalendarDaysType.MONTH);

        JobMailNotification montlyNotification = new JobMailNotification(0,0,new String[]{"someone@mycompany.org"},"My Monthly Report","The report of the month.",ResultSendType.SEND_ATTACHMENT,true);

        job = new Job();
        job.setLabel("Monthly report");
        job.setReportUnitURI(reportUri);
        job.setBaseOutputFilename("monthly_report");
        job.setUsername("myuser");
        job.setCalendarTrigger(calendarTrigger);
        job.setParameters(params);
        job.setMailNotification(montlyNotification);
        job.setRepositoryDestination(dest);
        job.setParameters(new JobParameter[]{ new JobParameter("parameter1", parameter1) });
        job.setOutputFormats(new String[]{"pdf","xls"});

        client.getReportScheduler().scheduleJob(job);

        System.out.println("Monthly schedule created succesfully.");
    }


    

}
