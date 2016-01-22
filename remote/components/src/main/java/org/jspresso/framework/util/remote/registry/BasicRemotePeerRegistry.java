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
package org.jspresso.framework.util.remote.registry;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections4.map.AbstractReferenceMap;
import org.apache.commons.collections4.map.ReferenceMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jspresso.framework.util.automation.IPermIdSource;
import org.jspresso.framework.util.reflect.ReflectHelper;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The basic implementation of a remote peer registry. It is stored by a
 * reference map so that it is memory neutral.
 *
 * @author Vincent Vandenschrick
 */
public class BasicRemotePeerRegistry implements IRemotePeerRegistry {

  private static final Logger LOG = LoggerFactory.getLogger(BasicRemotePeerRegistry.class);

  private final Map<String, String>      automationBackingStore;
  private final Map<String, Integer>     automationIndices;
  private final Map<String, IRemotePeer> backingStore;

  private Set<IRemotePeerRegistryListener> rprListeners;

  private boolean automationEnabled;

  /**
   * Constructs a new {@code BasicRemotePeerRegistry} instance.
   */
  @SuppressWarnings("unchecked")
  public BasicRemotePeerRegistry() {
    backingStore = new RemotePeerReferenceMap(AbstractReferenceMap.ReferenceStrength.WEAK,
        AbstractReferenceMap.ReferenceStrength.WEAK, true);
    automationBackingStore = new ReferenceMap(AbstractReferenceMap.ReferenceStrength.WEAK,
        AbstractReferenceMap.ReferenceStrength.WEAK, true);
    automationIndices = new HashMap<>();
    setAutomationEnabled(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    LOG.debug("Clearing remote peer registry.");
    backingStore.clear();
    automationBackingStore.clear();
    automationIndices.clear();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRemotePeer getRegistered(String guid) {
    return backingStore.get(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public IRemotePeer getRegisteredForPermId(String permId) {
    if (permId != null) {
      return getRegistered(automationBackingStore.get(permId));
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isRegistered(String guid) {
    return backingStore.containsKey(guid);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void register(IRemotePeer remotePeer) {
    String guid = remotePeer.getGuid();
    LOG.trace("Registering {} with GUID {}.", remotePeer, guid);
    if (!backingStore.containsKey(guid)) {
      backingStore.put(guid, remotePeer);
    } else if (remotePeer != backingStore.get(guid)) {
      LOG.error(
          "The server is trying to register a remote peer ({}) having the same GUID as an existing registered one "
              + "({}).",
          remotePeer, backingStore.get(guid));
    }
    // if (remotePeer instanceof IPermIdSource) {
    // String permId = ((IPermIdSource) remotePeer).getPermId();
    // if (permId != null) {
    // automationBackingStore.put(permId, guid);
    // }
    // }
    fireRemotePeerAdded(remotePeer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String registerPermId(String automationsSeed, String guid) {
    if (automationEnabled) {
      String seed = automationsSeed;
      if (seed == null) {
        seed = "generic";
      }
      String permId = computeNextPermId(seed);
      LOG.trace("Associating GUID {} with permId {}.", guid, permId);
      automationBackingStore.put(permId, guid);
      return permId;
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String guid) {
    IRemotePeer remotePeer = backingStore.remove(guid);
    LOG.trace("Un-registering {} with GUID {}.", remotePeer, guid);
    if (remotePeer instanceof IPermIdSource) {
      String permId = ((IPermIdSource) remotePeer).getPermId();
      if (permId != null) {
        automationBackingStore.remove(permId);
      }
    }
    fireRemotePeerRemoved(guid);
  }

  private synchronized String computeNextPermId(String seed) {
    if (seed == null) {
      return null;
    }
    Integer currentIndex = automationIndices.get(seed);
    int idIndex = 0;
    if (currentIndex != null) {
      idIndex = currentIndex + 1;
    }
    automationIndices.put(seed, idIndex);
    if (idIndex > 0) {
      return seed + "#" + idIndex;
    }
    return seed;
  }

  private class RemotePeerReferenceMap<K, V> extends ReferenceMap<K,V> {

    private static final long serialVersionUID = 1494465151770293403L;

    private transient ReferenceQueue<IRemotePeer> remotePeerQueue;

    public RemotePeerReferenceMap(AbstractReferenceMap.ReferenceStrength keyType,
                                  AbstractReferenceMap.ReferenceStrength valueType, boolean purgeValues) {
      super(keyType, valueType, purgeValues);
    }

    @Override
    protected void init() {
      remotePeerQueue = new ReferenceQueue<>();
      super.init();
    }

    @Override
    public void clear() {
      //noinspection StatementWithEmptyBody
      while (remotePeerQueue.poll() != null) {
        // drain the queue.
      }
      super.clear();
    }

    @Override
    protected void purge() {
      Reference<? extends IRemotePeer> ref = remotePeerQueue.poll();
      while (ref != null) {
        purge(ref);
        ref = remotePeerQueue.poll();
      }
      super.purge();
    }

    @Override
    protected AbstractReferenceMap.ReferenceEntry<K, V> createEntry(HashEntry<K, V> next, int hashCode, K key, V value) {
      if (value instanceof IRemotePeer) {
        return new RemotePeerReferenceEntry(this, next, hashCode, key, value);
      }
      return super.createEntry(next, hashCode, key, value);
    }

    private class RemotePeerReferenceEntry extends ReferenceEntry<K, V> {

      public RemotePeerReferenceEntry(RemotePeerReferenceMap<K, V> parent, HashEntry<K, V> next, int hashCode, K key,
                                      V value) {
        super(parent, next, hashCode, key, value);
      }

      @Override
      protected <T> Object toReference(AbstractReferenceMap.ReferenceStrength type, T referent, int hash) {
        if (referent instanceof IRemotePeer) {
          RemotePeerReferenceMap<?,?> parent;
          try {
            parent = (RemotePeerReferenceMap) ReflectHelper.getPrivateFieldValue(
                AbstractReferenceMap.ReferenceEntry.class, "parent", this);
          } catch (Exception e) {
            throw new RuntimeException("An unexpected runtime exception occurred", e);
          }
          switch (type) {
            case SOFT:
              return new RemotePeerSoftRef(hash, (IRemotePeer) referent, parent.remotePeerQueue);
            case WEAK:
              return new RemotePeerWeakRef(hash, (IRemotePeer) referent, parent.remotePeerQueue);
            default:
              break;
          }
        }
        return super.toReference(type, referent, hash);
      }
    }
  }

  private class RemotePeerSoftRef extends SoftReference<IRemotePeer> implements IRemotePeer {

    private final int    hash;
    private final String guid;

    public RemotePeerSoftRef(int hash, IRemotePeer r, ReferenceQueue<IRemotePeer> q) {
      super(r, q);
      this.hash = hash;
      this.guid = r.getGuid();
    }

    @Override
    public int hashCode() {
      return hash;
    }

    @Override
    public String getGuid() {
      return guid;
    }

    @Override
    public void clear() {
      fireRemotePeerRemoved(guid);
      super.clear();
    }
  }

  private class RemotePeerWeakRef extends WeakReference<IRemotePeer> implements IRemotePeer {

    private final int    hash;
    private final String guid;

    public RemotePeerWeakRef(int hash, IRemotePeer r, ReferenceQueue<IRemotePeer> q) {
      super(r, q);
      this.hash = hash;
      this.guid = r.getGuid();
    }

    @Override
    public int hashCode() {
      return hash;
    }

    @Override
    public String getGuid() {
      return guid;
    }

    @Override
    public void clear() {
      fireRemotePeerRemoved(guid);
      super.clear();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addRemotePeerRegistryListener(IRemotePeerRegistryListener listener) {
    if (rprListeners == null && listener != null) {
      rprListeners = new LinkedHashSet<>();
    }
    if (rprListeners != null) {
      rprListeners.add(listener);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeRemotePeerRegistryListener(IRemotePeerRegistryListener listener) {
    if (rprListeners == null || listener == null) {
      return;
    }
    rprListeners.remove(listener);
  }

  /**
   * Notifies the listeners that a remote peer has been added.
   *
   * @param peer
   *     the added remote peer.
   */
  protected void fireRemotePeerAdded(IRemotePeer peer) {
    if (rprListeners != null) {
      for (IRemotePeerRegistryListener listener : rprListeners) {
        listener.remotePeerAdded(peer);
      }
    }
  }

  /**
   * Notifies the listeners that a remote peer has been removed.
   *
   * @param guid
   *     the removed remote peer guid.
   */
  protected void fireRemotePeerRemoved(String guid) {
    LOG.trace("Notifying listeners that GUID {} has been removed from the registry.", guid);
    if (rprListeners != null) {
      for (IRemotePeerRegistryListener listener : rprListeners) {
        listener.remotePeerRemoved(guid);
      }
    }
  }

  /**
   * Sets the automationEnabled.
   *
   * @param automationEnabled
   *     the automationEnabled to set.
   */
  public void setAutomationEnabled(boolean automationEnabled) {
    this.automationEnabled = automationEnabled;
  }
}
