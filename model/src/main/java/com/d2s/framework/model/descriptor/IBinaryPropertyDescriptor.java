/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.List;
import java.util.Map;

/**
 * This interface is implemented by descriptors of binary properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBinaryPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Gets the maximum byte size of the underlying binary structure.
   * 
   * @return the string property maximum length.
   */
  Integer getMaxLength();

  /**
   * Gets the map of allowed file types (descriptions) and their associated
   * extensions.
   * 
   * @return the map of allowed file types (descriptions) and their associated
   *         extensions.
   */
  Map<String, List<String>> getFileFilter();
}
