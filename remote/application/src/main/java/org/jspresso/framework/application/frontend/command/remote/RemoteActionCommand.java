/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
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

/**
 * A command to trigger a remote action.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RemoteActionCommand extends RemoteCommand {

  private static final long serialVersionUID = -3055572538206728529L;

  private String            parameter;
  private String            viewStatePermId;
  private String            viewStateGuid;

  /**
   * Gets the parameter.
   * 
   * @return the parameter.
   */
  public String getParameter() {
    return parameter;
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
   * Sets the parameter.
   * 
   * @param parameter
   *          the parameter to set.
   */
  public void setParameter(String parameter) {
    this.parameter = parameter;
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
