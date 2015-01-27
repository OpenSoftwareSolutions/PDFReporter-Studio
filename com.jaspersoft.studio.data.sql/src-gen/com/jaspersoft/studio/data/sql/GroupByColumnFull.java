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


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Group By Column Full</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.GroupByColumnFull#getColGrBy <em>Col Gr By</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getGroupByColumnFull()
 * @model
 * @generated
 */
public interface GroupByColumnFull extends OrGroupByColumn
{
  /**
   * Returns the value of the '<em><b>Col Gr By</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Col Gr By</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Col Gr By</em>' containment reference.
   * @see #setColGrBy(ColumnFull)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getGroupByColumnFull_ColGrBy()
   * @model containment="true"
   * @generated
   */
  ColumnFull getColGrBy();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.GroupByColumnFull#getColGrBy <em>Col Gr By</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Col Gr By</em>' containment reference.
   * @see #getColGrBy()
   * @generated
   */
  void setColGrBy(ColumnFull value);

} // GroupByColumnFull
