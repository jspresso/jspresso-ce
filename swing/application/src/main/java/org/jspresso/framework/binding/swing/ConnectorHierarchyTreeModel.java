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

import java.util.Collection;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreePath;

import org.jspresso.framework.binding.CollectionConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;
import org.jspresso.framework.util.swing.SwingUtil;

/**
 * This tree model maps a connector hierarchy.
 *
 * @author Vincent Vandenschrick
 */
public class ConnectorHierarchyTreeModel extends AbstractTreeModel implements
    TreeModelListener {

  private final TreeConnectorsListener   connectorsListener;
  private final ICompositeValueConnector rootConnector;

  /**
   * Constructs a new {@code ConnectorHierarchyTreeModel} instance.
   *
   * @param rootConnector
   *     the connector being the root node of the tree.
   */
  public ConnectorHierarchyTreeModel(ICompositeValueConnector rootConnector) {
    this.rootConnector = rootConnector;
    connectorsListener = new TreeConnectorsListener();
    checkListenerRegistrationForConnector(rootConnector);
    addTreeModelListener(this);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getChild(Object parent, int index) {
    if (parent instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) parent)
          .getCollectionConnector();
      return collectionConnector.getChildConnector(index);
    }
    if (parent instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().get(index);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int getChildCount(Object parent) {
    if (parent instanceof IValueConnector
        && ((IValueConnector) parent).getConnectorValue() == null) {
      return 0;
    }
    if (parent instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) parent)
          .getCollectionConnector();
      if (collectionConnector == null
          || collectionConnector.getConnectorValue() == null) {
        return 0;
      }
      return ((Collection<?>) collectionConnector.getConnectorValue()).size();
    }
    if (parent instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().size();
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  @SuppressWarnings("SuspiciousMethodCalls")
  @Override
  public int getIndexOfChild(Object parent, Object child) {
    if (parent instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) parent)
          .getCollectionConnector();
      int childCount = collectionConnector.getChildConnectorCount();
      for (int i = 0; i < childCount; i++) {
        if (collectionConnector.getChildConnector(i) == child) {
          return i;
        }
      }
    } else if (parent instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().indexOf(child);
    }
    return -1;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getRoot() {
    return rootConnector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLeaf(Object node) {
    if (node == rootConnector) {
      return false;
    }
    return getChildCount(node) == 0;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void treeNodesChanged(TreeModelEvent event) {
    // NO-OP as of now.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void treeNodesInserted(TreeModelEvent event) {
    for (Object insertedConnector : event.getChildren()) {
      checkListenerRegistrationForConnector((IValueConnector) insertedConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void treeNodesRemoved(TreeModelEvent event) {
    // NO-OP as of now.
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void treeStructureChanged(TreeModelEvent event) {
    ICollectionConnectorListProvider changedConnector = (ICollectionConnectorListProvider) event
        .getTreePath().getLastPathComponent();
    checkListenerRegistrationForConnector(changedConnector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void valueForPathChanged(TreePath path, Object newValue) {
    // NO-OP. Not used (yet!)
  }

  private void checkListenerRegistrationForConnector(IValueConnector connector) {
    if (connector != null) {
      // we can add the listener many times since the backing store listener
      // collection is a Set.
      connector.addValueChangeListener(connectorsListener);
      if (connector instanceof ICompositeValueConnector) {
        for (String childConnectorId : ((ICompositeValueConnector) connector)
            .getChildConnectorKeys()) {
          checkListenerRegistrationForConnector(((ICompositeValueConnector) connector)
              .getChildConnector(childConnectorId));
        }
      }
    }
  }

  private TreePath getTreePathForConnector(IValueConnector connector) {
    return ConnectorTreeHelper
        .getTreePathForConnector(rootConnector, connector);
  }

  private class TreeConnectorsListener implements IValueChangeListener {

    /**
     * {@inheritDoc}
     */
    @Override
    public void valueChange(final ValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        @Override
        public void run() {
          IValueConnector connector = (IValueConnector) evt.getSource();
          if (connector == rootConnector) {
            fireTreeStructureChanged(ConnectorHierarchyTreeModel.this,
                new TreePath(rootConnector));
            return;
          }
          if (rootConnector.getConnectorValue() == null) {
            return;
          }
          if (connector instanceof ICollectionConnector
              && connector.getConnectorValue() != null) {
            // don't know why but this fixes a tree repaint bug
            // when the root connector is assigned a null value.
            TreePath connectorPath = getTreePathForConnector(connector);
            if (connectorPath != null) {
              Collection<?> oldCollection = (Collection<?>) evt.getOldValue();
              Collection<?> newCollection = (Collection<?>) evt.getNewValue();
              int oldCollectionSize = 0;
              int newCollectionSize = 0;
              if (oldCollection != null) {
                oldCollectionSize = oldCollection.size();
              }
              if (newCollection != null) {
                newCollectionSize = newCollection.size();
              }
              if (newCollection != null
                  && newCollectionSize > oldCollectionSize) {
                Object[] insertedChildren = new Object[newCollectionSize
                    - oldCollectionSize];
                int[] childIndices = new int[newCollectionSize
                    - oldCollectionSize];
                int i = 0;
                int j = 0;
                for (Object newElem : newCollection) {
                  if (oldCollection == null || !oldCollection.contains(newElem)) {
                    insertedChildren[j] = ((ICollectionConnector) connector)
                        .getChildConnector(i);
                    childIndices[j] = i;
                    j++;
                  }
                  i++;
                }
                fireTreeNodesInserted(ConnectorHierarchyTreeModel.this,
                    connectorPath.getPath(), childIndices, insertedChildren);
              } else if (oldCollection != null
                  && newCollectionSize < oldCollectionSize) {
                int[] childIndices = new int[oldCollectionSize
                    - newCollectionSize];
                int i = 0;
                int j = 0;
                for (Object newElem : oldCollection) {
                  if (newCollection == null || !newCollection.contains(newElem)) {
                    childIndices[j] = i;
                    j++;
                  }
                  i++;
                }
                List<IValueConnector> removedChildrenConnectors = ((CollectionConnectorValueChangeEvent) evt)
                    .getRemovedChildrenConnectors();
                fireTreeNodesRemoved(ConnectorHierarchyTreeModel.this,
                    connectorPath.getPath(), childIndices,
                    removedChildrenConnectors.toArray());
              }
            }
          } else {
            while (!(connector instanceof ICollectionConnectorListProvider)) {
              connector = connector.getParentConnector();
            }
            if (connector == rootConnector) {
              fireTreeNodesChanged(ConnectorHierarchyTreeModel.this,
                  getTreePathForConnector(connector).getPath(), null, (Object[]) null);
            } else if (connector.getConnectorValue() != null) {
              IValueConnector parentConnector = connector.getParentConnector();
              while (parentConnector != null
                  && !(parentConnector instanceof ICollectionConnectorProvider)) {
                parentConnector = parentConnector.getParentConnector();
              }
              if (parentConnector != null
                  && parentConnector.getConnectorValue() != null) {
                // don't know why but this fixes a tree repaint bug
                // when the root connector is assigned a null value.
                TreePath connectorPath = getTreePathForConnector(parentConnector);
                if (connectorPath != null) {
                  fireTreeNodesChanged(ConnectorHierarchyTreeModel.this,
                      getTreePathForConnector(parentConnector).getPath(),
                      new int[]{
                          getIndexOfChild(parentConnector, connector)
                      }, connector);
                }
              }
            }
          }
        }
      });
    }
  }
}
