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
 * A representation of the model object '<em><b>Sub Query Operand</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.SubQueryOperand#getSel <em>Sel</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getSubQueryOperand()
 * @model
 * @generated
 */
public interface SubQueryOperand extends EObject
{
  /**
   * Returns the value of the '<em><b>Sel</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Sel</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Sel</em>' containment reference.
   * @see #setSel(SelectQuery)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getSubQueryOperand_Sel()
   * @model containment="true"
   * @generated
   */
  SelectQuery getSel();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.SubQueryOperand#getSel <em>Sel</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Sel</em>' containment reference.
   * @see #getSel()
   * @generated
   */
  void setSel(SelectQuery value);

} // SubQueryOperand
