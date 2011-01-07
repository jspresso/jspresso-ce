/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.remote.registry;

import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * This interface is implemented by remote peer registries.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IRemotePeerRegistry {

  /**
   * Clears the registry.
   */
  void clear();

  /**
   * Retrieves a registered remote peer.
   * 
   * @param guid
   *          the remote peer guid.
   * @return the registered remote peer or null if no remote peer with the given
   *         guid is registered.
   */
  IRemotePeer getRegistered(String guid);

  /**
   * Retrieves a registered remote peer using its automation id.
   * 
   * @param automationId
   *          the remote peer automationId.
   * @return the registered remote peer or null if no remote peer with the given
   *         automationId is registered.
   */
  IRemotePeer getRegisteredForAutomationId(String automationId);

  /**
   * Tests wether a remote peer is already registered.
   * 
   * @param guid
   *          the remote peer guid.
   * @return true if the remote peer is already registered.
   */
  boolean isRegistered(String guid);

  /**
   * Registers a remote peer.
   * 
   * @param remotePeer
   *          the remote peer to register.
   */
  void register(IRemotePeer remotePeer);

  /**
   * Generates and registers an automation Id.
   * 
   * @param automationSeed
   *          the automation seed to use.
   * @param guid
   *          the guid to associate the generated automation idto.
   * @return the generated automation id.
   */
  String registerAutomationId(String automationSeed, String guid);

  /**
   * Unregisters a remote peer.
   * 
   * @param guid
   *          the remote peer guid.
   */
  void unregister(String guid);

  /**
   * Adds a remote peer registry listener.
   * 
   * @param listener
   *          the listener to add.
   */
  void addRemotePeerRegistryListener(IRemotePeerRegistryListener listener);

  /**
   * Removes a remote peer registry listener.
   * 
   * @param listener
   *          the listener to remove.
   */
  void removeRemotePeerRegistryListener(IRemotePeerRegistryListener listener);
}
