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
package org.jspresso.framework.util.remote.registry {

import org.jspresso.framework.util.remote.IRemotePeer;

public class BasicRemotePeerRegistry implements IRemotePeerRegistry {

  private var _backingStore:Object;

  public function BasicRemotePeerRegistry() {
    _backingStore = {};
  }

  public function register(remotePeer:IRemotePeer):void {
    if (remotePeer && remotePeer.guid) {
      _backingStore[remotePeer.guid] = remotePeer;
    }
  }

  public function getRegistered(guid:String):IRemotePeer {
    return _backingStore[guid];
  }

  public function unregister(remotePeer:IRemotePeer):void {
    if (remotePeer && remotePeer.guid) {
      delete _backingStore[remotePeer.guid];
    }
  }

  public function isRegistered(guid:String):Boolean {
    return _backingStore.hasOwnProperty(guid);
  }
}
}
