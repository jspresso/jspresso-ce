/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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

qx.Class.define("org.jspresso.framework.view.qx.mobile.ImageCanvas", {
  extend: qx.ui.mobile.container.Composite,

  statics: {
  },

  /**
   * @param submitUrl {String}
   * @param height {Integer}
   */
  construct: function (submitUrl, height) {
    this.base(arguments, new qx.ui.mobile.layout.VBox());
    this.__ratio = qx.core.Environment.get("device.pixelRatio");
    this.__submitUrl = submitUrl;
    this._initialize(height);
  },

  members: {
    __submitUrl: null,
    __canvasLeft: 0,
    __canvasTop: 0,
    __canvas: null,
    __lastPoint: null,
    __canvasWidth: 1000,
    __canvasHeight: 1000,
    __ratio: 1,


    _initialize: function (height) {

      this.__lastPoint = {};

      var clearButton = new qx.ui.mobile.navigationbar.Button("Clear");
      clearButton.addListener("tap", this.__clearCanvas, this);

      this.add(clearButton);

      var canvas = this.__canvas = new qx.ui.mobile.embed.Canvas();

      canvas.addListener("trackstart", this._onTrackStart, this);
      canvas.addListener("trackend", this._onTrackEnd, this);
      canvas.addListener("track", this._onTrack, this);

      canvas.addListener("touchstart", qx.bom.Event.preventDefault, this);

      canvas.setWidth(this._to(this.__canvasWidth));
      canvas.setHeight(this._to(this.__canvasHeight));
      qx.bom.element.Style.set(canvas.getContentElement(), "width", this.__canvasWidth + "px");
      qx.bom.element.Style.set(canvas.getContentElement(), "height", this.__canvasHeight + "px");

      var scroll = new qx.ui.mobile.container.Scroll();
      if (!height) {
        height = 300;
      }
      scroll._setStyle("height", height + "px");
      scroll.add(canvas);
      this.add(scroll, {flex: 1});

      this.__clearCanvas();
    },


    /**
     * Calculates the correct position in relation to the device pixel ratio.
     * @return {Number} the correct position.
     */
    _to: function (value) {
      return value * this.__ratio;
    },


    /**
     * Removes any drawings off the canvas.
     */
    __clearCanvas: function () {
      //this.__canvas.getContentElement().width = this.__canvas.getContentElement().width;
      var ctx = this.__canvas.getContext2d();
      ctx.clearRect(0, 0, this._to(this.__canvasWidth), this._to(this.__canvasHeight));
      ctx.fillStyle = "#ffffff";
      ctx.fillRect(0, 0, this._to(this.__canvasWidth), this._to(this.__canvasHeight));
      ctx.fill();
    },


    /**
     * Handles the <code>trackstart</code>  event on canvas.
     */
    _onTrackStart: function (evt) {
      this.__canvasLeft = qx.bom.element.Location.getLeft(this.__canvas.getContentElement(), "padding");
      this.__canvasTop = qx.bom.element.Location.getTop(this.__canvas.getContentElement(), "padding");

      this.__draw(evt);
    },


    /**
     * Handles the <code>track</code>  event on canvas.
     */
    _onTrack: function (evt) {
      this.__draw(evt);
      evt.preventDefault();
      evt.stopPropagation();
    },


    /**
     * Handles the <code>trackend</code> event on canvas.
     */
    _onTrackEnd: function (evt) {
      this.__lastPoint = {};
    },


    /**
     * Draws the line on canvas.
     */
    __draw: function (evt) {
      var ctx = this.__canvas.getContext2d();
      var lastPoint = this.__lastPoint[evt.getPointerId()];

      var pointerLeft = evt.getViewportLeft() - this.__canvasLeft;
      var pointerTop = evt.getViewportTop() - this.__canvasTop;

      var opacity = null;

      if (lastPoint) {
        ctx.beginPath();
        ctx.lineCap = 'round';
        ctx.moveTo(this._to(lastPoint.x), this._to(lastPoint.y));
        ctx.lineTo(this._to(pointerLeft), this._to(pointerTop));

        var deltaX = Math.abs(lastPoint.x - pointerLeft);
        var deltaY = Math.abs(lastPoint.y - pointerTop);

        var velocity = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        opacity = (100 - velocity) / 100;
        opacity = Math.round(opacity * Math.pow(10, 2)) / Math.pow(10, 2);

        if (!lastPoint.opacity) {
          lastPoint.opacity = 1;
        }
        if (opacity < 0.1) {
          opacity = 0.1;
        }

        // linear gradient from start to end of line
        var grad = ctx.createLinearGradient(lastPoint.x, lastPoint.y, pointerLeft, pointerTop);
        grad.addColorStop(0, 'rgba(61,114,201,' + lastPoint.opacity + ')');
        grad.addColorStop(1, 'rgba(61,114,201,' + opacity + ')');
        ctx.strokeStyle = grad;

        ctx.lineWidth = this._to(1.5);

        ctx.stroke();
      }

      this.__lastPoint[evt.getPointerId()] = {
        "x": pointerLeft,
        "y": pointerTop,
        "opacity": opacity
      };
    }
  }
});
