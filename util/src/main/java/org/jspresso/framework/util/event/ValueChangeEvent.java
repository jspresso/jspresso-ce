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
package org.jspresso.framework.util.event;

import java.util.EventObject;

import org.jspresso.framework.util.lang.ObjectUtils;

/**
 * A "ValueChangeEvent" event gets delivered whenever a source detects a change
 * in its value. A ValueChangeEvent object is sent as an argument to the
 * IValueChangeListener methods. Normally ValueChangeEvent are accompanied by
 * the old and new value of the changed value. If the new value is a primitive
 * type (such as int or boolean) it must be wrapped as the corresponding
 * java.lang.* Object type (such as Integer or Boolean). Null values may be
 * provided for the old and the new values if their true values are not known.
 *
 * @author Vincent Vandenschrick
 */
public class ValueChangeEvent extends EventObject {

  private static final long serialVersionUID = -8122264101249785686L;

  /**
   * New value for connector.
   */
  private final Object            newValue;

  /**
   * Previous value for connector.
   */
  private final Object            oldValue;

  /**
   * Constructs a new {@code ValueChangeEvent}.
   *
   * @param source
   *          the source that initiated the event.
   * @param oldValue
   *          the old value of the connector.
   * @param newValue
   *          the new value of the connector.
   */
  public ValueChangeEvent(Object source, Object oldValue, Object newValue) {
    super(source);
    this.newValue = newValue;
    this.oldValue = oldValue;
  }

  /**
   * Gets the new value of the source.
   *
   * @return The new value of the source, expressed as an Object.
   */
  public Object getNewValue() {
    return newValue;
  }

  /**
   * Gets the old value of the source.
   *
   * @return The old value of the source, expressed as an Object.
   */
  public Object getOldValue() {
    return oldValue;
  }

  /**
   * Tells if this event requires propagation.
   *
   * @return true if oldValue != newValue.
   */
  public boolean needsFiring() {
    return !ObjectUtils.equals(oldValue, newValue);
  }
}
