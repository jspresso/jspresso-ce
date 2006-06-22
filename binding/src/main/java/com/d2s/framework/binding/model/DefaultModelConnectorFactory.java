/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import java.util.Collection;

import com.d2s.framework.util.accessor.IAccessorFactory;
import com.d2s.framework.util.bean.IPropertyChangeCapable;

/**
 * Default implementation for model connectors factory.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class DefaultModelConnectorFactory implements IModelConnectorFactory {

  private IAccessorFactory accessorFactory;

  /**
   * Default ModelConnector factory implementation.
   * <p>
   * {@inheritDoc}
   */
  public ModelConnector createModelConnector(String id, Class modelClass) {
    return new ModelConnector(id, modelClass, this);
  }

  /**
   * Default ModelCollectionConnector factory implementation.
   * <p>
   * {@inheritDoc}
   */
  public ModelCollectionConnector createModelCollectionConnector(String id,
      Class elementClass) {
    return new ModelCollectionConnector(id, elementClass, this);
  }

  /**
   * Creates a subclass of IValueConnector depending on the type of the model
   * property. As of now the created connectors include :
   * <ul>
   * <li><code>ModelRefPropertyConnector</code> in case of a model reference
   * property.
   * <li><code>ModelCollectionPropertyConnector</code> in case of a model
   * collection property.
   * <li><code>ModelSimplePropertyConnector</code> in all other cases.
   * </ul>
   * <p>
   * {@inheritDoc}
   */
  public ModelPropertyConnector createModelPropertyConnector(String property,
      Class propertyType) {

    if (IPropertyChangeCapable.class.isAssignableFrom(propertyType)) {
      return new ModelRefPropertyConnector(property, propertyType, this);
    }
    if (Collection.class.isAssignableFrom(propertyType)) {
      return new ModelCollectionPropertyConnector(property, this);
    }
    return new ModelSimplePropertyConnector(property, accessorFactory);
  }

  /**
   * Sets the factory for the accessors used to access the model properties.
   * 
   * @param accessorFactory
   *          The <code>IAccessorFactory</code> to use.
   */
  public void setAccessorFactory(IAccessorFactory accessorFactory) {
    this.accessorFactory = accessorFactory;
  }

  /**
   * {@inheritDoc}
   */
  public IAccessorFactory getAccessorFactory() {
    return accessorFactory;
  }
}
