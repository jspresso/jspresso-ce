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
package org.jspresso.framework.util.remote.registry;

import java.util.Map;

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.jspresso.framework.util.remote.IRemotePeer;

/**
 * The basic implementation of a remote peer registry. It is stored by a
 * reference map so that it is memory neutral.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
public class BasicRemotePeerRegistry implements IRemotePeerRegistry {

  private Map<String, IRemotePeer> backingStore;

  /**
   * Constructs a new <code>BasicRemotePeerRegistry</code> instance.
   */
  @SuppressWarnings("unchecked")
  public BasicRemotePeerRegistry() {
    backingStore = new ReferenceMap(AbstractReferenceMap.HARD,
        AbstractReferenceMap.WEAK, true);
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
  public void register(IRemotePeer remotePeer) {
    backingStore.put(remotePeer.getGuid(), remotePeer);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unregister(String guid) {
    backingStore.remove(guid);
  }

}
