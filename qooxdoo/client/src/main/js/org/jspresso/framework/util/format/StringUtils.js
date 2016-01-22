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

qx.Class.define("org.jspresso.framework.util.format.StringUtils", {
  statics: {

    /**
     * Trimming space from both side of the string.
     * @param source {String}
     * @return {String}
     */
    trim: function (source) {
      return source.replace(/^\s+|\s+$/g, "");
    },

    /**
     * Trimming space from left side of the string
     * @param source {String}
     * @return {String}
     */
    ltrim: function (source) {
      return source.replace(/^\s+/, "");
    },

    /**
     * Trimming space from right side of the string
     * @param source {String}
     * @return {String}
     */
    rtrim: function (source) {
      return source.replace(/\s+$/, "");
    },

    /**
     * Pads left
     * @param source {String}
     * @param padString {String}
     * @param length {int}
     * @return {String}
     */
    lpad: function (source, padString, length) {
      var str = source;
      while (str.length < length) {
        str = padString + str;
      }
      return str;
    },

    /**
     * Pads left
     * @param source {String}
     * @param padString {String}
     * @param length {int}
     * @return {String}
     */
    rpad: function (source, padString, length) {
      var str = this;
      while (str.length < length) {
        str = str + padString;
      }
      return str;
    }

  }
});
