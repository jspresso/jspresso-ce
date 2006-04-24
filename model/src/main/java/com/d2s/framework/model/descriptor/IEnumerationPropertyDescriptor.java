/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.List;

/**
 * This interface is implemented by descriptors of enumeration properties. This
 * type of properties is constrained by a fixed value enumeration like
 * MALE/FEMALE or MONDAY/TUESDAY/WEDNESDAY/...
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IEnumerationPropertyDescriptor extends
    IScalarPropertyDescriptor {

  /**
   * Gets the name of the enumeration (like GENDER for a MALE/FEMALE
   * enumeration).
   * 
   * @return name of the underlying enumeration.
   */
  String getEnumerationName();

  /**
   * Gets the admisible values of the enumeration.
   * 
   * @return the list of values contained in the underlying enumeration.
   */
  List<String> getEnumerationValues();

  /**
   * Gets the icon image url to use to render a enumeration value.
   * 
   * @param value
   *          the value to render.
   * @return the image url to use.
   */
  String getIconImageURL(String value);
  
  /**
   * Gets the maximum length of the underlying string property.
   * 
   * @return the string property maximum length.
   */
  Integer getMaxLength();

}
