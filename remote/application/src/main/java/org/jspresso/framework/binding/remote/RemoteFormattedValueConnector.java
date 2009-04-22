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
package org.jspresso.framework.binding.remote;

import org.jspresso.framework.binding.basic.BasicFormattedValueConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The server peer of a remote value connector that formats its value back and
 * forth as strings.
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
public class RemoteFormattedValueConnector extends BasicFormattedValueConnector
    implements IRemotePeer, IRemoteStateOwner {

  private RemoteConnectorFactory connectorFactory;
  private String                 guid;
  private RemoteValueState       state;

  /**
   * Constructs a new <code>RemoteFormattedValueConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param connectorFactory
   *          the remote connector factory.
   * @param formatter
   *          the format used to parse and format connector value object.
   */
  public RemoteFormattedValueConnector(String id,
      RemoteConnectorFactory connectorFactory, IFormatter formatter) {
    super(id, formatter);
    this.guid = connectorFactory.generateGUID();
    this.connectorFactory = connectorFactory;
    connectorFactory.register(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteFormattedValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteFormattedValueConnector clone(String newConnectorId) {
    RemoteFormattedValueConnector clonedConnector = (RemoteFormattedValueConnector) super
        .clone(newConnectorId);
    clonedConnector.guid = connectorFactory.generateGUID();
    clonedConnector.state = null;
    connectorFactory.attachListeners(clonedConnector);
    connectorFactory.register(clonedConnector);
    return clonedConnector;
  }

  /**
   * Gets the guid.
   * 
   * @return the guid.
   */
  public String getGuid() {
    return guid;
  }

  /**
   * {@inheritDoc}
   */
  public RemoteValueState getState() {
    if (state == null) {
      state = createState();
      synchRemoteState();
    }
    return state;
  }

  /**
   * Creates a new state instance rerpesenting this connector.
   * 
   * @return the newly created state.
   */
  protected RemoteValueState createState() {
    RemoteValueState createdState = connectorFactory
        .createRemoteValueState(getGuid());
    return createdState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchRemoteState() {
    RemoteValueState currentState = getState();
    currentState.setValue(getConnectorValueAsString());
    currentState.setReadable(isReadable());
    currentState.setWritable(isWritable());
  }
}
