/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of duration properties
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDurationPropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * One Day constant.
   */
  int ONE_DAY    = 24 * 60 * 60 * 1000;

  /**
   * One hour constant.
   */
  int ONE_HOUR   = 60 * 60 * 1000;

  /**
   * One minute constant.
   */
  int ONE_MINUTE = 60 * 1000;

  /**
   * One second constant.
   */
  int ONE_SECOND = 1000;

  /**
   * One week constant.
   */
  int ONE_WEEK   = 7 * 24 * 60 * 60 * 1000;

  /**
   * Gets the upper bound of this duration property in milliseconds. The method
   * should use the constants defined above.
   * 
   * @return the maximum number of milliseconds this duration property can take.
   */
  Long getMaxMillis();

}
