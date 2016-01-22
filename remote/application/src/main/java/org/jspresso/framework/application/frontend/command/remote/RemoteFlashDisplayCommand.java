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
 * A command to trigger a modal remote dialog pop-up to display a flash movie.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteFlashDisplayCommand extends RemoteAbstractDialogCommand {

  private static final long serialVersionUID = -5032857814132356840L;

  private String[]          paramNames;
  private String[]          paramValues;
  private String            swfUrl;

  /**
   * Gets the paramNames.
   *
   * @return the paramNames.
   */
  public String[] getParamNames() {
    return paramNames;
  }

  /**
   * Gets the paramValues.
   *
   * @return the paramValues.
   */
  public String[] getParamValues() {
    return paramValues;
  }

  /**
   * Gets the swfUrl.
   *
   * @return the swfUrl.
   */
  public String getSwfUrl() {
    return swfUrl;
  }

  /**
   * Sets the paramNames.
   *
   * @param paramNames
   *          the paramNames to set.
   */
  public void setParamNames(String... paramNames) {
    this.paramNames = paramNames;
  }

  /**
   * Sets the paramValues.
   *
   * @param paramValues
   *          the paramValues to set.
   */
  public void setParamValues(String... paramValues) {
    this.paramValues = paramValues;
  }

  /**
   * Sets the swfUrl.
   *
   * @param swfUrl
   *          the swfUrl to set.
   */
  public void setSwfUrl(String swfUrl) {
    this.swfUrl = swfUrl;
  }
}
