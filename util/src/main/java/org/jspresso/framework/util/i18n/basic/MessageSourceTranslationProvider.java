/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.util.i18n.basic;

import java.util.Locale;

import org.jspresso.framework.util.i18n.AbstractTranslationProvider;
import org.jspresso.framework.util.i18n.EnhancedResourceBundleMessageSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;

/**
 * A translation provider which relies on a spring message source. It will
 * typically be configured with a resource bundle message source.
 *
 * @author Vincent Vandenschrick
 */
public class MessageSourceTranslationProvider extends
    AbstractTranslationProvider {

  private static final Logger LOG = LoggerFactory
                                      .getLogger(MessageSourceTranslationProvider.class);

  private MessageSource       messageSource;

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTranslation(String key, Object[] args,
      String defaultMessage, Locale locale) {
    if (key == null || key.length() == 0) {
      return "";
    }
    String translation = messageSource.getMessage(key, args, defaultMessage,
        locale);
    if (translation != null
        && translation
            .startsWith(EnhancedResourceBundleMessageSource.DEFAULT_MARKER)) {
      LOG.debug("{}=/*TO_REPLACE*/", key);
      StringBuilder message = new StringBuilder("[" + locale.getLanguage()
          + ":" + key + "]");
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
    if (translation != null) {
      translation = translation.trim();
    }
    return translation;
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

  /**
   * Gets the messageSource.
   *
   * @return the messageSource.
   */
  protected MessageSource getMessageSource() {
    return messageSource;
  }
}
