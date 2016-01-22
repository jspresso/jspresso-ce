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

qx.Class.define("org.jspresso.framework.view.qx.mobile.EnhancedCarousel", {
  extend: qx.ui.mobile.container.Carousel,

  statics: {
  },

  construct: function (transitionDuration) {
    this.base(arguments, transitionDuration);
    this.__pagesCopy = [];
  },

  members: {
    __pagesCopy: null,

    add: function (page) {
      this.__pagesCopy.push(page);
      this.base(arguments, page);
    },

    removePageByIndex: function (pageIndex) {
      this.base(arguments, pageIndex);
      if (this.__pagesCopy && this.__pagesCopy.length > pageIndex) {
        this.__pagesCopy.splice(pageIndex, 1);
      }
    },

    _updateCarouselLayout: function () {
      this.base(arguments);
      if (this.getHeight() === null) {
        this._setStyle("height", "initial");
      }
      for (var i = 0; i < this.__pagesCopy.length; i++) {
        var pageContentElement = this.__pagesCopy[i].getContentElement();
        qx.bom.element.Style.set(pageContentElement, "height", "initial");
      }
    }
  },

  destruct: function () {
    this.__pagesCopy = null;
  }

});
