/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.model;

import com.d2s.framework.model.EmbeddedModelProvider;
import com.d2s.framework.model.IModelProvider;
import com.d2s.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * This class implements the connector mechanism on an arbitrry model. This type
 * of connector is not targetted at a specific property but at the model
 * instance itself. This implies that the <code>getConnectorValue</code>
 * method returns the model instance itself.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ModelConnector extends ModelRefPropertyConnector {

  private EmbeddedModelProvider modelProvider;

  /**
   * Constructs a new instance based on the model class passed as parameter.
   *
   * @param modelDescriptor
   *          the model descriptor backing this connector.
   * @param modelConnectorFactory
   *          the factory used to create the child property connectors.
   */
  ModelConnector(IComponentDescriptorProvider modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory);
    this.modelProvider = new EmbeddedModelProvider(modelDescriptor);
    modelProviderChanged(null);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IModelProvider getModelProvider() {
    return modelProvider;
  }

  /**
   * Returns the model itself (the java model instance).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return getModelProvider().getModel();
  }

  /**
   * Sets the model itself (the java model instance).
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    ((EmbeddedModelProvider) getModelProvider()).setModel(aValue);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean isValueAccessedAsProperty() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelConnector clone(String newConnectorId) {
    ModelConnector clonedConnector = (ModelConnector) super
        .clone(newConnectorId);
    clonedConnector.modelProvider = new EmbeddedModelProvider(modelProvider
        .getModelDescriptor());
    clonedConnector.modelProviderChanged(null);
    return clonedConnector;
  }
}
