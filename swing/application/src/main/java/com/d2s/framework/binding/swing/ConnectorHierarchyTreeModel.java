/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.swing;

import java.util.Collection;
import java.util.List;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.TreePath;

import com.d2s.framework.binding.CollectionConnectorHelper;
import com.d2s.framework.binding.CollectionConnectorValueChangeEvent;
import com.d2s.framework.binding.ConnectorValueChangeEvent;
import com.d2s.framework.binding.ICollectionConnector;
import com.d2s.framework.binding.ICollectionConnectorListProvider;
import com.d2s.framework.binding.ICollectionConnectorProvider;
import com.d2s.framework.binding.ICompositeValueConnector;
import com.d2s.framework.binding.IConnectorValueChangeListener;
import com.d2s.framework.binding.IValueConnector;
import com.d2s.framework.util.swing.SwingUtil;

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
    TreeWillExpandListener, TreeModelListener {

  private ICompositeValueConnector rootConnector;
  private TreeConnectorsListener   connectorsListener;

  /**
   * Constructs a new <code>ConnectorHierarchyTreeModel</code> instance.
   * 
   * @param rootConnector
   *          the connector being the root node of the tree.
   * @param tree
   *          the tree to which this model wiil be attached to. It will be used
   *          for the model to bea notified of expansions so that it can
   *          lazy-load the tree hierarchy.
   */
  public ConnectorHierarchyTreeModel(ICompositeValueConnector rootConnector,
      JTree tree) {
    this.rootConnector = rootConnector;
    connectorsListener = new TreeConnectorsListener();
    checkListenerRegistrationForConnector(rootConnector);
    addTreeModelListener(this);
    tree.addTreeWillExpandListener(this);
    //treeWillExpand(new TreeExpansionEvent(tree, new TreePath(rootConnector)));
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
  public Object getChild(Object parent, int index) {
    Object child = null;
    if (parent instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) parent)
          .getCollectionConnector();
      collectionConnector.setAllowLazyChildrenLoading(false);
      child = collectionConnector.getChildConnector(index);
    } else if (parent instanceof ICollectionConnectorListProvider) {
      child = ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().get(index);
    }
    return child;
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
      return ((Collection) collectionConnector.getConnectorValue()).size();
    } else if (parent instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) parent)
          .getCollectionConnectors().size();
    }
    return 0;
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
  public void valueForPathChanged(@SuppressWarnings("unused")
  TreePath path, @SuppressWarnings("unused")
  Object newValue) {
    // NO-OP. Not used (yet!)
  }

  private void checkListenerRegistrationForConnector(IValueConnector connector) {
    if (connector instanceof ICollectionConnectorProvider) {
      checkListenerRegistrationForConnector(connector, 3);
    } else {
      checkListenerRegistrationForConnector(connector, 1);
    }
  }

  private void checkListenerRegistrationForConnector(IValueConnector connector,
      int depth) {
    if (depth >= 0) {
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
    public void connectorValueChange(final ConnectorValueChangeEvent evt) {
      SwingUtil.updateSwingGui(new Runnable() {

        public void run() {
          IValueConnector connector = evt.getSource();
          if (connector == rootConnector) {
            fireTreeStructureChanged(ConnectorHierarchyTreeModel.this,
                new TreePath(rootConnector));
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
              if (newCollectionSize > oldCollectionSize) {
                Object[] insertedChildren = new Object[newCollectionSize
                    - oldCollectionSize];
                int[] childIndices = new int[newCollectionSize
                    - oldCollectionSize];
                for (int i = oldCollectionSize; i < newCollectionSize; i++) {
                  insertedChildren[i - oldCollectionSize] = ((ICollectionConnector) connector)
                      .getChildConnector(i);
                  childIndices[i - oldCollectionSize] = i;
                }
                fireTreeNodesInserted(ConnectorHierarchyTreeModel.this,
                    connectorPath.getPath(), childIndices, insertedChildren);
              } else if (newCollectionSize < oldCollectionSize) {
                int[] childIndices = new int[oldCollectionSize
                    - newCollectionSize];
                for (int i = newCollectionSize; i < oldCollectionSize; i++) {
                  childIndices[i - newCollectionSize] = i;
                }
                if (connectorPath != null) {
                  List<IValueConnector> removedChildrenConnectors = ((CollectionConnectorValueChangeEvent) evt)
                      .getRemovedChildrenConnectors();
                  fireTreeNodesRemoved(ConnectorHierarchyTreeModel.this,
                      connectorPath.getPath(), childIndices,
                      removedChildrenConnectors.toArray());
                }
              }
            }
          } else {
            while (!(connector instanceof ICollectionConnectorListProvider)) {
              connector = connector.getParentConnector();
            }
            if (connector == rootConnector) {
              fireTreeNodesChanged(ConnectorHierarchyTreeModel.this,
                  getTreePathForConnector(connector).getPath(), null, null);
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
                      new int[] {getIndexOfChild(parentConnector, connector)},
                      new Object[] {connector});
                }
              }
            }
          }
        }
      });
    }
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesChanged(@SuppressWarnings("unused")
  TreeModelEvent e) {
    // NO-OP as of now.
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesInserted(TreeModelEvent e) {
    checkListenerRegistrationForConnector((IValueConnector) e.getTreePath()
        .getLastPathComponent());
  }

  /**
   * {@inheritDoc}
   */
  public void treeNodesRemoved(@SuppressWarnings("unused")
  TreeModelEvent e) {
    // NO-OP as of now.
  }

  /**
   * {@inheritDoc}
   */
  public void treeStructureChanged(TreeModelEvent e) {
    ICollectionConnectorListProvider changedConnector = (ICollectionConnectorListProvider) e
        .getTreePath().getLastPathComponent();
    if (changedConnector == rootConnector) {
      for (ICollectionConnector collectionConnector : ((ICollectionConnectorListProvider) rootConnector)
          .getCollectionConnectors()) {
        CollectionConnectorHelper.setAllowLazyChildrenLoadingForConnector(
            collectionConnector, true, true);
      }
      CollectionConnectorHelper.setAllowLazyChildrenLoadingForConnector(
          changedConnector, false, false);
    } else {
      CollectionConnectorHelper.setAllowLazyChildrenLoadingForConnector(
          changedConnector, true, true);
    }
    checkListenerRegistrationForConnector(changedConnector);
  }

  /**
   * {@inheritDoc}
   */
  public void treeWillCollapse(TreeExpansionEvent event) {
    ICollectionConnectorListProvider collapsedConnector = (ICollectionConnectorListProvider) event
        .getPath().getLastPathComponent();
    CollectionConnectorHelper.setAllowLazyChildrenLoadingForConnector(
        collapsedConnector, true, false);
  }

  /**
   * {@inheritDoc}
   */
  public void treeWillExpand(TreeExpansionEvent event) {
    ICollectionConnectorListProvider expandedConnector = (ICollectionConnectorListProvider) event
        .getPath().getLastPathComponent();
    CollectionConnectorHelper.setAllowLazyChildrenLoadingForConnector(
        expandedConnector, false, false);
    checkListenerRegistrationForConnector((IValueConnector) event.getPath()
        .getLastPathComponent());
  }
}
