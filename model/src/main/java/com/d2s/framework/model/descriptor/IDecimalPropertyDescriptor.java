/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of decimal properties.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDecimalPropertyDescriptor extends INumberPropertyDescriptor {

  /**
   * Returns the maximum number of fraction digits allowed in this decimal
   * property.
   * 
   * @return the number of fraction digits
   */
  Integer getMaxFractionDigit();

}
