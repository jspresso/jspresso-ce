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

qx.Class.define("org.jspresso.framework.util.format.ScaledNumberFormat", {
  extend: qx.util.format.NumberFormat,

  /**
   * @param locale {String} optional locale to be used
   */
  construct: function (locale) {
    this.base(arguments, locale);
    this.__savedLocale = locale;
  },

  properties: {
    scale: {
      check: "Number",
      nullable: true
    },
    decimalSeparator: {
      check: "String",
      nullable: true
    },
    thousandsSeparator: {
      check: "String",
      nullable: true
    }
  },

  members: {

    __savedLocale : null,

    /**
     * Formats a number.
     *
     * @param num {Number} the number to format.
     * @return {String} the formatted number as a string.
     */
    format: function (num) {
      var actualNumberToFormat = num;
      if (this.getScale() && num) {
        actualNumberToFormat = num * this.getScale();
      }
      /** @type {String } */
      var formatted = this.base(arguments, actualNumberToFormat);
      var defaultDecimalSeparator = qx.locale.Number.getDecimalSeparator(this.__savedLocale);
      var defaultGroupSeparator = qx.locale.Number.getGroupSeparator(this.__savedLocale);
      if (defaultDecimalSeparator) {
        formatted = formatted.replace(new RegExp("\\" + defaultDecimalSeparator,"g"), "#dec#");
      }
      if (defaultGroupSeparator) {
        formatted = formatted.replace(new RegExp("\\" + defaultGroupSeparator,"g"), this.getThousandsSeparator());
      }
      formatted = formatted.replace(/#dec#/g, this.getDecimalSeparator());
      return formatted;
    },


    /**
     * Parses a number.
     *
     * @param str {String} the string to parse.
     * @return {Double} the number.
     */
    parse: function (str) {
      /** @type {Double} */
      var parsed = /** @type {Double} */ this.base(arguments, str);
      if (this.getScale() && parsed) {
        parsed = parsed / this.getScale();
      }
      return parsed;
    }
  }
});
