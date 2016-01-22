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

import java.beans.PropertyChangeEvent;
import java.util.Collection;

import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;

/**
 * A connector on an independent model collection.
 *
 * @author Vincent Vandenschrick
 */
public class ModelCollectionConnector extends ModelCollectionPropertyConnector {

  private Collection<?> connecteeValue;

  /**
   * Constructs a new {@code ModelCollectionConnector} instance.
   *
   * @param id
   *          the connector identifier.
   * @param modelDescriptor
   *          the model descriptor backing this connector.
   * @param modelConnectorFactory
   *          the factory used to create the collection model connectors.
   */
  ModelCollectionConnector(String id,
      ICollectionDescriptorProvider<?> modelDescriptor,
      IModelConnectorFactory modelConnectorFactory) {
    super(modelDescriptor, modelConnectorFactory);
    if (id != null) {
      setId(id);
    }
  }

  /**
   * Returns the inner held collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected Object getConnecteeValue() {
    return connecteeValue;
  }

  /**
   * Sets the inner held collection.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void setConnecteeValue(Object aValue) {
    Collection<?> oldValue = connecteeValue;
    connecteeValue = (Collection<?>) aValue;
    propertyChange(new PropertyChangeEvent(this, "connecteeValue",
        computeOldConnectorValue(oldValue), connecteeValue));
  }
}
