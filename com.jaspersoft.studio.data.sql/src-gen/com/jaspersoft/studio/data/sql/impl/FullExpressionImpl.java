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
package com.jaspersoft.studio.data.sql.impl;

import com.jaspersoft.studio.data.sql.Between;
import com.jaspersoft.studio.data.sql.Comparison;
import com.jaspersoft.studio.data.sql.ExprGroup;
import com.jaspersoft.studio.data.sql.FullExpression;
import com.jaspersoft.studio.data.sql.InOper;
import com.jaspersoft.studio.data.sql.Like;
import com.jaspersoft.studio.data.sql.Operands;
import com.jaspersoft.studio.data.sql.SqlPackage;
import com.jaspersoft.studio.data.sql.XExpr;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Full Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getC <em>C</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getEfrag <em>Efrag</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getNotPrm <em>Not Prm</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getExpgroup <em>Expgroup</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getExp <em>Exp</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getXexp <em>Xexp</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getOp1 <em>Op1</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getIsnull <em>Isnull</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getIn <em>In</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getBetween <em>Between</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getLike <em>Like</em>}</li>
 *   <li>{@link com.jaspersoft.studio.data.sql.impl.FullExpressionImpl#getComp <em>Comp</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class FullExpressionImpl extends OrExprImpl implements FullExpression
{
  /**
   * The default value of the '{@link #getC() <em>C</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getC()
   * @generated
   * @ordered
   */
  protected static final String C_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getC() <em>C</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getC()
   * @generated
   * @ordered
   */
  protected String c = C_EDEFAULT;

  /**
   * The cached value of the '{@link #getEfrag() <em>Efrag</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getEfrag()
   * @generated
   * @ordered
   */
  protected FullExpression efrag;

  /**
   * The default value of the '{@link #getNotPrm() <em>Not Prm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNotPrm()
   * @generated
   * @ordered
   */
  protected static final String NOT_PRM_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getNotPrm() <em>Not Prm</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNotPrm()
   * @generated
   * @ordered
   */
  protected String notPrm = NOT_PRM_EDEFAULT;

  /**
   * The cached value of the '{@link #getExpgroup() <em>Expgroup</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExpgroup()
   * @generated
   * @ordered
   */
  protected ExprGroup expgroup;

  /**
   * The cached value of the '{@link #getExp() <em>Exp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getExp()
   * @generated
   * @ordered
   */
  protected FullExpression exp;

  /**
   * The cached value of the '{@link #getXexp() <em>Xexp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getXexp()
   * @generated
   * @ordered
   */
  protected XExpr xexp;

  /**
   * The cached value of the '{@link #getOp1() <em>Op1</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getOp1()
   * @generated
   * @ordered
   */
  protected Operands op1;

  /**
   * The default value of the '{@link #getIsnull() <em>Isnull</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIsnull()
   * @generated
   * @ordered
   */
  protected static final String ISNULL_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getIsnull() <em>Isnull</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIsnull()
   * @generated
   * @ordered
   */
  protected String isnull = ISNULL_EDEFAULT;

  /**
   * The cached value of the '{@link #getIn() <em>In</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIn()
   * @generated
   * @ordered
   */
  protected InOper in;

  /**
   * The cached value of the '{@link #getBetween() <em>Between</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getBetween()
   * @generated
   * @ordered
   */
  protected Between between;

  /**
   * The cached value of the '{@link #getLike() <em>Like</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getLike()
   * @generated
   * @ordered
   */
  protected Like like;

  /**
   * The cached value of the '{@link #getComp() <em>Comp</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getComp()
   * @generated
   * @ordered
   */
  protected Comparison comp;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected FullExpressionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return SqlPackage.Literals.FULL_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getC()
  {
    return c;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setC(String newC)
  {
    String oldC = c;
    c = newC;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__C, oldC, c));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FullExpression getEfrag()
  {
    return efrag;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetEfrag(FullExpression newEfrag, NotificationChain msgs)
  {
    FullExpression oldEfrag = efrag;
    efrag = newEfrag;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__EFRAG, oldEfrag, newEfrag);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setEfrag(FullExpression newEfrag)
  {
    if (newEfrag != efrag)
    {
      NotificationChain msgs = null;
      if (efrag != null)
        msgs = ((InternalEObject)efrag).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__EFRAG, null, msgs);
      if (newEfrag != null)
        msgs = ((InternalEObject)newEfrag).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__EFRAG, null, msgs);
      msgs = basicSetEfrag(newEfrag, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__EFRAG, newEfrag, newEfrag));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getNotPrm()
  {
    return notPrm;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNotPrm(String newNotPrm)
  {
    String oldNotPrm = notPrm;
    notPrm = newNotPrm;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__NOT_PRM, oldNotPrm, notPrm));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public ExprGroup getExpgroup()
  {
    return expgroup;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExpgroup(ExprGroup newExpgroup, NotificationChain msgs)
  {
    ExprGroup oldExpgroup = expgroup;
    expgroup = newExpgroup;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__EXPGROUP, oldExpgroup, newExpgroup);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExpgroup(ExprGroup newExpgroup)
  {
    if (newExpgroup != expgroup)
    {
      NotificationChain msgs = null;
      if (expgroup != null)
        msgs = ((InternalEObject)expgroup).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__EXPGROUP, null, msgs);
      if (newExpgroup != null)
        msgs = ((InternalEObject)newExpgroup).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__EXPGROUP, null, msgs);
      msgs = basicSetExpgroup(newExpgroup, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__EXPGROUP, newExpgroup, newExpgroup));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FullExpression getExp()
  {
    return exp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetExp(FullExpression newExp, NotificationChain msgs)
  {
    FullExpression oldExp = exp;
    exp = newExp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__EXP, oldExp, newExp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setExp(FullExpression newExp)
  {
    if (newExp != exp)
    {
      NotificationChain msgs = null;
      if (exp != null)
        msgs = ((InternalEObject)exp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__EXP, null, msgs);
      if (newExp != null)
        msgs = ((InternalEObject)newExp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__EXP, null, msgs);
      msgs = basicSetExp(newExp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__EXP, newExp, newExp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public XExpr getXexp()
  {
    return xexp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetXexp(XExpr newXexp, NotificationChain msgs)
  {
    XExpr oldXexp = xexp;
    xexp = newXexp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__XEXP, oldXexp, newXexp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setXexp(XExpr newXexp)
  {
    if (newXexp != xexp)
    {
      NotificationChain msgs = null;
      if (xexp != null)
        msgs = ((InternalEObject)xexp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__XEXP, null, msgs);
      if (newXexp != null)
        msgs = ((InternalEObject)newXexp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__XEXP, null, msgs);
      msgs = basicSetXexp(newXexp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__XEXP, newXexp, newXexp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Operands getOp1()
  {
    return op1;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetOp1(Operands newOp1, NotificationChain msgs)
  {
    Operands oldOp1 = op1;
    op1 = newOp1;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__OP1, oldOp1, newOp1);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setOp1(Operands newOp1)
  {
    if (newOp1 != op1)
    {
      NotificationChain msgs = null;
      if (op1 != null)
        msgs = ((InternalEObject)op1).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__OP1, null, msgs);
      if (newOp1 != null)
        msgs = ((InternalEObject)newOp1).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__OP1, null, msgs);
      msgs = basicSetOp1(newOp1, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__OP1, newOp1, newOp1));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getIsnull()
  {
    return isnull;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsnull(String newIsnull)
  {
    String oldIsnull = isnull;
    isnull = newIsnull;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__ISNULL, oldIsnull, isnull));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public InOper getIn()
  {
    return in;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetIn(InOper newIn, NotificationChain msgs)
  {
    InOper oldIn = in;
    in = newIn;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__IN, oldIn, newIn);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIn(InOper newIn)
  {
    if (newIn != in)
    {
      NotificationChain msgs = null;
      if (in != null)
        msgs = ((InternalEObject)in).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__IN, null, msgs);
      if (newIn != null)
        msgs = ((InternalEObject)newIn).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__IN, null, msgs);
      msgs = basicSetIn(newIn, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__IN, newIn, newIn));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Between getBetween()
  {
    return between;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetBetween(Between newBetween, NotificationChain msgs)
  {
    Between oldBetween = between;
    between = newBetween;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__BETWEEN, oldBetween, newBetween);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setBetween(Between newBetween)
  {
    if (newBetween != between)
    {
      NotificationChain msgs = null;
      if (between != null)
        msgs = ((InternalEObject)between).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__BETWEEN, null, msgs);
      if (newBetween != null)
        msgs = ((InternalEObject)newBetween).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__BETWEEN, null, msgs);
      msgs = basicSetBetween(newBetween, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__BETWEEN, newBetween, newBetween));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Like getLike()
  {
    return like;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetLike(Like newLike, NotificationChain msgs)
  {
    Like oldLike = like;
    like = newLike;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__LIKE, oldLike, newLike);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setLike(Like newLike)
  {
    if (newLike != like)
    {
      NotificationChain msgs = null;
      if (like != null)
        msgs = ((InternalEObject)like).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__LIKE, null, msgs);
      if (newLike != null)
        msgs = ((InternalEObject)newLike).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__LIKE, null, msgs);
      msgs = basicSetLike(newLike, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__LIKE, newLike, newLike));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Comparison getComp()
  {
    return comp;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetComp(Comparison newComp, NotificationChain msgs)
  {
    Comparison oldComp = comp;
    comp = newComp;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__COMP, oldComp, newComp);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setComp(Comparison newComp)
  {
    if (newComp != comp)
    {
      NotificationChain msgs = null;
      if (comp != null)
        msgs = ((InternalEObject)comp).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__COMP, null, msgs);
      if (newComp != null)
        msgs = ((InternalEObject)newComp).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - SqlPackage.FULL_EXPRESSION__COMP, null, msgs);
      msgs = basicSetComp(newComp, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SqlPackage.FULL_EXPRESSION__COMP, newComp, newComp));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case SqlPackage.FULL_EXPRESSION__EFRAG:
        return basicSetEfrag(null, msgs);
      case SqlPackage.FULL_EXPRESSION__EXPGROUP:
        return basicSetExpgroup(null, msgs);
      case SqlPackage.FULL_EXPRESSION__EXP:
        return basicSetExp(null, msgs);
      case SqlPackage.FULL_EXPRESSION__XEXP:
        return basicSetXexp(null, msgs);
      case SqlPackage.FULL_EXPRESSION__OP1:
        return basicSetOp1(null, msgs);
      case SqlPackage.FULL_EXPRESSION__IN:
        return basicSetIn(null, msgs);
      case SqlPackage.FULL_EXPRESSION__BETWEEN:
        return basicSetBetween(null, msgs);
      case SqlPackage.FULL_EXPRESSION__LIKE:
        return basicSetLike(null, msgs);
      case SqlPackage.FULL_EXPRESSION__COMP:
        return basicSetComp(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case SqlPackage.FULL_EXPRESSION__C:
        return getC();
      case SqlPackage.FULL_EXPRESSION__EFRAG:
        return getEfrag();
      case SqlPackage.FULL_EXPRESSION__NOT_PRM:
        return getNotPrm();
      case SqlPackage.FULL_EXPRESSION__EXPGROUP:
        return getExpgroup();
      case SqlPackage.FULL_EXPRESSION__EXP:
        return getExp();
      case SqlPackage.FULL_EXPRESSION__XEXP:
        return getXexp();
      case SqlPackage.FULL_EXPRESSION__OP1:
        return getOp1();
      case SqlPackage.FULL_EXPRESSION__ISNULL:
        return getIsnull();
      case SqlPackage.FULL_EXPRESSION__IN:
        return getIn();
      case SqlPackage.FULL_EXPRESSION__BETWEEN:
        return getBetween();
      case SqlPackage.FULL_EXPRESSION__LIKE:
        return getLike();
      case SqlPackage.FULL_EXPRESSION__COMP:
        return getComp();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case SqlPackage.FULL_EXPRESSION__C:
        setC((String)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__EFRAG:
        setEfrag((FullExpression)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__NOT_PRM:
        setNotPrm((String)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__EXPGROUP:
        setExpgroup((ExprGroup)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__EXP:
        setExp((FullExpression)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__XEXP:
        setXexp((XExpr)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__OP1:
        setOp1((Operands)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__ISNULL:
        setIsnull((String)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__IN:
        setIn((InOper)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__BETWEEN:
        setBetween((Between)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__LIKE:
        setLike((Like)newValue);
        return;
      case SqlPackage.FULL_EXPRESSION__COMP:
        setComp((Comparison)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case SqlPackage.FULL_EXPRESSION__C:
        setC(C_EDEFAULT);
        return;
      case SqlPackage.FULL_EXPRESSION__EFRAG:
        setEfrag((FullExpression)null);
        return;
      case SqlPackage.FULL_EXPRESSION__NOT_PRM:
        setNotPrm(NOT_PRM_EDEFAULT);
        return;
      case SqlPackage.FULL_EXPRESSION__EXPGROUP:
        setExpgroup((ExprGroup)null);
        return;
      case SqlPackage.FULL_EXPRESSION__EXP:
        setExp((FullExpression)null);
        return;
      case SqlPackage.FULL_EXPRESSION__XEXP:
        setXexp((XExpr)null);
        return;
      case SqlPackage.FULL_EXPRESSION__OP1:
        setOp1((Operands)null);
        return;
      case SqlPackage.FULL_EXPRESSION__ISNULL:
        setIsnull(ISNULL_EDEFAULT);
        return;
      case SqlPackage.FULL_EXPRESSION__IN:
        setIn((InOper)null);
        return;
      case SqlPackage.FULL_EXPRESSION__BETWEEN:
        setBetween((Between)null);
        return;
      case SqlPackage.FULL_EXPRESSION__LIKE:
        setLike((Like)null);
        return;
      case SqlPackage.FULL_EXPRESSION__COMP:
        setComp((Comparison)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case SqlPackage.FULL_EXPRESSION__C:
        return C_EDEFAULT == null ? c != null : !C_EDEFAULT.equals(c);
      case SqlPackage.FULL_EXPRESSION__EFRAG:
        return efrag != null;
      case SqlPackage.FULL_EXPRESSION__NOT_PRM:
        return NOT_PRM_EDEFAULT == null ? notPrm != null : !NOT_PRM_EDEFAULT.equals(notPrm);
      case SqlPackage.FULL_EXPRESSION__EXPGROUP:
        return expgroup != null;
      case SqlPackage.FULL_EXPRESSION__EXP:
        return exp != null;
      case SqlPackage.FULL_EXPRESSION__XEXP:
        return xexp != null;
      case SqlPackage.FULL_EXPRESSION__OP1:
        return op1 != null;
      case SqlPackage.FULL_EXPRESSION__ISNULL:
        return ISNULL_EDEFAULT == null ? isnull != null : !ISNULL_EDEFAULT.equals(isnull);
      case SqlPackage.FULL_EXPRESSION__IN:
        return in != null;
      case SqlPackage.FULL_EXPRESSION__BETWEEN:
        return between != null;
      case SqlPackage.FULL_EXPRESSION__LIKE:
        return like != null;
      case SqlPackage.FULL_EXPRESSION__COMP:
        return comp != null;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (c: ");
    result.append(c);
    result.append(", notPrm: ");
    result.append(notPrm);
    result.append(", isnull: ");
    result.append(isnull);
    result.append(')');
    return result.toString();
  }

} //FullExpressionImpl
