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

/**
 * This is a simple helper class to be able to cope with html.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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
 * <p>
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
}
