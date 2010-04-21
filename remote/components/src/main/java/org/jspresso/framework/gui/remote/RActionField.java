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
package org.jspresso.framework.gui.remote;

/**
 * A remote actionnable field component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RActionField extends RComponent {

  private static final long serialVersionUID = -5445056567090394779L;

  private boolean           showTextField;

  /**
   * Constructs a new <code>RActionField</code> instance.
   * 
   * @param guid
   *          the guid.
   */
  public RActionField(String guid) {
    super(guid);
    showTextField = true;
  }

  /**
   * Constructs a new <code>RActionField</code> instance. Only used for GWT
   * serialization support.
   */
  protected RActionField() {
    // For GWT support
  }

  /**
   * Gets the showTextField.
   * 
   * @return the showTextField.
   */
  public boolean isShowTextField() {
    return showTextField;
  }

  /**
   * Sets the showTextField.
   * 
   * @param showTextField
   *          the showTextField to set.
   */
  public void setShowTextField(boolean showTextField) {
    this.showTextField = showTextField;
  }

}
