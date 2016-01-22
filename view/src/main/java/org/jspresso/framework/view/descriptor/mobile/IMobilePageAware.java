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
package org.jspresso.framework.view.descriptor.mobile;

import org.jspresso.framework.view.action.IDisplayableAction;

/**
 * Interface for mobile view descriptors that are aware of page end events.
 *
 * @author Vincent Vandenschrick
 */
public interface IMobilePageAware {

  /**
   * Gets enter action. The enter action will be triggered when the user enters the page.
   *
   * @return the enter action
   */
  IDisplayableAction getEnterAction();

  /**
   * Sets enter action.
   *
   * @param enterAction the enter action
   */
  void setEnterAction(IDisplayableAction enterAction);

  /**
   * Gets back action. The back action will be triggered when the user requests back navigation.
   *
   * @return the back action
   */
  IDisplayableAction getBackAction();

  /**
   * Sets back action.
   *
   * @param backAction the back action
   */
  void setBackAction(IDisplayableAction backAction);

  /**
   * Gets main action. The main action will be displayed at the up right corner of the page.
   *
   * @return the main action
   */
  IDisplayableAction getMainAction();

  /**
   * Sets main action.
   *
   * @param mainAction the main action
   */
  void setMainAction(IDisplayableAction mainAction);

  /**
   * Gets page end action. The page end action will be triggered when the user scrolls the page to the end.
   *
   * @return the page end action
   */
  IDisplayableAction getPageEndAction();

  /**
   * Sets page end action.
   *
   * @param pageEndAction the page end action
   */
  void setPageEndAction(IDisplayableAction pageEndAction);

  /**
   * Gets swipe left action.
   *
   * @return the swipe left action
   */
  IDisplayableAction getSwipeLeftAction();

  /**
   * Sets swipe left action.
   *
   * @param swipeLeftAction
   *     the swipe left action
   */
  void setSwipeLeftAction(IDisplayableAction swipeLeftAction);

  /**
   * Gets swipe right action.
   *
   * @return the swipe right action
   */
  IDisplayableAction getSwipeRightAction();

  /**
   * Sets swipe right action.
   *
   * @param swipeRightAction
   *     the swipe right action
   */
  void setSwipeRightAction(IDisplayableAction swipeRightAction);
}
