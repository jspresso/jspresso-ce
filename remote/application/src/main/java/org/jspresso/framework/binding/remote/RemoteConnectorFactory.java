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

import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteChildrenCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteReadabilityCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSelectionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteValueCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteWritabilityCommand;
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
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteValueStateFactory;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
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
public class RemoteConnectorFactory implements IConfigurableConnectorFactory,
    IRemoteValueStateFactory, IRemotePeerRegistry {

  private IGUIDGenerator                guidGenerator;
  private PropertyChangeListener        readabilityListener;
  private PropertyChangeListener        writabilityListener;
  private IConnectorValueChangeListener connectorValueChangeListener;
  private IConnectorValueChangeListener formattedConnectorValueChangeListener;
  private IConnectorValueChangeListener renderingConnectorValueChangeListener;
  private IConnectorValueChangeListener collectionConnectorValueChangeListener;
  private ISelectionChangeListener      selectionChangeListener;
  private IRemotePeerRegistry           remotePeerRegistry;
  private IRemoteCommandHandler         remoteCommandHandler;

  /**
   * Constructs a new <code>RemoteConnectorFactory</code> instance.
   */
  public RemoteConnectorFactory() {
    readabilityListener = new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        if (connector.getParentConnector() instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) connector
                .getParentConnector()).getRenderingConnector() == connector) {
          // don't listen to rendering connectors.
          connector.removePropertyChangeListener(
              IValueConnector.READABLE_PROPERTY, this);
        } else if (connector.getParentConnector() instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) connector.getParentConnector())
                .getCollectionConnector() == connector) {
          // don't listen to provided collection connector.
          connector.removePropertyChangeListener(
              IValueConnector.READABLE_PROPERTY, this);
        } else {
          RemoteValueState state = ((IRemoteStateOwner) connector).getState();
          state.setReadable(((Boolean) evt.getNewValue()).booleanValue());
          RemoteReadabilityCommand command = new RemoteReadabilityCommand();
          command.setTargetPeerGuid(state.getGuid());
          command.setReadable(state.isReadable());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
    writabilityListener = new PropertyChangeListener() {

      public void propertyChange(PropertyChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        if (connector.getParentConnector() instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) connector
                .getParentConnector()).getRenderingConnector() == connector) {
          // don't listen to rendering connectors.
          connector.removePropertyChangeListener(
              IValueConnector.WRITABLE_PROPERTY, this);
        } else if (connector.getParentConnector() instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) connector.getParentConnector())
                .getCollectionConnector() == connector) {
          // don't listen to provided collection connector.
          connector.removePropertyChangeListener(
              IValueConnector.WRITABLE_PROPERTY, this);
        } else {
          RemoteValueState state = ((IRemoteStateOwner) connector).getState();
          state.setWritable(((Boolean) evt.getNewValue()).booleanValue());
          RemoteWritabilityCommand command = new RemoteWritabilityCommand();
          command.setTargetPeerGuid(state.getGuid());
          command.setWritable(state.isWritable());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
    connectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        IValueConnector connector = evt.getSource();
        if (connector.getParentConnector() instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) connector
                .getParentConnector()).getRenderingConnector() == connector) {
          // don't listen to rendering connectors.
          connector.removeConnectorValueChangeListener(this);
        } else if (connector.getParentConnector() == null) {
          // don't listen to root connectors.
          connector.removeConnectorValueChangeListener(this);
        } else {
          RemoteValueState state = ((IRemoteStateOwner) evt.getSource())
              .getState();
          state.setValue(evt.getNewValue());
          RemoteValueCommand command = new RemoteValueCommand();
          command.setTargetPeerGuid(state.getGuid());
          command.setValue(state.getValue());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
    formattedConnectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        RemoteValueState state = ((IRemoteStateOwner) evt.getSource())
            .getState();
        state.setValue(((IFormattedValueConnector) evt.getSource())
            .getConnectorValueAsString());
        RemoteValueCommand command = new RemoteValueCommand();
        command.setTargetPeerGuid(state.getGuid());
        command.setValue(state.getValue());
        remoteCommandHandler.registerCommand(command);
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
        RemoteValueCommand command = new RemoteValueCommand();
        command.setTargetPeerGuid(state.getGuid());
        command.setValue(state.getValue());
        command.setDescription(state.getDescription());
        command.setIconImageUrl(state.getIconImageUrl());
        remoteCommandHandler.registerCommand(command);
      }
    };
    collectionConnectorValueChangeListener = new IConnectorValueChangeListener() {

      public void connectorValueChange(ConnectorValueChangeEvent evt) {
        ICollectionConnector connector = (ICollectionConnector) evt.getSource();
        IValueConnector parentConnector = connector.getParentConnector();
        List<RemoteValueState> children = new ArrayList<RemoteValueState>();
        for (int i = 0; i < connector.getChildConnectorCount(); i++) {
          IValueConnector childConnector = connector.getChildConnector(i);
          if (childConnector instanceof IRemoteStateOwner) {
            children.add(((IRemoteStateOwner) childConnector).getState());
          }
        }
        if (parentConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) parentConnector)
                .getCollectionConnector() == connector
            // The next condition is to prevent commands on master-detail
            // connector wrappers.
            && !isCascadingModelWrapperConnector(parentConnector)) {
          RemoteCompositeValueState parentState = ((RemoteCompositeValueState) ((IRemoteStateOwner) parentConnector)
              .getState());
          parentState.setChildren(new ArrayList<RemoteValueState>(children));
          RemoteChildrenCommand parentCommand = new RemoteChildrenCommand();
          parentCommand.setTargetPeerGuid(parentState.getGuid());
          if (parentState.getChildren() != null) {
            parentCommand.setChildren(new ArrayList<RemoteValueState>(
                parentState.getChildren()));
          } else {
            parentCommand.setChildren(null);
          }
          remoteCommandHandler.registerCommand(parentCommand);
        } else {
          RemoteCompositeValueState compositeValueState = ((RemoteCompositeValueState) ((IRemoteStateOwner) connector)
              .getState());
          compositeValueState.setChildren(children);
          RemoteChildrenCommand command = new RemoteChildrenCommand();
          command.setTargetPeerGuid(compositeValueState.getGuid());
          if (compositeValueState.getChildren() != null) {
            command.setChildren(new ArrayList<RemoteValueState>(
                compositeValueState.getChildren()));
          } else {
            command.setChildren(null);
          }
          remoteCommandHandler.registerCommand(command);
        }
        // reset selection to force details refresh if any.
        connector.setSelectedIndices(null, -1);
      }
    };
    selectionChangeListener = new ISelectionChangeListener() {

      public void selectionChange(SelectionChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        IValueConnector parentConnector = connector.getParentConnector();
        if (parentConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) parentConnector)
                .getCollectionConnector() == connector
            // The next condition is to prevent commands on master-detail
            // connector wrappers.
            && !isCascadingModelWrapperConnector(parentConnector)) {
          RemoteCompositeValueState parentState = ((RemoteCompositeValueState) ((IRemoteStateOwner) parentConnector)
              .getState());
          parentState.setSelectedIndices(evt.getNewSelection());
          parentState.setLeadingIndex(evt.getLeadingIndex());
          RemoteSelectionCommand parentCommand = new RemoteSelectionCommand();
          parentCommand.setTargetPeerGuid(parentState.getGuid());
          parentCommand.setLeadingIndex(parentState.getLeadingIndex());
          parentCommand.setSelectedIndices(parentState.getSelectedIndices());
          remoteCommandHandler.registerCommand(parentCommand);
        } else {
          RemoteCompositeValueState compositeValueState = ((RemoteCompositeValueState) ((IRemoteStateOwner) connector)
              .getState());
          compositeValueState.setSelectedIndices(evt.getNewSelection());
          compositeValueState.setLeadingIndex(evt.getLeadingIndex());
          RemoteSelectionCommand command = new RemoteSelectionCommand();
          command.setTargetPeerGuid(compositeValueState.getGuid());
          command.setLeadingIndex(compositeValueState.getLeadingIndex());
          command.setSelectedIndices(compositeValueState.getSelectedIndices());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
  }

  private boolean isCascadingModelWrapperConnector(IValueConnector connector) {
    return ModelRefPropertyConnector.THIS_PROPERTY.equals(connector.getId())
        && connector.getParentConnector() == null
        && !(connector instanceof IRenderableCompositeValueConnector && ((IRenderableCompositeValueConnector) connector)
            .getRenderingConnector() != null);
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

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCompositeValueState createRemoteCompositeValueState(String guid) {
    RemoteCompositeValueState state = new RemoteCompositeValueState(guid);
    // connectors are registered with the same guid as their state.
    // remotePeerRegistry.register(state);
    return state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteValueState createRemoteValueState(String guid) {
    RemoteValueState state = new RemoteValueState(guid);
    // connectors are registered with the same guid as their state.
    // remotePeerRegistry.register(state);
    return state;
  }

  /**
   * Sets the remotePeerRegistry.
   * 
   * @param remotePeerRegistry
   *          the remotePeerRegistry to set.
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    this.remotePeerRegistry = remotePeerRegistry;
  }

  /**
   * Sets the remoteCommandHandler.
   * 
   * @param remoteCommandHandler
   *          the remoteCommandHandler to set.
   */
  public void setRemoteCommandHandler(IRemoteCommandHandler remoteCommandHandler) {
    this.remoteCommandHandler = remoteCommandHandler;
  }

  /**
   * {@inheritDoc}
   */
  public IRemotePeer getRegistered(String guid) {
    return remotePeerRegistry.getRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRegistered(String guid) {
    return remotePeerRegistry.isRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  public void register(IRemotePeer remotePeer) {
    remotePeerRegistry.register(remotePeer);
  }

  /**
   * {@inheritDoc}
   */
  public void unregister(String guid) {
    remotePeerRegistry.unregister(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    remotePeerRegistry.clear();
  }
}
