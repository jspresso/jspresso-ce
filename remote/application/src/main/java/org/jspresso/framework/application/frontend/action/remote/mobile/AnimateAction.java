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
package org.jspresso.framework.application.frontend.action.remote.mobile;

import java.util.Map;

import org.jspresso.framework.action.IAction;
import org.jspresso.framework.action.IActionHandler;
import org.jspresso.framework.application.frontend.action.remote.AbstractRemoteAction;
import org.jspresso.framework.application.frontend.controller.remote.mobile.MobileRemoteController;
import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.gui.remote.RComponent;
import org.jspresso.framework.view.IView;

/**
 * Selects animates current page.
 *
 * @author Vincent Vandenschrick
 */
public class AnimateAction extends AbstractRemoteAction {

  private String  animation;
  private String  direction;
  private boolean reverse;
  private boolean hideView;
  private int     duration;
  private IAction callbackAction;

  /**
   * Gets animation.
   *
   * @return the animation
   */
  protected String getAnimation() {
    return animation;
  }

  /**
   * Sets animation.
   *
   * @param animation
   *     the animation
   */
  public void setAnimation(String animation) {
    this.animation = animation;
  }

  /**
   * Gets direction.
   *
   * @return the direction
   */
  protected String getDirection() {
    return direction;
  }

  /**
   * Sets direction.
   *
   * @param direction
   *     the direction
   */
  public void setDirection(String direction) {
    this.direction = direction;
  }

  /**
   * Is reverse.
   *
   * @return the boolean
   */
  protected boolean isReverse() {
    return reverse;
  }

  /**
   * Sets reverse.
   *
   * @param reverse
   *     the reverse
   */
  public void setReverse(boolean reverse) {
    this.reverse = reverse;
  }

  /**
   * Gets callback action.
   *
   * @return the callback action
   */
  public IAction getCallbackAction() {
    return callbackAction;
  }

  /**
   * Sets callback action.
   *
   * @param callbackAction
   *     the callback action
   */
  public void setCallbackAction(IAction callbackAction) {
    this.callbackAction = callbackAction;
  }

  /**
   * Gets duration.
   *
   * @return the duration
   */
  protected int getDuration() {
    return duration;
  }

  /**
   * Sets duration.
   *
   * @param duration
   *     the duration
   */
  public void setDuration(int duration) {
    this.duration = duration;
  }

  /**
   * Is hide view.
   *
   * @return the boolean
   */
  protected boolean isHideView() {
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
   * {@inheritDoc}
   */
  @Override
  public boolean execute(IActionHandler actionHandler, Map<String, Object> context) {
    RAction callbackRAction = null;
    if (getCallbackAction() != null) {
      IView<RComponent> currentView = getView(context);
      callbackRAction = getActionFactory(context).createAction(getCallbackAction(), actionHandler, currentView,
          getLocale(context));
      callbackRAction.putValue(IAction.STATIC_CONTEXT_KEY, getUiAction(context).getValue(IAction.STATIC_CONTEXT_KEY));
    }
    ((MobileRemoteController) getController(context)).animatePage(null, getAnimation(), getDirection(), isReverse(),
        getDuration(), isHideView(), callbackRAction);
    return super.execute(actionHandler, context);
  }
}
