/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view.descriptor;

import java.awt.Color;
import java.awt.Font;
import java.util.Collection;

import com.d2s.framework.model.descriptor.IModelDescriptor;
import com.d2s.framework.security.ISecurable;
import com.d2s.framework.util.IGate;
import com.d2s.framework.util.descriptor.IIconDescriptor;
import com.d2s.framework.view.action.IActionable;

/**
 * This public interface is the super-interface of all view descriptors.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 *
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IViewDescriptor extends IIconDescriptor, IActionable, ISecurable {

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
   * Gets the foreground color of this view.
   *
   * @return this view's foreground color.
   */
  Color getForeground();

  /**
   * Gets the background color of this view.
   *
   * @return this view's foreground color.
   */
  Color getBackground();

  /**
   * Gets the font of this view.
   *
   * @return this view's font.
   */
  Font getFont();

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
   * Gets the model descriptor this view descriptor acts on.
   *
   * @return the view model descriptor.
   */
  IModelDescriptor getModelDescriptor();

  /**
   * Gets wether this view is read-only.
   *
   * @return true if the view is read-only.
   */
  boolean isReadOnly();

  /**
   * Gets the collection of gates determining the readability state of this
   * property.
   *
   * @return the collection of gates determining the readability state of this
   *         property.
   */
  Collection<IGate> getReadabilityGates();

  /**
   * Gets the collection of gates determining the writability state of this
   * property.
   *
   * @return the collection of gates determining the writability state of this
   *         property.
   */
  Collection<IGate> getWritabilityGates();
}
