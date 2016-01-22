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
package org.jspresso.framework.util.i18n.mock;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceResolvable;

/**
 * Mock implementation of a spring message source.
 *
 * @author Vincent Vandenschrick
 */
public class MockMessageSource implements MessageSource {

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage(MessageSourceResolvable resolvable, Locale locale) {
    return getMessage(resolvable.getCodes()[0], resolvable.getArguments(),
        resolvable.getDefaultMessage(), locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage(String key, Object[] args, Locale locale) {
    return getMessage(key, args, null, locale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getMessage(String key, Object[] args, String defaultMessage,
      Locale locale) {
    if (defaultMessage != null) {
      return defaultMessage;
    }
    StringBuilder message = new StringBuilder("[" + locale.getLanguage() + ":"
        + key + "]");
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
