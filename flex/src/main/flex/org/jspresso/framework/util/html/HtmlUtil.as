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

package org.jspresso.framework.util.html {

public class HtmlUtil {

  private static const HTML_ENTITIES:Object = {};
  {
    HTML_ENTITIES["&nbsp;"] = "\u00A0"; // non-breaking space
    HTML_ENTITIES["&iexcl;"] = "\u00A1"; // inverted exclamation mark
    HTML_ENTITIES["&cent;"] = "\u00A2"; // cent sign
    HTML_ENTITIES["&pound;"] = "\u00A3"; // pound sign
    HTML_ENTITIES["&curren;"] = "\u00A4"; // currency sign
    HTML_ENTITIES["&yen;"] = "\u00A5"; // yen sign
    HTML_ENTITIES["&brvbar;"] = "\u00A6"; // broken vertical bar (|)
    HTML_ENTITIES["&sect;"] = "\u00A7"; // section sign
    HTML_ENTITIES["&uml;"] = "\u00A8"; // diaeresis
    HTML_ENTITIES["&copy;"] = "\u00A9"; // copyright sign
    HTML_ENTITIES["&reg;"] = "\u00AE"; // registered sign
    HTML_ENTITIES["&deg;"] = "\u00B0"; // degree sign
    HTML_ENTITIES["&plusmn;"] = "\u00B1"; // plus-minus sign
    HTML_ENTITIES["&sup1;"] = "\u00B9"; // superscript one
    HTML_ENTITIES["&sup2;"] = "\u00B2"; // superscript two
    HTML_ENTITIES["&sup3;"] = "\u00B3"; // superscript three
    HTML_ENTITIES["&acute;"] = "\u00B4"; // acute accent
    HTML_ENTITIES["&micro;"] = "\u00B5"; // micro sign
    HTML_ENTITIES["&frac14;"] = "\u00BC"; // vulgar fraction one quarter
    HTML_ENTITIES["&frac12;"] = "\u00BD"; // vulgar fraction one half
    HTML_ENTITIES["&frac34;"] = "\u00BE"; // vulgar fraction three quarters
    HTML_ENTITIES["&iquest;"] = "\u00BF"; // inverted question mark
    HTML_ENTITIES["&Agrave;"] = "\u00C0"; // Latin capital letter A with grave
    HTML_ENTITIES["&Aacute;"] = "\u00C1"; // Latin capital letter A with acute
    HTML_ENTITIES["&Acirc;"] = "\u00C2"; // Latin capital letter A with circumflex
    HTML_ENTITIES["&Atilde;"] = "\u00C3"; // Latin capital letter A with tilde
    HTML_ENTITIES["&Auml;"] = "\u00C4"; // Latin capital letter A with diaeresis
    HTML_ENTITIES["&Aring;"] = "\u00C5"; // Latin capital letter A with ring above
    HTML_ENTITIES["&AElig;"] = "\u00C6"; // Latin capital letter AE
    HTML_ENTITIES["&Ccedil;"] = "\u00C7"; // Latin capital letter C with cedilla
    HTML_ENTITIES["&Egrave;"] = "\u00C8"; // Latin capital letter E with grave
    HTML_ENTITIES["&Eacute;"] = "\u00C9"; // Latin capital letter E with acute
    HTML_ENTITIES["&Ecirc;"] = "\u00CA"; // Latin capital letter E with circumflex
    HTML_ENTITIES["&Euml;"] = "\u00CB"; // Latin capital letter E with diaeresis
    HTML_ENTITIES["&Igrave;"] = "\u00CC"; // Latin capital letter I with grave
    HTML_ENTITIES["&Iacute;"] = "\u00CD"; // Latin capital letter I with acute
    HTML_ENTITIES["&Icirc;"] = "\u00CE"; // Latin capital letter I with circumflex
    HTML_ENTITIES["&Iuml;"] = "\u00CF"; // Latin capital letter I with diaeresis
    HTML_ENTITIES["&ETH;"] = "\u00D0"; // Latin capital letter ETH
    HTML_ENTITIES["&Ntilde;"] = "\u00D1"; // Latin capital letter N with tilde
    HTML_ENTITIES["&Ograve;"] = "\u00D2"; // Latin capital letter O with grave
    HTML_ENTITIES["&Oacute;"] = "\u00D3"; // Latin capital letter O with acute
    HTML_ENTITIES["&Ocirc;"] = "\u00D4"; // Latin capital letter O with circumflex
    HTML_ENTITIES["&Otilde;"] = "\u00D5"; // Latin capital letter O with tilde
    HTML_ENTITIES["&Ouml;"] = "\u00D6"; // Latin capital letter O with diaeresis
    HTML_ENTITIES["&Oslash;"] = "\u00D8"; // Latin capital letter O with stroke
    HTML_ENTITIES["&Ugrave;"] = "\u00D9"; // Latin capital letter U with grave
    HTML_ENTITIES["&Uacute;"] = "\u00DA"; // Latin capital letter U with acute
    HTML_ENTITIES["&Ucirc;"] = "\u00DB"; // Latin capital letter U with circumflex
    HTML_ENTITIES["&Uuml;"] = "\u00DC"; // Latin capital letter U with diaeresis
    HTML_ENTITIES["&Yacute;"] = "\u00DD"; // Latin capital letter Y with acute
    HTML_ENTITIES["&THORN;"] = "\u00DE"; // Latin capital letter THORN
    HTML_ENTITIES["&szlig;"] = "\u00DF"; // Latin small letter sharp s = ess-zed
    HTML_ENTITIES["&agrave;"] = "\u00E0"; // Latin small letter a with grave
    HTML_ENTITIES["&aacute;"] = "\u00E1"; // Latin small letter a with acute
    HTML_ENTITIES["&acirc;"] = "\u00E2"; // Latin small letter a with circumflex
    HTML_ENTITIES["&atilde;"] = "\u00E3"; // Latin small letter a with tilde
    HTML_ENTITIES["&auml;"] = "\u00E4"; // Latin small letter a with diaeresis
    HTML_ENTITIES["&aring;"] = "\u00E5"; // Latin small letter a with ring above
    HTML_ENTITIES["&aelig;"] = "\u00E6"; // Latin small letter ae
    HTML_ENTITIES["&ccedil;"] = "\u00E7"; // Latin small letter c with cedilla
    HTML_ENTITIES["&egrave;"] = "\u00E8"; // Latin small letter e with grave
    HTML_ENTITIES["&eacute;"] = "\u00E9"; // Latin small letter e with acute
    HTML_ENTITIES["&ecirc;"] = "\u00EA"; // Latin small letter e with circumflex
    HTML_ENTITIES["&euml;"] = "\u00EB"; // Latin small letter e with diaeresis
    HTML_ENTITIES["&igrave;"] = "\u00EC"; // Latin small letter i with grave
    HTML_ENTITIES["&iacute;"] = "\u00ED"; // Latin small letter i with acute
    HTML_ENTITIES["&icirc;"] = "\u00EE"; // Latin small letter i with circumflex
    HTML_ENTITIES["&iuml;"] = "\u00EF"; // Latin small letter i with diaeresis
    HTML_ENTITIES["&eth;"] = "\u00F0"; // Latin small letter eth
    HTML_ENTITIES["&ntilde;"] = "\u00F1"; // Latin small letter n with tilde
    HTML_ENTITIES["&ograve;"] = "\u00F2"; // Latin small letter o with grave
    HTML_ENTITIES["&oacute;"] = "\u00F3"; // Latin small letter o with acute
    HTML_ENTITIES["&ocirc;"] = "\u00F4"; // Latin small letter o with circumflex
    HTML_ENTITIES["&otilde;"] = "\u00F5"; // Latin small letter o with tilde
    HTML_ENTITIES["&ouml;"] = "\u00F6"; // Latin small letter o with diaeresis
    HTML_ENTITIES["&oslash;"] = "\u00F8"; // Latin small letter o with stroke
    HTML_ENTITIES["&ugrave;"] = "\u00F9"; // Latin small letter u with grave
    HTML_ENTITIES["&uacute;"] = "\u00FA"; // Latin small letter u with acute
    HTML_ENTITIES["&ucirc;"] = "\u00FB"; // Latin small letter u with circumflex
    HTML_ENTITIES["&uuml;"] = "\u00FC"; // Latin small letter u with diaeresis
    HTML_ENTITIES["&yacute;"] = "\u00FD"; // Latin small letter y with acute
    HTML_ENTITIES["&thorn;"] = "\u00FE"; // Latin small letter thorn
    HTML_ENTITIES["&yuml;"] = "\u00FF"; // Latin small letter y with diaeresis
  }

  public static function sanitizeHtml(source:String):String {
    var buf:String = source;
    var finished:Boolean = false;
    for (var entity:String in HTML_ENTITIES) {
      if (!finished) {     // if it finds other occurrences of the & symbol
        //noinspection JSUnfilteredForInLoop
        buf = buf.replace(new RegExp(entity, "g"), HTML_ENTITIES[entity]);
        finished = finished || buf.indexOf('&') < 0;
      }
    }
    return buf;
  }

  public static function isHtml(content:String):Boolean {
    if (content) {
      return content.toLowerCase().indexOf("<html>") > -1 || content.toLowerCase().indexOf("<textformat") > -1;
    }
    return false;
  }

  public static function extractFirstLine(multiLine:String):String {
    var firstLine:String = multiLine;
    if (firstLine) {
      var lineBreaks:Array = ["\n", "\r", "<p>", "<br>"];
      for (var i:int = 0; i < lineBreaks.length; i++) {
        var j:int = firstLine.indexOf(lineBreaks[i]);
        if (j > 0) {
          firstLine = firstLine.substr(0, j);
        }
      }
    }
    return firstLine;
  }

  public static function convertFromXHtml(str:String):String {

    if (str == null) {
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

    pattern = /<span style="(.*?)">/g;
    str = str.replace(pattern, "<FONT $1>");
    pattern = /color:(.*?);/g;
    str = str.replace(pattern, "COLOR=\"$1\" ");
    pattern = /font-size:(.*?)px;/g;
    str = str.replace(pattern, "SIZE=\"$1\" ");
    pattern = /font-family:(.*?);/g;
    str = str.replace(pattern, "FACE=\"$1\" ");
    pattern = /text-align:(.*?);/g;
    str = str.replace(pattern, "ALIGN=\"$1\" ");

    // in order to restore empty lines modifications
    pattern = />&nbsp;<\/span>/g;
    str = str.replace(pattern, "></span>");

    pattern = /<\/span.*?>/g;
    str = str.replace(pattern, "</FONT>");

    pattern = /<\/li><li>/g;
    str = str.replace(pattern, "</LI><LI>");
    pattern = /<\/li><\/ul>/g;
    str = str.replace(pattern, "</LI>");
    pattern = /<ul><li>/g;
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

    pattern = /&#39;/g;
    str = str.replace(pattern, "&apos;");

    // Remove extra white space
    pattern = /  /g;
    str = str.replace(pattern, " ");

    return str;
  }

  public static function convertToXHtml(str:String):String {

    if (str == null) {
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

    pattern = /COLOR="(.*?)"/g;
    str = str.replace(pattern, "color:$1;");

    pattern = /SIZE="(.*?)"/g;
    str = str.replace(pattern, "font-size:$1px;");

    pattern = /FACE="(.*?)"/g;
    str = str.replace(pattern, "font-family:$1;");

    pattern = /ALIGN="(.*?)"/g;
    str = str.replace(pattern, "text-align:$1;");

    pattern = /LETTERSPACING=".*?"/g;
    str = str.replace(pattern, "");

    pattern = /KERNING=".*?"/g;
    str = str.replace(pattern, "");

    pattern = /<\/FONT.*?>/g;
    str = str.replace(pattern, "</span>");

    // in order to preserve empty lines
    pattern = /><\/span>/g;
    str = str.replace(pattern, ">&nbsp;</span>");

    pattern = /<\/LI><LI>/g;
    str = str.replace(pattern, "</li><li>");
    pattern = /<\/LI>/g;
    str = str.replace(pattern, "</li></ul>");
    pattern = /<LI>/g;
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

    pattern = /&apos;/g;
    str = str.replace(pattern, "&#39;");

    return str;
  }
}
}
