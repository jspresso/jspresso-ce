/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

import java.util.List;
import java.util.Map;

/**
 * This interface is implemented by descriptors of properties which can be
 * filled using files. Typically, binary property descriptors and text property
 * descriptors implement this interface.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IFileFilterable {

  /**
   * Gets the map of allowed file types (descriptions) and their associated
   * extensions.
   * 
   * @return the map of allowed file types (descriptions) and their associated
   *         extensions.
   */
  Map<String, List<String>> getFileFilter();

}
