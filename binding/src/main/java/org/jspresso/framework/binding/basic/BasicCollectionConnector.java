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
package org.jspresso.framework.binding.basic;

import org.jspresso.framework.binding.AbstractCollectionConnector;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IMvcBinder;

/**
 * This is a simple collection connector which itself holds the connector's
 * value and which allows child connectors. This connector is useful for
 * building complex technical view models (e.g. TableModel where the underlying
 * structure would be a {@code BasicCollectionConnector}).
 *
 * @author Vincent Vandenschrick
 */
public class BasicCollectionConnector extends AbstractCollectionConnector {

  private Object connecteeValue;

  /**
   * Constructs a new instance of BasicCollectionConnector.
   *
   * @param id
   *          the connector identifier.
   * @param binder
   *          the IMvcBinder used to bind the dynamically created connectors of
   *          the collection.
   * @param childConnectorPrototype
   *          the connector prototype used to create new instances of child
   *          connectors.
   */
  public BasicCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype) {
    super(id, binder, childConnectorPrototype);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicCollectionConnector clone(String newConnectorId) {
    BasicCollectionConnector clonedConnector = (BasicCollectionConnector) super
        .clone(newConnectorId);
    clonedConnector.connecteeValue = null;
    return clonedConnector;
  }

  /**
   * Gets the self-hosted value.
   *
   * @return the self-hosted value.
   */
  @Override
  protected Object getConnecteeValue() {
    return connecteeValue;
  }

  /**
   * Sets the self-hosted value.
   *
   * @param connecteeValue
   *          the value to host
   */
  @Override
  protected void setConnecteeValue(Object connecteeValue) {
    this.connecteeValue = connecteeValue;
    if (getConnecteeValue() == null) {
      updateChildConnectors();
    }
  }
}
