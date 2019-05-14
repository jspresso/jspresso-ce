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
package org.jspresso.framework.binding.remote;

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicCollectionConnector;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteStateValueMapper;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * The server peer of a remote collection connector.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteCollectionConnector extends BasicCollectionConnector
    implements IRemotePeer, IRemoteStateOwner, IPermIdSource {

  private       String                    permId;
  private final RemoteConnectorFactory    connectorFactory;
  private       String                    guid;
  private       IRemoteStateValueMapper   remoteStateValueMapper;
  private       RemoteCompositeValueState state;

  /**
   * Constructs a new {@code RemoteCollectionConnector} instance.
   *
   * @param id
   *          the connector id.
   * @param binder
   *          the MVC binder.
   * @param childConnectorPrototype
   *          the prototype of connector children.
   * @param connectorFactory
   *          the remote connector factory.
   */
  public RemoteCollectionConnector(String id, IMvcBinder binder, ICompositeValueConnector childConnectorPrototype,
                                   RemoteConnectorFactory connectorFactory) {
    super(id, binder, childConnectorPrototype);
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
  public RemoteCollectionConnector clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCollectionConnector clone(String newConnectorId) {
    RemoteCollectionConnector clonedConnector = (RemoteCollectionConnector) super.clone(newConnectorId);
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
  public RemoteCompositeValueState getState() {
    if (state == null) {
      state = connectorFactory.createRemoteCompositeValueState(getGuid(), getPermId());
    }
    state.setValue(getDisplayValue());
    state.setReadable(isReadable());
    state.setWritable(isWritable());
    state.setDescription(getDisplayDescription());
    state.setIconImageUrl(ResourceProviderServlet.computeImageResourceDownloadUrl(getDisplayIcon(), RIcon.DEFAULT_DIM));
    state.setForeground(getDisplayForeground());
    state.setBackground(getDisplayBackground());
    state.setFont(getDisplayFont());
    state.setSelectedIndices(getSelectedIndices());
    List<RemoteValueState> children = new ArrayList<>();
    for (int i = 0; i < getChildConnectorCount(); i++) {
      IValueConnector childConnector = getChildConnector(i);
      if (childConnector instanceof IRemoteStateOwner) {
        children.add(((IRemoteStateOwner) childConnector).getState());
      }
    }
    state.setChildren(children);
    return state;
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
  @Override
  public IRemoteStateValueMapper currentRemoteStateValueMapper() {
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
    if (currentRemoteStateValueMapper() != null) {
      valueForState = currentRemoteStateValueMapper().getValueForState(state, valueForState);
    }
    return valueForState;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setValueFromState(Object stateValue) {
    Object valueFromState;
    if (currentRemoteStateValueMapper() != null) {
      valueFromState = currentRemoteStateValueMapper()
          .getValueFromState(state, stateValue);
    } else {
      valueFromState = stateValue;
    }
    setConnectorValue(valueFromState);
  }
}
