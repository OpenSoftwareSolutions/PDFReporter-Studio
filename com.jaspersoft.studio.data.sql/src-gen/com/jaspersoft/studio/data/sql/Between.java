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
 * A representation of the model object '<em><b>Between</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.Between#getOpBetween <em>Op Between</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Between#getOp2 <em>Op2</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Between#getOp3 <em>Op3</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getBetween()
 * @model
 * @generated
 */
public interface Between extends EObject
{
  /**
   * Returns the value of the '<em><b>Op Between</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op Between</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op Between</em>' attribute.
   * @see #setOpBetween(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getBetween_OpBetween()
   * @model
   * @generated
   */
  String getOpBetween();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Between#getOpBetween <em>Op Between</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op Between</em>' attribute.
   * @see #getOpBetween()
   * @generated
   */
  void setOpBetween(String value);

  /**
   * Returns the value of the '<em><b>Op2</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op2</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op2</em>' containment reference.
   * @see #setOp2(Operands)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getBetween_Op2()
   * @model containment="true"
   * @generated
   */
  Operands getOp2();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Between#getOp2 <em>Op2</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op2</em>' containment reference.
   * @see #getOp2()
   * @generated
   */
  void setOp2(Operands value);

  /**
   * Returns the value of the '<em><b>Op3</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op3</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op3</em>' containment reference.
   * @see #setOp3(Operands)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getBetween_Op3()
   * @model containment="true"
   * @generated
   */
  Operands getOp3();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Between#getOp3 <em>Op3</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op3</em>' containment reference.
   * @see #getOp3()
   * @generated
   */
  void setOp3(Operands value);

} // Between
