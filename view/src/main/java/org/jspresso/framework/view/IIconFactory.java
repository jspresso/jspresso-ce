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
package org.jspresso.framework.view;

import org.jspresso.framework.util.gui.Dimension;
import org.jspresso.framework.util.gui.Icon;

/**
 * A factory for icons.
 *
 * @author Vincent Vandenschrick
 * @param <E>
 *          the actual icon class created.
 */
public interface IIconFactory<E> {

  /**
   * Creates an icon from an image url or get it from a local cache.
   *
   * @param urlSpec
   *          the url of the image to be used on the icon.
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getIcon(String urlSpec, Dimension iconSize);

  /**
   * Creates an icon from the image url provided by an icon or get it from a
   * local cache. Using an icon allows to specify an icon dimension that will
   * override, if not null, the icon size passed as parameter.
   *
   * @param icon
   *          the icon.
   * @param iconSize
   *          the size of the constructed icon if the icon provider does not
   *          specify one. The image will be resized if necessary to match the
   *          requested size.
   * @return the constructed icon.
   */
  E getIcon(Icon icon, Dimension iconSize);

  /**
   * Gets large icon size.
   *
   * @return large icon size.
   */
  Dimension getLargeIconSize();

  /**
   * Gets medium icon size.
   *
   * @return medium icon size.
   */
  Dimension getMediumIconSize();

  /**
   * Gets small icon size.
   *
   * @return small icon size.
   */
  Dimension getSmallIconSize();

  /**
   * Gets tiny icon size.
   *
   * @return tiny icon size.
   */
  Dimension getTinyIconSize();

  /**
   * Gets the iconSet.
   *
   * @return the iconSet.
   */
  IIconSet getIconSet();

  /**
   * Gets the backwardIconImageURL. Delegates to the icon set.
   *
   * @return the backwardIconImageURL.
   */
  String getBackwardIconImageURL();

  /**
   * Gets the cancelIconImageURL. Delegates to the icon set.
   *
   * @return the cancelIconImageURL.
   */
  String getCancelIconImageURL();

  /**
   * Gets the downIconImageURL. Delegates to the icon set.
   *
   * @return the downIconImageURL.
   */
  String getDownIconImageURL();

  /**
   * Gets the errorIconImageURL. Delegates to the icon set.
   *
   * @return the errorIconImageURL.
   */
  String getErrorIconImageURL();

  /**
   * Gets the forbiddenIconImageURL. Delegates to the icon set.
   *
   * @return the forbiddenIconImageURL.
   */
  String getForbiddenIconImageURL();

  /**
   * Gets the forwardIconImageURL. Delegates to the icon set.
   *
   * @return the forwardIconImageURL.
   */
  String getForwardIconImageURL();

  /**
   * Gets the infoIconImageURL. Delegates to the icon set.
   *
   * @return the infoIconImageURL.
   */
  String getInfoIconImageURL();

  /**
   * Gets the noIconImageURL. Delegates to the icon set.
   *
   * @return the noIconImageURL.
   */
  String getNoIconImageURL();

  /**
   * Gets the okYesIconImageURL. Delegates to the icon set.
   *
   * @return the okYesIconImageURL.
   */
  String getOkYesIconImageURL();

  /**
   * Gets the questionIconImageURL. Delegates to the icon set.
   *
   * @return the questionIconImageURL.
   */
  String getQuestionIconImageURL();

  /**
   * Gets the upIconImageURL. Delegates to the icon set.
   *
   * @return the upIconImageURL.
   */
  String getUpIconImageURL();

  /**
   * Gets the warningIconImageURL. Delegates to the icon set.
   *
   * @return the warningIconImageURL.
   */
  String getWarningIconImageURL();

  /**
   * Gets the standard Back icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getBackwardIcon(Dimension iconSize);

  /**
   * Gets the standard cancel icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getCancelIcon(Dimension iconSize);

  /**
   * Gets the standard down icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getDownIcon(Dimension iconSize);

  /**
   * Gets the standard error icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getErrorIcon(Dimension iconSize);

  /**
   * Gets the standard forbidden icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getForbiddenIcon(Dimension iconSize);

  /**
   * Gets the standard Forward icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getForwardIcon(Dimension iconSize);

  /**
   * Gets the standard info icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getInfoIcon(Dimension iconSize);

  /**
   * Gets the standard no icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getNoIcon(Dimension iconSize);

  /**
   * Gets the standard ok / yes icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getOkYesIcon(Dimension iconSize);

  /**
   * Gets the standard question icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getQuestionIcon(Dimension iconSize);

  /**
   * Gets the standard up icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getUpIcon(Dimension iconSize);

  /**
   * Gets the standard warning icon.
   *
   * @param iconSize
   *          the size of the constructed icon. The image will be resized if
   *          necessary to match the requested size.
   * @return the constructed icon.
   */
  E getWarningIcon(Dimension iconSize);
}
