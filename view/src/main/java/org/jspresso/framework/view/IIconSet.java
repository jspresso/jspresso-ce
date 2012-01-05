/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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

/**
 * Defines the contract of an icon set.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IIconSet {

  /**
   * <code>BACKWARD_ICON</code>.
   */
  String       BACKWARD_ICON  = "BACKWARD_ICON";
  /**
   * <code>CANCEL_ICON</code>.
   */
  String       CANCEL_ICON    = "CANCEL_ICON";
  /**
   * <code>DOWN_ICON</code>.
   */
  String       DOWN_ICON      = "DOWN_ICON";
  /**
   * <code>ERROR_ICON</code>.
   */
  String       ERROR_ICON     = "ERROR_ICON";
  /**
   * <code>FORBIDDEN_ICON</code>.
   */
  String       FORBIDDEN_ICON = "FORBIDDEN_ICON";
  /**
   * <code>FORWARD_ICON</code>.
   */
  String       FORWARD_ICON   = "FORWARD_ICON";
  /**
   * <code>INFO_ICON</code>.
   */
  String       INFO_ICON      = "INFO_ICON";
  /**
   * <code>NO_ICON</code>.
   */
  String       NO_ICON        = "NO_ICON";
  /**
   * <code>OK_YES_ICON</code>.
   */
  String       OK_YES_ICON    = "OK_YES_ICON";
  /**
   * <code>QUESTION_ICON</code>.
   */
  String       QUESTION_ICON  = "QUESTION_ICON";
  /**
   * <code>UP_ICON</code>.
   */
  String       UP_ICON        = "UP_ICON";
  /**
   * <code>WARNING_ICON</code>.
   */
  String       WARNING_ICON   = "WARNING_ICON";

  /**
   * Gets the backwardIconImageURL.
   * 
   * @return the backwardIconImageURL.
   */
  String getBackwardIconImageURL();

  /**
   * Gets the cancelIconImageURL.
   * 
   * @return the cancelIconImageURL.
   */
  String getCancelIconImageURL();

  /**
   * Gets the downIconImageURL.
   * 
   * @return the downIconImageURL.
   */
  String getDownIconImageURL();

  /**
   * Gets the errorIconImageURL.
   * 
   * @return the errorIconImageURL.
   */
  String getErrorIconImageURL();

  /**
   * Gets the forbiddenIconImageURL.
   * 
   * @return the forbiddenIconImageURL.
   */
  String getForbiddenIconImageURL();

  /**
   * Gets the forwardIconImageURL.
   * 
   * @return the forwardIconImageURL.
   */
  String getForwardIconImageURL();

  /**
   * Gets the infoIconImageURL.
   * 
   * @return the infoIconImageURL.
   */
  String getInfoIconImageURL();

  /**
   * Gets the noIconImageURL.
   * 
   * @return the noIconImageURL.
   */
  String getNoIconImageURL();

  /**
   * Gets the okYesIconImageURL.
   * 
   * @return the okYesIconImageURL.
   */
  String getOkYesIconImageURL();

  /**
   * Gets the questionIconImageURL.
   * 
   * @return the questionIconImageURL.
   */
  String getQuestionIconImageURL();

  /**
   * Gets the upIconImageURL.
   * 
   * @return the upIconImageURL.
   */
  String getUpIconImageURL();

  /**
   * Gets the warningIconImageURL.
   * 
   * @return the warningIconImageURL.
   */
  String getWarningIconImageURL();

  /**
   * Gets the icon image URL for the icon key passed as parameter.
   * 
   * @param iconNameOrKey
   *          the icon name or key to retrieve the icon for.
   * @param dim
   *          the dimension this iconImage is intended for. You can safely
   *          ignore this parameter, i.e. always return the same dimension
   *          image. In that case, Jspresso will resize the image at runtime.
   * @return the URL based on the icon determination strategy implemented by
   *         this icon set.
   */
  String getIconImageUrl(String iconNameOrKey, Dimension dim);
}
