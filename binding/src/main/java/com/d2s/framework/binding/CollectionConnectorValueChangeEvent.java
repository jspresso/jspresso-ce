/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.List;

/**
 * This class is a specific connector value change event for collection
 * connectors. It keeps track of the children connectors that were potentially
 * removed during the notification process.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class CollectionConnectorValueChangeEvent extends
    ConnectorValueChangeEvent {

  private static final long     serialVersionUID = 6547764843701088585L;
  private List<IValueConnector> removedChildrenConnectors;

  /**
   * Constructs a new <code>CollectionConnectorValueChangeEvent</code>
   * instance.
   * 
   * @param source
   *            the connector that initiated the event.
   * @param oldValue
   *            the old value of the connector.
   * @param newValue
   *            the new value of the connector.
   * @param removedChildrenConnectors
   *            the children connectors that have just been removed.
   */
  public CollectionConnectorValueChangeEvent(IValueConnector source,
      Object oldValue, Object newValue,
      List<IValueConnector> removedChildrenConnectors) {
    super(source, oldValue, newValue);
    this.removedChildrenConnectors = removedChildrenConnectors;
  }

  /**
   * Gets the removedChildrenConnectors.
   * 
   * @return the removedChildrenConnectors.
   */
  public List<IValueConnector> getRemovedChildrenConnectors() {
    return removedChildrenConnectors;
  }
}
