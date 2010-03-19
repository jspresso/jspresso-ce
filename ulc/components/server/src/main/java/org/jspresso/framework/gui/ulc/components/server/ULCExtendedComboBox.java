/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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

import com.ulcjava.base.application.IComboBoxModel;
import com.ulcjava.base.application.ULCComboBox;

/**
 * This subclass implements some default behaviors which are not yet
 * configurable using ULC.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ULCExtendedComboBox extends ULCComboBox {

  private static final long serialVersionUID = -7959139931231333809L;

  /**
   * Constructs a new <code>ULCExtendedComboBox</code> instance.
   */
  public ULCExtendedComboBox() {
    super();
  }

  /**
   * Constructs a new <code>ULCExtendedComboBox</code> instance.
   * 
   * @param model
   *            the combobox model.
   */
  public ULCExtendedComboBox(IComboBoxModel model) {
    super(model);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected String typeString() {
    return "org.jspresso.framework.gui.ulc.components.client.UIExtendedComboBox";
  }

}
