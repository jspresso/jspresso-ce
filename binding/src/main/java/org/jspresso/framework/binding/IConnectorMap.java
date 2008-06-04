/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

import java.util.Collection;

/**
 * This represents a map of connectors indexed by their identifier. The
 * objective of this public interface Is to federate a set of connectors
 * belonging to the same system (a form view for instance providing a connector
 * for each control). This mecanism prevents wiring one by one connectors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IConnectorMap {

  /**
   * Adds a connector to the connector map.
   * 
   * @param storageKey
   *            the key used to store the connector in the map.
   * @param connector
   *            The added connector
   */
  void addConnector(String storageKey, IValueConnector connector);

  /**
   * Gets a connector based on its storage key.
   * 
   * @param storageKey
   *            The identifier of the looked-up connector
   * @return The retrieved connector or null if none exists
   */
  IValueConnector getConnector(String storageKey);

  /**
   * Gets the collection of connector storage keys hosted by this connector map.
   * 
   * @return The collection of connector storage keys hosted by this map.
   */
  Collection<String> getStorageKeys();

  /**
   * Tells wether this connector map holds connectors.
   * 
   * @return true if this map holds connectors.
   */
  boolean hasConnectors();

  /**
   * Removes a connector from the connector map.
   * 
   * @param storageKey
   *            The removed connector storage key.
   */
  void removeConnector(String storageKey);
}
