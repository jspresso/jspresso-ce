/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of binary properties.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBinaryPropertyDescriptor extends IScalarPropertyDescriptor,
    IFileFilterable {

  /**
   * Gets the maximum byte size of the underlying binary structure.
   * 
   * @return the string property maximum length.
   */
  Integer getMaxLength();
}
