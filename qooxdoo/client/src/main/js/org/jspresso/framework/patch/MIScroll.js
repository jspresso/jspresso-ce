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
qx.Mixin.define("org.jspresso.framework.patch.MIScroll", {

  members: {
    /**
     * Returns a map with default iScroll properties for the iScroll instance.
     * @return {Object} Map with default iScroll properties
     */
    _getDefaultScrollProperties: function () {
      var container = this;

      return {
        hideScrollbar: true,
        fadeScrollbar: true,
        hScrollbar: false,
        scrollbarClass: "scrollbar",
        useTransform: true,
        useTransition: true,
        onScrollEnd: function () {
          // Alert interested parties that we scrolled to end of page.
          if (qx.core.Environment.get("qx.mobile.nativescroll") == false) {

            container._setCurrentX(-this.x);
            container._setCurrentY(-this.y);
            container.fireEvent("scrollEnd");
            if (this.y == this.maxScrollY) {
              container.fireEvent("pageEnd");
            }
          }
        },
        onScrollMove: function (e) {
          //Patch to allow jqPlot to react on iOS
          if (e.target.className == 'jqplot-event-canvas') {
            e.preventDefault()
          }
          // Alert interested parties that we scrolled to end of page.
          if (qx.core.Environment.get("qx.mobile.nativescroll") == false) {

            container._setCurrentX(-this.x);
            container._setCurrentY(-this.y);
            if (this.y == this.maxScrollY) {
              container.fireEvent("pageEnd");
            }
          }
        },
        onBeforeScrollStart: function (e) {
          // QOOXDOO ENHANCEMENT: Do not prevent default for form elements
          /* When updating iScroll, please check out that doubleTapTimer is not active (commented out)
           * in code. DoubleTapTimer creates a fake click event. Android 4.1. and newer
           * is able to fire native events, which  create side effect with the fake event of iScroll. */
          var target = e.target;
          while (target.nodeType != 1) {
            target = target.parentNode;
          }

          if (target.tagName != 'SELECT' && target.tagName != 'INPUT' && target.tagName != 'TEXTAREA' && target.tagName
              != 'LABEL'
            //Patch to allow hyperlinks to react on iOS
              && target.tagName != 'A'
            //Patch to allow jqPlot to react on iOS
              && target.className != 'jqplot-event-canvas') {
            // Remove focus from input elements, so that the keyboard and the mouse cursor is hidden
            var elements = [];
            var inputElements = qx.lang.Array.cast(document.getElementsByTagName("input"), Array);
            var textAreaElements = qx.lang.Array.cast(document.getElementsByTagName("textarea"), Array);
            elements = elements.concat(inputElements);
            elements = elements.concat(textAreaElements);

            for (var i = 0, length = elements.length; i < length; i++) {
              elements[i].blur();
            }

            e.preventDefault();
          }
        }
      };
    }

  }
});
