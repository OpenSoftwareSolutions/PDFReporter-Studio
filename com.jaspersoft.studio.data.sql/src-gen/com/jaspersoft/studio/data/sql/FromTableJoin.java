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
/**
 */
package com.jaspersoft.studio.data.sql;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>From Table Join</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.FromTableJoin#getJoin <em>Join</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FromTableJoin#getOnTable <em>On Table</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FromTableJoin#getJoinExpr <em>Join Expr</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTableJoin()
 * @model
 * @generated
 */
public interface FromTableJoin extends EObject
{
  /**
   * Returns the value of the '<em><b>Join</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Join</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Join</em>' attribute.
   * @see #setJoin(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTableJoin_Join()
   * @model
   * @generated
   */
  String getJoin();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FromTableJoin#getJoin <em>Join</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Join</em>' attribute.
   * @see #getJoin()
   * @generated
   */
  void setJoin(String value);

  /**
   * Returns the value of the '<em><b>On Table</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>On Table</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>On Table</em>' containment reference.
   * @see #setOnTable(TableOrAlias)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTableJoin_OnTable()
   * @model containment="true"
   * @generated
   */
  TableOrAlias getOnTable();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FromTableJoin#getOnTable <em>On Table</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>On Table</em>' containment reference.
   * @see #getOnTable()
   * @generated
   */
  void setOnTable(TableOrAlias value);

  /**
   * Returns the value of the '<em><b>Join Expr</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Join Expr</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Join Expr</em>' containment reference.
   * @see #setJoinExpr(OrExpr)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTableJoin_JoinExpr()
   * @model containment="true"
   * @generated
   */
  OrExpr getJoinExpr();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FromTableJoin#getJoinExpr <em>Join Expr</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Join Expr</em>' containment reference.
   * @see #getJoinExpr()
   * @generated
   */
  void setJoinExpr(OrExpr value);

} // FromTableJoin
