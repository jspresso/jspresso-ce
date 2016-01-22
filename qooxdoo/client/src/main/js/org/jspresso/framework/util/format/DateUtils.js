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

qx.Class.define("org.jspresso.framework.util.format.DateUtils", {
  statics: {

    /**
     * @param source {org.jspresso.framework.util.lang.DateDto}
     * @return {Date}
     */
    fromDateDto: function (source) {
      if (source) {
        return new Date(source.getYear(), source.getMonth(), source.getDate(), source.getHour(), source.getMinute(),
            source.getSecond(), source.getMillisecond());
      }
      return null;
    },

    /**
     * @param source {Date}
     * @return {org.jspresso.framework.util.lang.DateDto}
     */
    fromDate: function (source) {
      if (source) {
        var dateDto = new org.jspresso.framework.util.lang.DateDto();
        dateDto.setYear(source.getFullYear());
        dateDto.setMonth(source.getMonth());
        dateDto.setDate(source.getDate());
        dateDto.setHour(source.getHours());
        dateDto.setMinute(source.getMinutes());
        dateDto.setSecond(source.getSeconds());
        dateDto.setMillisecond(source.getMilliseconds());
        return dateDto;
      }
      return null;
    }
  }
});
