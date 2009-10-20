/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

import java.util.Locale;
import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.model.descriptor.ICollectionDescriptor;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.AbstractActionFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A remote action factory.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class RemoteActionFactory extends
    AbstractActionFactory<RAction, RComponent, RIcon> {

  private IGUIDGenerator        guidGenerator;
  private IRemoteCommandHandler remoteCommandHandler;
  private IRemotePeerRegistry   remotePeerRegistry;

  /**
   * {@inheritDoc}
   */
  public RAction createAction(IAction action, IActionHandler actionHandler,
      IView<RComponent> view, Locale locale) {
    return createAction(action, actionHandler, view.getPeer(), view
        .getDescriptor().getModelDescriptor(), view.getConnector(), locale);
  }

  /**
   * {@inheritDoc}
   */
  public RAction createAction(IAction action, IActionHandler actionHandler,
      RComponent sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector, Locale locale) {
    RAction remoteAction = createRAction(action, actionHandler,
        sourceComponent, modelDescriptor, viewConnector, locale);
    if (action instanceof IDisplayableAction) {
      attachActionGates((IDisplayableAction) action, actionHandler,
          modelDescriptor, viewConnector, remoteAction);
    }
    return remoteAction;
  }

  /**
   * {@inheritDoc}
   */
  public void setActionEnabled(RAction action, boolean enabled) {
    action.setEnabled(enabled);

    RemoteEnablementCommand command = new RemoteEnablementCommand();
    command.setTargetPeerGuid(action.getGuid());
    command.setEnabled(action.isEnabled());
    remoteCommandHandler.registerCommand(command);
  }

  /**
   * {@inheritDoc}
   */
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

  private RAction createRAction(IAction action, IActionHandler actionHandler,
      RComponent sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector, Locale locale) {
    RAction remoteAction = new RAction(guidGenerator.generateGUID());
    if (action instanceof IDisplayableAction) {
      remoteAction.setName(((IDisplayableAction) action).getI18nName(
          getTranslationProvider(), locale));
      String i18nDescription = ((IDisplayableAction) action)
          .getI18nDescription(getTranslationProvider(), locale);
      if (i18nDescription != null) {
        remoteAction.setDescription(i18nDescription);
      }
      remoteAction.setIcon(getIconFactory().getIcon(
          ((IDisplayableAction) action).getIconImageURL(),
          getIconFactory().getTinyIconSize()));
      if (((IDisplayableAction) action).getMnemonicAsString() != null) {
        remoteAction.setMnemonicAsString(((IDisplayableAction) action)
            .getMnemonicAsString());
      }
    }
    ActionAdapter remoteActionAdapter = new ActionAdapter(remoteAction, action,
        actionHandler, sourceComponent, modelDescriptor, viewConnector);
    remotePeerRegistry.register(remoteActionAdapter);
    return remoteAction;
  }

  private final class ActionAdapter extends RAction {

    private static final long serialVersionUID = -922942515333636161L;

    private IAction           action;
    private IActionHandler    actionHandler;
    private IModelDescriptor  modelDescriptor;
    private RComponent        sourceComponent;
    private IValueConnector   viewConnector;

    public ActionAdapter(RAction remoteAction, IAction anAction,
        IActionHandler anActionHandler, RComponent aSourceComponent,
        IModelDescriptor aModelDescriptor, IValueConnector aViewConnector) {
      super(remoteAction.getGuid());
      this.action = anAction;
      this.actionHandler = anActionHandler;
      this.sourceComponent = aSourceComponent;
      this.modelDescriptor = aModelDescriptor;
      if (aModelDescriptor instanceof ICollectionDescriptor<?>) {
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
    public void actionPerformed(String parameter, String viewStateGuid,
        Map<String, Object> context) {
      if (actionHandler != null) {
        Map<String, Object> actionContext = actionHandler.createEmptyContext();
        if (context != null) {
          actionContext.putAll(context);
        }
        IValueConnector contextViewConnector;
        if (viewStateGuid != null) {
          contextViewConnector = (IValueConnector) remotePeerRegistry
              .getRegistered(viewStateGuid);
        } else {
          contextViewConnector = viewConnector;
        }
        Map<String, Object> defaultActionContext = createActionContext(
            actionHandler, modelDescriptor, sourceComponent,
            contextViewConnector, parameter, sourceComponent);
        actionContext.putAll(defaultActionContext);
        actionHandler.execute(action, actionContext);
      }
    }
  }
}
