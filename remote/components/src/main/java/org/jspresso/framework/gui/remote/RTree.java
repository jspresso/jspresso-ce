/*
 * Copyright (c) 2005-2012 Vincent Vandenschrick. All rights reserved.
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
 * A remote tree component.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class RTree extends RComponent {

  private static final long serialVersionUID = 7886711955466326634L;

  private boolean           expanded;
  private RAction           rowAction;

  /**
   * Constructs a new <code>RTreeComponent</code> instance.
   * 
   * @param guid
   *          the guid.
   */
  public RTree(String guid) {
    super(guid);
  }

  /**
   * Constructs a new <code>RTree</code> instance. Only used for serialization
   * support.
   */
  public RTree() {
    // For serialization support
  }

  /**
   * Gets the rowAction.
   * 
   * @return the rowAction.
   */
  public RAction getRowAction() {
    return rowAction;
  }

  /**
   * Gets the expanded.
   * 
   * @return the expanded.
   */
  public boolean isExpanded() {
    return expanded;
  }

  /**
   * Sets the expanded.
   * 
   * @param expanded
   *          the expanded to set.
   */
  public void setExpanded(boolean expanded) {
    this.expanded = expanded;
  }

  /**
   * Sets the rowAction.
   * 
   * @param rowAction
   *          the rowAction to set.
   */
  public void setRowAction(RAction rowAction) {
    this.rowAction = rowAction;
  }
}
