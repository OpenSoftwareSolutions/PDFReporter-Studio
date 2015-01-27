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
package com.jaspersoft.studio.server.wizard.permission;

public class PermissionOptions {
	private boolean effectivePermissions = false;
	private boolean recipientTypeUser = false;
	private String recipientId;
	private boolean resolveAll = true;

	public boolean isEffectivePermissions() {
		return effectivePermissions;
	}

	public void setEffectivePermissions(boolean effectivePermissions) {
		this.effectivePermissions = effectivePermissions;
	}

	public boolean isRecipientTypeUser() {
		return recipientTypeUser;
	}

	public void setRecipientTypeUser(boolean recipientTypeUser) {
		this.recipientTypeUser = recipientTypeUser;
	}

	public String getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(String recipientId) {
		this.recipientId = recipientId;
	}

	public boolean isResolveAll() {
		return resolveAll;
	}

	public void setResolveAll(boolean resolveAll) {
		this.resolveAll = resolveAll;
	}
}
