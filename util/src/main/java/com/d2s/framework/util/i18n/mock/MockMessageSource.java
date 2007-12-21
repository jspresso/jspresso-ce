/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.util.i18n.mock;

import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 * Mock implementation of a spring message source.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class MockMessageSource implements MessageSource {

  private static final Log LOG = LogFactory.getLog(MockMessageSource.class);

  /**
   * {@inheritDoc}
   */
  public String getMessage(MessageSourceResolvable resolvable, Locale locale) {
    return getMessage(resolvable.getCodes()[0], resolvable.getArguments(),
        resolvable.getDefaultMessage(), locale);
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
    if (LOG.isWarnEnabled()) {
      LOG.warn(key + "=/*TO_REPLACE*/");
    }
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

  private String getTranslation(String key, Locale locale) {
    return "[" + locale.getLanguage() + ":" + key + "]";
  }

}
