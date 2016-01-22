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

import org.jspresso.framework.binding.AbstractValueConnector;

/**
 * This is a simple connector which itself holds the connector's value. This
 * connector is useful for building complex technical view models (e.g.
 * TableModel where each cell model would be an instance of
 * {@code BasicValueConnector}).
 *
 * @author Vincent Vandenschrick
 */
public class BasicValueConnector extends AbstractValueConnector {

  private Object connecteeValue;

  /**
   * Constructs a new instance of BasicValueConnector.
   *
   * @param id
   *          the connector identifier
   */
  public BasicValueConnector(String id) {
    super(id);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public BasicValueConnector clone(String newConnectorId) {
    BasicValueConnector clonedConnector = (BasicValueConnector) super
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
  }
}
