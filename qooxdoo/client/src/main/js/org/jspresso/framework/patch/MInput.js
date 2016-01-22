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
qx.Mixin.define("org.jspresso.framework.patch.MInput", {
  members: {

    /**
     * Handles the <code>click</code> and <code>focus</code> event on this input widget.
     * @param evt {qx.event.type.Event} <code>click</code> or <code>focus</code> event
     */
    _onSelected: function (evt) {
      if (!(evt.getTarget() instanceof qx.ui.mobile.form.TextField) && !(evt.getTarget()
          instanceof qx.ui.mobile.form.NumberField)) {
        return;
      }

      if (qx.core.Environment.get("os.name") == "ios") {
      } else {
        setTimeout(function () {
          if (qx.core.Environment.get("os.name") == "android") {
            var old = this._getCaretPosition();
            window.getSelection().empty();

            setTimeout(function () {
              this._setCaretPosition(old);
            }.bind(this), 50);
          }

        }.bind(this), 0);
      }
    }
  }
});
