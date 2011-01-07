/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.ulc.components.server.event;

import com.ulcjava.base.application.event.IEventListener;

/**
 * The extended tree listener.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public interface IExtendedTreeWillExpandListener extends IEventListener {

  /**
   * Whenever the tree will be collapsed.
   * 
   * @param event
   *            the event.
   */
  void treeWillCollapse(ExtendedTreeExpansionEvent event);

  /**
   * Whenever the tree will be expanded.
   * 
   * @param event
   *            the event.
   */
  void treeWillExpand(ExtendedTreeExpansionEvent event);
}
