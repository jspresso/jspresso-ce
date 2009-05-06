/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.springframework.context.MessageSource;

/**
 * A translation provider wich relies on a spring message source. It will
 * typically be configured with a ressource bundle message source.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
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
      return "";
    }
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

  /**
   * Gets the messageSource.
   * 
   * @return the messageSource.
   */
  protected MessageSource getMessageSource() {
    return messageSource;
  }

}
