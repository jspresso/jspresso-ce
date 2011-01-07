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
package org.jspresso.framework.gui.ulc.components.shared;

/**
 * Constants shared by ULCExtendedTree and UIExtendedTree.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public final class ExtendedTreeConstants {

  /**
   * <code>EXTENDED_TREE_EXPANSION_EVENT</code>.
   */
  public static final int    EXTENDED_TREE_EXPANSION_EVENT = 10002;

  /**
   * <code>EXTENDED_TREE_WILL_COLLAPSE</code>.
   */
  public static final int    EXTENDED_TREE_WILL_COLLAPSE   = 2;

  /**
   * <code>EXTENDED_TREE_WILL_EXPAND</code>.
   */
  public static final int    EXTENDED_TREE_WILL_EXPAND     = 1;

  // request constants
  /**
   * <code>PREPARE_POPUP_REQUEST</code>.
   */
  public static final String PREPARE_POPUP_REQUEST         = "preparePopup";

  // anything key constants
  /**
   * <code>ROW_KEY</code>.
   */
  public static final String ROW_KEY                       = "row";

  private ExtendedTreeConstants() {
    // Empty constructor for utility class
  }
}
