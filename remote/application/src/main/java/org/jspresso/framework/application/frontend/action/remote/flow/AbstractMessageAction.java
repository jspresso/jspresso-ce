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
package org.jspresso.framework.application.frontend.action.remote.flow;

import java.util.Map;

import org.jspresso.framework.action.ActionContextConstants;
import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.WrappingAction;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Base class for all message remote actions. It just keeps a reference on the
 * message to be displayed.
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
public abstract class AbstractMessageAction extends AbstractRemoteAction {

  /**
   * Displays the message using a <code>JOptionPane.INFORMATION_MESSAGE</code>.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler,
      Map<String, Object> context) {
    RemoteMessageCommand messageCommand = createMessageCommand();
    completeMessageCommand(messageCommand, context, actionHandler,
        getSourceComponent(context), getModelDescriptor(context),
        getViewConnector(context));
    registerCommand(messageCommand, context);
    return super.execute(actionHandler, context);
  }

  /**
   * Gets the message.
   * 
   * @param context
   *          the actionContext.
   * @return the message.
   */
  protected String getMessage(Map<String, Object> context) {
    return (String) context.get(ActionContextConstants.ACTION_PARAM);
  }

  /**
   * Creates the message command to be handled by the client peer. It should be
   * created empty and will be automatically completed bay a call to
   * completeMessageCommand.
   * 
   * @return the messageCommand.
   */
  protected abstract RemoteMessageCommand createMessageCommand();

  /**
   * Completes the message command.
   * 
   * @param messageCommand
   *          the message command to complete informations on.
   * @param context
   *          the action context.
   * @param actionHandler
   *          the current actionHandler.
   * @param sourceComponent
   *          the source component.
   * @param modelDescriptor
   *          the model descriptor.
   * @param viewConnector
   *          the view connector.
   */
  protected void completeMessageCommand(RemoteMessageCommand messageCommand,
      Map<String, Object> context, IActionHandler actionHandler,
      RComponent sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector) {
    messageCommand.setTitle(getI18nName(getTranslationProvider(context),
        getLocale(context)));
    messageCommand.setMessage(getMessage(context));
  }

  /**
   * Wraps an action if necessary (if it's a backend action).
   * 
   * @param action
   *          the frontend or backend action.
   * @return the frontend action.
   */
  protected IDisplayableAction wrapAction(IAction action) {
    if (action instanceof IDisplayableAction) {
      return (IDisplayableAction) action;
    }
    WrappingAction<RComponent, RIcon, RAction> displayableAction = new WrappingAction<RComponent, RIcon, RAction>();
    displayableAction.setWrappedAction(action);
    return displayableAction;
  }
}
