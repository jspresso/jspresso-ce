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
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.AbstractActionFactory;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A remote action factory.
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
  public RAction createAction(IAction action, Dimension dimension,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale) {
    Dimension d = dimension;
    if (d == null) {
      d = getIconFactory().getTinyIconSize();
    }
    RAction remoteAction = createRAction(action, d, actionHandler, view, locale);
    if (action instanceof IDisplayableAction) {
      attachActionGates((IDisplayableAction) action, actionHandler, view,
          remoteAction);
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

  private RAction createRAction(IAction action, Dimension dimension,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale) {
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
          ((IDisplayableAction) action).getIconImageURL(), dimension));
      if (((IDisplayableAction) action).getMnemonicAsString() != null) {
        remoteAction.setMnemonicAsString(((IDisplayableAction) action)
            .getMnemonicAsString());
      }
    }
    ActionAdapter remoteActionAdapter = new ActionAdapter(remoteAction, action,
        actionHandler, view);
    String automationSeed = action.getAutomationSeed();
    if (automationSeed == null && action instanceof IDisplayableAction) {
      automationSeed = ((IDisplayableAction) action).getName();
    }
    String automationId = remotePeerRegistry.registerAutomationId(
        automationSeed, remoteAction.getGuid());
    remoteAction.setAutomationId(automationId);
    remotePeerRegistry.register(remoteActionAdapter);
    return remoteAction;
  }

  private final class ActionAdapter extends RAction {

    private static final long serialVersionUID = -922942515333636161L;

    private IAction           action;
    private IActionHandler    actionHandler;
    private IView<RComponent> view;

    public ActionAdapter(RAction remoteAction, IAction anAction,
        IActionHandler anActionHandler, IView<RComponent> view) {
      super(remoteAction.getGuid());
      this.action = anAction;
      this.actionHandler = anActionHandler;
      this.view = view;
    }

    /**
     * Triggers the action execution on the action handler.
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
        RComponent sourceComponent = null;
        IValueConnector viewConnector = null;
        if (view != null) {
          sourceComponent = view.getPeer();
          viewConnector = view.getConnector();
        }
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
            actionHandler, view, contextViewConnector, parameter,
            sourceComponent);
        actionContext.putAll(defaultActionContext);
        actionHandler.execute(action, actionContext);
      }
    }
  }
}
