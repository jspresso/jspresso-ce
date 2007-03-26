/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.util.html;

/**
 * This is a simple helper class to be able to cope with html.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class HtmlHelper {

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
      return "<html><b>" + message + "</b></html>";
    }
    return message;
  }
}
