/*
 * Copyright (c) 2005-2017 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view.descriptor.basic;

import org.jspresso.framework.view.descriptor.IStaticTextViewDescriptor;

/**
 * This type of view descriptor is used to display a static text in a form as if it was a property.
 *
 * @author Vincent Vandenschrick
 */
public class BasicStaticTextViewDescriptor extends BasicPropertyViewDescriptor
    implements IStaticTextViewDescriptor {

  private String  i18nTextKey;
  private boolean multiLine;

  /**
   * Gets the key used for getting the translated static text.
   *
   * @return the i 18 n text key
   */
  public String getI18nTextKey() {
    return i18nTextKey;
  }

  /**
   * Sets i 18 n text key.
   *
   * @param i18nTextKey
   *     the 18 n text key
   */
  public void setI18nTextKey(String i18nTextKey) {
    this.i18nTextKey = i18nTextKey;
  }

  /**
   * Is the static text multi line. {@code false} by default.
   *
   * @return the boolean
   */
  @Override
  public boolean isMultiLine() {
    return multiLine;
  }

  /**
   * Sets multi line.
   *
   * @param multiLine
   *     the multi line
   */
  public void setMultiLine(boolean multiLine) {
    this.multiLine = multiLine;
  }

  /**
   * Is read only boolean.
   *
   * @return the boolean
   */
  @Override
  public boolean isReadOnly() {
    return true;
  }

  /**
   * Sets read only.
   *
   * @param readOnly
   *     the read only
   */
  @Override
  public void setReadOnly(boolean readOnly) {
    if (!readOnly) {
      throw new UnsupportedOperationException("Static text can not be R/W.");
    }
  }
}
