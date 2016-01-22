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
package org.jspresso.framework.view.remote;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteEnablementCommand;
import org.jspresso.framework.application.view.ControllerAwareActionFactory;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RActionEvent;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.util.descriptor.IStylable;
import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.remote.registry.IRemotePeerRegistry;
import org.jspresso.framework.util.uid.IGUIDGenerator;
import org.jspresso.framework.view.IView;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * A remote action factory.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteActionFactory extends
    ControllerAwareActionFactory<RAction, RComponent, RIcon> {

  private IGUIDGenerator<String> guidGenerator;
  private IRemoteCommandHandler  remoteCommandHandler;
  private IRemotePeerRegistry    remotePeerRegistry;

  @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
  private Set<RAction>           hardReferences;

  /**
   * {@inheritDoc}
   */
  @Override
  public RAction createAction(IAction action, Dimension dimension,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale) {
    if (action == null) {
      return null;
    }
    Dimension d = dimension;
    if (d == null) {
      d = getIconFactory().getTinyIconSize();
    }
    RAction remoteAction = createRAction(action, d, actionHandler, view, locale);
    if (action instanceof IDisplayableAction) {
      attachActionGates((IDisplayableAction) action, actionHandler, view,
          remoteAction);
    }
    if (action instanceof IStylable) {
      remoteAction.setStyleName(((IStylable) action).getStyleName());
    }
    return remoteAction;
  }

  /**
   * {@inheritDoc}
   */
  @Override
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
    this.hardReferences = new HashSet<>();
  }

  private RAction createRAction(IAction action, Dimension dimension,
      IActionHandler actionHandler, IView<RComponent> view, Locale locale) {
    RAction remoteAction = new RAction(guidGenerator.generateGUID());
    if (action instanceof IDisplayableAction) {
      remoteAction.setName(((IDisplayableAction) action).getI18nName(
          actionHandler, locale));
      String i18nDescription = ((IDisplayableAction) action)
          .getI18nDescription(actionHandler, locale);
      i18nDescription = completeDescriptionWithLiveDebugUI(action, i18nDescription);
      if (i18nDescription != null && i18nDescription.length() > 0) {
        remoteAction.setDescription(i18nDescription + TOOLTIP_ELLIPSIS);
      }
      remoteAction.setIcon(getIconFactory().getIcon(
          ((IDisplayableAction) action).getIcon(), dimension));
      if (((IDisplayableAction) action).getMnemonicAsString() != null) {
        remoteAction.setMnemonicAsString(((IDisplayableAction) action)
            .getMnemonicAsString());
      }
    }
    ActionAdapter remoteActionAdapter = new ActionAdapter(remoteAction, action,
        actionHandler, view);
    String permId = action.getPermId();
    permId = remotePeerRegistry.registerPermId(permId, remoteAction.getGuid());
    remoteAction.setPermId(permId);
    if (view == null) {
      hardReferences.add(remoteActionAdapter);
    } else {
      view.getPeer().addReferencedAction(remoteActionAdapter);
    }
    remotePeerRegistry.register(remoteActionAdapter);
    return remoteAction;
  }

  private final class ActionAdapter extends RAction {

    private static final long serialVersionUID = -922942515333636161L;

    private final RAction           delegate;
    private final IAction           action;
    private final IActionHandler    actionHandler;
    private final IView<RComponent> view;

    public ActionAdapter(RAction remoteAction, IAction anAction,
        IActionHandler anActionHandler, IView<RComponent> view) {
      super(remoteAction.getGuid());
      this.delegate = remoteAction;
      this.action = anAction;
      this.actionHandler = anActionHandler;
      this.view = view;
    }

    // Always keep in sync the delegate state.
    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public Object getValue(String key) {
      return delegate.getValue(key);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void putValue(String key, Object value) {
      delegate.putValue(key, value);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public String getAcceleratorAsString() {
      return delegate.getAcceleratorAsString();
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public String getDescription() {
      return delegate.getDescription();
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public RIcon getIcon() {
      return delegate.getIcon();
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public String getMnemonicAsString() {
      return delegate.getMnemonicAsString();
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public String getName() {
      return delegate.getName();
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public boolean isEnabled() {
      return delegate.isEnabled();
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void setAcceleratorAsString(String acceleratorAsString) {
      delegate.setAcceleratorAsString(acceleratorAsString);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void setDescription(String description) {
      delegate.setDescription(description);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void setEnabled(boolean enabled) {
      delegate.setEnabled(enabled);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void setIcon(RIcon icon) {
      delegate.setIcon(icon);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void setMnemonicAsString(String mnemonicAsString) {
      delegate.setMnemonicAsString(mnemonicAsString);
    }

    /**
     * Forward to delegate.
     * {@inheritDoc}
     */
    @Override
    public void setName(String name) {
      delegate.setName(name);
    }

    /**
     * Triggers the action execution on the action handler.
     *
     * @param actionEvent
     *          the action event.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void actionPerformed(RActionEvent actionEvent,
        Map<String, Object> context) {
      if (actionHandler != null) {
        RComponent sourceComponent = null;
        IValueConnector viewConnector = null;
        if (view != null) {
          sourceComponent = view.getPeer();
          viewConnector = view.getConnector();
        }
        Map<String, Object> actionContext = new HashMap<>();
        if (context != null) {
          actionContext.putAll(context);
        }
        IValueConnector contextViewConnector;
        if (actionEvent.getViewStateGuid() != null) {
          contextViewConnector = (IValueConnector) remotePeerRegistry
              .getRegistered(actionEvent.getViewStateGuid());
        } else {
          contextViewConnector = viewConnector;
        }
        Map<String, Object> defaultActionContext = createActionContext(
            actionHandler, view, contextViewConnector,
            actionEvent.getActionCommand(), sourceComponent);
        actionContext.putAll(defaultActionContext);
        actionContext.put(ActionContextConstants.UI_ACTION, delegate);
        actionContext.put(ActionContextConstants.UI_EVENT, actionEvent);
        Map<String, Object> staticContext = (Map<String, Object>) getValue(IAction.STATIC_CONTEXT_KEY);
        if (staticContext != null) {
          actionContext.putAll(staticContext);
        }
        actionHandler.execute(action, actionContext);
      }
    }
  }
}
