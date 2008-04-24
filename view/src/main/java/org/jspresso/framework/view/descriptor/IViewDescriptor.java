/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.view.descriptor;

import java.awt.Color;
import java.awt.Font;

import org.jspresso.framework.view.action.IActionable;

/**
 * This public interface is the super-interface of all view descriptors.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IViewDescriptor extends ISubViewDescriptor, IActionable {

  /**
   * <code>NONE</code> border constant.
   */
  int NONE   = -1;

  /**
   * <code>SIMPLE</code> border constant.
   */
  int SIMPLE = 1;

  /**
   * <code>TITLED</code> border constant.
   */
  int TITLED = 2;

  /**
   * Gets the background color of this view.
   * 
   * @return this view's foreground color.
   */
  Color getBackground();

  /**
   * Gets the border type used to surround view.
   * 
   * @return the border type :
   *         <li> <code>NONE</code> means no border.
   *         <li> <code>SIMPLE</code> means a simple line border.
   *         <li> <code>TITLED</code> means a titled border. The title will be
   *         the name of the view.
   */
  int getBorderType();

  /**
   * Gets the font of this view.
   * 
   * @return this view's font.
   */
  Font getFont();

  /**
   * Gets the foreground color of this view.
   * 
   * @return this view's foreground color.
   */
  Color getForeground();
}
