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
qx.Class.define("org.jspresso.framework.patch.MEvent", {
  statics: {
    /**
     * Checks if MSPointer events are available.
     *
     * @internal
     * @return {Boolean} <code>true</code> if pointer events are supported.
     */
    getPatchedMsPointer: function () {
      // Fixes issue #9182: new unified pointer input model since Chrome 55
      // see https://github.com/qooxdoo/qooxdoo/issues/9182
      if ("PointerEvent" in window) {
        return true;
      }
      if ("pointerEnabled" in window.navigator) {
        return window.navigator.pointerEnabled;
      } else if ("msPointerEnabled" in window.navigator) {
        return window.navigator.msPointerEnabled;
      }
      return false;
    }
  },

  defer: function (statics) {
    qx.core.Environment.add("event.mspointer", statics.getPatchedMsPointer);
  }
});
