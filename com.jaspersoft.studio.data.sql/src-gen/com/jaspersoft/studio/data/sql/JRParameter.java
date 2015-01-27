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
 * A representation of the model object '<em><b>JR Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.JRParameter#getJrprm <em>Jrprm</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getJRParameter()
 * @model
 * @generated
 */
public interface JRParameter extends Prms
{
  /**
   * Returns the value of the '<em><b>Jrprm</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Jrprm</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Jrprm</em>' attribute.
   * @see #setJrprm(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getJRParameter_Jrprm()
   * @model
   * @generated
   */
  String getJrprm();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.JRParameter#getJrprm <em>Jrprm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Jrprm</em>' attribute.
   * @see #getJrprm()
   * @generated
   */
  void setJrprm(String value);

} // JRParameter
