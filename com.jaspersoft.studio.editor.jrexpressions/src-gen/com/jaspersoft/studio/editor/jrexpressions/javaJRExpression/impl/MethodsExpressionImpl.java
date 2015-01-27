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
package com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JasperReportsExpression;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodsExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Methods Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl#isIncludeObjectInstatiation <em>Include Object Instatiation</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl#getMethodInvocations <em>Method Invocations</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl#getObjectExpression <em>Object Expression</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodsExpressionImpl#getArrayIndexes <em>Array Indexes</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MethodsExpressionImpl extends JasperReportsExpressionImpl implements MethodsExpression
{
  /**
   * The default value of the '{@link #isIncludeObjectInstatiation() <em>Include Object Instatiation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeObjectInstatiation()
   * @generated
   * @ordered
   */
  protected static final boolean INCLUDE_OBJECT_INSTATIATION_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIncludeObjectInstatiation() <em>Include Object Instatiation</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIncludeObjectInstatiation()
   * @generated
   * @ordered
   */
  protected boolean includeObjectInstatiation = INCLUDE_OBJECT_INSTATIATION_EDEFAULT;

  /**
   * The cached value of the '{@link #getMethodInvocations() <em>Method Invocations</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getMethodInvocations()
   * @generated
   * @ordered
   */
  protected EList<MethodInvocation> methodInvocations;

  /**
   * The cached value of the '{@link #getObjectExpression() <em>Object Expression</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getObjectExpression()
   * @generated
   * @ordered
   */
  protected JasperReportsExpression objectExpression;

  /**
   * The cached value of the '{@link #getArrayIndexes() <em>Array Indexes</em>}' containment reference list.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArrayIndexes()
   * @generated
   * @ordered
   */
  protected EList<JasperReportsExpression> arrayIndexes;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MethodsExpressionImpl()
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
    return JavaJRExpressionPackage.Literals.METHODS_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIncludeObjectInstatiation()
  {
    return includeObjectInstatiation;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIncludeObjectInstatiation(boolean newIncludeObjectInstatiation)
  {
    boolean oldIncludeObjectInstatiation = includeObjectInstatiation;
    includeObjectInstatiation = newIncludeObjectInstatiation;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION, oldIncludeObjectInstatiation, includeObjectInstatiation));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<MethodInvocation> getMethodInvocations()
  {
    if (methodInvocations == null)
    {
      methodInvocations = new EObjectContainmentEList<MethodInvocation>(MethodInvocation.class, this, JavaJRExpressionPackage.METHODS_EXPRESSION__METHOD_INVOCATIONS);
    }
    return methodInvocations;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public JasperReportsExpression getObjectExpression()
  {
    return objectExpression;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetObjectExpression(JasperReportsExpression newObjectExpression, NotificationChain msgs)
  {
    JasperReportsExpression oldObjectExpression = objectExpression;
    objectExpression = newObjectExpression;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION, oldObjectExpression, newObjectExpression);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setObjectExpression(JasperReportsExpression newObjectExpression)
  {
    if (newObjectExpression != objectExpression)
    {
      NotificationChain msgs = null;
      if (objectExpression != null)
        msgs = ((InternalEObject)objectExpression).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION, null, msgs);
      if (newObjectExpression != null)
        msgs = ((InternalEObject)newObjectExpression).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION, null, msgs);
      msgs = basicSetObjectExpression(newObjectExpression, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION, newObjectExpression, newObjectExpression));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EList<JasperReportsExpression> getArrayIndexes()
  {
    if (arrayIndexes == null)
    {
      arrayIndexes = new EObjectContainmentEList<JasperReportsExpression>(JasperReportsExpression.class, this, JavaJRExpressionPackage.METHODS_EXPRESSION__ARRAY_INDEXES);
    }
    return arrayIndexes;
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
      case JavaJRExpressionPackage.METHODS_EXPRESSION__METHOD_INVOCATIONS:
        return ((InternalEList<?>)getMethodInvocations()).basicRemove(otherEnd, msgs);
      case JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION:
        return basicSetObjectExpression(null, msgs);
      case JavaJRExpressionPackage.METHODS_EXPRESSION__ARRAY_INDEXES:
        return ((InternalEList<?>)getArrayIndexes()).basicRemove(otherEnd, msgs);
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
      case JavaJRExpressionPackage.METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION:
        return isIncludeObjectInstatiation();
      case JavaJRExpressionPackage.METHODS_EXPRESSION__METHOD_INVOCATIONS:
        return getMethodInvocations();
      case JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION:
        return getObjectExpression();
      case JavaJRExpressionPackage.METHODS_EXPRESSION__ARRAY_INDEXES:
        return getArrayIndexes();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @SuppressWarnings("unchecked")
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case JavaJRExpressionPackage.METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION:
        setIncludeObjectInstatiation((Boolean)newValue);
        return;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__METHOD_INVOCATIONS:
        getMethodInvocations().clear();
        getMethodInvocations().addAll((Collection<? extends MethodInvocation>)newValue);
        return;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION:
        setObjectExpression((JasperReportsExpression)newValue);
        return;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__ARRAY_INDEXES:
        getArrayIndexes().clear();
        getArrayIndexes().addAll((Collection<? extends JasperReportsExpression>)newValue);
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
      case JavaJRExpressionPackage.METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION:
        setIncludeObjectInstatiation(INCLUDE_OBJECT_INSTATIATION_EDEFAULT);
        return;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__METHOD_INVOCATIONS:
        getMethodInvocations().clear();
        return;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION:
        setObjectExpression((JasperReportsExpression)null);
        return;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__ARRAY_INDEXES:
        getArrayIndexes().clear();
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
      case JavaJRExpressionPackage.METHODS_EXPRESSION__INCLUDE_OBJECT_INSTATIATION:
        return includeObjectInstatiation != INCLUDE_OBJECT_INSTATIATION_EDEFAULT;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__METHOD_INVOCATIONS:
        return methodInvocations != null && !methodInvocations.isEmpty();
      case JavaJRExpressionPackage.METHODS_EXPRESSION__OBJECT_EXPRESSION:
        return objectExpression != null;
      case JavaJRExpressionPackage.METHODS_EXPRESSION__ARRAY_INDEXES:
        return arrayIndexes != null && !arrayIndexes.isEmpty();
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
    result.append(" (includeObjectInstatiation: ");
    result.append(includeObjectInstatiation);
    result.append(')');
    return result.toString();
  }

} //MethodsExpressionImpl
