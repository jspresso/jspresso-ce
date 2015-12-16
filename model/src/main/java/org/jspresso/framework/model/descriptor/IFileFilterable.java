/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

import java.util.List;
import java.util.Map;

/**
 * This interface is implemented by descriptors of properties which can be
 * filled using files. Typically, binary property descriptors and text property
 * descriptors implement this interface.
 * 
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

  /**
   * Returns the default file name to use when saving the content as a file.
   * 
   * @return the default file name to use when saving the content as a file.
   */
  String getFileName();

  /**
   * Returns the default content type to use when saving the content as a file.
   * 
   * @return the default content type to use when saving the content as a file.
   */
  String getContentType();
}
