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
package org.jspresso.framework.binding;

import org.jspresso.framework.util.bean.AbstractPropertyChangeCapable;

/**
 * This abstract class holds some default implementation for connector. All the
 * default connectors inherit from this default behaviour.
 *
 * @author Vincent Vandenschrick
 */

public abstract class AbstractConnector extends AbstractPropertyChangeCapable
    implements IConnector {

  private String id;

  /**
   * Constructs a new AbstractConnector using an identifier. In case of a bean
   * connector, this identifier must be the bean property the connector
   * connects.
   *
   * @param id
   *          The connector identifier.
   */
  public AbstractConnector(String id) {
    this.id = id;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public AbstractConnector clone(String newConnectorId) {
    AbstractConnector clonedConnector = (AbstractConnector) super.clone();
    clonedConnector.id = newConnectorId;
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getId() {
    return id;
  }

  /**
   * Changes the connector id.
   *
   * @param id
   *          the connector identifier.
   */
  @Override
  public void setId(String id) {
    this.id = id;
  }
}
