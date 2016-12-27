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

qx.Class.define("org.jspresso.framework.io.Rpc", {
  extend: qx.io.remote.Rpc,

  construct: function (url, serviceName) {
    this.base(arguments, url, serviceName);
  },

  properties: {
  },

  statics: {
    makeServerURL: function (instanceId) {
      var retVal = qx.io.remote.Rpc.makeServerURL(instanceId);
      retVal = retVal.replace(/;jsessionid=[^;]*/g, "");
      return retVal;
    }

  },

  members: {
    createRequest: function () {
      var request = this.base(arguments);
      request.setProhibitCaching("no-url-params-on-post");
      return request;
    }
  }
});
