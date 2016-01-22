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
package org.jspresso.framework.application.startup.remote;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.LocaleUtils;
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
import org.jspresso.framework.util.Build;
import org.jspresso.framework.util.http.HttpRequestHolder;
import org.jspresso.framework.util.http.RequestParamsHttpFilter;
import org.jspresso.framework.util.resources.server.ResourceProviderServlet;
import org.jspresso.framework.view.IIconFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default remote startup class.
 *
 * @author Vincent Vandenschrick
 */
public abstract class RemoteStartup extends
    AbstractFrontendStartup<RComponent, RIcon, RAction> implements
    IRemoteCommandHandler, Remote {

  private static final Logger LOG = LoggerFactory
                                      .getLogger(AbstractFrontendStartup.class);

  private Locale              startupLocale;
  private TimeZone            clientTimeZone;
  private boolean             dupSessionDetectionEnabled;
  private boolean             dupSessionNotifiedOnce;

  /**
   * Constructs a new {@code RemoteStartup} instance.
   */
  public RemoteStartup() {
    dupSessionDetectionEnabled = true;
    dupSessionNotifiedOnce = false;
  }

  /**
   * Delegates to the frontend controller.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public List<RemoteCommand> handleCommands(List<RemoteCommand> commands) {
    IFrontendController<RComponent, RIcon, RAction> controller = getFrontendController();
    if (controller == null || !controller.isStarted()) {
      // we are on a brand new session instance.
      return Collections
          .singletonList((RemoteCommand) new RemoteRestartCommand());
    }
    try {
      return ((IRemoteCommandHandler) controller).handleCommands(commands);
    } catch (Throwable ex) {
      controller.traceUnexpectedException(ex);
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
    if (System.getProperty("java.security.auth.login.config") == null
        && HttpRequestHolder.isAvailable()) {
      System.setProperty("java.security.auth.login.config",
          ResourceProviderServlet.computeStaticUrl("conf/jaas.config"));
    }
    super.start();
    if (HttpRequestHolder.isAvailable()) {
      HttpSession session = HttpRequestHolder.getServletRequest().getSession();
      if (session != null) {
        session.setAttribute(RemotePeerRegistryServlet.PEER_REGISTRY,
            getFrontendController());
        session.setAttribute(RemoteUtilServlet.REMOTE_STARTUP, this);
      }
    }
  }

  /**
   * Starts the remote application passing it the client locale.
   *
   * @param startCommand
   *          the start command wrapping the various client start parameters.
   * @return the commands to be executed by the client peer on startup.
   */
  public List<RemoteCommand> start(RemoteStartCommand startCommand) {
    try {
      Locale locale = LocaleUtils.toLocale(startCommand.getLanguage());
      IFrontendController<RComponent, RIcon, RAction> controller = getFrontendController();
      if (!dupSessionNotifiedOnce && isDupSessionDetectionEnabled()
          && controller != null && controller.isStarted()) {
        dupSessionNotifiedOnce = true;
        RemoteMessageCommand errorMessage = createErrorMessageCommand();
        errorMessage.setMessage(controller.getTranslation("session.dup",
            new Object[] {
              controller.getI18nName(controller, locale)
            }, locale));
        // Do not return the singleton list directly since subclasses might add commands to it.
        return new ArrayList<>(Collections.singleton((RemoteCommand) errorMessage));
      }
      dupSessionNotifiedOnce = false;
      setStartupLocale(locale);
      TimeZone serverTimeZone = TimeZone.getDefault();
      int currentOffset = serverTimeZone.getOffset(System.currentTimeMillis());
      TimeZone clientTz = null;
      if (currentOffset == startCommand.getTimezoneOffset()) {
        clientTz = serverTimeZone;
      } else {
        String[] availableIds = TimeZone.getAvailableIDs(startCommand
            .getTimezoneOffset());
        if (availableIds.length > 0) {
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
      controller = getFrontendController();
      if (startCommand.getVersion() != null
          && !isClientVersionCompatible(startCommand.getVersion())) {
        RemoteMessageCommand errorMessage = createErrorMessageCommand();
        assert controller != null;
        errorMessage.setMessage(controller.getTranslation(
            "incompatible.client.version", new Object[] {
                startCommand.getVersion(), Build.getJspressoVersion()
            }, locale));
        // Do not return the singleton list directly since subclasses might add commands to it.
        return new ArrayList<>(Collections.singleton((RemoteCommand) errorMessage));
      }
      try {
        return handleCommands(Collections
            .singletonList((RemoteCommand) startCommand));
      } catch (Throwable ex) {
        if (controller != null) {
          controller.traceUnexpectedException(ex);
        }
        return Collections.emptyList();
      }
    } catch (RuntimeException ex) {
      LOG.error("An unexpected error occurred while starting the server.", ex);
      RemoteMessageCommand errorMessage = createErrorMessageCommand();
      errorMessage
          .setMessage("An unexpected error occurred while starting the server. Please contact the application manager.");
      // Do not return the singleton list directly since subclasses might add commands to it.
      return new ArrayList<>(Collections.singleton((RemoteCommand) errorMessage));
    }
  }

  private boolean isClientVersionCompatible(String clientVersion) {
    return Boolean.parseBoolean(System.getProperty("skipJspressoVersionCheck"))
        || Build.getJspressoVersion() == null
        || Build.UNKNOWN.equals(Build.getJspressoVersion())
        || Build.getJspressoVersion().equals(clientVersion);
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

  /**
   * Gets the dupSessionDetectionEnabled.
   *
   * @return the dupSessionDetectionEnabled.
   */
  public boolean isDupSessionDetectionEnabled() {
    if (Boolean.TRUE.toString().equalsIgnoreCase(System.getProperty("skipDupSessionCheck"))) {
      return false;
    }
    boolean isPermalink = false;
    if (HttpRequestHolder.isAvailable()) {
      HttpSession session = HttpRequestHolder.getServletRequest().getSession();
      if (session != null
          && session.getAttribute(RequestParamsHttpFilter.REQUEST_PARAMS_KEY) != null) {
        isPermalink = true;
      }
    }
    return dupSessionDetectionEnabled && !isPermalink;
  }

  /**
   * Sets the dupSessionDetectionEnabled.
   *
   * @param dupSessionDetectionEnabled
   *          the dupSessionDetectionEnabled to set.
   */
  public void setDupSessionDetectionEnabled(boolean dupSessionDetectionEnabled) {
    this.dupSessionDetectionEnabled = dupSessionDetectionEnabled;
  }
}
