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
qx.Mixin.define("org.jspresso.framework.patch.MTouchCore", {
  members: {

    /**
     * Return the target of the event.
     *
     * @param domEvent {Event} DOM event
     * @return {Element} Event target
     */
    _getTarget: function (domEvent) {
      var target = qx.bom.Event.getTarget(domEvent);

      // Text node. Fix Safari Bug, see http://www.quirksmode.org/js/events_properties.html
      if (qx.core.Environment.get("engine.name") == "webkit") {
        if (target && target.nodeType == 3) {
          target = target.parentNode;
        }
      } else if (qx.core.Environment.get("event.mspointer")
          // For Firefox
          && qx.core.Environment.get("engine.name") != "gecko") {
        // Fix for IE10 and pointer-events:none
        var targetForIE = this.__evaluateTargetForIE(domEvent);
        if (targetForIE) {
          target = targetForIE;
        }
      }

      return target;
    },

    __evaluateTargetForIE: function (domEvent) {
      var clientX = null;
      var clientY = null;
      if (domEvent && domEvent.touches && domEvent.touches.length !== 0) {
        clientX = domEvent.touches[0].clientX;
        clientY = domEvent.touches[0].clientY;
      }

      // Retrieve an array with elements on point X/Y.
      var hitTargets = document.msElementsFromPoint(clientX, clientY);
      if (hitTargets) {
        // Traverse this array for the elements which has no pointer-events:none inside.
        for (var i = 0; i < hitTargets.length; i++) {
          var currentTarget = hitTargets[i];
          var pointerEvents = qx.bom.element.Style.get(currentTarget, "pointer-events", 3);

          if (pointerEvents != "none") {
            return currentTarget;
          }
        }
      }

      return null;
    }
  }
});
