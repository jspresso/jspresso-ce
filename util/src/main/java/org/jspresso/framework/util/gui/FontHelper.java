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
package org.jspresso.framework.util.gui;

/**
 * This is a simple helper class to deal with font representations.
 *
 * @author Vincent Vandenschrick
 */
public final class FontHelper {

  private static final String BOLD   = "BOLD";
  private static final String ITALIC = "ITALIC";
  private static final String PLAIN  = "PLAIN";
  private static final String SEP    = ";";

  private FontHelper() {
    // private constructor for helper class.
  }

  /**
   * Transforms a font string representation to a font.
   *
   * @param fontString
   *          the font string representation. The font is coded
   *          {@code [name];[style];[size]}.
   *          <ul>
   *          <li>[name] is the name of the font.</li>
   *          <li>[style] is PLAIN, BOLD, ITALIC or a union of BOLD and ITALIC
   *          combined with the '|' character, i.e. BOLD|ITALIC.</li>
   *          <li>[size] is the size of the font.</li>
   *          </ul>
   * @return the font represented by the string.
   */
  public static Font fromString(String fontString) {
    if (fontString != null) {
      String[] fontAttributes = fontString.split(SEP, -1);
      if (fontAttributes.length == 3) {
        Font font = new Font();
        font.setName(fontAttributes[0]);
        if (fontAttributes[1].contains(BOLD)) {
          font.setBold(true);
        }
        if (fontAttributes[1].contains(ITALIC)) {
          font.setItalic(true);
        }
        if (fontAttributes[2] != null && fontAttributes[2].length() > 0) {
          font.setSize(Integer.parseInt(fontAttributes[2]));
        } else {
          font.setSize(-1);
        }
        return font;
      }
    }
    throw new IllegalArgumentException(
        "Font string representation must be formed as [name];[style];[size]");
  }

  /**
   * Transforms a font to its string representation.
   *
   * @param font
   *          the font to transform.
   * @return the font string representation.
   */
  public static String toString(Font font) {
    StringBuilder fontString = new StringBuilder();
    fontString.append(font.getName());
    fontString.append(SEP);
    if (font.isBold() && font.isItalic()) {
      fontString.append(BOLD).append("|").append(ITALIC);
    } else if (font.isBold()) {
      fontString.append(BOLD);
    } else if (font.isItalic()) {
      fontString.append(ITALIC);
    } else {
      fontString.append(PLAIN);
    }
    fontString.append(SEP);
    fontString.append(font.getSize());
    return fontString.toString();
  }

  /**
   * Tests whether the parameter string is a font specification.
   *
   * @param fontString
   *          the string to test.
   * @return {@code true} if the string can be parsed as a font.
   */
  public static boolean isFontSpec(String fontString) {
    return fontString != null && fontString.split(SEP, -1).length == 3;
  }

}
