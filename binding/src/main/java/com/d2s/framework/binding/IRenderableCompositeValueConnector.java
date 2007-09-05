/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This is the interface implemented by displayable composite connectors.
 * (composite).
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IRenderableCompositeValueConnector extends
    ICompositeValueConnector {

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  IRenderableCompositeValueConnector clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  IRenderableCompositeValueConnector clone(String newConnectorId);

  /**
   * Gets the connector responsible for rendering the composite connector.
   * 
   * @return the rendering connector.
   */
  IValueConnector getRenderingConnector();
}
