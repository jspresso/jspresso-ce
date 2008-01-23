/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package com.d2s.framework.gui.ulc.components.shared;

/**
 * Constants shared by ULCExtendedTree and UIExtendedTree.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
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
