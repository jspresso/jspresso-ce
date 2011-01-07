/*
 * Copyright (c) 2005-2011 Vincent Vandenschrick. All rights reserved.
 */
package org.jspresso.framework.gui.ulc.components.server.event;

import org.jspresso.framework.gui.ulc.components.shared.ExtendedTreeConstants;

import com.ulcjava.base.application.event.IEventListener;
import com.ulcjava.base.application.event.TreeExpansionEvent;
import com.ulcjava.base.application.tree.TreePath;

/**
 * Event for ExtendedTree.
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ExtendedTreeExpansionEvent extends TreeExpansionEvent {

  private static final long serialVersionUID = -6046904351013523291L;

  /**
   * Constructs a new <code>ExtendedTreeExpansionEvent</code> instance.
   * 
   * @param source
   *            the event source.
   * @param id
   *            the event id.
   * @param path
   *            the tree path.
   */
  public ExtendedTreeExpansionEvent(Object source, int id, TreePath path) {
    super(source, id, path);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispatch(IEventListener listener) {
    if (listener instanceof IExtendedTreeWillExpandListener) {
      IExtendedTreeWillExpandListener extendedTreeWillExpandListener = (IExtendedTreeWillExpandListener) listener;

      if (getId() == ExtendedTreeConstants.EXTENDED_TREE_WILL_EXPAND) {

        extendedTreeWillExpandListener.treeWillExpand(this);
      } else if (getId() == ExtendedTreeConstants.EXTENDED_TREE_WILL_COLLAPSE) {

        extendedTreeWillExpandListener.treeWillCollapse(this);
      } else {
        throw new IllegalArgumentException("Illegal event id : " + getId());
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getCategory() {
    return ExtendedTreeConstants.EXTENDED_TREE_EXPANSION_EVENT;
  }

}
