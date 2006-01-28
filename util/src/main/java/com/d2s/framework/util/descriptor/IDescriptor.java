/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.descriptor;

/**
 * This interface is implemented by anything which can be described.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDescriptor {

  /**
   * Gets the name of this descriptor. Depending on the implementation, this
   * name can be technically meaningful (e.g. a method name, a property name,
   * ...).
   * 
   * @return The name of this descripted object
   */
  String getName();

  /**
   * Gets the end-user understandable description.
   * 
   * @return The user-friendly description
   */
  String getDescription();
}
