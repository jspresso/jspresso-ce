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
package org.jspresso.framework.util.html;

import java.io.StringWriter;

import org.apache.commons.lang3.StringEscapeUtils;

/**
 * This is a simple helper class to be able to cope with html.
 *
 * @author Vincent Vandenschrick
 */
public final class HtmlHelper {

  /**
   * {@code HTML_END}.
   */
  public static final String HTML_END   = "</HTML>";

  /**
   * {@code HTML_START}.
   */
  public static final String HTML_START = "<HTML>";

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
    return null;
  }

  /**
   * Escapes special characters for HTML. Does not escape spaces. If you need
   * to, you must use {@link #escapeForHTML(String, boolean)} with
   * {@code escapeSpaces=true}.
   *
   * @param text
   *          the text to escape.
   * @return the escaped HTML text.
   */
  public static String escapeForHTML(String text) {
    return escapeForHTML(text, false);
  }

  /**
   * Escapes special characters for HTML.
   *
   * @param text
   *          the text to escape.
   * @param escapeSpaces
   *          should we also escape spaces using &'nbsp'; entity ?
   * @return the escaped HTML text.
   */
  public static String escapeForHTML(String text, boolean escapeSpaces) {
    if (text == null) {
      return null;
    }
    // use apache lib to escape...
    // this library doesn't escape spaces (see workaround bellow)
    String str = StringEscapeUtils.escapeHtml4(text);

    if (escapeSpaces) {
      // Workaround : we have also to escape spaces...
      StringWriter writer = new StringWriter((int) (str.length() * 1.5));
      boolean spaces = false;
      int len = str.length();
      for (int i = 0; i < len; i++) {
        char c = str.charAt(i);

        if (c != ' ') {
          writer.write(c);
          spaces = false;
        } else {
          // space or spaces...
          if (i == 0) {
            // start with space
            spaces = true;
          } else if (i == len - 1) {
            // ends with space
            spaces = true;
          } else if (i < len - 1 && str.charAt(i + 1) == ' ') {
            // two or more spaces
            spaces = true;
          }

          if (spaces) {
            writer.write("&nbsp;");
          } else {
            writer.write(' ');
          }
        }
      }
      str = writer.toString();
    }
    return str;
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
    return msg.toUpperCase().contains(HTML_START);
  }

  /**
   * Keeps the text pre-formatted.
   *
   * @param message
   *          the message to transform.
   * @return the html pre-formatted text.
   */
  public static String preformat(String message) {
    if (message != null) {
      return "<pre>" + message + "</pre>";
    }
    return null;
  }

  /**
   * Surrounds with html tags.
   *
   * @param message
   *          the message to transform.
   * @return the html pre-formatted text.
   */
  public static String toHtml(String message) {
    if (message != null) {
      return HTML_START + message + HTML_END;
    }
    return null;
  }
}
