/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.EventObject;

/**
 * A "ConnectorValueChangeEvent" event gets delivered whenever a connector
 * detects a change in its connectee's value. A ConnectorValueChangeEvent object
 * is sent as an argument to the IConnectorValueChangeListener methods. Normally
 * ConnectorValueChangeEvent are accompanied by the old and new value of the
 * changed value. If the new value is a primitive type (such as int or boolean)
 * it must be wrapped as the corresponding java.lang.* Object type (such as
 * Integer or Boolean). Null values may be provided for the old and the new
 * values if their true values are not known.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorValueChangeEvent extends EventObject {

  private static final long serialVersionUID = -8122264101249785686L;

  /**
   * New value for connector.
   */
  private Object            newValue;

  /**
   * Previous value for connector.
   */
  private Object            oldValue;

  /**
   * Constructs a new <code>ConnectorValueChangeEvent</code>.
   * 
   * @param source
   *            the connector that initiated the event.
   * @param oldValue
   *            the old value of the connector.
   * @param newValue
   *            the new value of the connector.
   */
  public ConnectorValueChangeEvent(IValueConnector source, Object oldValue,
      Object newValue) {
    super(source);
    this.newValue = newValue;
    this.oldValue = oldValue;
  }

  /**
   * Gets the new value of the connector.
   * 
   * @return The new value of the connector, expressed as an Object.
   */
  public Object getNewValue() {
    return newValue;
  }

  /**
   * Gets the old value of the connector.
   * 
   * @return The old value of the connector, expressed as an Object.
   */
  public Object getOldValue() {
    return oldValue;
  }

  /**
   * Narrows return type.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getSource() {
    return (IValueConnector) source;
  }
}
