/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding;

import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * This public interface has to be implemented by any class which implements a
 * connector. A connector implements the glue between different part of an
 * application. For instance, there might be a client service connector which
 * dialogs with a server service connector and forwards user actions to the
 * server. Another kind of connector might be a view connector connected to a
 * model connector with both connectors' states kept synchronized.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnector extends IPropertyChangeCapable, Cloneable {

  /**
   * Clones this connector.
   * 
   * @return the connector's clone.
   */
  IConnector clone();

  /**
   * Clones this connector.
   * 
   * @param newConnectorId
   *            the identifier of the clone connector
   * @return the connector's clone.
   */
  IConnector clone(String newConnectorId);

  /**
   * Gets the identifier of the connector. In a bean property connector, this
   * identifier will be the property name.
   * 
   * @return The connector identifier
   */
  String getId();

  /**
   * Sets the identifier of the connector. In a bean property connector, this
   * identifier will be the property name.
   * 
   * @param id
   *            The connector identifier.
   */
  void setId(String id);

  /**
   * Tells this connector to fully update its state (readability, writability,
   * actionability ...).
   */
  void updateState();
}
