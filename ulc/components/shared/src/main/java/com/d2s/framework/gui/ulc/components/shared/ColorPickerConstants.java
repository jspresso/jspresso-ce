/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.shared;

/**
 * Constants shared by ULCColorPicker and UIColorPicker.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ColorPickerConstants {

  private ColorPickerConstants() {
    // Empty constructor for utility class
  }

  // request constants
  /**
   * <code>SET_VALUE_REQUEST</code>.
   */
  public static final String SET_VALUE_REQUEST      = "setValue";

  /**
   * <code>SET_RESETVALUE_REQUEST</code>.
   */
  public static final String SET_RESETVALUE_REQUEST = "setResetValue";

  // anything key constants
  /**
   * <code>VALUE_KEY</code>.
   */
  public static final String VALUE_KEY              = "value";

  /**
   * <code>RESETVALUE_KEY</code>.
   */
  public static final String RESETVALUE_KEY              = "resetValue";
}
