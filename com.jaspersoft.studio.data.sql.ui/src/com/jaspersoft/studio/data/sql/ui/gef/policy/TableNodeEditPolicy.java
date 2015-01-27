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
package com.jaspersoft.studio.data.sql.ui.gef.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.model.query.from.TableJoin;
import com.jaspersoft.studio.data.sql.ui.gef.command.MoveJoinCommand;
import com.jaspersoft.studio.data.sql.ui.gef.parts.RelationshipPart;
import com.jaspersoft.studio.data.sql.ui.gef.parts.TableEditPart;

public class TableNodeEditPolicy extends GraphicalNodeEditPolicy {

	/**
	 * @see GraphicalNodeEditPolicy#getConnectionCreateCommand(CreateConnectionRequest)
	 */
	protected Command getConnectionCreateCommand(CreateConnectionRequest request) {
		return null;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getConnectionCompleteCommand(CreateConnectionRequest)
	 */
	protected Command getConnectionCompleteCommand(CreateConnectionRequest request) {
		return null;
	}

	/**
	 * @see GraphicalNodeEditPolicy#getReconnectSourceCommand(ReconnectRequest)
	 */
	protected Command getReconnectSourceCommand(ReconnectRequest request) {
		TableJoin tj = ((RelationshipPart) request.getConnectionEditPart()).getModel();
		TableEditPart tep = (TableEditPart) getHost();
		SQLQueryDesigner designer = ((TableEditPart) getHost()).getDesigner();
		if (tep.getModel() == tj.getJoinTable())
			return null;
		return new MoveJoinCommand(tep.getModel(), tj, designer);
	}

	/**
	 * @see GraphicalNodeEditPolicy#getReconnectTargetCommand(ReconnectRequest)
	 */
	protected Command getReconnectTargetCommand(ReconnectRequest request) {
		TableJoin tj = ((RelationshipPart) request.getConnectionEditPart()).getModel();
		TableEditPart tep = (TableEditPart) getHost();
		if (tep.getModel() == tj.getFromTable())
			return null;

		SQLQueryDesigner designer = ((TableEditPart) getHost()).getDesigner();
		return new MoveJoinCommand(tj, tep.getModel(), designer);
	}

}
