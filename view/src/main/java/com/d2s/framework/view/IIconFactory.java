/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.view;

import java.awt.Dimension;

/**
 * A factory for icons.
 * <p>
 * Copyright 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 * @param <E>
 *            the actual icon class created.
 */
public interface IIconFactory<E> {

  /**
   * <code>LARGE_ICON_SIZE</code> is 48x48 dimension.
   */
  Dimension LARGE_ICON_SIZE  = new Dimension(48, 48);
  /**
   * <code>MEDIUM_ICON_SIZE</code> is 32x32 dimension.
   */
  Dimension MEDIUM_ICON_SIZE = new Dimension(32, 32);
  /**
   * <code>SMALL_ICON_SIZE</code> is 16x16 dimension.
   */
  Dimension SMALL_ICON_SIZE  = new Dimension(16, 16);
  /**
   * <code>SMALL_ICON_SIZE</code> is 16x16 dimension.
   */
  Dimension TINY_ICON_SIZE   = new Dimension(12, 12);

  /**
   * Gets the standard Back icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getBackwardIcon(Dimension iconSize);

  /**
   * Gets the standard cancel icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getCancelIcon(Dimension iconSize);

  /**
   * Gets the standard error icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getErrorIcon(Dimension iconSize);

  /**
   * Gets the standard forbidden icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getForbiddenIcon(Dimension iconSize);

  /**
   * Gets the standard Forward icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getForwardIcon(Dimension iconSize);

  /**
   * Creates an icon from an image url or get it from a local cache.
   * 
   * @param urlSpec
   *            the url of the image to be used on the icon.
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getIcon(String urlSpec, Dimension iconSize);

  /**
   * Gets the standard info icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getInfoIcon(Dimension iconSize);

  /**
   * Gets the standard no icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getNoIcon(Dimension iconSize);

  /**
   * Gets the standard ok / yes icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getOkYesIcon(Dimension iconSize);

  /**
   * Gets the standard warning icon.
   * 
   * @param iconSize
   *            the size of the constructed icon. The image will be resized if
   *            nacessary to match the requested size.
   * @return the constructed icon.
   */
  E getWarningIcon(Dimension iconSize);

}
