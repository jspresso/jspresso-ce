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
package org.jspresso.framework.util.remote.registry;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.jspresso.framework.util.automation.IAutomatable;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The basic implementation of a remote peer registry. It is stored by a
 * reference map so that it is memory neutral.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class BasicRemotePeerRegistry implements IRemotePeerRegistry {

  private Map<String, IRemotePeer> backingStore;
  private Map<String, String>      automationBackingStore;
  private Map<String, Integer>     automationIndices;

  /**
   * Constructs a new <code>BasicRemotePeerRegistry</code> instance.
   */
  @SuppressWarnings("unchecked")
  public BasicRemotePeerRegistry() {
    backingStore = new ReferenceMap(AbstractReferenceMap.HARD,
        AbstractReferenceMap.HARD, true);
    automationBackingStore = new HashMap<String, String>();
    automationIndices = new HashMap<String, Integer>();
  }

  /**
   * {@inheritDoc}
   */
  public void clear() {
    backingStore.clear();
    automationBackingStore.clear();
    automationIndices.clear();
  }

  /**
   * {@inheritDoc}
   */
  public IRemotePeer getRegistered(String guid) {
    return backingStore.get(guid);
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRegistered(String guid) {
    return backingStore.containsKey(guid);
  }

  /**
   * {@inheritDoc}
   */
  public void register(IRemotePeer remotePeer) {
    backingStore.put(remotePeer.getGuid(), remotePeer);
    if (remotePeer instanceof IAutomatable) {
      String automationId = ((IAutomatable) remotePeer).getAutomationId();
      if (automationId != null) {
        automationBackingStore.put(automationId, remotePeer.getGuid());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  public void unregister(String guid) {
    IRemotePeer remotePeer = backingStore.remove(guid);
    if (remotePeer instanceof IAutomatable) {
      String automationId = ((IAutomatable) remotePeer).getAutomationId();
      if (automationId != null) {
        automationBackingStore.remove(automationId);
      }
    }
  }

  private synchronized String computeNextAutomationId(String seed) {
    if (seed == null) {
      return null;
    }
    Integer currentIndex = automationIndices.get(seed);
    int idIndex = 0;
    if (currentIndex != null) {
      idIndex = currentIndex.intValue() + 1;
    }
    automationIndices.put(seed, new Integer(idIndex));
    return new StringBuffer(seed).append("#").append(idIndex).toString();
  }

  /**
   * {@inheritDoc}
   */
  public String registerAutomationId(String automationsSeed, String guid) {
    String seed = automationsSeed;
    if (seed == null) {
      seed = "generic";
    }
    String automationId = computeNextAutomationId(seed);
    automationBackingStore.put(automationId, guid);
    return automationId;
  }

  /**
   * {@inheritDoc}
   */
  public IRemotePeer getRegisteredForAutomationId(String automationId) {
    if (automationId != null) {
      return getRegistered(automationBackingStore.get(automationId));
    }
    return null;
  }

}
