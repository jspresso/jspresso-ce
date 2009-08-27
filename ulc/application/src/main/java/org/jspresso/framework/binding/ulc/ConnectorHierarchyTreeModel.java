/*
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
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
package org.jspresso.framework.binding.ulc;

import java.util.Collection;
import java.util.List;

import org.jspresso.framework.binding.CollectionConnectorValueChangeEvent;
import org.jspresso.framework.binding.ICollectionConnector;
import org.jspresso.framework.binding.ICollectionConnectorListProvider;
import org.jspresso.framework.binding.ICollectionConnectorProvider;
import org.jspresso.framework.binding.ICompositeValueConnector;
import org.jspresso.framework.binding.IValueConnector;
import org.jspresso.framework.util.event.IValueChangeListener;
import org.jspresso.framework.util.event.ValueChangeEvent;

import com.ulcjava.base.application.event.TreeModelEvent;
import com.ulcjava.base.application.event.serializable.ITreeModelListener;
import com.ulcjava.base.application.tree.AbstractTreeModel;
import com.ulcjava.base.application.tree.TreePath;

/**
 * This tree model maps a connector hierarchy.
 * <p>
 * Copyright (c) 2005-2008 Vincent Vandenschrick. All rights reserved.
 * <p>
 * This file is part of the Jspresso framework. Jspresso is free software: you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version. Jspresso is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details. You should have received a copy of the GNU Lesser General Public
 * License along with Jspresso. If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorHierarchyTreeModel extends AbstractTreeModel implements
    ITreeModelListener {

  private static final long        serialVersionUID = -1578891934062045656L;
  private TreeConnectorsListener   connectorsListener;
  private ICompositeValueConnector rootConnector;

  /**
   * Constructs a new <code>ConnectorHierarchyTreeModel</code> instance.
   * 
   * @param rootConnector
   *          the connector being the root node of the tree.
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
  public Object getChild(Object parent, int index) {
    if (parent instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) parent)
          .getCollectionConnector();
      return collectionConnector.getChildConnector(index);
    } else if (parent instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().get(index);
    }
    return null;
  }

  /**
   * {@inheritDoc}
   */
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
    } else if (parent instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().size();
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
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
  public Object getRoot() {
    return rootConnector;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isLeaf(Object node) {
    if (node == rootConnector) {
      return false;
    }
    return getChildCount(node) == 0;
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesChanged(@SuppressWarnings("unused") TreeModelEvent event) {
    // NO-OP as of now.
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesInserted(TreeModelEvent event) {
    for (Object insertedConnector : event.getChildren()) {
      checkListenerRegistrationForConnector((IValueConnector) insertedConnector);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesRemoved(@SuppressWarnings("unused") TreeModelEvent event) {
    // NO-OP as of now.
  }

  /**
   * {@inheritDoc}
   */
  public void treeStructureChanged(TreeModelEvent event) {
    ICollectionConnectorListProvider changedConnector = (ICollectionConnectorListProvider) event
        .getTreePath().getLastPathComponent();
    checkListenerRegistrationForConnector(changedConnector);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void valueForPathChanged(@SuppressWarnings("unused") TreePath path,
      @SuppressWarnings("unused") Object newValue) {
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
    public void valueChange(ValueChangeEvent evt) {
      IValueConnector connector = (IValueConnector) evt.getSource();
      if (connector == rootConnector) {
        nodeStructureChanged(new TreePath(rootConnector));
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
          int changedSize = 0;
          if (newCollectionSize > oldCollectionSize) {
            int[] childIndices = new int[newCollectionSize - oldCollectionSize];
            for (int i = oldCollectionSize; i < newCollectionSize; i++) {
              childIndices[i - oldCollectionSize] = i;
            }
            nodesWereInserted(connectorPath, childIndices);
            changedSize = oldCollectionSize;
          } else if (newCollectionSize < oldCollectionSize) {
            int[] childIndices = new int[oldCollectionSize - newCollectionSize];
            for (int i = newCollectionSize; i < oldCollectionSize; i++) {
              childIndices[i - newCollectionSize] = i;
            }
            List<IValueConnector> removedChildrenConnectors = ((CollectionConnectorValueChangeEvent) evt)
                .getRemovedChildrenConnectors();
            nodesWereRemoved(connectorPath, childIndices,
                removedChildrenConnectors.toArray());
            changedSize = newCollectionSize;
          } else {
            changedSize = newCollectionSize;
          }
          for (int i = 0; i < changedSize; i++) {
            nodeStructureChanged(connectorPath
                .pathByAddingChild(((ICollectionConnector) connector)
                    .getChildConnector(i)));
          }
        }
      } else {
        while (!(connector instanceof ICollectionConnectorListProvider)) {
          connector = connector.getParentConnector();
        }
        TreePath connectorPath = getTreePathForConnector(connector);
        if (connector == rootConnector) {
          // TODO Check ULC bug UBA-920. Root node does not get updated on
          // nodeChanged event.
          // nodeChanged(connectorPath);
          nodeStructureChanged(connectorPath);
        } else if (connector.getConnectorValue() != null) {
          // don't know why but this fixes a tree repaint bug
          // when the root connector is assigned a null value.
          if (connectorPath != null) {
            nodeChanged(connectorPath);
          }
        }
      }
    }
  }
}
