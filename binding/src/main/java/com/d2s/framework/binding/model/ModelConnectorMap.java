/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.binding.ConnectorMap;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.model.descriptor.IComponentDescriptor;

/**
 * Serves as an auto-generating ConnectorMap for maps. It may be used to hold a
 * map model in a MVC pattern.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelConnectorMap extends ConnectorMap {

  private IModelConnectorFactory modelConnectorFactory;

  /**
   * Constructs a new instance based on the model class passed as parameter.
   * 
   * @param parentConnector
   *            the model connector holding the connector map.
   * @param modelConnectorFactory
   *            the factory used to create the model connectors.
   */
  ModelConnectorMap(ModelRefPropertyConnector parentConnector,
      IModelConnectorFactory modelConnectorFactory) {
    super(parentConnector);
    this.modelConnectorFactory = modelConnectorFactory;
  }

  /**
   * Will throw an <code>UnsupportedOperationException</code>. This is a
   * self-generating connector map.
   * <p>
   * {@inheritDoc}
   * 
   * @see com.d2s.framework.binding.IConnectorMap#getConnector(String)
   */
  @Override
  public void addConnector(@SuppressWarnings("unused")
  String storageKey, @SuppressWarnings("unused")
  IValueConnector connector) {
    throw new UnsupportedOperationException();
  }

  /**
   * This method implements connector auto creation. If a connector with the
   * <code>connectorId</code> doesn't already exist, a new one is created
   * using the <code>IModelConnectorFactory</code> and register it as
   * <code>IModelChangeListener</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public IValueConnector getConnector(String connectorId) {
    ModelPropertyConnector connector = (ModelPropertyConnector) super
        .getConnector(connectorId);
    if (connector == null) {
      IComponentDescriptor<?> componentDescriptor = getParentConnector()
          .getModelDescriptor().getComponentDescriptor();
      connector = (ModelPropertyConnector) modelConnectorFactory
          .createModelConnector(componentDescriptor
              .getPropertyDescriptor(connectorId));
      super.addConnector(connectorId, connector);
    }
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected ModelRefPropertyConnector getParentConnector() {
    return (ModelRefPropertyConnector) super.getParentConnector();
  }
}
