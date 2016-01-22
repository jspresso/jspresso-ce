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

qx.Class.define("org.jspresso.framework.util.object.ObjectUtil", {
  statics: {

    /**
     * Transforms a Qooxdoo object graph into a simple untyped JS object graph
     * ready to be sent to server-side. Only public properties are handled.
     * @param root {Object} the object graph containing Qooxdoo objects.
     * @return {Object} the corresponding untyped JS Object graph.
     */
    untypeObjectGraph: function (root) {
      var untypedRoot = null;
      var i;
      if (root != null) {
        if (root instanceof Array) {
          untypedRoot = [];
          for (i = 0; i < root.length; i++) {
            untypedRoot[i] = org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(root[i]);
          }
        } else if (root instanceof qx.core.Object) {
          untypedRoot = {};
          untypedRoot["class"] = (/** @type{qx.core.Object}*/ root).classname;
          if (qx.Class.implementsInterface(root, qx.data.IListData)) {
            untypedRoot["array"] = org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(
                (/** @type{qx.data.IListData}*/root).toArray());
          } else {
            var clazz = root.constructor;
            var properties = qx.Class.getProperties(clazz);
            for (i = 0; i < properties.length; i++) {
              var propertyName = properties[i];
              if (propertyName.charAt(0) != "_") {
                untypedRoot[propertyName] = org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(
                    (/** @type{qx.core.Object}*/ root).get(propertyName));
              }
            }
          }
        } else if (root instanceof Object) {
          if (root instanceof Date) {
            untypedRoot = root;
          } else {
            untypedRoot = {};
            for (var member in root) {
              //noinspection JSUnfilteredForInLoop
              untypedRoot[member] = org.jspresso.framework.util.object.ObjectUtil.untypeObjectGraph(root[member]);
            }
          }
        } else {
          untypedRoot = root;
        }
      }
      return untypedRoot;
    },

    /**
     * Transforms an untyped JS object graph into a Qooxdoo object graph.
     * All JS object members are considered public properties whenever the class
     * hint refers to a Qooxdoo object.
     * @param root {var} the object graph containing untyped JS Object.
     * @return {var} the corresponding object graph containing typed Qooxdoo objects.
     */
    typeObjectGraph: function (root) {
      var codec = {};
      var payload = root;
      if (root.hasOwnProperty("codec")) {
        codec = root["codec"];
        payload = root["payload"];
      }
      return org.jspresso.framework.util.object.ObjectUtil._typeAndDedupObjectGraph(payload, codec,
          new org.jspresso.framework.util.remote.registry.BasicRemotePeerRegistry());
    },

    _typeAndDedupObjectGraph: function (payload, codec, registry) {
      var typedRoot = null;
      if (payload != null) {
        if (payload instanceof Array) {
          typedRoot = [];
          var l = payload.length;
          for (var i = 0; i < l; i++) {
            typedRoot[i] = org.jspresso.framework.util.object.ObjectUtil._typeAndDedupObjectGraph(payload[i], codec,
                registry);
          }
        } else if (payload instanceof Object) {
          var decodedPayload = {};
          for (var member in payload) {
            var decodedKey = member;
            var decodedValue = payload[member];
            delete payload[member];
            if (codec.hasOwnProperty(member)) {
              decodedKey = codec[decodedKey];
              if (decodedKey == "class") {
                if (codec.hasOwnProperty(decodedValue)) {
                  decodedValue = codec[decodedValue];
                }
              }
            }
            decodedPayload[decodedKey] = decodedValue;
          }
          var className = decodedPayload["class"];
          delete decodedPayload["class"];
          if (className) {
            var typedClass = qx.Class.getByName(className);
            if (typedClass) {
              typedRoot = new typedClass();
              var guid = decodedPayload["guid"];
              if (guid && typedRoot instanceof org.jspresso.framework.util.remote.RemotePeer) {
                delete decodedPayload["guid"];
                if (registry.isRegistered(guid)) {
                  typedRoot = registry.getRegistered(guid);
                  return typedRoot;
                } else {
                  typedRoot.setGuid(guid);
                  registry.register(typedRoot);
                }
              }
              var array = decodedPayload["array"];
              if (array) {
                delete decodedPayload["array"];
                typedRoot.append(
                    org.jspresso.framework.util.object.ObjectUtil._typeAndDedupObjectGraph(array, codec, registry));
              } else {
                for (var propertyName in decodedPayload) {
                  var propertyValue = decodedPayload[propertyName];
                  delete decodedPayload[propertyName];
                  //noinspection JSUnfilteredForInLoop
                  typedRoot.set(propertyName,
                      org.jspresso.framework.util.object.ObjectUtil._typeAndDedupObjectGraph(propertyValue, codec,
                          registry));
                }
              }
            } else {
              throw Error(className + " cannot be de-serialized. Please include the class in meta.")
            }
          } else if (decodedPayload instanceof Date) {
            typedRoot = decodedPayload;
          } else {
            typedRoot = {};
            for (var member in decodedPayload) {
              //noinspection JSUnfilteredForInLoop
              typedRoot[member] = org.jspresso.framework.util.object.ObjectUtil._typeAndDedupObjectGraph(
                  decodedPayload[member], codec, registry);
            }
          }
        } else if (typeof payload === "string") {
          var iso8601regexp = "^([0-9]{4})-([0-9]{2})-([0-9]{2})" + "T([0-9]{2}):([0-9]{2})(:([0-9]{2})(\.([0-9]+))?)?"
              + "(Z|(([-+])([0-9]{2}):([0-9]{2})))?$";
          if (payload.match(iso8601regexp)) {
            typedRoot = new Date(payload);
          } else {
            typedRoot = payload
          }
        } else {
          typedRoot = payload;
        }
      }
      return typedRoot;
    }
  }
});
