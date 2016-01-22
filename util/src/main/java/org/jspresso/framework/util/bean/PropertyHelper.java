/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.bean;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import org.jspresso.framework.util.accessor.IAccessor;
import org.jspresso.framework.util.exception.NestedRuntimeException;

/**
 * This helper class contains utility methods to work with bean properties.
 *
 * @author Vincent Vandenschrick
 */
public final class PropertyHelper {

  private PropertyHelper() {
    // Just here to prevent direct instantiation.
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
  public static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass,
      String property) {
    PropertyDescriptor descriptor = getPropertyDescriptorNoException(beanClass,
        property);
    if (descriptor == null) {
      throw new MissingPropertyException("Missing property " + property
          + " for bean class " + beanClass);
    }
    return descriptor;
  }

  private static PropertyDescriptor getPropertyDescriptorNoException(
      Class<?> beanClass, String property) {
    PropertyDescriptor descriptorToReturn = null;
    int nestedDotIndex = property.indexOf(IAccessor.NESTED_DELIM);
    if (nestedDotIndex > 0) {
      PropertyDescriptor rootDescriptor = getPropertyDescriptorNoException(
          beanClass, property.substring(0, nestedDotIndex));
      if (rootDescriptor != null) {
        descriptorToReturn = getPropertyDescriptorNoException(
            rootDescriptor.getPropertyType(),
            property.substring(nestedDotIndex + 1));
      }
    } else {
      PropertyDescriptor[] descriptors = PropertyUtils
          .getPropertyDescriptors(beanClass);
      for (PropertyDescriptor descriptor : descriptors) {
        if (property.substring(0, 1).equalsIgnoreCase(
            descriptor.getName().substring(0, 1))
            && property.substring(1).equals(descriptor.getName().substring(1))) {
          // 1st letter might be uppercase in descriptor and lowercase in
          // property when property name is like 'tEst'.
          descriptorToReturn = descriptor;
        }
      }
    }
    if (descriptorToReturn == null
        || descriptorToReturn.getWriteMethod() == null) {
      // If we reach this point, no property with the given name has been found.
      // or the found descriptor is read-only.
      // If beanClass is indeed an interface, we must also deal with all its
      // super-interfaces.
      List<Class<?>> superTypes = new ArrayList<>();
      if (beanClass.getSuperclass() != null
          && beanClass.getSuperclass() != Object.class) {
        superTypes.add(beanClass.getSuperclass());
      }
      Collections.addAll(superTypes, beanClass.getInterfaces());
      for (Class<?> superType : superTypes) {
        PropertyDescriptor descriptor;
        descriptor = getPropertyDescriptorNoException(superType, property);
        if (descriptor != null) {
          if (descriptorToReturn != null) {
            try {
              descriptorToReturn.setWriteMethod(descriptor.getWriteMethod());
            } catch (IntrospectionException ex) {
              throw new NestedRuntimeException(ex);
            }
          } else {
            descriptorToReturn = descriptor;
          }
        }
      }
    }
    return descriptorToReturn;
  }

  /**
   * Retrieves all property names declared by a bean class.
   *
   * @param beanClass
   *          the class to introspect.
   * @return the collection of property names.
   */
  public static Collection<String> getPropertyNames(Class<?> beanClass) {
    Collection<String> propertyNames = new HashSet<>();
    PropertyDescriptor[] descriptors = PropertyUtils
        .getPropertyDescriptors(beanClass);
    for (PropertyDescriptor descriptor : descriptors) {
      propertyNames.add(descriptor.getName());
    }
    if (beanClass.isInterface()) {
      for (Class<?> superInterface : beanClass.getInterfaces()) {
        propertyNames.addAll(getPropertyNames(superInterface));
      }
    }
    return propertyNames;
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
  public static Class<?> getPropertyType(Class<?> beanClass, String property) {
    return getPropertyDescriptor(beanClass, property).getPropertyType();
  }

  /**
   * Whenever a property name starts with a single lowercase letter, the actual
   * java bean property starts with an upper case letter.
   *
   * @param prop
   *     the property name.
   * @return the fixed java bean property name.
   */
  public static String toJavaBeanPropertyName(String prop) {
    if (prop != null && prop.length() >= 2) {
      if (Character.isLowerCase(prop.charAt(0)) && Character.isUpperCase(prop.charAt(1))) {
        StringBuilder fixedProp = new StringBuilder(prop.substring(0, 1).toUpperCase());
        fixedProp.append(prop.substring(1));
        return fixedProp.toString();
      }
    }
    return prop;
  }

  /**
   * Whenever a property name starts with a single lowercase letter, the actual
   * java bean property starts with an upper case letter.
   *
   * @param prop
   *     the property name.
   * @return the fixed java bean property name.
   */
  public static String fromJavaBeanPropertyName(String prop) {
    if (prop != null && prop.length() >= 2) {
      if (Character.isUpperCase(prop.charAt(0)) && Character.isUpperCase(prop.charAt(1))) {
        StringBuilder fixedProp = new StringBuilder(prop.substring(0, 1).toLowerCase());
        fixedProp.append(prop.substring(1));
        return fixedProp.toString();
      }
    }
    return prop;
  }
}
