/**
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

qx.Class.define("org.jspresso.framework.state.remote.RemoteCompositeValueState",
{
  extend : org.jspresso.framework.state.remote.RemoteValueState,
  
  construct : function() {
    this.base(arguments);
  },

  properties :
  {
    children :
    {
      check : "qx.data.Array",
      nullable : true,
      event : "changeChildren",
      apply : "__bindChildrenArray"
    },
    description :
    {
      check : "String",
      nullable : true,
      event : "changeDescription"
    },
    iconImageUrl :
    {
      check : "String",
      nullable : true,
      event : "changeIconImageUrl"
    },
    leadingIndex :
    {
      check : "Integer",
      nullable : true,
      event : "changeLeadingIndex"
    },
    selectedIndices :
    {
      check : "Array",
      nullable : true,
      event : "changeSelectedIndices"
    }
  },
  
  members :
  {
    __bindChildrenArray : function(value, old) {
      if(value == null) {
        this.setChildren(new qx.data.Array());
      } else {
        for(var i = 0; i < value.length; i++) {
          value.getItem(i).setParent(this);
        }
      }
    },
    
    notifyChildrenChanged : function() {
      this.fireDataEvent(
          "changeChildren",
          this.getChildren(),
          this.getChildren(),
          false
      );
    }
  }
});
