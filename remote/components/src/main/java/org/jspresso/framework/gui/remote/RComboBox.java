/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RComboBox extends RComponent {

  private static final long serialVersionUID = -2604499683793881316L;

  private RIcon[]           icons;
  private String[]          translations;
  private String[]          values;
  private boolean           readOnly;

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
   * Constructs a new <code>RComboBox</code> instance. Only used for
   * serialization support.
   */
  public RComboBox() {
    // For serialization support
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

  /**
   * Gets the readOnly.
   * 
   * @return the readOnly.
   */
  public boolean isReadOnly() {
    return readOnly;
  }

  /**
   * Sets the readOnly.
   * 
   * @param readOnly
   *          the readOnly to set.
   */
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
  }
}
