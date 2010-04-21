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

import org.jspresso.framework.binding.basic.BasicFormattedValueConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.automation.IAutomationSource;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The server peer of a remote value connector that formats its value back and
 * forth as strings.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteFormattedValueConnector extends BasicFormattedValueConnector
    implements IRemotePeer, IRemoteStateOwner, IAutomationSource {

  private String                 automationSeed;
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
    return getModelConnector() != null && super.isWritable();
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
    RemoteValueState currentState = getState();
    currentState.setValue(getConnectorValueAsString());
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
        getGuid(), getAutomationSeed());
    return createdState;
  }
}
