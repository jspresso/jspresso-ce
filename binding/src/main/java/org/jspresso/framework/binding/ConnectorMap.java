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
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This class is a simple implementation of IConnectorMap. For instance, it may
 * serve for federating all the connectors of a view designed to represent the
 * same model.
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
public class ConnectorMap implements IConnectorMap {

  private ICompositeValueConnector     parentConnector;
  private Map<String, IValueConnector> storageMap;

  /**
   * Constructs a new <code>ConnectorMap</code> instance.
   * 
   * @param parentConnector
   *            the composite connector holding the connector map.
   */
  public ConnectorMap(ICompositeValueConnector parentConnector) {
    this.parentConnector = parentConnector;
  }

  /**
   * {@inheritDoc}
   */
  public void addConnector(String storageKey, IValueConnector connector) {
    if (storageKey != null && connector != null) {
      getStorageMap().put(storageKey, connector);
      connector.setParentConnector(parentConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector getConnector(String connectorId) {
    return getStorageMap().get(connectorId);
  }

  /**
   * {@inheritDoc}
   */
  public Collection<String> getStorageKeys() {
    return getStorageMap().keySet();
  }

  /**
   * {@inheritDoc}
   */
  public boolean hasConnectors() {
    return storageMap != null && !storageMap.isEmpty();
  }

  /**
   * {@inheritDoc}
   */
  public void removeConnector(String storageKey) {
    IValueConnector connectorToRemove = null;
    if (storageKey != null) {
      connectorToRemove = getStorageMap().remove(storageKey);
    }
    if (connectorToRemove != null) {
      connectorToRemove.setParentConnector(null);
    }
  }

  /**
   * Gets the parentConnector.
   * 
   * @return the parentConnector.
   */
  protected ICompositeValueConnector getParentConnector() {
    return parentConnector;
  }

  /**
   * @return Returns the storageMap.
   */
  private Map<String, IValueConnector> getStorageMap() {
    if (storageMap == null) {
      storageMap = new LinkedHashMap<String, IValueConnector>(8);
    }
    return storageMap;
  }
}
