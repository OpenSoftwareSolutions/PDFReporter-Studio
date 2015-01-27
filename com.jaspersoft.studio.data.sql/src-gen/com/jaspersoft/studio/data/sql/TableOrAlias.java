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
 * A representation of the model object '<em><b>Table Or Alias</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.TableOrAlias#getTfull <em>Tfull</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.TableOrAlias#getSq <em>Sq</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.TableOrAlias#getAlias <em>Alias</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.TableOrAlias#getTblAlias <em>Tbl Alias</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getTableOrAlias()
 * @model
 * @generated
 */
public interface TableOrAlias extends EObject
{
  /**
   * Returns the value of the '<em><b>Tfull</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tfull</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tfull</em>' containment reference.
   * @see #setTfull(TableFull)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getTableOrAlias_Tfull()
   * @model containment="true"
   * @generated
   */
  TableFull getTfull();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.TableOrAlias#getTfull <em>Tfull</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tfull</em>' containment reference.
   * @see #getTfull()
   * @generated
   */
  void setTfull(TableFull value);

  /**
   * Returns the value of the '<em><b>Sq</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sq</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sq</em>' containment reference.
   * @see #setSq(SubQueryOperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getTableOrAlias_Sq()
   * @model containment="true"
   * @generated
   */
  SubQueryOperand getSq();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.TableOrAlias#getSq <em>Sq</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sq</em>' containment reference.
   * @see #getSq()
   * @generated
   */
  void setSq(SubQueryOperand value);

  /**
   * Returns the value of the '<em><b>Alias</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Alias</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Alias</em>' attribute.
   * @see #setAlias(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getTableOrAlias_Alias()
   * @model
   * @generated
   */
  String getAlias();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.TableOrAlias#getAlias <em>Alias</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Alias</em>' attribute.
   * @see #getAlias()
   * @generated
   */
  void setAlias(String value);

  /**
   * Returns the value of the '<em><b>Tbl Alias</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Tbl Alias</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Tbl Alias</em>' containment reference.
   * @see #setTblAlias(DbObjectName)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getTableOrAlias_TblAlias()
   * @model containment="true"
   * @generated
   */
  DbObjectName getTblAlias();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.TableOrAlias#getTblAlias <em>Tbl Alias</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Tbl Alias</em>' containment reference.
   * @see #getTblAlias()
   * @generated
   */
  void setTblAlias(DbObjectName value);

} // TableOrAlias
