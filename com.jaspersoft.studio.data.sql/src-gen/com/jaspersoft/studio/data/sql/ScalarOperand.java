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

import java.math.BigDecimal;

import java.util.Date;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Scalar Operand</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSoint <em>Soint</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSostr <em>Sostr</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSodbl <em>Sodbl</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSodate <em>Sodate</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSotime <em>Sotime</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSodt <em>Sodt</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand()
 * @model
 * @generated
 */
public interface ScalarOperand extends OperandList
{
  /**
   * Returns the value of the '<em><b>Soint</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Soint</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Soint</em>' attribute.
   * @see #setSoint(int)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand_Soint()
   * @model
   * @generated
   */
  int getSoint();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSoint <em>Soint</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Soint</em>' attribute.
   * @see #getSoint()
   * @generated
   */
  void setSoint(int value);

  /**
   * Returns the value of the '<em><b>Sostr</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sostr</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sostr</em>' attribute.
   * @see #setSostr(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand_Sostr()
   * @model
   * @generated
   */
  String getSostr();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSostr <em>Sostr</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sostr</em>' attribute.
   * @see #getSostr()
   * @generated
   */
  void setSostr(String value);

  /**
   * Returns the value of the '<em><b>Sodbl</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sodbl</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sodbl</em>' attribute.
   * @see #setSodbl(BigDecimal)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand_Sodbl()
   * @model
   * @generated
   */
  BigDecimal getSodbl();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSodbl <em>Sodbl</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sodbl</em>' attribute.
   * @see #getSodbl()
   * @generated
   */
  void setSodbl(BigDecimal value);

  /**
   * Returns the value of the '<em><b>Sodate</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sodate</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sodate</em>' attribute.
   * @see #setSodate(Date)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand_Sodate()
   * @model
   * @generated
   */
  Date getSodate();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSodate <em>Sodate</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sodate</em>' attribute.
   * @see #getSodate()
   * @generated
   */
  void setSodate(Date value);

  /**
   * Returns the value of the '<em><b>Sotime</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sotime</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sotime</em>' attribute.
   * @see #setSotime(Date)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand_Sotime()
   * @model
   * @generated
   */
  Date getSotime();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSotime <em>Sotime</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sotime</em>' attribute.
   * @see #getSotime()
   * @generated
   */
  void setSotime(Date value);

  /**
   * Returns the value of the '<em><b>Sodt</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sodt</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sodt</em>' attribute.
   * @see #setSodt(Date)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getScalarOperand_Sodt()
   * @model
   * @generated
   */
  Date getSodt();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.ScalarOperand#getSodt <em>Sodt</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sodt</em>' attribute.
   * @see #getSodt()
   * @generated
   */
  void setSodt(Date value);

} // ScalarOperand
