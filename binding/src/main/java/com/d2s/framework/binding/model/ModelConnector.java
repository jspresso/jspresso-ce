/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
   * @param id
   *            the connector identifier.
   * @param modelDescriptor
   *            the model descriptor backing this connector.
   * @param modelConnectorFactory
   *            the factory used to create the child property connectors.
   */
  ModelConnector(String id, IComponentDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory);
    if (id != null) {
      setId(id);
    }
    this.modelProvider = new EmbeddedModelProvider(modelDescriptor);
    modelProviderChanged(null);
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
   * {@inheritDoc}
   */
  @Override
  protected boolean isValueAccessedAsProperty() {
    return false;
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
}
