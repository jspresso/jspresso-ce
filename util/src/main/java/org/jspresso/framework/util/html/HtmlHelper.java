/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.util.html;

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;

/**
 * This is a simple helper class to be able to cope with html.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class HtmlHelper {

  /**
   * <code>HTML_START</code>.
   */
  public static final String HTML_START = "<HTML>";

  /**
   * <code>HTML_END</code>.
   */
  public static final String HTML_END   = "</HTML>";

  private HtmlHelper() {
    // private constructor for helper class.
  }

  /**
   * Transforms a string to html and emphasis.
   * 
   * @param message
   *          the message to transform.
   * @return the html emphasised string.
   */
  public static String emphasis(String message) {
    if (message != null) {
      return "<b>" + message + "</b>";
    }
    return message;
  }

  /**
   * Keeps the text preformatted.
   * 
   * @param message
   *          the message to transform.
   * @return the html preformatted text.
   */
  public static String preformat(String message) {
    if (message != null) {
      return "<pre>" + message + "</pre>";
    }
    return message;
  }

  /**
   * Surrounds with html tags.
   * 
   * @param message
   *          the message to transform.
   * @return the html preformatted text.
   */
  public static String toHtml(String message) {
    if (message != null) {
      return HTML_START + message + HTML_END;
    }
    return message;
  }

  /**
   * Escapes special characters for HTML.
   * 
   * @param aText
   *          the test to escape.
   * @return the ecaped HTML text.
   */
  public static String escapeForHTML(String aText) {
    final StringBuilder result = new StringBuilder();
    final StringCharacterIterator iterator = new StringCharacterIterator(aText);
    char character = iterator.current();
    while (character != CharacterIterator.DONE) {
      if (character == '<') {
        result.append("&lt;");
      } else if (character == '>') {
        result.append("&gt;");
      } else if (character == '&') {
        result.append("&amp;");
      } else if (character == '\"') {
        result.append("&quot;");
      } else if (character == '\t') {
        addCharEntity(9, result);
      } else if (character == '!') {
        addCharEntity(33, result);
      } else if (character == '#') {
        addCharEntity(35, result);
      } else if (character == '$') {
        addCharEntity(36, result);
      } else if (character == '%') {
        addCharEntity(37, result);
      } else if (character == '\'') {
        addCharEntity(39, result);
      } else if (character == '(') {
        addCharEntity(40, result);
      } else if (character == ')') {
        addCharEntity(41, result);
      } else if (character == '*') {
        addCharEntity(42, result);
      } else if (character == '+') {
        addCharEntity(43, result);
      } else if (character == ',') {
        addCharEntity(44, result);
      } else if (character == '-') {
        addCharEntity(45, result);
      } else if (character == '.') {
        addCharEntity(46, result);
      } else if (character == '/') {
        addCharEntity(47, result);
      } else if (character == ':') {
        addCharEntity(58, result);
      } else if (character == ';') {
        addCharEntity(59, result);
      } else if (character == '=') {
        addCharEntity(61, result);
      } else if (character == '?') {
        addCharEntity(63, result);
      } else if (character == '@') {
        addCharEntity(64, result);
      } else if (character == '[') {
        addCharEntity(91, result);
      } else if (character == '\\') {
        addCharEntity(92, result);
      } else if (character == ']') {
        addCharEntity(93, result);
      } else if (character == '^') {
        addCharEntity(94, result);
      } else if (character == '_') {
        addCharEntity(95, result);
      } else if (character == '`') {
        addCharEntity(96, result);
      } else if (character == '{') {
        addCharEntity(123, result);
      } else if (character == '|') {
        addCharEntity(124, result);
      } else if (character == '}') {
        addCharEntity(125, result);
      } else if (character == '~') {
        addCharEntity(126, result);
      } else {
        // the char is not a special one
        // add it to the result as is
        result.append(character);
      }
      character = iterator.next();
    }
    return result.toString();
  }

  private static void addCharEntity(int aIdx, StringBuilder aBuilder) {
    String padding = "";
    if (aIdx <= 9) {
      padding = "00";
    } else if (aIdx <= 99) {
      padding = "0";
    } else {
      // no prefix
    }
    String number = padding + aIdx;
    aBuilder.append("&#" + number + ";");
  }

  /**
   * Is this message HTML code.
   * 
   * @param msg
   *          the message to test.
   * @return true if it contains &lt;HTML&gt;
   */
  public static boolean isHtml(String msg) {
    if (msg == null) {
      return false;
    }
    return msg.toUpperCase().indexOf(HTML_START) >= 0;
  }
}
