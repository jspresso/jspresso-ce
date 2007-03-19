/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;


/**
 * This interface is implemented by descriptors of binary properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IBinaryPropertyDescriptor extends IScalarPropertyDescriptor, IFileFilterable {

  /**
   * Gets the maximum byte size of the underlying binary structure.
   * 
   * @return the string property maximum length.
   */
  Integer getMaxLength();
}
