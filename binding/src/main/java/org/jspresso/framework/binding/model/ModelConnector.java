/*
 * Copyright (c) 2005-2016 Vincent Vandenschrick. All rights reserved.
 *
 *  This file is part of the Jspresso framework.
 *
 *  Jspresso is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Jspresso is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Jspresso.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.jspresso.framework.binding.model;

import org.jspresso.framework.model.EmbeddedModelProvider;
import org.jspresso.framework.model.IModelProvider;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;

/**
 * This class implements the connector mechanism on an arbitrary model. This type
 * of connector is not targeted at a specific property but at the model
 * instance itself. This implies that the {@code getConnectorValue} method
 * returns the model instance itself.
 *
 * @author Vincent Vandenschrick
 */
public class ModelConnector extends ModelRefPropertyConnector {

  private EmbeddedModelProvider modelProvider;

  /**
   * Constructs a new instance based on the model class passed as parameter.
   *
   * @param id
   *          the connector identifier.
   * @param modelDescriptor
   *          the model descriptor backing this connector.
   * @param modelConnectorFactory
   *          the factory used to create the child property connectors.
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
  public boolean areChildrenWritable() {
    // overridden to restore local writability condition for children (opposite
    // to ModelRefPropertyConnector).
    return isWritable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public ModelConnector clone(String newConnectorId) {
    ModelConnector clonedConnector = (ModelConnector) super
        .clone(newConnectorId);
    clonedConnector.modelProvider = new EmbeddedModelProvider(
        modelProvider.getModelDescriptor());
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
