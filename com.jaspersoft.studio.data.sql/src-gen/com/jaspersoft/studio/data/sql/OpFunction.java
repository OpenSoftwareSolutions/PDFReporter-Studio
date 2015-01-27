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
 * A representation of the model object '<em><b>Op Function</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.OpFunction#getFname <em>Fname</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.OpFunction#getArgs <em>Args</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getOpFunction()
 * @model
 * @generated
 */
public interface OpFunction extends EObject
{
  /**
   * Returns the value of the '<em><b>Fname</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Fname</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fname</em>' attribute.
   * @see #setFname(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOpFunction_Fname()
   * @model
   * @generated
   */
  String getFname();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.OpFunction#getFname <em>Fname</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fname</em>' attribute.
   * @see #getFname()
   * @generated
   */
  void setFname(String value);

  /**
   * Returns the value of the '<em><b>Args</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Args</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Args</em>' containment reference.
   * @see #setArgs(OpFunctionArg)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOpFunction_Args()
   * @model containment="true"
   * @generated
   */
  OpFunctionArg getArgs();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.OpFunction#getArgs <em>Args</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Args</em>' containment reference.
   * @see #getArgs()
   * @generated
   */
  void setArgs(OpFunctionArg value);

} // OpFunction
