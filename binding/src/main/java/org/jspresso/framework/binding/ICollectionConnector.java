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

import org.jspresso.framework.util.event.ISelectionChangeListener;

/**
 * This is the interface implemented by connectors on collections.
 *
 * @author Vincent Vandenschrick
 */
public interface ICollectionConnector extends ISelectionChangeListener,
    ICollectionConnectorProvider {

  /**
   * Clones this connector.
   *
   * @return the connector's clone.
   */
  @Override
  ICollectionConnector clone();

  /**
   * Clones this connector.
   *
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  @Override
  ICollectionConnector clone(String newConnectorId);

  /**
   * Creates a new collection element connector.
   *
   * @param connectorId
   *          the identifier of the new created connector.
   * @return the created connector
   */
  IValueConnector createChildConnector(String connectorId);

  /**
   * Returns the connector at the given index in the collection.
   *
   * @param index
   *          the index of the searched connector.
   * @return the searched connector or null if the collection is not large
   *         enough.
   */
  IValueConnector getChildConnector(int index);
}
