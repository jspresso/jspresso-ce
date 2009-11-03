/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.gui.ulc.components.server;

import com.ulcjava.base.application.ULCTextField;

/**
 * A subclass of ULCTextField which selects its content on focus gained..
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCOnFocusSelectTextField extends ULCTextField {

  private static final long serialVersionUID = 5891586122459055284L;

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   */
  public ULCOnFocusSelectTextField() {
    super();
  }

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   * 
   * @param columns
   *            field column.
   */
  public ULCOnFocusSelectTextField(int columns) {
    super(columns);
  }

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   * 
   * @param text
   *            field text.
   */
  public ULCOnFocusSelectTextField(String text) {
    super(text);
  }

  /**
   * Constructs a new <code>ULCOnFocusSelectTextField</code> instance.
   * 
   * @param text
   *            field text.
   * @param columns
   *            field columns.
   */
  public ULCOnFocusSelectTextField(String text, int columns) {
    super(text, columns);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIOnFocusSelectTextField";
  }
}
