/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.i18n;

import java.util.Locale;

/**
 * The interface being implemented by all I18N string providers.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITranslationProvider {

  /**
   * Gets a translated string based on a key.
   * 
   * @param key
   *            the i18n key.
   * @param locale
   *            the locale the string must be translated into.
   * @return the translated string.
   */
  String getTranslation(String key, Locale locale);

  /**
   * Gets a translated message based on a key.
   * 
   * @param key
   *            the i18n key.
   * @param args
   *            the message arguments used in message format.
   * @param locale
   *            the locale the string must be translated into.
   * @return the translated string.
   */
  String getTranslation(String key, Object[] args, Locale locale);
}
