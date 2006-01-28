/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of duration properties
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDurationPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * One second constant.
   */
  int ONE_SECOND = 1000;

  /**
   * One minute constant.
   */
  int ONE_MINUTE = 60 * ONE_SECOND;

  /**
   * One hour constant.
   */
  int ONE_HOUR   = 60 * ONE_MINUTE;

  /**
   * One Day constant.
   */
  int ONE_DAY    = 24 * ONE_HOUR;

  /**
   * One week constant.
   */
  int ONE_WEEK   = 7 * ONE_HOUR;

  /**
   * Gets the upper bound of this duration property in milliseconds. The method
   * should use the constants defined above.
   * 
   * @return the maximum number of milliseconds this duration property can take.
   */
  long getMaxMillis();

}
