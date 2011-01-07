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

import java.io.Serializable;

/**
 * A remote command to pilot the remote frontend.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class RemoteCommand implements Serializable {

  private static final long serialVersionUID = 4102568065040838006L;

  private String            automationId;
  private String            targetPeerGuid;

  /**
   * Gets the automationId.
   * 
   * @return the automationId.
   */
  public String getAutomationId() {
    return automationId;
  }

  /**
   * Gets the targetPeerGuid.
   * 
   * @return the targetPeerGuid.
   */
  public String getTargetPeerGuid() {
    return targetPeerGuid;
  }

  /**
   * Sets the automationId.
   * 
   * @param automationId
   *          the automationId to set.
   */
  public void setAutomationId(String automationId) {
    this.automationId = automationId;
  }

  /**
   * Sets the targetPeerGuid.
   * 
   * @param targetPeerGuid
   *          the targetPeerGuid to set.
   */
  public void setTargetPeerGuid(String targetPeerGuid) {
    this.targetPeerGuid = targetPeerGuid;
  }

}
