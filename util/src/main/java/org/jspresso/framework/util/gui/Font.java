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
package org.jspresso.framework.util.gui;

import java.io.Serializable;

/**
 * A simple class to represent a font.
 *
 * @author Vincent Vandenschrick
 */
public class Font implements Serializable {

  private static final long serialVersionUID = 8646399909287784996L;

  private boolean           bold;
  private boolean           italic;
  private String            name;
  private int               size;

  /**
   * Constructs a new {@code Font} instance.
   */
  public Font() {
    italic = false;
    bold = false;
  }

  /**
   * Gets the name.
   *
   * @return the name.
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the size.
   *
   * @return the size.
   */
  public int getSize() {
    return size;
  }

  /**
   * Gets the bold.
   *
   * @return the bold.
   */
  public boolean isBold() {
    return bold;
  }

  /**
   * Gets the italic.
   *
   * @return the italic.
   */
  public boolean isItalic() {
    return italic;
  }

  /**
   * Sets the bold.
   *
   * @param bold
   *          the bold to set.
   */
  public void setBold(boolean bold) {
    this.bold = bold;
  }

  /**
   * Sets the italic.
   *
   * @param italic
   *          the italic to set.
   */
  public void setItalic(boolean italic) {
    this.italic = italic;
  }

  /**
   * Sets the name.
   *
   * @param name
   *          the name to set.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Sets the size.
   *
   * @param size
   *          the size to set.
   */
  public void setSize(int size) {
    this.size = size;
  }
}
