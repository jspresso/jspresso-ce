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
 *
 * @asset(qx/icon/Oxygen/22/actions/dialog-ok.png)
 * @asset(qx/icon/Oxygen/22/actions/dialog-close.png)
 * @asset(qx/icon/Oxygen/22/actions/dialog-cancel.png)
 *
 */

qx.Class.define("org.jspresso.framework.view.qx.AbstractQxViewFactory", {
  extend: qx.core.Object,

  type: "abstract",

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.__remotePeerRegistry = remotePeerRegistry;
    this.__actionHandler = actionHandler;
    this.__commandHandler = commandHandler;
  },

  members: {

    /** @type {org.jspresso.framework.util.remote.registry.IRemotePeerRegistry} */
    __remotePeerRegistry: null,
    /** @type {org.jspresso.framework.action.IActionHandler} */
    __actionHandler: null,
    /** @type {org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler} */
    __commandHandler: null,

    /** @type {String} */
    __datePattern: null,

    /**
     * @return {org.jspresso.framework.util.remote.registry.IRemotePeerRegistry}
     */
    _getRemotePeerRegistry: function () {
      return this.__remotePeerRegistry;
    },

    /**
     * @return {org.jspresso.framework.action.IActionHandler}
     */
    _getActionHandler: function () {
      return this.__actionHandler;
    },

    /**
     * @return {org.jspresso.framework.application.frontend.command.remote.IRemoteCommandHandler}
     */
    _getCommandHandler: function () {
      return this.__commandHandler;
    },

    /**
     * @param datePattern {String}
     * @return {undefined}
     */
    setDatePattern: function (datePattern) {
      this.__datePattern = datePattern;
    },

    /**
     * @return {String}
     */
    _getDatePattern: function () {
      return this.__datePattern;
    },

    /**
     * @param component {qx.ui.core.Widget | qx.ui.core.mobile.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     */
    setIcon: function (component, icon) {
      if (icon) {
        component.setIcon(icon.getImageUrlSpec());
      }
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createOkButton: function () {
      var b = this.createButton(this.__actionHandler.translate("ok"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "qx/icon/Oxygen/22/actions/dialog-ok.png"
          }));
      return b;
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createCancelButton: function () {
      var b = this.createButton(this.__actionHandler.translate("cancel"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "qx/icon/Oxygen/22/actions/dialog-cancel.png"
          }));
      return b;
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createYesButton: function () {
      var b = this.createButton(this.__actionHandler.translate("yes"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "qx/icon/Oxygen/22/actions/dialog-ok.png"
          }));
      return b;
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createNoButton: function () {
      var b = this.createButton(this.__actionHandler.translate("no"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "qx/icon/Oxygen/22/actions/dialog-close.png"
          }));
      return b;
    }


  }
});
