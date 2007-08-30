/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.gui;

/**
 * This is a simple helper class to deal with color representations.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ColorHelper {

  private ColorHelper() {
    // private constructor for helper class.
  }

  /**
   * Transforms a color rgba components to their hex string representation.
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
    StringBuffer hexString = new StringBuffer("0x");
    hexString.append(formatByteToPaddedHex(a, 2));
    hexString.append(formatByteToPaddedHex(b, 2));
    hexString.append(formatByteToPaddedHex(g, 2));
    hexString.append(formatByteToPaddedHex(r, 2));
    return hexString.toString();
  }

  /**
   * Transforms a color rgba components to their hex string representation.
   *
   * @param hexString
   *          the Hex string representation.
   * @return the rgba components of the color once parsed in an array.
   */
  public static int[] fromHexString(String hexString) {
    int[] rgba = new int[4];
    for (int i = 3; i >= 0; i--) {
      rgba[3 - i] = Integer.parseInt(hexString.substring(2 * i + 2, 2 * i + 4),
          16);
    }
    return rgba;
  }

  private static String formatByteToPaddedHex(int i, int l) {
    StringBuffer hex = new StringBuffer(Integer.toString(i, 16).toUpperCase());
    while (hex.length() < l) {
      hex.insert(0, "0");
    }
    return hex.toString();
  }
}
