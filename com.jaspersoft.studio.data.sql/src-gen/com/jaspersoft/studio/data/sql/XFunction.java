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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>XFunction</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see com.jaspersoft.studio.data.sql.SqlPackage#getXFunction()
 * @model
 * @generated
 */
public enum XFunction implements Enumerator
{
  /**
   * The '<em><b>Xin</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XIN_VALUE
   * @generated
   * @ordered
   */
  XIN(0, "xin", "IN"),

  /**
   * The '<em><b>Xnotin</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XNOTIN_VALUE
   * @generated
   * @ordered
   */
  XNOTIN(1, "xnotin", "NOTIN"),

  /**
   * The '<em><b>Xeq</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XEQ_VALUE
   * @generated
   * @ordered
   */
  XEQ(2, "xeq", "EQUAL"),

  /**
   * The '<em><b>Xnoteq</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XNOTEQ_VALUE
   * @generated
   * @ordered
   */
  XNOTEQ(3, "xnoteq", "NOTEQUAL"),

  /**
   * The '<em><b>Xls</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XLS_VALUE
   * @generated
   * @ordered
   */
  XLS(4, "xls", "LESS"),

  /**
   * The '<em><b>Xgt</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XGT_VALUE
   * @generated
   * @ordered
   */
  XGT(5, "xgt", "GREATER"),

  /**
   * The '<em><b>Xlsr</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XLSR_VALUE
   * @generated
   * @ordered
   */
  XLSR(6, "xlsr", "LESS]"),

  /**
   * The '<em><b>Xgtl</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XGTL_VALUE
   * @generated
   * @ordered
   */
  XGTL(7, "xgtl", "[GREATER"),

  /**
   * The '<em><b>Xbwn</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XBWN_VALUE
   * @generated
   * @ordered
   */
  XBWN(8, "xbwn", "BETWEEN"),

  /**
   * The '<em><b>Xbwnc</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XBWNC_VALUE
   * @generated
   * @ordered
   */
  XBWNC(9, "xbwnc", "[BETWEEN]"),

  /**
   * The '<em><b>Xbwnl</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XBWNL_VALUE
   * @generated
   * @ordered
   */
  XBWNL(10, "xbwnl", "[BETWEEN"),

  /**
   * The '<em><b>Xbwnr</b></em>' literal object.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #XBWNR_VALUE
   * @generated
   * @ordered
   */
  XBWNR(11, "xbwnr", "BETWEEN]");

  /**
   * The '<em><b>Xin</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xin</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XIN
   * @model name="xin" literal="IN"
   * @generated
   * @ordered
   */
  public static final int XIN_VALUE = 0;

  /**
   * The '<em><b>Xnotin</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xnotin</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XNOTIN
   * @model name="xnotin" literal="NOTIN"
   * @generated
   * @ordered
   */
  public static final int XNOTIN_VALUE = 1;

  /**
   * The '<em><b>Xeq</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xeq</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XEQ
   * @model name="xeq" literal="EQUAL"
   * @generated
   * @ordered
   */
  public static final int XEQ_VALUE = 2;

  /**
   * The '<em><b>Xnoteq</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xnoteq</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XNOTEQ
   * @model name="xnoteq" literal="NOTEQUAL"
   * @generated
   * @ordered
   */
  public static final int XNOTEQ_VALUE = 3;

  /**
   * The '<em><b>Xls</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xls</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XLS
   * @model name="xls" literal="LESS"
   * @generated
   * @ordered
   */
  public static final int XLS_VALUE = 4;

  /**
   * The '<em><b>Xgt</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xgt</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XGT
   * @model name="xgt" literal="GREATER"
   * @generated
   * @ordered
   */
  public static final int XGT_VALUE = 5;

  /**
   * The '<em><b>Xlsr</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xlsr</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XLSR
   * @model name="xlsr" literal="LESS]"
   * @generated
   * @ordered
   */
  public static final int XLSR_VALUE = 6;

  /**
   * The '<em><b>Xgtl</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xgtl</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XGTL
   * @model name="xgtl" literal="[GREATER"
   * @generated
   * @ordered
   */
  public static final int XGTL_VALUE = 7;

  /**
   * The '<em><b>Xbwn</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xbwn</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XBWN
   * @model name="xbwn" literal="BETWEEN"
   * @generated
   * @ordered
   */
  public static final int XBWN_VALUE = 8;

  /**
   * The '<em><b>Xbwnc</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xbwnc</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XBWNC
   * @model name="xbwnc" literal="[BETWEEN]"
   * @generated
   * @ordered
   */
  public static final int XBWNC_VALUE = 9;

  /**
   * The '<em><b>Xbwnl</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xbwnl</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XBWNL
   * @model name="xbwnl" literal="[BETWEEN"
   * @generated
   * @ordered
   */
  public static final int XBWNL_VALUE = 10;

  /**
   * The '<em><b>Xbwnr</b></em>' literal value.
   * <!-- begin-user-doc -->
   * <p>
   * If the meaning of '<em><b>Xbwnr</b></em>' literal object isn't clear,
   * there really should be more of a description here...
   * </p>
   * <!-- end-user-doc -->
   * @see #XBWNR
   * @model name="xbwnr" literal="BETWEEN]"
   * @generated
   * @ordered
   */
  public static final int XBWNR_VALUE = 11;

  /**
   * An array of all the '<em><b>XFunction</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private static final XFunction[] VALUES_ARRAY =
    new XFunction[]
    {
      XIN,
      XNOTIN,
      XEQ,
      XNOTEQ,
      XLS,
      XGT,
      XLSR,
      XGTL,
      XBWN,
      XBWNC,
      XBWNL,
      XBWNR,
    };

  /**
   * A public read-only list of all the '<em><b>XFunction</b></em>' enumerators.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static final List<XFunction> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

  /**
   * Returns the '<em><b>XFunction</b></em>' literal with the specified literal value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static XFunction get(String literal)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      XFunction result = VALUES_ARRAY[i];
      if (result.toString().equals(literal))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>XFunction</b></em>' literal with the specified name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static XFunction getByName(String name)
  {
    for (int i = 0; i < VALUES_ARRAY.length; ++i)
    {
      XFunction result = VALUES_ARRAY[i];
      if (result.getName().equals(name))
      {
        return result;
      }
    }
    return null;
  }

  /**
   * Returns the '<em><b>XFunction</b></em>' literal with the specified integer value.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static XFunction get(int value)
  {
    switch (value)
    {
      case XIN_VALUE: return XIN;
      case XNOTIN_VALUE: return XNOTIN;
      case XEQ_VALUE: return XEQ;
      case XNOTEQ_VALUE: return XNOTEQ;
      case XLS_VALUE: return XLS;
      case XGT_VALUE: return XGT;
      case XLSR_VALUE: return XLSR;
      case XGTL_VALUE: return XGTL;
      case XBWN_VALUE: return XBWN;
      case XBWNC_VALUE: return XBWNC;
      case XBWNL_VALUE: return XBWNL;
      case XBWNR_VALUE: return XBWNR;
    }
    return null;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final int value;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String name;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private final String literal;

  /**
   * Only this class can construct instances.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  private XFunction(int value, String name, String literal)
  {
    this.value = value;
    this.name = name;
    this.literal = literal;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getValue()
  {
    return value;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getLiteral()
  {
    return literal;
  }

  /**
   * Returns the literal value of the enumerator, which is its string representation.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    return literal;
  }
  
} //XFunction
