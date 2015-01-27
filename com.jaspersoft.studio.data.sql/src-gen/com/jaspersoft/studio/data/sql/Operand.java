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
 * A representation of the model object '<em><b>Operand</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getColumn <em>Column</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getXop <em>Xop</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getSubq <em>Subq</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getFcast <em>Fcast</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getFunc <em>Func</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getSqlcase <em>Sqlcase</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getParam <em>Param</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getEparam <em>Eparam</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.Operand#getScalar <em>Scalar</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand()
 * @model
 * @generated
 */
public interface Operand extends EObject
{
  /**
   * Returns the value of the '<em><b>Column</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Column</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Column</em>' containment reference.
   * @see #setColumn(ColumnOperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Column()
   * @model containment="true"
   * @generated
   */
  ColumnOperand getColumn();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getColumn <em>Column</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Column</em>' containment reference.
   * @see #getColumn()
   * @generated
   */
  void setColumn(ColumnOperand value);

  /**
   * Returns the value of the '<em><b>Xop</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Xop</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Xop</em>' containment reference.
   * @see #setXop(Operand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Xop()
   * @model containment="true"
   * @generated
   */
  Operand getXop();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getXop <em>Xop</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Xop</em>' containment reference.
   * @see #getXop()
   * @generated
   */
  void setXop(Operand value);

  /**
   * Returns the value of the '<em><b>Subq</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Subq</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Subq</em>' containment reference.
   * @see #setSubq(SubQueryOperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Subq()
   * @model containment="true"
   * @generated
   */
  SubQueryOperand getSubq();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getSubq <em>Subq</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Subq</em>' containment reference.
   * @see #getSubq()
   * @generated
   */
  void setSubq(SubQueryOperand value);

  /**
   * Returns the value of the '<em><b>Fcast</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Fcast</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Fcast</em>' containment reference.
   * @see #setFcast(OpFunctionCast)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Fcast()
   * @model containment="true"
   * @generated
   */
  OpFunctionCast getFcast();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getFcast <em>Fcast</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Fcast</em>' containment reference.
   * @see #getFcast()
   * @generated
   */
  void setFcast(OpFunctionCast value);

  /**
   * Returns the value of the '<em><b>Func</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Func</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Func</em>' containment reference.
   * @see #setFunc(OpFunction)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Func()
   * @model containment="true"
   * @generated
   */
  OpFunction getFunc();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getFunc <em>Func</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Func</em>' containment reference.
   * @see #getFunc()
   * @generated
   */
  void setFunc(OpFunction value);

  /**
   * Returns the value of the '<em><b>Sqlcase</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sqlcase</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sqlcase</em>' containment reference.
   * @see #setSqlcase(SQLCaseOperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Sqlcase()
   * @model containment="true"
   * @generated
   */
  SQLCaseOperand getSqlcase();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getSqlcase <em>Sqlcase</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sqlcase</em>' containment reference.
   * @see #getSqlcase()
   * @generated
   */
  void setSqlcase(SQLCaseOperand value);

  /**
   * Returns the value of the '<em><b>Param</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Param</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Param</em>' containment reference.
   * @see #setParam(POperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Param()
   * @model containment="true"
   * @generated
   */
  POperand getParam();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getParam <em>Param</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Param</em>' containment reference.
   * @see #getParam()
   * @generated
   */
  void setParam(POperand value);

  /**
   * Returns the value of the '<em><b>Eparam</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Eparam</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Eparam</em>' containment reference.
   * @see #setEparam(ExpOperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Eparam()
   * @model containment="true"
   * @generated
   */
  ExpOperand getEparam();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getEparam <em>Eparam</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Eparam</em>' containment reference.
   * @see #getEparam()
   * @generated
   */
  void setEparam(ExpOperand value);

  /**
   * Returns the value of the '<em><b>Scalar</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Scalar</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Scalar</em>' containment reference.
   * @see #setScalar(ScalarOperand)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getOperand_Scalar()
   * @model containment="true"
   * @generated
   */
  ScalarOperand getScalar();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.Operand#getScalar <em>Scalar</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Scalar</em>' containment reference.
   * @see #getScalar()
   * @generated
   */
  void setScalar(ScalarOperand value);

} // Operand
