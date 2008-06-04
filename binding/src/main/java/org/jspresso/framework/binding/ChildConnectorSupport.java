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
import java.util.Collections;

/**
 * This class supports the child connectors management. It is used by composite
 * connectors.
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
public class ChildConnectorSupport {

  private IConnectorMapProvider connectorMapProvider;

  /**
   * Constructs a new <code>ChildConnectorSupport</code> instance.
   * 
   * @param connectorMapProvider
   *            the provider of the connector map.
   */
  public ChildConnectorSupport(IConnectorMapProvider connectorMapProvider) {
    this.connectorMapProvider = connectorMapProvider;
  }

  /**
   * Support method holding the implementation of
   * {@link ICompositeValueConnector#getChildConnector(String)}.
   * 
   * @param connectorKey
   *            the key indexing the connector.
   * @return the child connector.
   */
  public IValueConnector getChildConnector(String connectorKey) {
    IConnectorMap childConnectors = connectorMapProvider.getConnectorMap();
    if (childConnectors != null) {
      return childConnectors.getConnector(connectorKey);
    }
    return null;
  }

  /**
   * Support method holding the implementation of
   * {@link ICompositeValueConnector#getChildConnectorKeys()}.
   * 
   * @return the child connector ids collection.
   */
  public Collection<String> getChildConnectorKeys() {
    IConnectorMap childConnectors = connectorMapProvider.getConnectorMap();
    if (childConnectors != null) {
      return childConnectors.getStorageKeys();
    }
    return Collections.emptySet();
  }

}
