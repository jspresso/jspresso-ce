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

import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.model.descriptor.IModelDescriptor;

/**
 * Action to present a message to the user.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class InfoAction extends AbstractMessageAction {

  /**
   * {@inheritDoc}
   */
  @Override
  protected void completeMessageCommand(RemoteMessageCommand messageCommand,
      Map<String, Object> context, IActionHandler actionHandler,
      RComponent sourceComponent, IModelDescriptor modelDescriptor,
      IValueConnector viewConnector) {
    messageCommand.setTitleIcon(getIconFactory(context).getInfoIcon(
        getIconFactory(context).getTinyIconSize()));
    if (getIconImageURL() != null) {
      messageCommand.setMessageIcon(getIconFactory(context).getIcon(
          getIconImageURL(), getIconFactory(context).getLargeIconSize()));
    } else {
      messageCommand.setMessageIcon(getIconFactory(context).getInfoIcon(
          getIconFactory(context).getLargeIconSize()));
    }
    super.completeMessageCommand(messageCommand, context, actionHandler,
        sourceComponent, modelDescriptor, viewConnector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected RemoteMessageCommand createMessageCommand() {
    return new RemoteMessageCommand();
  }

}
