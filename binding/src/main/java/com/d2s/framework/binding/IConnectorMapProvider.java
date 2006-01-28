/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * A simple interface marking objects capable of providing a
 * <code>IConnectorMap</code> instance.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorMapProvider {

  /**
   * Gets the connector map holding connectors.
   * 
   * @return the connector map instance.
   */
  IConnectorMap getConnectorMap();

}
