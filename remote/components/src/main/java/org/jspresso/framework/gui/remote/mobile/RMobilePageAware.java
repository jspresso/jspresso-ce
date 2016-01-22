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
package org.jspresso.framework.gui.remote.mobile;

import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.gui.remote.RContainer;

/**
 * A mobile page.
 *
 * @author Vincent Vandenschrick
 */
public abstract class RMobilePageAware extends RContainer {

  private static final long serialVersionUID = 7539787721325728438L;

  private RAction enterAction;
  private RAction backAction;
  private RAction mainAction;
  private RAction pageEndAction;
  private RAction swipeLeftAction;
  private RAction swipeRightAction;

  /**
   * Constructs a new {@code RMobilePageAwareContainer} instance.
   *
   * @param guid
   *     the guid
   */
  public RMobilePageAware(String guid) {
    super(guid);
  }

  /**
   * Constructs a new {@code RMobilePageAware} instance. Only used for
   * serialization support.
   */
  public RMobilePageAware() {
    // For serialization support
  }

  /**
   * Gets enter action.
   *
   * @return the enter action
   */
  public RAction getEnterAction() {
    return enterAction;
  }

  /**
   * Sets enter action.
   *
   * @param enterAction the enter action
   */
  public void setEnterAction(RAction enterAction) {
    this.enterAction = enterAction;
  }

  /**
   * Gets back action.
   *
   * @return the back action
   */
  public RAction getBackAction() {
    return backAction;
  }

  /**
   * Sets back action.
   *
   * @param backAction
   *     the back action
   */
  public void setBackAction(RAction backAction) {
    this.backAction = backAction;
  }

  /**
   * Gets main action.
   *
   * @return the main action
   */
  public RAction getMainAction() {
    return mainAction;
  }

  /**
   * Sets main action.
   *
   * @param mainAction
   *     the main action
   */
  public void setMainAction(RAction mainAction) {
    this.mainAction = mainAction;
  }

  /**
   * Gets page end action.
   *
   * @return the page end action
   */
  public RAction getPageEndAction() {
    return pageEndAction;
  }

  /**
   * Sets page end action.
   *
   * @param pageEndAction the page end action
   */
  public void setPageEndAction(RAction pageEndAction) {
    this.pageEndAction = pageEndAction;
  }

  /**
   * Gets swipe left action.
   *
   * @return the swipe left action
   */
  public RAction getSwipeLeftAction() {
    return swipeLeftAction;
  }

  /**
   * Sets swipe left action.
   *
   * @param swipeLeftAction the swipe left action
   */
  public void setSwipeLeftAction(RAction swipeLeftAction) {
    this.swipeLeftAction = swipeLeftAction;
  }

  /**
   * Gets swipe right action.
   *
   * @return the swipe right action
   */
  public RAction getSwipeRightAction() {
    return swipeRightAction;
  }

  /**
   * Sets swipe right action.
   *
   * @param swipeRightAction the swipe right action
   */
  public void setSwipeRightAction(RAction swipeRightAction) {
    this.swipeRightAction = swipeRightAction;
  }
}
