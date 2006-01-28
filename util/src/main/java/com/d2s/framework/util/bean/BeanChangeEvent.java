/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.bean;

import java.util.EventObject;

/**
 * A "BeanChangeEvent" event gets delivered whenever a
 * <code>IBeanProvider</code> detects a change of its bean. A BeanChangeEvent
 * object is sent as an argument to the <code>IBeanChangeListener</code>
 * methods. Normally ConnectorValueChangeEvent are accompanied by the old and
 * new value of the changed value. If the new value is a primitive type (such as
 * int or boolean) it must be wrapped as the corresponding java.lang.* Object
 * type (such as Integer or Boolean). Null values may be provided for the old
 * and the new values if their true values are not known.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BeanChangeEvent extends EventObject {

  private static final long      serialVersionUID = 3740337339792161447L;

  /**
   * New bean.
   */
  private IPropertyChangeCapable newValue;

  /**
   * Previous bean.
   */
  private IPropertyChangeCapable oldValue;

  /**
   * Constructs a new <code>BeanChangeEvent</code>.
   * 
   * @param source
   *          The bean provider that initiated the event.
   * @param oldValue
   *          The old value of the bean.
   * @param newValue
   *          The new value of the bean.
   */
  public BeanChangeEvent(IBeanProvider source, IPropertyChangeCapable oldValue,
      IPropertyChangeCapable newValue) {
    super(source);
    this.newValue = newValue;
    this.oldValue = oldValue;
  }

  /**
   * Gets the new bean.
   * 
   * @return The new bean, expressed as an <code>IPropertyChangeCapable</code>.
   */
  public IPropertyChangeCapable getNewValue() {
    return newValue;
  }

  /**
   * Gets the old bean.
   * 
   * @return The old bean, expressed as an <code>IPropertyChangeCapable</code>.
   */
  public IPropertyChangeCapable getOldValue() {
    return oldValue;
  }
}
