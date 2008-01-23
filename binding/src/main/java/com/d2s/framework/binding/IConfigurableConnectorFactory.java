/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.binding;

/**
 * This is the interface defining the contract of a configurable connector
 * factory. These factories are designed to be used by view factories.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConfigurableConnectorFactory {

  /**
   * Creates a <code>ICollectionConnector</code>.
   * 
   * @param id
   *            the connector identifier.
   * @param binder
   *            the MVC bider used by the created collection connector.
   * @param childConnectorPrototype
   *            the element prototype connector used by the created collection
   *            connector.
   * @return the created connector.
   */
  ICollectionConnector createCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype);

  /**
   * Creates a <code>ICompositeValueConnector</code>.
   * 
   * @param id
   *            the connector identifier.
   * @param renderingConnectorId
   *            the child connector used to render the composite connector
   *            value.
   * @return the created connector.
   */
  ICompositeValueConnector createCompositeValueConnector(String id,
      String renderingConnectorId);

  /**
   * Creates a <code>IConfigurableCollectionConnectorListProvider</code>.
   * 
   * @param id
   *            the connector identifier.
   * @param renderingConnectorId
   *            the child connector used to render the composite connector
   *            value.
   * @return the created connector.
   */
  IConfigurableCollectionConnectorListProvider createConfigurableCollectionConnectorListProvider(
      String id, String renderingConnectorId);

  /**
   * Creates a <code>IConfigurableCollectionConnectorProvider</code>.
   * 
   * @param id
   *            the connector identifier.
   * @param renderingConnectorId
   *            the child connector used to render the composite connector
   *            value.
   * @return the created connector.
   */
  IConfigurableCollectionConnectorProvider createConfigurableCollectionConnectorProvider(
      String id, String renderingConnectorId);

  /**
   * Creates a <code>IValueConnector</code>.
   * 
   * @param id
   *            the connector identifier.
   * @return the created connector.
   */
  IValueConnector createValueConnector(String id);
}
