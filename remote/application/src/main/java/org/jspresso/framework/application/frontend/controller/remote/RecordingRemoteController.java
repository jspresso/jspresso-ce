/*
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.application.frontend.controller.remote;

import java.beans.XMLEncoder;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Locale;

import org.jspresso.framework.application.backend.IBackendController;
import org.jspresso.framework.application.frontend.command.remote.RemoteActionCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.application.frontend.command.remote.RemoteSortCommand;
import org.jspresso.framework.util.exception.NestedRuntimeException;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * A special remote controller capable of dumping incoming commands. The dump
 * file can then be re-used in tools like SOAP-UI to perform :
 * <ul>
 * <li>functional testing</li>
 * <li>load testing</li>
 * </ul>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RecordingRemoteController extends DefaultRemoteController {

  private XMLEncoder commandsEncoder;
  private String     commandsFileName;

  /**
   * Configures the file name where to append the incoming commands dump.
   * 
   * @param commandsFileName
   *          the commandsFileName to set.
   */
  public void setCommandsFileName(String commandsFileName) {
    this.commandsFileName = commandsFileName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean start(IBackendController peerController, Locale theClientLocale) {
    if (commandsEncoder == null && commandsFileName != null) {
      try {
        FileOutputStream os = new FileOutputStream(commandsFileName, true);
        commandsEncoder = new XMLEncoder(os);
      } catch (FileNotFoundException ex) {
        throw new NestedRuntimeException(ex);
      }
    }
    return super.start(peerController, theClientLocale);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean stop() {
    if (commandsEncoder != null) {
      commandsEncoder.close();
    }
    return super.stop();
  }

  /**
   * Records the incoming commands into a file dumping them in XML.
   * <p>
   * {@inheritDoc}
   */
  @Override
  protected void handleCommand(RemoteCommand command) {
    if (command.getTargetPeerGuid() != null) {
      IRemotePeer rPeer = getRegisteredForAutomationId(command
          .getAutomationId());
      if (rPeer == null || !command.getTargetPeerGuid().equals(rPeer.getGuid())) {
        System.err.println();
        System.err.println("####################################");
        System.err.println("############### AUTOMATION ERROR ###");
        System.err.println("####################################");
        System.err.println();
      }
    }

    super.handleCommand(command);

    if (commandsEncoder != null) {
      // perform some cleanup before dumping the command.
      command.setTargetPeerGuid(null);
      if (command instanceof RemoteActionCommand) {
        ((RemoteActionCommand) command).setViewStateGuid(null);
      } else if (command instanceof RemoteSortCommand) {
        ((RemoteSortCommand) command).setViewStateGuid(null);
      }
      try {
        commandsEncoder.writeObject(command);
      } catch (Exception ex) {
        handleException(ex, new HashMap<String, Object>());
      }
    }
  }
}
