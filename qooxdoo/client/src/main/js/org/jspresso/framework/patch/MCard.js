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
qx.Mixin.define("org.jspresso.framework.patch.MCard", {

  /*
   *****************************************************************************
   CONSTRUCTOR
   *****************************************************************************
   */
  construct: function () {

    this.__patchCardAnimation = new qx.ui.mobile.layout.CardAnimation();
  },


  /*
   *****************************************************************************
   MEMBERS
   *****************************************************************************
   */

  members: {
    __patchNextWidget: null,
    __patchCurrentWidget: null,
    __patchInAnimation: null,
    __patchAnimation: null,
    __patchReverse: null,
    __patchCardAnimation: null,

    __lastAnimatedTo: null,
    __toExcludeManually: null,


    // overridden
    _getCssClasses: function () {
      return ["layout-card", "vbox"];
    },

    // overridden
    updateLayout: function (widget, action, properties) {
      if (action == "visible") {
        this._showWidget(widget, properties);
      }
      this.base(arguments, widget, action, properties);
    },


    /**
     * Setter for this.__patchCardAnimation.
     * @param value {qx.ui.mobile.layout.CardAnimation} the new CardAnimation object.
     */
    setCardAnimation: function (value) {
      this.__patchCardAnimation = value;
    },


    /**
     * Getter for this.__patchCardAnimation.
     * @return {qx.ui.mobile.layout.CardAnimation} the current CardAnimation object.
     */
    getCardAnimation: function () {
      return this.__patchCardAnimation;
    },


    /**
     * Shows the widget with the given properties.
     *
     * @param widget {qx.ui.mobile.core.Widget} The target widget
     * @param properties {Map} The layout properties to set. Key / value pairs.
     */
    _showWidget: function (widget, properties) {
      if (this.__patchNextWidget == widget) {
        return;
      }

      if (this.__patchInAnimation) {
        this.__patchStopAnimation();
      }

      this.__patchNextWidget = widget;
      if (this.__patchCurrentWidget && this.getShowAnimation() && qx.core.Environment.get("css.transform.3d")) {
        properties = properties || {};

        this.__patchAnimation = properties.animation || this.getDefaultAnimation();
        if (properties.action && properties.action === "back") {
          this.__patchReverse = true;
        } else {
          properties.reverse = properties.reverse === null ? false : properties.reverse;
          this.__patchReverse = properties.reverse;
        }

        qx.bom.AnimationFrame.request(function () {
          this.__patchStartAnimation(widget);
        }, this);
      } else {
        this._swapWidget();
      }
    },


    /**
     * Excludes the current widget and sets the next widget to the current widget.
     */
    _swapWidget: function () {
      if (this.__patchCurrentWidget) {
        var pageToExclude = this.__patchCurrentWidget;
        pageToExclude.removeCssClass("active");
        qx.event.Timer.once(function () {
          pageToExclude.exclude();
        }, this, 0);
      }
      this.__patchCurrentWidget = this.__patchNextWidget;
      this.__patchCurrentWidget.addCssClass("active");

      this.__lastAnimatedTo = null;
      this.__toExcludeManually = null;
    },


    /**
     * Fix size, only if widget has mixin MResize set,
     * and nextWidget is set.
     *
     * @param widget {qx.ui.mobile.core.Widget} The target widget which should have a fixed size.
     */
    _fixWidgetSize: function (widget) {
      if (widget) {
        var hasResizeMixin = qx.Class.hasMixin(widget.constructor, qx.ui.mobile.core.MResize);
        if (hasResizeMixin) {
          // Size has to be fixed for animation.
          widget.fixSize();
        }
      }
    },


    /**
     * Releases recently fixed widget size (width/height). This is needed for allowing further
     * flexbox layouting.
     *
     * @param widget {qx.ui.mobile.core.Widget} The target widget which should have a flexible size.
     */
    _releaseWidgetSize: function (widget) {
      if (widget) {
        var hasResizeMixin = qx.Class.hasMixin(widget.constructor, qx.ui.mobile.core.MResize);
        if (hasResizeMixin) {
          // Size has to be released after animation.
          widget.releaseFixedSize();
        }
      }
    },


    /**
     * Starts the animation for the page transition.
     *
     * @param widget {qx.ui.mobile.core.Widget} The target widget
     */
    __patchStartAnimation: function (widget) {
      if (widget.isDisposed()) {
        return;
      }
      if (this.__toExcludeManually) {
        this.__toExcludeManually.exclude();
      }
      this.__toExcludeManually = this.__lastAnimatedTo;
      this.__lastAnimatedTo = widget;

      // Fix size of current and next widget, then start animation.
      this.__patchInAnimation = true;

      this.fireDataEvent("animationStart", [this.__patchCurrentWidget, widget]);
      var fromElement = this.__patchCurrentWidget.getContainerElement();
      var toElement = widget.getContainerElement();

      var onAnimationEnd = qx.lang.Function.bind(this._onAnimationEnd, this);

      if (qx.core.Environment.get("browser.name") == "iemobile" || qx.core.Environment.get("browser.name") == "ie") {
        qx.bom.Event.addNativeListener(fromElement, "MSAnimationEnd", onAnimationEnd, false);
        qx.bom.Event.addNativeListener(toElement, "MSAnimationEnd", onAnimationEnd, false);
      } else {
        qx.event.Registration.addListener(fromElement, "animationEnd", this._onAnimationEnd, this);
        qx.event.Registration.addListener(toElement, "animationEnd", this._onAnimationEnd, this);
      }

      var fromCssClasses = this.__patchGetAnimationClasses("out");
      var toCssClasses = this.__patchGetAnimationClasses("in");

      this._widget.addCssClass("animationParent");

      var toElementAnimation = this.__patchCardAnimation.getAnimation(this.__patchAnimation, "in", this.__patchReverse);
      var fromElementAnimation = this.__patchCardAnimation.getAnimation(this.__patchAnimation, "out",
          this.__patchReverse);

      qx.bom.element.Class.addClasses(toElement, toCssClasses);
      qx.bom.element.Class.addClasses(fromElement, fromCssClasses);

      qx.bom.element.Animation.animate(toElement, toElementAnimation);
      qx.bom.element.Animation.animate(fromElement, fromElementAnimation);
    },


    /**
     * Event handler. Called when the animation of the page transition ends.
     *
     * @param evt {qx.event.type.Event} The causing event
     */
    _onAnimationEnd: function (evt) {
      this.__patchStopAnimation();
      this.fireDataEvent("animationEnd", [this.__patchCurrentWidget, this.__patchNextWidget]);
    },


    /**
     * Stops the animation for the page transition.
     */
    __patchStopAnimation: function () {
      if (this.__patchInAnimation) {
        var fromElement = this.__patchCurrentWidget.getContainerElement();
        var toElement = this.__patchNextWidget.getContainerElement();

        if (qx.core.Environment.get("browser.name") == "iemobile" || qx.core.Environment.get("browser.name") == "ie") {
          qx.bom.Event.removeNativeListener(fromElement, "MSAnimationEnd", this._onAnimationEnd, false);
          qx.bom.Event.removeNativeListener(toElement, "MSAnimationEnd", this._onAnimationEnd, false);
        } else {
          qx.event.Registration.removeListener(fromElement, "animationEnd", this._onAnimationEnd, this);
          qx.event.Registration.removeListener(toElement, "animationEnd", this._onAnimationEnd, this);
        }

        qx.bom.element.Class.removeClasses(fromElement, this.__patchGetAnimationClasses("out"));
        qx.bom.element.Class.removeClasses(toElement, this.__patchGetAnimationClasses("in"));

        this._swapWidget();
        this._widget.removeCssClass("animationParent");
        this.__patchInAnimation = false;
      }
    },


    /**
     * Returns the animation CSS classes for a given direction. The direction
     * can be <code>in</code> or <code>out</code>.
     *
     * @param direction {String} The direction of the animation. <code>in</code> or <code>out</code>.
     * @return {String[]} The CSS classes for the set animation.
     */
    __patchGetAnimationClasses: function (direction) {
      var classes = ["animationChild", this.__patchAnimation, direction];
      if (this.__patchReverse) {
        classes.push("reverse");
      }
      return classes;
    }
  }

});
