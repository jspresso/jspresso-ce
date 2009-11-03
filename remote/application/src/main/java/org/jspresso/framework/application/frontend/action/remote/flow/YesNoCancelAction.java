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
package org.jspresso.framework.application.frontend.action.remote.flow;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteYesNoCancelCommand;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;
import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Action to ask a binary question to the user with a cancel option.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class YesNoCancelAction extends YesNoAction {

  private IDisplayableAction cancelAction;

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
   * {@inheritDoc}
   */
  @Override
  protected void completeMessageCommand(RemoteMessageCommand messageCommand,
      Map<String, Object> context, IActionHandler actionHandler,
      RComponent sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector) {
    if (cancelAction != null) {
      ((RemoteYesNoCancelCommand) messageCommand)
          .setCancelAction(getActionFactory(context).createAction(cancelAction,
              actionHandler, sourceComponent, modelDescriptor, viewConnector,
              getLocale(context)));
    }
    super.completeMessageCommand(messageCommand, context, actionHandler,
        sourceComponent, modelDescriptor, viewConnector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RemoteMessageCommand createMessageCommand() {
    return new RemoteYesNoCancelCommand();
  }
}
