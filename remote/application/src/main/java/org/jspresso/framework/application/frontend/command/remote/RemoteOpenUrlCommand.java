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
 * This command opens an url in a new browser window.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteOpenUrlCommand extends RemoteCommand {

  private static final long serialVersionUID = -6590187375894054973L;

  private String            urlSpec;
  private String            target;

  /**
   * Gets the urlSpec.
   *
   * @return the urlSpec.
   */
  public String getUrlSpec() {
    return urlSpec;
  }

  /**
   * Sets the urlSpec.
   *
   * @param urlSpec
   *          the urlSpec to set.
   */
  public void setUrlSpec(String urlSpec) {
    this.urlSpec = urlSpec;
  }

  /**
   * Gets target.
   *
   * @return the target
   */
  public String getTarget() {
    return target;
  }

  /**
   * Sets target.
   *
   * @param target the target
   */
  public void setTarget(String target) {
    this.target = target;
  }
}
