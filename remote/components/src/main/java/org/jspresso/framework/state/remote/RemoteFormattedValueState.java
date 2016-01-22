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
package org.jspresso.framework.state.remote;

import java.io.Serializable;

/**
 * The state of a remote formatted value.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteFormattedValueState extends RemoteValueState {

  private static final long serialVersionUID = 84592858772339890L;

  private Serializable      valueAsObject;

  /**
   * Constructs a new {@code RemoteFormattedValueState} instance.
   *
   * @param guid
   *          the state guid.
   */
  public RemoteFormattedValueState(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RemoteFormattedValueState} instance. Only used
   * for GWT serialization support.
   */
  public RemoteFormattedValueState() {
    // For serialization support
  }

  /**
   * Gets the valueAsObject.
   *
   * @return the valueAsObject.
   */
  public Object getValueAsObject() {
    return valueAsObject;
  }

  /**
   * Sets the valueAsObject.
   *
   * @param valueAsObject
   *          the valueAsObject to set.
   */
  public void setValueAsObject(Object valueAsObject) {
    this.valueAsObject = tranformValue(valueAsObject);
  }

}
