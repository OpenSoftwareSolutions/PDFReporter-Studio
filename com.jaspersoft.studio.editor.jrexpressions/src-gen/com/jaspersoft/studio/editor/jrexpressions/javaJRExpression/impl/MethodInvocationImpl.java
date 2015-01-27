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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.Arguments;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.FullMethodName;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.JavaJRExpressionPackage;
import com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.MethodInvocation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Method Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodInvocationImpl#getFullyQualifiedMethodName <em>Fully Qualified Method Name</em>}</li>
 *   <li>{@link com.jaspersoft.studio.editor.jrexpressions.javaJRExpression.impl.MethodInvocationImpl#getArgs <em>Args</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MethodInvocationImpl extends MinimalEObjectImpl.Container implements MethodInvocation
{
  /**
   * The cached value of the '{@link #getFullyQualifiedMethodName() <em>Fully Qualified Method Name</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getFullyQualifiedMethodName()
   * @generated
   * @ordered
   */
  protected FullMethodName fullyQualifiedMethodName;

  /**
   * The cached value of the '{@link #getArgs() <em>Args</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getArgs()
   * @generated
   * @ordered
   */
  protected Arguments args;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MethodInvocationImpl()
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
    return JavaJRExpressionPackage.Literals.METHOD_INVOCATION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FullMethodName getFullyQualifiedMethodName()
  {
    return fullyQualifiedMethodName;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetFullyQualifiedMethodName(FullMethodName newFullyQualifiedMethodName, NotificationChain msgs)
  {
    FullMethodName oldFullyQualifiedMethodName = fullyQualifiedMethodName;
    fullyQualifiedMethodName = newFullyQualifiedMethodName;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME, oldFullyQualifiedMethodName, newFullyQualifiedMethodName);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setFullyQualifiedMethodName(FullMethodName newFullyQualifiedMethodName)
  {
    if (newFullyQualifiedMethodName != fullyQualifiedMethodName)
    {
      NotificationChain msgs = null;
      if (fullyQualifiedMethodName != null)
        msgs = ((InternalEObject)fullyQualifiedMethodName).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME, null, msgs);
      if (newFullyQualifiedMethodName != null)
        msgs = ((InternalEObject)newFullyQualifiedMethodName).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME, null, msgs);
      msgs = basicSetFullyQualifiedMethodName(newFullyQualifiedMethodName, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME, newFullyQualifiedMethodName, newFullyQualifiedMethodName));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public Arguments getArgs()
  {
    return args;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetArgs(Arguments newArgs, NotificationChain msgs)
  {
    Arguments oldArgs = args;
    args = newArgs;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHOD_INVOCATION__ARGS, oldArgs, newArgs);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setArgs(Arguments newArgs)
  {
    if (newArgs != args)
    {
      NotificationChain msgs = null;
      if (args != null)
        msgs = ((InternalEObject)args).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.METHOD_INVOCATION__ARGS, null, msgs);
      if (newArgs != null)
        msgs = ((InternalEObject)newArgs).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - JavaJRExpressionPackage.METHOD_INVOCATION__ARGS, null, msgs);
      msgs = basicSetArgs(newArgs, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, JavaJRExpressionPackage.METHOD_INVOCATION__ARGS, newArgs, newArgs));
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
      case JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME:
        return basicSetFullyQualifiedMethodName(null, msgs);
      case JavaJRExpressionPackage.METHOD_INVOCATION__ARGS:
        return basicSetArgs(null, msgs);
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
      case JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME:
        return getFullyQualifiedMethodName();
      case JavaJRExpressionPackage.METHOD_INVOCATION__ARGS:
        return getArgs();
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
      case JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME:
        setFullyQualifiedMethodName((FullMethodName)newValue);
        return;
      case JavaJRExpressionPackage.METHOD_INVOCATION__ARGS:
        setArgs((Arguments)newValue);
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
      case JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME:
        setFullyQualifiedMethodName((FullMethodName)null);
        return;
      case JavaJRExpressionPackage.METHOD_INVOCATION__ARGS:
        setArgs((Arguments)null);
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
      case JavaJRExpressionPackage.METHOD_INVOCATION__FULLY_QUALIFIED_METHOD_NAME:
        return fullyQualifiedMethodName != null;
      case JavaJRExpressionPackage.METHOD_INVOCATION__ARGS:
        return args != null;
    }
    return super.eIsSet(featureID);
  }

} //MethodInvocationImpl
