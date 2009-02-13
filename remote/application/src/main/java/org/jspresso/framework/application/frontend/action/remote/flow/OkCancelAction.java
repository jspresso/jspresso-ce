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

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteOkCancelCommand;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.IIconFactory;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Action to ask a user validation.
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
public class OkCancelAction extends AbstractMessageAction {

  private IDisplayableAction cancelAction;
  private IDisplayableAction okAction;

  /**
   * {@inheritDoc}
   */
  @Override
  protected RemoteMessageCommand createMessageCommand() {
    return new RemoteOkCancelCommand();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void completeMessageCommand(RemoteMessageCommand messageCommand,
      Map<String, Object> context, IActionHandler actionHandler,
      RComponent sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector) {
    messageCommand.setTitleIcon(getIconFactory(context).getWarningIcon(
        IIconFactory.TINY_ICON_SIZE));
    if (getIconImageURL() != null) {
      messageCommand.setMessageIcon(getIconFactory(context).getIcon(
          getIconImageURL(), IIconFactory.LARGE_ICON_SIZE));
    } else {
      messageCommand.setMessageIcon(getIconFactory(context).getWarningIcon(
          IIconFactory.LARGE_ICON_SIZE));
    }
    if (okAction != null) {
      ((RemoteOkCancelCommand) messageCommand).setOkAction(getActionFactory(
          context).createAction(okAction, actionHandler, sourceComponent,
          modelDescriptor, viewConnector, getLocale(context)));
    }
    if (cancelAction != null) {
      ((RemoteOkCancelCommand) messageCommand)
          .setCancelAction(getActionFactory(context).createAction(cancelAction,
              actionHandler, sourceComponent, modelDescriptor, viewConnector,
              getLocale(context)));
    }
    super.completeMessageCommand(messageCommand, context, actionHandler,
        sourceComponent, modelDescriptor, viewConnector);
  }

  /**
   * Sets the cancelAction.
   * 
   * @param cancelAction
   *          the cancelAction to set.
   */
  public void setCancelAction(IAction cancelAction) {
    this.cancelAction = wrapAction(cancelAction);
  }

  /**
   * Sets the okAction.
   * 
   * @param okAction
   *          the okAction to set.
   */
  public void setOkAction(IAction okAction) {
    this.okAction = wrapAction(okAction);
  }

}
