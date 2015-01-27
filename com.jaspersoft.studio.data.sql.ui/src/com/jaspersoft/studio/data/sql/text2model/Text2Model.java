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
package com.jaspersoft.studio.data.sql.text2model;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.model.XtextDocument;
import org.eclipse.xtext.util.concurrent.IUnitOfWork;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.SelectQuery;
import com.jaspersoft.studio.data.sql.SelectSubSet;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.union.CreateUnion;
import com.jaspersoft.studio.data.sql.impl.ModelImpl;
import com.jaspersoft.studio.data.sql.impl.SelectImpl;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.MHaving;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.MWhere;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.utils.Misc;

public class Text2Model {
	private static boolean isRunning = false;

	public static void text2model(final SQLQueryDesigner designer, XtextDocument doc) {
		text2model(designer, doc, false);
	}

	public static void text2model(final SQLQueryDesigner designer, final XtextDocument doc, final boolean showWarning) {
		try {
			if (isRunning)
				return;
			isRunning = true;
			designer.refreshViewer();
			ConvertUtil.cleanDBMetadata(designer.getDbMetadata().getRoot());
			System.out.println("convert the model"); //$NON-NLS-1$
			doc.readOnly(new IUnitOfWork<String, XtextResource>() {
				public String exec(XtextResource resource) {
					if (!resource.getErrors().isEmpty()) {
						if (showWarning && !doc.get().trim().isEmpty())
							UIUtils.showWarning(Messages.Text2Model_warn);
						// designer.showWarning("Parser is not able to convert Query to the model");
						isRunning = false;
						return ""; //$NON-NLS-1$
					}
					designer.showInfo(""); //$NON-NLS-1$

					ANode root = designer.getRoot();
					EList<?> list = resource.getContents();
					if (list != null && !list.isEmpty()) {
						for (Object obj : list) {
							if (obj instanceof ModelImpl) {
								SelectQuery sq = ((ModelImpl) obj).getQuery();
								if (sq instanceof SelectImpl) {
									convertSelect(designer, root, (SelectImpl) sq);
									EList<SelectSubSet> op = ((SelectImpl) sq).getOp();
									if (op != null && !op.isEmpty()) {
										for (SelectSubSet sss : op) {
											MUnion munion = null;
											if (sss.getOp() != null) {
												munion = CreateUnion.createUnion(root);
												String setop = sss.getOp().toUpperCase();
												if (setop.equals(AMKeyword.SET_OPERATOR_UNION) && sss.getAll() != null)
													setop += " ALL"; //$NON-NLS-1$
												munion.setValue(setop);
											}
											convertSelect(designer, Misc.nvl(munion, root), (SelectImpl) sss.getQuery());
										}
									}
								}
							}
							ConvertOrderBy.convertOrderBy(designer, ((ModelImpl) obj).getOrderByEntry());
						}
					}
					isRunning = false;
					return ""; //$NON-NLS-1$
				}
			});
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static void convertSelect(SQLQueryDesigner designer, ANode qroot, SelectImpl sel) {
		ConvertTables.convertTables(designer, qroot, sel.getTbl());
		ConvertSelectColumns.convertSelectColumns(designer, qroot, sel.getCols());
		ConvertExpression.convertExpression(designer, qroot, Util.getKeyword(qroot, MWhere.class), sel.getWhereExpression());
		ConvertGroupBy.convertGroupBy(designer, qroot, sel.getGroupByEntry());
		ConvertExpression.convertExpression(designer, qroot, Util.getKeyword(qroot, MHaving.class), sel.getHavingEntry());
	}
}
