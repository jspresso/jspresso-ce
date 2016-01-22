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
package org.jspresso.framework.gui.remote;

/**
 * A component containing enumerated values.
 *
 * @author Vincent Vandenschrick
 */
public abstract class REnumBox extends RComponent {

  private static final long serialVersionUID = 2417248698310163199L;

  private String[]          translations;
  private String[]          values;

  /**
   * Constructs a new {@code RComboBox} instance.
   *
   * @param guid
   *          the guid
   */
  public REnumBox(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RComboBox} instance. Only used for
   * serialization support.
   */
  public REnumBox() {
    // For serialization support
  }

  /**
   * Gets the translations.
   *
   * @return the translations.
   */
  public String[] getTranslations() {
    return translations;
  }

  /**
   * Gets the values.
   *
   * @return the values.
   */
  public String[] getValues() {
    return values;
  }

  /**
   * Sets the translations.
   *
   * @param translations
   *          the translations to set.
   */
  public void setTranslations(String... translations) {
    this.translations = translations;
  }

  /**
   * Sets the values.
   *
   * @param values
   *          the values to set.
   */
  public void setValues(String... values) {
    this.values = values;
  }
}
