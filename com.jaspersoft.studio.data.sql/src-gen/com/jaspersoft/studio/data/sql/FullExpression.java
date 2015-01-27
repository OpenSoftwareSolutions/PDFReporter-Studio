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
 * A representation of the model object '<em><b>Full Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getC <em>C</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getEfrag <em>Efrag</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getNotPrm <em>Not Prm</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getExpgroup <em>Expgroup</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getExp <em>Exp</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getXexp <em>Xexp</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getOp1 <em>Op1</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getIsnull <em>Isnull</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getIn <em>In</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getBetween <em>Between</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getLike <em>Like</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.FullExpression#getComp <em>Comp</em>}</li>
 * </ul>
 * </p>
 *
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression()
 * @model
 * @generated
 */
public interface FullExpression extends OrExpr
{
  /**
   * Returns the value of the '<em><b>C</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>C</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>C</em>' attribute.
   * @see #setC(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_C()
   * @model
   * @generated
   */
  String getC();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getC <em>C</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>C</em>' attribute.
   * @see #getC()
   * @generated
   */
  void setC(String value);

  /**
   * Returns the value of the '<em><b>Efrag</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Efrag</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Efrag</em>' containment reference.
   * @see #setEfrag(FullExpression)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Efrag()
   * @model containment="true"
   * @generated
   */
  FullExpression getEfrag();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getEfrag <em>Efrag</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Efrag</em>' containment reference.
   * @see #getEfrag()
   * @generated
   */
  void setEfrag(FullExpression value);

  /**
   * Returns the value of the '<em><b>Not Prm</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Not Prm</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Not Prm</em>' attribute.
   * @see #setNotPrm(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_NotPrm()
   * @model
   * @generated
   */
  String getNotPrm();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getNotPrm <em>Not Prm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Not Prm</em>' attribute.
   * @see #getNotPrm()
   * @generated
   */
  void setNotPrm(String value);

  /**
   * Returns the value of the '<em><b>Expgroup</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Expgroup</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Expgroup</em>' containment reference.
   * @see #setExpgroup(ExprGroup)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Expgroup()
   * @model containment="true"
   * @generated
   */
  ExprGroup getExpgroup();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getExpgroup <em>Expgroup</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Expgroup</em>' containment reference.
   * @see #getExpgroup()
   * @generated
   */
  void setExpgroup(ExprGroup value);

  /**
   * Returns the value of the '<em><b>Exp</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Exp</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Exp</em>' containment reference.
   * @see #setExp(FullExpression)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Exp()
   * @model containment="true"
   * @generated
   */
  FullExpression getExp();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getExp <em>Exp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Exp</em>' containment reference.
   * @see #getExp()
   * @generated
   */
  void setExp(FullExpression value);

  /**
   * Returns the value of the '<em><b>Xexp</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Xexp</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Xexp</em>' containment reference.
   * @see #setXexp(XExpr)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Xexp()
   * @model containment="true"
   * @generated
   */
  XExpr getXexp();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getXexp <em>Xexp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Xexp</em>' containment reference.
   * @see #getXexp()
   * @generated
   */
  void setXexp(XExpr value);

  /**
   * Returns the value of the '<em><b>Op1</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Op1</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Op1</em>' containment reference.
   * @see #setOp1(Operands)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Op1()
   * @model containment="true"
   * @generated
   */
  Operands getOp1();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getOp1 <em>Op1</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Op1</em>' containment reference.
   * @see #getOp1()
   * @generated
   */
  void setOp1(Operands value);

  /**
   * Returns the value of the '<em><b>Isnull</b></em>' attribute.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Isnull</em>' attribute isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Isnull</em>' attribute.
   * @see #setIsnull(String)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Isnull()
   * @model
   * @generated
   */
  String getIsnull();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getIsnull <em>Isnull</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Isnull</em>' attribute.
   * @see #getIsnull()
   * @generated
   */
  void setIsnull(String value);

  /**
   * Returns the value of the '<em><b>In</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>In</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>In</em>' containment reference.
   * @see #setIn(InOper)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_In()
   * @model containment="true"
   * @generated
   */
  InOper getIn();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getIn <em>In</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>In</em>' containment reference.
   * @see #getIn()
   * @generated
   */
  void setIn(InOper value);

  /**
   * Returns the value of the '<em><b>Between</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Between</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Between</em>' containment reference.
   * @see #setBetween(Between)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Between()
   * @model containment="true"
   * @generated
   */
  Between getBetween();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getBetween <em>Between</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Between</em>' containment reference.
   * @see #getBetween()
   * @generated
   */
  void setBetween(Between value);

  /**
   * Returns the value of the '<em><b>Like</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Like</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Like</em>' containment reference.
   * @see #setLike(Like)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Like()
   * @model containment="true"
   * @generated
   */
  Like getLike();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getLike <em>Like</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Like</em>' containment reference.
   * @see #getLike()
   * @generated
   */
  void setLike(Like value);

  /**
   * Returns the value of the '<em><b>Comp</b></em>' containment reference.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of the '<em>Comp</em>' containment reference isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @return the value of the '<em>Comp</em>' containment reference.
   * @see #setComp(Comparison)
   * @see com.jaspersoft.studio.data.sql.SqlPackage#getFullExpression_Comp()
   * @model containment="true"
   * @generated
   */
  Comparison getComp();

  /**
   * Sets the value of the '{@link com.jaspersoft.studio.data.sql.FullExpression#getComp <em>Comp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @param value the new value of the '<em>Comp</em>' containment reference.
   * @see #getComp()
   * @generated
   */
  void setComp(Comparison value);

} // FullExpression
