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
 * A representation of the model object '<em><b>XExpr</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.XExpr#getXf <em>Xf</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.XExpr#getCol <em>Col</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.XExpr#getPrm <em>Prm</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getXExpr()
 * @model
 * @generated
 */
public interface XExpr extends EObject
{
  /**
   * Returns the value of the '<em><b>Xf</b></em>' attribute.
   * The literals are from the enumeration {@link com.jaspersoft.studio.data.sql.XFunction}.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Xf</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Xf</em>' attribute.
   * @see com.jaspersoft.studio.data.sql.XFunction
   * @see #setXf(XFunction)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getXExpr_Xf()
   * @model
   * @generated
   */
  XFunction getXf();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.XExpr#getXf <em>Xf</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Xf</em>' attribute.
   * @see com.jaspersoft.studio.data.sql.XFunction
   * @see #getXf()
   * @generated
   */
  void setXf(XFunction value);

  /**
   * Returns the value of the '<em><b>Col</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Col</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Col</em>' containment reference.
   * @see #setCol(Operands)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getXExpr_Col()
   * @model containment="true"
   * @generated
   */
  Operands getCol();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.XExpr#getCol <em>Col</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Col</em>' containment reference.
   * @see #getCol()
   * @generated
   */
  void setCol(Operands value);

  /**
   * Returns the value of the '<em><b>Prm</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Prm</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Prm</em>' containment reference.
   * @see #setPrm(Prms)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getXExpr_Prm()
   * @model containment="true"
   * @generated
   */
  Prms getPrm();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.XExpr#getPrm <em>Prm</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Prm</em>' containment reference.
   * @see #getPrm()
   * @generated
   */
  void setPrm(Prms value);

} // XExpr
