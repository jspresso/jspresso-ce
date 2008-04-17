/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.util.i18n.basic;

import java.util.Locale;

import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.springframework.context.MessageSource;


/**
 * A translation provider wich relies on a spring message source. It will
 * typically be configured with a ressource bundle message source.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
    if (key == null || key.length() == 0) {
      return "";
    }
    return messageSource.getMessage(key, null, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getTranslation(String key, Object[] args, Locale locale) {
    if (key == null || key.length() == 0) {
      return null;
    }
    return messageSource.getMessage(key, args, locale);
  }

  /**
   * Sets the messageSource.
   * 
   * @param messageSource
   *            the messageSource to set.
   */
  public void setMessageSource(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

}
