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
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param registerPeers {Boolean}
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    createComponent: function (remoteComponent, registerPeers) {
      if (!remoteComponent) {
        return new qx.ui.core.Widget();
      }
      if (registerPeers == null) {
        registerPeers = true;
      }

      /**
       * @type {qx.ui.core.Widget}
       */
      var component = this._createCustomComponent(remoteComponent);
      if (component == null) {
        if (remoteComponent instanceof org.jspresso.framework.gui.remote.RActionField) {
          component = this._createActionField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RActionComponent) {
          component = this._createActionComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RCheckBox) {
          component = this._createCheckBox(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RComboBox) {
          component = this._createComboBox(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RRadioBox) {
          component = this._createRadioBox(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RColorField) {
          component = this._createColorField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RContainer) {
          component = this._createContainer(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDateField) {
          component = this._createDateComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDurationField) {
          component = this._createDurationField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RImageComponent) {
          component = this._createImageComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RList) {
          component = this._createList(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
          component = this._createNumericComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.REmptyComponent) {
          component = this._createEmptyComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RSecurityComponent) {
          component = this._createSecurityComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RForm) {
          component = this._createForm(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTextComponent) {
          component = this._createTextComponent(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTimeField) {
          component = this._createTimeField(remoteComponent);
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTree) {
          component = this._createTree(remoteComponent);
        }
      }
      if (component == null) {
        component = this._createDefaultComponent();
      }
      remoteComponent.assignPeer(component);
      if (registerPeers) {
        this._getRemotePeerRegistry().register(remoteComponent);
      }
      return component;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    _createCustomComponent: function (remoteComponent) {
      return null;
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    _createCustomComponent: function (remoteComponent) {
      return null;
    },

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
        if(typeof component.setIcon == 'function') {
          component.setIcon(icon.getImageUrlSpec());
        }
        if(typeof component.setShow == 'function') {
          component.setShow("both");
        }
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
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _isMultiline: function (rComponent) {
      return rComponent instanceof org.jspresso.framework.gui.remote.RTable || rComponent
          instanceof org.jspresso.framework.gui.remote.RTextArea || rComponent
          instanceof org.jspresso.framework.gui.remote.RList || rComponent
          instanceof org.jspresso.framework.gui.remote.RHtmlArea;
    },

    /**
     * @return {qx.ui.core.Widget}
     * @param remoteTextComponent {org.jspresso.framework.gui.remote.RTextComponent}
     */
    _createTextComponent: function (remoteTextComponent) {
      var textComponent;
      if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RTextArea) {
        textComponent = this._createTextArea(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RHtmlArea) {
        textComponent = this._createHtmlArea(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RPasswordField) {
        textComponent = this._createPasswordField(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RTextField) {
        textComponent = this._createTextField(remoteTextComponent);
      } else if (remoteTextComponent instanceof org.jspresso.framework.gui.remote.RLabel) {
        textComponent = this._createLabel(remoteTextComponent);
      }
      return textComponent;
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteSecurityComponent {org.jspresso.framework.gui.remote.RSecurityComponent}
     */
    _createSecurityComponent: function (remoteSecurityComponent) {
      return _createDefaultComponent();
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteEmptyComponent {org.jspresso.framework.gui.remote.REmptyComponent}
     */
    _createEmptyComponent: function (remoteEmptyComponent) {
      return _createDefaultComponent();
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    _createDefaultComponent: function () {
      throw new Error("_createDefaultComponent is abstract.");
    },


    _modelToViewFieldConverter: function (modelValue, model) {
      if (modelValue == null) {
        return "";
      }
      return modelValue;
    },

    _viewToModelFieldConverter: function (viewValue, model) {
      if (viewValue == "") {
        return null;
      }
      return viewValue;
    },

    _readOnlyFieldConverter: function (writable, model) {
      return !writable;
    },

    /**
     * @param button{qx.ui.form.Button | qx.ui.mobile.form.Button | qx.ui.toolbar.Button | qx.ui.mobile.toolbar.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    _bindButton: function (button, remoteAction) {
      remoteAction.bind("enabled", button, "enabled");
      this._getRemotePeerRegistry().register(remoteAction);
      this.addButtonListener(button, function (event) {
        this._getActionHandler().execute(remoteAction);
      }, this);
    }





  }
});
