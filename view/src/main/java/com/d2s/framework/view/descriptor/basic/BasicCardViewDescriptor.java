/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor.basic;

import java.util.LinkedHashMap;
import java.util.Map;

import com.d2s.framework.util.exception.NestedRuntimeException;

/**
 * Basic implementation of a card view descriptor. The choice of the card is
 * made upon the class of the model.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicCardViewDescriptor extends AbstractCardViewDescriptor {

  private Map<Class<?>, String> classCardMapping;

  /**
   * {@inheritDoc}
   */
  public String getCardNameForModel(Object model) {
    if (model == null) {
      return null;
    }
    Class<?> modelClass = model.getClass();
    for (Map.Entry<Class<?>, String> registeredClassCard : classCardMapping
        .entrySet()) {
      if (registeredClassCard.getKey().isAssignableFrom(modelClass)) {
        return registeredClassCard.getValue();
      }
    }
    return null;
  }

  /**
   * Sets the classCardMapping.
   * 
   * @param classNameToCardMapping
   *          the classCardMapping to set.
   */
  public void setClassNameToCardMapping(
      Map<String, String> classNameToCardMapping) {
    this.classCardMapping = new LinkedHashMap<Class<?>, String>();
    for (Map.Entry<String, String> classNameToCard : classNameToCardMapping
        .entrySet()) {
      try {
        classCardMapping.put(Class.forName(classNameToCard.getKey()),
            classNameToCard.getValue());
      } catch (ClassNotFoundException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
  }

}
