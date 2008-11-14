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

import java.util.List;

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

  private List<String> values;
  private List<String> translations;
  private List<RIcon>  icons;

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
   * Gets the translations.
   * 
   * @return the translations.
   */
  public List<String> getTranslations() {
    return translations;
  }

  /**
   * Sets the translations.
   * 
   * @param translations
   *          the translations to set.
   */
  public void setTranslations(List<String> translations) {
    this.translations = translations;
  }

  /**
   * Sets the values.
   * 
   * @param values
   *          the values to set.
   */
  public void setValues(List<String> values) {
    this.values = values;
  }

  /**
   * Gets the values.
   * 
   * @return the values.
   */
  public List<String> getValues() {
    return values;
  }

  /**
   * Sets the icons.
   * 
   * @param icons the icons to set.
   */
  public void setIcons(List<RIcon> icons) {
    this.icons = icons;
  }

  /**
   * Gets the icons.
   * 
   * @return the icons.
   */
  public List<RIcon> getIcons() {
    return icons;
  }

}
