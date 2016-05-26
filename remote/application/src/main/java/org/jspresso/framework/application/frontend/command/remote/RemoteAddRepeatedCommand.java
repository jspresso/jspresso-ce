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

import org.jspresso.framework.gui.remote.RComponent;

/**
 * This command is used to add repeated views in a remote repeater.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteAddRepeatedCommand extends RemoteCommand {

  private static final long serialVersionUID = -3157446085372670892L;

  private RComponent[] newSections;

  /**
   * Get new sections r component [ ].
   *
   * @return the r component [ ]
   */
  public RComponent[] getNewSections() {
    return newSections;
  }

  /**
   * Sets new sections.
   *
   * @param newSections
   *     the new sections
   */
  public void setNewSections(RComponent[] newSections) {
    this.newSections = newSections;
  }
}
