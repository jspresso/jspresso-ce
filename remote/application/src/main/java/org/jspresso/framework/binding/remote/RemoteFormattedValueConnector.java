/*
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.RemoteFormattedValueState;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The server peer of a remote value connector that formats its value back and
 * forth as strings.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteFormattedValueConnector extends BasicFormattedValueConnector
    implements IRemotePeer, IRemoteStateOwner, IPermIdSource {

  private String                    permId;
  private final RemoteConnectorFactory    connectorFactory;
  private String                    guid;
  private IRemoteStateValueMapper   remoteStateValueMapper;
  private RemoteFormattedValueState state;

  /**
   * Constructs a new {@code RemoteFormattedValueConnector} instance.
   *
   * @param id
   *          the connector id.
   * @param connectorFactory
   *          the remote connector factory.
   * @param formatter
   *          the format used to parse and format connector value object.
   */
  public RemoteFormattedValueConnector(String id,
      RemoteConnectorFactory connectorFactory, IFormatter<?, ?> formatter) {
    super(id, formatter);
    this.guid = connectorFactory.generateGUID();
    this.connectorFactory = connectorFactory;
    connectorFactory.register(this);
  }

  /**
   * Returns the actual connector value.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public Object actualValue() {
    return getConnectorValue();
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
   * Gets the permId.
   *
   * @return the permId.
   */
  @Override
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
  @Override
  public String getGuid() {
    return guid;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteFormattedValueState getState() {
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
   * Sets the permanent identifier to this application element. Permanent
   * identifiers are used by different framework parts, like dynamic security or
   * record/replay controllers to uniquely identify an application element.
   * Permanent identifiers are generated by the SJS build based on the element
   * id but must be explicitly set if Spring XML is used.
   *
   * @param permId
   *          the permId to set.
   */
  @Override
  public void setPermId(String permId) {
    this.permId = permId;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void synchRemoteState() {
    RemoteFormattedValueState currentState = getState();
    currentState.setValue(getFormattedValue());
    currentState.setValueAsObject(getValueForState());
    currentState.setReadable(isReadable());
    currentState.setWritable(isWritable());
  }

  /**
   * Sets the remoteStateValueMapper.
   *
   * @param remoteStateValueMapper
   *          the remoteStateValueMapper to set.
   */
  @Override
  public void setRemoteStateValueMapper(
      IRemoteStateValueMapper remoteStateValueMapper) {
    this.remoteStateValueMapper = remoteStateValueMapper;
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
   * defaults to the connector value but the  is given a chance here
   * to mutate the actual object returned. This allows for changing the type of
   * objects actually exchanged with the remote frontend peer.
   *
   * @return the value that has to be set to the remote state when updating it.
   */
  protected Object getValueForState() {
    Object valueForState = getConnectorValue();
    if (getRemoteStateValueMapper() != null) {
      valueForState = getRemoteStateValueMapper().getValueForState(getState(), valueForState);
    }
    return valueForState;
  }

  /**
   * Sets the value that has to be set from the remote state when updating it.
   * It defaults to the incoming value but the developer is given a chance here
   * to mutate the actual object that comes in. This allows for changing the
   * type of objects actually exchanged with the remote frontend peer.
   *
   * @param stateValue
   *          the incoming value.
   */

  @Override
  public void setValueFromState(Object stateValue) {
    Object valueFromState;
    if (getRemoteStateValueMapper() != null) {
      valueFromState = getRemoteStateValueMapper()
          .getValueFromState(getState(), stateValue);
    } else {
      valueFromState = stateValue;
    }
    setConnectorValue(valueFromState);
    // There are rare cases (e.g. due to interceptSetter that resets the command value to the connector
    // actual state), when the connector and the state are not synced.
    synchRemoteState();
  }

  /**
   * Creates a new state instance representing this connector.
   *
   * @return the newly created state.
   */
  protected RemoteFormattedValueState createState() {
    RemoteFormattedValueState createdState = connectorFactory
        .createRemoteFormattedValueState(getGuid(), getPermId());
    return createdState;
  }
}
