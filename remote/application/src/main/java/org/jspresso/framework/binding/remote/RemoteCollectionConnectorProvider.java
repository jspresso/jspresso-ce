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

import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.basic.BasicCollectionConnectorProvider;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * The server peer of a remote collection connector provider.
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
public class RemoteCollectionConnectorProvider extends
    BasicCollectionConnectorProvider implements IRemotePeer, IRemoteStateOwner {

  private IGUIDGenerator            guidGenerator;
  private String                    guid;
  private RemoteCompositeValueState state;

  /**
   * Constructs a new <code>RemoteCollectionConnectorProvider</code> instance.
   * 
   * @param id
   *          the connector id.
   * @param guidGenerator
   *          the guid generator.
   */
  public RemoteCollectionConnectorProvider(String id,
      IGUIDGenerator guidGenerator) {
    super(id);
    this.guid = guidGenerator.generateGUID();
    this.guidGenerator = guidGenerator;
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
  @Override
  public RemoteCollectionConnectorProvider clone() {
    return clone(getId());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCollectionConnectorProvider clone(String newConnectorId) {
    RemoteCollectionConnectorProvider clonedConnector = (RemoteCollectionConnectorProvider) super
        .clone(newConnectorId);
    clonedConnector.guid = guidGenerator.generateGUID();
    return clonedConnector;
  }

  /**
   * {@inheritDoc}
   */
  public RemoteCompositeValueState getState() {
    if (state == null) {
      state = createState();
    }
    return state;
  }

  /**
   * Creates a new state instance rerpesenting this connector.
   * 
   * @return the newly created state.
   */
  protected RemoteCompositeValueState createState() {
    RemoteCompositeValueState createdState = new RemoteCompositeValueState(
        getGuid());
    createdState.setValue(getDisplayValue());
    createdState.setReadable(isReadable());
    createdState.setWritable(isWritable());
    createdState.setDescription(getDisplayDescription());
    createdState.setIconImageUrl(getDisplayIconImageUrl());
    ICollectionConnector collectionConnector = getCollectionConnector();
    List<RemoteValueState> children = new ArrayList<RemoteValueState>();
    if (collectionConnector != null) {
      for (int i = 0; i < collectionConnector.getChildConnectorCount(); i++) {
        IValueConnector childConnector = collectionConnector
            .getChildConnector(i);
        if (childConnector instanceof IRemoteStateOwner) {
          children.add(((IRemoteStateOwner) childConnector).getState());
        }
      }
    }
    createdState.setChildren(children);
    return createdState;
  }
}
