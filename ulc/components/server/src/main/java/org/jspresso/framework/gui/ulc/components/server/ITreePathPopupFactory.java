/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.ulc.components.server;

import com.ulcjava.base.application.ULCPopupMenu;
import com.ulcjava.base.application.tree.TreePath;

/**
 * This is a popup-menu factory for treepath-based popup menus.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface ITreePathPopupFactory {

  /**
   * Creates a popup menu for the treepath.
   * 
   * @param path
   *            the treepath to create the popupo menu for.
   * @return the created popup menu or null if none.
   */
  ULCPopupMenu createPopupForTreepath(TreePath path);
}
