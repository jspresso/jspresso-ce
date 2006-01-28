/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is the super-interface of all number property descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface INumberPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Gets the minimum value this property can have.
   * 
   * @return the minimum admissible value.
   */
  Double getMinValue();

  /**
   * Gets the maximum value this property can have.
   * 
   * @return the maximum admissible value
   */
  Double getMaxValue();

}
