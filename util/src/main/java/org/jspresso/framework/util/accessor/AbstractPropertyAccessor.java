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
   * @param prop
   *          the property to acccess.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   * @return the property value.
   */
  protected Object getValue(Object target, String prop)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      int indexOfNestedDelim = prop.indexOf(IAccessor.NESTED_DELIM);
      if (indexOfNestedDelim < 0) {
        if (target instanceof Map) {
          if (PropertyHelper.getPropertyNames(target.getClass()).contains(prop)) {
            // We are explicitely on a bean property. Do not use
            // PropertyUtils.getProperty since it will detect that the target is
            // a
            // Map and access its properties as such.
            return PropertyUtils.getSimpleProperty(target, prop);
          }
          return PropertyUtils.getProperty(target, prop);
        }
        return PropertyUtils.getProperty(target, prop);
      }
      Object root = getValue(target, prop.substring(0, indexOfNestedDelim));
      return getValue(root, prop.substring(indexOfNestedDelim + 1));
    }
    return null;
  }

  /**
   * Sets a property value, taking care of Map vs bean implementation and nested
   * properties.
   * 
   * @param target
   *          the target object.
   * @param prop
   *          the property to acccess.
   * @param value
   *          the value to set.
   * @throws IllegalAccessException
   *           whenever an exception occurs.
   * @throws InvocationTargetException
   *           whenever an exception occurs.
   * @throws NoSuchMethodException
   *           whenever an exception occurs.
   */
  protected void setValue(Object target, String prop, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    if (target != null) {
      try {
        int indexOfNestedDelim = prop.indexOf(IAccessor.NESTED_DELIM);
        if (indexOfNestedDelim < 0) {
          if (target instanceof Map) {
            if (PropertyHelper.getPropertyNames(target.getClass()).contains(
                prop)) {
              // We are explicitely on a bean property. Do not use
              // PropertyUtils.getProperty since it will detect that the target
              // is a Map and access its properties as such.
              PropertyUtils.setSimpleProperty(target, prop, value);
            } else {
              PropertyUtils.setProperty(target, prop, value);
            }
          } else {
            PropertyUtils.setProperty(target, prop, value);
          }
        } else {
          Object root = getValue(target, prop.substring(0, indexOfNestedDelim));
          setValue(root, prop.substring(indexOfNestedDelim + 1), value);
        }
      } catch (InvocationTargetException ex) {
        if (ex.getTargetException() instanceof RuntimeException) {
          throw (RuntimeException) ex.getTargetException();
        }
        throw ex;
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public Object getValue(Object target) throws IllegalAccessException,
      InvocationTargetException, NoSuchMethodException {
    return getValue(target, getProperty());
  }

  /**
   * {@inheritDoc}
   */
  public void setValue(Object target, Object value)
      throws IllegalAccessException, InvocationTargetException,
      NoSuchMethodException {
    setValue(target, getProperty(), value);
  }

  /**
   * Gets the property property.
   * 
   * @return the property.
   */
  protected String getProperty() {
    return property;
  }
}
