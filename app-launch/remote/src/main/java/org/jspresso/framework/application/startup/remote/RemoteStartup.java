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
package org.jspresso.framework.application.startup.remote;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.swing.Action;

import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand;
import org.jspresso.framework.application.startup.AbstractStartup;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;

/**
 * Default remote startup class.
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
public abstract class RemoteStartup extends
    AbstractStartup<RComponent, RIcon, Action> implements IRemoteCommandHandler {

  private Locale startupLocale;

  /**
   * Delegates to the frontend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List<RemoteCommand> handleCommands(List<RemoteCommand> commands) {
    return ((IRemoteCommandHandler) getFrontendController())
        .handleCommands(commands);
  }

  /**
   * Delegates to the frontend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void registerCommand(RemoteCommand command) {
    ((IRemoteCommandHandler) getFrontendController()).registerCommand(command);
  }

  /**
   * Starts the remote application passing it the client locale.
   * 
   * @param startupLanguage
   *          the client language.
   * @return the commands to be executed by the client peer on startup.
   */
  public List<RemoteCommand> start(String startupLanguage) {
    setStartupLocale(new Locale(startupLanguage));
    super.start();
    return handleCommands(Collections
        .singletonList((RemoteCommand) new RemoteStartCommand()));
  }

  /**
   * Gets the startupLocale.
   * 
   * @return the startupLocale.
   */
  @Override
  protected Locale getStartupLocale() {
    return startupLocale;
  }

  /**
   * Sets the startupLocale.
   * 
   * @param startupLocale
   *          the startupLocale to set.
   */
  protected void setStartupLocale(Locale startupLocale) {
    this.startupLocale = startupLocale;
  }

}
