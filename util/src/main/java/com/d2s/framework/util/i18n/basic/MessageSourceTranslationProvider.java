/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.i18n.basic;

import java.util.Locale;

import org.springframework.context.MessageSource;

import com.d2s.framework.util.i18n.ITranslationProvider;

/**
 * A translation provider wich relies on a spring message source. It will
 * typically be configured with a ressource bundle message source.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MessageSourceTranslationProvider implements ITranslationProvider {

  private MessageSource messageSource;

  /**
   * {@inheritDoc}
   */
  public String getTranslation(String key, Locale locale) {
    return messageSource.getMessage(key, null, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getMessage(String key, Object[] args, Locale locale) {
    return messageSource.getMessage(key, args, locale);
  }

  /**
   * Sets the messageSource.
   * 
   * @param messageSource
   *          the messageSource to set.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

}
