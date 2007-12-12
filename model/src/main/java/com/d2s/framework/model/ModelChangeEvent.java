/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.model;

import java.util.EventObject;

/**
 * A "ModelChangeEvent" event gets delivered whenever a
 * <code>IModelProvider</code> detects a change of its model. A
 * ModelChangeEvent object is sent as an argument to the
 * <code>IModelChangeListener</code> methods. Normally
 * ConnectorValueChangeEvent are accompanied by the old and new value of the
 * changed value. If the new value is a primitive type (such as int or boolean)
 * it must be wrapped as the corresponding java.lang.* Object type (such as
 * Integer or Boolean). Null values may be provided for the old and the new
 * values if their true values are not known.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelChangeEvent extends EventObject {

  private static final long serialVersionUID = 3740337339792161447L;

  /**
   * New model.
   */
  private Object            newValue;

  /**
   * Previous model.
   */
  private Object            oldValue;

  /**
   * Constructs a new <code>ModelChangeEvent</code>.
   * 
   * @param source
   *            The model provider that initiated the event.
   * @param oldValue
   *            The old value of the model.
   * @param newValue
   *            The new value of the model.
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
   * @return The new model, expressed as an <code>IPropertyChangeCapable</code>.
   */
  public Object getNewValue() {
    return newValue;
  }

  /**
   * Gets the old model.
   * 
   * @return The old model, expressed as an <code>IPropertyChangeCapable</code>.
   */
  public Object getOldValue() {
    return oldValue;
  }
}
