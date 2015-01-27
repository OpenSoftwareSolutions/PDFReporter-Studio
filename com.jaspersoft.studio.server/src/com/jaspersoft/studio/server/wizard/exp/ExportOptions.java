package com.jaspersoft.studio.server.wizard.exp;

import java.util.ArrayList;
import java.util.List;

import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;

public class ExportOptions {
	private String file;
	private boolean everything = false;
	private boolean incRepositoryPermission = true;
	private boolean incReportJobs = true;
	private boolean roleUsers = false;
	private boolean includeAccessEvents = false;
	private boolean includeAuditEvents = false;
	private boolean includeMonitoringEvents = false;

	private StateDto state;
	private List<String> users = new ArrayList<String>();
	private List<String> roles = new ArrayList<String>();
	private List<String> jobs = new ArrayList<String>();
	private List<String> paths = new ArrayList<String>();

	public List<String> getParameters() {
		List<String> params = new ArrayList<String>();
		if (everything)
			params.add("everything");
		if (incRepositoryPermission)
			params.add("repository-permissions");
		if (incReportJobs)
			params.add("report-jobs");
		if (roleUsers)
			params.add("role-users");
		if (includeAccessEvents)
			params.add("include-access-events");
		if (includeAuditEvents)
			params.add("include-audit-events");
		if (includeMonitoringEvents)
			params.add("include-monitoring-events");
		return params;
	}

	public boolean isIncludeAccessEvents() {
		return includeAccessEvents;
	}

	public void setIncludeAccessEvents(boolean includeAccessEvents) {
		this.includeAccessEvents = includeAccessEvents;
	}

	public boolean isIncludeAuditEvents() {
		return includeAuditEvents;
	}

	public void setIncludeAuditEvents(boolean includeAuditEvents) {
		this.includeAuditEvents = includeAuditEvents;
	}

	public boolean isIncludeMonitoringEvents() {
		return includeMonitoringEvents;
	}

	public void setIncludeMonitoringEvents(boolean includeMonitoringEvents) {
		this.includeMonitoringEvents = includeMonitoringEvents;
	}

	public List<String> getJobs() {
		return jobs;
	}

	public void setJobs(List<String> jobs) {
		this.jobs = jobs;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public List<String> getUsers() {
		return users;
	}

	public void setUsers(List<String> users) {
		this.users = users;
	}

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}

	public StateDto getState() {
		return state;
	}

	public void setState(StateDto state) {
		this.state = state;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public boolean isIncRepositoryPermission() {
		return incRepositoryPermission;
	}

	public void setIncRepositoryPermission(boolean incRepositoryPermission) {
		this.incRepositoryPermission = incRepositoryPermission;
	}

	public boolean isIncReportJobs() {
		return incReportJobs;
	}

	public void setIncReportJobs(boolean incReportJobs) {
		this.incReportJobs = incReportJobs;
	}
}
