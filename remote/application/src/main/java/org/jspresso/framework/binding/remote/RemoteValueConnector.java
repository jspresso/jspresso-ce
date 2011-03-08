/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

import org.jspresso.framework.binding.basic.BasicValueConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IPermIdentifiable;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The server peer of a remote value connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteValueConnector extends BasicValueConnector implements
    IRemotePeer, IRemoteStateOwner, IPermIdentifiable {

  private String                  permId;
  private RemoteConnectorFactory  connectorFactory;
  private String                  guid;
  private IRemoteStateValueMapper remoteStateValueMapper;
  private RemoteValueState        state;

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
   * Gets the permId.
   * 
   * @return the permId.
   */
  public String getPermId() {
    if (permId != null) {
      return permId;
    }
    return getId();
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
   * {@inheritDoc}
   */
  @Override
  public boolean isWritable() {
    return (getModelConnector() != null) && super.isWritable();
  }

  /**
   * Sets the permId.
   * 
   * @param permId
   *          the permId to set.
   */
  public void setPermId(String permId) {
    this.permId = permId;
  }

  /**
   * Sets the remoteStateValueMapper.
   * 
   * @param remoteStateValueMapper
   *          the remoteStateValueMapper to set.
   */
  public void setRemoteStateValueMapper(
      IRemoteStateValueMapper remoteStateValueMapper) {
    this.remoteStateValueMapper = remoteStateValueMapper;
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
   * Creates a new state instance rerpesenting this connector.
   * 
   * @return the newly created state.
   */
  protected RemoteValueState createState() {
    RemoteValueState createdState = connectorFactory.createRemoteValueState(
        getGuid(), getPermId());
    return createdState;
  }

  /**
   * Gets the remoteStateValueMapper.
   * 
   * @return the remoteStateValueMapper.
   */
  protected IRemoteStateValueMapper getRemoteStateValueMapper() {
    return remoteStateValueMapper;
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
    if (getRemoteStateValueMapper() != null) {
      valueForState = getRemoteStateValueMapper().getValueForState(
          valueForState);
    }
    return valueForState;
  }
}
