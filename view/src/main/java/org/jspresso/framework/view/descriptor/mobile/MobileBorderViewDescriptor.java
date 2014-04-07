/*
 * Copyright (c) 2005-2014 Vincent Vandenschrick. All rights reserved.
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
import org.jspresso.framework.view.descriptor.IViewDescriptor;
import org.jspresso.framework.view.descriptor.basic.BasicBorderViewDescriptor;

/**
 * A composite view descriptor that aggregates mobile views.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision$
 */
public class MobileBorderViewDescriptor extends BasicBorderViewDescriptor
    implements IMobilePageSectionViewDescriptor, IMobilePageAware {

  private IDisplayableAction enterAction;
  private IDisplayableAction backAction;
  private IDisplayableAction mainAction;
  private IDisplayableAction pageEndAction;

  /**
   *  Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setWestViewDescriptor(IViewDescriptor westViewDescriptor) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
  }

  /**
   *  Not supported in mobile environment.
   * <p>
   * {@inheritDoc}
   */
  @Override
  public void setEastViewDescriptor(IViewDescriptor eastViewDescriptor) {
    throw new UnsupportedOperationException("Not supported in mobile environment.");
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

}