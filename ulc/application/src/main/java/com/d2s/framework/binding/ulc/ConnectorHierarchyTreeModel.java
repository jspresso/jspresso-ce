/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ulc;

import java.util.Collection;
import java.util.List;

import com.d2s.framework.binding.CollectionConnectorValueChangeEvent;
import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorListProvider;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.ulcjava.base.application.ULCTree;
import com.ulcjava.base.application.event.TreeExpansionEvent;
import com.ulcjava.base.application.event.TreeModelEvent;
import com.ulcjava.base.application.event.serializable.ITreeExpansionListener;
import com.ulcjava.base.application.event.serializable.ITreeModelListener;
import com.ulcjava.base.application.tree.AbstractTreeModel;
import com.ulcjava.base.application.tree.TreePath;

/**
 * This tree model maps a connector hierarchy.
 * <p>
 * Copyright 2005 Design2See. All rights reserved.
 * <p>
 * 
 * @version $LastChangedRevision$
 * @author Vincent Vandenschrick
 */
public class ConnectorHierarchyTreeModel extends AbstractTreeModel implements
    ITreeModelListener, ITreeExpansionListener {

  private static final long        serialVersionUID = -1578891934062045656L;
  private TreeConnectorsListener   connectorsListener;
  private ULCTree                  myTree;
  private ICompositeValueConnector rootConnector;

  /**
   * Constructs a new <code>ConnectorHierarchyTreeModel</code> instance.
   * 
   * @param rootConnector
   *            the connector being the root node of the tree.
   * @param tree
   *            the tree to which this model wiil be attached to. It will be
   *            used for the model to bea notified of expansions so that it can
   *            lazy-load the tree hierarchy.
   */
  public ConnectorHierarchyTreeModel(ICompositeValueConnector rootConnector,
      ULCTree tree) {
    this.rootConnector = rootConnector;
    connectorsListener = new TreeConnectorsListener();
    checkListenerRegistrationForConnector(rootConnector);
    addTreeModelListener(this);
    if (tree != null) {
      // tree mignt be null if this tree model is used as delegate for a
      // treetable.
      tree.addTreeExpansionListener(this);
    }
    myTree = tree;
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
  public void treeCollapsed(@SuppressWarnings("unused")
  TreeExpansionEvent event) {
    // NO-OP.
  }

  /**
   * {@inheritDoc}
   */
  public void treeExpanded(TreeExpansionEvent event) {
    ICollectionConnectorListProvider expandedConnector = (ICollectionConnectorListProvider) event
        .getPath().getLastPathComponent();
    checkListenerRegistrationForConnector(expandedConnector);
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesChanged(@SuppressWarnings("unused")
  TreeModelEvent event) {
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
  public void treeNodesRemoved(@SuppressWarnings("unused")
  TreeModelEvent event) {
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
  public void valueForPathChanged(@SuppressWarnings("unused")
  TreePath path, @SuppressWarnings("unused")
  Object newValue) {
    // NO-OP. Not used (yet!)
  }

  private void checkListenerRegistrationForConnector(IValueConnector connector) {
    if (connector instanceof ICollectionConnectorProvider) {
      checkListenerRegistrationForConnector(connector, 3);
    } else {
      checkListenerRegistrationForConnector(connector, 2);
    }
  }

  private void checkListenerRegistrationForConnector(IValueConnector connector,
      int depth) {
    if (connector != null && depth >= 0) {
      depth--;
      // we can add the listener many times since the backing store listener
      // collection is a Set.
      connector.addConnectorValueChangeListener(connectorsListener);
      if (connector instanceof ICompositeValueConnector) {
        for (String childConnectorId : ((ICompositeValueConnector) connector)
            .getChildConnectorKeys()) {
          checkListenerRegistrationForConnector(
              ((ICompositeValueConnector) connector)
                  .getChildConnector(childConnectorId), depth);
        }
      }
    }
  }

  private TreePath getTreePathForConnector(IValueConnector connector) {
    return ConnectorTreeHelper
        .getTreePathForConnector(rootConnector, connector);
  }

  private class TreeConnectorsListener implements IConnectorValueChangeListener {

    /**
     * {@inheritDoc}
     */
    public void connectorValueChange(ConnectorValueChangeEvent evt) {
      IValueConnector connector = evt.getSource();
      if (connector == rootConnector) {
        nodeStructureChanged(new TreePath(rootConnector));
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
