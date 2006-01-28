/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.beans.PropertyDescriptor;
import java.util.Collection;
import java.util.HashSet;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * This helper class contains utility methods to work with bean properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class PropertyHelper {

  private PropertyHelper() {
    // Just here to prevent direct instanciation.
  }

  /**
   * Retrieves the type of a bean property.
   * 
   * @param beanClass
   *          the bean class on which to look for the property.
   * @param property
   *          the property to look for.
   * @return the type of the property.
   */
  public static Class getPropertyType(Class beanClass, String property) {
    return getPropertyDescriptor(beanClass, property).getPropertyType();
  }

  /**
   * Retrieves all property names declared by a bean class.
   * 
   * @param beanClass
   *          the class to introspect.
   * @return the collection of property names.
   */
  public static Collection<String> getPropertyNames(Class beanClass) {
    Collection<String> propertyNames = new HashSet<String>();
    PropertyDescriptor[] descriptors = PropertyUtils
        .getPropertyDescriptors(beanClass);
    for (PropertyDescriptor descriptor : descriptors) {
      propertyNames.add(descriptor.getName());
    }
    if (beanClass.isInterface()) {
      for (Class superInterface : beanClass.getInterfaces()) {
        propertyNames.addAll(getPropertyNames(superInterface));
      }
    }
    return propertyNames;
  }

  /**
   * Gets the property descriptor of a property on a specified class. If the
   * specified class is an interface, all its super-interfaces are also
   * processed.
   * 
   * @param beanClass
   *          the class to get the property descriptor of.
   * @param property
   *          the property to be searched for its descriptor.
   * @return the property descriptor found.
   */
  public static PropertyDescriptor getPropertyDescriptor(Class beanClass,
      String property) {
    PropertyDescriptor[] descriptors = PropertyUtils
        .getPropertyDescriptors(beanClass);
    for (PropertyDescriptor descriptor : descriptors) {
      if (property.equals(descriptor.getName())) {
        return descriptor;
      }
    }
    // If we reach this point, no property with the given name has been found.
    // If beanClass is indeed an interface, we must also deal with all its
    // super-interfaces.
    if (beanClass.isInterface()) {
      for (Class superInterface : beanClass.getInterfaces()) {
        PropertyDescriptor descriptor = null;
        try {
          descriptor = getPropertyDescriptor(superInterface, property);
        } catch (MissingPropertyException ex) {
          // This exception must be ignored until we traverse all the super
          // interfaces.
        }
        if (descriptor != null) {
          return descriptor;
        }
      }
    }
    throw new MissingPropertyException("Missing property " + property
        + " for bean class " + beanClass);
  }

}
