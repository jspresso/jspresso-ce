/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model.integrity;

import java.util.Locale;

import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * This exception is thrown whenever an integrity problem occurs on an component
 * modification.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class IntegrityException extends RuntimeException {

  private static final long serialVersionUID = -5965567517919706757L;

  /**
   * Constructs a new <code>IntegrityException</code> instance.
   * 
   * @param message
   *          the exception message.
   */
  protected IntegrityException(String message) {
    super(message);
  }

  /**
   * Gets the exception localized message using a translation provider.
   * 
   * @param translationProvider
   *          the translation provider used to translate the exception message.
   * @param locale
   *          the locale to translate the exception to.
   * @return the translated message.
   */
  public abstract String getI18nMessage(
      ITranslationProvider translationProvider, Locale locale);

}
