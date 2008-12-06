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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.jspresso.framework.binding.AbstractCompositeValueConnector;
import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * This connector factory implementation creates remote connector server peers.
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
public class RemoteConnectorFactory implements IConfigurableConnectorFactory {

  private IGUIDGenerator                guidGenerator;
  private PropertyChangeListener        readabilityListener;
  private PropertyChangeListener        writabilityListener;
  private IConnectorValueChangeListener connectorValueChangeListener;
  private IConnectorValueChangeListener formattedConnectorValueChangeListener;
  private IConnectorValueChangeListener renderingConnectorValueChangeListener;
  private IConnectorValueChangeListener collectionConnectorValueChangeListener;
  private ISelectionChangeListener      selectionChangeListener;

  /**
   * Constructs a new <code>RemoteConnectorFactory</code> instance.
   */
  public RemoteConnectorFactory() {
    readabilityListener = new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        ((IRemoteStateOwner) evt.getSource()).getState().setReadable(
            ((Boolean) evt.getNewValue()).booleanValue());
      }
    };
    writabilityListener = new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        ((IRemoteStateOwner) evt.getSource()).getState().setWritable(
            ((Boolean) evt.getNewValue()).booleanValue());
      }
    };
    connectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        ((IRemoteStateOwner) evt.getSource()).getState().setValue(
            evt.getNewValue());
      }
    };
    formattedConnectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        ((IRemoteStateOwner) evt.getSource()).getState().setValue(
            ((IFormattedValueConnector) evt.getSource())
                .getConnectorValueAsString());
      }
    };
    renderingConnectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        IRenderableCompositeValueConnector connector = (IRenderableCompositeValueConnector) evt
            .getSource().getParentConnector();
        RemoteCompositeValueState state = (RemoteCompositeValueState) ((IRemoteStateOwner) connector)
            .getState();
        state.setValue(connector.getDisplayValue());
        state.setDescription(connector.getDisplayDescription());
        state.setIconImageUrl(connector.getDisplayIconImageUrl());
      }
    };
    collectionConnectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        RemoteCompositeValueState compositeValueState = ((RemoteCompositeValueState) ((IRemoteStateOwner) evt
            .getSource()).getState());
        ICollectionConnector connector = (ICollectionConnector) evt.getSource();
        List<RemoteValueState> children = new ArrayList<RemoteValueState>();
        for (int i = 0; i < connector.getChildConnectorCount(); i++) {
          IValueConnector childConnector = connector.getChildConnector(i);
          if (childConnector instanceof IRemoteStateOwner) {
            children.add(((IRemoteStateOwner) childConnector).getState());
          }
        }
        compositeValueState.setChildren(children);
        if (connector.getParentConnector() instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) connector.getParentConnector())
                .getCollectionConnector() == connector) {
          RemoteCompositeValueState parentState = ((RemoteCompositeValueState) ((IRemoteStateOwner) connector
              .getParentConnector()).getState());
          parentState.setChildren(new ArrayList<RemoteValueState>(children));
        }
      }
    };
    selectionChangeListener = new ISelectionChangeListener() {

      public void selectionChange(SelectionChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        RemoteCompositeValueState compositeValueState = ((RemoteCompositeValueState) ((IRemoteStateOwner) connector)
            .getState());
        compositeValueState.setSelectedIndices(evt.getNewSelection());
        compositeValueState.setLeadingIndex(evt.getLeadingIndex());
        if (connector.getParentConnector() instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) connector.getParentConnector())
                .getCollectionConnector() == connector) {
          RemoteCompositeValueState parentState = ((RemoteCompositeValueState) ((IRemoteStateOwner) connector
              .getParentConnector()).getState());
          parentState.setSelectedIndices(evt.getNewSelection());
          parentState.setLeadingIndex(evt.getLeadingIndex());
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  public ICollectionConnector createCollectionConnector(String id,
      IMvcBinder binder, ICompositeValueConnector childConnectorPrototype) {
    RemoteCollectionConnector connector = new RemoteCollectionConnector(id,
        binder, childConnectorPrototype, this);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IRenderableCompositeValueConnector createCompositeValueConnector(
      String id, String renderingConnectorId) {
    RemoteCompositeConnector connector = new RemoteCompositeConnector(id, this);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IConfigurableCollectionConnectorListProvider createConfigurableCollectionConnectorListProvider(
      String id, String renderingConnectorId) {
    RemoteCollectionConnectorListProvider connector = new RemoteCollectionConnectorListProvider(
        id, this);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IConfigurableCollectionConnectorProvider createConfigurableCollectionConnectorProvider(
      String id, String renderingConnectorId) {
    RemoteCollectionConnectorProvider connector = new RemoteCollectionConnectorProvider(
        id, this);
    createAndAddRenderingChildConnector(connector, renderingConnectorId);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IValueConnector createValueConnector(String id) {
    RemoteValueConnector connector = new RemoteValueConnector(id, this);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  public IFormattedValueConnector createFormattedValueConnector(String id,
      IFormatter formatter) {
    RemoteFormattedValueConnector connector = new RemoteFormattedValueConnector(
        id, this, formatter);
    attachListeners(connector);
    return connector;
  }

  private void createAndAddRenderingChildConnector(
      AbstractCompositeValueConnector compositeValueConnector,
      String renderingConnectorId) {
    if (renderingConnectorId != null) {
      compositeValueConnector
          .addChildConnector(createValueConnector(renderingConnectorId));
      compositeValueConnector
          .setRenderingChildConnectorId(renderingConnectorId);
    }
  }

  /**
   * Sets the guidGenerator.
   * 
   * @param guidGenerator
   *          the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator guidGenerator) {
    this.guidGenerator = guidGenerator;
  }

  void attachListeners(IValueConnector connector) {
    connector.addPropertyChangeListener(IValueConnector.READABLE_PROPERTY,
        readabilityListener);
    connector.addPropertyChangeListener(IValueConnector.WRITABLE_PROPERTY,
        writabilityListener);
    if (connector instanceof ICollectionConnector) {
      connector
          .addConnectorValueChangeListener(collectionConnectorValueChangeListener);
      ((ICollectionConnector) connector)
          .addSelectionChangeListener(selectionChangeListener);
    } else if (connector instanceof IRenderableCompositeValueConnector) {
      if (((IRenderableCompositeValueConnector) connector)
          .getRenderingConnector() != null) {
        ((IRenderableCompositeValueConnector) connector)
            .getRenderingConnector().addConnectorValueChangeListener(
                renderingConnectorValueChangeListener);
      }
    } else if (connector instanceof IFormattedValueConnector) {
      connector
          .addConnectorValueChangeListener(formattedConnectorValueChangeListener);
    } else {
      connector.addConnectorValueChangeListener(connectorValueChangeListener);
    }
  }

  String generateGUID() {
    return guidGenerator.generateGUID();
  }

}
