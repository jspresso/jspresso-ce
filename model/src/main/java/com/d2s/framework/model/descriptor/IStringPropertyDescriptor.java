/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of string properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IStringPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Gets the maximum length of the underlying string property.
   * 
   * @return the string property maximum length.
   */
  Integer getMaxLength();

  /**
   * Gets the regular expression pattern this string preoperty must conform to.
   * 
   * @return the regular expression pattern this string preoperty must conform
   *         to.
   */
  String getRegexpPattern();
}
