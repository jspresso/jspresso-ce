/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.view;

import java.awt.Dimension;

/**
 * A factory for icons.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
