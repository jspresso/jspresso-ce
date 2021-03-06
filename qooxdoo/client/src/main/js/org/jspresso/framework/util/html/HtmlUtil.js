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

qx.Class.define("org.jspresso.framework.util.html.HtmlUtil", {
  statics: {

    /**
     * @param content {String}
     * @return {Boolean}
     */
    isHtml: function (content) {
      if (typeof content === "string" || content instanceof String) {
        return content.toLowerCase().indexOf("<html>") > -1 || content.toLowerCase().indexOf("<p") > -1
            || content.toLowerCase().indexOf("<br") > -1;
      }
      return false;
    },

    preformat: function (message) {
      if (message) {
        return "<pre>" + message + "</pre>";
      }
      return message;
    },

    toHtml: function (message) {
      if (message != null) {
        return "<html>" + message + "</html>";
      }
      return message;
    },

    sanitizeHtml: function (message) {
      if (message != null) {
        return qx.bom.String.escape(message);
      }
      return message;
    },

    replaceNewlines: function (message) {
      if (message != null) {
        return message.replace(new RegExp("\n", 'g'), "<br/>");
      }
      return message;
    },

    replaceWhiteSpaces: function (message) {
      if (message != null) {
        return message.replace(new RegExp(" ", 'g'), "&nbsp;");
      }
      return message;
    },

    bindActionToHtmlContent: function (htmlContent, action) {
      if (htmlContent && action) {
        var executeAction = "'executeAction(\"$1\")' ";
        htmlContent = htmlContent.replace(/<a href='executeAction\(["]?([^"]*)["]?\)'>([^<]*)<\/a>/g,
            "<u style='cursor: pointer;'"
            + "onMouseUp=" + executeAction
            + "onPointerUp=" + executeAction
            + "onTouchEnd=" + executeAction
					  + ">$2</u>"
				);
        htmlContent = htmlContent.replace(/executeAction\(/g, "executeAction(\"" + action.getGuid() + "\",");
        htmlContent = htmlContent.replace(/\,\)/g, ")");
      }
      return htmlContent;
    }
  }
});
