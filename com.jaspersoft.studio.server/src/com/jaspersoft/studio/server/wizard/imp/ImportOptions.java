package com.jaspersoft.studio.server.wizard.imp;

import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;

public class ImportOptions {
	private String file;
	private boolean update = true;
	private boolean skipUserUpdates = false;
	private boolean inclAuditEvents = true;
	private boolean inclAccessEvents = true;
	private boolean inclMonitorEvents = true;
	private boolean inclSrvSettings = false;
	private StateDto state;

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

	public boolean isUpdate() {
		return update;
	}

	public void setUpdate(boolean update) {
		this.update = update;
	}

	public boolean isSkipUserUpdates() {
		return skipUserUpdates;
	}

	public void setSkipUserUpdates(boolean skipUserUpdates) {
		this.skipUserUpdates = skipUserUpdates;
	}

	public boolean isInclAuditEvents() {
		return inclAuditEvents;
	}

	public void setInclAuditEvents(boolean inclAuditEvents) {
		this.inclAuditEvents = inclAuditEvents;
	}

	public boolean isInclAccessEvents() {
		return inclAccessEvents;
	}

	public void setInclAccessEvents(boolean inclAccessEvents) {
		this.inclAccessEvents = inclAccessEvents;
	}

	public boolean isInclMonitorEvents() {
		return inclMonitorEvents;
	}

	public void setInclMonitorEvents(boolean inclMonitorEvents) {
		this.inclMonitorEvents = inclMonitorEvents;
	}

	public boolean isInclSrvSettings() {
		return inclSrvSettings;
	}

	public void setInclSrvSettings(boolean inclSrvSettings) {
		this.inclSrvSettings = inclSrvSettings;
	}
}
