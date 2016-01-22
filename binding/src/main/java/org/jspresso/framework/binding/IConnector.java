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

import org.jspresso.framework.util.bean.IPropertyChangeCapable;
import org.jspresso.framework.util.lang.ICloneable;

/**
 * This public interface has to be implemented by any class which implements a
 * connector. A connector implements the glue between different part of an
 * application. For instance, there might be a client service connector which
 * dialogs with a server service connector and forwards user actions to the
 * server. Another kind of connector might be a view connector connected to a
 * model connector with both connectors' states kept synchronized.
 *
 * @author Vincent Vandenschrick
 */
public interface IConnector extends IPropertyChangeCapable, ICloneable {

  /**
   * Clones this connector.
   *
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  IConnector clone(String newConnectorId);

  /**
   * Gets the identifier of the connector. In a bean property connector, this
   * identifier will be the property name.
   *
   * @return The connector identifier
   */
  String getId();

  /**
   * Sets the identifier of the connector. In a bean property connector, this
   * identifier will be the property name.
   *
   * @param id
   *          The connector identifier.
   */
  void setId(String id);
}
