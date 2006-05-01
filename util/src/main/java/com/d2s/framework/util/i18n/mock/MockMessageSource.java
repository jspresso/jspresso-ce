/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.i18n.mock;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 * Mock implementation of a spring message source.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MockMessageSource implements MessageSource {

  private String getTranslation(String key, Locale locale) {
    return "[" + locale.getISO3Language() + ":" + key + "]";
  }

  /**
   * {@inheritDoc}
   */
  public String getMessage(String key, Object[] args, Locale locale) {
    return getMessage(key, args, null, locale);
  }

  /**
   * {@inheritDoc}
   */
  public String getMessage(String key, Object[] args, String defaultMessage,
      Locale locale) {
    StringBuffer message = new StringBuffer(getTranslation(key, locale));
    if (args != null && args.length > 0) {
      message.append(" { ");
      for (Object arg : args) {
        message.append(String.valueOf(arg));
        message.append(" ");
      }
      message.append("}");
    }
    if (defaultMessage != null) {
      message.append("[").append(defaultMessage).append("]");
    }
    return message.toString();
  }

  /**
   * {@inheritDoc}
   */
  public String getMessage(MessageSourceResolvable resolvable, Locale locale) {
    return getMessage(resolvable.getCodes()[0], resolvable.getArguments(),
        resolvable.getDefaultMessage(), locale);
  }

}
