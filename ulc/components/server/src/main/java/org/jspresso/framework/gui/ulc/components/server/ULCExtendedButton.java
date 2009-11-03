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

import com.ulcjava.base.application.ULCButton;
import com.ulcjava.base.application.util.ULCIcon;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCExtendedButton extends ULCButton {

  private static final long serialVersionUID = 9221025686823039202L;

  /**
   * Constructs a new <code>ULCExtendedButton</code> instance.
   */
  public ULCExtendedButton() {
    super();
    setBorder(null);
  }

  /**
   * Constructs a new <code>ULCExtendedButton</code> instance.
   * 
   * @param icon
   *            the button icon
   */
  public ULCExtendedButton(ULCIcon icon) {
    super(icon);
    setBorder(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIExtendedButton";
  }

}
