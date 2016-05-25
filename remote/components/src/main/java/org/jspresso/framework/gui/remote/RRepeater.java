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

import org.jspresso.framework.state.remote.RemoteCompositeValueState;

/**
 * A repeater component.
 *
 * @author Vincent Vandenschrick
 */
public class RRepeater extends RCollectionComponent {

  private static final long serialVersionUID = 3448113257778789L;

  private RComponent                repeated;
  private RemoteCompositeValueState viewPrototype;

  /**
   * Constructs a new {@code RRepeater} instance.
   *
   * @param guid
   *     the guid
   */
  public RRepeater(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RRepeater} instance. Only used for serialization
   * support.
   */
  public RRepeater() {
    // For serialization support
  }

  /**
   * Gets repeated.
   *
   * @return the repeated
   */
  public RComponent getRepeated() {
    return repeated;
  }

  /**
   * Sets repeated.
   *
   * @param repeated
   *     the repeated
   */
  public void setRepeated(RComponent repeated) {
    this.repeated = repeated;
  }

  /**
   * Gets view prototype.
   *
   * @return the view prototype
   */
  public RemoteCompositeValueState getViewPrototype() {
    return viewPrototype;
  }

  /**
   * Sets view prototype.
   *
   * @param viewPrototype
   *     the view prototype
   */
  public void setViewPrototype(RemoteCompositeValueState viewPrototype) {
    this.viewPrototype = viewPrototype;
  }
}
