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

package org.jspresso.framework.view.flex {

  import mx.controls.RichTextEditor;

  public class EnhancedRichTextEditor extends RichTextEditor {

    public function EnhancedRichTextEditor() {
      super();
    }
    
    public function get xhtmlText():String {
      return convertToXHtml(this.htmlText);
    }
    
    public function set xhtmlText(val:String):void {
      this.htmlText = convertFromXHtml(val);
    }
  
    public static function convertFromXHtml(str:String):String {

      if(str == null) {
        return str;
      }
      
      var pattern:RegExp;
    
      pattern = /<p style="text-align:left">/g;
      str = str.replace(pattern, "<P ALIGN=\"LEFT\">");
      pattern = /<p style="text-align:right">/g;
      str = str.replace(pattern, "<P ALIGN=\"RIGHT\">");
      pattern = /<p style="text-align:justify">/g;
      str = str.replace(pattern, "<P ALIGN=\"JUSTIFY\">");
      pattern = /<\/p>/g;
      str = str.replace(pattern, "</P>");
    
      pattern = /<span style=\"(.*?)\">/g;
      str = str.replace(pattern, "<FONT $1>");
      pattern = /color:(.*?);/g;
      str = str.replace(pattern, "COLOR=\"$1\" ");
      pattern = /font-size:(.*?)px;/g;
      str = str.replace(pattern, "SIZE=\"$1\" ");
      pattern = /font-family:(.*?);/g;
      str = str.replace(pattern, "FACE=\"$1\" ");
      pattern = /text-align:(.*?);/g;
      str = str.replace(pattern, "ALIGN=\"$1\" ");
    
      pattern = /<\/span.*?>/g;
      str = str.replace(pattern, "</FONT>");
    
      pattern= /<\/li><li>/g;
      str = str.replace(pattern, "</LI><LI>");
      pattern= /<\/li><\/ul>/g;
      str = str.replace(pattern, "</LI>");
      pattern= /<ul><li>/g;
      str = str.replace(pattern, "<LI>");
    
      pattern = /<em>/g;
      str = str.replace(pattern, "<I>");
      pattern = /<\/em>/g;
      str = str.replace(pattern, "</I>");
      pattern = /<strong>/g;
      str = str.replace(pattern, "<B>");
      pattern = /<\/strong>/g;
      str = str.replace(pattern, "</B>");
      pattern = /<u>/g;
      str = str.replace(pattern, "<U>");
      pattern = /<\/u>/g;
      str = str.replace(pattern, "</U>");
    
      // Remove extra white space
      pattern = /  /g;
      str = str.replace(pattern, " ");
    
      return str;
    }
  
    public static function convertToXHtml(str:String):String {

      if(str == null) {
        return str;
      }
      
      var pattern:RegExp;
      
      pattern = /<TEXTFORMAT.*?>/g;
      str = str.replace(pattern, "");
      pattern = /<\/TEXTFORMAT.*?>/g;
      str = str.replace(pattern, "");
      
      pattern = /<P ALIGN="LEFT">/g;
      str = str.replace(pattern, "<p style=\"text-align:left\">");
      pattern = /<P ALIGN="RIGHT">/g;
      str = str.replace(pattern, "<p style=\"text-align:right\">");
      pattern = /<P ALIGN="JUSTIFY">/g;
      str = str.replace(pattern, "<p style=\"text-align:justify\">");
      pattern = /<\/P>/g;
      str = str.replace(pattern, "</p>");
      
      pattern = /<FONT (.*?)>/g;
      str = str.replace(pattern, "<span style=\"$1\">");
      
      pattern = /COLOR=\"(.*?)\"/g;
      str = str.replace(pattern, "color:$1;");
     
      pattern = /SIZE=\"(.*?)\"/g;
      str = str.replace(pattern, "font-size:$1px;");
      
      pattern = /FACE=\"(.*?)\"/g;
      str = str.replace(pattern, "font-family:$1;");
      
      pattern = /ALIGN=\"(.*?)\"/g;
      str = str.replace(pattern, "text-align:$1;");
      
      pattern = /LETTERSPACING=\".*?\"/g;
      str = str.replace(pattern, "");
      
      pattern = /KERNING=\".*?\"/g;
      str = str.replace(pattern, "");
      
      pattern = /<\/FONT.*?>/g;
      str = str.replace(pattern, "</span>");
      
      pattern= /<\/LI><LI>/g;
      str = str.replace(pattern, "</li><li>");
      pattern= /<\/LI>/g;
      str = str.replace(pattern, "</li></ul>");
      pattern= /<LI>/g;
      str = str.replace(pattern, "<ul><li>");
     
      pattern = /<I>/g;
      str = str.replace(pattern, "<em>");
      pattern = /<\/I>/g;
      str = str.replace(pattern, "</em>");
      pattern = /<B>/g;
      str = str.replace(pattern, "<strong>");
      pattern = /<\/B>/g;
      str = str.replace(pattern, "</strong>");
      pattern = /<U>/g;
      str = str.replace(pattern, "<u>");
      pattern = /<\/U>/g;
      str = str.replace(pattern, "</u>");
      
      return str;
    }
  }
}