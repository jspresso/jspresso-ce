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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.server.remote.RemotePeerRegistryServlet;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The server peer of a remote value connector.
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
public class RemoteValueConnector extends BasicValueConnector implements
    IRemotePeer, IRemoteStateOwner {

  private RemoteConnectorFactory connectorFactory;
  private String                 guid;
  private RemoteValueState       state;

  /**
   * Constructs a new <code>RemoteValueConnector</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param connectorFactory
   *          the remote connector factory.
   */
  public RemoteValueConnector(String id, RemoteConnectorFactory connectorFactory) {
    super(id);
    this.guid = connectorFactory.generateGUID();
    this.connectorFactory = connectorFactory;
    connectorFactory.register(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteValueConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteValueConnector clone(String newConnectorId) {
    RemoteValueConnector clonedConnector = (RemoteValueConnector) super
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
  public void synchRemoteState() {
    RemoteValueState currentState = getState();
    currentState.setValue(getValueForState());
    currentState.setReadable(isReadable());
    currentState.setWritable(isWritable());
  }

  /**
   * Gets the value that has to be set to the remote state when updating it. It
   * defaults to the connector value but the developper is given a chance here
   * to mutate the actual object returned. This allows for changing the type of
   * objects actually exchanged with the remote frontend peer.
   * 
   * @return the value that has to be set to the remote state when updating it.
   */
  protected Object getValueForState() {
    Object valueForState = getConnectorValue();
    if (valueForState instanceof byte[]) {
      String valueForStateUrl = RemotePeerRegistryServlet
          .computeDownloadUrl(getGuid());
      Checksum checksumEngine = new CRC32();
      checksumEngine.update((byte[]) valueForState, 0,
          ((byte[]) valueForState).length);
      // we must add a check sum so that the client nows when the url content
      // changes.
      valueForStateUrl += ("&cs=" + checksumEngine.getValue());
      return valueForStateUrl;
    } else if (valueForState instanceof BigDecimal) {
      valueForState = new Double(((BigDecimal) valueForState).doubleValue());
    } else if (valueForState instanceof BigInteger) {
      valueForState = new Long(((BigInteger) valueForState).longValue());
    }
    return valueForState;
  }

  /**
   * Returns the actual connector value.
   * <p>
   * {@inheritDoc}
   */
  public Object actualValue() {
    return getConnectorValue();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    return getModelConnector() != null && super.isWritable();
  }
}
