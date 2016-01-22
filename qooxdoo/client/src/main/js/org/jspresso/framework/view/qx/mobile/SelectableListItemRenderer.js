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

qx.Class.define("org.jspresso.framework.view.qx.mobile.SelectableListItemRenderer", {
  extend: qx.ui.mobile.list.renderer.Default,

  statics: {
  },

  construct: function () {
    this.base(arguments);
  },

  members: {

    __selectBox : null,

    /**
     * Inits the widgets for the renderer.
     *
     */
    _init : function()
    {
      this.__selectBox = this._createSelectBox();
      this.add(this.__selectBox);
      this.base(arguments);
    },

    /**
     * Creates and returns the SelectBox widget. Override this to adapt the widget code.
     *
     * @return {qx.ui.mobile.form.CheckBox} the SelectBox widget.
     */
    _createSelectBox : function() {
      var selectBox = new qx.ui.mobile.form.CheckBox();
      //var selectBox = new qx.ui.mobile.form.RadioButton();
      selectBox.setAnonymous(true);
      selectBox.addCssClass("list-item-image");
      return selectBox;
    },

    // property apply
    _applySelected : function(value, old)
    {
      this.__selectBox.setValue(value);
      this.base(arguments, value,  old);
    }
  }
});
