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
package org.jspresso.framework.application.frontend.command.remote;

import org.jspresso.framework.gui.remote.RAction;
import org.jspresso.framework.util.gui.Dimension;

/**
 * Base class to dialog pop-up based commands.
 *
 * @author Vincent Vandenschrick
 */
public abstract class RemoteAbstractDialogCommand extends RemoteCommand {

  private static final long serialVersionUID = -5918787333137992725L;

  private RAction[]         actions;
  private Dimension         dimension;
  private String            title;
  private boolean           useCurrent;

  /**
   * Gets the actions.
   *
   * @return the actions.
   */
  public RAction[] getActions() {
    return actions;
  }

  /**
   * Gets the dimension.
   *
   * @return the dimension.
   */
  public Dimension getDimension() {
    return dimension;
  }

  /**
   * Gets the title.
   *
   * @return the title.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets the useCurrent.
   *
   * @return the useCurrent.
   */
  public boolean isUseCurrent() {
    return useCurrent;
  }

  /**
   * Sets the actions.
   *
   * @param actions
   *          the actions to set.
   */
  public void setActions(RAction... actions) {
    this.actions = actions;
  }

  /**
   * Sets the dimension.
   *
   * @param dimension
   *          the dimension to set.
   */
  public void setDimension(Dimension dimension) {
    this.dimension = dimension;
  }

  /**
   * Sets the title.
   *
   * @param title
   *          the title to set.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Sets the useCurrent.
   *
   * @param useCurrent
   *          the useCurrent to set.
   */
  public void setUseCurrent(boolean useCurrent) {
    this.useCurrent = useCurrent;
  }
}
