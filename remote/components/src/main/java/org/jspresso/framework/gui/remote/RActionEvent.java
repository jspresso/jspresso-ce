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
package org.jspresso.framework.gui.remote;

import java.io.Serializable;

/**
 * A remote action event.
 *
 * @author Vincent Vandenschrick
 */
public class RActionEvent implements Serializable {

  private static final long serialVersionUID = -4440788717317944053L;

  private String            actionCommand;
  private String            viewStatePermId;
  private String            viewStateGuid;

  /**
   * Gets the parameter.
   *
   * @return the parameter.
   */
  public String getActionCommand() {
    return actionCommand;
  }

  /**
   * Sets the parameter.
   *
   * @param parameter
   *          the parameter to set.
   */
  public void setActionCommand(String parameter) {
    this.actionCommand = parameter;
  }

  /**
   * Gets the viewStatePermId.
   *
   * @return the viewStatePermId.
   */
  public String getViewStatePermId() {
    return viewStatePermId;
  }

  /**
   * Gets the viewStateGuid.
   *
   * @return the viewStateGuid.
   */
  public String getViewStateGuid() {
    return viewStateGuid;
  }

  /**
   * Sets the viewStatePermId.
   *
   * @param viewStatePermId
   *          the viewStatePermId to set.
   */
  public void setViewStatePermId(String viewStatePermId) {
    this.viewStatePermId = viewStatePermId;
  }

  /**
   * Sets the viewStateGuid.
   *
   * @param viewStateGuid
   *          the viewStateGuid to set.
   */
  public void setViewStateGuid(String viewStateGuid) {
    this.viewStateGuid = viewStateGuid;
  }
}
