/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.model.descriptor;

/**
 * This interface is implemented by descriptors of date properties.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IDatePropertyDescriptor extends IScalarPropertyDescriptor {

  /**
   * Date-time date.
   */
  String DATE_TIME_TYPE = "DATE_TIME";

  /**
   * Day only date.
   */
  String DATE_TYPE      = "DATE";

  /**
   * Gets the date type. Values are among :
   * <li> <code>DATE_TYPE</code>
   * <li> <code>DATE_TIME_TYPE</code>
   * 
   * @return the type of this date descriptor.
   */
  String getType();

}
