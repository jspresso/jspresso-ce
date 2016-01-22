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

import java.util.List;

/**
 * Marks objects being able to provide a list of collection connectors.
 *
 * @author Vincent Vandenschrick
 */
public interface ICollectionConnectorListProvider extends
    ICompositeValueConnector {

  /**
   * Clones this connector.
   *
   * @return the connector's clone.
   */
  @Override
  ICollectionConnectorListProvider clone();

  /**
   * Clones this connector.
   *
   * @param newConnectorId
   *          the identifier of the clone connector
   * @return the connector's clone.
   */
  @Override
  ICollectionConnectorListProvider clone(String newConnectorId);

  /**
   * Gets the collection connector.
   *
   * @return the collection connector.
   */
  List<ICollectionConnector> getCollectionConnectors();

  /**
   * Sets whether this connector should also forward its children selections (a
   * tree connector for instance).
   *
   * @param tracksChildrenSelection
   *          true if the connector selector should also forward its children
   *          selections.
   */
  void setTracksChildrenSelection(boolean tracksChildrenSelection);

}
