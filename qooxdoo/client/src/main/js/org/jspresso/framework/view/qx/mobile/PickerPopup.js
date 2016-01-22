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
qx.Class.define("org.jspresso.framework.view.qx.mobile.PickerPopup",
    {
      extend: qx.ui.mobile.dialog.Popup,

      /**
       * @param picker {qx.ui.mobile.control.Picker ? null} The pickup component to show in the popup.
       * @param anchor {qx.ui.mobile.core.Widget ? null} The anchor widget for this item. If no anchor is available,
       *       the menu will be displayed modal and centered on screen.
       */
      construct: function (picker, anchor) {
        this.__picker = picker;

        this.__pickerContent = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.VBox());

        this.__pickerConfirmButton = new qx.ui.mobile.form.Button("Choose");
        this.__pickerConfirmButton.addListener("tap", this.confirm, this);

        this.__pickerCancelButton = new qx.ui.mobile.form.Button("Cancel");
        this.__pickerCancelButton.addListener("tap", this.hide, this);

        this.__pickerButtonContainer = new qx.ui.mobile.container.Composite(new qx.ui.mobile.layout.HBox());
        this.__pickerButtonContainer.add(this.__pickerConfirmButton);
        this.__pickerButtonContainer.add(this.__pickerCancelButton);
        this.__pickerButtonContainer.addCssClass("gap");

        this.__pickerContent.add(this.__picker);
        this.__pickerContent.add(this.__pickerButtonContainer);

        if (anchor) {
          this.setModal(false);
        } else {
          this.setModal(true);
        }

        this.base(arguments, this.__pickerContent, anchor);
      },


      /*
       *****************************************************************************
       EVENTS
       *****************************************************************************
       */

      events: {
        /**
         * Fired when the picker is closed. This means user has confirmed its selection.
         * Thie events contains all data which were chosen by user.
         */
        confirmSelection: "qx.event.type.Data"
      },


      /*
       *****************************************************************************
       PROPERTIES
       *****************************************************************************
       */

      properties: {
        // overridden
        defaultCssClass: {
          refine: true,
          init: "picker-dialog"
        }
      },


      /*
       *****************************************************************************
       MEMBERS
       *****************************************************************************
       */

      members: {
        /** @type {qx.ui.mobile.control.Picker} */
        __picker: null,
        __pickerConfirmButton: null,
        __pickerCancelButton: null,
        __pickerButtonContainer: null,
        __pickerContent: null,
        __labelHeight: null,


        /**
         * Confirms the selection, fires "confirmSelection" data event and hides the picker dialog.
         */
        confirm: function () {
          this.hide();
          this._fireConfirmSelection();
        },

        /**
         * Setter for the caption of the picker dialog's confirm button.
         * Default is "OK".
         * @param caption {String} the caption of the confirm button.
         */
        setConfirmButtonCaption: function (caption) {
          if (this.__pickerConfirmButton) {
            this.__pickerConfirmButton.setValue(caption);
          }
        },

        /**
         * Setter for the caption of the picker dialog's cancel button.
         * Default is "Cancel".
         * @param caption {String} the caption of the cancel button.
         */
        setCancelButtonCaption: function (caption) {
          if (this.__pickerCancelButton) {
            this.__pickerCancelButton.setValue(caption);
          }
        },

        /**
         * Collects data for the "confirmSelection" event and fires it.
         */
        _fireConfirmSelection: function () {
          var model = this.__picker.getModel();
          var slotCount = this.__picker.getSlotCount();
          var selectionData = [];
          for (var slotIndex = 0; slotIndex < slotCount; slotIndex++) {
            var selectedIndex = this.__picker.getSelectedIndex(slotIndex);
            var selectedValue = model.getItem(slotIndex).getItem(selectedIndex);

            var slotData = {index: selectedIndex, item: selectedValue, slot: slotIndex};
            selectionData.push(slotData);
          }

          this.fireDataEvent("confirmSelection", selectionData);
        }
      },


      /*
       *****************************************************************************
       DESTRUCTOR
       *****************************************************************************
       */

      destruct: function () {
        this.__pickerConfirmButton.removeListener("tap", this.confirm, this);
        this.__pickerCancelButton.removeListener("tap", this.hide, this);

        this._disposeObjects("__picker", "__pickerButtonContainer", "__pickerConfirmButton",
            "__pickerCancelButton", "__pickerContent");
      }

    });
