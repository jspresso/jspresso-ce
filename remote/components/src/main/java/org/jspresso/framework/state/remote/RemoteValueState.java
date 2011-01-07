/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
import java.util.Date;

import org.jspresso.framework.util.remote.RemotePeer;

/**
 * The state of a remote value.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteValueState extends RemotePeer {

  private static final long serialVersionUID = 8957401466928527268L;

  private boolean           readable;
  private Serializable      value;
  private boolean           writable;

  /**
   * Constructs a new <code>RemoteValueState</code> instance.
   * 
   * @param guid
   *          the state guid.
   */
  public RemoteValueState(String guid) {
    super(guid);
  }

  /**
   * Constructs a new <code>RemoteValueState</code> instance. Only used for GWT
   * serialization support.
   */
  protected RemoteValueState() {
    // For GWT support
  }

  /**
   * Gets the value.
   * 
   * @return the value.
   */
  public Object getValue() {
    return value;
  }

  /**
   * Gets the readable.
   * 
   * @return the readable.
   */
  public boolean isReadable() {
    return readable;
  }

  /**
   * Gets the writable.
   * 
   * @return the writable.
   */
  public boolean isWritable() {
    return writable;
  }

  /**
   * Sets the readable.
   * 
   * @param readable
   *          the readable to set.
   */
  public void setReadable(boolean readable) {
    this.readable = readable;
  }

  /**
   * Sets the value.
   * 
   * @param value
   *          the value to set.
   */
  public void setValue(Object value) {
    if (value instanceof Date || value instanceof String
        || value instanceof Boolean || value instanceof Number) {
      // if (value instanceof Serializable) {
      this.value = (Serializable) value;
    } else if (value != null) {
      this.value = value.toString();
    } else {
      this.value = null;
    }
  }

  /**
   * Sets the writable.
   * 
   * @param writable
   *          the writable to set.
   */
  public void setWritable(boolean writable) {
    this.writable = writable;
  }

}
