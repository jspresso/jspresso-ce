/*
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.wings.components;

import java.util.EventListener;

/**
 * The listener inteface for table header click listeners.
 * <p>
 * Copyright (c) 2005-2009 Vincent Vandenschrick. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface HeaderClickListener extends EventListener {

  /**
   * Called when an <code>HeaderClickEvent</code> occurs on a STable.
   * 
   * @param event
   *          the <code>HeaderClickEvent</code>.
   */
  void headerClicked(HeaderClickEvent event);
}
