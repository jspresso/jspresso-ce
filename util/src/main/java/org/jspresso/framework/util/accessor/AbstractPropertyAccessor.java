/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.accessor;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.jspresso.framework.util.bean.PropertyHelper;

/**
 * Abstract class for property accessors.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractPropertyAccessor implements IAccessor {

  private String property;

  /**
   * Constructs a new <code>AbstractPropertyAccessor</code> instance.
   * 
   * @param property
   *          the property to access.
   */
  public AbstractPropertyAccessor(String property) {
    this.property = property;
  }

  /**
   * Gets a property value, taking care of Map vs bean implementation and nested
   * properties.
   * 
   * @param target
   *          the target object.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   * @return the property value.
   */
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    Object finalTarget = getLastNestedTarget(target, getProperty());
    if (finalTarget != null) {
      if (finalTarget instanceof Map) {
        if (PropertyHelper.getPropertyNames(finalTarget.getClass()).contains(
            getLastNestedProperty())) {
          // We are explicitely on a bean property. Do not use
          // PropertyUtils.getProperty since it will detect that the target
          // is a Map and access its properties as such.
          return PropertyUtils.getSimpleProperty(finalTarget,
              getLastNestedProperty());
        }
        return PropertyUtils.getProperty(finalTarget, getLastNestedProperty());
      }
      return PropertyUtils.getProperty(finalTarget, getLastNestedProperty());
    }
    return null;
  }

  /**
   * Sets a property value, taking care of Map vs bean implementation and nested
   * properties.
   * 
   * @param target
   *          the target object.
   * @param value
   *          the value to set.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   */
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    Object finalTarget = getLastNestedTarget(target, getProperty());
    if (finalTarget != null) {
      if (finalTarget instanceof Map) {
        if (PropertyHelper.getPropertyNames(finalTarget.getClass()).contains(
            getLastNestedProperty())) {
          // We are explicitely on a bean property. Do not use
          // PropertyUtils.getProperty since it will detect that the target
          // is a Map and access its properties as such.
          PropertyUtils.setSimpleProperty(finalTarget, getLastNestedProperty(),
              value);
        } else {
          PropertyUtils
              .setProperty(finalTarget, getLastNestedProperty(), value);
        }
      } else {
        PropertyUtils.setProperty(finalTarget, getLastNestedProperty(), value);
      }
    }
  }

  /**
   * Gets the property property.
   * 
   * @return the property.
   */
  protected String getProperty() {
    return property;
  }

  /**
   * Gets the last target of a nested property.
   * 
   * @param target
   *          the starting target.
   * @param prop
   *          the property.
   * @return the last target of a nested property.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   */
  protected Object getLastNestedTarget(Object target, String prop)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      int indexOfNestedDelim = prop.indexOf(IAccessor.NESTED_DELIM);
      if (indexOfNestedDelim < 0) {
        return target;
      }
      if (target instanceof Map) {
        if (PropertyHelper.getPropertyNames(target.getClass()).contains(prop)) {
          // We are explicitely on a bean property. Do not use
          // PropertyUtils.getProperty since it will detect that the target is
          // a
          // Map and access its properties as such.
          return getLastNestedTarget(PropertyUtils.getSimpleProperty(target,
              prop.substring(0, indexOfNestedDelim)), prop
              .substring(indexOfNestedDelim + 1));
        }
        return getLastNestedTarget(PropertyUtils.getProperty(target, prop
            .substring(0, indexOfNestedDelim)), prop
            .substring(indexOfNestedDelim + 1));
      }
      return getLastNestedTarget(PropertyUtils.getProperty(target, prop
          .substring(0, indexOfNestedDelim)), prop
          .substring(indexOfNestedDelim + 1));
    }
    return null;
  }

  /**
   * Gets the final nested property.
   * 
   * @return the final nested property.
   */
  protected String getLastNestedProperty() {
    return getProperty().substring(
        getProperty().lastIndexOf(IAccessor.NESTED_DELIM) + 1);
  }
}
