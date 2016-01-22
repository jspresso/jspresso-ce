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
package org.jspresso.framework.model;

import java.util.EventObject;

/**
 * A "ModelChangeEvent" event gets delivered whenever a
 * {@code IModelProvider} detects a change of its model. A ModelChangeEvent
 * object is sent as an argument to the {@code IModelChangeListener}
 * methods. Normally ValueChangeEvent are accompanied by the old and new value
 * of the changed value. If the new value is a primitive type (such as int or
 * boolean) it must be wrapped as the corresponding java.lang.* Object type
 * (such as Integer or Boolean). Null values may be provided for the old and the
 * new values if their true values are not known.
 *
 * @author Vincent Vandenschrick
 */
public class ModelChangeEvent extends EventObject {

  private static final long serialVersionUID = 3740337339792161447L;

  /**
   * New model.
   */
  private final Object            newValue;

  /**
   * Previous model.
   */
  private final Object            oldValue;

  /**
   * Constructs a new {@code ModelChangeEvent}.
   *
   * @param source
   *          The model provider that initiated the event.
   * @param oldValue
   *          The old value of the model.
   * @param newValue
   *          The new value of the model.
   */
  public ModelChangeEvent(IModelProvider source, Object oldValue,
      Object newValue) {
    super(source);
    this.newValue = newValue;
    this.oldValue = oldValue;
  }

  /**
   * Gets the new model.
   *
   * @return The new model, expressed as an {@code IPropertyChangeCapable}.
   */
  public Object getNewValue() {
    return newValue;
  }

  /**
   * Gets the old model.
   *
   * @return The old model, expressed as an {@code IPropertyChangeCapable}.
   */
  public Object getOldValue() {
    return oldValue;
  }
}
