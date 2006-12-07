/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.echo;

import nextapp.echo2.app.event.EventListenerList;
import echopointng.tree.TreeModel;
import echopointng.tree.TreeModelEvent;
import echopointng.tree.TreeModelListener;
import echopointng.tree.TreePath;

/**
 * Abstract class for tree models. It mainly manages listeners on the model.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTreeModel implements TreeModel {

  private EventListenerList listenerList = new EventListenerList();

  /**
   * Adds a listener for the TreeModelEvent posted after the tree changes.
   * 
   * @see #removeTreeModelListener
   * @param l
   *          the listener to add
   */
  public void addTreeModelListener(TreeModelListener l) {
    listenerList.addListener(TreeModelListener.class, l);
  }

  /**
   * Removes a listener previously added with <B>addTreeModelListener()</B>.
   * 
   * @see #addTreeModelListener
   * @param l
   *          the listener to remove
   */
  public void removeTreeModelListener(TreeModelListener l) {
    listenerList.removeListener(TreeModelListener.class, l);
  }

  /**
   * Returns an array of all the tree model listeners registered on this model.
   * 
   * @return all of this model's <code>TreeModelListener</code>s or an empty
   *         array if no tree model listeners are currently registered
   * @see #addTreeModelListener
   * @see #removeTreeModelListener
   */
  public TreeModelListener[] getTreeModelListeners() {
    return (TreeModelListener[]) listenerList
        .getListeners(TreeModelListener.class);
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @param source
   *          the node being changed
   * @param path
   *          the path to the root node
   * @param childIndices
   *          the indices of the changed elements
   * @param children
   *          the changed elements
   * @see EventListenerList
   */
  protected void fireTreeNodesChanged(Object source, Object[] path,
      int[] childIndices, Object[] children) {
    Object[] listeners = listenerList.getListeners(TreeModelListener.class);
    TreeModelEvent e = null;

    for (int index = 0; index < listeners.length; ++index) {
      // Lazily create the event:
      if (e == null) {
        e = new TreeModelEvent(source, path, childIndices, children);
      }
      ((TreeModelListener) listeners[index]).treeNodesChanged(e);
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @param source
   *          the node where new elements are being inserted
   * @param path
   *          the path to the root node
   * @param childIndices
   *          the indices of the new elements
   * @param children
   *          the new elements
   * @see EventListenerList
   */
  protected void fireTreeNodesInserted(Object source, Object[] path,
      int[] childIndices, Object[] children) {
    Object[] listeners = listenerList.getListeners(TreeModelListener.class);
    TreeModelEvent e = null;

    for (int index = 0; index < listeners.length; ++index) {
      // Lazily create the event:
      if (e == null) {
        e = new TreeModelEvent(source, path, childIndices, children);
      }
      ((TreeModelListener) listeners[index]).treeNodesInserted(e);
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @param source
   *          the node where elements are being removed
   * @param path
   *          the path to the root node
   * @param childIndices
   *          the indices of the removed elements
   * @param children
   *          the removed elements
   * @see EventListenerList
   */
  protected void fireTreeNodesRemoved(Object source, Object[] path,
      int[] childIndices, Object[] children) {
    Object[] listeners = listenerList.getListeners(TreeModelListener.class);
    TreeModelEvent e = null;

    for (int index = 0; index < listeners.length; ++index) {
      // Lazily create the event:
      if (e == null) {
        e = new TreeModelEvent(source, path, childIndices, children);
      }
      ((TreeModelListener) listeners[index]).treeNodesRemoved(e);
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @param source
   *          the node where the tree model has changed
   * @param path
   *          the path to the root node
   * @param childIndices
   *          the indices of the affected elements
   * @param children
   *          the affected elements
   * @see EventListenerList
   */
  protected void fireTreeStructureChanged(Object source, Object[] path,
      int[] childIndices, Object[] children) {
    Object[] listeners = listenerList.getListeners(TreeModelListener.class);
    TreeModelEvent e = null;

    for (int index = 0; index < listeners.length; ++index) {
      // Lazily create the event:
      if (e == null) {
        e = new TreeModelEvent(source, path, childIndices, children);
      }
      ((TreeModelListener) listeners[index]).treeStructureChanged(e);
    }
  }

  /**
   * Notifies all listeners that have registered interest for notification on
   * this event type. The event instance is lazily created using the parameters
   * passed into the fire method.
   * 
   * @param source
   *          the node where the tree model has changed
   * @param path
   *          the path to the root node
   * @see EventListenerList
   */
  protected void fireTreeStructureChanged(Object source, TreePath path) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListeners(TreeModelListener.class);
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new TreeModelEvent(source, path);
        }
        ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
      }
    }
  }
}
