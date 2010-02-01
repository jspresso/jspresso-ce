/**
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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

package org.jspresso.framework.util.html {
  public class HtmlUtil {
    
    public static function isHtml(content:String):Boolean {
      if(content) {
        return content.toLowerCase().indexOf("<html>") > -1 || content.toLowerCase().indexOf("<textformat") > -1;
      }
      return false;
    }

    public static function preprocessHtml(content:String):String {
      var result:String = content;
      if(content) {
        // Flex does not handle the <p> tag. Transorm it to <br>.
//        result = result.replace(/<[p|P]>/g, "<br>");
//        result = result.replace(/<\/[p|P]>/g, "<br>");
      }
      return result;
    }
  }
}