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

qx.Class.define("org.jspresso.framework.view.qx.mobile.CheckBoxListItemRenderer", {
  extend: qx.ui.mobile.list.renderer.Default,

  statics: {
  },

  construct: function () {
    this.base(arguments);
  },

  members: {

    __checkBox : null,

    /**
     * Inits the widgets for the renderer.
     *
     */
    _init : function()
    {
      this.__checkBox = this._createCheckBox();
      this.add(this.__checkBox);
      this.base(arguments);
    },

    /**
     * Creates and returns the CheckBox widget. Override this to adapt the widget code.
     *
     * @return {qx.ui.mobile.form.CheckBox} the CheckBox widget.
     */
    _createCheckBox : function() {
      var checkBox = new qx.ui.mobile.form.CheckBox();
      checkBox.setAnonymous(true);
      checkBox.addCssClass("list-itemimage");
      return checkBox;
    },

    // property apply
    _applySelected : function(value, old)
    {
      this.__checkBox.setValue(value);
      this.base(arguments, value,  old);
    }
  }
});
