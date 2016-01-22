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

/**
 * Updates the application name and description.
 *
 * @author Vincent Vandenschrick
 * @version $LastChangedRevision : 7847 $
 */
public class RemoteApplicationDescriptionCommand extends RemoteCommand {

  private static final long serialVersionUID = -5667477467232307515L;
  private String applicationName;
  private String applicationDescription;

  /**
   * Gets application name.
   *
   * @return the application name
   */
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * Sets application name.
   *
   * @param applicationName
   *     the application name
   */
  public void setApplicationName(String applicationName) {
    this.applicationName = applicationName;
  }

  /**
   * Gets application description.
   *
   * @return the application description
   */
  public String getApplicationDescription() {
    return applicationDescription;
  }

  /**
   * Sets application description.
   *
   * @param applicationDescription
   *     the application description
   */
  public void setApplicationDescription(String applicationDescription) {
    this.applicationDescription = applicationDescription;
  }
}
