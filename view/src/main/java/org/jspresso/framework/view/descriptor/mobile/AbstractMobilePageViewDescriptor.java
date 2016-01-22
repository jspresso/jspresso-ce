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

import java.util.List;

import org.jspresso.framework.view.action.IDisplayableAction;
import org.jspresso.framework.view.descriptor.EPosition;
import org.jspresso.framework.view.descriptor.basic.BasicCompositeViewDescriptor;

/**
 * Abstract base class for mobile page view descriptors.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractMobilePageViewDescriptor extends BasicCompositeViewDescriptor
    implements IMobilePageViewDescriptor {

  private IDisplayableAction enterAction;
  private IDisplayableAction backAction;
  private IDisplayableAction mainAction;
  private IDisplayableAction pageEndAction;
  private IDisplayableAction swipeLeftAction;
  private IDisplayableAction swipeRightAction;
  private EPosition          position;
  private List<String>       forClientTypes;
  private String             i18nName;
  private String             i18nDescription;

  /**
   * Instantiates a new Mobile border view descriptor.
   */
  public AbstractMobilePageViewDescriptor() {
    this.position = EPosition.RIGHT;
  }

  /**
   * Gets enter action.
   *
   * @return the enter action
   */
  @Override
  public IDisplayableAction getEnterAction() {
    return enterAction;
  }

  /**
   * Sets enter action.
   *
   * @param enterAction the enter action
   */
  @Override
  public void setEnterAction(IDisplayableAction enterAction) {
    this.enterAction = enterAction;
  }

  /**
   * Gets back action.
   *
   * @return the back action
   */
  @Override
  public IDisplayableAction getBackAction() {
    return backAction;
  }

  /**
   * Sets back action.
   *
   * @param backAction the back action
   */
  @Override
  public void setBackAction(IDisplayableAction backAction) {
    this.backAction = backAction;
  }

  /**
   * Gets main action.
   *
   * @return the main action
   */
  @Override
  public IDisplayableAction getMainAction() {
    return mainAction;
  }

  /**
   * Sets main action.
   *
   * @param mainAction the main action
   */
  @Override
  public void setMainAction(IDisplayableAction mainAction) {
    this.mainAction = mainAction;
  }


  /**
   * Gets page end action.
   *
   * @return the page end action
   */
  @Override
  public IDisplayableAction getPageEndAction() {
    return pageEndAction;
  }

  /**
   * Sets page end action.
   *
   * @param pageEndAction the page end action
   */
  @Override
  public void setPageEndAction(IDisplayableAction pageEndAction) {
    this.pageEndAction = pageEndAction;
  }

  /**
   * Gets swipe left action.
   *
   * @return the swipe left action
   */
  @Override
  public IDisplayableAction getSwipeLeftAction() {
    return swipeLeftAction;
  }

  /**
   * Sets swipe left action.
   *
   * @param swipeLeftAction the swipe left action
   */
  @Override
  public void setSwipeLeftAction(IDisplayableAction swipeLeftAction) {
    this.swipeLeftAction = swipeLeftAction;
  }

  /**
   * Gets swipe right action.
   *
   * @return the swipe right action
   */
  @Override
  public IDisplayableAction getSwipeRightAction() {
    return swipeRightAction;
  }

  /**
   * Sets swipe right action.
   *
   * @param swipeRightAction the swipe right action
   */
  @Override
  public void setSwipeRightAction(IDisplayableAction swipeRightAction) {
    this.swipeRightAction = swipeRightAction;
  }

  /**
   * Gets position.
   *
   * @return the position
   */
  @Override
  public EPosition getPosition() {
    return position;
  }

  /**
   * Sets position.
   *
   * @param position
   *     the position
   */
  public void setPosition(EPosition position) {
    this.position = position;
  }

  /**
   * Gets for client types.
   *
   * @return the for client types
   */
  @Override
  public List<String> getForClientTypes() {
    return forClientTypes;
  }

  /**
   * Sets for client types.
   *
   * @param forClientTypes the for client types
   */
  public void setForClientTypes(List<String> forClientTypes) {
    this.forClientTypes = forClientTypes;
  }

  /**
   * Gets i 18 n name.
   *
   * @return the i 18 n name
   */
  public String getI18nName() {
    return i18nName;
  }

  /**
   * Sets i 18 n name.
   *
   * @param i18nName the i 18 n name
   */
  public void setI18nName(String i18nName) {
    this.i18nName = i18nName;
  }

  /**
   * Gets i 18 n description.
   *
   * @return the i 18 n description
   */
  public String getI18nDescription() {
    return i18nDescription;
  }

  /**
   * Sets i 18 n description.
   *
   * @param i18nDescription the i 18 n description
   */
  public void setI18nDescription(String i18nDescription) {
    this.i18nDescription = i18nDescription;
  }
}
