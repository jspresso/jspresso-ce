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
package org.jspresso.framework.binding.swing;

import java.util.EventListener;

import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * Abstract class for tree models. It mainly manages listeners on the model.
 *
 * @author Vincent Vandenschrick
 */
public abstract class AbstractTreeModel implements TreeModel {

  private final EventListenerList listenerList = new EventListenerList();

  /**
   * Adds a listener for the TreeModelEvent posted after the tree changes.
   *
   * @see #removeTreeModelListener
   * @param l
   *          the listener to add
   */
  @Override
  public void addTreeModelListener(TreeModelListener l) {
    listenerList.add(TreeModelListener.class, l);
  }

  /**
   * Returns an array of all the objects currently registered as
   * {@code <em>Foo</em>Listener}s upon this model.
   * {@code <em>Foo</em>Listener}s are registered using the
   * {@code add<em>Foo</em>Listener} method.
   * <p>
   * You can specify the {@code listenerType} argument with a class
   * literal, such as {@code <em>Foo</em>Listener.class}. For example, you
   * can query a {@code DefaultTreeModel} {@code m} for its tree model
   * listeners with the following code:
   *
   * <pre>
   *
   * TreeModelListener[] tmls = (TreeModelListener[]) (m
   *                              .getListeners(TreeModelListener.class));
   * </pre>
   *
   * If no such listeners exist, this method returns an empty array.
   *
   * @param <T>
   *          The type of EventListener.
   * @param listenerType
   *          the type of listeners requested; this parameter should specify an
   *          interface that descends from {@code java.util.EventListener}
   * @return an array of all objects registered as
   *         {@code <em>Foo</em>Listener}s on this component, or an empty
   *         array if no such listeners have been added
   * @see #getTreeModelListeners
   */
  public <T extends EventListener> T[] getListeners(Class<T> listenerType) {
    return listenerList.getListeners(listenerType);
  }

  /**
   * Returns an array of all the tree model listeners registered on this model.
   *
   * @return all of this model's {@code TreeModelListener}s or an empty
   *         array if no tree model listeners are currently registered
   * @see #addTreeModelListener
   * @see #removeTreeModelListener
   */
  public TreeModelListener[] getTreeModelListeners() {
    return listenerList.getListeners(TreeModelListener.class);
  }

  /**
   * Removes a listener previously added with <B>addTreeModelListener()</B>.
   *
   * @see #addTreeModelListener
   * @param l
   *          the listener to remove
   */
  @Override
  public void removeTreeModelListener(TreeModelListener l) {
    listenerList.remove(TreeModelListener.class, l);
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
      int[] childIndices, Object... children) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new TreeModelEvent(source, path, childIndices, children);
        }
        ((TreeModelListener) listeners[i + 1]).treeNodesChanged(e);
      }
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
      int[] childIndices, Object... children) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new TreeModelEvent(source, path, childIndices, children);
        }
        ((TreeModelListener) listeners[i + 1]).treeNodesInserted(e);
      }
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
      int[] childIndices, Object... children) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new TreeModelEvent(source, path, childIndices, children);
        }
        ((TreeModelListener) listeners[i + 1]).treeNodesRemoved(e);
      }
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
      int[] childIndices, Object... children) {
    // Guaranteed to return a non-null array
    Object[] listeners = listenerList.getListenerList();
    TreeModelEvent e = null;
    // Process the listeners last to first, notifying
    // those that are interested in this event
    for (int i = listeners.length - 2; i >= 0; i -= 2) {
      if (listeners[i] == TreeModelListener.class) {
        // Lazily create the event:
        if (e == null) {
          e = new TreeModelEvent(source, path, childIndices, children);
        }
        ((TreeModelListener) listeners[i + 1]).treeStructureChanged(e);
      }
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
    Object[] listeners = listenerList.getListenerList();
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
