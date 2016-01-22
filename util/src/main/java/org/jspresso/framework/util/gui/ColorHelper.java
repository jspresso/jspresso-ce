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
 * This is a simple helper class to deal with color representations.
 *
 * @author Vincent Vandenschrick
 */
public final class ColorHelper {

  private ColorHelper() {
    // private constructor for helper class.
  }

  /**
   * Transforms a color rgba hex string representation to its rgba components.
   *
   * @param hexString
   *          the Hex string representation argb coded.
   * @return the rgba components of the color once parsed in an array.
   */
  public static int[] fromHexString(String hexString) {
    int[] rgba = new int[4];
    int offset = 0;
    if (hexString.length() == 10) {
      rgba[3] = Integer.parseInt(hexString.substring(2, 4), 16);
      offset = 2;
    } else {
      // to support un-existing alpha
      rgba[3] = Integer.parseInt("FF", 16);
    }
    rgba[0] = Integer.parseInt(hexString.substring(2 + offset, 4 + offset), 16);
    rgba[1] = Integer.parseInt(hexString.substring(4 + offset, 6 + offset), 16);
    rgba[2] = Integer.parseInt(hexString.substring(6 + offset, 8 + offset), 16);
    return rgba;
  }

  /**
   * Transforms a color rgba components to their hex string representation argb
   * coded.
   *
   * @param r
   *          the red component.
   * @param g
   *          the green component.
   * @param b
   *          the blue component.
   * @param a
   *          the alpha (transparency) component.
   * @return the Hex string representation.
   */
  public static String toHexString(int r, int g, int b, int a) {
    StringBuilder hexString = new StringBuilder("0x");
    hexString.append(formatByteToPaddedHex(a, 2));
    hexString.append(formatByteToPaddedHex(r, 2));
    hexString.append(formatByteToPaddedHex(g, 2));
    hexString.append(formatByteToPaddedHex(b, 2));
    return hexString.toString();
  }

  private static String formatByteToPaddedHex(int i, int l) {
    StringBuilder hex = new StringBuilder(Integer.toString(i, 16).toUpperCase());
    while (hex.length() < l) {
      hex.insert(0, "0");
    }
    return hex.toString();
  }

  /**
   * Tests whether the parameter string is a color specification.
   *
   * @param colorString
   *          the string to test.
   * @return {@code true} if the string can be parsed as a color.
   */
  public static boolean isColorSpec(String colorString) {
    return colorString != null && colorString.toLowerCase().startsWith("0x");
  }
}
