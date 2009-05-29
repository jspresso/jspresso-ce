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
package org.jspresso.framework.gui.remote;

/**
 * A component containing enumerated values.
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
public class RComboBox extends RComponent {

  private static final long serialVersionUID = -2604499683793881316L;

  private RIcon[]           icons;
  private String[]          translations;
  private String[]          values;

  /**
   * Constructs a new <code>RComboBox</code> instance. Only used for GWT
   * serialization support.
   */
  protected RComboBox() {
    // For GWT support
  }

  /**
   * Constructs a new <code>RComboBox</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RComboBox(String guid) {
    super(guid);
  }

  /**
   * Gets the icons.
   * 
   * @return the icons.
   */
  public RIcon[] getIcons() {
    return icons;
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
   * Sets the icons.
   * 
   * @param icons
   *          the icons to set.
   */
  public void setIcons(RIcon[] icons) {
    this.icons = icons;
  }

  /**
   * Sets the translations.
   * 
   * @param translations
   *          the translations to set.
   */
  public void setTranslations(String[] translations) {
    this.translations = translations;
  }

  /**
   * Sets the values.
   * 
   * @param values
   *          the values to set.
   */
  public void setValues(String[] values) {
    this.values = values;
  }

}
