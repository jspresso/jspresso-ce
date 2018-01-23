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

qx.Class.define("org.jspresso.framework.view.qx.FormattedTableCellRenderer", {
  extend: qx.ui.table.cellrenderer.Default, include: [org.jspresso.framework.view.qx.MCellAdditionalStyle],

  construct: function (table, format) {
    this.base(arguments);
    this.__table = table;
    this.__format = format;
  },

  members: {
    __format: null,
    __table: null,
    __action: null,
    __asideActions: null,
    __disableActionsWithField: false,

    _formatValue: function (cellInfo) {
      if (this.__format && cellInfo.value) {
        return this.__format.format(cellInfo.value);
      }
      return this.base(arguments, cellInfo);
    },

    _getContentHtml: function (cellInfo) {
      if (!org.jspresso.framework.util.html.HtmlUtil.isHtml(cellInfo.value) && (typeof(cellInfo.value) == "string"
          || cellInfo.value instanceof String)) {
        if (cellInfo.value.indexOf("\n") >= 0) {
          cellInfo.value = org.jspresso.framework.util.html.HtmlUtil.toHtml(
              org.jspresso.framework.util.html.HtmlUtil.replaceNewlines(cellInfo.value));
        }
      }
      if (org.jspresso.framework.util.html.HtmlUtil.isHtml(cellInfo.value) && this.__table.getRowHeight() < 150) {
        var contentHeight = qx.bom.Label.getHtmlSize(cellInfo.value).height;
        if (contentHeight) {
          if (contentHeight > 150) {
            contentHeight = 150;
          }
          if (this.__table.getRowHeight() < contentHeight) {
            this.__table.setRowHeight(parseInt(contentHeight + (cellInfo.styleHeight * 2 / 5) + 4));
          }
        }
      }
      var htmlContent;
      if (org.jspresso.framework.util.html.HtmlUtil.isHtml(cellInfo.value)) {
        htmlContent = cellInfo.value;
      } else if (this.__action) {
        var executeAction = "'executeAction(\"" + this.__action.getGuid() + " \");' ";
        htmlContent = "<u style='cursor: pointer;' "
            + "onMouseUp=" + executeAction
            + "onPointerUp=" + executeAction
            + "onTouchEnd=" + executeAction
            + ">"
            + this.base(arguments, cellInfo) + "</u>";
      } else {
        htmlContent = this.base(arguments, cellInfo);
      }
      htmlContent = org.jspresso.framework.util.html.HtmlUtil.bindActionToHtmlContent(htmlContent, this.__action);
      if ((!this.__disableActionsWithField || cellInfo.editable) && this.__asideActions) {
        var cellViewState = cellInfo.rowData.getChildren().getItem(cellInfo.col + 1);
        var actionsHtmlContent = "";
        for (var i = 0; i < this.__asideActions.length; i++) {
          var actionList = this.__asideActions[i];
          for (var j = 0; j < actionList.getActions().length ; j++) {
            var remoteAction = actionList.getActions()[j];
            if (remoteAction.isEnabled()) {
              var icon = remoteAction.getIcon();
              if (icon) {
                var imageUrlSpec = icon.getImageUrlSpec();
                imageUrlSpec = org.jspresso.framework.view.qx.AbstractQxViewFactory.completeForSVG(imageUrlSpec);
                var iconDimension = new org.jspresso.framework.util.gui.Dimension().set({
                  width: 16,
                  height: 16
                });

                actionsHtmlContent += "<div";
                var executeAction = "'executeAction(\"" + remoteAction.getGuid() + "\", null, \""
                    + cellViewState.getGuid() + "\", \"" + cellViewState.getPermId() + "\")'";
                actionsHtmlContent += " onMouseUp=" + executeAction
                actionsHtmlContent += " onPointerUp=" + executeAction
                actionsHtmlContent += " onTouchEnd=" + executeAction
                actionsHtmlContent += " style='overflow: hidden;";
                actionsHtmlContent += " cursor: pointer;";
                actionsHtmlContent += " top: 5px;";

                if (iconDimension) {
                  if (iconDimension.getWidth()) {
                    actionsHtmlContent += " width: " + iconDimension.getWidth() + "px;";
                  }
                  if (iconDimension.getHeight()) {
                    actionsHtmlContent += " height: " + iconDimension.getHeight() + "px;";
                  }
                }
                actionsHtmlContent += " margin-left: 5px;"
                //actionsHtmlContent += " margin-right: 5px;"
                actionsHtmlContent += " background-position: center;";
                actionsHtmlContent += " background-image: url(\"" + imageUrlSpec + "\");";
                actionsHtmlContent += " background-repeat: no-repeat;'>";
                actionsHtmlContent += "</div>";
              }
            }
          }
        }
      }
      htmlContent = "<div style='overflow: hidden; text-overflow: ellipsis; flex: 1'>" + htmlContent + "</div>"
      if (actionsHtmlContent) {
        htmlContent += actionsHtmlContent;
      }
      return "<div style='display: flex'>" + htmlContent + "</div>";
    },

    setAction: function (action) {
      this.__action = action;
    },

    getAction: function () {
      return this.__action;
    },

    setAsideActions: function (asideActions) {
      this.__asideActions = asideActions;
    },

    setDisableActionsWithField: function (disableActionsWithField) {
      this.__disableActionsWithField = disableActionsWithField;
    },

    _getCellStyle: function (cellInfo) {
      var superStyle = this.base(arguments, cellInfo);
      return superStyle + this._getAdditionalCellStyle(cellInfo);
    }
  }
});
