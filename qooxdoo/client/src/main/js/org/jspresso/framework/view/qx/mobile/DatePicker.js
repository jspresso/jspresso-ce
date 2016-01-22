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

qx.Class.define("org.jspresso.framework.view.qx.mobile.DatePicker", {
  extend: qx.ui.mobile.control.Picker,

  statics: {
  },

  construct: function (monthNames) {
    this.base(arguments);
    this.__monthNames = monthNames;
    this.__pickerDaySlotData = this._createDayPickerSlot(1, new Date().getFullYear());

    this.addSlot(this.__pickerDaySlotData);
    this.addSlot(this._createMonthPickerSlot());
    this.addSlot(this._createYearPickerSlot());
    this.addListener("changeSelection", function(e) {
      if (e.getData().slot > 0) {
        setTimeout(this._updatePickerDaySlot.bind(this), 100);
      }
    },this);
  },

  members: {

    __monthNames :null,
    __pickerDaySlotData : null,

    /**
     * Creates the picker slot data for days in month.
     * @param month {Integer} current month.
     * @param year {Integer} current year.
     */
    _createDayPickerSlot : function(month, year) {
      var daysInMonth = new Date(year, month + 1, 0).getDate();

      var slotData = [];
      for (var i = 1; i <= daysInMonth; i++) {
        slotData.push({title: org.jspresso.framework.util.format.StringUtils.lpad("" + i, "0", 2)});
      }
      return new qx.data.Array(slotData);
    },


    /**
     * Creates the picker slot data for month names, based on current locale settings.
     */
    _createMonthPickerSlot : function() {
      var names = this.__monthNames;
      var slotData = [];
      for (var i = 0; i < names.length; i++) {
        slotData.push({title: "" + names[i]});
      }
      return new qx.data.Array(slotData);
    },


    /**
     * Creates the picker slot data from 1950 till current year +50.
     */
    _createYearPickerSlot : function() {
      var slotData = [];
      for (var i = 1950; i < new Date().getFullYear() + 50 ; i++) {
        slotData.push({title: "" + i});
      }
      return new qx.data.Array(slotData);
    },

    getYearIndex: function(year) {
      return year - 1950;
    },

    /**
     * Updates the shown days in the picker slot.
     */
    _updatePickerDaySlot : function() {
      var dayIndex = this.getSelectedIndex(0);
      var monthIndex = this.getSelectedIndex(1);
      var yearIndex = this.getSelectedIndex(2);
      var slotData = this._createDayPickerSlot(monthIndex, new Date().getFullYear() - yearIndex);
      this.__pickerDaySlotData.removeAll();
      this.__pickerDaySlotData.append(slotData);

      this.setSelectedIndex(0, dayIndex, false);
    }
  }
});
