/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Needed at collection getter level to determine the type of the collection
 * elements at runtime.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */

// Needed for runtime introspection.
@Retention(RetentionPolicy.RUNTIME)
// Targetted at getters
@Target(ElementType.METHOD)
public @interface ElementClass {

  /**
   * Gets the collection element class.
   */
  Class<?> value();
}
