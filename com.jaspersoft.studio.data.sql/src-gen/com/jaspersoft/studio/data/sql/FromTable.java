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

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>From Table</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.FromTable#getTable <em>Table</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FromTable#getFjoin <em>Fjoin</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTable()
 * @model
 * @generated
 */
public interface FromTable extends OrTable
{
  /**
   * Returns the value of the '<em><b>Table</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Table</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Table</em>' containment reference.
   * @see #setTable(TableOrAlias)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTable_Table()
   * @model containment="true"
   * @generated
   */
  TableOrAlias getTable();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FromTable#getTable <em>Table</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Table</em>' containment reference.
   * @see #getTable()
   * @generated
   */
  void setTable(TableOrAlias value);

  /**
   * Returns the value of the '<em><b>Fjoin</b></em>' containment reference list.
   * The list contents are of type {@link com.jaspersoft.studio.data.sql.FromTableJoin}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Fjoin</em>' containment reference list isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fjoin</em>' containment reference list.
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFromTable_Fjoin()
   * @model containment="true"
   * @generated
   */
  EList<FromTableJoin> getFjoin();

} // FromTable
