/*
 * Copyright (c) 2005 Design2see. All rights reserved.
 */
package com.d2s.framework.binding.ui.swing;

import java.util.Collection;

import javax.swing.tree.TreePath;

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
public class ConnectorHierarchyTreeModel extends AbstractTreeModel {

  private ICompositeValueConnector rootConnector;
  private TreeConnectorsListener   connectorsListener;

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
    if (parent instanceof ICollectionConnectorProvider) {
      return ((ICollectionConnectorProvider) parent).getCollectionConnector()
          .getChildConnector(index);
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
      if (collectionConnector != null) {
        return collectionConnector.getChildConnectorCount();
      }
    } else if (parent instanceof ICollectionConnectorListProvider) {
      if (((ICollectionConnectorListProvider) parent).getCollectionConnectors() != null) {
        return ((ICollectionConnectorListProvider) parent)
            .getCollectionConnectors().size();
      }
    }
    return 0;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isLeaf(Object node) {
    if (node instanceof ICollectionConnectorProvider) {
      ICollectionConnector collectionConnector = ((ICollectionConnectorProvider) node)
          .getCollectionConnector();
      if (collectionConnector == null) {
        return true;
      }
      return false;
    } else if (node instanceof ICollectionConnectorListProvider) {
      return ((ICollectionConnectorListProvider) node)
          .getCollectionConnectors().isEmpty();
    }
    return true;
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
    ConnectorTreeHelper.checkListenerRegistrationForConnector(connector,
        connectorsListener);
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
          checkListenerRegistrationForConnector(connector);
          if (connector == rootConnector) {
            fireTreeStructureChanged(ConnectorHierarchyTreeModel.this,
                new TreePath(rootConnector));
            return;
          }
          if (connector instanceof ICollectionConnector) {
            if (getTreePathForConnector(connector) != null) {
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
                    getTreePathForConnector(connector).getPath(), childIndices,
                    insertedChildren);
              } else if (newCollectionSize < oldCollectionSize) {
                int[] childIndices = new int[oldCollectionSize
                    - newCollectionSize];
                for (int i = newCollectionSize; i < oldCollectionSize; i++) {
                  childIndices[i - newCollectionSize] = i;
                }
                fireTreeNodesRemoved(ConnectorHierarchyTreeModel.this,
                    getTreePathForConnector(connector).getPath(), childIndices,
                    ((CollectionConnectorValueChangeEvent) evt)
                        .getRemovedChildrenConnectors().toArray());
              }
            }
          } else {
            while (!(connector instanceof ICollectionConnectorListProvider)) {
              connector = connector.getParentConnector();
            }
            if (connector == rootConnector) {
              fireTreeNodesChanged(ConnectorHierarchyTreeModel.this,
                  getTreePathForConnector(connector).getPath(), null, null);
            } else {
              IValueConnector parentConnector = connector.getParentConnector();
              while (parentConnector != null
                  && !(parentConnector instanceof ICollectionConnectorProvider)) {
                parentConnector = parentConnector.getParentConnector();
              }
              if (parentConnector != null) {
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
}
