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
 * A command to notify of table columns reordering / resize.
 *
 * @author Vincent Vandenschrick
 */
public class RemoteMapChangedCommand extends RemoteCommand {

  private static final long serialVersionUID = -1034273084659844092L;

  private String mapId;
  private Integer zoom;

  /**
   * Gets map id.
   *
   * @return the map id
   */
  public String getMapId() {
    return mapId;
  }

  /**
   * Sets map id.
   *
   * @param mapId
   *     the map id
   */
  public void setMapId(String mapId) {
    this.mapId = mapId;
  }

  /**
   * Gets zoom.
   *
   * @return the zoom
   */
  public Integer getZoom() {
    return zoom;
  }

  /**
   * Sets zoom.
   *
   * @param zoom
   *     the zoom
   */
  public void setZoom(Integer zoom) {
    this.zoom = zoom;
  }
}
