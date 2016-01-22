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
package org.jspresso.framework.application.frontend.command.remote.mobile;

import org.jspresso.framework.application.frontend.command.remote.RemoteCommand;
import org.jspresso.framework.gui.remote.RAction;

/**
 * A command to animate current page.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 5621 $
 */
public class RemoteAnimationCommand extends RemoteCommand {

  private static final long serialVersionUID = -4636275718017683944L;

  private String  animation;
  private String  direction;
  private boolean reverse;
  private int     duration;
  private boolean hideView;
  private RAction callbackAction;

  /**
   * Gets animation.
   *
   * @return the animation
   */
  public String getAnimation() {
    return animation;
  }

  /**
   * Sets animation.
   *
   * @param animation the animation
   */
  public void setAnimation(String animation) {
    this.animation = animation;
  }

  /**
   * Gets direction.
   *
   * @return the direction
   */
  public String getDirection() {
    return direction;
  }

  /**
   * Sets direction.
   *
   * @param direction the direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Is reverse.
   *
   * @return the boolean
   */
  public boolean isReverse() {
    return reverse;
  }

  /**
   * Sets reverse.
   *
   * @param reverse the reverse
   */
  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

  /**
   * Gets duration.
   *
   * @return the duration
   */
  public int getDuration() {
    return duration;
  }

  /**
   * Sets duration.
   *
   * @param duration the duration
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Is hide view.
   *
   * @return the boolean
   */
  public boolean isHideView() {
    return hideView;
  }

  /**
   * Sets hide view.
   *
   * @param hideView the hide view
   */
  public void setHideView(boolean hideView) {
    this.hideView = hideView;
  }

  /**
   * Gets callback action.
   *
   * @return the callback action
   */
  public RAction getCallbackAction() {
    return callbackAction;
  }

  /**
   * Sets callback action.
   *
   * @param callbackAction the callback action
   */
  public void setCallbackAction(RAction callbackAction) {
    this.callbackAction = callbackAction;
  }
}
