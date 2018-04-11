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
 * @asset(org/openlayers/js/ol.js)
 * @asset(org/openlayers/css/ol.css)
 */
qx.Class.define("org.jspresso.framework.view.qx.AbstractQxViewFactory", {
  extend: qx.core.Object,

  type: "abstract",

  statics: {
    _TAP_THRESHOLD: 500,
    _INPUT_THRESHOLD: 400,
    _INPUT_TIMESTAMP: "inputTimestamp",

    /**
     * @param imageUrlSpec {String}
     * @return {String}
     */
    completeForSVG: function (imageUrlSpec) {
      if (imageUrlSpec && imageUrlSpec.indexOf("svg") < 0
          /*QX resource loading will break on parameters*/ && (imageUrlSpec.indexOf("http") == 0
              || imageUrlSpec.indexOf("file") == 0)) {
        if (imageUrlSpec.indexOf("?") >= 0) {
          return imageUrlSpec + "&preferSVG=true";
        } else {
          return imageUrlSpec + "?preferSVG=true";
        }
      }
      return imageUrlSpec;
    }
  },

  construct: function (remotePeerRegistry, actionHandler, commandHandler) {
    this.__remotePeerRegistry = remotePeerRegistry;
    this.__actionHandler = actionHandler;
    this.__commandHandler = commandHandler;
    this.__setupOpenLayers();
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
    /** @type {String} */
    __decimalSeparator: null,
    /** @type {String} */
    __thousandsSeparator: null,
    /** @type {Array} */
    __dateFormats: null,
    /** @type {Array} */
    __timeFormats: null,
    /** @type {Array} */
    __shortTimeFormats: null,
    /** @type {Array} */
    __longTimeFormats: null,
    /** @type {Array} */
    __dateTimeFormats: null,
    /** @type {Array} */
    __shortDateTimeFormats: null,
    /** @type {Array} */
    __longDateTimeFormats: null,
    /** @type {Integer} */
    __firstDayOfWeek: null,

    _loadScript: function (jsResource) {
      var scriptSrc = qx.util.ResourceManager.getInstance().toUri(jsResource);
      var req = new qx.bom.request.Script();
      req.open("GET", scriptSrc);
      req.send();
    },

    _loadCss: function (cssResource) {
      var head = document.getElementsByTagName("head")[0];
      var el = document.createElement("link");
      el.type = "text/css";
      el.rel = "stylesheet";
      el.href = qx.util.ResourceManager.getInstance().toUri(cssResource);
      setTimeout(function () {
        head.appendChild(el);
      }, 0);
    },

    __setupOpenLayers: function () {
      this._loadScript("org/openlayers/js/ol.js");
      this._loadCss("org/openlayers/css/ol.css");
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    _createEmptyWidget: function () {
      throw new Error("_createEmptyWidget is abstract.");
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param component {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    _bindVisibility: function (remoteComponent, component) {
      var state = remoteComponent.getState();
      if (state) {
        var modelController = new qx.data.controller.Object(state);
        modelController.addTarget(component, "visibility", "readable", false, {
          converter: this._visibleFieldConverter
        });
      }
    },

    /**
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @param registerPeers {Boolean}
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    createComponent: function (remoteComponent, registerPeers) {
      if (!remoteComponent) {
        return this._createEmptyWidget();
      }
      if (registerPeers == null) {
        registerPeers = true;
      }

      /**
       * @type {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
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
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RRepeater) {
          component = this._createRepeater(remoteComponent);
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
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RMap) {
          component = this._createMap(remoteComponent);
        }
      }
      if (component == null) {
        component = this._createDefaultComponent();
      }
      if (!remoteComponent.retrievePeer()) {
        remoteComponent.assignPeer(component);
      }
      if (registerPeers) {
        this._getRemotePeerRegistry().register(remoteComponent);
      }
      this.applyComponentStyle(component, remoteComponent);
      return component;
    },

    /**
     * @param component {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     * @return {undefined}
     */
    applyComponentStyle: function (component, remoteComponent) {
      throw new Error("applyComponentStyle is abstract.");
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     */
    _createDefaultComponent: function () {
      throw new Error("_createDefaultComponent is abstract.");
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
     * @param decimalSeparator {String}
     * @return {undefined}
     */
    setDecimalSeparator: function (decimalSeparator) {
      this.__decimalSeparator = decimalSeparator;
    },

    /**
     * @return {String}
     */
    _getDecimalSeparator: function () {
      return this.__decimalSeparator;
    },

    /**
     * @param thousandsSeparator {String}
     * @return {undefined}
     */
    setThousandsSeparator: function (thousandsSeparator) {
      this.__thousandsSeparator = thousandsSeparator;
    },

    /**
     * @return {String}
     */
    _getThousandsSeparator: function () {
      return this.__thousandsSeparator;
    },

    /**
     * @param component {qx.ui.core.Widget | qx.ui.core.mobile.Widget}
     * @param icon {org.jspresso.framework.gui.remote.RIcon}
     * @param preferSVG {Boolean}
     */
    setIcon: function (component, icon, preferSVG) {
      if (typeof preferSVG == 'undefined') {
        preferSVG = true;
      }
      if (icon) {
        if (typeof component.setIcon == 'function') {
          var imageUrlSpec = icon.getImageUrlSpec();
          if (preferSVG) {
            imageUrlSpec = org.jspresso.framework.view.qx.AbstractQxViewFactory.completeForSVG(imageUrlSpec);
          }
          component.setIcon(imageUrlSpec);
        }
      }
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createOkButton: function () {
      var b = this.createButton(this.__actionHandler.translate("ok"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "org/jspresso/framework/dialog-ok.svg"
          }));
      return b;
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createCancelButton: function () {
      var b = this.createButton(this.__actionHandler.translate("cancel"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "org/jspresso/framework/dialog-cancel.svg"
          }));
      return b;
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createYesButton: function () {
      var b = this.createButton(this.__actionHandler.translate("yes"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "org/jspresso/framework/dialog-ok.svg"
          }));
      return b;
    },

    /**
     * @return {qx.ui.form.Button | qx.ui.form.mobile.Button}
     */
    createNoButton: function () {
      var b = this.createButton(this.__actionHandler.translate("no"), null,
          new org.jspresso.framework.gui.remote.RIcon().set({
            imageUrlSpec: "org/jspresso/framework/dialog-close.svg"
          }));
      return b;
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    isMultiline: function (rComponent) {
      throw new Error("isMultiline is abstract.");
    },

    /**
     * @return {Boolean}
     * @param rComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    isFixedWidth: function (rComponent) {
      throw new Error("_sFixedWidth is abstract.");
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
      return this._createDefaultComponent();
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteEmptyComponent {org.jspresso.framework.gui.remote.REmptyComponent}
     */
    _createEmptyComponent: function (remoteEmptyComponent) {
      return this._createDefaultComponent();
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

    _visibleFieldConverter: function (readable, model) {
      return readable ? "visible" : "hidden";
    },

    addComponentThresholdListener: function (component, type, listener, that) {
      component.addListener(type, function (event) {
        var b = event.getCurrentTarget();
        var lastTimeStamp = b.getUserData("lastExecTimeStamp");
        var eventTimeStamp = new Date().getTime();
        if (lastTimeStamp == null || eventTimeStamp - lastTimeStamp
            > org.jspresso.framework.view.qx.AbstractQxViewFactory._TAP_THRESHOLD) {
          listener.call(that, event);
          b.setUserData("lastExecTimeStamp", eventTimeStamp);
        }
      }, that);
    },

    /**
     * @param button{qx.ui.form.Button | qx.ui.mobile.form.Button | qx.ui.toolbar.Button | qx.ui.mobile.toolbar.Button}
     * @param remoteAction {org.jspresso.framework.gui.remote.RAction}
     */
    _bindButton: function (button, remoteAction) {
      remoteAction.bind("enabled", button, "enabled");
      if (remoteAction.isHiddenWhenDisabled()) {
        button.setVisibility(remoteAction.isEnabled() ? "visible" : "excluded");
        remoteAction.addListener("changeEnabled", function (e) {
          if (e.getData()) {
            button.setVisibility("visible");
          } else {
            button.setVisibility("excluded");
          }
        }, this);
      }
      this._getRemotePeerRegistry().register(remoteAction);
      this.addButtonListener(button, function (event) {
        this._getActionHandler().execute(remoteAction);
      }, this);
    },

    _initDateAndTimeFormatsIfNecessary: function () {
      if (!this.__longDateTimeFormats) {
        this.__longDateTimeFormats = this._createDateFormats(this._createLongDateTimeFormatPatterns());
      }
      if (!this.__dateTimeFormats) {
        this.__dateTimeFormats = this._createDateFormats(this._createDateTimeFormatPatterns());
      }
      if (!this.__shortDateTimeFormats) {
        this.__shortDateTimeFormats = this._createDateFormats(this._createShortDateTimeFormatPatterns());
      }
      if (!this.__dateFormats) {
        this.__dateFormats = this._createDateFormats(this._createDateFormatPatterns());
      }
      if (!this.__longTimeFormats) {
        this.__longTimeFormats = this._createDateFormats(this._createLongTimeFormatPatterns());
      }
      if (!this.__timeFormats) {
        this.__timeFormats = this._createDateFormats(this._createTimeFormatPatterns());
      }
      if (!this.__shortTimeFormats) {
        this.__shortTimeFormats = this._createDateFormats(this._createShortTimeFormatPatterns());
      }
    },

    /**
     *
     * @return {qx.util.format.IFormat}
     * @param remoteComponent {org.jspresso.framework.gui.remote.RComponent}
     */
    _createFormat: function (remoteComponent) {
      var format;
      if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDateField) {
        this._initDateAndTimeFormatsIfNecessary();
        var formatDelegates = [];
        if (remoteComponent.getFormatPattern()) {
          formatDelegates.push(new org.jspresso.framework.util.format.DateFormat(remoteComponent.getFormatPattern()));
        }
        if (remoteComponent.getType() == "DATE_TIME") {
          var dateTimeFormat = new org.jspresso.framework.util.format.DateFormatDecorator();
          if (remoteComponent.getMillisecondsAware()) {
            formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
            // For a lenient parsing
            formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
            formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
            formatDelegates = formatDelegates.concat(this.__dateFormats);
            dateTimeFormat.setFormatDelegates(formatDelegates);
          } else if (remoteComponent.getSecondsAware()) {
            formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
            // For a lenient parsing
            formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
            formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
            formatDelegates = formatDelegates.concat(this.__dateFormats);
            dateTimeFormat.setFormatDelegates(formatDelegates);
          } else {
            formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
            // For a lenient parsing
            formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
            formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
            formatDelegates = formatDelegates.concat(this.__dateFormats);
            dateTimeFormat.setFormatDelegates(formatDelegates);
          }
          dateTimeFormat.setRemoteComponent(remoteComponent);
          return dateTimeFormat;
        } else {
          var dateFormat = new org.jspresso.framework.util.format.DateFormatDecorator();
          formatDelegates = formatDelegates.concat(this.__dateFormats);
          // For a lenient parsing
          formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
          dateFormat.setFormatDelegates(formatDelegates);
          dateFormat.setRemoteComponent(remoteComponent);
          return dateFormat;
        }
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RTimeField) {
        this._initDateAndTimeFormatsIfNecessary();
        var formatDelegates = [];
        if (remoteComponent.getFormatPattern()) {
          formatDelegates.push(new org.jspresso.framework.util.format.DateFormat(remoteComponent.getFormatPattern()));
        }
        var timeFormat = new org.jspresso.framework.util.format.DateFormatDecorator();
        if (remoteComponent.getMillisecondsAware()) {
          formatDelegates = formatDelegates.concat(this.__longTimeFormats);
          // For a lenient parsing
          formatDelegates = formatDelegates.concat(this.__timeFormats);
          formatDelegates = formatDelegates.concat(this.__shortTimeFormats);
          formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
        } else if (remoteComponent.getSecondsAware()) {
          formatDelegates = formatDelegates.concat(this.__timeFormats);
          // For a lenient parsing
          formatDelegates = formatDelegates.concat(this.__longTimeFormats);
          formatDelegates = formatDelegates.concat(this.__shortTimeFormats);
          formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
        } else {
          formatDelegates = formatDelegates.concat(this.__shortTimeFormats);
          // For a lenient parsing
          formatDelegates = formatDelegates.concat(this.__longTimeFormats);
          formatDelegates = formatDelegates.concat(this.__timeFormats);
          formatDelegates = formatDelegates.concat(this.__longDateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__dateTimeFormats);
          formatDelegates = formatDelegates.concat(this.__shortDateTimeFormats);
        }
        timeFormat.setFormatDelegates(formatDelegates);
        timeFormat.setRemoteComponent(remoteComponent);
        return timeFormat;
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RPasswordField) {
        return new org.jspresso.framework.util.format.PasswordFormat();
      } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RNumericComponent) {
        format = new org.jspresso.framework.util.format.ScaledNumberFormat(qx.locale.Manager.getInstance().getLocale());
        format.setThousandsSeparator(this._getThousandsSeparator());
        format.setDecimalSeparator(this._getDecimalSeparator());
        format.setGroupingUsed(remoteComponent.getThousandsGroupingUsed());
        if (remoteComponent instanceof org.jspresso.framework.gui.remote.RDecimalComponent) {
          var maxFractionDigit = remoteComponent.getMaxFractionDigit();
          if (remoteComponent instanceof org.jspresso.framework.gui.remote.RPercentField) {
            format.setScale(100);
            format.setPostfix(" %");
            if (maxFractionDigit) {
              if (maxFractionDigit >= 2) {
                maxFractionDigit -= 2;
              } else {
                maxFractionDigit = 0;
              }
            }
          }
          if (maxFractionDigit) {
            format.setMaximumFractionDigits(maxFractionDigit);
            format.setMinimumFractionDigits(maxFractionDigit);
          }
        } else if (remoteComponent instanceof org.jspresso.framework.gui.remote.RIntegerField) {
          format.setMaximumFractionDigits(0);
        }
      }
      return format;
    },

    _createTimeFormatPatterns: function () {
      var formatPatterns = [];
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH:mm:ss", "HH:mm:ss"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmmss", "HHmmss"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH:mm", "HH:mm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmm", "HHmm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH", "HH"));
      return formatPatterns;
    },

    _createShortTimeFormatPatterns: function () {
      var formatPatterns = [];
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH:mm", "HH:mm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmm", "HHmm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH", "HH"));
      return formatPatterns;
    },

    _createLongTimeFormatPatterns: function () {
      var formatPatterns = [];
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH:mm:ss.SSS", "HH:mm:ss.SSS"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmmssSSS", "HHmmssSSS"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH:mm:ss", "HH:mm:ss"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmmss", "HHmmss"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH:mm", "HH:mm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HHmm", "HHmm"));
      formatPatterns.push(qx.locale.Date.getDateTimeFormat("HH", "HH"));
      return formatPatterns;
    },

    _createDateFormatPatterns: function () {
      var formatPatterns = [];
      var defaultFormatPattern = this._getDatePattern();
      formatPatterns.push(defaultFormatPattern);
      formatPatterns.push(defaultFormatPattern.replace(/\//g, ""));
      for (var i = defaultFormatPattern.length; i > 0; i--) {
        var subPattern = defaultFormatPattern.substring(0, i);
        if (subPattern.charAt(subPattern.length - 1) == "/") {
          var truncated = subPattern.substring(0, subPattern.length - 1);
          formatPatterns.push(truncated);
          if (truncated.indexOf("/") >= 0) {
            formatPatterns.push(truncated.replace(/\//g, ""));
          }
        }
      }
      return formatPatterns;
    },

    _createDateTimeFormatPatterns: function () {
      var dateFormatPatterns = this._createDateFormatPatterns();
      var timeFormatPatterns = this._createTimeFormatPatterns();
      var formatPatterns = [];
      for (var i = 0; i < dateFormatPatterns.length; i++) {
        for (var j = 0; j < timeFormatPatterns.length; j++) {
          formatPatterns.push(dateFormatPatterns[i] + " " + timeFormatPatterns[j]);
        }
        formatPatterns.push(dateFormatPatterns[i]);
      }
      return formatPatterns;
    },

    _createLongDateTimeFormatPatterns: function () {
      var dateFormatPatterns = this._createDateFormatPatterns();
      var timeFormatPatterns = this._createLongTimeFormatPatterns();
      var formatPatterns = [];
      for (var i = 0; i < dateFormatPatterns.length; i++) {
        for (var j = 0; j < timeFormatPatterns.length; j++) {
          formatPatterns.push(dateFormatPatterns[i] + " " + timeFormatPatterns[j]);
        }
        formatPatterns.push(dateFormatPatterns[i]);
      }
      return formatPatterns;
    },

    _createShortDateTimeFormatPatterns: function () {
      var dateFormatPatterns = this._createDateFormatPatterns();
      var timeFormatPatterns = this._createShortTimeFormatPatterns();
      var formatPatterns = [];
      for (var i = 0; i < dateFormatPatterns.length; i++) {
        for (var j = 0; j < timeFormatPatterns.length; j++) {
          formatPatterns.push(dateFormatPatterns[i] + " " + timeFormatPatterns[j]);
        }
        formatPatterns.push(dateFormatPatterns[i]);
      }
      return formatPatterns;
    },

    _createDateFormats: function (formatPatterns) {
      var formats = [];
      for (var i = 0; i < formatPatterns.length; i++) {
        formats.push(new org.jspresso.framework.util.format.DateFormat(formatPatterns[i]));
      }
      return formats;
    },

    /**
     *
     * @return {undefined}
     * @param firstDayOfWeek {Integer}
     */
    setFirstDayOfWeek: function (firstDayOfWeek) {
      this.__firstDayOfWeek = firstDayOfWeek;
    },

    /**
     *
     * @return {Integer}
     */
    _getFirstDayOfWeek: function () {
      return this.__firstDayOfWeek;
    },

    _createDateComponent: function (remoteDateField) {
      var dateComponent;
      if (remoteDateField.getType() == "DATE_TIME") {
        dateComponent = this._createDateTimeField(remoteDateField);
      } else {
        dateComponent = this._createDateField(remoteDateField);
      }
      return dateComponent;
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteDecimalComponent {org.jspresso.framework.gui.remote.RDecimalComponent}
     */
    _createDecimalComponent: function (remoteDecimalComponent) {
      var decimalComponent;
      if (remoteDecimalComponent instanceof org.jspresso.framework.gui.remote.RDecimalField) {
        decimalComponent = this._createDecimalField(remoteDecimalComponent);
      } else if (remoteDecimalComponent instanceof org.jspresso.framework.gui.remote.RPercentField) {
        decimalComponent = this._createPercentField(remoteDecimalComponent);
      }
      return decimalComponent;
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteNumericComponent {org.jspresso.framework.gui.remote.RNumericComponent}
     */
    _createNumericComponent: function (remoteNumericComponent) {
      var numericComponent;
      if (remoteNumericComponent instanceof org.jspresso.framework.gui.remote.RDecimalComponent) {
        numericComponent = this._createDecimalComponent(remoteNumericComponent);
      } else if (remoteNumericComponent instanceof org.jspresso.framework.gui.remote.RIntegerField) {
        numericComponent = this._createIntegerField(remoteNumericComponent);
      }
      return numericComponent;
    },

    /**
     * @return {qx.ui.core.Widget | qx.ui.mobile.core.Widget}
     * @param remoteHtmlArea {org.jspresso.framework.gui.remote.RHtmlArea}
     */
    _createHtmlArea: function (remoteHtmlArea) {
      var htmlComponent;
      if (remoteHtmlArea.getReadOnly()) {
        htmlComponent = this._createHtmlText(remoteHtmlArea);
      } else {
        htmlComponent = this._createHtmlEditor(remoteHtmlArea);
      }
      return htmlComponent;
    },

    /**
     * @param actionLists {Array}
     * @return {Array}
     */
    extractAllActions: function (actionLists) {
      var allActions = [];
      if (actionLists && actionLists.length > 0) {
        for (var i = 0; i < actionLists.length; i++) {
          var actionList = actionLists[i];
          var actions = actionList.getActions();
          if (actions) {
            for (var j = 0; j < actions.length; j++) {
              allActions.push(actions[j]);
            }
          }
        }
      }
      return allActions;
    },

    addCard: function (cardContainer, rCardComponent, cardName) {
      throw new Error("_createEmptyWidget is abstract.");
    },


    addRepeated: function (repeater, newSections) {
      repeater.addRepeated(newSections);
    }

  }
});
