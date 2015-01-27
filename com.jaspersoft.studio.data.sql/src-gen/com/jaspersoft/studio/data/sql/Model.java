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
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.Model#getQuery <em>Query</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Model#getOrderByEntry <em>Order By Entry</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getModel()
 * @model
 * @generated
 */
public interface Model extends EObject
{
  /**
   * Returns the value of the '<em><b>Query</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Query</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Query</em>' containment reference.
   * @see #setQuery(SelectQuery)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getModel_Query()
   * @model containment="true"
   * @generated
   */
  SelectQuery getQuery();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Model#getQuery <em>Query</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Query</em>' containment reference.
   * @see #getQuery()
   * @generated
   */
  void setQuery(SelectQuery value);

  /**
   * Returns the value of the '<em><b>Order By Entry</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Order By Entry</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Order By Entry</em>' containment reference.
   * @see #setOrderByEntry(OrOrderByColumn)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getModel_OrderByEntry()
   * @model containment="true"
   * @generated
   */
  OrOrderByColumn getOrderByEntry();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Model#getOrderByEntry <em>Order By Entry</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Order By Entry</em>' containment reference.
   * @see #getOrderByEntry()
   * @generated
   */
  void setOrderByEntry(OrOrderByColumn value);

} // Model
