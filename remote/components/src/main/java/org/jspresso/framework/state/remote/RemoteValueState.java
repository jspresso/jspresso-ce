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
import java.math.BigDecimal;
import java.sql.Timestamp;
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
   * Constructs a new <code>RemoteValueState</code> instance. Only used for
   * serialization support.
   */
  public RemoteValueState() {
    // For serialization support
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
    this.value = tranformValue(value);
  }

  /**
   * tansform object value before using it as value state.
   * 
   * @param incomingValue
   *          the original value.
   * @return the transformed value.
   */
  protected Serializable tranformValue(Object incomingValue) {
    Serializable transformedValue = null;
    if (incomingValue instanceof Timestamp) {
      transformedValue = new Date(((Timestamp) incomingValue).getTime());
    } else if (incomingValue instanceof java.sql.Date) {
      transformedValue = new Date(((java.sql.Date) incomingValue).getTime());
    } else if (incomingValue instanceof BigDecimal) {
      transformedValue = new Double(((BigDecimal) incomingValue).doubleValue());
    } else if (incomingValue instanceof Date || incomingValue instanceof String
        || incomingValue instanceof Boolean || incomingValue instanceof Number) {
      // if (value instanceof Serializable) {
      transformedValue = (Serializable) incomingValue;
    } else if (incomingValue != null) {
      transformedValue = incomingValue.toString();
    }
    return transformedValue;
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
