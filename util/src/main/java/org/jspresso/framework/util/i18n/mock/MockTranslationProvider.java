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

import org.jspresso.framework.util.i18n.basic.MessageSourceTranslationProvider;

/**
 * Mock implementation returning the passed key.
 *
 * @author Vincent Vandenschrick
 */
public class MockTranslationProvider extends MessageSourceTranslationProvider {

  /**
   * Constructs a new {@code MockTranslationProvider} instance.
   */
  public MockTranslationProvider() {
    setMessageSource(new MockMessageSource());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getDatePattern(Locale locale) {
    return "dd/MM/yyyy";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getTimePattern(Locale locale) {
    return "HH:mm:ss";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getShortTimePattern(Locale locale) {
    return "HH:mm";
  }
}
