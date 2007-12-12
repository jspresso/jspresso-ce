/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is the super-interface of all number property descriptors.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface INumberPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Gets the maximum value this property can have.
   * 
   * @return the maximum admissible value
   */
  Double getMaxValue();

  /**
   * Gets the minimum value this property can have.
   * 
   * @return the minimum admissible value.
   */
  Double getMinValue();

}
