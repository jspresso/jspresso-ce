/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of string properties.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

  /**
   * Gets the regular expression pattern sample this string property must
   * conform to. This property might be used to inform the end-user of a
   * erroneous value.
   * 
   * @return the regular expression pattern sample this string preoperty must
   *         conform to.
   */
  String getRegexpPatternSample();

  /**
   * Gets whether the underlying string property should be made uppercase
   * automatically.
   * 
   * @return true if the underlying string property should be made uppercase
   *         automatically.
   */
  boolean isUpperCase();
}
