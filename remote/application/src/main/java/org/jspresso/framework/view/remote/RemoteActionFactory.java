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
package org.jspresso.framework.view.remote;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
import org.jspresso.framework.binding.ConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IConnectorValueChangeListener;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.binding.model.IModelGate;
import org.jspresso.framework.binding.model.IModelValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.model.EmbeddedModelProvider;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.ICollectionDescriptorProvider;
import org.jspresso.framework.model.descriptor.ICollectionPropertyDescriptor;
import org.jspresso.framework.model.descriptor.IComponentDescriptorProvider;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.model.descriptor.IRelationshipEndPropertyDescriptor;
import org.jspresso.framework.util.gate.GateHelper;
import org.jspresso.framework.util.gate.IGate;
import org.jspresso.framework.util.i18n.ITranslationProvider;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.IActionFactory;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A remote action factory.
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
public class RemoteActionFactory implements IActionFactory<RAction, RComponent> {

  private IGUIDGenerator        guidGenerator;
  private IIconFactory<RIcon>   iconFactory;
  private IRemoteCommandHandler remoteCommandHandler;
  private IRemotePeerRegistry   remotePeerRegistry;
  private ITranslationProvider  translationProvider;

  /**
   * {@inheritDoc}
   */
  public RAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public RAction createAction(IDisplayableAction action,
      IActionHandler actionHandler, RComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    RAction remoteAction = createRAction(action, actionHandler,
        sourceComponent, modelDescriptor, viewConnector, locale);
    if (action.getActionabilityGates() != null) {
      Collection<IGate> clonedGates = new HashSet<IGate>();
      for (IGate gate : action.getActionabilityGates()) {
        final IGate clonedGate = gate.clone();
        if (clonedGate instanceof IModelGate) {
          if (modelDescriptor instanceof IComponentDescriptorProvider) {
            ((IModelGate) clonedGate)
                .setModelProvider(new EmbeddedModelProvider(
                    (IComponentDescriptorProvider<?>) modelDescriptor));
            viewConnector
                .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

                  public void connectorValueChange(ConnectorValueChangeEvent evt) {
                    ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                        .getModelProvider()).setModel(evt.getNewValue());
                  }
                });
          } else if (modelDescriptor instanceof ICollectionPropertyDescriptor) {
            IRelationshipEndPropertyDescriptor reverseDescriptor = ((ICollectionPropertyDescriptor<?>) modelDescriptor)
                .getReverseRelationEnd();
            if (reverseDescriptor instanceof IComponentDescriptorProvider) {
              ((IModelGate) clonedGate)
                  .setModelProvider(new EmbeddedModelProvider(
                      (IComponentDescriptorProvider<?>) reverseDescriptor));
            } else if (reverseDescriptor instanceof ICollectionDescriptorProvider) {
              ((IModelGate) clonedGate)
                  .setModelProvider(new EmbeddedModelProvider(
                      ((ICollectionDescriptorProvider<?>) reverseDescriptor)
                          .getCollectionDescriptor().getElementDescriptor()));
            }
            final ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) viewConnector)
                .getCollectionConnector();
            collectionConnector
                .addConnectorValueChangeListener(new IConnectorValueChangeListener() {

                  public void connectorValueChange(
                      @SuppressWarnings("unused") ConnectorValueChangeEvent evt) {
                    if (collectionConnector.getModelConnector() != null) {
                      ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                          .getModelProvider())
                          .setModel(((IModelValueConnector) collectionConnector
                              .getModelConnector()).getModelProvider()
                              .getModel());
                    } else {
                      ((EmbeddedModelProvider) ((IModelGate) clonedGate)
                          .getModelProvider()).setModel(null);
                    }
                  }
                });
          }
        }
        clonedGates.add(clonedGate);
      }
      new GatesListener(remoteAction, clonedGates);
    }
    return remoteAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActionEnabled(RAction action, boolean enabled) {
    action.setEnabled(enabled);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setActionName(RAction action, String name) {
    action.setName(name);
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

  /**
   * Sets the iconFactory.
   * 
   * @param iconFactory
   *          the iconFactory to set.
   */
  public void setIconFactory(IIconFactory<RIcon> iconFactory) {
    this.iconFactory = iconFactory;
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
   * Sets the translationProvider.
   * 
   * @param translationProvider
   *          the translationProvider to set.
   */
  public void setTranslationProvider(ITranslationProvider translationProvider) {
    this.translationProvider = translationProvider;
  }

  private RAction createRAction(IDisplayableAction action,
      IActionHandler actionHandler, RComponent sourceComponent,
      IModelDescriptor modelDescriptor, IValueConnector viewConnector,
      Locale locale) {
    RAction remoteAction = new RAction(guidGenerator.generateGUID());
    remoteAction.setName(action.getI18nName(translationProvider, locale));
    String i18nDescription = action.getI18nDescription(translationProvider,
        locale);
    if (i18nDescription != null) {
      remoteAction.setDescription(i18nDescription);
    }
    remoteAction.setIcon(iconFactory.getIcon(action.getIconImageURL(),
        IIconFactory.TINY_ICON_SIZE));
    if (action.getMnemonicAsString() != null) {
      remoteAction.setMnemonicAsString(action.getMnemonicAsString());
    }
    ActionAdapter remoteActionAdapter = new ActionAdapter(remoteAction, action,
        actionHandler, sourceComponent, modelDescriptor, viewConnector);
    remotePeerRegistry.register(remoteActionAdapter);
    return remoteAction;
  }

  private final class ActionAdapter extends RAction {

    private IDisplayableAction action;
    private IActionHandler     actionHandler;
    private IModelDescriptor   modelDescriptor;
    private RComponent         sourceComponent;
    private IValueConnector    viewConnector;

    public ActionAdapter(RAction remoteAction, IDisplayableAction anAction,
        IActionHandler anActionHandler, RComponent aSourceComponent,
        IModelDescriptor aModelDescriptor, IValueConnector aViewConnector) {
      super(remoteAction.getGuid());
      this.action = anAction;
      this.actionHandler = anActionHandler;
      this.sourceComponent = aSourceComponent;
      this.modelDescriptor = aModelDescriptor;
      if (aModelDescriptor instanceof ICollectionDescriptor) {
        this.viewConnector = ((ICollectionConnectorProvider) aViewConnector)
            .getCollectionConnector();
      } else {
        this.viewConnector = aViewConnector;
      }
    }

    /**
     * Triggers the action execution on the action handler. The following
     * initial action context is filled in : <li>
     * <code>ActionContextConstants.SOURCE_COMPONENT</code> <li>
     * <code>ActionContextConstants.VIEW_CONNECTOR</code> <li>
     * <code>ActionContextConstants.MODEL_CONNECTOR</code> <li>
     * <code>ActionContextConstants.MODEL_DESCRIPTOR</code> <li>
     * <code>ActionContextConstants.SELECTED_INDICES</code> <li>
     * <code>ActionContextConstants.LOCALE</code>
     * 
     * @param parameter
     *          the action parameter.
     * @param viewStateGuid
     *          the guid to retrieve the view connector the action is triggred
     *          on. This is fundamental for the cell editors.
     */
    @Override
    public void actionPerformed(String parameter, String viewStateGuid) {
      if (actionHandler != null) {
        Map<String, Object> actionContext = actionHandler.createEmptyContext();
        actionContext.put(ActionContextConstants.MODEL_DESCRIPTOR,
            modelDescriptor);
        actionContext.put(ActionContextConstants.SOURCE_COMPONENT,
            sourceComponent);
        IValueConnector contextViewConnector;
        if (viewStateGuid != null) {
          contextViewConnector = (IValueConnector) remotePeerRegistry
              .getRegistered(viewStateGuid);
        } else {
          contextViewConnector = viewConnector;
        }
        actionContext.put(ActionContextConstants.VIEW_CONNECTOR,
            contextViewConnector);
        if (contextViewConnector instanceof ICollectionConnectorProvider
            && ((ICollectionConnectorProvider) contextViewConnector)
                .getCollectionConnector() != null) {
          actionContext.put(ActionContextConstants.SELECTED_INDICES,
              ((ICollectionConnectorProvider) contextViewConnector)
                  .getCollectionConnector().getSelectedIndices());
        }
        actionContext.put(ActionContextConstants.ACTION_COMMAND, parameter);
        // actionContext.put(ActionContextConstants.ACTION_WIDGET,
        // e.getSource());
        actionHandler.execute(action, actionContext);
      }
    }
  }

  private final class GatesListener implements PropertyChangeListener {

    private RAction           action;
    private Collection<IGate> gates;

    /**
     * Constructs a new <code>GatesListener</code> instance.
     * 
     * @param action
     *          the action to (de)activate based on gates state.
     * @param gates
     *          the gates that determine action state.
     */
    public GatesListener(RAction action, Collection<IGate> gates) {
      this.action = action;
      this.gates = gates;
      for (IGate gate : gates) {
        gate.addPropertyChangeListener(IGate.OPEN_PROPERTY, this);
      }
    }

    /**
     * {@inheritDoc}
     */
    public void propertyChange(
        @SuppressWarnings("unused") PropertyChangeEvent evt) {
      action.setEnabled(GateHelper.areGatesOpen(gates));

      RemoteEnablementCommand command = new RemoteEnablementCommand();
      command.setTargetPeerGuid(action.getGuid());
      command.setEnabled(action.isEnabled());
      remoteCommandHandler.registerCommand(command);
    }
  }
}
