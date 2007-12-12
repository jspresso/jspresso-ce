/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

import java.util.EventObject;

/**
 * An event notifying a connector selection change. It contains the connector at
 * the source of the event (generally a collection connector) and the newly
 * selected connector (or null if none).
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorSelectionEvent extends EventObject {

  private static final long serialVersionUID = -5022556820638206657L;

  /**
   * New selected connector.
   */
  private IValueConnector   selectedConnector;

  /**
   * Constructs a new <code>ConnectorSelectionEvent</code>.
   * 
   * @param source
   *            the connector that initiated the event.
   * @param selectedConnector
   *            the new connector.
   */
  public ConnectorSelectionEvent(IValueConnector source,
      IValueConnector selectedConnector) {
    super(source);
    this.selectedConnector = selectedConnector;
  }

  /**
   * Gets the selectedConnector.
   * 
   * @return the selectedConnector.
   */
  public IValueConnector getSelectedConnector() {
    return selectedConnector;
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
