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
 * Mobile page including its own navigation bar.
 */
qx.Class.define("org.jspresso.framework.view.qx.mobile.StandaloneNavigationPage", {
  extend: qx.ui.mobile.page.NavigationPage,

  construct: function () {
    this.base(arguments);

    this.__navigationBar = this._createNavigationBar();
    if (this.__navigationBar) {
      this.add(this.__navigationBar, {flex: 1});
    }
    this.addListener("appear", this._update, this);
  },

  members: {
    /** @type {qx.ui.mobile.navigationbar.NavigationBar} */
    __navigationBar: null,

    /**
     * Updates the navigation bar.
     */
    _update: function () {
      this._setStyle("transitionDuration", this.getNavigationBarToggleDuration() + "s");

      this.__navigationBar.removeAll();

      if (this.basename) {
        this._setAttribute("data-target-page", this.basename.toLowerCase());
      }

      var leftContainer = this.getLeftContainer();
      if (leftContainer) {
        this.__navigationBar.add(leftContainer);
      }

      var title = this.getTitleWidget();
      if (title) {
        this.__navigationBar.add(title, {flex: 1});
      }

      var rightContainer = this.getRightContainer();
      if (rightContainer) {
        this.__navigationBar.add(rightContainer);
      }
    },

    /**
     * Creates the navigation bar.
     *
     * @return {qx.ui.mobile.navigationbar.NavigationBar} The created navigation bar
     */
    _createNavigationBar: function () {
      return new qx.ui.mobile.navigationbar.NavigationBar();
    }
  }
});
