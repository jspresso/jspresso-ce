/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
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

/**
 * A container. Its children are indexed by their view names.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class RContainer extends RComponent {

  private static final long serialVersionUID = -7174072538766465667L;

  /**
   * Constructs a new <code>RContainer</code> instance. Only used for GWT
   * serialization support.
   */
  protected RContainer() {
    // For GWT support
  }

  /**
   * Constructs a new <code>RContainer</code> instance.
   * 
   * @param guid
   *          the guid
   */
  public RContainer(String guid) {
    super(guid);
  }

}
