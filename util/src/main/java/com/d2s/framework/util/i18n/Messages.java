/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.i18n;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * I18n helper class.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class Messages {

  private static final String         BUNDLE_NAME     = "com.d2s.framework.util.i18n.Messages"; //$NON-NLS-1$

  private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
                                                          .getBundle(BUNDLE_NAME);

  /**
   * Gets an internationalized string.
   * 
   * @param key
   *          the i18n key
   * @return the internationalized string.
   */
  public static String getString(String key) {
    try {
      return RESOURCE_BUNDLE.getString(key);
    } catch (MissingResourceException e) {
      return '!' + key + '!';
    }
  }

  private Messages() {
    // Helper constructor.
  }
}
