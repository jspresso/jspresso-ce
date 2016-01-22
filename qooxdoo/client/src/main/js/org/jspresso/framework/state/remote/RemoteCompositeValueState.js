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

qx.Class.define("org.jspresso.framework.state.remote.RemoteCompositeValueState", {
  extend: org.jspresso.framework.state.remote.RemoteValueState,

  construct: function () {
    this.base(arguments);
  },

  statics: {

    /**
     * @param compositeState {org.jspresso.framework.state.remote.RemoteCompositeValueState}
     * @param nestedLevel {Integer}
     * @return {qx.data.Array}
     */
    flatten: function (compositeState, nestedLevel) {
      var targetArray = new qx.data.Array();
      targetArray.append(org.jspresso.framework.state.remote.RemoteCompositeValueState.__flattenNested(compositeState,
          nestedLevel, targetArray));
      return targetArray;
    },

    /**
     * @param compositeState {org.jspresso.framework.state.remote.RemoteCompositeValueState}
     * @param nestedLevel {Integer}
     * @param targetArray {qx.data.Array}
     * @return {qx.data.Array}
     */
    __flattenNested: function (compositeState, nestedLevel, targetArray) {
      var flat = new qx.data.Array();
      var children = compositeState.getChildren();
      var listener = function (evt) {
        var startIndex = -1;
        var levelToInvalidate = -1;
        var changedItemsCount = 0;
        for (var j = 0; j < targetArray.length; j++) {
          if (startIndex == -1) {
            if (targetArray.getItem(j)["state"] == compositeState) {
              startIndex = j;
              changedItemsCount = 1;
              levelToInvalidate = targetArray.getItem(j)["level"];
            }
          } else {
            if (targetArray.getItem(j)["level"] > levelToInvalidate) {
              var nestedState = targetArray.getItem(j)["state"];
              nestedState.getChildren().removeListenerById(targetArray.getItem(j)["listenerId"]);
              changedItemsCount++;
            } else {
              break;
            }
          }
        }
        qx.data.Array.prototype.splice.apply(targetArray, [startIndex, changedItemsCount
        ].concat(org.jspresso.framework.state.remote.RemoteCompositeValueState.__flattenNested(compositeState,
                levelToInvalidate, targetArray).toArray()));
      };

      var id = children.addListener("change", listener);

      flat.push({
        state: compositeState,
        level: nestedLevel,
        listenerId: id
      });

      if (children) {
        for (var i = 0; i < children.length; i++) {
          if (children.getItem(i) instanceof org.jspresso.framework.state.remote.RemoteCompositeValueState) {
            flat.append(org.jspresso.framework.state.remote.RemoteCompositeValueState.__flattenNested(children.getItem(i),
                    nestedLevel + 1, targetArray));
          }
        }
      }
      return flat;
    }
  },

  properties: {
    children: {
      check: "qx.data.Array",
      nullable: true,
      event: "changeChildren",
      apply: "__bindChildrenArray"
    },
    description: {
      check: "String",
      nullable: true,
      event: "changeDescription",
      apply: "_applyEventPropagation"
    },
    iconImageUrl: {
      check: "String",
      nullable: true,
      event: "changeIconImageUrl",
      apply: "_applyEventPropagation"
    },
    leadingIndex: {
      check: "Integer",
      nullable: true,
      event: "changeLeadingIndex"
    },
    selectedIndices: {
      check: "Array",
      nullable: true,
      event: "changeSelectedIndices"
    }
  },

  members: {
    __bindChildrenArray: function (value, old) {
      var children = this.getChildren();
      var child;
      if (children) {
        for (var i = 0; i < children.length; i++) {
          var child = children.getItem(i);
          if (value == null || !value.contains(child)) {
            child.setParent(null);
          }
        }
      }
      if (value == null) {
        this.setChildren(new qx.data.Array());
      } else {
        for (var i = 0; i < value.length; i++) {
          child = value.getItem(i);
          child.setParent(this);
        }
      }
      /*
       children = this.getChildren();
       children.addListener("changeBubble", function(event) {
       this._applyEventPropagation(value,  old, "children." + event.getData()["name"]);
       }, this);
       */
    },

    notifyChildrenChanged: function () {
      this.fireDataEvent("changeChildren", this.getChildren(), this.getChildren(), false);
    }
  }
});
