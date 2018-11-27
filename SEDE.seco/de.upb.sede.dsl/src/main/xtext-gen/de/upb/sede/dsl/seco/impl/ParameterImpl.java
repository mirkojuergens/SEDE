/**
 * generated by Xtext 2.15.0
 */
package de.upb.sede.dsl.seco.impl;

import de.upb.sede.dsl.seco.Parameter;
import de.upb.sede.dsl.seco.SecoPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#isIsIndexed <em>Is Indexed</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#getIndex <em>Index</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#isIsNumb <em>Is Numb</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#isIsString <em>Is String</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#isIsBool <em>Is Bool</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#isIsNull <em>Is Null</em>}</li>
 *   <li>{@link de.upb.sede.dsl.seco.impl.ParameterImpl#isIsField <em>Is Field</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParameterImpl extends MinimalEObjectImpl.Container implements Parameter
{
  /**
   * The default value of the '{@link #isIsIndexed() <em>Is Indexed</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsIndexed()
   * @generated
   * @ordered
   */
  protected static final boolean IS_INDEXED_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsIndexed() <em>Is Indexed</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsIndexed()
   * @generated
   * @ordered
   */
  protected boolean isIndexed = IS_INDEXED_EDEFAULT;

  /**
   * The default value of the '{@link #getIndex() <em>Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIndex()
   * @generated
   * @ordered
   */
  protected static final int INDEX_EDEFAULT = 0;

  /**
   * The cached value of the '{@link #getIndex() <em>Index</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getIndex()
   * @generated
   * @ordered
   */
  protected int index = INDEX_EDEFAULT;

  /**
   * The default value of the '{@link #isIsNumb() <em>Is Numb</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsNumb()
   * @generated
   * @ordered
   */
  protected static final boolean IS_NUMB_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsNumb() <em>Is Numb</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsNumb()
   * @generated
   * @ordered
   */
  protected boolean isNumb = IS_NUMB_EDEFAULT;

  /**
   * The default value of the '{@link #isIsString() <em>Is String</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsString()
   * @generated
   * @ordered
   */
  protected static final boolean IS_STRING_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsString() <em>Is String</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsString()
   * @generated
   * @ordered
   */
  protected boolean isString = IS_STRING_EDEFAULT;

  /**
   * The default value of the '{@link #isIsBool() <em>Is Bool</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsBool()
   * @generated
   * @ordered
   */
  protected static final boolean IS_BOOL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsBool() <em>Is Bool</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsBool()
   * @generated
   * @ordered
   */
  protected boolean isBool = IS_BOOL_EDEFAULT;

  /**
   * The default value of the '{@link #isIsNull() <em>Is Null</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsNull()
   * @generated
   * @ordered
   */
  protected static final boolean IS_NULL_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsNull() <em>Is Null</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsNull()
   * @generated
   * @ordered
   */
  protected boolean isNull = IS_NULL_EDEFAULT;

  /**
   * The default value of the '{@link #isIsField() <em>Is Field</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsField()
   * @generated
   * @ordered
   */
  protected static final boolean IS_FIELD_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isIsField() <em>Is Field</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isIsField()
   * @generated
   * @ordered
   */
  protected boolean isField = IS_FIELD_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected ParameterImpl()
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
    return SecoPackage.Literals.PARAMETER;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsIndexed()
  {
    return isIndexed;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsIndexed(boolean newIsIndexed)
  {
    boolean oldIsIndexed = isIndexed;
    isIndexed = newIsIndexed;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__IS_INDEXED, oldIsIndexed, isIndexed));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public int getIndex()
  {
    return index;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIndex(int newIndex)
  {
    int oldIndex = index;
    index = newIndex;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__INDEX, oldIndex, index));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsNumb()
  {
    return isNumb;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsNumb(boolean newIsNumb)
  {
    boolean oldIsNumb = isNumb;
    isNumb = newIsNumb;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__IS_NUMB, oldIsNumb, isNumb));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsString()
  {
    return isString;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsString(boolean newIsString)
  {
    boolean oldIsString = isString;
    isString = newIsString;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__IS_STRING, oldIsString, isString));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsBool()
  {
    return isBool;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsBool(boolean newIsBool)
  {
    boolean oldIsBool = isBool;
    isBool = newIsBool;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__IS_BOOL, oldIsBool, isBool));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsNull()
  {
    return isNull;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsNull(boolean newIsNull)
  {
    boolean oldIsNull = isNull;
    isNull = newIsNull;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__IS_NULL, oldIsNull, isNull));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isIsField()
  {
    return isField;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setIsField(boolean newIsField)
  {
    boolean oldIsField = isField;
    isField = newIsField;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, SecoPackage.PARAMETER__IS_FIELD, oldIsField, isField));
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
      case SecoPackage.PARAMETER__IS_INDEXED:
        return isIsIndexed();
      case SecoPackage.PARAMETER__INDEX:
        return getIndex();
      case SecoPackage.PARAMETER__IS_NUMB:
        return isIsNumb();
      case SecoPackage.PARAMETER__IS_STRING:
        return isIsString();
      case SecoPackage.PARAMETER__IS_BOOL:
        return isIsBool();
      case SecoPackage.PARAMETER__IS_NULL:
        return isIsNull();
      case SecoPackage.PARAMETER__IS_FIELD:
        return isIsField();
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
      case SecoPackage.PARAMETER__IS_INDEXED:
        setIsIndexed((Boolean)newValue);
        return;
      case SecoPackage.PARAMETER__INDEX:
        setIndex((Integer)newValue);
        return;
      case SecoPackage.PARAMETER__IS_NUMB:
        setIsNumb((Boolean)newValue);
        return;
      case SecoPackage.PARAMETER__IS_STRING:
        setIsString((Boolean)newValue);
        return;
      case SecoPackage.PARAMETER__IS_BOOL:
        setIsBool((Boolean)newValue);
        return;
      case SecoPackage.PARAMETER__IS_NULL:
        setIsNull((Boolean)newValue);
        return;
      case SecoPackage.PARAMETER__IS_FIELD:
        setIsField((Boolean)newValue);
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
      case SecoPackage.PARAMETER__IS_INDEXED:
        setIsIndexed(IS_INDEXED_EDEFAULT);
        return;
      case SecoPackage.PARAMETER__INDEX:
        setIndex(INDEX_EDEFAULT);
        return;
      case SecoPackage.PARAMETER__IS_NUMB:
        setIsNumb(IS_NUMB_EDEFAULT);
        return;
      case SecoPackage.PARAMETER__IS_STRING:
        setIsString(IS_STRING_EDEFAULT);
        return;
      case SecoPackage.PARAMETER__IS_BOOL:
        setIsBool(IS_BOOL_EDEFAULT);
        return;
      case SecoPackage.PARAMETER__IS_NULL:
        setIsNull(IS_NULL_EDEFAULT);
        return;
      case SecoPackage.PARAMETER__IS_FIELD:
        setIsField(IS_FIELD_EDEFAULT);
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
      case SecoPackage.PARAMETER__IS_INDEXED:
        return isIndexed != IS_INDEXED_EDEFAULT;
      case SecoPackage.PARAMETER__INDEX:
        return index != INDEX_EDEFAULT;
      case SecoPackage.PARAMETER__IS_NUMB:
        return isNumb != IS_NUMB_EDEFAULT;
      case SecoPackage.PARAMETER__IS_STRING:
        return isString != IS_STRING_EDEFAULT;
      case SecoPackage.PARAMETER__IS_BOOL:
        return isBool != IS_BOOL_EDEFAULT;
      case SecoPackage.PARAMETER__IS_NULL:
        return isNull != IS_NULL_EDEFAULT;
      case SecoPackage.PARAMETER__IS_FIELD:
        return isField != IS_FIELD_EDEFAULT;
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
    result.append(" (isIndexed: ");
    result.append(isIndexed);
    result.append(", index: ");
    result.append(index);
    result.append(", isNumb: ");
    result.append(isNumb);
    result.append(", isString: ");
    result.append(isString);
    result.append(", isBool: ");
    result.append(isBool);
    result.append(", isNull: ");
    result.append(isNull);
    result.append(", isField: ");
    result.append(isField);
    result.append(')');
    return result.toString();
  }

} //ParameterImpl
