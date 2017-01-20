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

/*
 The 'change' event on the input field requires that this handler be available:
 #use(qx.event.handler.Input)
 */


/**
 * An upload button which allows selection of a file through the browser fileselector.
 *
 */
qx.Class.define("org.jspresso.framework.view.qx.mobile.ImagePicker", {
  extend: qx.ui.mobile.form.Button,

  // --------------------------------------------------------------------------
  // [Constructor]
  // --------------------------------------------------------------------------

  /**
   * @param submitUrl {String}
   * @param label {String} button label
   * @param imageSize {org.jspresso.framework.util.gui.Dimension} image size
   * @param formatName {String} format name
   */

  construct: function (submitUrl, label, imageSize, formatName) {
    this.base(arguments, label);
    this.__imageSize = imageSize;
    this.__formatName = formatName;
    this.__formEl = this._createForm(submitUrl);
    this.addListenerOnce('appear', function () {
      this.getContentElement().appendChild(this.__formEl);
    }, this);
  },

  // --------------------------------------------------------------------------
  // [Events]
  // --------------------------------------------------------------------------

  events: {
    "imagePicked": "qx.event.type.Data"
  },

  // --------------------------------------------------------------------------
  // [Members]
  // --------------------------------------------------------------------------

  members: {

    __formEl: null,
    __imageSize: null,
    __formatName: null,

    // ------------------------------------------------------------------------
    // [Modifiers]
    // ------------------------------------------------------------------------

    /**
     * Apply the enabled property.
     * @param value {Boolean} Current value
     * @param old {Boolean} Previous value
     */
    _applyEnabled: function (value, old) {
      // just move it behind the button, do not actually
      // disable it since this would stop any upload in progress
      qx.bom.element.Style.set(this.__inputEl, 'zIndex', -10000);
      this.base(arguments, value, old);
    },

    /**
     * Create the widget child controls.
     */
    _createInput: function () {
      var input;
      // styling the input[type=file]
      // element is a bit tricky. Some browsers just ignore the normal
      // css style input. Firefox is especially tricky in this regard.
      // since we are providing our one look via the underlying qooxdoo
      // button anyway, all we have to do is position the ff upload
      // button over the button element. This is tricky in itself
      // as the ff upload button consists of a text and a button element
      // which are not css accessible themselves. So the best we can do,
      // is align to the top right corner of the upload widget and set its
      // font so large that it will cover even really large underlying buttons.
      var css = {
        //position: "absolute",
        //cursor: "pointer",
        hideFocus: "true",
        opacity: 0,
        top: '0px',
        right: '0px',
        width: '100%',
        height: '100%'
        /*,
         // ff ignores the width setting
         // pick a realy large font size to get
         // a huge button that covers
         // the area of the upload button
         fontSize: '400px'*/
      };
      input = qx.dom.Element.create('input', {
        type: 'file',
        name: 'input_' + this.toHashCode(),
        accept: 'image/*',
        capture: 'camera'
      });
      qx.bom.element.Style.setStyles(input, css, true);
      var that = this;
      qx.event.Registration.addListener(input, "change", function (event) {
        // Read in file
        var file = event.getTarget().files[0];

        // Ensure it's an image
        if (file.type.match(/image.*/)) {
          //console.log('An image has been loaded');

          // Load the image
          var reader = new FileReader();
          reader.onload = function (readerEvent) {
            var image = new Image();
            image.onload = function (imageEvent) {

              // Resize the image
              var canvas = document.createElement('canvas'),
                  width = image.width, height = image.height;
              var maxWidth = 600;
              var maxHeight = 600;
              if (that.__imageSize) {
                if (that.__imageSize.getWidth()) {
                  maxWidth = that.__imageSize.getWidth();
                }
                if (that.__imageSize.getHeight()) {
                  maxHeight = that.__imageSize.getHeight();
                }
              }
              if (width > maxWidth) {
                height *= maxWidth / width;
                width = maxWidth;
              }
              if (height > maxHeight) {
                width *= maxHeight / height;
                height = maxHeight;
              }
              canvas.width = width;
              canvas.height = height;
              canvas.getContext('2d').drawImage(image, 0, 0, width, height);
              var formatName = that.__formatName;
              if (!formatName) {
                formatName = "jpeg";
              }
              var dataUrl = canvas.toDataURL("image/" + formatName);
              //var dataUrl = canvas.toDataURL('image/png');

              /* Utility function to convert a canvas to a BLOB */
              var dataURLToBlob = function (dataURL) {
                var BASE64_MARKER = ';base64,';
                if (dataURL.indexOf(BASE64_MARKER) == -1) {
                  var parts = dataURL.split(',');
                  var contentType = parts[0].split(':')[1];
                  var raw = parts[1];

                  return new Blob([raw], {type: contentType});
                }

                var parts = dataURL.split(BASE64_MARKER);
                var contentType = parts[0].split(':')[1];
                var raw = window.atob(parts[1]);
                var rawLength = raw.length;

                var uInt8Array = new Uint8Array(rawLength);

                for (var i = 0; i < rawLength; ++i) {
                  uInt8Array[i] = raw.charCodeAt(i);
                }
                return new Blob([uInt8Array], {type: contentType});
              };
              var resizedImage = dataURLToBlob(dataUrl);
              that.fireDataEvent("imagePicked", {
                blob: resizedImage,
                url: dataUrl
              });
            };
            image.src = readerEvent.target.result;
          };
          reader.readAsDataURL(file);
        }
        //this.__formEl.submit();
        this.fireEvent();
      }, this);
      return input;
    },

    _createForm: function (submitUrl) {
      var form;
      var css = {
        'margin-top': '-30px'
      };
      form = qx.dom.Element.create('form', {
        // method: 'post',
        // action: submitUrl,
        // enctype: 'multipart/form-data',
        target: this._createIFrameTarget()
      });
      qx.bom.element.Style.setStyles(form, css, true);
      form.appendChild(this._createInput());
      return form;
    },

    /**
     * Create a hidden iframe which is used as target for the form submission.
     * Don't need a src attribute, if it was set to javascript:void we get an insecure
     * objects error in IE.
     *
     * @return {String}
     */
    _createIFrameTarget: function () {
      var frameName = "hiddenFrame_" + this.toHashCode();
      var frameNode;
      if (qx.core.Environment.get('browser.name') == 'ie' && qx.core.Environment.get('browser.version') < 8) {
        frameNode = document.createElement('<iframe name="' + frameName + '"></iframe>');
      } else {
        frameNode = document.createElement("iframe");
      }
      frameNode.id = (frameNode.name = frameName);
      frameNode.style.display = "none";
      document.body.appendChild(frameNode);
      return frameName;
    }
  }
});
