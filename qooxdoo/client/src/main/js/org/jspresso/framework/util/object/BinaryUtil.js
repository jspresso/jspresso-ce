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

qx.Class.define("org.jspresso.framework.util.object.BinaryUtil", {
  statics: {
    // This code was written by Tyler Akins and has been placed in the
    // public domain.  It would be nice if you left this header intact.
    // Base64 code from Tyler Akins -- http://rumkin.com
    __keyStr: "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=",

    /**
     * Encodes a byte array in base 64 string.
     * @return {string} the base64 encoded string.
     * @param input
     */
    encode64: function (input) {
      var output = "";
      var chr1, chr2, chr3;
      var enc1, enc2, enc3, enc4;
      var i = 0;

      do {
        chr1 = input[i++];
        chr2 = input[i++];
        chr3 = input[i++];

        enc1 = chr1 >> 2;
        enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
        enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
        enc4 = chr3 & 63;

        if (isNaN(chr2)) {
          enc3 = enc4 = 64;
        } else if (isNaN(chr3)) {
          enc4 = 64;
        }

        output = output + org.jspresso.framework.util.object.BinaryUtil.__keyStr.charAt(enc1)
            + org.jspresso.framework.util.object.BinaryUtil.__keyStr.charAt(enc2)
            + org.jspresso.framework.util.object.BinaryUtil.__keyStr.charAt(enc3)
            + org.jspresso.framework.util.object.BinaryUtil.__keyStr.charAt(enc4);
      } while (i < input.length);
      return output;
    },

    /**
     * Decodes a base 64 string in byte array.
     * @return {Array} The byte array.
     * @param input
     */
    decode64: function (input) {
      var output = [];
      var chr1, chr2, chr3;
      var enc1, enc2, enc3, enc4;
      var i = 0;

      // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
      input = input.replace(/[^A-Za-z0-9\+\/=]/g, "");

      do {
        enc1 = org.jspresso.framework.util.object.BinaryUtil.__keyStr.indexOf(input.charAt(i++));
        enc2 = org.jspresso.framework.util.object.BinaryUtil.__keyStr.indexOf(input.charAt(i++));
        enc3 = org.jspresso.framework.util.object.BinaryUtil.__keyStr.indexOf(input.charAt(i++));
        enc4 = org.jspresso.framework.util.object.BinaryUtil.__keyStr.indexOf(input.charAt(i++));

        chr1 = (enc1 << 2) | (enc2 >> 4);
        chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
        chr3 = ((enc3 & 3) << 6) | enc4;

        output.push(chr1);

        if (enc3 != 64) {
          output.push(chr2);
        }
        if (enc4 != 64) {
          output.push(chr3);
        }
      } while (i < input.length);

      return output;
    }
  }
});
