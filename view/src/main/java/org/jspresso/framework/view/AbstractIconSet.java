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


/**
 * Abstract base class for icon set.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractIconSet implements IIconSet {

  /**
   * Gets the backwardIconImageURL.
   * 
   * @return the backwardIconImageURL.
   */
  @Override
  public String getBackwardIconImageURL() {
    return getIconImageUrl(BACKWARD_ICON, null);
  }

  /**
   * Gets the cancelIconImageURL.
   * 
   * @return the cancelIconImageURL.
   */
  @Override
  public String getCancelIconImageURL() {
    return getIconImageUrl(CANCEL_ICON, null);
  }

  /**
   * Gets the downIconImageURL.
   * 
   * @return the downIconImageURL.
   */
  @Override
  public String getDownIconImageURL() {
    return getIconImageUrl(DOWN_ICON, null);
  }

  /**
   * Gets the errorIconImageURL.
   * 
   * @return the errorIconImageURL.
   */
  @Override
  public String getErrorIconImageURL() {
    return getIconImageUrl(ERROR_ICON, null);
  }

  /**
   * Gets the forbiddenIconImageURL.
   * 
   * @return the forbiddenIconImageURL.
   */
  @Override
  public String getForbiddenIconImageURL() {
    return getIconImageUrl(FORBIDDEN_ICON, null);
  }

  /**
   * Gets the forwardIconImageURL.
   * 
   * @return the forwardIconImageURL.
   */
  @Override
  public String getForwardIconImageURL() {
    return getIconImageUrl(CANCEL_ICON, null);
  }

  /**
   * Gets the infoIconImageURL.
   * 
   * @return the infoIconImageURL.
   */
  @Override
  public String getInfoIconImageURL() {
    return getIconImageUrl(INFO_ICON, null);
  }

  /**
   * Gets the noIconImageURL.
   * 
   * @return the noIconImageURL.
   */
  @Override
  public String getNoIconImageURL() {
    return getIconImageUrl(NO_ICON, null);
  }

  /**
   * Gets the okYesIconImageURL.
   * 
   * @return the okYesIconImageURL.
   */
  @Override
  public String getOkYesIconImageURL() {
    return getIconImageUrl(OK_YES_ICON, null);
  }

  /**
   * Gets the questionIconImageURL.
   * 
   * @return the questionIconImageURL.
   */
  @Override
  public String getQuestionIconImageURL() {
    return getIconImageUrl(QUESTION_ICON, null);
  }

  /**
   * Gets the upIconImageURL.
   * 
   * @return the upIconImageURL.
   */
  @Override
  public String getUpIconImageURL() {
    return getIconImageUrl(UP_ICON, null);
  }

  /**
   * Gets the warningIconImageURL.
   * 
   * @return the warningIconImageURL.
   */
  @Override
  public String getWarningIconImageURL() {
    return getIconImageUrl(WARNING_ICON, null);
  }
}
