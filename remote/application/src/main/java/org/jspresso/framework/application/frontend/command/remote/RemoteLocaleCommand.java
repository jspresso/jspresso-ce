/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.command.remote;

import java.util.Map;

/**
 * Changes the locale of the remote peer.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteLocaleCommand extends RemoteCommand {

  private static final long   serialVersionUID = -1105201434420105157L;

  private String              language;
  private String              datePattern;
  private Map<String, String> translations;

  /**
   * Gets the language.
   * 
   * @return the language.
   */
  public String getLanguage() {
    return language;
  }

  /**
   * Sets the language.
   * 
   * @param language
   *          the language to set.
   */
  public void setLanguage(String language) {
    this.language = language;
  }

  /**
   * Gets the datePattern.
   * 
   * @return the datePattern.
   */
  public String getDatePattern() {
    return datePattern;
  }

  /**
   * Sets the datePattern.
   * 
   * @param datePattern
   *          the datePattern to set.
   */
  public void setDatePattern(String datePattern) {
    this.datePattern = datePattern;
  }

  /**
   * Gets the translations.
   * 
   * @return the translations.
   */
  public Map<String, String> getTranslations() {
    return translations;
  }

  /**
   * Sets the translations.
   * 
   * @param translations
   *          the translations to set.
   */
  public void setTranslations(Map<String, String> translations) {
    this.translations = translations;
  }
}
