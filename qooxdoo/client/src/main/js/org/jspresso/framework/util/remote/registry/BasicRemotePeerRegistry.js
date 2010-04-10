/**
 * Copyright (c) 2005-2010 Vincent Vandenschrick. All rights reserved.
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
 */

qx.Class.define("org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry",
{
  extend : qx.core.Object,
  
  implement : [org.jspresso.framework.util.remote.registry.IRemotePeerRegistry],
  
  construct : function() {
    this.base(arguments);
  },

  members :
  {
    __backingStore : {},
    
    register : function(remotePeer){
      if(remotePeer) {
        this.__backingStore[remotePeer.getGuid()] = remotePeer;
      }
    },

    getRegistered : function(guid){
      return this.__backingStore[guid];
    },

    unregister : function(remotePeer){
      if(remotePeer) {
        delete this.__backingStore[remotePeer.getGuid()];
      }
    },

    isRegistered : function(guid){
      return this.__backingStore.hasOwnProperty(guid);
    }
    
  }
});
