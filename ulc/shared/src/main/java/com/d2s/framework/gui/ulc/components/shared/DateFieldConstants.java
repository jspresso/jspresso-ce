/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.shared;

/**
 * Constants shared by ULCDateField and UIDateField.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class DateFieldConstants {

  private DateFieldConstants() {
    // Empty constructor for utility class
  }

  // request constants
  /**
   * <code>SET_VALUE_REQUEST</code>.
   */
  public static final String SET_VALUE_REQUEST    = "setValue";

  /**
   * <code>SET_EDITABLE_REQUEST</code>.
   */
  public static final String SET_EDITABLE_REQUEST = "setEditable";

  // anything key constants
  /**
   * <code>VALUE_KEY</code>.
   */
  public static final String VALUE_KEY            = "value";

  /**
   * <code>LANGUAGE_KEY</code>.
   */
  public static final String LANGUAGE_KEY         = "locale";

  /**
   * <code>TIMEZONE_KEY</code>.
   */
  public static final String TIMEZONE_KEY         = "timezone";

  /**
   * <code>FORMAT_PATTERN_KEY</code>.
   */
  public static final String FORMAT_PATTERN_KEY   = "formatPattern";

  /**
   * <code>EDITABLE_KEY</code>.
   */
  public static final String EDITABLE_KEY         = "editable";
}
