/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.util.accessor.IAccessorFactory;

/**
 * Interface for all factories of model connectors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IModelConnectorFactory {

  /**
   * Creates a new <code>ModelConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param modelClass
   *          the model class used by the model connector.
   * @return the created ModelConnector.
   */
  ModelConnector createModelConnector(String id, Class modelClass);

  /**
   * Creates a new <code>ModelCollectionConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param elementClass
   *          the collection element class.
   * @return the created ModelCollectionConnector.
   */
  ModelCollectionConnector createModelCollectionConnector(String id,
      Class elementClass);

  /**
   * Creates a new <code>ModelPropertyConnector</code>. Depending on the type
   * of property, it might create adapted implementations of
   * <code>IValueConnector</code>. For instance, for a model property which is
   * a model reference a <code>ModelRefPropertyConnector</code> will be created.
   * 
   * @param property
   *          the java model property the connector will listen to.
   * @param propertyType
   *          the type of the property this connector is used for.
   * @return the created java model connector.
   */
  ModelPropertyConnector createModelPropertyConnector(String property,
      Class propertyType);

  /**
   * Gets the <code>IAccessorFactory</code> used.
   * 
   * @return the used accessor factory
   */
  IAccessorFactory getAccessorFactory();
}
