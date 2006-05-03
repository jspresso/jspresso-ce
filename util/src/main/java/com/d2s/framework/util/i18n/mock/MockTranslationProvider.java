/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.i18n.mock;

import java.util.Locale;

import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * Mock implementation returning the passed key.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MockTranslationProvider implements ITranslationProvider {

  /**
   * Just return "[key]" .
   * <p>
   * {@inheritDoc}
   */
  public String getTranslation(String key, Locale locale) {
    return "[" + locale.getISO3Language() + ":" + key + "]";
  }

  /**
   * {@inheritDoc}
   */
  public String getTranslation(String key, Object[] args, Locale locale) {
    StringBuffer message = new StringBuffer(getTranslation(key, locale));
    if (args != null && args.length > 0) {
      message.append(" { ");
      for (Object arg : args) {
        message.append(String.valueOf(arg));
        message.append(" ");
      }
      message.append("}");
    }
    return message.toString();
  }

}
