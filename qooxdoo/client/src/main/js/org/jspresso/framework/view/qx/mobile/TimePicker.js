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
/**
 * Copyright (c) 2005-2013 Vincent Vandenschrick. All rights reserved.
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

qx.Class.define("org.jspresso.framework.view.qx.mobile.TimePicker", {
  extend: qx.ui.mobile.control.Picker,

  statics: {
  },

  construct: function (showSeconds, showMilliseconds) {
    this.base(arguments);
    this.addSlot(this._createHourPickerSlot());
    this.addSlot(this._createMinutePickerSlot());
    if (showSeconds) {
      this.addSlot(this._createSecondPickerSlot());
    }
    if (showMilliseconds) {
      this.addSlot(this._createMillisecondPickerSlot());
    }
  },

  members: {

    /**
     * Creates the picker slot data for hours.
     */
    _createHourPickerSlot : function() {
      var slotData = [];
      for (var i = 0; i < 24; i++) {
        slotData.push({title: org.jspresso.framework.util.format.StringUtils.lpad("" + i, "0", 2)});
      }
      return new qx.data.Array(slotData);
    },

    /**
     * Creates the picker slot data for minutes.
     */
    _createMinutePickerSlot : function() {
      var slotData = [];
      for (var i = 0; i < 59; i++) {
        slotData.push({title: org.jspresso.framework.util.format.StringUtils.lpad("" + i, "0", 2)});
      }
      return new qx.data.Array(slotData);
    },

    /**
     * Creates the picker slot data for seconds.
     */
    _createSecondPickerSlot: function () {
      var slotData = [];
      for (var i = 0; i < 59; i++) {
        slotData.push({title: org.jspresso.framework.util.format.StringUtils.lpad("" + i, "0", 2)});
      }
      return new qx.data.Array(slotData);
    },

    /**
     * Creates the picker slot data for milliseconds.
     */
    _createMillisecondPickerSlot: function () {
      var slotData = [];
      for (var i = 0; i < 999; i++) {
        slotData.push({title: org.jspresso.framework.util.format.StringUtils.lpad("" + i, "0", 3)});
      }
      return new qx.data.Array(slotData);
    }
  }
});
