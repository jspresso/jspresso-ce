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
qx.Class.define("org.jspresso.framework.application.frontend.controller.qx.MobileQxController", {

  extend: org.jspresso.framework.application.frontend.controller.qx.AbstractQxController,

  implement: [
  ],

  statics: {
  },

  construct: function (application, remoteController, userLanguage) {
    this.base(arguments, application, remoteController, userLanguage);

    var busyIndicator = new qx.ui.mobile.dialog.BusyIndicator("Please wait...");
    this.__busyPopup = new qx.ui.mobile.dialog.Popup(busyIndicator);
    this.__busyPopup.setTitle("Loading...");

    this._manager = new qx.ui.mobile.page.Manager();
  },

  members: {
    /** @type {qx.ui.mobile.dialog.Popup} */
    __busyPopup : null,
    _manager    : null,


    _createViewFactory: function () {
      return new org.jspresso.framework.view.qx.MobileQxViewFactory(this, this, this);
    },

    _showBusy: function(busy) {
      if (busy) {
        this.__busyPopup.show();
      } else {
        this.__busyPopup.hide();
      }
    },

    /**
     *
     * @param title {String}
     * @param message {String}
     * @param dialogView {qx.ui.mobile.core.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param buttons {qx.ui.mobile.form.Button[]}
     * @param useCurrent {Boolean}
     * @param dimension {org.jspresso.framework.util.gui.Dimension}
     * @return {undefined}
     */
    _popupDialog: function (title, message, dialogView, icon, buttons, useCurrent, dimension) {
      useCurrent = (typeof useCurrent == 'undefined') ? false : useCurrent;

      var dialogPage = new qx.ui.mobile.page.NavigationPage();
      dialogPage.setTitle(title);

      var dialogMessage = new qx.ui.mobile.embed.Html(message);
      var group = new qx.ui.mobile.form.Group([dialogMessage, dialogView]);

      dialogPage.addListener("initialize", function (e) {
        var content = dialogPage.getContent();
        content.add(dialogMessage);
        content.add(dialogView);
        for (var i = 0; i < buttons.length; i++) {
          content.add(buttons[i]);
        }
      }, this);

      if (useCurrent && this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var currentDialogPage = this._dialogStack[this._dialogStack.length - 1][0];
        this._dialogStack.pop();
        currentDialogPage.exclude();
        currentDialogPage.destroy();
        this._manager.getDetailContainer().remove(currentDialogPage);
      }
      this._dialogStack.push([dialogPage, null, null]);
      this._manager.addDetail(dialogPage);
      dialogPage.show();
      this._viewFactory.focus(dialogPage);
    },

    /**
     * Close top most dialog.
     */
    _closeDialog: function () {
      if (this._dialogStack && this._dialogStack.length > 1) {
        /** @type {qx.ui.mobile.page.NavigationPage} */
        var topDialog = this._dialogStack.pop()[0];
        topDialog.exclude();
        topDialog.destroy();
      }
    }


  }
});
