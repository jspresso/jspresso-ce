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
import org.jspresso.framework.binding.CollectionConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorListProvider;
import org.jspresso.framework.binding.IConfigurableCollectionConnectorProvider;
import org.jspresso.framework.binding.IConfigurableConnectorFactory;
import org.jspresso.framework.binding.IFormattedValueConnector;
import org.jspresso.framework.binding.IMvcBinder;
import org.jspresso.framework.binding.IRenderableCompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.ModelRefPropertyConnector;
import org.jspresso.framework.state.remote.IRemoteStateOwner;
import org.jspresso.framework.state.remote.IRemoteValueStateFactory;
import org.jspresso.framework.state.remote.RemoteCompositeValueState;
import org.jspresso.framework.state.remote.RemoteFormattedValueState;
import org.jspresso.framework.state.remote.RemoteValueState;
import org.jspresso.framework.util.event.ISelectionChangeListener;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.SelectionChangeEvent;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.format.IFormatter;
import org.jspresso.framework.util.remote.IRemotePeer;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistryListener;
import org.jspresso.framework.util.uid.IGUIDGenerator;

/**
 * This connector factory implementation creates remote connector server peers.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteConnectorFactory implements IConfigurableConnectorFactory,
    IRemoteValueStateFactory, IRemotePeerRegistry {

  private IValueChangeListener     collectionConnectorValueChangeListener;
  private IValueChangeListener     formattedConnectorValueChangeListener;
  private IGUIDGenerator<String>   guidGenerator;
  private PropertyChangeListener   readabilityListener;
  private IRemoteCommandHandler    remoteCommandHandler;
  private IRemotePeerRegistry      remotePeerRegistry;
  private IValueChangeListener     renderingConnectorValueChangeListener;
  private ISelectionChangeListener selectionChangeListener;
  private IValueChangeListener     valueChangeListener;
  private PropertyChangeListener   writabilityListener;

  /**
   * Constructs a new {@code RemoteConnectorFactory} instance.
   */
  public RemoteConnectorFactory() {
    initAccessibilityListeners();
    initValueChangeListeners();
    initSelectionChangeListener();
  }

  private void initSelectionChangeListener() {
    selectionChangeListener = new ISelectionChangeListener() {

      @Override
      public void selectionChange(SelectionChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        IValueConnector parentConnector = connector.getParentConnector();
        if (parentConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) parentConnector)
                .getCollectionConnector() == connector
            // The next condition is to prevent commands on master-detail
            // connector wrappers.
            && !isCascadingModelWrapperConnector((ICollectionConnectorProvider) parentConnector)) {
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

  private void initValueChangeListeners() {
    valueChangeListener = new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        if (connector.getParentConnector() instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) connector
                .getParentConnector()).getRenderingConnector() == connector) {
          // don't listen to rendering connectors.
          connector.removeValueChangeListener(this);
        } else if (connector.getParentConnector() == null) {
          // don't listen to root connectors.
          connector.removeValueChangeListener(this);
        } else {
          ((IRemoteStateOwner) connector).synchRemoteState();
          RemoteValueState state = ((IRemoteStateOwner) connector).getState();
          RemoteValueCommand command = new RemoteValueCommand();
          command.setTargetPeerGuid(state.getGuid());
          command.setValue(state.getValue());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
    formattedConnectorValueChangeListener = new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        ((IRemoteStateOwner) connector).synchRemoteState();
        RemoteFormattedValueState state = (RemoteFormattedValueState) ((IRemoteStateOwner) connector)
            .getState();
        RemoteValueCommand command = new RemoteValueCommand();
        command.setTargetPeerGuid(state.getGuid());
        command.setValue(state.getValue());
        command.setValueAsObject(state.getValueAsObject());
        remoteCommandHandler.registerCommand(command);
      }
    };
    renderingConnectorValueChangeListener = new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        IRenderableCompositeValueConnector connector = (IRenderableCompositeValueConnector) ((IValueConnector) evt
            .getSource()).getParentConnector();
        ((IRemoteStateOwner) connector).synchRemoteState();
        RemoteCompositeValueState state = (RemoteCompositeValueState) ((IRemoteStateOwner) connector)
            .getState();
        RemoteValueCommand command = new RemoteValueCommand();
        command.setTargetPeerGuid(state.getGuid());
        command.setValue(state.getValue());
        command.setDescription(state.getDescription());
        command.setIconImageUrl(state.getIconImageUrl());
        remoteCommandHandler.registerCommand(command);
      }
    };
    collectionConnectorValueChangeListener = new IValueChangeListener() {

      @Override
      public void valueChange(ValueChangeEvent evt) {
        ICollectionConnector connector = (ICollectionConnector) evt.getSource();
        IValueConnector parentConnector = connector.getParentConnector();
        List<RemoteValueState> children = new ArrayList<>();
        for (int i = 0; i < connector.getChildConnectorCount(); i++) {
          IValueConnector childConnector = connector.getChildConnector(i);
          if (childConnector instanceof IRemoteStateOwner) {
            children.add(((IRemoteStateOwner) childConnector).getState());
          }
        }

        List<RemoteValueState> removedChildren = new ArrayList<>();
        if (evt instanceof CollectionConnectorValueChangeEvent) {
          if (((CollectionConnectorValueChangeEvent) evt)
              .getRemovedChildrenConnectors() != null) {
            for (IValueConnector removedChildConnector : ((CollectionConnectorValueChangeEvent) evt)
                .getRemovedChildrenConnectors()) {
              if (removedChildConnector instanceof IRemoteStateOwner) {
                removedChildren.add(((IRemoteStateOwner) removedChildConnector)
                    .getState());
              }
            }
          }
        }
        if (parentConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) parentConnector)
                .getCollectionConnector() == connector
            // The next condition is to prevent commands on master-detail
            // connector wrappers.
            && !isCascadingModelWrapperConnector((ICollectionConnectorProvider) parentConnector)) {
          RemoteCompositeValueState parentState = ((RemoteCompositeValueState) ((IRemoteStateOwner) parentConnector)
              .getState());
          parentState.setChildren(new ArrayList<>(children));

          if (!removedChildren.isEmpty()) {
            RemoteChildrenCommand parentRemoveCommand = new RemoteChildrenCommand();
            parentRemoveCommand.setTargetPeerGuid(parentState.getGuid());
            parentRemoveCommand.setChildren(removedChildren);
            parentRemoveCommand.setRemove(true);
            remoteCommandHandler.registerCommand(parentRemoveCommand);
          }

          RemoteChildrenCommand parentCommand = new RemoteChildrenCommand();
          parentCommand.setTargetPeerGuid(parentState.getGuid());
          if (parentState.getChildren() != null) {
            parentCommand.setChildren(new ArrayList<>(
                parentState.getChildren()));
          } else {
            parentCommand.setChildren(null);
          }
          parentCommand.setRemove(false);
          remoteCommandHandler.registerCommand(parentCommand);

        } else {
          RemoteCompositeValueState compositeValueState = ((RemoteCompositeValueState) ((IRemoteStateOwner) connector)
              .getState());
          compositeValueState.setChildren(children);

          if (!removedChildren.isEmpty()) {
            RemoteChildrenCommand removeCommand = new RemoteChildrenCommand();
            removeCommand.setTargetPeerGuid(compositeValueState.getGuid());
            removeCommand.setChildren(removedChildren);
            removeCommand.setRemove(true);
            remoteCommandHandler.registerCommand(removeCommand);
          }

          RemoteChildrenCommand command = new RemoteChildrenCommand();
          command.setTargetPeerGuid(compositeValueState.getGuid());
          if (compositeValueState.getChildren() != null) {
            command.setChildren(new ArrayList<>(
                compositeValueState.getChildren()));
          } else {
            command.setChildren(null);
          }
          remoteCommandHandler.registerCommand(command);
        }
        if (connector.getSelectedIndices() != null
            && connector.getSelectedIndices().length > 0) {
          // reset selection to force details refresh if any.
          connector.setSelectedIndices(null, -1);
        }
      }
    };
  }

  private void initAccessibilityListeners() {
    writabilityListener = new PropertyChangeListener() {

      @Override
      public void propertyChange(PropertyChangeEvent evt) {
        IValueConnector connector = (IValueConnector) evt.getSource();
        if (connector.getParentConnector() instanceof IRenderableCompositeValueConnector
            && ((IRenderableCompositeValueConnector) connector
                .getParentConnector()).getRenderingConnector() == connector) {
          // don't listen to rendering connectors.
          connector.removePropertyChangeListener(
              IValueConnector.WRITABLE_PROPERTY, this);
          // The following breaks notification on detail tables !!!
          // } else if (connector.getParentConnector() instanceof
          // ICollectionConnectorProvider
          // && ((ICollectionConnectorProvider) connector.getParentConnector())
          // .getCollectionConnector() == connector) {
          // // don't listen to provided collection connector.
          // connector.removePropertyChangeListener(
          // IValueConnector.WRITABLE_PROPERTY, this);
        } else if (connector.getParentConnector() == null
            && connector.getId() == null) {
          // don't listen to root connectors.
          connector.removePropertyChangeListener(
              IValueConnector.WRITABLE_PROPERTY, this);
        } else if (connector instanceof ICollectionConnectorProvider
            && isCascadingModelWrapperConnector((ICollectionConnectorProvider) connector)) {
          // don't listen to maser-detail wrappers.
          connector.removePropertyChangeListener(
              IValueConnector.WRITABLE_PROPERTY, this);
        } else {
          ((IRemoteStateOwner) connector).synchRemoteState();
          RemoteValueState state = ((IRemoteStateOwner) connector).getState();
          // state.setWritable(((Boolean) evt.getNewValue()).booleanValue());
          RemoteWritabilityCommand command = new RemoteWritabilityCommand();
          command.setTargetPeerGuid(state.getGuid());
          command.setWritable(state.isWritable());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
    readabilityListener = new PropertyChangeListener() {

      @Override
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
          ((IRemoteStateOwner) connector).synchRemoteState();
          RemoteValueState state = ((IRemoteStateOwner) connector).getState();
          RemoteReadabilityCommand command = new RemoteReadabilityCommand();
          command.setTargetPeerGuid(state.getGuid());
          command.setReadable(state.isReadable());
          remoteCommandHandler.registerCommand(command);
        }
      }
    };
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    remotePeerRegistry.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
  @Override
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
  @Override
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
  @Override
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
  @Override
  public IFormattedValueConnector createFormattedValueConnector(String id,
      IFormatter<?, ?> formatter) {
    RemoteFormattedValueConnector connector = new RemoteFormattedValueConnector(
        id, this, formatter);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteCompositeValueState createRemoteCompositeValueState(String guid,
      String permId) {
    RemoteCompositeValueState state = new RemoteCompositeValueState(guid);
    state.setPermId(registerPermId(permId, guid));
    // connectors are registered with the same guid as their state.
    // remotePeerRegistry.register(state);
    return state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteValueState createRemoteValueState(String guid, String permId) {
    RemoteValueState state = new RemoteValueState(guid);
    state.setPermId(registerPermId(permId, guid));
    // connectors are registered with the same guid as their state.
    // remotePeerRegistry.register(state);
    return state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public RemoteFormattedValueState createRemoteFormattedValueState(String guid,
      String permId) {
    RemoteFormattedValueState state = new RemoteFormattedValueState(guid);
    state.setPermId(registerPermId(permId, guid));
    // connectors are registered with the same guid as their state.
    // remotePeerRegistry.register(state);
    return state;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IValueConnector createValueConnector(String id) {
    RemoteValueConnector connector = new RemoteValueConnector(id, this);
    attachListeners(connector);
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRemotePeer getRegistered(String guid) {
    return remotePeerRegistry.getRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRemotePeer getRegisteredForPermId(String permId) {
    return remotePeerRegistry.getRegisteredForPermId(permId);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRegistered(String guid) {
    return remotePeerRegistry.isRegistered(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void register(IRemotePeer remotePeer) {
    remotePeerRegistry.register(remotePeer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String registerPermId(String permId, String guid) {
    return remotePeerRegistry.registerPermId(permId, guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRemotePeerRegistryListener(IRemotePeerRegistryListener listener) {
    remotePeerRegistry.addRemotePeerRegistryListener(listener);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRemotePeerRegistryListener(
      IRemotePeerRegistryListener listener) {
    remotePeerRegistry.removeRemotePeerRegistryListener(listener);
  }

  /**
   * Sets the guidGenerator.
   *
   * @param guidGenerator
   *          the guidGenerator to set.
   */
  public void setGuidGenerator(IGUIDGenerator<String> guidGenerator) {
    this.guidGenerator = guidGenerator;
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
   * Sets the remotePeerRegistry.
   *
   * @param remotePeerRegistry
   *          the remotePeerRegistry to set.
   */
  public void setRemotePeerRegistry(IRemotePeerRegistry remotePeerRegistry) {
    this.remotePeerRegistry = remotePeerRegistry;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String guid) {
    remotePeerRegistry.unregister(guid);
  }

  /**
   * Attach the necessary listeners to the value connector so that adequate
   * notifications get sent to the remote client side.
   *
   * @param connector
   *          the connector to attach listeners to.
   */
  protected void attachListeners(IValueConnector connector) {
    connector.addPropertyChangeListener(IValueConnector.READABLE_PROPERTY,
        readabilityListener);
    connector.addPropertyChangeListener(IValueConnector.WRITABLE_PROPERTY,
        writabilityListener);
    if (connector instanceof ICollectionConnector) {
      connector.addValueChangeListener(collectionConnectorValueChangeListener);
      ((ICollectionConnector) connector)
          .addSelectionChangeListener(selectionChangeListener);
    } else if (connector instanceof IRenderableCompositeValueConnector) {
      if (((IRenderableCompositeValueConnector) connector)
          .getRenderingConnector() != null) {
        ((IRenderableCompositeValueConnector) connector)
            .getRenderingConnector().addValueChangeListener(
                renderingConnectorValueChangeListener);
      }
    } else if (connector instanceof IFormattedValueConnector) {
      connector.addValueChangeListener(formattedConnectorValueChangeListener);
    } else {
      connector.addValueChangeListener(valueChangeListener);
    }
  }

  String generateGUID() {
    return guidGenerator.generateGUID();
  }

  private void createAndAddRenderingChildConnector(
      AbstractCompositeValueConnector compositeValueConnector,
      String renderingConnectorId) {
    if (renderingConnectorId != null) {
      compositeValueConnector.addChildConnector(renderingConnectorId,
          createValueConnector(renderingConnectorId));
      compositeValueConnector
          .setRenderingChildConnectorId(renderingConnectorId);
    }
  }

  private boolean isCascadingModelWrapperConnector(
      ICollectionConnectorProvider connector) {
    boolean hasRenderingConnector = false;
    if (connector instanceof IRenderableCompositeValueConnector) {
      hasRenderingConnector = (((IRenderableCompositeValueConnector) connector)
          .getRenderingConnector() != null);
    }
    // return (ModelRefPropertyConnector.THIS_PROPERTY.equals(connector.getId())
    // && connector.getParentConnector() == null && !hasRenderingConnector)
    // || (ModelRefPropertyConnector.THIS_PROPERTY.equals(connector.getId())
    // && connector.getParentConnector() != null
    // && ModelRefPropertyConnector.THIS_PROPERTY.equals(connector
    // .getParentConnector().getId()) && !hasRenderingConnector);
    return ModelRefPropertyConnector.THIS_PROPERTY.equals(connector.getId())
        && !hasRenderingConnector;
  }
}
