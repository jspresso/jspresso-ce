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
 * A command to update writability of a remote peer.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteWritabilityCommand extends RemoteCommand {

  private static final long serialVersionUID = 5690328483689434631L;

  private boolean           writable;

  /**
   * Gets the writable.
   *
   * @return the writable.
   */
  public boolean isWritable() {
    return writable;
  }

  /**
   * Sets the writable.
   *
   * @param writable
   *          the writable to set.
   */
  public void setWritable(boolean writable) {
    this.writable = writable;
  }

}
