/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IAutomationSource;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;

/**
 * The server peer of a remote collection connector.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteCollectionConnector extends BasicCollectionConnector
    implements IRemotePeer, IRemoteStateOwner, IAutomationSource {

  private String                    automationSeed;
  private RemoteConnectorFactory    connectorFactory;
  private String                    guid;
  private RemoteCompositeValueState state;

  /**
   * Constructs a new <code>RemoteCollectionConnector</code> instance.
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
  public RemoteCollectionConnector(String id, IMvcBinder binder,
      ICompositeValueConnector childConnectorPrototype,
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
    RemoteCollectionConnector clonedConnector = (RemoteCollectionConnector) super
        .clone(newConnectorId);
    clonedConnector.guid = connectorFactory.generateGUID();
    clonedConnector.state = null;
    connectorFactory.attachListeners(clonedConnector);
    connectorFactory.register(clonedConnector);
    return clonedConnector;
  }

  /**
   * Gets the automationSeed.
   * 
   * @return the automationSeed.
   */
  public String getAutomationSeed() {
    if (automationSeed != null) {
      return automationSeed;
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
  public RemoteCompositeValueState getState() {
    if (state == null) {
      state = createState();
      synchRemoteState();
    }
    return state;
  }

  /**
   * Sets the automationSeed.
   * 
   * @param automationSeed
   *          the automationSeed to set.
   */
  public void setAutomationSeed(String automationSeed) {
    this.automationSeed = automationSeed;
  }

  /**
   * {@inheritDoc}
   */
  public void synchRemoteState() {
    RemoteCompositeValueState currentState = getState();
    currentState.setValue(getDisplayValue());
    currentState.setReadable(isReadable());
    currentState.setWritable(isWritable());
    currentState.setDescription(getDisplayDescription());
    currentState.setIconImageUrl(ResourceProviderServlet
        .computeImageResourceDownloadUrl(getDisplayIconImageUrl(),
            RIcon.DEFAULT_DIM));
  }

  /**
   * Creates a new state instance rerpesenting this connector.
   * 
   * @return the newly created state.
   */
  protected RemoteCompositeValueState createState() {
    RemoteCompositeValueState createdState = connectorFactory
        .createRemoteCompositeValueState(getGuid(), getAutomationSeed());
    createdState.setSelectedIndices(getSelectedIndices());
    List<RemoteValueState> children = new ArrayList<RemoteValueState>();
    for (int i = 0; i < getChildConnectorCount(); i++) {
      IValueConnector childConnector = getChildConnector(i);
      if (childConnector instanceof IRemoteStateOwner) {
        children.add(((IRemoteStateOwner) childConnector).getState());
      }
    }
    createdState.setChildren(children);
    return createdState;
  }
}
