/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.jspresso.framework.application.frontend.IFrontendController;
import org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteMessageCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteRestartCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteStartCommand;
import org.jspresso.framework.application.startup.AbstractFrontendStartup;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RIcon;
import org.jspresso.framework.qooxdoo.rpc.Remote;
import org.jspresso.framework.server.remote.RemotePeerRegistryServlet;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.IIconFactory;

/**
 * Default remote startup class.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class RemoteStartup extends
    AbstractFrontendStartup<RComponent, RIcon, RAction> implements
    IRemoteCommandHandler, Remote {

  private Locale   startupLocale;
  private TimeZone clientTimeZone;

  /**
   * Delegates to the frontend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List<RemoteCommand> handleCommands(List<RemoteCommand> commands) {
    if (!getFrontendController().isStarted()) {
      // we are on a brand new session instance.
      return Collections
          .singletonList((RemoteCommand) new RemoteRestartCommand());
    }
    try {
      return ((IRemoteCommandHandler) getFrontendController())
          .handleCommands(commands);
    } catch (Throwable ex) {
      getFrontendController().traceUnexpectedException(ex);
      return Collections.emptyList();
    }
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
   * Registers the controller in the http session.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void start() {
    if (System.getProperty("java.security.auth.login.config") == null) {
      System.setProperty("java.security.auth.login.config",
          ResourceProviderServlet.computeStaticUrl("conf/jaas.config"));
    }
    super.start();
    HttpSession session = HttpRequestHolder.getServletRequest().getSession();
    if (session != null) {
      session.setAttribute(RemotePeerRegistryServlet.PEER_REGISTRY,
          getFrontendController());
    }
  }

  /**
   * Starts the remote application passing it the client locale.
   * 
   * @param startupLanguage
   *          the client language.
   * @param clientKeysToTranslate
   *          the array of client keys to translate.
   * @param timeZoneOffset
   *          the client timeZone offset in milliseconds.
   * @return the commands to be executed by the client peer on startup.
   */
  public List<RemoteCommand> start(String startupLanguage,
      String[] clientKeysToTranslate, int timeZoneOffset) {
    Locale locale = new Locale(startupLanguage);
    if (getFrontendController().isStarted()) {
      IFrontendController<RComponent, RIcon, RAction> controller = getFrontendController();
      RemoteMessageCommand errorMessage = createErrorMessageCommand();
      errorMessage.setMessage(controller.getTranslation("session.dup",
          new Object[] {
            controller.getI18nName(controller, locale)
          }, locale));
      return Collections.singletonList((RemoteCommand) errorMessage);
    }
    setStartupLocale(locale);
    TimeZone serverTimeZone = TimeZone.getDefault();
    int currentOffset = serverTimeZone.getOffset(System.currentTimeMillis());
    TimeZone clientTz = null;
    if (currentOffset == timeZoneOffset) {
      clientTz = serverTimeZone;
    } else {
      String[] availableIds = TimeZone.getAvailableIDs(timeZoneOffset);
      if (availableIds != null && availableIds.length > 0) {
        for (int i = 0; i < availableIds.length && clientTz == null; i++) {
          TimeZone tz = TimeZone.getTimeZone(availableIds[i]);
          if (tz.useDaylightTime() == serverTimeZone.useDaylightTime()) {
            clientTz = tz;
          }
        }
        if (clientTz == null) {
          clientTz = TimeZone.getTimeZone(availableIds[0]);
        }
      } else {
        clientTz = TimeZone.getDefault();
      }
    }
    setClientTimeZone(clientTz);
    start();
    try {
      RemoteStartCommand startCommand = new RemoteStartCommand();
      startCommand.setKeysToTranslate(clientKeysToTranslate);
      return handleCommands(Collections
          .singletonList((RemoteCommand) startCommand));
    } catch (Throwable ex) {
      getFrontendController().traceUnexpectedException(ex);
      return Collections.emptyList();
    }
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

  /**
   * Sets the clientTimeZone.
   * 
   * @param clientTimeZone
   *          the clientTimeZone to set.
   */
  protected void setClientTimeZone(TimeZone clientTimeZone) {
    this.clientTimeZone = clientTimeZone;
  }

  /**
   * Gets the clientTimeZone.
   * 
   * @return the clientTimeZone.
   */
  @Override
  public TimeZone getClientTimeZone() {
    return clientTimeZone;
  }

  private RemoteMessageCommand createErrorMessageCommand() {
    IIconFactory<RIcon> iconFactory = getFrontendController().getViewFactory()
        .getIconFactory();
    RemoteMessageCommand messageCommand = new RemoteMessageCommand();
    messageCommand.setTitle(getFrontendController().getTranslation("error",
        getFrontendController().getLocale()));
    messageCommand.setTitleIcon(iconFactory.getErrorIcon(iconFactory
        .getTinyIconSize()));
    messageCommand.setMessageIcon(iconFactory.getErrorIcon(iconFactory
        .getLargeIconSize()));
    return messageCommand;
  }
}
